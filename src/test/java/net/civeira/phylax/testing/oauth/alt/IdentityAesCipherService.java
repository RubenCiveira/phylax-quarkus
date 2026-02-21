package net.civeira.phylax.testing.oauth.alt;

import java.util.Optional;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.common.crypto.AesCipherService;

@Alternative
@Priority(1)
@ApplicationScoped
public class IdentityAesCipherService extends AesCipherService {
  public IdentityAesCipherService() {
    super(ConfigProvider.getConfig().getValue("app.security.encryption.key", String.class));
  }

  public IdentityAesCipherService(
      @ConfigProperty(name = "app.security.encryption.key") String cipherKey) {
    super(cipherKey);
  }

  @Override
  public Optional<String> decrypt(String cipherText, String password) {
    return Optional.ofNullable(cipherText);
  }

  @Override
  public String encrypt(String value, String password) {
    return value;
  }

  @Override
  public Optional<String> decryptForAll(String cText) {
    return Optional.ofNullable(cText);
  }

  @Override
  public String encryptForAll(String value) {
    return value;
  }
}
