# States and Networking Layer

## UI states

Every screen that performs an async operation uses a `UiState` data class
with three fields that map to three exclusive UI branches:

```
isLoading = true,  data = null   → Loading indicator
error     ≠ null,  data = null   → Error message + retry button
data      ≠ null                 → Content (list or detail)
```

This covers all observable states without an explicit sealed class, which
would require more boilerplate for a PoC with simple state transitions.

---

## ApiResult wrapper

```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val statusCode: Int? = null) : ApiResult<Nothing>()
}
```

`TicketRepository` wraps every call in a `try/catch` and maps HTTP errors
via `ApiErrorHandler.fromHttpCode(code)`. ViewModels only see `ApiResult`
and never catch exceptions themselves.

---

## DTOs

DTOs live in `data/remote/dto/` and are plain data classes annotated for
Gson. They are mapped to domain models via `toDomain()` extension functions.
Domain models (`Ticket`, `TicketPriority`, etc.) have no Gson or Retrofit
annotations — they stay clean of framework dependencies.

```
TicketDto       → toDomain() → Ticket
CreateTicketRequest           (outbound)
UpdateTicketStatusRequest     (outbound)
UpdateTicketPriorityRequest   (outbound)
```

---

## OkHttp logging interceptor

`HttpLoggingInterceptor` is included as a dependency and ready to attach
to the `OkHttpClient` when the real backend is integrated. It is not
attached in the mock setup because there are no real HTTP calls to log.

---

## API contract location

All endpoint contracts are defined in `/docs/tickets-api.yaml`.
They describe exactly the requests and responses the `TicketApiService`
interface and DTOs implement.
