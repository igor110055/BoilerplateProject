package com.example.framework.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.example.framework.exception.BizException;
import com.google.gson.Gson;

public class HttpUtil {

    public String httpPostApi(String URL,  HashMap<String, String> params) throws URISyntaxException, ClientProtocolException, IOException, NoSuchAlgorithmException, com.example.framework.exception.BizException{
        String rtn="";
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            /* post 전송이므로 빠져야한다.
            if(!PjtUtil.isEmpty(queryString)){
                URL=URL+"?"+queryString;
            }
            */

            StringEntity body = new StringEntity(new Gson().toJson(params),"UTF-8");
            HttpPost request = new HttpPost(URL);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("charset", "UTF-8");
                     
            request.setEntity(body);
           

            System.out.println("body:"+ body);
            System.out.println("URL:"+URL);
            System.out.println("request.getURI().getRawQuery():"+request.getURI().getRawQuery());

            HttpResponse response = client.execute(request);
            StatusLine tmp_status =response.getStatusLine();
            int status_code = tmp_status.getStatusCode();
            System.out.println("status_code : "+ status_code);
            org.apache.http.HttpEntity entity = response.getEntity();
            rtn = EntityUtils.toString(entity, "UTF-8");
            System.out.println("rtn:"+rtn);
            if(status_code==404){
                throw new BizException("페이지 주소가 잘못되었다. URL :"+URL);
            }

            if((status_code==400) || (status_code==401)){
                PjtUtil.g();
				HashMap<String,Object> tmp_error=PjtUtil.g().JsonStringToObject(rtn, HashMap.class);
                throw new BizException("error :"+tmp_error.get("msg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  rtn;
	}
}
