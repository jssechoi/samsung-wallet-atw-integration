# Partner API URLs for Samsung Wallet Portal

Register the following **full URLs** in the Samsung Wallet Partner Portal for each card.  
Replace `{BASE_URL}` with your actual partner server base URL (e.g. `https://api.yourcompany.com` or for local demo `https://your-public-host:48080`).

---

## 1. Event Ticket (entrances) — cardId: `3iulq9hr93hg0`

| API | Method | Full URL |
|-----|--------|----------|
| **Get Card Data** | GET | `{BASE_URL}/cards/3iulq9hr93hg0/{refId}` |
| **Send Card State** | POST | `{BASE_URL}/cards/3iulq9hr93hg0/{refId}` |

**Example (BASE_URL = https://api.yourcompany.com):**
- Get Card Data: `https://api.yourcompany.com/cards/3iulq9hr93hg0/ref-ticket-001`
- Send Card State: `https://api.yourcompany.com/cards/3iulq9hr93hg0/ref-ticket-001` (with query `cc2`, `event` and optional body `callback`)

---

## 2. Boarding Pass (airlines) — cardId: `3iumevlevet00`

| API | Method | Full URL |
|-----|--------|----------|
| **Get Card Data** | GET | `{BASE_URL}/cards/3iumevlevet00/{refId}` |
| **Send Card State** | POST | `{BASE_URL}/cards/3iumevlevet00/{refId}` |

**Example (BASE_URL = https://api.yourcompany.com):**
- Get Card Data: `https://api.yourcompany.com/cards/3iumevlevet00/ref-bp-001`
- Send Card State: `https://api.yourcompany.com/cards/3iumevlevet00/ref-bp-001`

---

## 3. Coupon (others) — cardId: `3iumf1neuhjg0`

| API | Method | Full URL |
|-----|--------|----------|
| **Get Card Data** | GET | `{BASE_URL}/cards/3iumf1neuhjg0/{refId}` |
| **Send Card State** | POST | `{BASE_URL}/cards/3iumf1neuhjg0/{refId}` |

**Example (BASE_URL = https://api.yourcompany.com):**
- Get Card Data: `https://api.yourcompany.com/cards/3iumf1neuhjg0/ref-coupon-001`
- Send Card State: `https://api.yourcompany.com/cards/3iumf1neuhjg0/ref-coupon-001`

---

## 4. Digital ID (employees) — cardId: `3iumf2sua70g0`

| API | Method | Full URL |
|-----|--------|----------|
| **Get Card Data** | GET | `{BASE_URL}/cards/3iumf2sua70g0/{refId}` |
| **Send Card State** | POST | `{BASE_URL}/cards/3iumf2sua70g0/{refId}` |

**Example (BASE_URL = https://api.yourcompany.com):**
- Get Card Data: `https://api.yourcompany.com/cards/3iumf2sua70g0/ref-id-001`
- Send Card State: `https://api.yourcompany.com/cards/3iumf2sua70g0/ref-id-001`

---

## Notes

- **Get Card Data**: Optional query `fields` (e.g. `fields=balance,barcode.value`). Samsung sends `Authorization: Bearer <token>` and `x-request-id`.
- **Send Card State**: Required query `cc2` (e.g. `us`), `event` (e.g. `ADDED`, `DELETED`, `UPDATED`). Optional body `callback` with Samsung Server API base URL.
- `{refId}` in the path is the content reference ID (e.g. `ref-ticket-001`, `ref-bp-001`); Samsung replaces it with the actual refId when calling.
- Ensure your server is reachable from Samsung (HTTPS, correct firewall/DNS). If you use an allow list, register Samsung server IPs.
