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

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;
public class ExchangeGetOrders extends SAProxy {

    public DataSet GetOrders(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
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

    	DataTable OUT_REST = OUT_DS.addTable("OUT_RSET");

    	OUT_REST.addColumn("UUID", dataset.type.DataType.STRING,null,"주문의 고유 아이디");
    	OUT_REST.addColumn("SIDE", dataset.type.DataType.STRING,null,"주문 종류");
    	OUT_REST.addColumn("ORD_TYPE", dataset.type.DataType.STRING,null,"주문 방식");
    	OUT_REST.addColumn("PRICE", dataset.type.DataType.BIGDECIMAL,null,"주문 당시 화폐 가격");
    	OUT_REST.addColumn("STATE", dataset.type.DataType.STRING,null,"주문 상태");
    	OUT_REST.addColumn("MARKET", dataset.type.DataType.STRING,null,"마켓의 유일키");

    	OUT_REST.addColumn("CREATED_AT", dataset.type.DataType.STRING,null,"주문 생성 시간");
    	OUT_REST.addColumn("VOLUME", dataset.type.DataType.BIGDECIMAL,null,"사용자가 입력한 주문 양");
    	OUT_REST.addColumn("REMAINING_VOLUME", dataset.type.DataType.BIGDECIMAL,null,"체결 후 남은 주문 양");
    	OUT_REST.addColumn("RESERVED_FEE", dataset.type.DataType.BIGDECIMAL,null,"수수료로 예약된 비용");
    	OUT_REST.addColumn("REMAINING_FEE", dataset.type.DataType.BIGDECIMAL,null,"남은 수수료");
    	OUT_REST.addColumn("PAID_FEE", dataset.type.DataType.BIGDECIMAL,null,"사용된 수수료");
    	OUT_REST.addColumn("LOCKED", dataset.type.DataType.BIGDECIMAL,null,"거래에 사용중인 비용");
    	OUT_REST.addColumn("EXECUTED_VOLUME", dataset.type.DataType.BIGDECIMAL,null,"체결된 양");
    	OUT_REST.addColumn("TRADE_COUNT", dataset.type.DataType.INTEGER,null,"해당 주문에 걸린 체결 수");
    	
    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String MARKET =null;
    	String STATE =null;
    	String PAGE =null;
    	String LIMIT =null;
    	String ORDER_BY =null;
    	if(IN_PSET.getRowCount()>0) {
    		MARKET = IN_PSET.getRow(0).getStringNullToEmpty("MARKET");
    		STATE = IN_PSET.getRow(0).getStringNullToEmpty("STATE");
    		PAGE = IN_PSET.getRow(0).getStringNullToEmpty("PAGE");
    		LIMIT = IN_PSET.getRow(0).getStringNullToEmpty("LIMIT");
    		ORDER_BY = IN_PSET.getRow(0).getStringNullToEmpty("ORDER_BY");
    	}
       	HashMap<String, String> params = new HashMap<>();
		
        if(!PjtUtil.g().isEmpty(MARKET)){
        	params.put("market",MARKET);
	    }
        if(!PjtUtil.g().isEmpty(STATE)){
        	params.put("state",STATE);
	    }
        if(!PjtUtil.g().isEmpty(PAGE)){
        	params.put("page",PAGE);
        } else {
	    	params.put("page","1");
	    }
        if(!PjtUtil.g().isEmpty(LIMIT)){
        	params.put("limit",LIMIT);
	    } else {
	    	params.put("limit","100");
	    }
        if(!PjtUtil.g().isEmpty(ORDER_BY)){
        	params.put("order_by",ORDER_BY);
        } else {
	    	params.put("order_by","desc");
	    }
        
		ArrayList<String> queryElements = new ArrayList<>();
	    for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }
	    
		DataTable IN_STATE =InDs.getTable("IN_STATE");
		if(IN_STATE!=null){
			for(int i=0;i<IN_STATE.getRowCount();i++) {
				String state = IN_STATE.getRow(i).getString("STATE");
				if(!PjtUtil.g().isEmpty(state)) {
					queryElements.add("states[]=" + state);	
				}
				
			}
		}
		DataTable IN_UUID =InDs.getTable("IN_UUID");
		if(IN_UUID!=null){
			for(int i=0;i<IN_UUID.getRowCount();i++) {
				String uuid = IN_UUID.getRow(i).getString("UUID");
				if(!PjtUtil.g().isEmpty(uuid)) {
					queryElements.add("uuids[]=" + uuid);
				}
			}
		}
		DataTable IN_IDENTIFIER =InDs.getTable("IN_IDENTIFIER");
		if( IN_IDENTIFIER!=null){
			for(int i=0;i<IN_IDENTIFIER.getRowCount();i++) {
				String identifier = IN_IDENTIFIER.getRow(i).getString("IDENTIFIER");
				if(!PjtUtil.g().isEmpty(identifier)) {
					queryElements.add("identifiers[]=" + identifier);
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
		
		for(int i=0;i<al.size();i++) {
			HashMap<String,Object>  c= al.get(i);
            System.out.println(c);
			
			DataRow dr = OUT_REST.addRow();
			dr.setString("UUID",c.get("uuid").toString());
			dr.setString("SIDE",c.get("side").toString());
			dr.setString("ORD_TYPE",c.get("ord_type").toString());
            if(c.get("price")!=null){
                dr.setBigDecimal("PRICE",new BigDecimal(Double.parseDouble(c.get("price").toString())));
            }			
			dr.setString("STATE",c.get("state").toString());
			dr.setString("MARKET",c.get("market").toString());
			dr.setString("CREATED_AT",c.get("created_at").toString());
			if(c.get("volume")!=null){
				dr.setBigDecimal("VOLUME",new BigDecimal(Double.parseDouble(c.get("volume").toString())));
			}
			if(c.get("remaining_volume")!=null){
				dr.setBigDecimal("REMAINING_VOLUME",new BigDecimal(Double.parseDouble(c.get("remaining_volume").toString())));
			}
			if(c.get("reserved_fee")!=null){
				dr.setBigDecimal("RESERVED_FEE",new BigDecimal(Double.parseDouble(c.get("reserved_fee").toString())));
			}
			if(c.get("remaining_fee")!=null){
				dr.setBigDecimal("REMAINING_FEE",new BigDecimal(Double.parseDouble(c.get("remaining_fee").toString())));
			}
			if(c.get("paid_fee")!=null){
				dr.setBigDecimal("PAID_FEE",new BigDecimal(Double.parseDouble(c.get("paid_fee").toString())));
			}
			if(c.get("locked")!=null){
				dr.setBigDecimal("LOCKED",new BigDecimal(Double.parseDouble(c.get("locked").toString())));
			}
			if(c.get("executed_volume")!=null){
				dr.setBigDecimal("EXECUTED_VOLUME",new BigDecimal(Double.parseDouble(c.get("executed_volume").toString())));
			}
			
			if(c.get("trade_count")!=null){
				dr.setInt("TRADE_COUNT",Integer.parseInt(c.get("trade_count").toString()));
			}
		}

        return OUT_DS;
    }

}
