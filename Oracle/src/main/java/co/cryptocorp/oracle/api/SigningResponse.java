package co.cryptocorp.oracle.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

import co.cryptocorp.oracle.core.DateSerializer;

/**
 * @author devrandom
 */
public class SigningResponse extends ApiResponse {
    @JsonProperty
    private ApiTransaction transaction;

    @JsonProperty
    private Deferral deferral;

    @JsonProperty
    private final Date now = new Date();

    public class Deferral {
        private final Long until;
        private final String reason;
        private List<String> verifications;


        Deferral(String reason, long until, List<String> verifications) {
            this.reason = reason;
            this.until = until;
            this.verifications = verifications;
        }

        @JsonProperty
        @JsonSerialize(using=DateSerializer.class)
        public Date getUntil() {
            if (until > 0)
                return new Date(until);
            else
                return null;
        }

        public String getReason() {
            return reason;
        }

        @JsonProperty
        public List<String> getVerifications() {
            return verifications;
        }
    }

    public SigningResponse(String result) {
        super(result);
    }

    public SigningResponse(String result, long until, List<String> verifications) {
        super(result);
        String reason = (until == 0) ? "verification" : "delay";
        this.deferral = new Deferral(reason, until, verifications);
    }

    public Deferral getDeferral() {
        return deferral;
    }

    public void setTransaction(ApiTransaction transaction) {
        this.transaction = transaction;
    }

    @JsonProperty
    @JsonSerialize(using=DateSerializer.class)
    public Date getNow() {
        return now;
    }
}
