package net.civeira.phylax.common.value.validation.validators;

import java.util.HashMap;
import java.util.Map;

import net.civeira.phylax.common.value.validation.ValidationResult;
import net.civeira.phylax.common.value.validation.Validator;

/**
 * Validator for bank account numbers, including IBAN and optional SWIFT validation. Ensures that an
 * IBAN is correctly formatted and optionally checks SWIFT codes.
 */
public class BankAccountValidator implements Validator<String> {
  private final boolean requireSwift;
  private final String errorMessage;
  private static final Map<String, Integer> IBAN_LENGTHS = new HashMap<>();

  static {
    IBAN_LENGTHS.put("ES", 24); // Spain
    // Additional countries can be added as needed
  }

  /**
   * Constructs a BankAccountValidator.
   *
   * @param requireSwift Whether SWIFT validation is required.
   * @param errorMessage The error message to return when validation fails.
   */
  public BankAccountValidator(boolean requireSwift, String errorMessage) {
    this.requireSwift = requireSwift;
    this.errorMessage = errorMessage;
  }

  /**
   * Validates an IBAN and optionally a SWIFT code if required.
   *
   * @param input The bank account string to validate (IBAN, optionally followed by SWIFT).
   * @return A ValidationResult indicating success or failure.
   */
  @Override
  public ValidationResult validate(String input) {
    if (input == null || input.isEmpty()) {
      return new ValidationResult(errorMessage);
    }

    String[] parts = input.split("\\s+");
    String iban = parts[0].replaceAll("\\s+", "").toUpperCase();
    String swift = parts.length > 1 ? parts[1].toUpperCase() : null;

    if (!validateIBAN(iban)) {
      return new ValidationResult(errorMessage);
    }

    boolean wrongSwift = requireSwift && (swift == null || !validateSWIFT(swift, iban));
    return wrongSwift ? new ValidationResult(errorMessage) : new ValidationResult();
  }

  /**
   * Validates an IBAN format and length for supported countries.
   *
   * @param iban The IBAN string to validate.
   * @return True if valid, false otherwise.
   */
  private boolean validateIBAN(String iban) {
    if (iban.length() < 2) {
      return false;
    }

    String countryCode = iban.substring(0, 2);
    Integer expectedLength = IBAN_LENGTHS.get(countryCode);
    if (expectedLength == null || iban.length() != expectedLength) {
      return false;
    }

    String rearranged = iban.substring(4) + iban.substring(0, 4);
    StringBuilder numericIBAN = new StringBuilder();

    for (char ch : rearranged.toCharArray()) {
      if (Character.isDigit(ch)) {
        numericIBAN.append(ch);
      } else if (Character.isLetter(ch)) {
        numericIBAN.append(Character.getNumericValue(ch));
      } else {
        return false;
      }
    }

    return mod97(numericIBAN.toString()) == 1;
  }

  /**
   * Computes the MOD-97 checksum to validate an IBAN.
   *
   * @param numericIBAN The numeric representation of the IBAN.
   * @return The remainder when divided by 97 (should be 1 for valid IBANs).
   */
  private int mod97(String numericIBAN) {
    String remainder = numericIBAN;
    while (remainder.length() > 9) {
      String block = remainder.substring(0, 9);
      remainder = Integer.parseInt(block) % 97 + remainder.substring(block.length());
    }
    return Integer.parseInt(remainder) % 97;
  }

  /**
   * Validates a SWIFT/BIC code format and checks if it matches the IBAN country.
   *
   * @param swift The SWIFT/BIC code to validate.
   * @param iban The IBAN associated with the SWIFT code.
   * @return True if the SWIFT is valid and matches the IBAN's country code, false otherwise.
   */
  private boolean validateSWIFT(String swift, String iban) {
    if (!swift.matches("^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$")) {
      return false;
    }

    String ibanCountryCode = iban.substring(0, 2);
    String swiftCountryCode = swift.substring(4, 6);
    return ibanCountryCode.equals(swiftCountryCode);
  }
}
