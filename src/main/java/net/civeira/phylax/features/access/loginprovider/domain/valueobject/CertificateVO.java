package net.civeira.phylax.features.access.loginprovider.domain.valueobject;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.AbstractFailList;
import net.civeira.phylax.common.value.validation.ConstraintFail;
import net.civeira.phylax.common.value.validation.ConstraintFailList;

/**
 * Value Object representing the provider certificate used for signature verification, if required.
 * of loginProvider.
 * <p>
 * This class ensures that the certificate is a {@code String} and encapsulates its validation and
 * formatting logic. It is immutable and compliant with DDD (Domain-Driven Design) principles.
 * </p>
 * <p>
 * Use {@link #from(String)} or {@link #tryFrom(Object)} to safely create instances. Validation
 * errors are reported via {@link ConstraintFailList} or thrown as {@link ConstraintException}.
 * </p>
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class CertificateVO {

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param certificate
   * @return An empty instance
   */
  public static CertificateVO from(final String certificate) {
    return tryFrom(certificate);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @return An empty instance
   */
  public static CertificateVO nullValue() {
    return new CertificateVO(null);
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param certificate
   * @return An empty instance
   */
  public static CertificateVO tryFrom(final Object certificate) {
    ConstraintFailList list = new ConstraintFailList();
    CertificateVO result = tryFrom(certificate, list);
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    return result;
  }

  /**
   * Crear an empty instance with no value
   *
   * @autogenerated ValueObjectGenerator
   * @param certificate temptative value
   * @param fails Error list
   * @return An empty instance
   */
  public static <T extends AbstractFailList> CertificateVO tryFrom(final Object certificate,
      final T fails) {
    if (null == certificate) {
      return new CertificateVO(null);
    } else if (certificate instanceof String castedCertificate) {
      return new CertificateVO(castedCertificate);
    } else {
      fails.add(new ConstraintFail("wrong-type", "certificate", certificate.getClass(),
          "A String type is expected for certificate"));
      return null;
    }
  }

  /**
   * The actual value of the name.
   *
   * @autogenerated ValueObjectGenerator
   */
  private final String value;

  /**
   * Get the vo value
   *
   * @autogenerated ValueObjectGenerator
   * @return
   */
  public Optional<String> getCertificate() {
    return Optional.ofNullable(value);
  }

  /**
   * Returns a formatted string representation of the name.
   *
   * @autogenerated ValueObjectGenerator
   * @return the string value wrapped in square brackets.
   */
  @Override
  public String toString() {
    return "[" + value + "]";
  }
}
