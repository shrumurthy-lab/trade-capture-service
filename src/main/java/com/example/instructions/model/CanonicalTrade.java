package com.example.instructions.model;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CanonicalTrade {

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String securityId;

    @NotBlank
    private String tradeType;

    @NotNull
    private Long amount;

    @NotNull
    private Instant timestamp;

    public CanonicalTrade() {
    }

    public CanonicalTrade(String accountNumber, String securityId, String tradeType,
                          Long amount, Instant timestamp) {
        this.accountNumber = accountNumber;
        this.securityId = securityId;
        this.tradeType = tradeType;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
