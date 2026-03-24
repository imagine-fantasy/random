# Instructions for CCAPI TOME JAR — Partial Success Handling

## Context
You are working on the `ccapi-tome-client` JAR which wraps TOME SDK calls for dual namespace tokenization. The JAR handles two namespaces:
- **NS1:** ComCardCCInt (PCI, internal tokens)
- **NS2:** NpComCardCCExt (NonPCI, external tokens)

We had a previous discussion where exception categories were defined — permanent and retryable. TOME SDK handles retryable internally. Permanent exceptions surface to the JAR.

---

## Current Problem
Currently if any operation fails in NS1 or NS2 the entire request fails. This is too aggressive. We need partial success handling across both namespaces with a consolidated error list.

---

## New Error Object
Introduce the following error class:

```java
public class CCAPITomeError {
    private String errorKey;      // derived from TOME error message — see mapping below
    private String namespace;     // "NS1" or "NS2"
    private String correlationId; // which record failed
}
```

**Note:** No retryable flag needed — TOME SDK already retries retryable errors before surfacing to JAR. By the time an error reaches this object it is permanently failed.

---

## ErrorKey Mapping
TOME SDK does not return an errorKey directly. You need to derive it from the TOME error message. Please check with the firm LLM or TOME SDK documentation for the known error messages and map them to the following categories at minimum:

- `NOT_FOUND` — record not found in namespace
- `CONFIG_ERROR` — namespace configuration issue
- `VALIDATION_ERROR` — input validation failure
- `UNKNOWN_ERROR` — fallback for unmapped errors

---

## Method Signature Change
Do NOT create a new wrapper class or rename the existing result class — this will cause breaking changes for consumers.

Simply add the `errors` field to the **existing** result class:

```java
// In your EXISTING result class — just add this field
private List<CCAPITomeError> errors; // consolidated across NS1 and NS2
```

Method signature does not change. Return type stays the same existing class.

---

## Partial Success Logic

### For each item in the batch:

```
NS1 operation →
    if success → proceed to NS2
    if NOT_FOUND (permanent) → 
        for lookup+tokenize endpoint: tokenize instead
        add to error tracking if tokenize also fails
    if other permanent error → 
        add CCAPITomeError for this item (namespace=NS1)
        skip NS2 for this item

NS2 operation (only for items that succeeded NS1) →
    if success → add to success list
    if any permanent error →
        add CCAPITomeError for this item (namespace=NS2)
```

### Final result:
- `internalTokens` — items that succeeded NS1
- `externalTokens` — items that succeeded both NS1 and NS2
- `errors` — consolidated list of all failures across both namespaces

---

## Endpoints to Update
Apply this logic to the following endpoints:
1. **Lookup + Tokenize** — if NS1 lookup returns NOT_FOUND, catch exception and tokenize instead. All other permanent exceptions fail that item.
2. **Tokenize** — standard partial success handling
3. **Detokenize** — standard partial success handling

---

## Example Scenario
24 items submitted:
- 18 succeed NS1 → proceed to NS2
- 6 fail NS1 → added to error list with namespace=NS1
- 15 succeed NS2 out of 18
- 3 fail NS2 → added to error list with namespace=NS2

Result:
- `internalTokens` = 18 items
- `externalTokens` = 15 items  
- `errors` = 9 items (6 NS1 failures + 3 NS2 failures)

---

## What NOT to Change
- Exception categorisation (permanent vs retryable) — already defined, keep as is
- TOME SDK retry configuration — already handled, do not change
- Existing request/response model for consumers — only the return type of `process()` changes
