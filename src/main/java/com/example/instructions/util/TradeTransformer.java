package com.example.instructions.util;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;

public class TradeTransformer {

    // mask all but last 4 digits
    private static String maskAccount(String acc) {
        if (acc == null || acc.length() < 4) return "****";
        return "****" + acc.substring(acc.length() - 4);
    }

    private static String normalizeTradeType(String type) {
        if (type == null) return "";
        switch (type.trim().toLowerCase()) {
            case "buy":
                return "B";
            case "sell":
                return "S";
            default:
                return type.toUpperCase();
        }
    }

    public static PlatformTrade toPlatformTrade(CanonicalTrade ct) {
        PlatformTrade.Trade trade = new PlatformTrade.Trade(
                maskAccount(ct.getAccountNumber()),
                ct.getSecurityId().toUpperCase(),
                normalizeTradeType(ct.getTradeType()),
                String.valueOf(ct.getAmount()),
                ct.getTimestamp().toString()
        );

        return new PlatformTrade("ACCT123", trade);
    }
}
