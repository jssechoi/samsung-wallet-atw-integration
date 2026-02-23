# Core Crypto (Samsung Wallet ATW)

Pure Java module for Samsung Wallet token handling: **cdata** (JWS-wrapped JWE), **REST API Authorization token**, and **Authorization header validation**. No Spring or other framework dependencies—suitable for Java 17+ and for copy-paste into legacy Java 8/11 projects (with dependency version adjustments).

## Build

From repository root:

```bash
mvn -pl core-crypto test
```

## Usage

1. **Configure** – Create `WalletCryptoConfig` with partner ID, card ID, certificate ID, your private key, and Samsung’s public key (from cert or PEM).
2. **cdata** – Use `WalletCryptoUtil#buildCData(String cardPayloadJson)` to produce the token for Data Transmit links (30s TTL by default).
3. **Auth token** – Use `WalletCryptoUtil#buildAuthToken(String method, String path)` when calling Samsung APIs (e.g. Update Notification); path must match the request you will send.
4. **Validation** – When Samsung calls your server, use `AuthTokenValidator#validate(authorizationHeader, requestMethod, requestPath)` with Samsung’s public key in the same config.

## Dependencies

- **nimbus-jose-jwt** (JWS/JWE/JWT)
- **JUnit 5** (test only)

## References

- [Card Data Token (cdata)](https://developer.samsung.com/wallet/securityauthentication/carddatatoken.html)
- [REST API Authorization Token](https://developer.samsung.com/wallet/securityauthentication/restapiauthorizationtoken.html)
