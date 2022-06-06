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

public class MarketDataEndPointsGetApiV3RecentTradeList extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetApiV3RecentTradeList(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/api/v3/trades";
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
		OUT_REST.addColumn("ID", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("PRICE", dataset.type.DataType.BIGDECIMAL, null, "");
		OUT_REST.addColumn("QTY", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("QUOTE_QTY", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("TIME", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("IS_BUYER_MAKER", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("IS_BEST_MATCH", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("SEQ", dataset.type.DataType.INTEGER, null, "");

		DataTable IN_PSET = InDs.getTable("IN_PSET");
		String SYMBOL = "";
		String LIMIT = "";

		if (IN_PSET.getRowCount() > 0) {
			SYMBOL = IN_PSET.getRow(0).getStringNullToEmpty("SYMBOL");
			LIMIT = IN_PSET.getRow(0).getStringNullToEmpty("LIMIT");
		}

		if (PjtUtil.g().isEmpty(LIMIT)) {
			LIMIT = "500";
		}

		HashMap<String, String> params = new HashMap<>();
		params.put("symbol", SYMBOL);
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
				dr.setString("ID", c.get("id").toString());
				dr.setBigDecimal("PRICE", new BigDecimal(c.get("price").toString()));
				dr.setString("QTY", c.get("qty").toString());
				dr.setString("QUOTE_QTY", c.get("quoteQty").toString());
				dr.setString("TIME", c.get("time").toString());
				dr.setString("IS_BUYER_MAKER", c.get("isBuyerMaker").toString());
				dr.setString("IS_BEST_MATCH", c.get("isBestMatch").toString());
				dr.setString("SYMBOL", SYMBOL);
				dr.setInt("SEQ", (i + 1));
			}

		}

		return OUT_DS;
	}

}
