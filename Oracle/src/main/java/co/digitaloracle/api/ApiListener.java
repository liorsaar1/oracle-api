/**
 * 
 */
package co.digitaloracle.api;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 * 
 * @author liorsaar
 * 
 */
public interface ApiListener {
    void onSuccess(ApiResponse apiResponse);

    void onError(ApiResponse apiResponse);
}
