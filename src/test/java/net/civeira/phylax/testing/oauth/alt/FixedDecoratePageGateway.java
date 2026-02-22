package net.civeira.phylax.testing.oauth.alt;

import java.util.Locale;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.authentication.domain.gateway.DecoratePageGateway;

@Alternative
@Priority(1)
@ApplicationScoped
public class FixedDecoratePageGateway implements DecoratePageGateway {
  @Override
  public String getFullPage(String title, String innerContent, Locale locale) {
    return innerContent;
  }
}
