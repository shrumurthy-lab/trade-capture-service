package com.example.instructions.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, Object payload) {
        try {
            String json = mapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, json);
        } catch (Exception e) {
            System.err.println("Failed to publish message: " + e.getMessage());
        }
    }
}
