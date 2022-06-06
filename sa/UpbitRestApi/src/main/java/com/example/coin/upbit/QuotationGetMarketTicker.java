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
public class QuotationGetMarketTicker extends SAProxy {

    public DataSet GetMarketTicker(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	String  URL="https://api.upbit.com/v1/ticker";
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
    	OUT_RSET.addColumn("MARKET");  //종목 구분 코드
    	OUT_RSET.addColumn("TRADE_DATE"); //최근 거래 일자(UTC)	
    	OUT_RSET.addColumn("TRADE_TIME");  //최근 거래 시각(UTC)
    	OUT_RSET.addColumn("TRADE_DATE_KST"); //최근 거래 일자(KST)
    	OUT_RSET.addColumn("TRADE_TIME_KST"); //최근 거래 시각(KST)
    	OUT_RSET.addColumn("OPENING_PRICE", dataset.type.DataType.BIGDECIMAL,null,"시가");
    	OUT_RSET.addColumn("HIGH_PRICE", dataset.type.DataType.BIGDECIMAL,null,"고가");
    	OUT_RSET.addColumn("LOW_PRICE", dataset.type.DataType.BIGDECIMAL,null,"저가");
    	OUT_RSET.addColumn("TRADE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"종가");
    	OUT_RSET.addColumn("PREV_CLOSING_PRICE", dataset.type.DataType.BIGDECIMAL,null,"전일 종가");
    	OUT_RSET.addColumn("CHANGE"); /*	EVEN : 보합
											RISE : 상승
											FALL : 하락	String
    	 								*/
    	OUT_RSET.addColumn("CHANGE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"변화액의 절대값");
    	OUT_RSET.addColumn("CHANGE_RATE"); //변화율의 절대값
    	OUT_RSET.addColumn("SIGNED_CHANGE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"부호가 있는 변화액");
    	OUT_RSET.addColumn("SIGNED_CHANGE_RATE"); //부호가 있는 변화율
    	
    	OUT_RSET.addColumn("TRADE_VOLUME", dataset.type.DataType.BIGDECIMAL,null,"가장 최근 거래량");
    	OUT_RSET.addColumn("ACC_TRADE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"누적 거래대금(UTC 0시 기준)");
    	OUT_RSET.addColumn("ACC_TRADE_PRICE_24H", dataset.type.DataType.BIGDECIMAL,null,"24시간 누적 거래대금");
    	OUT_RSET.addColumn("ACC_TRADE_VOLUME", dataset.type.DataType.BIGDECIMAL,null,"누적 거래량(UTC 0시 기준)"); 	
    	OUT_RSET.addColumn("ACC_TRADE_VOLUME_24H", dataset.type.DataType.BIGDECIMAL,null,"24시간 누적 거래량");
    	OUT_RSET.addColumn("HIGHEST_52_WEEK_PRICE", dataset.type.DataType.BIGDECIMAL,null,"52주 신고가");
    	OUT_RSET.addColumn("HIGHEST_52_WEEK_DATE", dataset.type.DataType.STRING,null,"52주 신고가 달성일");
    	OUT_RSET.addColumn("LOWEST_52_WEEK_PRICE", dataset.type.DataType.BIGDECIMAL,null,"52주 신저가");
    	OUT_RSET.addColumn("LOWEST_52_WEEK_DATE", dataset.type.DataType.STRING,null,"52주 신저가 달성일");
    	OUT_RSET.addColumn("TIMESTAMP"); //타임스탬프
    	
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
			dr.setString("TRADE_DATE",c.get("trade_date").toString());
			dr.setString("TRADE_TIME",c.get("trade_time").toString());
			dr.setString("TRADE_DATE_KST",c.get("trade_date_kst").toString());
			dr.setString("TRADE_TIME_KST",c.get("trade_time_kst").toString());
			
			dr.setBigDecimal("OPENING_PRICE",new BigDecimal(Double.parseDouble(c.get("opening_price").toString())));
			dr.setBigDecimal("HIGH_PRICE",new BigDecimal(Double.parseDouble(c.get("high_price").toString())));
			dr.setBigDecimal("LOW_PRICE",new BigDecimal(Double.parseDouble(c.get("low_price").toString())));
			dr.setBigDecimal("TRADE_PRICE",new BigDecimal(Double.parseDouble(c.get("trade_price").toString())));
			dr.setBigDecimal("PREV_CLOSING_PRICE",new BigDecimal(Double.parseDouble(c.get("prev_closing_price").toString())));
			
			dr.setString("CHANGE",c.get("change").toString());
			dr.setBigDecimal("CHANGE_PRICE",new BigDecimal(Double.parseDouble(c.get("change_price").toString())));
			dr.setString("CHANGE_RATE",c.get("change_rate").toString());
			dr.setBigDecimal("SIGNED_CHANGE_PRICE",new BigDecimal(Double.parseDouble(c.get("signed_change_price").toString())));					
			dr.setString("SIGNED_CHANGE_RATE",c.get("signed_change_rate").toString());
			dr.setBigDecimal("TRADE_VOLUME",new BigDecimal(Double.parseDouble(c.get("trade_volume").toString())));
			dr.setBigDecimal("ACC_TRADE_PRICE",new BigDecimal(Double.parseDouble(c.get("acc_trade_price").toString())));
			dr.setBigDecimal("ACC_TRADE_PRICE_24H",new BigDecimal(Double.parseDouble(c.get("acc_trade_price_24h").toString())));
			dr.setBigDecimal("ACC_TRADE_VOLUME",new BigDecimal(Double.parseDouble(c.get("acc_trade_volume").toString())));
			dr.setBigDecimal("ACC_TRADE_VOLUME_24H",new BigDecimal(Double.parseDouble(c.get("acc_trade_volume_24h").toString())));
			dr.setBigDecimal("HIGHEST_52_WEEK_PRICE",new BigDecimal(Double.parseDouble(c.get("highest_52_week_price").toString())));
			dr.setString("HIGHEST_52_WEEK_DATE",c.get("highest_52_week_date").toString());
			dr.setBigDecimal("LOWEST_52_WEEK_PRICE",new BigDecimal(Double.parseDouble(c.get("lowest_52_week_price").toString())));
			dr.setString("LOWEST_52_WEEK_DATE",c.get("lowest_52_week_date").toString());
			dr.setString("TIMESTAMP",c.get("timestamp").toString());

		}
        return OUT_DS;
    }

}
