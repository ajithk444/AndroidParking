package com.tech.parking.beans;

import java.util.Date;

public class ChargeWallet {
    private String userId;
    private String chargeId;
    private Double totalCharge;
    private Date chargedDate;
    private WalletType walletType;
    private ChargeType chargeType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public Date getChargedDate() {
        return chargedDate;
    }

    public void setChargedDate(Date chargedDate) {
        this.chargedDate = chargedDate;
    }

    public Double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(Double totalCharge) {
        this.totalCharge = totalCharge;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public enum WalletType {
        RECHARGE, BOOKING
    }

    public enum ChargeType {
        CASH, VISA
    }
}
