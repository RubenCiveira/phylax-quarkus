# Tarea 04-03 — MFA por SMS y OTP por Email como segundo factor

> **Fase:** 04 — Autenticación Avanzada
> **Prioridad:** P3
> **Esfuerzo estimado:** Medio
> **Prerequisito:** Sistema de notificaciones existente

---

## Descripción

Ampliar el sistema MFA más allá del TOTP actual (Google Authenticator).
Los usuarios podrán elegir entre múltiples métodos MFA, y el tenant puede
configurar qué métodos están disponibles.

---

## Pasos de implementación

### 1. Tabla `access_user_mfa_method`

```sql
-- liquibase formatted sql

-- changeset phylax-dev:create-user-mfa-method-table
CREATE TABLE access_user_mfa_method (
  uid          VARCHAR(36)  NOT NULL,
  user_uid     VARCHAR(36)  NOT NULL,
  tenant_id    VARCHAR(36)  NOT NULL,
  method_type  VARCHAR(20)  NOT NULL COMMENT 'totp, email, sms',
  destination  VARCHAR(255) NULL     COMMENT 'phone number or email for otp',
  is_primary   TINYINT(1)   DEFAULT 0,
  enabled_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT PK_ACCESS_USER_MFA_METHOD PRIMARY KEY (uid),
  INDEX idx_user_mfa_user (user_uid, tenant_id)
);
```

### 2. Interfaz `MfaProvider` (puerto de dominio)

```java
public interface MfaProvider {
    MfaMethodType supports();
    String generateCode(UUID userUid, String destination, String tenantId);
    boolean verifyCode(UUID userUid, String destination, String code, String tenantId);
    void sendCode(UUID userUid, String destination, String code, String tenantId);
}

public enum MfaMethodType { TOTP, EMAIL_OTP, SMS_OTP }
```

### 3. Implementación — MFA por Email OTP

```java
@ApplicationScoped
public class EmailOtpProvider implements MfaProvider {

    @Inject
    NotificationOutboxGateway notificationGateway;

    @Inject
    OtpStore otpStore;  // Usa _oauth_temporal_codes con type=EMAIL_OTP

    @Override
    public MfaMethodType supports() { return MfaMethodType.EMAIL_OTP; }

    @Override
    public String generateCode(UUID userUid, String email, String tenantId) {
        String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        otpStore.store(userUid, email, code, Duration.ofMinutes(5));
        return code;
    }

    @Override
    public void sendCode(UUID userUid, String email, String code, String tenantId) {
        notificationGateway.enqueue(
            NotificationRequest.emailOtp(email, code, tenantId));
    }

    @Override
    public boolean verifyCode(UUID userUid, String email, String code, String tenantId) {
        return otpStore.verifyAndConsume(userUid, email, code);
    }
}
```

### 4. Implementación — MFA por SMS OTP

**Interfaz de proveedor SMS (intercambiable por tenant):**

```java
public interface SmsProviderAdapter {
    void send(String phoneNumber, String message, String tenantId);
}

// Implementaciones:
// TwilioSmsAdapter, AwsSnsAdapter, VonageSmsAdapter
```

```java
@ApplicationScoped
public class SmsOtpProvider implements MfaProvider {

    @Inject
    @Any
    Instance<SmsProviderAdapter> smsAdapters;

    // Mismo patrón que EmailOtpProvider pero usando smsAdapter.send()
}
```

**Configuración del proveedor por tenant:**

```sql
-- changeset phylax-dev:add-sms-provider-to-tenant
ALTER TABLE access_tenant_config
  ADD COLUMN sms_provider        VARCHAR(50) NULL COMMENT 'twilio, aws_sns, vonage',
  ADD COLUMN sms_provider_config TEXT        NULL COMMENT 'JSON con credenciales del proveedor';
```

### 5. Integración en el authorize flow

En el paso `mfa` del authorize flow, consultar `access_user_mfa_method`
para determinar qué métodos tiene el usuario y cuál es el primario.

**Lógica:**
1. Obtener métodos MFA del usuario para ese tenant
2. Si solo tiene TOTP: comportamiento actual
3. Si tiene Email OTP: generar y enviar código, mostrar formulario
4. Si tiene SMS OTP: generar y enviar SMS, mostrar formulario
5. Si tiene múltiples: mostrar selector de método

### 6. OTP Store usando `_oauth_temporal_codes`

Reutilizar la tabla existente con un prefijo en el `code`:

```java
// key = "OTP:{method}:{userUid}:{destination}"
// code_data = { "otp": "123456", "attempts": 0 }
// expiration = now + 5 minutos
```

**Límite de intentos:** máximo 3 intentos antes de invalidar el código.

### 7. Tests de integración

- Login con MFA por email: se envía OTP al email ✓
- Verificar OTP correcto → acceso concedido ✓
- OTP incorrecto × 3 → código invalidado ✗
- OTP expirado → error ✗
- Selector de método si el usuario tiene TOTP + Email ✓

---

## Criterios de aceptación

- [ ] Interfaz `MfaProvider` con implementaciones `EmailOtpProvider` y `SmsOtpProvider`
- [ ] `SmsProviderAdapter` con al menos implementación Twilio
- [ ] Tabla `access_user_mfa_method` con migración
- [ ] Paso MFA en authorize flow muestra el método adecuado por usuario
- [ ] Límite de 3 intentos con invalidación del código
- [ ] TTL de 5 minutos para OTP
- [ ] Proveedor SMS configurable por tenant
- [ ] 5 tests de integración
