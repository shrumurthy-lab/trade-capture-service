package com.example.instructions.controller;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.service.InMemoryStore;
import com.example.instructions.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trades")
@Validated
public class TradeController {

    private final TradeService tradeService;
    private final InMemoryStore store;
    private final ObjectMapper objectMapper;

    public TradeController(TradeService tradeService, InMemoryStore store, ObjectMapper objectMapper) {
        this.tradeService = tradeService;
        this.store = store;
        this.objectMapper = objectMapper;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String finaName = file.getOriginalFilename();
            if (finaName == null)
                return ResponseEntity.badRequest().body("Missing filename");

            if (finaName.endsWith(".csv")) {
                List<CanonicalTrade> trades = parseCsvFile(file);
                List<PlatformTrade> result = new ArrayList<>();

                for (CanonicalTrade ct : trades) {
                    result.add(tradeService.processAndPublish(UUID.randomUUID().toString(), ct));
                }
                return ResponseEntity.ok(result);

            }

            return ResponseEntity.badRequest().body("Unsupported file type");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/store")
    public ResponseEntity<?> listStore() {
        List<Map<String, Object>> entries = store.values().stream().map(canonicalTrade -> {
            Map<String, Object> m = new HashMap<>();
            m.put("security", canonicalTrade.getSecurityId());
            m.put("type", canonicalTrade.getTradeType());
            m.put("amount", canonicalTrade.getAmount());
            m.put("timestamp", canonicalTrade.getTimestamp());
            return m;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(entries);
    }

    private List<CanonicalTrade> parseCsvFile(MultipartFile file) throws Exception {
        List<CanonicalTrade> canonicalTradeList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String header = br.readLine();
            if (header == null)
                return canonicalTradeList;

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 5)
                    continue;

                CanonicalTrade canonicalTrade = new CanonicalTrade();
                canonicalTrade.setAccountNumber(parts[0].trim());
                canonicalTrade.setSecurityId(parts[1].trim());
                canonicalTrade.setTradeType(parts[2].trim());
                canonicalTrade.setAmount(Long.parseLong(parts[3].trim()));
                canonicalTrade.setTimestamp(Instant.parse(parts[4].trim()));

                canonicalTradeList.add(canonicalTrade);
            }
        }
        return canonicalTradeList;
    }
}
