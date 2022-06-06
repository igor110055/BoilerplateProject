package com.example.coin.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import com.example.framework.exception.BizException;
import com.example.framework.utils.PjtUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtilBinance {
    public String httpGetBinianceGetUrlNoAuth(String URL, String QueryString)
            throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, BizException {
        String rtn = "";
        try {

            if (!PjtUtil.g().isEmpty(QueryString)) {
                URL = URL + "?" + QueryString;
            }

            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(URL);
            // request.setHeader("Content-Type", "application/json");
            HttpResponse response = client.execute(request);
            org.apache.http.HttpEntity entity = response.getEntity();
            rtn = EntityUtils.toString(entity, "UTF-8");
            int status_code = response.getStatusLine().getStatusCode();
            System.out.println("status_code=>" + status_code);
            if (status_code >= 400 && status_code < 500) {
                // 에러
                /*
                 * Object error String error.name String error.message
                 * {"error":{"message":"권한이 부족합니다.","name":"out_of_scope"}}
                 */
                throw new com.example.framework.exception.BizException(rtn);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rtn;
    }

    public String httpDeleteBiniance(String BINANCE_API_KEY, String BINANCE_API_SECRET, String URL, String queryString)
            throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, BizException {
        String rtn = "";
        String accessKey = BINANCE_API_KEY;
        String secretKey = BINANCE_API_SECRET;

        String signature = HmacSHA256Signer.sign(queryString, secretKey);

        URL = URL + "?" + queryString + "&signature=" + signature;
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            System.out.println(URL);
            HttpDelete request = new HttpDelete(URL);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("X-MBX-APIKEY", accessKey);

            HttpResponse response = client.execute(request);
            org.apache.http.HttpEntity entity = response.getEntity();
            rtn = EntityUtils.toString(entity, "UTF-8");
            int status_code = response.getStatusLine().getStatusCode();
            System.out.println("status_code=>" + status_code);
            if (status_code >= 400 && status_code < 500) {
                throw new com.example.framework.exception.BizException(rtn);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rtn;
    }

    public String httpPostBiniance(String BINANCE_API_KEY, String BINANCE_API_SECRET, String URL, String queryString)
            throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, BizException {
        // https://github.com/binance/binance-signature-examples/blob/master/java/Spot.java
        // httpRequest.sendSignedRequest(parameters, "/api/v3/order", "POST");
        String rtn = "";
        String accessKey = BINANCE_API_KEY;
        String secretKey = BINANCE_API_SECRET;

        // https://github.com/binance-exchange/binance-java-api/blob/master/src/main/java/com/binance/api/client/security/HmacSHA256Signer.java

        String signature = HmacSHA256Signer.sign(queryString, secretKey);

        URL = URL + "?" + queryString + "&signature=" + signature;
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            System.out.println(URL);
            HttpPost request = new HttpPost(URL);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("X-MBX-APIKEY", accessKey);

            HttpResponse response = client.execute(request);
            org.apache.http.HttpEntity entity = response.getEntity();
            rtn = EntityUtils.toString(entity, "UTF-8");
            int status_code = response.getStatusLine().getStatusCode();
            System.out.println("status_code=>" + status_code);
            if (status_code >= 400 && status_code < 500) {
                // 에러
                /*
                 * Object error String error.name String error.message
                 * {"error":{"message":"권한이 부족합니다.","name":"out_of_scope"}}
                 */
                throw new com.example.framework.exception.BizException(rtn);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rtn;
    }

    public String httpGetBiniance(String BINANCE_API_KEY, String BINANCE_API_SECRET, String URL, String queryString)
            throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, BizException {
        String rtn = "";
        String accessKey = BINANCE_API_KEY;
        String secretKey = BINANCE_API_SECRET;

        // https://github.com/binance-exchange/binance-java-api/blob/master/src/main/java/com/binance/api/client/security/HmacSHA256Signer.java
        String signature = HmacSHA256Signer.sign(queryString, secretKey);

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            if (!PjtUtil.g().isEmpty(queryString)) {
                URL = URL + "?" + queryString + "&signature=" + signature;
                // URL=URL+"?"+queryString;
            }
            System.out.println(URL);
            HttpGet request = new HttpGet(URL);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("X-MBX-APIKEY", accessKey);

            HttpResponse response = client.execute(request);
            org.apache.http.HttpEntity entity = response.getEntity();
            rtn = EntityUtils.toString(entity, "UTF-8");
            int status_code = response.getStatusLine().getStatusCode();
            System.out.println("status_code=>" + status_code);
            if (status_code >= 400 && status_code < 500) {
                // 에러
                /*
                 * Object error String error.name String error.message
                 * {"error":{"message":"권한이 부족합니다.","name":"out_of_scope"}}
                 */
                throw new com.example.framework.exception.BizException(rtn);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rtn;
    }

    public String httpGetBinianceWithApiKey(String BINANCE_API_KEY, String URL, String queryString)
            throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, BizException {
        String rtn = "";
        String accessKey = BINANCE_API_KEY;
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            if (!PjtUtil.g().isEmpty(queryString)) {
                URL = URL + "?" + queryString;
            }
            System.out.println(URL);
            HttpGet request = new HttpGet(URL);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("X-MBX-APIKEY", accessKey);

            HttpResponse response = client.execute(request);
            org.apache.http.HttpEntity entity = response.getEntity();
            rtn = EntityUtils.toString(entity, "UTF-8");
            int status_code = response.getStatusLine().getStatusCode();
            System.out.println("status_code=>" + status_code);
            if (status_code >= 400 && status_code < 500) {
                // 에러
                /*
                 * Object error String error.name String error.message
                 * {"error":{"message":"권한이 부족합니다.","name":"out_of_scope"}}
                 */
                throw new com.example.framework.exception.BizException(rtn);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rtn;
    }
}
