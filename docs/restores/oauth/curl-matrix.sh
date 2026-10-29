#!/usr/bin/env bash

set -euo pipefail

# Basic configuration
BASE_URL="${BASE_URL:-http://localhost:8080}"
TENANT="${TENANT:-default}"
CLIENT_ID="${CLIENT_ID:-phylax-web}"
CLIENT_SECRET="${CLIENT_SECRET:-secret}"
REDIRECT_URI="${REDIRECT_URI:-http://localhost:3000/callback}"
STATE="${STATE:-state-123}"
SCOPE="${SCOPE:-openid profile email}"
USERNAME="${USERNAME:-demo}"
PASSWORD="${PASSWORD:-demo}"

# Optional values for specific flows
DEVICE_CODE="${DEVICE_CODE:-}"
USER_CODE="${USER_CODE:-}"
MAGIC_LINK_CODE="${MAGIC_LINK_CODE:-}"
AUTH_CODE="${AUTH_CODE:-}"
REFRESH_TOKEN="${REFRESH_TOKEN:-}"
ACCESS_TOKEN="${ACCESS_TOKEN:-}"

OIDC_BASE="${BASE_URL}/oauth/openid/${TENANT}"

run_case() {
  local label="$1"
  shift

  printf "\n==================================================\n"
  printf "[%s]\n" "$label"
  printf "==================================================\n"
  "$@"
}

run_case "GET auth (baseline)" \
  curl -sS -i "${OIDC_BASE}/auth?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code&scope=${SCOPE}&state=${STATE}"

run_case "GET authorize (PHP alias)" \
  curl -sS -i "${OIDC_BASE}/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code&scope=${SCOPE}&state=${STATE}"

run_case "POST token password grant" \
  curl -sS -i -X POST "${OIDC_BASE}/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "grant_type=password" \
    --data-urlencode "client_id=${CLIENT_ID}" \
    --data-urlencode "client_secret=${CLIENT_SECRET}" \
    --data-urlencode "username=${USERNAME}" \
    --data-urlencode "password=${PASSWORD}" \
    --data-urlencode "scope=${SCOPE}"

if [[ -n "${AUTH_CODE}" ]]; then
  run_case "POST token authorization_code grant" \
    curl -sS -i -X POST "${OIDC_BASE}/token" \
      -H "Content-Type: application/x-www-form-urlencoded" \
      --data-urlencode "grant_type=authorization_code" \
      --data-urlencode "client_id=${CLIENT_ID}" \
      --data-urlencode "client_secret=${CLIENT_SECRET}" \
      --data-urlencode "code=${AUTH_CODE}" \
      --data-urlencode "redirect_uri=${REDIRECT_URI}"
fi

if [[ -n "${REFRESH_TOKEN}" ]]; then
  run_case "POST token refresh_token grant" \
    curl -sS -i -X POST "${OIDC_BASE}/token" \
      -H "Content-Type: application/x-www-form-urlencoded" \
      --data-urlencode "grant_type=refresh_token" \
      --data-urlencode "client_id=${CLIENT_ID}" \
      --data-urlencode "client_secret=${CLIENT_SECRET}" \
      --data-urlencode "refresh_token=${REFRESH_TOKEN}"
fi

run_case "POST revocation (baseline)" \
  curl -sS -i -X POST "${OIDC_BASE}/revocation" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "token=${ACCESS_TOKEN}"

run_case "POST revoke (PHP alias)" \
  curl -sS -i -X POST "${OIDC_BASE}/revoke" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "token=${ACCESS_TOKEN}"

run_case "POST introspect (baseline)" \
  curl -sS -i -X POST "${OIDC_BASE}/introspect" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "token=${ACCESS_TOKEN}"

run_case "POST introspection (PHP alias)" \
  curl -sS -i -X POST "${OIDC_BASE}/introspection" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "token=${ACCESS_TOKEN}"

run_case "POST par-request (baseline)" \
  curl -sS -i -X POST "${OIDC_BASE}/par-request" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "client_id=${CLIENT_ID}" \
    --data-urlencode "redirect_uri=${REDIRECT_URI}" \
    --data-urlencode "response_type=code" \
    --data-urlencode "scope=${SCOPE}"

run_case "POST par (PHP alias)" \
  curl -sS -i -X POST "${OIDC_BASE}/par" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "client_id=${CLIENT_ID}" \
    --data-urlencode "redirect_uri=${REDIRECT_URI}" \
    --data-urlencode "response_type=code" \
    --data-urlencode "scope=${SCOPE}"

run_case "GET check-session (PHP alias)" \
  curl -sS -i "${OIDC_BASE}/check-session"

run_case "POST magic-link request" \
  curl -sS -i -X POST "${OIDC_BASE}/magic-link/request" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "username=${USERNAME}"

if [[ -n "${MAGIC_LINK_CODE}" ]]; then
  run_case "GET magic-link verify" \
    curl -sS -i "${OIDC_BASE}/magic-link/verify?code=${MAGIC_LINK_CODE}"
fi

run_case "GET device verify page" \
  curl -sS -i "${OIDC_BASE}/device/verify"

if [[ -n "${USER_CODE}" ]]; then
  run_case "POST device verify" \
    curl -sS -i -X POST "${OIDC_BASE}/device/verify" \
      -H "Content-Type: application/x-www-form-urlencoded" \
      --data-urlencode "user_code=${USER_CODE}"
fi

run_case "POST webauthn register begin" \
  curl -sS -i -X POST "${OIDC_BASE}/webauthn/register/begin" \
    -H "Content-Type: application/json" \
    -d '{"username":"'"${USERNAME}"'"}'

run_case "POST webauthn register finish (placeholder)" \
  curl -sS -i -X POST "${OIDC_BASE}/webauthn/register/finish" \
    -H "Content-Type: application/json" \
    -d '{"username":"'"${USERNAME}"'","credential":{}}'

run_case "POST webauthn authenticate begin" \
  curl -sS -i -X POST "${OIDC_BASE}/webauthn/authenticate/begin" \
    -H "Content-Type: application/json" \
    -d '{"username":"'"${USERNAME}"'"}'

run_case "POST webauthn authenticate finish (placeholder)" \
  curl -sS -i -X POST "${OIDC_BASE}/webauthn/authenticate/finish" \
    -H "Content-Type: application/json" \
    -d '{"username":"'"${USERNAME}"'","assertion":{}}'

if [[ -n "${DEVICE_CODE}" ]]; then
  run_case "POST device token polling" \
    curl -sS -i -X POST "${OIDC_BASE}/token" \
      -H "Content-Type: application/x-www-form-urlencoded" \
      --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:device_code" \
      --data-urlencode "client_id=${CLIENT_ID}" \
      --data-urlencode "device_code=${DEVICE_CODE}"
fi

printf "\nDone. Configure variables and re-run as needed.\n"
