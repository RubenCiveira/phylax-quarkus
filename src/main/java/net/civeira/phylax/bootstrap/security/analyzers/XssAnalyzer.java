/* @autogenerated */
package net.civeira.phylax.bootstrap.security.analyzers;

import java.util.regex.Pattern;

import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.bootstrap.security.MaliciousInjectionRiskAnalizer;

@ApplicationScoped
public class XssAnalyzer implements MaliciousInjectionRiskAnalizer {
  private static final Pattern XSS_SCRIPT_PATTERN = Pattern.compile("(?i)<script\\s++[^>]*+>");

  private static final Pattern XSS_JAVASCRIPT_PROTOCOL_PATTERN = Pattern.compile("(?i)javascript:");

  private static final Pattern XSS_ON_EVENT_PATTERN = Pattern.compile(
      "(?i)<[a-z0-9]++(?:\\s++(?!on[a-z])[^>\\s]++)*+\\s++on[a-z]++\\s*+=\\s*+['\"][^'\"]{0,1000}['\"]");

  @Override
  public int analyze(String input) {
    int riskScore = 0;
    if (XSS_SCRIPT_PATTERN.matcher(input).find()) {
      riskScore += 5;
    }
    if (XSS_JAVASCRIPT_PROTOCOL_PATTERN.matcher(input).find()) {
      riskScore += 5;
    }
    if (XSS_ON_EVENT_PATTERN.matcher(input).find()) {
      riskScore += 5;
    }
    return riskScore;
  }

}
