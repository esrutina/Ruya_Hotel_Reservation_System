package com.ruyahotel.util;

public class AppConfig {

    // ── verify.leul.et — Telebirr verification ──────────────────────────────
    public static final String TELEBIRR_VERIFY_URL =
            "https://verify.leul.et/verify-telebirr";

    // leul.et live API key
    public static final String STRIPE_API_KEY = System.getenv("STRIPE_API_KEY");
}