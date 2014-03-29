/**
 *
 */
package co.digitaloracle;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.CountDownLatch;

import co.digitaloracle.api.ApiListener;
import co.digitaloracle.api.ApiResponse;
import co.digitaloracle.model.KeychainParams;
import co.digitaloracle.model.SignatureRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 *
 * @author liorsaar
 */
public class DigitalOracleTest {

    private static final String DO_SANDBOX_URL = "https://s.digitaloracle.co";

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
        final ApiResponse[] apiResponse = {null};

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
        final ApiResponse[] apiResponse = {null};

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
     * Test method for {@link co.digitaloracle.DigitalOracle#signTx(java.lang.String, co.digitaloracle.model.SignatureRequest, co.digitaloracle.api.ApiListener)}.
     *
     * @throws Exception
     */
    @Test
    public void testSignTx() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final ApiResponse[] apiResponse = {null};

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
        KeychainParams keychainParams = getKeychainParams();
        assertEquals("rulesetId", "default", keychainParams.rulesetId);
        assertEquals("keys", 2, keychainParams.keys.size());
    }

    private KeychainParams getKeychainParams() throws Exception {
        return (KeychainParams) Util.getJson("keychainParams.json", KeychainParams.class);
    }

    private SignatureRequest getSignatureRequest() throws Exception {
        return (SignatureRequest) Util.getJson("signTxRequest.json", SignatureRequest.class);
    }


//    @Test
//    public void testDeferral() throws Exception {
//        Transaction transaction = getTransactionResponseDeferred();
//        String keychainId = "6408110e-900b-4dfd-891b-62ed0478ea4b";
//
//        ApiResponse apiResponse = ApiResponse.create(Util.getResourceString("signTxDeferral.json"));
//        // deferral
//        if (apiResponse.result.equals(ApiResponse.RESULT_DEFERRED)) {
//            // delay
//            if (apiResponse.deferral.get(ApiResponse.DEFERRAL_REASON).equals(ApiResponse.DEFERRAL_REASON_DELAY)) {
//                // "2014-03-28T16:31:43Z"
//                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.ENGLISH);
//                Date date = df.parse(apiResponse.now);
//                long serverNowMilli = date.getTime();
//                date = df.parse((String) apiResponse.deferral.get("until"));
//                long untilMilli = date.getTime();
//                long delayPeriod = untilMilli-serverNowMilli;
//
////                var timeout = untilMilli - serverNowMilli;
////                // resubmit after timeout
////                CCOracle.SetDelayedResubmission(timeout, payload);
////
//            }
//        }
//    }

}
/*
{"result":"deferred","transaction":null,"deferral":{"until":"2014-03-28T05:35:43Z","reason":"delay","verifications":null},"now":"2014-03-28T05:34:44Z"}"

{"signatureIndex":1,"transaction":{"bytes":"0100000001c8e4ad15df73f0dcc32533e2427decc2aa2c430aeee67323de0035b26ee2525301000000b50047304402205893879e553699fa399837f0a60ae7b6bb805e0f2f04411d6e17214f7f5b81100220347ce844dba36cdcc3ee4170c04162eac28789a2b9889d480317d24e9b4057eb01004c695221025acb09b1174ce40fc9661299781bf065dc843c957be420ede0bf6ae50221d5f62102fafb94344aa39c0b9efdb10814740365b44781f0813094866505eb28e550161921034856765a2387e1d2ccbcd273b31acf15281cfccb844abc127525a7b0d567ec2b53aeffffffff01f04902000000000017a91454b69fa529c05a07cc98f1bbbe659661fb6e56eb8700000000","inputScripts":["5221025acb09b1174ce40fc9661299781bf065dc843c957be420ede0bf6ae50221d5f62102fafb94344aa39c0b9efdb10814740365b44781f0813094866505eb28e550161921034856765a2387e1d2ccbcd273b31acf15281cfccb844abc127525a7b0d567ec2b53ae"],"inputTransactions":["0100000001d9706b4b4053db33debad5b46869f66c15e5cd7985d68a8bde7103dde3be23b8010000008b483045022100ae08f99268535c2fa37891b68dd68ac1a9c0f2e21fb8666af23bcbf9f0570f6402202199410ceae1e51dc95f815acb5a8243791ba4cdb0b89ba464415c809332fc1f014104edb21afd98061325022c8b16ac21f6491cb1271247296121a46012b03e4b18d4283dfb5560b7f05b7e19bc1adea97caddde982dd848960fdd24a644f5039ab27ffffffff02e0220200000000001976a914cf455cbf57cccfd4cbdafbf47cfa17e5879d2b7f88ac400d03000000000017a9142e029e9d22184b5afd0e45ef1d8b695abd401bb18700000000"]}}

 */