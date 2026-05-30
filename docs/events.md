# Event Strategy — Reactive Updates via TicketEventBus

## Why events instead of polling

The ticket list and ticket detail run in separate back-stack entries.
When the user creates a ticket or changes a priority in the detail screen,
the list ViewModel is not in the composition — it cannot observe the detail
ViewModel directly without coupling the two layers.

The solution is a lightweight, app-scoped event bus backed by `SharedFlow`.

---

## TicketEventBus

```kotlin
object TicketEventBus {
    private val _events = MutableSharedFlow<TicketEvent>(extraBufferCapacity = 8)
    val events: SharedFlow<TicketEvent> = _events.asSharedFlow()

    suspend fun emit(event: TicketEvent) { _events.emit(event) }
}
```

`extraBufferCapacity = 8` means events are never dropped if the collector
is momentarily busy. `SharedFlow` (not `StateFlow`) is used because events
are one-shot signals, not persistent state.

---

## Sealed event type

```kotlin
sealed class TicketEvent {
    data class Created(val ticketId: Int)       : TicketEvent()
    data class StatusChanged(val ticketId: Int) : TicketEvent()
    data class PriorityChanged(val ticketId: Int): TicketEvent()
}
```

Adding a new event type in the future (e.g. `Deleted`, `Assigned`) requires:
1. One new `data class` inside the sealed class.
2. One new `emit(TicketEvent.Xxx(…))` call at the source.
3. `TicketsViewModel.observeEvents()` already calls `fetchTickets()` on **any**
   event, so no change is needed there unless finer-grained behaviour is needed.

---

## Scenario 1 — Create ticket → list updates immediately

```
CreateTicketViewModel.submit()
  → repository.createTicket(request)   // mock returns new Ticket
  → TicketEventBus.emit(Created(id))
  → _created.emit(successMessage)      // triggers navigation pop

TicketsViewModel.observeEvents()       // collecting in viewModelScope
  → receives Created event
  → calls fetchTickets()
  → list reloads and the new ticket appears sorted by priority
```

The user sees the new ticket the moment the list screen is back in focus,
without any manual refresh.

---

## Scenario 2 — Change priority → list reorders

```
TicketDetailViewModel.updatePriority(priority)
  → repository.updatePriority(id, priority.name)
  → TicketEventBus.emit(PriorityChanged(id))

TicketsViewModel.observeEvents()
  → receives PriorityChanged event
  → calls fetchTickets()
  → list reloads with sortedByPriority():
      compareByDescending { it.priority.weight }
      .thenByDescending  { it.createdAt }
```

Priority weights are defined on the enum:

| Priority | Weight |
|---|---|
| CRITICAL | 4 |
| HIGH | 3 |
| MEDIUM | 2 |
| LOW | 1 |

Higher-weight tickets float to the top of the list automatically.

---

## Flow diagram

```
[CreateTicketScreen]          [TicketDetailScreen]
        |                             |
  submit()                   updatePriority()
        |                             |
        └──────┬──────────────────────┘
               ↓
       TicketEventBus.emit(event)
               ↓
       TicketsViewModel.observeEvents()
               ↓
         fetchTickets()
               ↓
       _uiState updated → TicketsScreen recomposed
```

---

## Evolving this strategy

For a future multi-module or multi-screen product:
- Replace `object TicketEventBus` with an interface injected via Hilt.
- Add granular collectors in other ViewModels (e.g. a dashboard) without
  changing the emitters.
- If WebSocket or push notifications are added, emit the same `TicketEvent`
  types from a background service — the UI layer does not need to change.
