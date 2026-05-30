# Panini Support Tickets — PoC Android App

Internal support ticket system for Panini's FIFA World Cup 2026 album
distribution operations. Built as a proof of concept for centralising
incident management across suppliers, logistics and warehouse teams.

---

## Context

Panini's current workflow (email threads, spreadsheets, informal messages)
causes lost follow-ups, duplicated requests and resolution delays.
This app provides a mobile-first, structured alternative that is fast to
implement and easy to hand off to other engineers.

---

## Technologies

| Technology | Purpose |
|---|---|
| Kotlin | Primary language |
| Jetpack Compose | Declarative UI |
| MVVM | Presentation pattern |
| Navigation Compose | Screen navigation |
| Retrofit + OkHttp | Networking layer (wired to mock for PoC) |
| Gson | JSON serialisation / deserialisation |
| Kotlin Coroutines + Flow | Async operations and reactive state |
| SharedFlow (TicketEventBus) | Decoupled event propagation between screens |
| Manual DI (AppContainer) | Dependency wiring without Hilt overhead |

---

## Running the project

1. Clone the repository.
2. Open the `/app` folder in **Android Studio Hedgehog (2023.1.1) or later**.
3. Wait for Gradle sync to complete.
4. Select an emulator or connected device running **Android 8.0 (API 26) or higher**.
5. Press **Run ▶**.

No backend, API keys or environment variables are required.
All data is served from `FakeTicketApiService` in-memory.

---

## Repository structure

```
/app          Android project (Kotlin, Jetpack Compose)
/contracts    API contracts in OpenAPI 3.0 YAML format
/docs         Technical documentation for the engineering team
/video        Link to the demo video (video-link.md)
README.md     This file
```

---

## Considerations

- **No real backend.** All data is mocked via `FakeTicketApiService`.
  To connect to a real API, replace one line in `AppContainer.kt` (see `docs/mock-strategy.md`).
- **Simulated authentication.** Login accepts any non-empty email + password
  and returns a fixed `userId = 1`. No token, session or auth library is used.
- **Feature flags** are compile-time Boolean constants in `core/FeatureFlags.kt`.
  Set `ENABLE_TICKET_CREATION = false` or `ENABLE_PRIORITY_CHANGE = false` and
  rebuild to disable those entry points globally.
- **Event bus** (`TicketEventBus`) keeps the ticket list reactive: creating a
  ticket or changing a priority automatically refreshes the list without manual
  navigation or polling.

---

## Documentation index

| Document | Contents |
|---|---|
| `docs/architecture.md` | MVVM layers, DI strategy, anti-over-engineering decisions |
| `docs/events.md` | TicketEventBus design, scenario walkthroughs, evolution path |
| `docs/feature-flags.md` | Flag definitions, toggle instructions, migration to remote config |
| `docs/mock-strategy.md` | Interface substitution pattern, migration to real backend |
| `docs/states-and-networking.md` | UI state model, ApiResult wrapper, DTO design |
| `contracts/tickets-api.yaml` | Full OpenAPI 3.0 contract for all endpoints |
