# Instructions for Card API — Partial Success Handling for TOME Integration

## Context
Card API consumes the `ccapi-tome-client` JAR for dual namespace tokenization. The JAR has been updated to return partial success results via a new `TomeResult` wrapper. Card API needs to handle this updated response.

---

## What Changed in JAR
JAR methods now return `TomeResult` instead of direct response:

```java
public class TomeResult {
    private List<InternalToken> internalTokens;
    private List<ExternalToken> externalTokens;
    private List<CCAPITomeError> errors;
}

public class CCAPITomeError {
    private String errorKey;      // e.g. NOT_FOUND, CONFIG_ERROR
    private String namespace;     // NS1 or NS2
    private String correlationId; // which record failed
}
```

---

## Endpoints to Update

### 1. Lookup + Tokenize Endpoint
This endpoint looks up existing token — if not found, tokenizes instead.

```java
TomeResult result = jar.lookupAndTokenize(request);

// Items that fully succeeded both namespaces
List<ExternalToken> successful = result.getExternalTokens();

// Items that failed
List<CCAPITomeError> failed = result.getErrors();

// Handle NOT_FOUND specifically — JAR already handles tokenize fallback
// If errorKey = NOT_FOUND and it still failed after tokenize attempt
// treat as a hard failure for that record
```

### 2. Tokenize Endpoint
```java
TomeResult result = jar.tokenize(request);

List<ExternalToken> successful = result.getExternalTokens();
List<CCAPITomeError> failed = result.getErrors();

// Process successful items
// Log and handle failed items based on errorKey
```

### 3. Detokenize Endpoint
```java
TomeResult result = jar.detokenize(request);

List<InternalToken> successful = result.getInternalTokens();
List<CCAPITomeError> failed = result.getErrors();
```

---

## Response Handling Pattern

For all endpoints, Card API should:

1. Read `externalTokens` (or `internalTokens` for detokenize) — these fully succeeded
2. Read `errors` — log each failure with errorKey, namespace, correlationId
3. Return partial success response to caller if applicable
4. Do NOT retry — JAR has already retried retryable errors

---

## Error Handling by ErrorKey

```java
for (CCAPITomeError error : result.getErrors()) {
    switch (error.getErrorKey()) {
        case "NOT_FOUND":
            // expected business case — log as info not error
            break;
        case "CONFIG_ERROR":
            // serious — log as error, may need investigation
            break;
        case "VALIDATION_ERROR":
            // bad input — log as warn
            break;
        default:
            // log as error
            break;
    }
}
```

---

## What NOT to Change
- Request model to JAR — only response handling changes
- Existing business logic around card creation flow
- Prefetch reservoir interaction — separate from JAR error handling
