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
public class QuotationGetTradesTicks extends SAProxy {

    public DataSet GetTradesTicks(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	String  URL="https://api.upbit.com/v1/trades/ticks";
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
    	OUT_RSET.addColumn("TRADE_DATE_UTC", dataset.type.DataType.STRING,null,"체결 일자(UTC 기준)");
    	OUT_RSET.addColumn("TRADE_TIME_UTC", dataset.type.DataType.STRING,null,"체결 시각(UTC 기준)");
    	OUT_RSET.addColumn("TIMESTAMP", dataset.type.DataType.BIGDECIMAL,null,"체결 타임스탬프");
    	OUT_RSET.addColumn("TRADE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"체결 가격");
    	OUT_RSET.addColumn("TRADE_VOLUME", dataset.type.DataType.BIGDECIMAL,null,"체결량");
    	OUT_RSET.addColumn("PREV_CLOSING_PRICE", dataset.type.DataType.BIGDECIMAL,null,"전일 종가");
    	OUT_RSET.addColumn("CHANGE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"변화량");
    	OUT_RSET.addColumn("ASK_BID", dataset.type.DataType.STRING,null,"매도/매수");
    	OUT_RSET.addColumn("SEQUENTIAL_ID", dataset.type.DataType.LONG,null,"체결 번호(Unique)");
    	/*
    	 * sequential_id 필드는 체결의 유일성 판단을 위한 근거로 쓰일 수 있습니다. 하지만 체결의 순서를 보장하지는 못합니다.
    	 * */


    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String MARKET =null;
    	String TO =null;
    	String COUNT =null;
    	String CURSOR =null;
    	String DAYS_AGO =null;

    	if(IN_PSET.getRowCount()>0) {
    		MARKET = IN_PSET.getRow(0).getStringNullToEmpty("MARKET");
    		TO = IN_PSET.getRow(0).getStringNullToEmpty("TO");
    		COUNT = IN_PSET.getRow(0).getStringNullToEmpty("COUNT");
    		CURSOR = IN_PSET.getRow(0).getStringNullToEmpty("CURSOR");
    		DAYS_AGO = IN_PSET.getRow(0).getStringNullToEmpty("DAYS_AGO");
    	}
    	HashMap<String, String> params = new HashMap<>();
        params.put("market", MARKET);
        params.put("to", TO);
        params.put("count", COUNT);
        params.put("cursor", CURSOR);
        params.put("daysAgo", DAYS_AGO);
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

		for (HashMap<String, Object> c : al) {
			DataRow dr = OUT_RSET.addRow();

			dr.setString("MARKET",c.get("market").toString());
			dr.setString("TRADE_DATE_UTC",c.get("trade_date_utc").toString());
			dr.setString("TRADE_TIME_UTC",c.get("trade_time_utc").toString());
			if(c.get("timestamp") != null){
				dr.setBigDecimal("TIMESTAMP",new BigDecimal(c.get("timestamp").toString()));
			}
			dr.setBigDecimal("TRADE_PRICE",  new BigDecimal(Double.parseDouble(c.get("trade_price").toString()))   );
			dr.setBigDecimal("TRADE_VOLUME",  new BigDecimal(Double.parseDouble(c.get("trade_volume").toString()))   );
			dr.setBigDecimal("PREV_CLOSING_PRICE",  new BigDecimal(Double.parseDouble(c.get("prev_closing_price").toString()))   );
			dr.setBigDecimal("CHANGE_PRICE",  new BigDecimal(Double.parseDouble(c.get("change_price").toString()))   );
			dr.setString("ASK_BID",c.get("ask_bid").toString());
			dr.setString("SEQUENTIAL_ID",c.get("sequential_id").toString());
			
		}
        return OUT_DS;
    }

}
