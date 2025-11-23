package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // <-- NEW IMPORT
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class KafkaListenerService {

    private final ObjectMapper mapper;
    private final TradeService tradeService;

    public KafkaListenerService(TradeService tradeService) {
        this.tradeService = tradeService;

        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "instructions.inbound", groupId = "instructions-group")
    public void listen(String message) {
        try {
            CanonicalTrade incoming = mapper.readValue(message, CanonicalTrade.class);
            if (incoming.getTimestamp() == null) incoming.setTimestamp(Instant.now());

            tradeService.processAndPublish(UUID.randomUUID().toString(), incoming);
        } catch (Exception e) {
            System.err.println("Failed to process inbound: " + e.getMessage());
        }
    }
}