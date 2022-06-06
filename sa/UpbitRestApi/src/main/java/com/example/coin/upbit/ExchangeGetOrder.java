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

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;
public class ExchangeGetOrder extends SAProxy {

    public DataSet GetOrder(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	String  URL="https://api.upbit.com/v1/order";
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

    	OUT_REST.addColumn("UUID");//주문의 고유 아이디
    	OUT_REST.addColumn("SIDE");//주문 종류
    	OUT_REST.addColumn("ORD_TYPE");//주문 방식
    	OUT_REST.addColumn("PRICE");//주문 당시 화폐 가격
    	OUT_REST.addColumn("STATE");//주문 상태
    	OUT_REST.addColumn("MARKET");//마켓의 유일키

    	OUT_REST.addColumn("CREATED_AT");//주문 생성 시간
    	OUT_REST.addColumn("VOLUME");//사용자가 입력한 주문 양
    	OUT_REST.addColumn("REMAINING_VOLUME");//체결 후 남은 주문 양
    	OUT_REST.addColumn("RESERVED_FEE");//수수료로 예약된 비용
    	OUT_REST.addColumn("REMAINING_FEE");//남은 수수료
    	OUT_REST.addColumn("PAID_FEE");//사용된 수수료
    	OUT_REST.addColumn("LOCKED");//거래에 사용중인 비용
    	OUT_REST.addColumn("EXECUTED_VOLUME");//체결된 양
    	OUT_REST.addColumn("TRADE_COUNT");//해당 주문에 걸린 체결 수
    	
    	
    	DataTable OUT_RSET_TRADES = OUT_DS.addTable("OUT_RSET_TRADES");

    	OUT_RSET_TRADES.addColumn("MARKET");//마켓의 유일 키
    	OUT_RSET_TRADES.addColumn("UUID");//체결의 고유 아이디
    	OUT_RSET_TRADES.addColumn("PRICE");//체결 가격
    	OUT_RSET_TRADES.addColumn("VOLUME");//체결 양
    	OUT_RSET_TRADES.addColumn("FUNDS");//체결된 총 가격
    	OUT_RSET_TRADES.addColumn("SIDE");//체결 종류
    	OUT_RSET_TRADES.addColumn("CREATED_AT");//주문 생성 시간
    	
    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String UUID =null;
    	String IDENTIFIER =null;
    	if(IN_PSET.getRowCount()>0) {
    		UUID = IN_PSET.getRow(0).getStringNullToEmpty("UUID");
    		IDENTIFIER = IN_PSET.getRow(0).getStringNullToEmpty("IDENTIFIER");
    	}
       	HashMap<String, String> params = new HashMap<>();
		
        if(!PjtUtil.g().isEmpty(UUID)){
        	params.put("uuid",UUID);
	    }
        if(!PjtUtil.g().isEmpty(IDENTIFIER)){
        	params.put("identifier",IDENTIFIER);
	    }
        
		ArrayList<String> queryElements = new ArrayList<>();
	    for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
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
	    drRst.setString("QUERY_STRING", queryString);
	    
	    if(PjtUtil.g().isEmpty(UUID)){
            //에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "UUID가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");

			return OUT_DS;
	    }


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
			jsonOutString = httpU.httpGetUpbitExchangeApi(UPBIT_OPEN_API_ACCESS_KEY,UPBIT_OPEN_API_SECRET_KEY,URL,queryString);
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
        System.out.println(jsonOutString);

        HashMap<String,Object> c = null;
		try {
			c = PjtUtil.g().JsonStringToObject(jsonOutString, HashMap.class);
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
		
		if(c!=null) {
			DataRow dr = OUT_REST.addRow();
			dr.setString("UUID",c.get("uuid").toString());
			dr.setString("SIDE",c.get("side").toString());
			dr.setString("ORD_TYPE",c.get("ord_type").toString());
            if(c.get("price")!=null){
			    dr.setString("PRICE",c.get("price").toString());
            }
			dr.setString("STATE",c.get("state").toString());
			dr.setString("MARKET",c.get("market").toString());
			dr.setString("CREATED_AT",c.get("created_at").toString());
			if(c.get("volume")!=null){
				dr.setString("VOLUME",c.get("volume").toString());
			}
			if(c.get("remaining_volume")!=null){
				dr.setString("REMAINING_VOLUME",c.get("remaining_volume").toString());
			}
			if(c.get("reserved_fee")!=null){
				dr.setString("RESERVED_FEE",c.get("reserved_fee").toString());
			}
			if(c.get("remaining_fee")!=null){
				dr.setString("REMAINING_FEE",c.get("remaining_fee").toString());
			}
			if(c.get("paid_fee")!=null){
				dr.setString("PAID_FEE",c.get("paid_fee").toString());
			}
			if(c.get("locked")!=null){
				dr.setString("LOCKED",c.get("locked").toString());
			}
			if(c.get("executed_volume")!=null){
				dr.setString("EXECUTED_VOLUME",c.get("executed_volume").toString());
			}
			
			if(c.get("trade_count")!=null){
				dr.setString("TRADE_COUNT",c.get("trade_count").toString());
			}
			
			if(c.get("trades")!=null){
				ArrayList<HashMap<String, Object>> t = (ArrayList<HashMap<String, Object>>)  c.get("trades");
				for(int j=0;j<t.size();j++){
					DataRow dr_trades = OUT_RSET_TRADES.addRow();
					HashMap<String, Object> tmp = t.get(j);
					dr_trades.setString("MARKET",tmp.get("market").toString());
					dr_trades.setString("UUID",tmp.get("uuid").toString());
					dr_trades.setString("PRICE",tmp.get("price").toString());
					dr_trades.setString("VOLUME",tmp.get("volume").toString());
					dr_trades.setString("FUNDS",tmp.get("funds").toString());
					dr_trades.setString("SIDE",tmp.get("side").toString());
					dr_trades.setString("CREATED_AT",tmp.get("created_at").toString());
				}
			}
		}

        return OUT_DS;
    }

}
