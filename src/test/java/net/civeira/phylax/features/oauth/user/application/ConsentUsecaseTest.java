package net.civeira.phylax.features.oauth.user.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.features.oauth.user.domain.PendingConsent;
import net.civeira.phylax.features.oauth.user.domain.gateway.ConsentGateway;

class ConsentUsecaseTest {

  private ConsentGateway gateway;
  private ConsentUsecase usecase;

  @BeforeEach
  void setUp() {
    gateway = mock(ConsentGateway.class);
    usecase = new ConsentUsecase(gateway);
  }

  // T2.2.1
  @Test
  void getPendingConsent_returnsPendingConsentFromGateway() {
    PendingConsent pendingConsent = mock(PendingConsent.class);
    when(gateway.getPendingConsent("test-tenant", "alice@example.com", List.of("rp-1"),
        Locale.ENGLISH)).thenReturn(Optional.of(pendingConsent));

    Optional<PendingConsent> result = usecase.getPendingConsent("test-tenant", "alice@example.com",
        List.of("rp-1"), Locale.ENGLISH);

    assertTrue(result.isPresent());
    assertTrue(result.get() == pendingConsent);
  }

  // T2.2.2
  @Test
  void getPendingConsent_returnsEmptyWhenGatewayReturnsEmpty() {
    when(gateway.getPendingConsent("test-tenant", "alice@example.com", List.of("rp-1"),
        Locale.ENGLISH)).thenReturn(Optional.empty());

    Optional<PendingConsent> result = usecase.getPendingConsent("test-tenant", "alice@example.com",
        List.of("rp-1"), Locale.ENGLISH);

    assertFalse(result.isPresent());
  }

  // T2.2.3
  @Test
  void storeAcceptedConsent_delegatesToGateway() {
    usecase.storeAcceptedConsent("test-tenant", "alice@example.com", "rp-1");

    verify(gateway).storeAcceptedConsent("test-tenant", "alice@example.com", "rp-1");
  }
}
