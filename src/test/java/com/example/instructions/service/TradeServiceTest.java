package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TradeServiceTest {

    private InMemoryStore store;
    private KafkaPublisher publisher;
    private TradeService tradeService;
    private CanonicalTrade sampleCanonicalTrade;

    @BeforeEach
    void setUp() {
        store = mock(InMemoryStore.class);
        publisher = mock(KafkaPublisher.class);
        tradeService = new TradeService(store, publisher);

        sampleCanonicalTrade = new CanonicalTrade(
                "9876543210",
                "xyz789",
                "Sell",
                50000L,
                Instant.now()
        );
    }

    @Test
    void processAndPublish_storesTransformsAndPublishes() {
        String key = "test-uuid";

        // Act
        PlatformTrade result = tradeService.processAndPublish(key, sampleCanonicalTrade);

        // Assert 1: Storing
        verify(store, times(1)).put(key, sampleCanonicalTrade);

        // Assert 2: Publishing
        ArgumentCaptor<PlatformTrade> platformTradeCaptor = ArgumentCaptor.forClass(PlatformTrade.class);
        verify(publisher, times(1)).publish(eq("instructions.outbound"), platformTradeCaptor.capture());

        PlatformTrade publishedTrade = platformTradeCaptor.getValue();

        // Check key transformations on the published object
        assertEquals("****3210", publishedTrade.getTrade().getAccount());
        assertEquals("S", publishedTrade.getTrade().getType());

        // Assert 3: Return Value
        assertEquals(publishedTrade, result);
    }
}