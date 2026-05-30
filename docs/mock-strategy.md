# Mock Strategy

## Interface-based substitution

`TicketApiService` is a Retrofit interface — the standard contract for HTTP
operations. `FakeTicketApiService` implements the exact same interface and
returns `Response.success(data)` with realistic in-memory data.

`AppContainer` wires exactly one implementation:

```kotlin
private val ticketApiService: TicketApiService by lazy {
    FakeTicketApiService()
}
```

`TicketRepository` depends on the **interface**, not the concrete class.
This is the only line that names `FakeTicketApiService`.

---

## Migrating to a real backend

Replace the single line above with a Retrofit builder call:

```kotlin
private val ticketApiService: TicketApiService by lazy {
    Retrofit.Builder()
        .baseUrl("https://api.panini.internal/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TicketApiService::class.java)
}
```

Zero other files change. Repository, ViewModels, and UI are all unaffected.

---

## Mock data quality

The fake data represents real-world Panini operational incidents:
- sticker shortage in a regional distribution centre
- defective packaging from a supplier
- shipment delay at customs
- inventory count discrepancy in a warehouse
- label printing error affecting a product batch

This allows the product owner and the evaluating professor to validate
the UX flows in a context that matches the actual use case.
