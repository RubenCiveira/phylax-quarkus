package net.civeira.phylax.features.oauth.mfa.application;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.user.domain.UserReference;
import net.civeira.phylax.features.access.usermfarecoverycode.domain.UserMfaRecoveryCode;
import net.civeira.phylax.features.access.usermfarecoverycode.domain.UserMfaRecoveryCodeChangeSet;
import net.civeira.phylax.features.access.usermfarecoverycode.domain.gateway.UserMfaRecoveryCodeFilter;
import net.civeira.phylax.features.access.usermfarecoverycode.domain.gateway.UserMfaRecoveryCodeReadRepositoryGateway;
import net.civeira.phylax.features.access.usermfarecoverycode.domain.gateway.UserMfaRecoveryCodeWriteRepositoryGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class MfaRecoveryService {

  private static final String ALPHABET = "BCDFGHJKLMNPQRSTVWXZ";
  private static final SecureRandom RANDOM = new SecureRandom();

  private final UserMfaRecoveryCodeReadRepositoryGateway readGateway;
  private final UserMfaRecoveryCodeWriteRepositoryGateway writeGateway;

  /** Generates {@code count} single-use codes, replacing any existing ones for the user. */
  public List<String> generateCodes(String userUid, int count) {
    readGateway.list(UserMfaRecoveryCodeFilter.builder().user(UserReference.of(userUid)).build())
        .forEach(code -> writeGateway.delete(code.delete()));

    List<String> rawCodes = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      String raw = generateRawCode();
      UserMfaRecoveryCode entity = UserMfaRecoveryCode.create(new UserMfaRecoveryCodeChangeSet()
          .newUid().user(UserReference.of(userUid)).codeHash(sha256hex(raw)));
      writeGateway.create(entity);
      rawCodes.add(raw);
    }
    return rawCodes;
  }

  /** Returns true and marks the code used if it matches an unused code; false otherwise. */
  public boolean consumeCode(String userUid, String rawCode) {
    String hash = sha256hex(rawCode.trim());
    return readGateway
        .list(UserMfaRecoveryCodeFilter.builder().userUnused(UserReference.of(userUid)).build())
        .stream().filter(c -> c.getCodeHash().equals(hash)).findFirst().map(code -> {
          writeGateway.update(code, code.consume(OffsetDateTime.now()));
          return true;
        }).orElse(false);
  }

  /** Returns the number of unused recovery codes for the user. */
  public long countUnused(String userUid) {
    return readGateway
        .count(UserMfaRecoveryCodeFilter.builder().userUnused(UserReference.of(userUid)).build());
  }

  private String generateRawCode() {
    char[] buf = new char[8];
    for (int i = 0; i < 8; i++) {
      buf[i] = ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()));
    }
    return new String(buf, 0, 4) + "-" + new String(buf, 4, 4);
  }

  private String sha256hex(String input) {
    try {
      byte[] hash =
          MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 not available", e);
    }
  }
}
