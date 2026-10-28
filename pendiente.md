Fallo del generador
En los ValueHolder generados, xNull() hace lo mismo que unsetX(): deja Optional.empty(), y al construir la entidad eso se lee como «sin cambio», así que se conserva el valor anterior. Ningún <set-null> vacía nada. La prueba nueva lo detectó. Afecta a:

Acción	Efecto
User.unlock()	No desbloquea al usuario
UserMfaEnrollment.resetMfaTemporalCode()	La semilla pendiente no se borra
UserVerificationCode.resetRegisterVerification()	El código no se invalida
En VerificationCodeService lo he evitado vaciando los campos con nullValue() explícitos. Las otras dos acciones siguen fallando hasta que se corrija la plantilla ValueHolderGenerator. No está en el repositorio de civi-generator que tengo a mano.

Para que lo revises en el modelo
user-verification-code:
No hay filtro por purpose; el servicio lista las filas del usuario y filtra en memoria.
purpose no es obligatorio.
Las acciones se siguen llamando generated-verification y reset-register-verification, aunque sirven para todos los propósitos.
user-mfa-enrollment.expired-at: debería ser expires-at, como en user-verification-code.
uses/multitenant-rbac.xml: sigue usando relying-party y trusted-client, así que fallará al generarlo con este modelo.
Migraciones (D1)
Las migraciones generadas crean las tablas y columnas nuevas vacías y dejan las antiguas. Con una base que ya tenga datos, además de copiarlos:

Las columnas antiguas access_user_accepted_terms_of_use.conditions y access_tenant_login_provider.source son NOT NULL sin valor por defecto, así que los INSERT del código nuevo fallarán.
Las nuevas provider_type y terms_of_use se añaden como NOT NULL sin valor por defecto sobre tablas que ya tienen filas.
El enabled de los proveedores hay que migrarlo como NOT disabled.
Las pruebas MariaDB pasan porque parten de una base vacía. No he escrito ningún changeset, siguiendo la regla de no tocar migraciones.

Lo he apuntado todo en semantica.md, en la sección «Tras la regeneración». Sigue pendiente la plantilla PHP.