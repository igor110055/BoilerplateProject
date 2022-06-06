package com.example.coin.binance.marketDataEndPoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

public class MarketDataEndPointsGetApiV3AvgPrice extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetApiV3AvgPrice(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/api/v3/avgPrice";
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
		OUT_REST.addColumn("MINS", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("PRICE", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");

		DataTable IN_PSET = InDs.getTable("IN_PSET");
		String SYMBOL = "";

		if (IN_PSET.getRowCount() > 0) {
			SYMBOL = IN_PSET.getRow(0).getStringNullToEmpty("SYMBOL");
		}

		HashMap<String, String> params = new HashMap<>();
		params.put("symbol", SYMBOL);

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

		HashMap<String, Object> c = null;
		try {
			c = PjtUtil.g().JsonStringToObject(jsonOutString, HashMap.class);
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

		if (c != null) {
			DataRow dr = OUT_REST.addRow();
			dr.setString("MINS", c.get("mins").toString());
			dr.setString("PRICE", c.get("price").toString());
			dr.setString("SYMBOL", SYMBOL);

		}
		return OUT_DS;
	}
}
