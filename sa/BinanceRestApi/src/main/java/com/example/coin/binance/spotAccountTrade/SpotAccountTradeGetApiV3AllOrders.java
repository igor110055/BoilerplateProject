package com.example.coin.binance.spotAccountTrade;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.coin.util.HttpUtilBinance;
import com.example.framework.exception.BizException;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;

public class SpotAccountTradeGetApiV3AllOrders extends SAProxy {
	public DataSet GetApiV3AllOrders(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/api/v3/allOrders";
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
		OUT_RST.addColumn("BINANCE_API_KEY");
		OUT_RST.addColumn("BINANCE_API_SECRET");

		DataTable OUT_RSET = OUT_DS.addTable("OUT_RSET");

		OUT_RSET.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol");
		OUT_RSET.addColumn("ORDER_ID", dataset.type.DataType.STRING, null, "orderId");
		OUT_RSET.addColumn("ORDER_LIST_ID", dataset.type.DataType.STRING, null, "orderListId");
		OUT_RSET.addColumn("CLIENT_ORDER_ID", dataset.type.DataType.STRING, null, "clientOrderId");
		OUT_RSET.addColumn("PRICE", dataset.type.DataType.STRING, null, "price");
		OUT_RSET.addColumn("ORIG_QTY", dataset.type.DataType.STRING, null, "origQty");
		OUT_RSET.addColumn("EXECUTED_QTY", dataset.type.DataType.STRING, null, "executedQty");
		OUT_RSET.addColumn("CUMMULATIVE_QUOTE_QTY", dataset.type.DataType.STRING, null, "cummulativeQuoteQty");
		OUT_RSET.addColumn("STATUS", dataset.type.DataType.STRING, null, "status");
		OUT_RSET.addColumn("TIME_IN_FORCE", dataset.type.DataType.STRING, null, "timeInForce");
		OUT_RSET.addColumn("TYPE", dataset.type.DataType.STRING, null, "type");
		OUT_RSET.addColumn("SIDE", dataset.type.DataType.STRING, null, "side");

		OUT_RSET.addColumn("STOP_PRICE", dataset.type.DataType.STRING, null, "stopPrice");
		OUT_RSET.addColumn("ICEBERG_QTY", dataset.type.DataType.STRING, null, "icebergQty");
		OUT_RSET.addColumn("TIME", dataset.type.DataType.STRING, null, "time");
		OUT_RSET.addColumn("UPDATE_TIME", dataset.type.DataType.STRING, null, "updateTime");
		OUT_RSET.addColumn("IS_WORKING", dataset.type.DataType.STRING, null, "isWorking");
		OUT_RSET.addColumn("ORIG_QUOTE_ORDER_QTY", dataset.type.DataType.STRING, null, "origQuoteOrderQty");

		DataTable dtKey = InDs.getTable("IN_KEY");
		DataTable dt = InDs.getTable("IN_PSET");

		String SYMBOL = null;
		String ORDER_ID = null;
		String START_TIME = null;
		String END_TIME = null;
		String LIMIT = null;

		String RECV_WINDOW = null;
		String TIMESTAMP = null;

		String BINANCE_API_KEY = null;
		String BINANCE_API_SECRET = null;

		if (dt.getRowCount() > 0) {
			SYMBOL = dt.getRow(0).getStringNullToEmpty("SYMBOL");
			ORDER_ID = dt.getRow(0).getStringNullToEmpty("ORDER_ID");
			START_TIME = dt.getRow(0).getStringNullToEmpty("START_TIME");
			END_TIME = dt.getRow(0).getStringNullToEmpty("END_TIME");
			LIMIT = dt.getRow(0).getStringNullToEmpty("LIMIT");

			RECV_WINDOW = dt.getRow(0).getStringNullToEmpty("RECV_WINDOW");
			TIMESTAMP = dt.getRow(0).getStringNullToEmpty("TIMESTAMP");
		}

		if (dtKey.getRowCount() > 0) {
			BINANCE_API_KEY = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_KEY");
			BINANCE_API_SECRET = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_SECRET");
		}

		DataRow drRst = OUT_RST.addRow();
		drRst.setString("URL", URL);

		HashMap<String, String> params = new HashMap<>();
		params.put("symbol", SYMBOL);

		if (!PjtUtil.g().isEmpty(ORDER_ID)) {
			params.put("orderId", ORDER_ID);
		}
		if (!PjtUtil.g().isEmpty(START_TIME)) {
			params.put("startTime", START_TIME);
		}

		if (!PjtUtil.g().isEmpty(END_TIME)) {
			params.put("endTime", END_TIME);
		}

		if (!PjtUtil.g().isEmpty(LIMIT)) {
			params.put("limit", LIMIT);
		}

		if (!PjtUtil.g().isEmpty(RECV_WINDOW)) {
			params.put("recvWindow", RECV_WINDOW);
		}

		params.put("timestamp", TIMESTAMP);

		if (PjtUtil.g().isEmpty(SYMBOL)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "SYMBOL이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(TIMESTAMP)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "TIMESTAMP가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(BINANCE_API_KEY)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "BINANCE_API_KEY-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(BINANCE_API_SECRET)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "BINANCE_API_SECRET-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		ArrayList<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));
		String jsonOutString = "";

		drRst.setString("QUERY_STRING", queryString);

		HttpUtilBinance httpU = new HttpUtilBinance();
		try {
			jsonOutString = httpU.httpGetBiniance(BINANCE_API_KEY, BINANCE_API_SECRET, URL, queryString);
		} catch (NoSuchAlgorithmException | URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();

			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "100");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
			return OUT_DS;
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();

			drRst.setString("JSON_OUT", jsonOutString);
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
			return OUT_DS;
		}
		drRst.setString("JSON_OUT", jsonOutString);

		System.out.println(jsonOutString);

		List<Map<String, Object>> al = null;
		try {
			al = PjtUtil.g().JsonStringToObject(jsonOutString, List.class);
			drRst.setString("STATUS", "S");

			for (int i = 0; i < al.size(); i++) {
				Map<String, Object> c = al.get(i);
				DataRow DR_OUT_RSET = OUT_RSET.addRow();
				DR_OUT_RSET.setString("SYMBOL", c.get("symbol").toString());
				DR_OUT_RSET.setString("ORDER_ID", c.get("orderId").toString());
				DR_OUT_RSET.setString("ORDER_LIST_ID", c.get("orderListId").toString());
				DR_OUT_RSET.setString("CLIENT_ORDER_ID", c.get("clientOrderId").toString());
				DR_OUT_RSET.setString("PRICE", c.get("price").toString());
				DR_OUT_RSET.setString("ORIG_QTY", c.get("origQty").toString());
				DR_OUT_RSET.setString("EXECUTED_QTY", c.get("executedQty").toString());
				DR_OUT_RSET.setString("CUMMULATIVE_QUOTE_QTY", c.get("cummulativeQuoteQty").toString());
				DR_OUT_RSET.setString("STATUS", c.get("status").toString());
				DR_OUT_RSET.setString("TIME_IN_FORCE", c.get("timeInForce").toString());
				DR_OUT_RSET.setString("TYPE", c.get("type").toString());
				DR_OUT_RSET.setString("SIDE", c.get("side").toString());

				DR_OUT_RSET.setString("STOP_PRICE", c.get("stopPrice").toString());
				DR_OUT_RSET.setString("ICEBERG_QTY", c.get("icebergQty").toString());
				DR_OUT_RSET.setString("TIME", c.get("time").toString());
				DR_OUT_RSET.setString("UPDATE_TIME", c.get("updateTime").toString());
				DR_OUT_RSET.setString("IS_WORKING", c.get("isWorking").toString());
				DR_OUT_RSET.setString("ORIG_QUOTE_ORDER_QTY", c.get("origQuoteOrderQty").toString());
			}

			return OUT_DS;
		} catch (

		JsonProcessingException e) {
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

	}

}
