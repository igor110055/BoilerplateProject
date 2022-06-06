package com.example.coin.util;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.framework.exception.BizException;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class HttpUtilUpbit {
    public String httpGetUpbit(String URL,List nameValuePairs) throws URISyntaxException, ClientProtocolException, IOException, BizException{
    	//QUOTATION API

		/*
		Websocket 연결 요청 수 제한
		초당 5회, 분당 100회
		REST API 요청 수 제한
		분당 600회, 초당 10회 (종목, 캔들, 체결, 티커, 호가별)
		*/
		/*
		[Quotation API 추가 안내 사항]
		. Quotation API의 요청 수 제한은 IP 주소를 기준으로 합니다.
		. 향후 안정적인 서비스 제공을 위하여 API 요청 수는 추가적인 조정이 이루어질 수 있습니다. 요청 수 조정 필요 시 별도 공지를 통해 안내드리겠습니다.
		. 초당 제한 조건과 분당 제한 조건 중 하나의 조건이라도 요청 수를 초과할 경우 요청 수 제한 적용 됩니다.
		. 요청 수 제한 조건에 적용되는 시간 조건은 첫 요청 시간을 기준으로하며, 일정 시간 이후 초기화됩니다.(실패한 요청은 요청 횟수에 포함되지 않습니다.)
		. 다수의 REST API 요청이 필요하신 경우, 웹소켓을 통한 수신 부탁드립니다.

		앞으로 더욱 안정적이고 고도화된 서비스 제공을 위하여 노력하는 업비트 개발자 센터가 될 수 있도록 노력하겠으며,
		추후 요청 수 제한 기준의 변경이 있을 경우, 공지를 통하여 안내해 드릴 수 있도록 하겠습니다.
		*/
		/*
		[Exchange API 잔여 요청 수 확인 방법]
		업비트 Open API 서비스는 원활한 사용 환경을 위해 초당 / 분당 요청 수를 제한하고 있습니다.
		Open API 호출 시 남아있는 요청 수는 Remaining-Req 응답 해더를 통해 확인 가능합니다.

		Remaining-Req: group=default; min=1799; sec=29
		위와 같은 포멧의 응답 해더를 수신했다면, default 라는 그룹에 대하여 해당 초에 29개의 요청, 남은 1분간 1799개의 요청이 가능하다는 것을 의미합니다.

		주문하기 Open API의 경우,

		Remaining-Req: group=order; min=59; sec=4
		위와 같은 응답이 올 수 있으며, 이는 order 라는 그룹에 대해 해당 초에 4번, 남은 1분은 59번의 주문 요청이 가능하다는 것을 의미합니다.
		*/
		/*
		해당 시간 내 초과된 요청에 대해서 429 Too Many Requests 오류가 발생할 수 있습니다. 하지만 별도의 추가적인 페널티는 부과되지 않습니다.
		*/
        String rtn="";
        String remaining_req="";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL);
        httpGet.addHeader("Content-type", "application/json; charset=UTF-8");
        URIBuilder uriBuilder = new URIBuilder(httpGet.getURI());

        if(nameValuePairs!=null && nameValuePairs.size()>0){
            uriBuilder.addParameters(nameValuePairs);
        }
        URI uri =uriBuilder.build();
        System.out.println("uri = "+uri.toString());
        HttpRequestBase req =httpGet;
        req.setURI(uri);
        try (CloseableHttpResponse response = client.execute(httpGet)){
            Header[] arr_header =response.getHeaders("remaining-req");
            System.out.print("arr_header.length =");
            System.out.println(arr_header.length);
            if(arr_header.length>0){
                remaining_req =arr_header[0].toString();
            }
            StatusLine tmp_status= response.getStatusLine();
            int status_code = tmp_status.getStatusCode();
            System.out.println("status_code : "+ status_code);


            System.out.println("URL:"+URL);
            System.out.println("request.getURI().getRawQuery():"+req.getURI().getRawQuery());
            rtn = EntityUtils.toString(response.getEntity(), "UTF-8");


            System.out.println("remaining_req:"+remaining_req);
            System.out.println("rtn:"+rtn);

            if(status_code==404){
                PjtUtil.g();
				HashMap<String,Object> tmp_error=PjtUtil.g().JsonStringToObject(rtn, HashMap.class);
                HashMap<String,Object> tmp_error2=(HashMap<String,Object>)tmp_error.get("error");
                throw new com.example.framework.exception.BizException("error :"+tmp_error2.get("message"));
            }
        }
        //remaining-req: group=candles; min=599; sec=9
        //분당 600회, 초당 10회


        String [] arr_tmp =remaining_req.split(";");
        String group = arr_tmp[0].replace("group=", "").trim();
        String min = arr_tmp[1].replace("min=", "").trim();
        String sec = arr_tmp[2].replace("sec=", "").trim();

        int i_sec= Integer.parseInt(sec);

        if(i_sec==0){
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e ){

            }
        }

        return  rtn;
	}

    public String httpGetUpbitExchangeApi(String UPBIT_OPEN_API_ACCESS_KEY, String UPBIT_OPEN_API_SECRET_KEY, String URL) throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, BizException{
        String rtn="";
        String remaining_req="";
        String accessKey = UPBIT_OPEN_API_ACCESS_KEY;
        String secretKey = UPBIT_OPEN_API_SECRET_KEY;

        System.out.println("전체 OS 환경변수 값 : " + System.getenv());
        System.out.println("accessKey = " +accessKey);
        System.out.println("secretKey = " +secretKey);

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);
        String authenticationToken = "Bearer " + jwtToken;
        int status_code = 0;
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(URL);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            Header[] arr_header =response.getHeaders("remaining-req");
            System.out.print("arr_header.length =");
            System.out.println(arr_header.length);
            if(arr_header.length>0){
                remaining_req =arr_header[0].toString();
            }
            org.apache.http.HttpEntity entity = response.getEntity();
            status_code = response.getStatusLine().getStatusCode();
            rtn = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String [] arr_tmp =remaining_req.split(";");
        String group = arr_tmp[0].replace("group=", "").trim();
        String min = arr_tmp[1].replace("min=", "").trim();
        String sec = arr_tmp[2].replace("sec=", "").trim();

        int i_sec= Integer.parseInt(sec);

        if(i_sec==0){
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e ){

            }
        }
        System.out.print("status code ="+status_code);
        if(status_code>=400 && status_code <500){
            //에러
            /*
            Object	error
            String	error.name
            String	error.message
            {"error":{"message":"권한이 부족합니다.","name":"out_of_scope"}}
            */
            throw new com.example.framework.exception.BizException(rtn);
        }
        return  rtn;
	}

    public String httpGetUpbitExchangeApi(String UPBIT_OPEN_API_ACCESS_KEY,String UPBIT_OPEN_API_SECRET_KEY, String URL, String queryString) throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, BizException{
        String rtn="";
        String remaining_req="";
        String accessKey = UPBIT_OPEN_API_ACCESS_KEY;
        String secretKey = UPBIT_OPEN_API_SECRET_KEY;

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));
        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);
        String authenticationToken = "Bearer " + jwtToken;
        int status_code = 0;
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            if(!PjtUtil.g().isEmpty(queryString)){
                URL=URL+"?"+queryString;
            }
            System.out.println("aaaaaaaaaaaa");
            System.out.println(URL);
            System.out.println("bbbbbbbbbbbb");
            HttpGet request = new HttpGet(URL);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            Header[] arr_header =response.getHeaders("remaining-req");
            status_code = response.getStatusLine().getStatusCode();
            System.out.print("arr_header.length =");
            System.out.println(arr_header.length);
            if(arr_header.length>0){
                remaining_req =arr_header[0].toString();

                String [] arr_tmp =remaining_req.split(";");
                String group = arr_tmp[0].replace("group=", "").trim();
                String min = arr_tmp[1].replace("min=", "").trim();
                String sec = arr_tmp[2].replace("sec=", "").trim();

                int i_sec= Integer.parseInt(sec);

                if(i_sec==0){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e ){

                    }
                }
            }
            org.apache.http.HttpEntity entity = response.getEntity();
            rtn = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.print("status code ="+status_code);
        if(status_code>=400 && status_code <500){
            //에러
            /*
            Object	error
            String	error.name
            String	error.message
            {"error":{"message":"권한이 부족합니다.","name":"out_of_scope"}}
            */
            throw new com.example.framework.exception.BizException(rtn);
        }


        return  rtn;
	}

    public String httpDelUpbitExchangeApi(String  UPBIT_OPEN_API_ACCESS_KEY, 
    		String UPBIT_OPEN_API_SECRET_KEY ,
    		String URL, String queryString) throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, BizException{
        String rtn="";
        String remaining_req="";
        //String accessKey = System.getenv("UPBIT_OPEN_API_ACCESS_KEY");
        //String secretKey = System.getenv("UPBIT_OPEN_API_SECRET_KEY");
        String accessKey = UPBIT_OPEN_API_ACCESS_KEY;
        String secretKey = UPBIT_OPEN_API_SECRET_KEY;

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));
        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);
        String authenticationToken = "Bearer " + jwtToken;
        int status_code = 0;
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            if(!PjtUtil.g().isEmpty(queryString)){
                URL=URL+"?"+queryString;
            }
            HttpDelete request = new HttpDelete(URL);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            Header[] arr_header =response.getHeaders("remaining-req");
            System.out.print("arr_header.length =");
            System.out.println(arr_header.length);
            if(arr_header.length>0){
                remaining_req =arr_header[0].toString();
            }
            status_code = response.getStatusLine().getStatusCode();
            org.apache.http.HttpEntity entity = response.getEntity();
            rtn = EntityUtils.toString(entity, "UTF-8");



        } catch (IOException e) {
            e.printStackTrace();
        }

        String [] arr_tmp =remaining_req.split(";");
        String group = arr_tmp[0].replace("group=", "").trim();
        String min = arr_tmp[1].replace("min=", "").trim();
        String sec = arr_tmp[2].replace("sec=", "").trim();

        int i_sec= Integer.parseInt(sec);

        if(i_sec==0){
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e ){

            }
        }
        System.out.print("status code ="+status_code);
        if(status_code>=400 && status_code <500){
            //에러
            /*
            Object	error
            String	error.name
            String	error.message
            {"error":{"message":"권한이 부족합니다.","name":"out_of_scope"}}
            */
            throw new com.example.framework.exception.BizException(rtn);
        }
        return  rtn;
	}

    public String httpPostUpbitExchangeApi(String UPBIT_OPEN_API_ACCESS_KEY,String UPBIT_OPEN_API_SECRET_KEY,String URL,  HashMap<String, String> params) throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, com.example.framework.exception.BizException{
        String rtn="";
        String remaining_req="";
        String accessKey = UPBIT_OPEN_API_ACCESS_KEY;
        String secretKey = UPBIT_OPEN_API_SECRET_KEY;

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));


        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));
        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);
        String authenticationToken = "Bearer " + jwtToken;

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            /* post 전송이므로 빠져야한다.
            if(!PjtUtil.isEmpty(queryString)){
                URL=URL+"?"+queryString;
            }
            */
            HttpPost request = new HttpPost(URL);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);
            request.setEntity(new StringEntity(new Gson().toJson(params)));


            System.out.println("accessKey:"+accessKey);
            System.out.println("secretKey:"+secretKey);

            System.out.println("URL:"+URL);
            System.out.println("request.getURI().getRawQuery():"+request.getURI().getRawQuery());

            HttpResponse response = client.execute(request);
            StatusLine tmp_status =response.getStatusLine();
            int status_code = tmp_status.getStatusCode();
            System.out.println("status_code : "+ status_code);

            Header[] arr_header =response.getHeaders("remaining-req");
            org.apache.http.HttpEntity entity = response.getEntity();
            rtn = EntityUtils.toString(entity, "UTF-8");
            System.out.println("rtn:"+rtn);
            if(status_code==404){
                throw new BizException("페이지 주소가 잘못되었다. URL :"+URL);
            }

            if((status_code==400) || (status_code==401)){
                PjtUtil.g();
				HashMap<String,Object> tmp_error=PjtUtil.g().JsonStringToObject(rtn, HashMap.class);
                HashMap<String,Object> tmp_error2=(HashMap<String,Object>)tmp_error.get("error");
                throw new BizException("error :"+tmp_error2.get("message"));
            }




            System.out.print("arr_header.length =");
            System.out.println(arr_header.length);
            if(arr_header.length>0){
                remaining_req =arr_header[0].toString();
            }

            System.out.print("remaining_req:");
            System.out.println(remaining_req);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String [] arr_tmp =remaining_req.split(";");
        String group = arr_tmp[0].replace("group=", "").trim();
        String min = arr_tmp[1].replace("min=", "").trim();
        String sec = arr_tmp[2].replace("sec=", "").trim();

        int i_sec= Integer.parseInt(sec);

        if(i_sec==0){
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e ){

            }
        }

        return  rtn;
	}

    
    public static String GetUpbitErrMsg(String  err) throws Exception {
    	String jsonStr =err;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.setSerializationInclusion(Include.NON_NULL);
	    objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	    Map<String, Object> map = new HashMap<>();
	    map = objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {

	    });
	    System.out.println(err);
	    //{"error":{"message":"등록된 출금 주소가 아닙니다.","name":"withdraw_address_not_registered"}}
	    HashMap<String,String> tmp= (HashMap<String, String>) map.get("error");
	    
	    String rtn="";
	    if(tmp.get("message")!=null) {
	    	rtn = "msg->"+tmp.get("message");
	    	System.out.println(tmp.get("message"));
	    }
	    if(tmp.get("name")!=null) {
	    	rtn = rtn+",name->"+ tmp.get("name");
	    	System.out.println(tmp.get("name"));
	    }
	    
        return rtn;
    }
}
