/**
 * 
 */
package co.digitaloracle;

import com.github.shaxbee.uuid.UUID;
import com.github.shaxbee.uuid.UUID.Namespace;

import java.io.IOException;

import co.digitaloracle.api.ApiListener;
import co.digitaloracle.api.ApiManager;
import co.digitaloracle.api.ApiResponse;
import co.digitaloracle.model.KeychainParams;
import co.digitaloracle.model.SignatureRequest;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 * 
 * @author liorsaar
 * 
 */
public class DigitalOracle extends ApiManager {

    private String hostUrl;

    public DigitalOracle(String hostUrl) {
        if (hostUrl == null)
            throw new NullPointerException("@param:hostUrl");
        this.hostUrl = hostUrl;
    }

    /**
     * @param keychainId
     * @param listener
     */
    public void getKeychain(String keychainId, ApiListener listener) {
        if (keychainId == null)
            throw new NullPointerException("@param:keychainId");
        String keychainUrl = getKeychainUrl(keychainId);
        get(keychainUrl, listener);
    }

    /**
     * @param keychainParams
     * @param listener
     * @throws IOException
     */
    public void createKeychain(KeychainParams keychainParams, ApiListener listener) throws IOException {
        String key = keychainParams.keys.get(0);
        String keychainId = getKeychainId(key);
        String keychainUrl = getKeychainUrl(keychainId);
        post(keychainUrl, ApiResponse.toJsonString(keychainParams), listener);
    };

    /**
     * @param keychainId
     * @param signatureRequest
     * @param listener
     * @throws IOException
     */
    public void signTx(String keychainId, SignatureRequest signatureRequest, ApiListener listener) throws IOException {
        String keychainUrl = getKeychainTxUrl(keychainId);
        post(keychainUrl, ApiResponse.toJsonString(signatureRequest), listener);
    }

    /**
     * @param key
     * @return UUID v5 from urn+key
     * @see <a href="https://cryptocorp.co/api/"/>
     */
    private String getKeychainId(String key) {
        String name = "urn:digitaloracle.co:" + key;
        UUID uuid = UUID.uuid5(Namespace.DNS, name.getBytes());
        return uuid.hex();
    }

    private String getKeychainUrl(String keychinId) {
        return hostUrl + "/keychains/" + keychinId;
    }

    private String getKeychainTxUrl(String keychinId) {
        return getKeychainUrl(keychinId) + "/transactions";
    }
}
