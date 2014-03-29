/**
 * 
 */
package co.digitaloracle;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import co.digitaloracle.api.ApiResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 * 
 * @author liorsaar
 * 
 */
public class ApiResponseTest {

    /**
     * Test method for {@link co.digitaloracle.api.ApiResponse}.
     */
    @Test
    public void testApiResponseString() throws Exception {
        String string = "{ \"result\" : \"success\", \"keys\" : { \"default\" : [ \"xpub68rQ8y4gfHpEN92JwBojkGCrZFM74gv6m9rw6H3iFiGbgRfRULKdtPZJkrD4BJj7yyerW9BgZ4YCoTHt5rVsXr3BaSfyKBt4dWW9KaHnMdk\" ] }	}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ApiResponse apiResponse = objectMapper.readValue(string, ApiResponse.class);
        String expected = "xpub68rQ8y4gfHpEN92JwBojkGCrZFM74gv6m9rw6H3iFiGbgRfRULKdtPZJkrD4BJj7yyerW9BgZ4YCoTHt5rVsXr3BaSfyKBt4dWW9KaHnMdk";
        String actual = apiResponse.keys.get("default").get(0);
        assertEquals("default key 0", expected, actual);
    }

    @Test
    public void testSignTxResponse() throws JsonParseException, JsonMappingException, IOException {
        ApiResponse apiResponse = ApiResponse.create(Util.getResourceString("signTxResponse.json"));
        assertEquals("result", "success", apiResponse.result);
        assertNotNull("transaction", apiResponse.transaction);
    }

    @Test
    public void testDeferral() throws JsonParseException, JsonMappingException, IOException {
        ApiResponse apiResponse = ApiResponse.create(Util.getResourceString("signTxDeferral.json"));
        assertEquals("result", "deferred", apiResponse.result);
        assertNotNull("deferral", apiResponse.deferral);
        assertEquals("reason", "delay", apiResponse.deferral.reason);
    }

}
