# Cambio de modelo para 3.7 (ES256/EdDSA)

Pendiente de regenerar; solo se aplicará al reabrir [3.7](pospuestos/issue-3.7-algoritmos-es256-eddsa.md).

```sql
ALTER TABLE _oauth_keys_storer ADD COLUMN alg VARCHAR(16) NOT NULL DEFAULT 'RS256';
CREATE INDEX idx_oauth_keys_storer_tenant_alg ON _oauth_keys_storer(tenant, alg, expiration);
```

- `alg`: `RS256`, `ES256` o `EdDSA`; las filas existentes quedan `RS256`.
- Equivalente PHP: B.3 `_oauth_keys_storer.alg` de `MODELO_Y_ESQUEMA_018-071.md`.
