/**
 * 
 */
package co.digitaloracle;

import static org.junit.Assert.*;

import org.codehaus.jackson.map.DeserializationConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import co.digitaloracle.DigitalOracle;
import co.digitaloracle.api.ApiListener;
import co.digitaloracle.api.ApiResponse;
import co.digitaloracle.model.KeychainParams;
import co.digitaloracle.model.SignatureRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.concurrent.CountDownLatch;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 * 
 * @author liorsaar
 * 
 */
public class DigitalOracleTest {

    private static final String DO_SANDBOX_URL = "http://btc2.hyper.to";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test method for {@link co.digitaloracle.DigitalOracle#DigitalOracle(java.lang.String)}.
     */
    @Test
    public void testDigitalOracle() {
        // assert null
        thrown.expect(NullPointerException.class);
        DigitalOracle digitalOracle = new DigitalOracle(null);
    }

    /**
     * Test method for {@link co.digitaloracle.DigitalOracle#getKeychain(java.lang.String, co.digitaloracle.api.ApiListener)}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testGetKeychain() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final ApiResponse[] apiResponse = { null };

        String keychainId = "6408110e-900b-4dfd-891b-62ed0478ea4b";
        String keyExpected = "xpub68rQ8y4gfHpEN92JwBojkGCrZFM74gv6m9rw6H3iFiGbgRfRULKdtPZJkrD4BJj7yyerW9BgZ4YCoTHt5rVsXr3BaSfyKBt4dWW9KaHnMdk";

        DigitalOracle digitalOracle = new DigitalOracle(DO_SANDBOX_URL);
        digitalOracle.getKeychain(keychainId, new ApiListener() {

            @Override
            public void onSuccess(ApiResponse aApiResponse) {
                System.out.println("onSuccess: " + aApiResponse);
                apiResponse[0] = aApiResponse;
                latch.countDown();
            }

            @Override
            public void onError(ApiResponse aApiResponse) {
                System.out.println("onError: " + aApiResponse.getError());
                apiResponse[0] = aApiResponse;
                latch.countDown();
            }
        });
        latch.await();
        assertNull("Get Keychain error", apiResponse[0].getError());
        assertEquals("Get Keychain", keyExpected, apiResponse[0].getKey());
    }

    /**
     * Test method for {@link co.digitaloracle.DigitalOracle#createKeychain(KeychainParams, co.digitaloracle.api.ApiListener)}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateKeychain() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);
        final ApiResponse[] apiResponse = { null };

        KeychainParams keychainParams = getKeychainParams();
        DigitalOracle digitalOracle = new DigitalOracle(DO_SANDBOX_URL);

        digitalOracle.createKeychain(keychainParams, new ApiListener() {

            @Override
            public void onSuccess(ApiResponse aApiResponse) {
                System.out.println("onSuccess: " + aApiResponse);
                apiResponse[0] = aApiResponse;
                latch.countDown();
            }

            @Override
            public void onError(ApiResponse aApiResponse) {
                System.out.println("onError: " + aApiResponse.getError());
                apiResponse[0] = aApiResponse;
                latch.countDown();
            }
        });
        latch.await();
        assertEquals("Create Keychain error", null, apiResponse[0].getError());
        assertNotNull("Create Keychain", apiResponse[0].getKey());
    }

    /**
     * Test method for {@link co.digitaloracle.DigitalOracle#signTx(java.lang.String, SignatureRequest, co.digitaloracle.api.ApiListener)}.
     * 
     * @throws Exception
     */
    @Test
    public void testSignTx() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final ApiResponse[] apiResponse = { null };

        SignatureRequest signatureRequest = getSignatureRequest();
        String keychainId = "6408110e-900b-4dfd-891b-62ed0478ea4b";

        DigitalOracle digitalOracle = new DigitalOracle(DO_SANDBOX_URL);
        digitalOracle.signTx(keychainId, signatureRequest, new ApiListener() {

            @Override
            public void onSuccess(ApiResponse aApiResponse) {
                System.out.println("onSuccess: " + aApiResponse);
                apiResponse[0] = aApiResponse;
                latch.countDown();
            }

            @Override
            public void onError(ApiResponse aApiResponse) {
                System.out.println("onError: " + aApiResponse.getError());
                apiResponse[0] = aApiResponse;
                latch.countDown();
            }
        });
        latch.await();
        assertEquals("Sign Tx error", null, apiResponse[0].getError());
        assertFalse("Sign Tx", apiResponse[0].isError());
    }

    @Test
    public void testKeychainParams() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KeychainParams kp = objectMapper.readValue(KEYCHAIN_PARAMS, KeychainParams.class);
        assertEquals("rulesetId", "default", kp.getRulesetId());
        assertEquals("keys", 2, kp.getKeys().size());
    }

    private static final String KEYCHAIN_PARAMS = "{\"walletAgent\": \"HDM-2.0-cc-100\", \"rulesetId\":\"default\",\"keys\":[\"xpub69H7F5d8KSRgmmdJg2KhpAK8SR3DjMwAdkxj3ZuxV27CprR9LgpeyGmXUbC6wb7ERfvrnKZjXoUmmDznezpbZb7ap6r1D3tgFxHmwMkQTPH\",\"xpub69H7F5d8KSRgmmdJg2KhpAK8SR3DjMwAdkxj3ZuxV27CprR9LgpeyGmXUbC6wb7ERfvrnKZjXoUmmDznezpbZb7ap6r1D3tgFxHmwMkQTPH\"],\"parameters\":{\"velocity_1\":{\"value\":200,\"asset\":\"BTC\",\"period\":86400,\"limited_keys\":[0]},\"delay_1\":60},\"pii\":{\"email\":\"email@email.em\",\"phone\":\"+1 415-415-4155\"}}";

    private KeychainParams getKeychainParams() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KeychainParams keychainParams = objectMapper.readValue(KEYCHAIN_PARAMS, KeychainParams.class);
        return keychainParams;
    }

    private static final String SIGNATURE_REQUEST = "{\"signatureIndex\":1,\"transaction\":{\"bytes\":\"0100000001c8e4ad15df73f0dcc32533e2427decc2aa2c430aeee67323de0035b26ee2525301000000b600483045022100aa7a8e1588ab698b85db2ea58d4bd5ba31dcaf1a116b2cc6ed108ecf51917745022055beb750aa23761dfa40ccb02926044a51bab4facbc647d87034518bc6ca250901004c695221025acb09b1174ce40fc9661299781bf065dc843c957be420ede0bf6ae50221d5f62102fafb94344aa39c0b9efdb10814740365b44781f0813094866505eb28e550161921034856765a2387e1d2ccbcd273b31acf15281cfccb844abc127525a7b0d567ec2b53aeffffffff01f04902000000000017a91454b69fa529c05a07cc98f1bbbe659661fb6e56eb8700000000\",\"inputScripts\":[\"5221025acb09b1174ce40fc9661299781bf065dc843c957be420ede0bf6ae50221d5f62102fafb94344aa39c0b9efdb10814740365b44781f0813094866505eb28e550161921034856765a2387e1d2ccbcd273b31acf15281cfccb844abc127525a7b0d567ec2b53ae\"],\"inputTransactions\":[\"0100000001d9706b4b4053db33debad5b46869f66c15e5cd7985d68a8bde7103dde3be23b8010000008b483045022100ae08f99268535c2fa37891b68dd68ac1a9c0f2e21fb8666af23bcbf9f0570f6402202199410ceae1e51dc95f815acb5a8243791ba4cdb0b89ba464415c809332fc1f014104edb21afd98061325022c8b16ac21f6491cb1271247296121a46012b03e4b18d4283dfb5560b7f05b7e19bc1adea97caddde982dd848960fdd24a644f5039ab27ffffffff02e0220200000000001976a914cf455cbf57cccfd4cbdafbf47cfa17e5879d2b7f88ac400d03000000000017a9142e029e9d22184b5afd0e45ef1d8b695abd401bb18700000000\"]}}";

    private SignatureRequest getSignatureRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SignatureRequest signatureRequest = objectMapper.readValue(SIGNATURE_REQUEST, SignatureRequest.class);
        return signatureRequest;
    }
}
