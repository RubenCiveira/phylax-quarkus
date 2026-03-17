# ADR-001 — Sequential consent per relying party

**Status**: Accepted
**Date**: 2026-02

---

## Context

When a client application requests a token with multiple `audience` values (additional RPs beyond
the primary `client_id`), each relying party may define its own terms of use that the user must
accept before accessing it.

The `ConsentGateway.getPendingConsent()` port must return which RP (if any) still requires the
user's consent, and `storeAcceptedConsent()` must record the acceptance against the specific RP.

---

## Decision

Consent is handled **sequentially, one relying party at a time**:

1. After credentials are accepted, `revolve()` calls `getPendingConsent(tenant, user, audiences, locale)`.
2. The gateway returns the **first** pending RP wrapped in `PendingConsent(relyingParty, consentText)`.
3. `ConsentControllerPart` renders a form with the RP's consent text and a hidden `relying_party`
   field carrying the RP identifier.
4. On POST, `storeAcceptedConsent(tenant, user, relyingParty)` records the acceptance for that RP.
5. `revolve()` re-evaluates — if another RP is still pending, the form is shown again for the
   next one. The loop continues until `getPendingConsent()` returns `Optional.empty()`.

---

## Alternatives considered

| Option | Reason rejected |
|--------|----------------|
| Show all RPs in a single form with one checkbox each | More complex UI; the gateway interface becomes harder to implement since it must return a list of texts and identifiers simultaneously. The sequential model is simpler and better fits per-RP audit trails. |
| Accept consent for all audiences at once (no per-RP tracking) | Loses granularity — a user who revokes consent for one RP would lose it for all. |

---

## Consequences

- `ConsentGateway` is typed to return `Optional<PendingConsent>` (not a list). The gateway
  implementation is responsible for deciding the order in which pending RPs are returned.
- The `revolve()` loop handles multiple RPs transparently — `ConsentControllerPart` has no
  knowledge of how many RPs remain.
- `UserConsentInteractor` currently delegates to `PendingConsentUsecase`, which does not yet
  have full per-RP granularity in the persistence layer. It uses the first audience as the
  `relyingParty` value. Full per-RP persistence is a follow-up task.
