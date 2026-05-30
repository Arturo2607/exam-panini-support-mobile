# Feature Flags

## What they are

Feature flags are Boolean constants that enable or disable individual
UI entry points without touching business logic.
For this PoC the flags are in-memory constants in `core/FeatureFlags.kt`.

---

## Flags defined

| Flag | Default | Controls |
|---|---|---|
| `ENABLE_TICKET_CREATION` | `true` | Visibility of the FAB on the ticket list. When `false`, users cannot navigate to the Create Ticket screen. |
| `ENABLE_PRIORITY_CHANGE` | `true` | Visibility of the "Change Priority" button on the ticket detail screen. When `false`, the priority of an existing ticket cannot be changed from the app. |

---

## How to toggle a flag

Open `app/src/main/java/com/panini/tickets/core/FeatureFlags.kt` and change
the constant value:

```kotlin
const val ENABLE_TICKET_CREATION: Boolean = false  // hides FAB
const val ENABLE_PRIORITY_CHANGE: Boolean = false  // hides priority button
```

Rebuild and run. No other files need to change.

---

## Where the flags are applied

`ENABLE_TICKET_CREATION` — `navigation/AppNavHost.kt`
```kotlin
if (FeatureFlags.ENABLE_TICKET_CREATION) {
    FloatingActionButton(onClick = { … }) { … }
}
```

`ENABLE_PRIORITY_CHANGE` — `ui/detail/TicketDetailScreen.kt`
```kotlin
if (FeatureFlags.ENABLE_PRIORITY_CHANGE) {
    OutlinedButton(onClick = { showPriorityDialog = true }) { … }
}
```

The underlying ViewModel methods (`submit()`, `updatePriority()`) are not
gated by flags — only the UI entry point is hidden. This is intentional:
business logic must not depend on a presentation-layer toggle.

---

## Why in-memory constants

The exam context is a short-term PoC with one developer and no deployment
pipeline. Remote config services (Firebase Remote Config, LaunchDarkly) or
DataStore-backed flags would add dependencies, async loading, and UI state
complexity that is not justified at this stage.

**This was an explicit anti-over-engineering decision.**

---

## Evolving the flags

When the product moves to a real release cycle, the migration path is:

1. Define an interface:
```kotlin
interface FeatureFlagSource {
    val enableTicketCreation: Boolean
    val enablePriorityChange: Boolean
}
```

2. Provide a `RemoteFeatureFlagSource` backed by Firebase Remote Config and
   inject it via Hilt into `AppNavHost` and `TicketDetailScreen`.

3. The call sites (`if (flags.enableTicketCreation)`) do not change.
