/**
 * 
 */
package co.digitaloracle.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 * 
 * @author liorsaar
 * 
 */
public class KeychainParams {
    public String rulesetId; // ['default']: unique id of the ruleset for this keychain
    public ArrayList<String> keys;
    public HashMap<String, Object> parameters;
    public HashMap<String, Object> pii;
    public String walletAgent;
}
