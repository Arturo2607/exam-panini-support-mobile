# Architecture — Panini Support PoC

## Pattern: MVVM with layered separation

The project follows MVVM (Model–View–ViewModel) with three explicit layers.
The choice was deliberate for a short-term PoC: it is the Android-recommended
pattern, all team members will recognise it, and it keeps each file focused on
one responsibility without requiring a build-system split.

```
ui/          ← Compose screens + ViewModels  (presentation)
data/        ← Repository + DTOs + ApiService (data access)
domain/      ← Plain Kotlin models            (business types)
core/        ← Shared utilities: events, messages, feature flags
di/          ← AppContainer (manual DI)
navigation/  ← NavHost + route constants
```

### Layer responsibilities

| Layer | What it owns | What it must NOT know about |
|---|---|---|
| `ui` | Compose state, user events, snackbar feedback | HTTP, DTOs |
| `data` | Retrofit calls, DTO ↔ domain mapping, error wrapping | Compose, ViewModel |
| `domain` | Pure Kotlin data classes + enums | Android framework |
| `core` | Cross-cutting: event bus, user-facing strings, feature flags | Any specific layer |

---

## Dependency injection — AppContainer

A plain Kotlin `object` with `by lazy` properties.
No Hilt, no Dagger — they are not justified for a single-module PoC
with one team member and no testing infrastructure yet.

Migrating to Hilt in a future phase requires:
1. Adding the Hilt plugin + annotations to `AppContainer` fields.
2. Replacing `viewModel(factory = …)` calls with the standard `hiltViewModel()`.
3. No logic changes.

---

## Networking layer — prepared for real backend

`TicketApiService` is a Retrofit interface.
`FakeTicketApiService` implements the same interface returning in-memory data.
`AppContainer` wires one implementation.

**To switch from mock to a real backend:**
```kotlin
// AppContainer.kt — change ONE line
private val ticketApiService: TicketApiService by lazy {
    Retrofit.Builder()
        .baseUrl("https://api.panini.internal/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build())
        .build()
        .create(TicketApiService::class.java)
}
```

No other file changes are needed.

---

## State handling

Every screen that loads data from the repository exposes a sealed-like `UiState`
data class with three fields:

| Field | Meaning |
|---|---|
| `isLoading: Boolean` | spinner visible |
| `data: T?` | populated on success |
| `error: String?` | user-facing message on failure |

The UI renders exactly one branch per state combination:
`isLoading && data == null` → shimmer/loading text
`error != null && data == null` → error + retry button
`data != null` → content (loading overlay optional)

---

## Anti-over-engineering decisions

| Decision | Reason |
|---|---|
| `LoginScreen` has no ViewModel | Login is simulated; there is no async call or complex state to manage. A ViewModel would be empty. |
| `AppContainer` is a plain object | Manual DI is sufficient for one module and one developer. |
| `TicketEventBus` uses a flat SharedFlow | The event set is small and well-known. A full Rx or MVI framework would obscure the logic without adding value. |
| No desugaring / `coreLibraryDesugaring` | `SimpleDateFormat` is used for date formatting; `minSdk 26` covers all required Java time APIs without desugaring. |
