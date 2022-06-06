package com.example.demoScheduler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.example.demoScheduler.YmlConfig;
import com.example.demoScheduler.exception.BizException;
import com.example.demoScheduler.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoRestService {
    @Autowired
    YmlConfig ymlC;

    @Autowired
    PjtUtil pjtU;

    public String callApiBizActorMap(String br, Map<String, Object> inDs) throws ResourceAccessException, BizException {
        HashMap<String, Object> input_msg = new HashMap<String, Object>();
        input_msg.put("actID", br);
        String brRq = inDs.get("brRq").toString();
        String brRs = inDs.get("brRs").toString();

        input_msg.put("inDTName", brRq);
        input_msg.put("outDTName", brRs);

        if (inDs.get("API_UUID") != null) {
            String _id = inDs.get("API_UUID").toString();
            input_msg.put("_id", _id);
        }

        HashMap<String, Object> refDS = new HashMap<String, Object>();
        String[] arr_brRq = brRq.split(",");
        for (int i = 0; i < arr_brRq.length; i++) {
            String tmp = arr_brRq[i];
            ArrayList<HashMap<String, Object>> arr_input_param = (ArrayList<HashMap<String, Object>>) inDs.get(tmp);
            ArrayList<HashMap<String, Object>> arr_input_param_tmp = new ArrayList<HashMap<String, Object>>();
            if (arr_input_param != null) {
                for (int j = 0; j < arr_input_param.size(); j++) {
                    HashMap<String, Object> input_param_tmp = new HashMap<String, Object>();
                    HashMap<String, Object> input_param = arr_input_param.get(j);
                    for (Map.Entry<String, Object> entry : input_param.entrySet()) {
                        String tmpKey = entry.getKey();
                        String tmpValue = Objects.toString(entry.getValue(), "");

                        if (!pjtU.isEmpty(tmpValue)) {
                            input_param_tmp.put(tmpKey, tmpValue);
                        } else {
                        }
                    }
                    arr_input_param_tmp.add(input_param_tmp);
                }
            }
            refDS.put(tmp, arr_input_param_tmp);
        }
        input_msg.put("refDS", refDS);

        String jsonInString = "";
        try {
            jsonInString = pjtU.ObjectToJsonString(input_msg);
        } catch (JsonProcessingException e1) {
            log.error(e1.getMessage(),e1);
        }

        String jsonOutString = null;
        HashMap<String, Object> result = new HashMap<String, Object>();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        // factory.setConnectTimeout(100000); // 타임아웃 설정 5초
        // factory.setReadTimeout(100000);// 타임아웃 설정 5초
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // log.info("jsonInString last =>" + jsonInString);
        HttpEntity<?> entity = new HttpEntity<>(jsonInString, headers);
        //log.info("ymlC.getApiurlbizactor() =>" + ymlC.getApiurlbizactor());
        String url = ymlC.getApiurlbizactor();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        ResponseEntity<String> resultMap = null;
        try {
            resultMap = restTemplate.exchange(uriBuilder.build().toString(), HttpMethod.POST, entity, String.class);
        } catch (ResourceAccessException e) {
            log.error(e.getMessage(),e);
            throw new ResourceAccessException("제한시간이 100초가 초과되었습니다.");
            // 이렇게 에러를 던지는게 아니라 결과셋을 보내야한다.
        }

        int statusCode = resultMap.getStatusCodeValue();
        String body = resultMap.getBody();
        // log.info("statusCode=>" + statusCode);
        // log.info("header=>" + resultMap.getHeaders());
        // log.info("body=>" + body);

        HashMap<String, Object> out_msg = null;
        try {
            out_msg = pjtU.JsonStringToObject(body, HashMap.class);
        } catch (JsonMappingException e) {
            log.error(e.getMessage(),e);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }

        if (statusCode == 200 && out_msg.get("status").toString().equals("SUCCESS")) {
            try {
                jsonOutString = pjtU.ObjectToJsonString(out_msg.get("result"));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(),e);
            }
        } else {
            // throw new BizException(out_msg.get("message").toString());
            throw new BizException(out_msg.get("message").toString());
        }
        return jsonOutString;
    }
}
