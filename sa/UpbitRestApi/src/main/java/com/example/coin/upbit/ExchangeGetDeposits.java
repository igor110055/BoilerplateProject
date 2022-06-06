package com.example.coin.upbit;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.coin.util.HttpUtilUpbit;
import com.example.framework.exception.BizException;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import dataset.*;
import dataset.type.*;
import running.common.SAProxy;
public class ExchangeGetDeposits extends SAProxy {

    public DataSet GetDeposits(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	String  URL="https://api.upbit.com/v1/deposits";
    	DataSet OUT_DS = new DataSet();
    	/*상태 */
    	DataTable OUT_RST = OUT_DS.addTable("OUT_RST");
    	OUT_RST.addColumn("URL");
    	OUT_RST.addColumn("QUERY_STRING");
    	OUT_RST.addColumn("JSON_OUT");
    	OUT_RST.addColumn("STATUS");  // E에러   S 성공
    	OUT_RST.addColumn("ERR_MSG");
    	OUT_RST.addColumn("ERR_CODE");  //100  외부 api 오류  200 내부오류
    	OUT_RST.addColumn("ERR_STACK_TRACE");
    	
    	DataTable OUT_RSET = OUT_DS.addTable("OUT_RSET");

    	OUT_RSET.addColumn("TYPE");//입출금 종류
    	OUT_RSET.addColumn("UUID");//입금의 고유 아이디
    	OUT_RSET.addColumn("CURRENCY");//화폐를 의미하는 영문 대문자 코드
    	OUT_RSET.addColumn("TXID");//입금의 트랜잭션 아이디
    	OUT_RSET.addColumn("STATE");//입금 상태
    	OUT_RSET.addColumn("CREATED_AT");//입금 생성 시간
    	OUT_RSET.addColumn("DONE_AT");//입금 완료 시간
    	OUT_RSET.addColumn("AMOUNT", dataset.type.DataType.BIGDECIMAL,null,"입금 수량");
    	OUT_RSET.addColumn("FEE", dataset.type.DataType.BIGDECIMAL,null,"입금 수수료");
    	OUT_RSET.addColumn("TRANSACTION_TYPE");//입금 유형
    	/*
    	 * default : 일반입금
		   internal : 바로입금
    	 * */
    	
    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String CURRENCY =null;
    	String STATE =null;
    	String LIMIT =null;
    	String PAGE =null;
    	String ORDER_BY =null;
    	if(IN_PSET.getRowCount()>0) {
    		CURRENCY = IN_PSET.getRow(0).getStringNullToEmpty("CURRENCY");
    		STATE = IN_PSET.getRow(0).getStringNullToEmpty("STATE");
    		LIMIT = IN_PSET.getRow(0).getStringNullToEmpty("LIMIT");
    		PAGE = IN_PSET.getRow(0).getStringNullToEmpty("PAGE");
    		ORDER_BY = IN_PSET.getRow(0).getStringNullToEmpty("ORDER_BY");
    	}
    	HashMap<String, String> params = new HashMap<>();
		if(!PjtUtil.g().isEmpty(CURRENCY)){
			params.put("currency",CURRENCY);
		}
		if(!PjtUtil.g().isEmpty(STATE)){
			params.put("state",STATE);
		}
		
    	
	    if(PjtUtil.g().isEmpty(ORDER_BY)){
	    	ORDER_BY="desc";
	    }
		
		params.put("limit",LIMIT);
	    params.put("page",PAGE);
	    params.put("order_by",ORDER_BY);
        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }
        
        DataTable IN_TXID =InDs.getTable("IN_TXID");
        if(IN_TXID!=null) {
        	for(int i=0;i<IN_TXID.getRowCount();i++) {
            	String TXID=IN_TXID.getRow(i).getString("TXID");
            	if(!PjtUtil.g().isEmpty(TXID)) {
            		queryElements.add("txids[]=" + TXID);
            	}
            }	
        }
        
	    DataTable IN_UUID =InDs.getTable("IN_UUID");
	    if(IN_UUID!=null) {
	        for(int i=0;i<IN_UUID.getRowCount();i++) {
	        	String UUID=IN_UUID.getRow(i).getString("UUID");
	        	if(!PjtUtil.g().isEmpty(UUID)) {
	        		queryElements.add("uuids[]=" + UUID);
	        	}
	        }
        }
        String queryString = String.join("&", queryElements.toArray(new String[0]));
        
    	DataTable dtKey =InDs.getTable("IN_KEY");
    	String UPBIT_OPEN_API_ACCESS_KEY =null;
    	String UPBIT_OPEN_API_SECRET_KEY =null;
    	if(dtKey.getRowCount()>0) {
    		UPBIT_OPEN_API_ACCESS_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_ACCESS_KEY");
    		UPBIT_OPEN_API_SECRET_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_SECRET_KEY");
    	}
    	DataRow drRst = OUT_RST.addRow();
    	drRst.setString("URL", URL);

	    
	    if(PjtUtil.g().isEmpty(UPBIT_OPEN_API_ACCESS_KEY)){
            //에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "UPBIT_OPEN_API_ACCESS_KEY-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "201");
			drRst.setString("ERR_STACK_TRACE", "");
			
			return OUT_DS;
	    }
	
	    if(PjtUtil.g().isEmpty(UPBIT_OPEN_API_SECRET_KEY)){
	        //에러처리
	    	drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "UPBIT_OPEN_API_SECRET_KEY-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "202");
			drRst.setString("ERR_STACK_TRACE", "");
			
			return OUT_DS;
	    }
	    String jsonOutString = "";
    	HttpUtilUpbit  httpU = new HttpUtilUpbit();
        try {
			jsonOutString = httpU.httpGetUpbitExchangeApi(UPBIT_OPEN_API_ACCESS_KEY,UPBIT_OPEN_API_SECRET_KEY,URL,queryString);
		} catch (NoSuchAlgorithmException | URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString(); 
			//System.out.println(exceptionAsString);
			//출처: https://blog.miyam.net/81 [낭만 프로그래머]
			
			drRst.setString("JSON_OUT", jsonOutString);
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "100");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			return OUT_DS;
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString(); 
			//System.out.println(exceptionAsString);
			//출처: https://blog.miyam.net/81 [낭만 프로그래머]
			
			drRst.setString("JSON_OUT", jsonOutString);
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			return OUT_DS;
		}
        drRst.setString("JSON_OUT", jsonOutString);
        
        ArrayList<HashMap<String,Object>> al = null;
		try {
			al = PjtUtil.g().JsonStringToObject(jsonOutString, ArrayList.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString(); 
			//System.out.println(exceptionAsString);
			//출처: https://blog.miyam.net/81 [낭만 프로그래머]
			
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_CODE", "203");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			
			return OUT_DS;
		}
		drRst.setString("STATUS", "S");
		
		for (int i = 0; i < al.size(); i++) {
			DataRow dr = OUT_RSET.addRow();
			HashMap<String,Object> c = al.get(i);
			dr.setString("TYPE",c.get("type").toString());
    		dr.setString("UUID",c.get("uuid").toString());
    		dr.setString("CURRENCY",c.get("currency").toString());
    		dr.setString("TXID",c.get("txid").toString());
    		dr.setString("STATE",c.get("state").toString());
    		dr.setString("CREATED_AT",c.get("created_at").toString());
    		if(c.get("done_at")!=null) {
    			dr.setString("DONE_AT",c.get("done_at").toString());	
    		}
    		dr.setBigDecimal("AMOUNT",new BigDecimal(Double.parseDouble(c.get("amount").toString())));
    		dr.setBigDecimal("FEE",new BigDecimal(Double.parseDouble(c.get("fee").toString())));
    		dr.setString("TRANSACTION_TYPE",c.get("transaction_type").toString());
    	}
        return OUT_DS;
    }

}
