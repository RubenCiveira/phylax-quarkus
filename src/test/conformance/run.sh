#!/usr/bin/env bash
# OpenID Conformance Suite contra phylax-api (V.2; Dynamic OP de V.3 con RUN_DYNAMIC=1).
#
#   conformance/run.sh            # levanta suite + OP, siembra, registra clientes y ejecuta
#   KEEP_UP=1 conformance/run.sh  # deja los contenedores levantados al terminar
#
# Requisitos: docker (compose v2), git, mvn, curl, jq, envsubst, python3 con las dependencias de
# scripts/run-test-plan.py de la suite. Resultados en conformance/out/.
set -euo pipefail

HERE="$(cd "$(dirname "$0")" && pwd)"
SUITE_DIR="${SUITE_DIR:-$HERE/.suite}"
# Fijar una etiqueta publicada para que los resultados sean reproducibles.
SUITE_REF="${SUITE_REF:-master}"
SUITE_URL="${SUITE_URL:-https://localhost.emobix.co.uk:8443/}"
OUT="$HERE/out"
TENANT="main"
OP_HOST="op.phylax.test"
export ISSUER="https://$OP_HOST:9443/oauth/openid/$TENANT"
export ALIAS="phylax"
export SUITE_BASE="${SUITE_URL%/}/test/a/$ALIAS"
export USERNAME="ROOT"
export PASSWORD="${CONFORMANCE_PASSWORD:-Conformance-1234}"
PEPPER="${CONFORMANCE_PEPPER:-conformance-digest-pepper}"
OP_COMPOSE=(docker compose -f "$HERE/docker-compose.yml")

mkdir -p "$OUT"

cleanup() {
  if [ "${KEEP_UP:-0}" != "1" ]; then
    "${OP_COMPOSE[@]}" down -v || true
    docker compose -f "$SUITE_DIR/docker-compose-dev.yml" down -v || true
  fi
}
trap cleanup EXIT

# ── 1. Suite ───────────────────────────────────────────────────────────────
if [ ! -d "$SUITE_DIR/.git" ]; then
  git clone https://gitlab.com/openid/conformance-suite.git "$SUITE_DIR"
fi
git -C "$SUITE_DIR" fetch --tags --quiet
git -C "$SUITE_DIR" checkout --quiet "$SUITE_REF"
(cd "$SUITE_DIR" && mvn -B -ntp -q -DskipTests package)
docker compose -f "$SUITE_DIR/docker-compose-dev.yml" -f "$HERE/suite-override.yml" up -d --build

# ── 2. OP ──────────────────────────────────────────────────────────────────
"${OP_COMPOSE[@]}" up -d --build
curl_op() { curl -sk --resolve "$OP_HOST:9443:127.0.0.1" "$@"; }
for _ in $(seq 1 90); do
  curl_op -f "$ISSUER/.well-known/openid-configuration" -o "$OUT/discovery.json" && break
  sleep 2
done
jq -e .issuer "$OUT/discovery.json" > /dev/null

# ── 3. DCR del tenant y initial access token (digest = SecretDigester) ────────
IAT="$(openssl rand -hex 32)"
IAT_HASH="hmac-sha256:c0:$(printf '%s' "$IAT" | openssl dgst -sha256 -hmac "$PEPPER" -r | cut -d' ' -f1)"
"${OP_COMPOSE[@]}" exec -T mariadb mariadb -uroot -pconformance phylax <<SQL
INSERT INTO identity_tenant_config (uid, version, tenant, dynamic_registration_policy,
  allow_password_grant, force_mfa, invitation_expiry_days, magic_link_expiry_minutes,
  refresh_token_max_lifetime_seconds, refresh_token_ttl_seconds, require_email_verification,
  require_pushed_authorization_requests, session_sso_ttl_seconds)
SELECT UUID(), 0, t.uid, 'token-required', 0, 0, 7, 15, 86400, 3600, 0, 0, 3600
FROM identity_tenant t WHERE t.name = '$TENANT'
  AND NOT EXISTS (SELECT 1 FROM identity_tenant_config c WHERE c.tenant = t.uid);
UPDATE identity_tenant_config SET dynamic_registration_policy = 'token-required'
WHERE tenant = (SELECT uid FROM identity_tenant WHERE name = '$TENANT');
INSERT INTO access_dcr_initial_access_token (uid, version, tenant, token_hash, max_uses, uses,
  expires_at)
SELECT UUID(), 0, t.uid, '$IAT_HASH', 10, 0, NOW() + INTERVAL 1 DAY
FROM identity_tenant t WHERE t.name = '$TENANT';
SQL

# ── 4. Clientes estáticos de los planes, registrados por DCR ───────────────
register() {
  local method="$1"
  curl_op -f -X POST "$ISSUER/connect" -H "Authorization: Bearer $IAT" \
    -H 'Content-Type: application/json' -d "$(jq -n --arg m "$method" --arg b "$SUITE_BASE" '{
      client_name: "conformance \($m)", token_endpoint_auth_method: $m,
      grant_types: ["authorization_code", "refresh_token"],
      redirect_uris: ["\($b)/callback"],
      post_logout_redirect_uris: ["\($b)/post_logout_redirect"],
      backchannel_logout_uri: "\($b)/backchannel_logout",
      backchannel_logout_session_required: true,
      frontchannel_logout_uri: "\($b)/frontchannel_logout",
      frontchannel_logout_session_required: true}')"
}
register client_secret_basic > "$OUT/client1.json"
register client_secret_basic > "$OUT/client2.json"
register client_secret_post > "$OUT/client-post.json"
export CLIENT1_ID CLIENT1_SECRET CLIENT2_ID CLIENT2_SECRET POST_ID POST_SECRET
CLIENT1_ID="$(jq -r .client_id "$OUT/client1.json")"
CLIENT1_SECRET="$(jq -r .client_secret "$OUT/client1.json")"
CLIENT2_ID="$(jq -r .client_id "$OUT/client2.json")"
CLIENT2_SECRET="$(jq -r .client_secret "$OUT/client2.json")"
POST_ID="$(jq -r .client_id "$OUT/client-post.json")"
POST_SECRET="$(jq -r .client_secret "$OUT/client-post.json")"
for template in "$HERE"/plans/*.json; do
  envsubst < "$template" > "$OUT/$(basename "$template")"
done

# ── 5. Planes ──────────────────────────────────────────────────────────────
export CONFORMANCE_SERVER="$SUITE_URL" CONFORMANCE_DEV_MODE=1
PLANS=(
  "oidcc-basic-certification-test-plan[server_metadata=discovery][client_registration=static_client] $OUT/static-client.json"
  "oidcc-config-certification-test-plan $OUT/static-client.json"
  "oidcc-rp-initiated-logout-certification-test-plan[response_type=code][client_registration=static_client] $OUT/static-client.json"
  "oidcc-backchannel-rp-initiated-logout-certification-test-plan[response_type=code][client_registration=static_client] $OUT/static-client.json"
)
if [ "${RUN_DYNAMIC:-0}" = "1" ]; then
  PLANS+=("oidcc-dynamic-certification-test-plan[response_type=code] $OUT/dynamic.json")
fi
# shellcheck disable=SC2086
python3 "$SUITE_DIR/scripts/run-test-plan.py" --export-dir "$OUT" ${PLANS[*]}
