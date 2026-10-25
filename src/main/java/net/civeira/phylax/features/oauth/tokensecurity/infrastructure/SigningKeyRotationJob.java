package net.civeira.phylax.features.oauth.tokensecurity.infrastructure;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.common.infrastructure.migration.Migrations;
import net.civeira.phylax.features.oauth.tokensecurity.domain.gateway.TokenSigner;

/**
 * Generates signing keys outside the requests (task 2.6).
 *
 * <p>
 * Runs once on startup, after the schema migrations, so the first token can be signed, and then
 * periodically ({@code oauth.jwt.authorization.rotation-cron}, every 15 minutes by default) to keep
 * {@code key-futures} keys ahead. Signing and the JWKS only read the stored keys. Instances that
 * rotate at the same time may both add keys: the extra ones are published and expire as usual.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SigningKeyRotationJob {

  private final TokenSigner signer;

  void onStart(@Observes @Priority(Migrations.POST_MIGRATION_PHASE_PRIORITY) StartupEvent event) {
    rotate();
  }

  /**
   * Tops up the key ring.
   */
  @Scheduled(cron = "${oauth.jwt.authorization.rotation-cron:0 */15 * * * ?}",
      concurrentExecution = ConcurrentExecution.SKIP)
  public void rotate() {
    try {
      signer.rotateKeys();
    } catch (RuntimeException ex) {
      // The current keys stay valid for a while: retry on the next run.
      log.error("Signing key rotation failed: {}", ex.getMessage(), ex);
    }
  }
}
