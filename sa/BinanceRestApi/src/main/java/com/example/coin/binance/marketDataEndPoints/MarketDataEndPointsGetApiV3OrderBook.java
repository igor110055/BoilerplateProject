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
import com.example.framework.exception.BizException;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;

public class MarketDataEndPointsGetApiV3OrderBook extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetApiV3OrderBook(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/api/v3/depth";
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
		OUT_REST.addColumn("LAST_UPDATE_ID", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("LIMIT", dataset.type.DataType.STRING, null, "");

		DataTable OUT_RSET_ORDER_BOOK = OUT_DS.addTable("OUT_RSET_ORDER_BOOK");
		OUT_RSET_ORDER_BOOK.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_ORDER_BOOK.addColumn("BID_PRICE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_ORDER_BOOK.addColumn("BID_QTY", dataset.type.DataType.STRING, null, "");
		OUT_RSET_ORDER_BOOK.addColumn("ASK_PRICE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_ORDER_BOOK.addColumn("ASK_QTY", dataset.type.DataType.STRING, null, "");
		OUT_RSET_ORDER_BOOK.addColumn("SEQ", dataset.type.DataType.INTEGER, null, "");

		DataTable IN_PSET = InDs.getTable("IN_PSET");
		String SYMBOL = "";
		String LIMIT = "";

		if (IN_PSET.getRowCount() > 0) {
			SYMBOL = IN_PSET.getRow(0).getStringNullToEmpty("SYMBOL");
			LIMIT = IN_PSET.getRow(0).getStringNullToEmpty("LIMIT");
		}

		if (PjtUtil.g().isEmpty(LIMIT)) {
			LIMIT = "100";
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

		if (!PjtUtil.g().isEmpty(LIMIT)) {
			if (!(LIMIT.equals("5") || LIMIT.equals("10") || LIMIT.equals("20") || LIMIT.equals("50")
					|| LIMIT.equals("100") || LIMIT.equals("500") || LIMIT.equals("1000") || LIMIT.equals("5000"))) {

				// 에러처리
				drRst.setString("JSON_OUT", "");
				drRst.setString("STATUS", "E");
				drRst.setString("ERR_MSG", "LIMIT는 (5,10,20,50,100,500,1000,5000)중 하나 이어야 합니다.");
				drRst.setString("ERR_CODE", "200");
				drRst.setString("ERR_STACK_TRACE", "");

				return OUT_DS;
			}
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
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			// System.out.println(exceptionAsString);
			// 출처: https://blog.miyam.net/81 [낭만 프로그래머]

			drRst.setString("JSON_OUT", jsonOutString);
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			return OUT_DS;
		}
		drRst.setString("JSON_OUT", jsonOutString);
		System.out.println(jsonOutString);

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
			dr.setString("LAST_UPDATE_ID", c.get("lastUpdateId").toString());
			dr.setBigDecimal("LAST_UPDATE_ID", new BigDecimal(c.get("lastUpdateId").toString()));
			dr.setString("SYMBOL", SYMBOL);
			dr.setString("LIMIT", LIMIT);

			if (c.get("bids") != null) {
				ArrayList<ArrayList<String>> al = (ArrayList<ArrayList<String>>) c.get("bids");

				for (int i = 0; i < al.size(); i++) {
					ArrayList<String> tmp = al.get(i);
					DataRow tmp_dr = OUT_RSET_ORDER_BOOK.addRow();
					tmp_dr.setString("SYMBOL", SYMBOL);
					tmp_dr.setString("BID_PRICE", tmp.get(0));
					tmp_dr.setString("BID_QTY", tmp.get(1));
					tmp_dr.setInt("SEQ", (i + 1));
				}
			}

			if (c.get("asks") != null) {
				ArrayList<ArrayList<String>> al = (ArrayList<ArrayList<String>>) c.get("asks");

				for (int i = 0; i < OUT_RSET_ORDER_BOOK.getRowCount(); i++) {

                    if(al.size() > i) {
                        ArrayList<String> tmp = al.get(i);
                        DataRow tmp_dr = OUT_RSET_ORDER_BOOK.getRow(i);
                        tmp_dr.setString("ASK_PRICE", tmp.get(0));
                        tmp_dr.setString("ASK_QTY", tmp.get(1));
                    }
                    
				}
			}
		}
		return OUT_DS;
	}
}
