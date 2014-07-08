package co.cryptocorp.oracle.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.bitcoin.core.AddressFormatException;

import java.util.List;
import java.util.Map;

/**
 * @author devrandom
 */
public class WalletCreationRequest {
    private final String rulesetId;
    private final Map<String, Object> parameters;
    private final List<String> keys;
    private final Map<String, String> pii;

    public WalletCreationRequest(@JsonProperty("rulesetId") String rulesetId,
                                 @JsonProperty("parameters") Map<String, Object> parameters,
                                 @JsonProperty("keys") List<String> keys,
                                 @JsonProperty("pii") Map<String, String> pii
                                 ) throws AddressFormatException {
        this.rulesetId = rulesetId;
        this.parameters = parameters;
        this.keys = keys;
        this.pii = pii;
    }

    public String getRulesetId() {
        return rulesetId;
    }

    public List<String> getKeys() {
        return keys;
    }

    public Map<String, String> getPii() {
        return pii;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

}
