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
public class QuotationGetOrderBook extends SAProxy {

    public DataSet GetOrderBook(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	String  URL="https://api.upbit.com/v1/orderbook";
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
    	OUT_RSET.addColumn("MARKET", dataset.type.DataType.STRING,null,"마켓명");
    	OUT_RSET.addColumn("TIMESTAMP", dataset.type.DataType.BIGDECIMAL,null,"호가 생성 시각");
    	OUT_RSET.addColumn("TOTAL_ASK_SIZE", dataset.type.DataType.BIGDECIMAL,null,"호가 매도 총 잔량");
    	OUT_RSET.addColumn("TOTAL_BID_SIZE", dataset.type.DataType.BIGDECIMAL,null,"호가 매수 총 잔량");
    	
    	
    	DataTable OUT_RSET_ORDER_BOOK = OUT_DS.addTable("OUT_RSET_ORDER_BOOK");
    	OUT_RSET_ORDER_BOOK.addColumn("MARKET", dataset.type.DataType.STRING,null,"마켓명");
    	OUT_RSET_ORDER_BOOK.addColumn("TIMESTAMP", dataset.type.DataType.BIGDECIMAL,null,"호가 생성 시각");
    	OUT_RSET_ORDER_BOOK.addColumn("SEQ", dataset.type.DataType.INTEGER,null,"순번");
    	OUT_RSET_ORDER_BOOK.addColumn("ASK_PRICE", dataset.type.DataType.BIGDECIMAL,null,"매도호가");
    	OUT_RSET_ORDER_BOOK.addColumn("BID_PRICE", dataset.type.DataType.BIGDECIMAL,null,"매수호가");
    	OUT_RSET_ORDER_BOOK.addColumn("ASK_SIZE", dataset.type.DataType.BIGDECIMAL,null,"매도 잔량");
    	OUT_RSET_ORDER_BOOK.addColumn("BID_SIZE", dataset.type.DataType.BIGDECIMAL,null,"매수 잔량");
    	
    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String MARKETS =null;
    	
    	if(IN_PSET.getRowCount()>0) {
    		MARKETS = IN_PSET.getRow(0).getStringNullToEmpty("MARKETS");
    	}
    	HashMap<String, String> params = new HashMap<>();
        params.put("markets", MARKETS);
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
			HashMap<String,Object> c =al.get(i);
			DataRow dr = OUT_RSET.addRow();
			
			dr.setString("MARKET",c.get("market").toString());
			dr.setBigDecimal("TIMESTAMP",  new BigDecimal(c.get("timestamp").toString())   );
			dr.setBigDecimal("TOTAL_ASK_SIZE",  new BigDecimal(Double.parseDouble(c.get("total_ask_size").toString()))   );
			dr.setBigDecimal("TOTAL_BID_SIZE",  new BigDecimal(Double.parseDouble(c.get("total_bid_size").toString()))   );
		
			Object orderbook_units = c.get("orderbook_units");
			if(orderbook_units!=null){
				ArrayList<HashMap<String, Object>> al_tmp = (ArrayList<HashMap<String, Object>>)orderbook_units;
				for(int j=0;j<al_tmp.size();j++){
					HashMap<String, Object> d = al_tmp.get(j);
					DataRow dr_OUT_RSET_ORDER_BOOK = OUT_RSET_ORDER_BOOK.addRow();
					dr_OUT_RSET_ORDER_BOOK.setString("MARKET",c.get("market").toString());
					dr_OUT_RSET_ORDER_BOOK.setBigDecimal("TIMESTAMP",  new BigDecimal(c.get("timestamp").toString())   );
					dr_OUT_RSET_ORDER_BOOK.setInt("SEQ",  j  );
					
					
					dr_OUT_RSET_ORDER_BOOK.setBigDecimal("ASK_PRICE",  new BigDecimal(Double.parseDouble(d.get("ask_price").toString()))   );
					dr_OUT_RSET_ORDER_BOOK.setBigDecimal("BID_PRICE",  new BigDecimal(Double.parseDouble(d.get("bid_price").toString()))   );
					dr_OUT_RSET_ORDER_BOOK.setBigDecimal("ASK_SIZE",  new BigDecimal(Double.parseDouble(d.get("ask_size").toString()))   );
					dr_OUT_RSET_ORDER_BOOK.setBigDecimal("BID_SIZE",  new BigDecimal(Double.parseDouble(d.get("bid_size").toString()))   );
				}
			}
		}
        return OUT_DS;
    }

}
