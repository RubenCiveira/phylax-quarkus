package net.civeira.phylax.features.oauth.webauthn.infrastructure.driven;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.AuthenticationParameters;
import com.webauthn4j.data.AuthenticationRequest;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialType;
import com.webauthn4j.data.RegistrationParameters;
import com.webauthn4j.data.RegistrationRequest;
import com.webauthn4j.data.attestation.authenticator.AAGUID;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.authenticator.COSEKey;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;

import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.features.oauth.webauthn.domain.exception.WebAuthnException;
import net.civeira.phylax.features.oauth.webauthn.domain.gateway.WebAuthnVerifierGateway;

/**
 * Implements WebAuthn registration and assertion verification using webauthn4j.
 *
 * <p>
 * Uses {@link WebAuthnManager#createNonStrictWebAuthnManager()} which skips attestation format
 * validation — suitable for passkey flows where attestation trust anchors are not required. Switch
 * to a strict manager with a real trust-anchor store if attestation is mandated.
 * </p>
 */
@ApplicationScoped
public class WebAuthnVerifierAdapter implements WebAuthnVerifierGateway {

  private static final List<PublicKeyCredentialParameters> PUB_KEY_CRED_PARAMS = List.of(
      new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY,
          COSEAlgorithmIdentifier.ES256),
      new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY,
          COSEAlgorithmIdentifier.RS256));

  private final WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
  private final ObjectConverter objectConverter = new ObjectConverter();

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> verifyRegistration(Map<String, Object> clientCredential,
      String challenge, String origin, String rpId) {
    try {
      Map<String, Object> response = (Map<String, Object>) clientCredential.get("response");

      byte[] attestationObject = decode((String) response.get("attestationObject"));
      byte[] clientDataJSON = decode((String) response.get("clientDataJSON"));
      Set<String> transports =
          Optional.ofNullable((List<String>) clientCredential.get("transports")).map(Set::copyOf)
              .orElse(Set.of());

      RegistrationRequest request =
          new RegistrationRequest(attestationObject, clientDataJSON, transports);

      ServerProperty serverProperty = ServerProperty.builder().origin(new Origin(origin)).rpId(rpId)
          .challenge(new DefaultChallenge(decode(challenge))).build();

      var data = webAuthnManager.verify(request,
          new RegistrationParameters(serverProperty, PUB_KEY_CRED_PARAMS, false, false));

      var authenticatorData = data.getAttestationObject().getAuthenticatorData();
      var attestedCredential = authenticatorData.getAttestedCredentialData();

      byte[] credentialIdBytes = attestedCredential.getCredentialId();
      COSEKey coseKey = attestedCredential.getCOSEKey();
      byte[] publicKeyBytes = objectConverter.getCborMapper().writeValueAsBytes(coseKey);

      AAGUID aaguid = attestedCredential.getAaguid();
      String aaguidStr =
          (aaguid != null && !AAGUID.NULL.equals(aaguid)) ? aaguid.getValue().toString() : null;

      Map<String, Object> result = new HashMap<>();
      result.put("credentialId", encode(credentialIdBytes));
      result.put("publicKey", encode(publicKeyBytes));
      result.put("signCount", (int) authenticatorData.getSignCount());
      result.put("aaguid", aaguidStr);
      if (!transports.isEmpty()) {
        result.put("transports", List.copyOf(transports));
      }
      return result;
    } catch (WebAuthnException e) {
      throw e;
    } catch (Exception e) {
      throw WebAuthnException.verificationFailed(e.getMessage());
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public int verifyAssertion(Map<String, Object> clientCredential, String challenge, String origin,
      String rpId, String storedPublicKey, int storedSignCount) {
    try {
      Map<String, Object> response = (Map<String, Object>) clientCredential.get("response");

      byte[] credentialId = decode((String) clientCredential.get("id"));
      byte[] authenticatorData = decode((String) response.get("authenticatorData"));
      byte[] clientDataJSON = decode((String) response.get("clientDataJSON"));
      byte[] signature = decode((String) response.get("signature"));
      byte[] userHandle =
          Optional.ofNullable((String) response.get("userHandle")).map(this::decode).orElse(null);

      AuthenticationRequest request = new AuthenticationRequest(credentialId, userHandle,
          authenticatorData, clientDataJSON, signature);

      COSEKey coseKey =
          objectConverter.getCborMapper().readValue(decode(storedPublicKey), COSEKey.class);

      AttestedCredentialData attestedCredentialData =
          new AttestedCredentialData(AAGUID.NULL, credentialId, coseKey);

      CredentialRecordImpl credentialRecord = new CredentialRecordImpl(null, // attestationStatement
                                                                             // — not needed for
                                                                             // stored credentials
          null, // uvInitialized
          null, // backupEligible
          null, // backupState
          storedSignCount, attestedCredentialData, null, // authenticatorExtensions
          null, // clientData
          null, // clientExtensions
          Set.of());

      ServerProperty serverProperty = ServerProperty.builder().origin(new Origin(origin)).rpId(rpId)
          .challenge(new DefaultChallenge(decode(challenge))).build();

      var data = webAuthnManager.verify(request,
          new AuthenticationParameters(serverProperty, credentialRecord, null, false, false));

      int newSignCount = (int) data.getAuthenticatorData().getSignCount();
      if (storedSignCount > 0 && newSignCount <= storedSignCount) {
        throw WebAuthnException.replayAttack();
      }
      return newSignCount;
    } catch (WebAuthnException e) {
      throw e;
    } catch (Exception e) {
      throw WebAuthnException.verificationFailed(e.getMessage());
    }
  }

  private byte[] decode(String value) {
    return Base64.getUrlDecoder().decode(value);
  }

  private String encode(byte[] bytes) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
