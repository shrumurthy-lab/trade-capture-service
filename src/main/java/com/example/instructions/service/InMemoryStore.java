package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

import org.springframework.stereotype.Component;

@Component
public class InMemoryStore {

    private final ConcurrentHashMap<String, CanonicalTrade> store = new ConcurrentHashMap<>();

    public void put(String key, CanonicalTrade canonicalTrade) {
        store.put(key, canonicalTrade);
    }

    public CanonicalTrade get(String key) {
        return store.get(key);
    }

    public Collection<CanonicalTrade> values() {
        return store.values();
    }

    public void remove(String key) {
        store.remove(key);
    }
}
