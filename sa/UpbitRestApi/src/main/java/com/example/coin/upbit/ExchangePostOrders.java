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
public class ExchangePostOrders extends SAProxy {

    public DataSet PostOrders(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	String  URL="https://api.upbit.com/v1/orders";
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
    	OUT_RSET.addColumn("UUID", dataset.type.DataType.STRING,null,"주문의 고유 아이디");
    	OUT_RSET.addColumn("SIDE", dataset.type.DataType.STRING,null,"주문 종류");
    	OUT_RSET.addColumn("ORD_TYPE", dataset.type.DataType.STRING,null,"주문 방식");
    	OUT_RSET.addColumn("PRICE", dataset.type.DataType.BIGDECIMAL,null,"주문 당시 화폐 가격");
    	OUT_RSET.addColumn("AVG_PRICE");//체결 가격의 평균가
    	OUT_RSET.addColumn("STATE");//주문 상태
    	OUT_RSET.addColumn("MARKET");//마켓의 유일키
    	OUT_RSET.addColumn("CREATED_AT");//주문 생성 시간
    	OUT_RSET.addColumn("VOLUME");//사용자가 입력한 주문 양	
    	OUT_RSET.addColumn("REMAINING_VOLUME");//체결 후 남은 주문 양
    	OUT_RSET.addColumn("RESERVED_FEE");//수수료로 예약된 비용
    	OUT_RSET.addColumn("REMAINING_FEE");//남은 수수료
    	OUT_RSET.addColumn("PAID_FEE");//사용된 수수료
    	OUT_RSET.addColumn("LOCKED");//거래에 사용중인 비용
    	OUT_RSET.addColumn("EXECUTED_VOLUME");//체결된 양
    	OUT_RSET.addColumn("TRADE_COUNT");//해당 주문에 걸린 체결 수
        OUT_RSET.addColumn("IDENTIFIER");//조회용 사용자 지정값 (선택)  String (Uniq 값 사용)
    	
    
    	
    	
    
    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String MARKET =null;    //마켓 ID (필수)
    	String SIDE =null; 
        /* 주문 종류 (필수)
        - bid : 매수
        - ask : 매도
        */
    	String VOLUME =null;
        /*	주문량 (지정가, 시장가 매도 시 필수)*/
    	String PRICE =null;
        /*
        주문 가격. (지정가, 시장가 매수 시 필수)
        ex) KRW-BTC 마켓에서 1BTC당 1,000 KRW로 거래할 경우, 값은 1000 이 된다.
        ex) KRW-BTC 마켓에서 1BTC당 매도 1호가가 500 KRW 인 경우, 시장가 매수 시 값을 1000으로 세팅하면 2BTC가 매수된다.
            (수수료가 존재하거나 매도 1호가의 수량에 따라 상이할 수 있음)
        
        */

    	String ORD_TYPE =null;
        String IDENTIFIER =null;
    	if(IN_PSET.getRowCount()>0) {
    		MARKET = IN_PSET.getRow(0).getStringNullToEmpty("MARKET");
    		SIDE = IN_PSET.getRow(0).getStringNullToEmpty("SIDE");
    		VOLUME = IN_PSET.getRow(0).getStringNullToEmpty("VOLUME");
    		PRICE = IN_PSET.getRow(0).getStringNullToEmpty("PRICE");
    		ORD_TYPE = IN_PSET.getRow(0).getStringNullToEmpty("ORD_TYPE");
            IDENTIFIER = IN_PSET.getRow(0).getStringNullToEmpty("IDENTIFIER");
    	}

    	HashMap<String, String> params = new HashMap<>();
		params.put("market", MARKET);
		params.put("side", SIDE);
		
		if(!PjtUtil.g().isEmpty(VOLUME)){
		  params.put("volume", VOLUME);
		}
		
		if(!PjtUtil.g().isEmpty(PRICE)){
		  params.put("price", PRICE);
		}

        if(!PjtUtil.g().isEmpty(IDENTIFIER)){
            params.put("identifier", IDENTIFIER);
          }
		    
		params.put("ord_type", ORD_TYPE);
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
			jsonOutString = httpU.httpPostUpbitExchangeApi(UPBIT_OPEN_API_ACCESS_KEY,UPBIT_OPEN_API_SECRET_KEY,URL,params);
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
		
		if(c!=null){				
			DataRow dr = OUT_RSET.addRow();
			dr.setString("UUID",c.get("uuid").toString());
			dr.setString("SIDE",c.get("side").toString());
			dr.setString("ORD_TYPE",c.get("ord_type").toString());
            if(c.get("price")!=null){
			    dr.setString("PRICE",c.get("price").toString());
            }
            if(c.get("avg_price")!=null){
                dr.setString("AVG_PRICE",c.get("avg_price").toString());
            }			
			dr.setString("STATE",c.get("state").toString());
			dr.setString("MARKET",c.get("market").toString());
			dr.setString("CREATED_AT",c.get("created_at").toString());
            if(c.get("volume")!=null){
                /*시장가 매수를 했는데  VOLUME  리턴 되지 않았다.
                  시장가 매수는 총가격만 넣으면 알아서 수량이 정해지는 매커니즘이다.
                */
                dr.setString("VOLUME",c.get("volume").toString());
            }
			
            if(c.get("remaining_volume")!=null){
                /*시장가 매수를 했는데  VOLUME  리턴 되지 않았다.
                  시장가 매수는 총가격만 넣으면 알아서 수량이 정해지는 매커니즘이다.
                */
			    dr.setString("REMAINING_VOLUME",c.get("remaining_volume").toString());
            }
			dr.setString("RESERVED_FEE",c.get("reserved_fee").toString());
			dr.setString("REMAINING_FEE",c.get("remaining_fee").toString());
			dr.setString("PAID_FEE",c.get("paid_fee").toString());
			dr.setString("LOCKED",c.get("locked").toString());
			dr.setString("EXECUTED_VOLUME",c.get("executed_volume").toString());
            if(c.get("trades_count")!=null){
                dr.setString("TRADE_COUNT",c.get("trades_count").toString());
            }
            if(IDENTIFIER!=null){
                dr.setString("IDENTIFIER",IDENTIFIER);
            }
            
            /*시장가 매수로  */
		}
        return OUT_DS;
    }

}
