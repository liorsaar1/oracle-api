/**
 * 
 */
package co.digitaloracle.api;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 * 
 * @author liorsaar
 * 
 */
public class ApiManager {

    private static final String RESULT_CANCELLED = "cancelled";

    /*
     * @see http://hc.apache.org/httpcomponents-asyncclient-4.0.x/quickstart.html
     */
    protected void get(String aUrl, final ApiListener aListener) {
        final HttpGet request = new HttpGet(aUrl);
        // System.out.println(request.getRequestLine());
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
        httpClient.execute(request, new ApiFutureCallback(aListener));
    }

    protected void post(String aUrl, String aJsonString, final ApiListener aListener) throws UnsupportedEncodingException {
        final HttpPost request = new HttpPost(aUrl);
        StringEntity entity = new StringEntity(aJsonString);
        entity.setContentType("application/json");
        request.setEntity(entity);

        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
        httpClient.execute(request, new ApiFutureCallback(aListener));
    }

    protected void requestCompleted(HttpResponse httpResponse, ApiListener apiListener) {
        try {
            // parse
            String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            ApiResponse apiResponse = ApiResponse.create(responseString);
            // api error
            if (apiResponse.isError()) {
                apiListener.onError(apiResponse);
                return;
            }
            // api success
            apiListener.onSuccess(apiResponse);
        } catch (Exception e) {
            // parse error
            requestError(e.getMessage(), apiListener);
        }
    }

    private void requestFailed(Exception aException, ApiListener aListener) {
        requestError(aException.toString(), aListener);
    }

    private void requestCancelled(ApiListener aListener) {
        requestError(RESULT_CANCELLED, aListener);
    }

    private void requestError(String aMessage, ApiListener aListener) {
        ApiResponse apiResponse = ApiResponse.createError(aMessage);
        aListener.onError(apiResponse);
    }

    class ApiFutureCallback implements FutureCallback<HttpResponse> {
        private ApiListener apiListener;

        public ApiFutureCallback(ApiListener apiListener) {
            this.apiListener = apiListener;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.http.concurrent.FutureCallback#cancelled()
         */
        @Override
        public void cancelled() {
            requestCancelled(apiListener);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.http.concurrent.FutureCallback#completed(java.lang.Object)
         */
        @Override
        public void completed(HttpResponse httpResponse) {
            requestCompleted(httpResponse, apiListener);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.http.concurrent.FutureCallback#failed(java.lang.Exception)
         */
        @Override
        public void failed(Exception exception) {
            requestFailed(exception, apiListener);
        }
    }
}
