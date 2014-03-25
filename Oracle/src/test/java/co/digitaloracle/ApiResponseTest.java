/**
 * 
 */
package co.digitaloracle;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.codehaus.jackson.map.DeserializationConfig;
import org.junit.Test;

import co.digitaloracle.api.ApiResponse;
import co.digitaloracle.model.KeychainParams;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

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
    public void testKeychainParams() throws Exception {
        String string = "{\"walletAgent\": \"HDM-2.0-cc-100\", \"rulesetId\":\"default\",\"keys\":[\"xpub69H7F5d8KSRgmmdJg2KhpAK8SR3DjMwAdkxj3ZuxV27CprR9LgpeyGmXUbC6wb7ERfvrnKZjXoUmmDznezpbZb7ap6r1D3tgFxHmwMkQTPH\",\"xpub69H7F5d8KSRgmmdJg2KhpAK8SR3DjMwAdkxj3ZuxV27CprR9LgpeyGmXUbC6wb7ERfvrnKZjXoUmmDznezpbZb7ap6r1D3tgFxHmwMkQTPH\"],\"parameters\":{\"velocity_1\":{\"value\":200,\"asset\":\"BTC\",\"period\":86400,\"limited_keys\":[0]},\"delay_1\":60},\"pii\":{\"email\":\"email@email.em\",\"phone\":\"+1 415-415-4155\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KeychainParams kp = objectMapper.readValue(string, KeychainParams.class);
        assertEquals("rulesetId", "default", kp.getRulesetId());
        assertEquals("keys", 2, kp.getKeys().size());
    }

    @Test
    public void testSignTxResponse() throws JsonParseException, JsonMappingException, IOException {
        File f = new File( "./");
        String x = f.getAbsolutePath();
        String rootPath = "./Oracle/src/test/resources/co/digitaloracle/";
        String jsonString = new Scanner(new File(rootPath + "signTxResponse.json")).useDelimiter("\\Z").next();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ApiResponse apiResponse = objectMapper.readValue(jsonString, ApiResponse.class);
        assertEquals("result", "success", apiResponse.result);
    }
}
