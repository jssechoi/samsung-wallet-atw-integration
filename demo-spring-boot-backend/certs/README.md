# Test certificates/keys folder (optional)

Place the following files in this folder; when you run with the **certs** profile, the app will load keys/certificates from here.

- `wltprtnrs.key` – partner private key (PEM)
- `wlt-260223_walletsvc.samsung.com.crt` – Samsung Wallet server public certificate
- (reference) `wlt-260223.crt` – partner certificate issued from CSR (not used in current flow)

**Run example:**

```bash
# Using certs folder (run from this directory)
mvn spring-boot:run -Dspring-boot.run.profiles=certs
```

For **fixed paths** instead, use the `application-partner-test` profile (see `application-partner-test.yml` for paths).

**Security:** `.key` and `.crt` files in this folder are excluded from the repository via `.gitignore`.
