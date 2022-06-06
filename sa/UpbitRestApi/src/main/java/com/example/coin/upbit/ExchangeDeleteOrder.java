package com.example.coin.upbit;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
public class ExchangeDeleteOrder extends SAProxy {

    public DataSet DeleteOrder(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
        String URL = "https://api.upbit.com/v1/order";
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

    	OUT_REST.addColumn("UUID");  //주문의 고유 아이디
    	OUT_REST.addColumn("SIDE");  //주문 종류
    	OUT_REST.addColumn("ORD_TYPE");  //주문 방식
    	OUT_REST.addColumn("PRICE");  //주문 당시 화폐 가격
    	OUT_REST.addColumn("STATE");  //주문 상태
    	OUT_REST.addColumn("MARKET");  //마켓의 유일키
    	OUT_REST.addColumn("CREATED_AT");  //주문 생성 시간
    	OUT_REST.addColumn("VOLUME");  //사용자가 입력한 주문 양
    	OUT_REST.addColumn("REMAINING_VOLUME");  //체결 후 남은 주문 양
    	OUT_REST.addColumn("RESERVED_FEE");  //수수료로 예약된 비용
    	OUT_REST.addColumn("REMAINING_FEE");  //남은 수수료
    	OUT_REST.addColumn("PAID_FEE");  //사용된 수수료
    	OUT_REST.addColumn("LOCKED");  //거래에 사용중인 비용
    	OUT_REST.addColumn("EXECUTED_VOLUME");  //체결된 양
    	OUT_REST.addColumn("TRADE_COUNT");  //해당 주문에 걸린 체결 수  	
    	
    	DataTable dtKey =InDs.getTable("IN_KEY");
    	DataTable dt =InDs.getTable("IN_PSET");
    	String UUID = null;
    	String IDENTIFIER = null;
    	
    	String UPBIT_OPEN_API_ACCESS_KEY =null;
    	String UPBIT_OPEN_API_SECRET_KEY =null;
    	
    	if(dt.getRowCount()>0) {
    		UUID = dt.getRow(0).getStringNullToEmpty("UUID");
    		IDENTIFIER = dt.getRow(0).getStringNullToEmpty("IDENTIFIER");
    	}
    	
    	if(dtKey.getRowCount()>0) {
    		UPBIT_OPEN_API_ACCESS_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_ACCESS_KEY");
    		UPBIT_OPEN_API_SECRET_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_SECRET_KEY");
    	}
    	HashMap<String, String> params = new HashMap<>();
        if(!PjtUtil.g().isEmpty(UUID)){
            params.put("uuid",UUID);
	    }
	
	    if(!PjtUtil.g().isEmpty(IDENTIFIER)){
	        params.put("identifier",IDENTIFIER);
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
	    
	    ArrayList<String> queryElements = new ArrayList<>();
	    for(Map.Entry<String, String> entity : params.entrySet()) {
	        queryElements.add(entity.getKey() + "=" + entity.getValue());
	    }

	    String queryString = String.join("&", queryElements.toArray(new String[0]));
	    String jsonOutString = "";

	    drRst.setString("QUERY_STRING", queryString);

    	HttpUtilUpbit  httpU = new HttpUtilUpbit();
        try {
			jsonOutString = httpU.httpDelUpbitExchangeApi(UPBIT_OPEN_API_ACCESS_KEY,UPBIT_OPEN_API_SECRET_KEY,URL, queryString);
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
        
        
        
        
        HashMap<String, Object> rtn = null;
		try {
			rtn = PjtUtil.g().JsonStringToObject(jsonOutString, HashMap.class);
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
		
    	if(rtn!=null) {
    		DataRow dr = OUT_REST.addRow();
    		dr.setString("UUID",rtn.get("uuid").toString());
    		dr.setString("SIDE",rtn.get("side").toString());
    		dr.setString("ORD_TYPE",rtn.get("ord_type").toString());
    		dr.setString("PRICE",rtn.get("price").toString());
    		dr.setString("STATE",rtn.get("state").toString());
    		dr.setString("MARKET",rtn.get("market").toString());
    		dr.setString("CREATED_AT",rtn.get("created_at").toString());
    		dr.setString("VOLUME",rtn.get("volume").toString());
    		dr.setString("REMAINING_VOLUME",rtn.get("remaining_volume").toString());
    		dr.setString("RESERVED_FEE",rtn.get("reserved_fee").toString());
    		dr.setString("REMAINING_FEE",rtn.get("remaining_fee").toString());
    		dr.setString("PAID_FEE",rtn.get("paid_fee").toString());
    		dr.setString("LOCKED",rtn.get("locked").toString());
    		dr.setString("EXECUTED_VOLUME",rtn.get("executed_volume").toString());
    		if(rtn.get("trades_count")!=null) {
    			dr.setString("TRADE_COUNT",rtn.get("trades_count").toString());
			}
    	}
        return OUT_DS;
    }

}
