package co.cryptocorp.oracle.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author devrandom
 */
public class SigningRequest {
    public static final int DEFAULT_SIGNATURE_INDEX = 1;

    private final ApiTransaction transaction;
    private int signatureIndex = DEFAULT_SIGNATURE_INDEX;

    @JsonProperty("verifications")
    private Map<String, String> verifications;

    public SigningRequest(@JsonProperty("transaction")ApiTransaction transaction) {
        this.transaction = transaction;
    }

    public ApiTransaction getTransaction() {
        return transaction;
    }

    @JsonProperty
    public void setSignatureIndex(int signatureIndex) {
        this.signatureIndex = signatureIndex;
    }

    public int getSignatureIndex() {
        return signatureIndex;
    }

    public void setVerifications(Map<String, String> verifications) {
        this.verifications = verifications;
    }

    @JsonProperty
    public Map<String, String> getVerifications() {
        return verifications;
    }
}
