/**
 *
 */
package co.digitaloracle.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 *
 * @author liorsaar
 */
public class ApiManager {

    /*
     * @see http://hc.apache.org/httpcomponents-asyncclient-4.0.x/quickstart.html
     */
    protected void get(String url, final ApiListener listener) {
        final HttpGet request = new HttpGet(url);
        // System.out.println(request.getRequestLine());
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
        httpClient.execute(request, new ApiFutureCallback(listener));
    }

    protected void post(String url, String jsonString, final ApiListener listener) throws UnsupportedEncodingException {
        final HttpPost request = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonString);
        entity.setContentType("application/json");
        request.setEntity(entity);

        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
        httpClient.execute(request, new ApiFutureCallback(listener));
    }

    protected void requestCompleted(HttpResponse httpResponse, ApiListener apiListener) {
        try {
            // check http status
            if (httpResponse.getStatusLine().getStatusCode() == 500) {
                requestError(httpResponse.getStatusLine().toString(), apiListener);
                return;
            }
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

    private void requestFailed(Exception exception, ApiListener listener) {
        requestError(exception.toString(), listener);
    }

    private void requestCancelled(ApiListener listener) {
        requestError(ApiResponse.RESULT_CANCELLED, listener);
    }

    private void requestError(String message, ApiListener listener) {
        ApiResponse apiResponse = ApiResponse.createError(message);
        listener.onError(apiResponse);
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
