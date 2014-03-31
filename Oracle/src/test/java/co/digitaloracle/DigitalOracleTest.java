/**
 *
 */
package co.digitaloracle;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import co.digitaloracle.api.ApiListener;
import co.digitaloracle.api.ApiResponse;
import co.digitaloracle.model.KeychainParams;
import co.digitaloracle.model.SignatureRequest;
import co.digitaloracle.model.Transaction;

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

    private Transaction getTransactionRequestDeferred() throws Exception {
        return (Transaction) Util.getJson("signTxRequestDeferred.json", Transaction.class);
    }

    @Test
    public void testDeferral() throws Exception {
        Transaction transaction = getTransactionRequestDeferred();
        String keychainId = "6408110e-900b-4dfd-891b-62ed0478ea4b";

        ApiResponse apiResponse = ApiResponse.create(Util.getResourceString("signTxDeferral.json"));
        // deferral
        if (apiResponse.result.equals(ApiResponse.RESULT_DEFERRED)) {
            // delay
            if (apiResponse.deferral.reason.equals(ApiResponse.DEFERRAL_REASON_DELAY)) {
                // "2014-03-28T16:31:43Z"
                DateFormat df = new SimpleDateFormat(ApiResponse.DATE_FORMAT_PATTERN, Locale.ENGLISH);
                long serverNowMilli = df.parse(apiResponse.now).getTime();
                long untilMilli = df.parse(apiResponse.deferral.until).getTime();
                long delayPeriod = untilMilli-serverNowMilli;
                assertEquals("delayPeriod", 59*1000, delayPeriod);
                // delayed resubmission here
            }
        }
    }
}
