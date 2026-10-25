package net.civeira.phylax.features.oauth.tokensecurity.application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Authentication context classes (OIDC {@code acr}) derived from the methods actually used (tasks
 * 1.13 and 3.10, decision of PHP 067).
 *
 * <p>
 * The ACR is not stored: it follows from the session {@code amr}. Two levels: single factor (any
 * authenticated session) and multi factor ({@code amr} with {@code mfa}). Their values are
 * configuration, {@code security.acr.single-factor} and {@code security.acr.multi-factor}, each a
 * comma-separated list whose first value is the one emitted and announced and the rest accepted
 * aliases. A value cannot name both levels.
 */
@ApplicationScoped
public class AcrPolicy {

  /**
   * Levels of assurance, in increasing order.
   */
  public enum Level {
    SINGLE_FACTOR, MULTI_FACTOR
  }

  private final List<String> single;
  private final List<String> multi;

  public AcrPolicy(
      @ConfigProperty(name = "security.acr.single-factor",
          defaultValue = "urn:lugh:acr:1fa") String single,
      @ConfigProperty(name = "security.acr.multi-factor",
          defaultValue = "urn:lugh:acr:mfa") String multi) {
    this.single = values(single);
    this.multi = values(multi);
    if (this.single.isEmpty() || this.multi.isEmpty()
        || this.single.stream().anyMatch(this.multi::contains)) {
      throw new IllegalArgumentException("Each ACR level needs its own values");
    }
  }

  /**
   * Level reached with the given methods.
   *
   * @param amr authentication methods of the session
   * @return multi factor when they include {@code mfa}
   */
  public Level level(List<String> amr) {
    return amr != null && amr.contains("mfa") ? Level.MULTI_FACTOR : Level.SINGLE_FACTOR;
  }

  /**
   * ACR to emit for the given methods.
   *
   * @param amr authentication methods of the session
   * @return the emitted value of its level
   */
  public String acr(List<String> amr) {
    return emitted(level(amr));
  }

  /**
   * Emitted value of a level.
   *
   * @param level level
   * @return its first configured value
   */
  public String emitted(Level level) {
    return level == Level.MULTI_FACTOR ? multi.get(0) : single.get(0);
  }

  /**
   * First known level of a space-separated {@code acr_values}; unknown values are ignored.
   *
   * @param acrValues requested values
   * @return the level, empty when none is known
   */
  public Optional<Level> requested(String acrValues) {
    if (acrValues == null) {
      return Optional.empty();
    }
    return Arrays.stream(acrValues.trim().split("\\s+")).map(this::levelOf)
        .flatMap(Optional::stream).findFirst();
  }

  /**
   * Whether {@code acr_values} asks for a second factor.
   *
   * @param acrValues requested values
   * @return true when its first known level is multi factor
   */
  public boolean requiresMultiFactor(String acrValues) {
    return requested(acrValues).filter(Level.MULTI_FACTOR::equals).isPresent();
  }

  /**
   * Values announced as {@code acr_values_supported}.
   *
   * @return the emitted value of each level
   */
  public List<String> supported() {
    return List.of(single.get(0), multi.get(0));
  }

  private Optional<Level> levelOf(String value) {
    if (single.contains(value)) {
      return Optional.of(Level.SINGLE_FACTOR);
    }
    return multi.contains(value) ? Optional.of(Level.MULTI_FACTOR) : Optional.empty();
  }

  private static List<String> values(String configured) {
    return configured == null ? List.of()
        : Arrays.stream(configured.split(",")).map(String::trim).filter(v -> !v.isEmpty())
            .distinct().toList();
  }
}
