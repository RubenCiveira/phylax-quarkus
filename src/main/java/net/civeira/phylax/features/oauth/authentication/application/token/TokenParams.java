package net.civeira.phylax.features.oauth.authentication.application.token;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Parsing of token endpoint parameters shared by the grants.
 */
final class TokenParams {

  private TokenParams() {}

  static String first(Map<String, List<String>> params, String name) {
    List<String> values = params == null ? null : params.get(name);
    return values == null || values.isEmpty() || values.get(0) == null || values.get(0).isEmpty()
        ? null
        : values.get(0);
  }

  /**
   * Scope values of a {@code scope} parameter: space separated, without blanks or duplicates.
   */
  static List<String> scopes(String scope) {
    return scope == null ? List.of()
        : Arrays.stream(scope.split("\\s+")).filter(s -> !s.isBlank()).distinct().toList();
  }
}
