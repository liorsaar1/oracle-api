package co.cryptocorp.oracle.api;

import java.util.List;
import java.util.Map;

/**
 * @author devrandom
 */
public class WalletCreationResponse extends ApiResponse {
    private final Map<String, List<String>> keys;

    public WalletCreationResponse(String result, Map<String, List<String>> keys) {
        super(result);
        this.keys = keys;
    }

    public Map<String, List<String>> getKeys() {
        return keys;
    }
}
