# Demo Android Native (Phase 2 – Placeholder)

This directory is reserved for the **Android (Kotlin)** demo that will:

- Call the partner backend (`GET /api/v1/wallet/transit-data` or `fetch-data`) to obtain `cdata` or `pdata`.
- Build the Samsung Wallet ATW link and open it via Intent so the Samsung Wallet app adds the card.

**Planned stack:** Kotlin, Retrofit/OkHttp, and standard Android Intent for `https://a.swallet.link/atw/v3/...` links.

**Status:** Not yet implemented. See the root [README](../README.md) for the overall roadmap.
