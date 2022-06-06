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
public class ExchangeGetOrderChance extends SAProxy {

    public DataSet GetOrderChance(DataSet InDs,String InDsNames, String outDsNames) throws Exception {
    	String  URL="https://api.upbit.com/v1/orders/chance";
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

    	OUT_REST.addColumn("BID_FEE", dataset.type.DataType.STRING,null,"매수 수수료 비율");//
    	OUT_REST.addColumn("ASK_FEE", dataset.type.DataType.STRING,null,"매도 수수료 비율");//
    	OUT_REST.addColumn("MARKET__ID");//마켓의 유일 키
    	OUT_REST.addColumn("MARKET__NAME");//마켓 이름
    	OUT_REST.addColumn("MARKET__ORDER_TYPES");//지원 주문 방식
    	OUT_REST.addColumn("MARKET__ORDER_SIDES");//지원 주문 종류

    	OUT_REST.addColumn("MARKET__BID__CURRENCY");//화폐를 의미하는 영문 대문자 코드
    	OUT_REST.addColumn("MARKET__BID__PRICE_UNIT", dataset.type.DataType.BIGDECIMAL,null,"주문금액 단위");
    	OUT_REST.addColumn("MARKET__BID__MIN_TOTAL", dataset.type.DataType.BIGDECIMAL,null,"최소 매수 금액"); 
    	OUT_REST.addColumn("MARKET__ASK__CURRENCY");//화폐를 의미하는 영문 대문자 코드
    	OUT_REST.addColumn("MARKET__ASK__PRICE_UNIT", dataset.type.DataType.BIGDECIMAL,null,"주문금액 단위");
    	OUT_REST.addColumn("MARKET__ASK__MIN_TOTAL", dataset.type.DataType.BIGDECIMAL,null,"최소 매도 금액");
    	OUT_REST.addColumn("MARKET__MAX_TOTAL");//최대 매도/매수 금액
    	OUT_REST.addColumn("MARKET__STATE");//마켓 운영 상태

    	OUT_REST.addColumn("BID_ACCOUNT__CURRENCY");//화폐를 의미하는 영문 대문자 코드
    	OUT_REST.addColumn("BID_ACCOUNT__BALANCE");//주문가능 금액/수량
    	OUT_REST.addColumn("BID_ACCOUNT__LOCKED");//주문 중 묶여있는 금액/수량
    	OUT_REST.addColumn("BID_ACCOUNT__AVG_BUY_PRICE");//매수평균가


    	OUT_REST.addColumn("BID_ACCOUNT__AVG_BUY_PRICE_MODIFIED");//매수평균가 수정 여부
    	OUT_REST.addColumn("BID_ACCOUNT__UNIT_CURRENCY");//평단가 기준 화폐
    	OUT_REST.addColumn("ASK_ACCOUNT__CURRENCY");//화폐를 의미하는 영문 대문자 코드
    	OUT_REST.addColumn("ASK_ACCOUNT__BALANCE");//주문가능 금액/수량
    	OUT_REST.addColumn("ASK_ACCOUNT__LOCKED");//주문 중 묶여있는 금액/수량
    	OUT_REST.addColumn("ASK_ACCOUNT__AVG_BUY_PRICE");//매수평균가
    	OUT_REST.addColumn("ASK_ACCOUNT__AVG_BUY_PRICE_MODIFIED");//매수평균가 수정 여부
    	OUT_REST.addColumn("ASK_ACCOUNT__UNIT_CURRENCY");//평단가 기준 화폐

    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String MARKET =null;
    	if(IN_PSET.getRowCount()>0) {
    		MARKET = IN_PSET.getRow(0).getStringNullToEmpty("MARKET");
    	}

    	HashMap<String, String> params = new HashMap<>();
		params.put("market",MARKET);
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
    		if(c.get("bid_fee")!=null){
				dr.setString("BID_FEE",c.get("bid_fee").toString());
			}
			if(c.get("ask_fee")!=null){
				dr.setString("ASK_FEE",c.get("ask_fee").toString());
			}
			HashMap market = (HashMap) c.get("market");

			if(c.get("market")!=null){
				dr.setString("MARKET__ID",market.get("id").toString());
				dr.setString("MARKET__NAME",market.get("name").toString());
				dr.setString("MARKET__MAX_TOTAL",market.get("max_total").toString());
				dr.setString("MARKET__STATE",market.get("state").toString());

				ArrayList<String> order_types = (ArrayList<String>)market.get("order_types");
				ArrayList<String> order_sides = (ArrayList<String>)market.get("order_sides");

				dr.setString("MARKET__ORDER_TYPES",String.join(",", order_types));
				dr.setString("MARKET__ORDER_SIDES",String.join(",", order_sides));

				HashMap bid = (HashMap) market.get("bid");

				dr.setString("MARKET__BID__CURRENCY",bid.get("currency").toString());
				if(bid.get("price_unit")!=null) {
					dr.setBigDecimal("MARKET__BID__PRICE_UNIT",new BigDecimal(Double.parseDouble(bid.get("price_unit").toString()))  );
				}
				dr.setBigDecimal("MARKET__BID__MIN_TOTAL",new BigDecimal(Double.parseDouble(bid.get("min_total").toString()))  );

				HashMap ask = (HashMap) market.get("ask");
				dr.setString("MARKET__ASK__CURRENCY",ask.get("currency").toString());
				if(ask.get("price_unit")!=null) {
					dr.setBigDecimal("MARKET__ASK__PRICE_UNIT",new BigDecimal(Double.parseDouble(ask.get("price_unit").toString()))  );
				}
				dr.setBigDecimal("MARKET__ASK__MIN_TOTAL",new BigDecimal(Double.parseDouble(ask.get("min_total").toString()))  );
			}

			HashMap bid_account = (HashMap) c.get("bid_account");
			dr.setString("BID_ACCOUNT__CURRENCY",bid_account.get("currency").toString());
			dr.setString("BID_ACCOUNT__BALANCE",bid_account.get("balance").toString());
			dr.setString("BID_ACCOUNT__LOCKED",bid_account.get("locked").toString());
			dr.setString("BID_ACCOUNT__AVG_BUY_PRICE",bid_account.get("avg_buy_price").toString());
			dr.setString("BID_ACCOUNT__AVG_BUY_PRICE_MODIFIED",bid_account.get("avg_buy_price_modified").toString());
			dr.setString("BID_ACCOUNT__UNIT_CURRENCY",bid_account.get("unit_currency").toString());

			HashMap ask_account = (HashMap) c.get("ask_account");
			dr.setString("ASK_ACCOUNT__CURRENCY",ask_account.get("currency").toString());
			dr.setString("ASK_ACCOUNT__BALANCE",ask_account.get("balance").toString());
			dr.setString("ASK_ACCOUNT__LOCKED",ask_account.get("locked").toString());
			dr.setString("ASK_ACCOUNT__AVG_BUY_PRICE",ask_account.get("avg_buy_price").toString());
			dr.setString("ASK_ACCOUNT__AVG_BUY_PRICE_MODIFIED",ask_account.get("avg_buy_price_modified").toString());
			dr.setString("ASK_ACCOUNT__UNIT_CURRENCY",ask_account.get("unit_currency").toString());
			dr.setString("ASK_ACCOUNT__CURRENCY",ask_account.get("unit_currency").toString());
		}

        return OUT_DS;
    }

}
