# Demo Spring Boot Backend (Samsung Wallet ATW)

Spring Boot 3.x demo that implements partner-side APIs and a simple web UI for Add to Samsung Wallet.

## Run

**From repository root (Maven Wrapper):**
```bash
.\mvnw.cmd -f demo-spring-boot-backend/pom.xml spring-boot:run
```

**Or** from this directory (after `.\mvnw.cmd install` from root):
```bash
cd demo-spring-boot-backend
..\mvnw.cmd spring-boot:run
```

Open [http://localhost:48080](http://localhost:48080).

## Test keys/certificates (Partner Test)

To test with real partner key and Samsung certificate, use one of the following.

### 1) Fixed paths (partner-test profile)

Place keys/certs in `c:\Developments\wlt-partners\dbR7\` and run:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=partner-test
```

Configuration: `src/main/resources/application-partner-test.yml`  
- `wltprtnrs.key` – partner private key  
- `wlt-260223_walletsvc.samsung.com.crt` – Samsung Wallet server public certificate  

Override `partner-id`, `card-id`, `certificate-id` via environment variables:

- `SAMSUNG_WALLET_PARTNER_ID`
- `SAMSUNG_WALLET_CARD_ID`
- `SAMSUNG_WALLET_CERTIFICATE_ID`

### 2) Project certs folder (certs profile)

Copy the following files into `demo-spring-boot-backend/certs/`:

- `wltprtnrs.key`
- `wlt-260223_walletsvc.samsung.com.crt`

**Run from this directory** (relative path `certs/` is resolved from working directory):

```bash
cd demo-spring-boot-backend
mvn spring-boot:run -Dspring-boot.run.profiles=certs
```

See `demo-spring-boot-backend/certs/README.md` for details.  
`.key` and `.crt` files under `certs/` are excluded from the repo via `.gitignore`.

## Configuration

Without any keys, the app uses **in-memory RSA keys** so you can run the demo. For real Samsung Wallet issuance and callbacks:

1. Set in `application.yml` (or env) your Partner Portal values and file paths:
   - `samsung.wallet.partner-id`
   - `samsung.wallet.card-id`
   - `samsung.wallet.certificate-id`
   - `samsung.wallet.partner-private-key-path` – PEM file of your private key
   - `samsung.wallet.samsung-public-cert-path` – PEM file of Samsung’s public certificate

2. Register your **Partner Server URL** in Samsung Partner Portal so that:
   - Get Card Data: `GET {your-base-url}/partner/v1/tickets/{refId}`
   - Send Card State: `POST {your-base-url}/partner/v1/tickets/{refId}/state?cc2=...&event=...`

## API Summary

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Thymeleaf page with ticket/boarding pass/coupon/ID lists + ATW buttons |
| `/api/v1/wallet/transit-data?refId=&type=` | GET | Returns `cdata` for Data Transmit link (type: ticket, boardingpass, coupon, idcard) |
| `/api/v1/wallet/fetch-data?refId=&type=` | GET | Returns `pdata` (refId) for Data Fetch link |
| `/api/v1/wallet/update-notification` | POST | Body: `{ "refId", "state" }` – calls Samsung Update Notification |
| `/partner/v1/tickets/{refId}` | GET | **Samsung → Partner** Get Card Data (auth required) |
| `/partner/v1/tickets/{refId}/state` | POST | **Samsung → Partner** Send Card State (auth required) |

## H2 Console

When enabled, H2 console is at [http://localhost:48080/h2-console](http://localhost:48080/h2-console). JDBC URL: `jdbc:h2:mem:wallet`, user `sa`, password empty.
