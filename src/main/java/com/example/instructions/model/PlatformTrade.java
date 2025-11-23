package com.example.instructions.model;

public class PlatformTrade {
    private String platformId;
    private Trade trade;

    public PlatformTrade() {
    }

    public PlatformTrade(String platformId, Trade trade) {
        this.platformId = platformId;
        this.trade = trade;
    }

    public static class Trade {
        private String account;
        private String security;
        private String type;
        private String amount;
        private String timestamp;

        public Trade() {
        }

        public Trade(String account, String security, String type,
                     String amount, String timestamp) {
            this.account = account;
            this.security = security;
            this.type = type;
            this.amount = amount;
            this.timestamp = timestamp;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getSecurity() {
            return security;
        }

        public void setSecurity(String security) {
            this.security = security;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }
}
