/**
 * 
 */
package co.digitaloracle.api;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import co.digitaloracle.model.Deferral;
import co.digitaloracle.model.Transaction;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 * 
 * @author liorsaar
 * 
 */
public class ApiResponse {
    public static final String RESULT_CANCELLED = "cancelled";
    public static final String RESULT_ERROR = "error";
    public static final String RESULT_DEFERRED = "deferred";
    public static final String DEFERRAL_REASON_DELAY = "delay";

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'hh:mm:ss'Z'";

    public String result;
    public String error;
    public HashMap<String, ArrayList<String>> keys;
    public Transaction transaction;
    public Deferral deferral;
    public String now;

    public static ApiResponse create(String jsonString) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ApiResponse apiResponse = objectMapper.readValue(jsonString, ApiResponse.class);
        return apiResponse;
    }

    public static ApiResponse createError(String aErrorMessage) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setError(aErrorMessage);
        return apiResponse;
    }

    public static String toJsonString(Object object) throws IOException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public String getError() {
        return error;
    }

    private void setError(String aErrorMessage) {
        result = RESULT_ERROR;
        error = aErrorMessage;
    }

    public boolean isError() {
        return result == null || result.equals(RESULT_ERROR);
    }

    /**
     * @return entry 0 in default set
     */
    public String getKey() {
        return keys.get("default").get(0);
    }

}
