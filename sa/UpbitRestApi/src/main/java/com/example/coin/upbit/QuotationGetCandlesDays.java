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
public class QuotationGetCandlesDays extends SAProxy {

    public DataSet GetCandlesDays(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	String  URL="https://api.upbit.com/v1/candles/days";
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
    	OUT_RSET.addColumn("CANDLE_DATE_TIME_UTC", dataset.type.DataType.STRING,null,"캔들 기준 시각(UTC 기준)");
    	OUT_RSET.addColumn("CANDLE_DATE_TIME_KST", dataset.type.DataType.STRING,null,"캔들 기준 시각(KST 기준)");
    	OUT_RSET.addColumn("OPENING_PRICE", dataset.type.DataType.BIGDECIMAL,null,"시가");
    	OUT_RSET.addColumn("HIGH_PRICE", dataset.type.DataType.BIGDECIMAL,null,"고가");
    	OUT_RSET.addColumn("LOW_PRICE", dataset.type.DataType.BIGDECIMAL,null,"저가");
    	OUT_RSET.addColumn("TRADE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"종가");
    	OUT_RSET.addColumn("TIMESTAMP", dataset.type.DataType.BIGDECIMAL,null,"마지막 틱이 저장된 시각");
    	OUT_RSET.addColumn("CANDLE_ACC_TRADE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"누적 거래 금액");
    	OUT_RSET.addColumn("CANDLE_ACC_TRADE_VOLUME", dataset.type.DataType.BIGDECIMAL,null,"누적 거래량");
    	OUT_RSET.addColumn("PREV_CLOSING_PRICE", dataset.type.DataType.BIGDECIMAL,null,"전일 종가(UTC 0시 기준)");
    	
    	OUT_RSET.addColumn("CHANGE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"전일 종가 대비 변화 금액");
    	OUT_RSET.addColumn("CHANGE_RATE", dataset.type.DataType.BIGDECIMAL,null,"전일 종가 대비 변화량");
    	OUT_RSET.addColumn("CONVERTED_TRADE_PRICE", dataset.type.DataType.BIGDECIMAL,null,"종가 환산 화폐 단위로 환산된 가격(요청에 convertingPriceUnit 파라미터 없을 시 해당 필드 포함되지 않음.)");
    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String MARKET =null;
    	String TO =null;
    	String COUNT =null;
    	String CONVERTING_PRICE_UNIT =null;
    	
    	//파라미터의 경우, 원화 마켓이 아닌 다른 마켓(ex. BTC, ETH)의 일봉 요청시 종가를 명시된 파라미터 값으로 환산해 
    	// converted_trade_price 필드에 추가하여 반환합니다.
		//현재는 원화(KRW) 로 변환하는 기능만 제공하며 추후 기능을 확장할 수 있습니다.
    	
    	if(IN_PSET.getRowCount()>0) {
    		MARKET = IN_PSET.getRow(0).getStringNullToEmpty("MARKET");
    		TO = IN_PSET.getRow(0).getStringNullToEmpty("TO");
    		COUNT = IN_PSET.getRow(0).getStringNullToEmpty("COUNT");
    		CONVERTING_PRICE_UNIT = IN_PSET.getRow(0).getStringNullToEmpty("CONVERTING_PRICE_UNIT");
    	}

    	HashMap<String, String> params = new HashMap<>();
        params.put("market", MARKET);
        params.put("to", TO);
        params.put("count", COUNT);
        params.put("convertingPriceUnit", CONVERTING_PRICE_UNIT);
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
			dr.setString("CANDLE_DATE_TIME_UTC",c.get("candle_date_time_utc").toString());
			dr.setString("CANDLE_DATE_TIME_KST",c.get("candle_date_time_kst").toString());
			dr.setBigDecimal("OPENING_PRICE",  new BigDecimal(Double.parseDouble(c.get("opening_price").toString()))   );
			dr.setBigDecimal("HIGH_PRICE",  new BigDecimal(Double.parseDouble(c.get("high_price").toString()))   );
			dr.setBigDecimal("LOW_PRICE",  new BigDecimal(Double.parseDouble(c.get("low_price").toString()))   );
			dr.setBigDecimal("TRADE_PRICE",  new BigDecimal(Double.parseDouble(c.get("trade_price").toString()))   );
			dr.setBigDecimal("TIMESTAMP",new BigDecimal(c.get("timestamp").toString()));
			dr.setBigDecimal("CANDLE_ACC_TRADE_PRICE",  new BigDecimal(Double.parseDouble(c.get("candle_acc_trade_price").toString()))   );
			
			dr.setBigDecimal("CANDLE_ACC_TRADE_VOLUME",  new BigDecimal(Double.parseDouble(c.get("candle_acc_trade_volume").toString()))   );
			dr.setBigDecimal("PREV_CLOSING_PRICE",  new BigDecimal(Double.parseDouble(c.get("prev_closing_price").toString()))   );
			
			if(c.get("change_price")!=null){
			      dr.setBigDecimal("CHANGE_PRICE",  new BigDecimal(Double.parseDouble(c.get("change_price").toString()))   ); 
			}
			
			if(c.get("change_rate")!=null){
			      dr.setBigDecimal("CHANGE_RATE",  new BigDecimal(Double.parseDouble(c.get("change_rate").toString()))   );
			}
			
			if(c.get("converted_trade_price")!=null){
			      dr.setBigDecimal("CONVERTED_TRADE_PRICE",  new BigDecimal(Double.parseDouble(c.get("converted_trade_price").toString()))   );
			}
		}
        return OUT_DS;
    }

}
