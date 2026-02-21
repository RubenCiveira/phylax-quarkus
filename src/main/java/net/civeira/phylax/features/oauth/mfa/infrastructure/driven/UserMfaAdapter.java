package net.civeira.phylax.features.oauth.mfa.infrastructure.driven;

import java.io.IOException;
import java.util.Base64;
import java.util.Locale;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.authentication.application.spi.UserMfaConfigSpi;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.mfa.domain.gateway.UserMfaGateway;
import net.civeira.phylax.features.oauth.mfa.domain.model.PublicLoginMfaBuildResponse;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class UserMfaAdapter implements UserMfaGateway {

  private final UserMfaConfigSpi mfaConfigSpi;

  @Override
  public PublicLoginMfaBuildResponse configurationForNewMfa(String tenant, String username,
      Locale locale) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).locale(locale).build();
    boolean needsImage = mfaConfigSpi.requireImage(request, username);
    String imageDataUri = mfaConfigSpi.configQr(request, username, locale).map(ds -> {
      try {
        byte[] bytes = ds.getInputStream().readAllBytes();
        return "data:" + ds.getContentType() + ";base64,"
            + Base64.getEncoder().encodeToString(bytes);
      } catch (IOException e) {
        log.error("Error reading QR image", e);
        return null;
      }
    }).orElse(null);
    return PublicLoginMfaBuildResponse.builder().requiresImage(needsImage).image(imageDataUri)
        .build();
  }

  @Override
  public boolean verifyOtp(String tenant, String username, String otp) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).build();
    return mfaConfigSpi.validateOtpConfig(request, username, otp);
  }

  @Override
  public boolean verifyNewOtp(String tenant, String username, String otp) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).build();
    return mfaConfigSpi.validateOtpConfig(request, username, otp);
  }

  @Override
  public void storeSeed(String tenant, String username, String seed) {
    // El SPI legacy almacena el seed como parte de validateOtpConfig al confirmar
    // la configuración. Este método queda disponible para implementaciones futuras.
  }
}
