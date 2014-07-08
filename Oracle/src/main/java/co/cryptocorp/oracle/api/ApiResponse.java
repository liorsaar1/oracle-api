package co.cryptocorp.oracle.api;

/**
 * @author devrandom
 */
public class ApiResponse {
    private final String result;

    public ApiResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
