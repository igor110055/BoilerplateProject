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

public class MarketDataEndPointsGetApiV3KlineCandlestickData extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetApiV3KlineCandlestickData(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/api/v3/klines";
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
		OUT_REST.addColumn("OPEN_TIME", dataset.type.DataType.STRING, null, "Open time");
		OUT_REST.addColumn("OPEN", dataset.type.DataType.STRING, null, "Open");
		OUT_REST.addColumn("HIGH", dataset.type.DataType.STRING, null, "High");
		OUT_REST.addColumn("LOW", dataset.type.DataType.STRING, null, "Low");
		OUT_REST.addColumn("CLOSE", dataset.type.DataType.STRING, null, "Close");
		OUT_REST.addColumn("VOLUME", dataset.type.DataType.STRING, null, "Volume");
		OUT_REST.addColumn("CLOSE_TIME", dataset.type.DataType.STRING, null, "Close time");
		OUT_REST.addColumn("QUOTE_ASSET_VOLUME", dataset.type.DataType.STRING, null, "Quote asset volume");
		OUT_REST.addColumn("NUMBER_OF_TRADES", dataset.type.DataType.STRING, null, "Number of trades");
		OUT_REST.addColumn("TAKER_BUY_BASE_ASSET_VOLUME", dataset.type.DataType.STRING, null,
				"Taker buy base asset volume");
		OUT_REST.addColumn("TAKER_BUY_QUOTE_ASSET_VOLUME", dataset.type.DataType.STRING, null,
				"Taker buy quote asset volume");
		OUT_REST.addColumn("IGNORE", dataset.type.DataType.STRING, null, "Ignore");
		OUT_REST.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "SYMBOL");
		OUT_REST.addColumn("INTERVAL", dataset.type.DataType.STRING, null, "INTERVAL");

		DataTable IN_PSET = InDs.getTable("IN_PSET");
		String SYMBOL = "";
		String INTERVAL = "";
		String START_TIME = "";
		String END_TIME = "";
		String LIMIT = "";

		if (IN_PSET.getRowCount() > 0) {
			SYMBOL = IN_PSET.getRow(0).getStringNullToEmpty("SYMBOL");
			INTERVAL = IN_PSET.getRow(0).getStringNullToEmpty("INTERVAL");
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
		params.put("interval", INTERVAL);

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

		if (PjtUtil.g().isEmpty(INTERVAL)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "INTERVAL이 인풋으로 넘어오지 않았습니다.");
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

		ArrayList<ArrayList> al = null;
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
				ArrayList c = al.get(i);
				DataRow dr = OUT_REST.addRow();
				dr.setString("OPEN_TIME", c.get(0).toString());
				dr.setString("OPEN", c.get(1).toString());
				dr.setString("HIGH", c.get(2).toString());
				dr.setString("LOW", c.get(3).toString());

				dr.setString("CLOSE", c.get(4).toString());
				dr.setString("VOLUME", c.get(5).toString());
				dr.setString("CLOSE_TIME", c.get(6).toString());
				dr.setString("QUOTE_ASSET_VOLUME", c.get(7).toString());
				dr.setString("NUMBER_OF_TRADES", c.get(8).toString());
				dr.setString("TAKER_BUY_BASE_ASSET_VOLUME", c.get(9).toString());
				dr.setString("TAKER_BUY_QUOTE_ASSET_VOLUME", c.get(10).toString());
				dr.setString("IGNORE", c.get(11).toString());
				dr.setString("SYMBOL", SYMBOL);
				dr.setString("INTERVAL", INTERVAL);
			}
		}
		return OUT_DS;
	}

}
