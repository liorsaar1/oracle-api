package co.cryptocorp.oracle.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

import co.cryptocorp.oracle.core.HexDeserializer;

/**
 * @author devrandom
 */
public class Spend {
    private final BigInteger amount;
    private final byte[] hash;
    private String status;
    private int delay;
    private Date date;
    private UUID id;
    private String walletName;

    public Spend(
            @JsonProperty("amount")
            BigInteger amount,
            @JsonProperty("hash") @JsonDeserialize(using = HexDeserializer.class)
            byte[] hash,
            @JsonProperty("status")
            String status
            ) {
        this.amount = amount;
        this.hash = hash;
        this.status = status;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public BigDecimal getValue() { return new BigDecimal(getAmount(), 8); }

    public byte[] getHash() {
        return hash;
    }

    public String getStatus() {
        return status;
    }

    public int getDelay() {
        return delay;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletName() {
        return walletName;
    }
}
