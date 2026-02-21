package net.civeira.phylax.testing.oauth.alt;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.session.domain.gateway.TemporalKeysGateway;
import net.civeira.phylax.features.oauth.session.domain.model.TemporalAuthCode;

@Alternative
@Priority(1)
@ApplicationScoped
public class FixedTemporalKeysGateway implements TemporalKeysGateway {
  private static final String FIXED_KEY = "0123456789abcdef0123456789abcdef";
  private final Map<String, TemporalAuthCode> codes = new ConcurrentHashMap<>();

  @Override
  public String currentKey() {
    return FIXED_KEY;
  }

  @Override
  public String encrypt(String value) {
    return value;
  }

  @Override
  public Optional<String> verifyCypher(String value) {
    return Optional.ofNullable(value);
  }

  @Override
  public Optional<String> verifyToken(String token) {
    return token == null || token.isBlank() ? Optional.empty() : Optional.of("verified");
  }

  @Override
  public String registerTemporalAuthCode(TemporalAuthCode code) {
    String uid = UUID.randomUUID().toString();
    codes.put(uid, code);
    return uid;
  }

  @Override
  public Optional<TemporalAuthCode> retrieveTemporalAuthCode(String code) {
    return Optional.ofNullable(codes.remove(code));
  }

  public void clear() {
    codes.clear();
  }
}
