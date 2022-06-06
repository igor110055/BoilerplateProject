package com.example.coin.binance.marketDataEndPoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.coin.util.HttpUtilBinance;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;

public class MarketDataEndPointsGetApiV3CompressedAggreateTradeList extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetApiV3CompressedAggreateTradeList(DataSet InDs, String InDsNames, String outDsNames)
			throws Exception {
		String URL = "https://api.binance.com/api/v3/aggTrades";
		DataSet OUT_DS = new DataSet();
		/* 상태 */
		DataTable OUT_RST = OUT_DS.addTable("OUT_RST");
		OUT_RST.addColumn("URL");
		OUT_RST.addColumn("QUERY_STRING");
		OUT_RST.addColumn("JSON_OUT");
		OUT_RST.addColumn("STATUS"); // E에러 S 성공
		OUT_RST.addColumn("ERR_MSG");
		OUT_RST.addColumn("ERR_CODE"); // 100 외부 api 오류 200 내부오류
		OUT_RST.addColumn("ERR_STACK_TRACE");

		DataTable OUT_REST = OUT_DS.addTable("OUT_RSET");
		OUT_REST.addColumn("AGGREGATE_TRADE_ID", dataset.type.DataType.STRING, null, "Aggregate tradeId");
		OUT_REST.addColumn("PRICE", dataset.type.DataType.BIGDECIMAL, null, "Price");
		OUT_REST.addColumn("QUANTITY", dataset.type.DataType.STRING, null, "Quantity");
		OUT_REST.addColumn("FIRST_TRADE_ID", dataset.type.DataType.STRING, null, "First tradeId");
		OUT_REST.addColumn("LAST_TRADE_ID", dataset.type.DataType.STRING, null, "Last tradeId");
		OUT_REST.addColumn("TIMESTAMP", dataset.type.DataType.STRING, null, "Timestamp");
		OUT_REST.addColumn("BUYER_MARKET", dataset.type.DataType.STRING, null, "Was the buyer the maker?");
		OUT_REST.addColumn("BEST_PRICE_MATCH", dataset.type.DataType.STRING, null,
				" Was the trade the best price match?");
		OUT_REST.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "SYMBOL");

		DataTable IN_PSET = InDs.getTable("IN_PSET");
		String SYMBOL = "";
		String FROM_ID = "";
		String START_TIME = "";
		String END_TIME = "";
		String LIMIT = "";

		if (IN_PSET.getRowCount() > 0) {
			SYMBOL = IN_PSET.getRow(0).getStringNullToEmpty("SYMBOL");
			FROM_ID = IN_PSET.getRow(0).getStringNullToEmpty("FROM_ID");
			START_TIME = IN_PSET.getRow(0).getStringNullToEmpty("START_TIME");
			END_TIME = IN_PSET.getRow(0).getStringNullToEmpty("END_TIME");
			LIMIT = IN_PSET.getRow(0).getStringNullToEmpty("LIMIT");
		}
		/*
		 * If startTime and endTime are sent, time between startTime and endTime must be
		 * less than 1 hour. If fromId, startTime, and endTime are not sent, the most
		 * recent aggregate trades will be returned.
		 */

		if (PjtUtil.g().isEmpty(LIMIT)) {
			LIMIT = "500";
		}

		HashMap<String, String> params = new HashMap<>();
		params.put("symbol", SYMBOL);
		if (!PjtUtil.g().isEmpty(FROM_ID)) {
			params.put("fromId", FROM_ID);
		}
		if (!PjtUtil.g().isEmpty(START_TIME)) {
			params.put("startTime", START_TIME);
		}
		if (!PjtUtil.g().isEmpty(END_TIME)) {
			params.put("endTime", END_TIME);
		}
		params.put("limit", LIMIT);

		ArrayList<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String QueryString = String.join("&", queryElements.toArray(new String[0]));

		DataRow drRst = OUT_RST.addRow();
		drRst.setString("URL", URL);
		drRst.setString("QUERY_STRING", QueryString);

		if (PjtUtil.g().isEmpty(SYMBOL)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "SYMBOL이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");

			return OUT_DS;
		}

		String jsonOutString = "";
		HttpUtilBinance httpU = new HttpUtilBinance();
		try {
			jsonOutString = httpU.httpGetBinianceGetUrlNoAuth(URL, QueryString);
		} catch (NoSuchAlgorithmException | URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			// System.out.println(exceptionAsString);
			// 출처: https://blog.miyam.net/81 [낭만 프로그래머]

			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "100");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			return OUT_DS;
		}
		drRst.setString("JSON_OUT", jsonOutString);

		ArrayList<HashMap<String, Object>> al = null;
		try {
			al = PjtUtil.g().JsonStringToObject(jsonOutString, ArrayList.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			// System.out.println(exceptionAsString);
			// 출처: https://blog.miyam.net/81 [낭만 프로그래머]

			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);

			return OUT_DS;
		}
		drRst.setString("STATUS", "S");

		if (al != null) {
			for (int i = 0; i < al.size(); i++) {
				HashMap<String, Object> c = al.get(i);
				DataRow dr = OUT_REST.addRow();
				dr.setString("AGGREGATE_TRADE_ID", c.get("a").toString());
				dr.setString("PRICE", c.get("p").toString());
				dr.setString("QUANTITY", c.get("q").toString());
				dr.setString("FIRST_TRADE_ID", c.get("f").toString());
				dr.setString("LAST_TRADE_ID", c.get("l").toString());
				dr.setString("TIMESTAMP", c.get("T").toString());
				dr.setString("BUYER_MARKET", c.get("m").toString());
				dr.setString("BEST_PRICE_MATCH", c.get("M").toString());
				dr.setString("SYMBOL", SYMBOL);

			}

		}

		return OUT_DS;
	}

}
