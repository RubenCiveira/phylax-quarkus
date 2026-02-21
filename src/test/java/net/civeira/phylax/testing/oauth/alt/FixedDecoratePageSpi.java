package net.civeira.phylax.testing.oauth.alt;

import java.util.Locale;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.authentication.application.spi.DecoratePageSpi;

@Alternative
@Priority(1)
@ApplicationScoped
public class FixedDecoratePageSpi implements DecoratePageSpi {
  @Override
  public String getFullPage(String title, String innerContent, Locale locale) {
    return innerContent;
  }
}
