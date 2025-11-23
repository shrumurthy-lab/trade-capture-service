package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.util.TradeTransformer;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    private final InMemoryStore store;
    private final KafkaPublisher publisher;

    public TradeService(InMemoryStore store, KafkaPublisher publisher) {
        this.store = store;
        this.publisher = publisher;
    }

    public PlatformTrade processAndPublish(String key, CanonicalTrade ct) {
        store.put(key, ct);

        PlatformTrade transformed = TradeTransformer.toPlatformTrade(ct);

        publisher.publish("instructions.outbound", transformed);

        return transformed;
    }
}
