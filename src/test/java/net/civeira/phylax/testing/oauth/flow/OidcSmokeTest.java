package net.civeira.phylax.testing.oauth.flow;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Smoke test: verifica que el servidor Quarkus arranca correctamente con los beans
 * 
 * @Alternative del classpath de test. Sirve como punto de entrada mientras se construye la
 *              infraestructura de tests de integración OIDC.
 */
@Tag("oidc-flow")
@QuarkusTest
class OidcSmokeTest {

  @Test
  void applicationStarts() {
    // Si este test pasa, Quarkus ha arrancado con CDI completo y todos los beans
    // @Alternative han sido resueltos sin ambigüedad.
  }
}
