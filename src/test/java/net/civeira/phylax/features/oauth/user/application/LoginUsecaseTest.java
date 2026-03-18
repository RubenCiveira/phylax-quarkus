package net.civeira.phylax.features.oauth.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.features.oauth.authentication.domain.exception.ConsentRequiredException;
import net.civeira.phylax.features.oauth.authentication.domain.exception.MfaRequiredException;
import net.civeira.phylax.features.oauth.authentication.domain.exception.NewPasswordRequiredException;
import net.civeira.phylax.features.oauth.authentication.domain.exception.UnknownUserException;
import net.civeira.phylax.features.oauth.authentication.domain.exception.WrongCredentialsException;
import net.civeira.phylax.features.oauth.user.domain.gateway.LoginGateway;

@ExtendWith(MockitoExtension.class)
class LoginUsecaseTest {

  @Mock
  LoginGateway gateway;

  LoginUsecase usecase;

  @BeforeEach
  void setUp() {
    usecase = new LoginUsecase(gateway);
  }

  // --- validatedUserData ---

  @Test
  void validatedUserData_happyPath() {
    AuthenticationData data = defaultData();
    when(gateway.validateUserData(any(), eq("alice"), eq("pass"), any(), any()))
        .thenReturn(AuthenticationResult.right(data));

    AuthenticationResult result = usecase.validatedUserData(null, "alice", "pass", null, List.of());

    assertThat(result.isRight()).isTrue();
    assertThat(result.getData().getUsername()).isEqualTo("alice@example.com");
  }

  @Test
  void validatedUserData_wrongCredentials() {
    when(gateway.validateUserData(any(), any(), any(), any(), any()))
        .thenReturn(AuthenticationResult.wrongCredential("t", "alice"));

    AuthenticationResult result = usecase.validatedUserData(null, "alice", "wrong", null, List.of());

    assertThat(result.isRight()).isFalse();
    assertThat(result.getFail()).isInstanceOf(WrongCredentialsException.class);
  }

  @Test
  void validatedUserData_unknownUser() {
    when(gateway.validateUserData(any(), any(), any(), any(), any()))
        .thenReturn(AuthenticationResult.unknownName("t", "nobody"));

    AuthenticationResult result = usecase.validatedUserData(null, "nobody", "pass", null, List.of());

    assertThat(result.isRight()).isFalse();
    assertThat(result.getFail()).isInstanceOf(UnknownUserException.class);
  }

  // --- fillPreAuthenticated ---

  @Test
  void fillPreAuthenticated_happyPath() {
    AuthenticationData data = defaultData();
    when(gateway.validatePreAuthenticated(any(), eq("alice"), any(), any()))
        .thenReturn(AuthenticationResult.right(data));

    AuthenticationResult result = usecase.fillPreAuthenticated(null, "alice", null, List.of());

    assertThat(result.isRight()).isTrue();
  }

  @Test
  void fillPreAuthenticated_consentRequired() {
    when(gateway.validatePreAuthenticated(any(), any(), any(), any()))
        .thenReturn(AuthenticationResult.consentRequired("t", "alice"));

    AuthenticationResult result = usecase.fillPreAuthenticated(null, "alice", null, List.of());

    assertThat(result.isRight()).isFalse();
    assertThat(result.getFail()).isInstanceOf(ConsentRequiredException.class);
  }

  @Test
  void fillPreAuthenticated_mfaRequired() {
    when(gateway.validatePreAuthenticated(any(), any(), any(), any()))
        .thenReturn(AuthenticationResult.mfaRequired("t", "alice"));

    AuthenticationResult result = usecase.fillPreAuthenticated(null, "alice", null, List.of());

    assertThat(result.isRight()).isFalse();
    assertThat(result.getFail()).isInstanceOf(MfaRequiredException.class);
  }

  @Test
  void fillPreAuthenticated_newPasswordRequired() {
    when(gateway.validatePreAuthenticated(any(), any(), any(), any()))
        .thenReturn(AuthenticationResult.newPasswordRequired("t", "alice"));

    AuthenticationResult result = usecase.fillPreAuthenticated(null, "alice", null, List.of());

    assertThat(result.isRight()).isFalse();
    assertThat(result.getFail()).isInstanceOf(NewPasswordRequiredException.class);
  }

  // --- helpers ---

  private static AuthenticationData defaultData() {
    AuthenticationData data = new AuthenticationData();
    data.setUsername("alice@example.com");
    data.setTenant("test-tenant");
    return data;
  }
}
