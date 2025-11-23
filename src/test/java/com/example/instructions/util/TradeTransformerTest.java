package com.example.instructions.util;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TradeTransformerTest {

    @Test
    void toPlatformTrade_appliesAllTransformations() {
        // Arrange
        Instant fixedTime = Instant.parse("2025-08-04T21:15:33Z");
        CanonicalTrade canonical = new CanonicalTrade(
                "123456789012", // 12 digits
                "abc123",      // lowercase
                "buy",         // mixed case
                100000L,
                fixedTime
        );


        PlatformTrade platform = TradeTransformer.toPlatformTrade(canonical);

        PlatformTrade.Trade trade = platform.getTrade();

        // 1. Account Masking: **** + last 4
        assertEquals("****9012", trade.getAccount(), "Account number must be masked.");

        // 2. Security ID Uppercasing
        assertEquals("ABC123", trade.getSecurity(), "Security ID must be uppercase.");

        // 3. Trade Type Normalization: "buy" -> "B"
        assertEquals("B", trade.getType(), "Trade type must be normalized.");

        // 4. Other fields
        assertEquals("ACCT123", platform.getPlatformId(), "Platform ID must be set.");
        assertEquals("100000", trade.getAmount(), "Amount must match.");
        assertEquals(fixedTime.toString(), trade.getTimestamp(), "Timestamp must match.");
    }

    @Test
    void toPlatformTrade_handlesShortAccount() {
        CanonicalTrade canonical = new CanonicalTrade(
                "123",
                "XYZ",
                "SELL",
                1L,
                Instant.now()
        );
        PlatformTrade platform = TradeTransformer.toPlatformTrade(canonical);
        // Expects "****" for account shorter than 4 digits
        assertEquals("****", platform.getTrade().getAccount());
        assertEquals("S", platform.getTrade().getType()); // SELL -> S
    }
}