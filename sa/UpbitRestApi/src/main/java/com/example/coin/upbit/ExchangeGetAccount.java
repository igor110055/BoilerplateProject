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
public class ExchangeGetAccount extends SAProxy {

    public DataSet GetAccount(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	String  URL = "https://api.upbit.com/v1/accounts";
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
    	
    	DataTable OUT_REST = OUT_DS.addTable("OUT_RSET");

    	OUT_REST.addColumn("CURRENCY", dataset.type.DataType.STRING,null,"화폐를 의미하는 영문 대문자 코드"); 
    	OUT_REST.addColumn("BALANCE", dataset.type.DataType.BIGDECIMAL,null,"주문가능 금액/수량"); 
    	OUT_REST.addColumn("LOCKED", dataset.type.DataType.BIGDECIMAL,null,"주문 중 묶여있는 금액/수량"); 
    	OUT_REST.addColumn("AVG_BUY_PRICE", dataset.type.DataType.BIGDECIMAL,null,"매수평균가"); 
    	OUT_REST.addColumn("AVG_BUY_PRICE_MODIFIED", dataset.type.DataType.STRING,null,"매수평균가 수정 여부"); 
    	OUT_REST.addColumn("UNIT_CURRENCY" , dataset.type.DataType.STRING,null,"평단가 기준 화폐");
    	
    	DataTable dtKey =InDs.getTable("IN_KEY");
    	String UPBIT_OPEN_API_ACCESS_KEY =null;
    	String UPBIT_OPEN_API_SECRET_KEY =null;
    	if(dtKey.getRowCount()>0) {
    		UPBIT_OPEN_API_ACCESS_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_ACCESS_KEY");
    		UPBIT_OPEN_API_SECRET_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_SECRET_KEY");
    	}
	    
	    DataRow drRst = OUT_RST.addRow();
	    drRst.setString("URL",URL);
	    if(PjtUtil.g().isEmpty(UPBIT_OPEN_API_ACCESS_KEY)){
            //에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "UPBIT_OPEN_API_ACCESS_KEY-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			
			return OUT_DS;
	    }
	
	    if(PjtUtil.g().isEmpty(UPBIT_OPEN_API_SECRET_KEY)){
	        //에러처리
	    	drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "UPBIT_OPEN_API_SECRET_KEY-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			
			return OUT_DS;
	    }
	    String jsonOutString = "";
    	HttpUtilUpbit  httpU = new HttpUtilUpbit();
        try {
			jsonOutString = httpU.httpGetUpbitExchangeApi(UPBIT_OPEN_API_ACCESS_KEY,UPBIT_OPEN_API_SECRET_KEY,URL);
		} catch (NoSuchAlgorithmException | URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString(); 
			//System.out.println(exceptionAsString);
			//출처: https://blog.miyam.net/81 [낭만 프로그래머]
			
			drRst.setString("JSON_OUT", "");
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
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			
			return OUT_DS;
		}
		drRst.setString("STATUS", "S");
		
		for (int i = 0; i < al.size(); i++) {
			DataRow dr = OUT_REST.addRow();
			HashMap<String,Object> c = al.get(i);
			dr.setString("CURRENCY",c.get("currency").toString());
    		dr.setBigDecimal("BALANCE",new BigDecimal(Double.parseDouble(c.get("balance").toString())));
    		dr.setBigDecimal("LOCKED",new BigDecimal(Double.parseDouble(c.get("locked").toString())));
    		dr.setBigDecimal("AVG_BUY_PRICE",new BigDecimal(Double.parseDouble(c.get("avg_buy_price").toString()))); 
    		dr.setString("AVG_BUY_PRICE_MODIFIED",c.get("avg_buy_price_modified").toString());
    		dr.setString("UNIT_CURRENCY",c.get("unit_currency").toString());
    	}
        return OUT_DS;
    }

}
