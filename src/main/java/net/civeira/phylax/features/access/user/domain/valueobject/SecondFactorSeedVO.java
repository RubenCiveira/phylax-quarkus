package net.civeira.phylax.features.access.user.domain.valueobject;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.crypto.AesCipherService;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.AbstractFailList;
import net.civeira.phylax.common.value.validation.ConstraintFail;
import net.civeira.phylax.common.value.validation.ConstraintFailList;

/**
 * Value Object representing the seed used to the otp login of user.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class SecondFactorSeedVO {

  /**
   * @autogenerated ValueObjectGenerator
   * @param secondFactorSeed
   * @return An empty instance
   */
  public static SecondFactorSeedVO fromCyphered(final String secondFactorSeed) {
    return new SecondFactorSeedVO(true, secondFactorSeed, null);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param secondFactorSeed
   * @return An empty instance
   */
  public static SecondFactorSeedVO fromPlain(final String secondFactorSeed) {
    return tryFrom(secondFactorSeed);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @return An empty instance
   */
  public static SecondFactorSeedVO nullValue() {
    return new SecondFactorSeedVO(false, null, null);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param secondFactorSeed
   * @return An empty instance
   */
  public static SecondFactorSeedVO tryFrom(final Object secondFactorSeed) {
    ConstraintFailList list = new ConstraintFailList();
    SecondFactorSeedVO result = tryFrom(secondFactorSeed, list);
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    return result;
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param secondFactorSeed temptative value
   * @param fails Error list
   * @return An empty instance
   */
  public static <T extends AbstractFailList> SecondFactorSeedVO tryFrom(
      final Object secondFactorSeed, final T fails) {
    if (null == secondFactorSeed) {
      return new SecondFactorSeedVO(false, null, null);
    } else if (secondFactorSeed instanceof String castedSecondFactorSeed) {
      return new SecondFactorSeedVO(false, null, castedSecondFactorSeed);
    } else {
      fails.add(new ConstraintFail("wrong-type", "secondFactorSeed", secondFactorSeed.getClass(),
          "A String type is expected for secondFactorSeed"));
      return null;
    }
  }

  /**
   * @autogenerated ValueObjectGenerator
   */
  private final boolean cyphered;

  /**
   * the seed used to the otp login
   *
   * @autogenerated ValueObjectGenerator
   */
  private final String cypheredValue;

  /**
   * @autogenerated ValueObjectGenerator
   */
  private final String plainValue;

  /**
   * Get the vo value
   *
   * @autogenerated ValueObjectGenerator
   * @param cypher
   * @return
   */
  public Optional<String> getCypheredSecondFactorSeed(final AesCipherService cypher) {
    return cyphered ? Optional.ofNullable(cypheredValue)
        : Optional.ofNullable(plainValue).map(text -> cypher.encryptForAll(text));
  }

  /**
   * Get the vo value
   *
   * @autogenerated ValueObjectGenerator
   * @param cypher
   * @return
   */
  public Optional<String> getPlainSecondFactorSeed(final AesCipherService cypher) {
    return cyphered
        ? Optional.ofNullable(cypheredValue)
            .map(text -> cypher.decryptForAll(text)
                .orElseThrow(() -> new IllegalStateException("Unable to decrypt")))
        : Optional.ofNullable(plainValue);
  }

  /**
   * Returns a formatted string representation of the name (ofuscated if is needed).
   *
   * @autogenerated ValueObjectGenerator
   * @return the string value wrapped in square brackets.
   */
  public String toString() {
    return cyphered ? "[cyphered:" + cypheredValue + "]" : ofuscatePlain();
  }

  /**
   * @autogenerated ValueObjectGenerator
   * @return
   */
  private String ofuscatePlain() {
    return null == plainValue ? "null"
        : "[plain:" + (plainValue.length() > 10 ? plainValue.substring(0, 5) + "*****" : "*****")
            + "]";
  }
}
