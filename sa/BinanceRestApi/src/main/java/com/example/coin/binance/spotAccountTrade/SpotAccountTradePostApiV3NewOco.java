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

public class SpotAccountTradePostApiV3NewOco extends SAProxy {
	public DataSet PostApiV3NewOco(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/api/v3/order/oco";
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
		OUT_RSET.addColumn("ORDER_LIST_ID", dataset.type.DataType.STRING, null, "orderListId");
		OUT_RSET.addColumn("CONTINGENCY_TYPE", dataset.type.DataType.STRING, null, "contingencyType");
		OUT_RSET.addColumn("LIST_STATUS_TYPE", dataset.type.DataType.STRING, null, "listStatusType");
		OUT_RSET.addColumn("LIST_ORDER_STATUS", dataset.type.DataType.STRING, null, "listOrderStatus");
		OUT_RSET.addColumn("LIST_CLIENT_ORDER_ID", dataset.type.DataType.STRING, null, "listClientOrderId");
		OUT_RSET.addColumn("TRANSACTION_TIME", dataset.type.DataType.STRING, null, "transactionTime");
		OUT_RSET.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol");

		DataTable OUT_RSET_ORDERS = OUT_DS.addTable("OUT_RSET_ORDERS");
		OUT_RSET_ORDERS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol");
		OUT_RSET_ORDERS.addColumn("ORDER_LIST_ID", dataset.type.DataType.STRING, null, "orderListId");
		OUT_RSET_ORDERS.addColumn("ORDER_ID", dataset.type.DataType.STRING, null, "orderId");
		OUT_RSET_ORDERS.addColumn("CLIENT_ORDER_ID", dataset.type.DataType.STRING, null, "ClientOrderId");

		DataTable OUT_RSET_ORDER_REPORTS = OUT_DS.addTable("OUT_RSET_ORDER_REPORTS");
		OUT_RSET_ORDER_REPORTS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol");
		OUT_RSET_ORDER_REPORTS.addColumn("ORDER_ID", dataset.type.DataType.STRING, null, "orderId");
		OUT_RSET_ORDER_REPORTS.addColumn("ORDER_LIST_ID", dataset.type.DataType.STRING, null, "orderListId");
		OUT_RSET_ORDER_REPORTS.addColumn("CLIENT_ORDER_ID", dataset.type.DataType.STRING, null, "clientOrderId");
		OUT_RSET_ORDER_REPORTS.addColumn("TRANSACT_TIME", dataset.type.DataType.STRING, null, "transactTime");

		OUT_RSET_ORDER_REPORTS.addColumn("PRICE", dataset.type.DataType.STRING, null, "price");
		OUT_RSET_ORDER_REPORTS.addColumn("ORIG_QTY", dataset.type.DataType.STRING, null, "origQty");
		OUT_RSET_ORDER_REPORTS.addColumn("EXECUTED_QTY", dataset.type.DataType.STRING, null, "executedQty");
		OUT_RSET_ORDER_REPORTS.addColumn("CUMMULATIVE_QUOTE_QTY", dataset.type.DataType.STRING, null,
				"cummulativeQuoteQty");
		OUT_RSET_ORDER_REPORTS.addColumn("STATUS", dataset.type.DataType.STRING, null, "status");
		OUT_RSET_ORDER_REPORTS.addColumn("TIME_IN_FORCE", dataset.type.DataType.STRING, null, "timeInForce");
		OUT_RSET_ORDER_REPORTS.addColumn("TYPE", dataset.type.DataType.STRING, null, "type");
		OUT_RSET_ORDER_REPORTS.addColumn("SIDE", dataset.type.DataType.STRING, null, "side");
		OUT_RSET_ORDER_REPORTS.addColumn("STOP_PRICE", dataset.type.DataType.STRING, null, "stopPrice");

		DataTable dtKey = InDs.getTable("IN_KEY");
		DataTable dt = InDs.getTable("IN_PSET");

		String SYMBOL = null;
		String LIST_CLIENT_ORDER_ID = null; // A unique Id for the entire orderList
		String SIDE = null;
		String QUANTITY = null;
		String LIMIT_CLIENT_ORDER_ID = null; // A unique Id for the limit order
		String PRICE = null;
		String LIMIT_ICEBERG_QTY = null;
		String STOP_CLIENT_ORDER_ID = null; // A unique Id for the stop loss/stop loss limit leg
		String STOP_PRICE = null;
		String STOP_LIMIT_PRICE = null; // If provided, stopLimitTimeInForce is required.
		String STOP_ICEBERG_QTY = null;
		String STOP_LIMIT_TIME_IN_FORCE = null; // Valid values are GTC/FOK/IOC
		String NEW_ORDER_RESP_TYPE = null;// Set the response JSON.

		String RECV_WINDOW = null;// The value cannot be greater than 60000
		String TIMESTAMP = null;

		String BINANCE_API_KEY = null;
		String BINANCE_API_SECRET = null;

		/*
		 * Price Restrictions: SELL: Limit Price > Last Price > Stop Price BUY: Limit
		 * Price < Last Price < Stop Price Quantity Restrictions: Both legs must have
		 * the same quantity ICEBERG quantities however do not have to be the same.
		 * Order Rate Limit OCO counts as 2 orders against the order rate limit.
		 */

		if (dt.getRowCount() > 0) {
			SYMBOL = dt.getRow(0).getStringNullToEmpty("SYMBOL");
			LIST_CLIENT_ORDER_ID = dt.getRow(0).getStringNullToEmpty("LIST_CLIENT_ORDER_ID");
			SIDE = dt.getRow(0).getStringNullToEmpty("SIDE");
			QUANTITY = dt.getRow(0).getStringNullToEmpty("QUANTITY");
			LIMIT_CLIENT_ORDER_ID = dt.getRow(0).getStringNullToEmpty("LIMIT_CLIENT_ORDER_ID");
			PRICE = dt.getRow(0).getStringNullToEmpty("PRICE");
			LIMIT_ICEBERG_QTY = dt.getRow(0).getStringNullToEmpty("LIMIT_ICEBERG_QTY");

			STOP_CLIENT_ORDER_ID = dt.getRow(0).getStringNullToEmpty("STOP_CLIENT_ORDER_ID");
			STOP_PRICE = dt.getRow(0).getStringNullToEmpty("STOP_PRICE");
			STOP_LIMIT_PRICE = dt.getRow(0).getStringNullToEmpty("STOP_LIMIT_PRICE");

			STOP_ICEBERG_QTY = dt.getRow(0).getStringNullToEmpty("STOP_ICEBERG_QTY");
			STOP_LIMIT_TIME_IN_FORCE = dt.getRow(0).getStringNullToEmpty("STOP_LIMIT_TIME_IN_FORCE");
			NEW_ORDER_RESP_TYPE = dt.getRow(0).getStringNullToEmpty("NEW_ORDER_RESP_TYPE");

			RECV_WINDOW = dt.getRow(0).getStringNullToEmpty("RECV_WINDOW");
			TIMESTAMP = dt.getRow(0).getStringNullToEmpty("TIMESTAMP");
		}

		if (dtKey.getRowCount() > 0) {
			BINANCE_API_KEY = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_KEY");
			BINANCE_API_SECRET = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_SECRET");
		}

		HashMap<String, String> params = new HashMap<>();
		params.put("symbol", SYMBOL);

		if (!PjtUtil.g().isEmpty(LIST_CLIENT_ORDER_ID)) {
			params.put("listClientOrderId", LIST_CLIENT_ORDER_ID);
		}

		params.put("side", SIDE);
		params.put("quantity", QUANTITY);

		if (!PjtUtil.g().isEmpty(LIMIT_CLIENT_ORDER_ID)) {
			params.put("limitClientOrderId", LIMIT_CLIENT_ORDER_ID);
		}

		params.put("price", PRICE);

		if (!PjtUtil.g().isEmpty(LIMIT_ICEBERG_QTY)) {
			params.put("limitClientOrderId", LIMIT_ICEBERG_QTY);
		}

		if (!PjtUtil.g().isEmpty(STOP_CLIENT_ORDER_ID)) {
			params.put("stopClientOrderId", STOP_CLIENT_ORDER_ID);
		}

		params.put("stopPrice", STOP_PRICE);

		if (!PjtUtil.g().isEmpty(STOP_LIMIT_PRICE)) {
			params.put("stopLimitPrice", STOP_LIMIT_PRICE);
		}

		if (!PjtUtil.g().isEmpty(STOP_ICEBERG_QTY)) {
			params.put("stopIcebergQty", STOP_ICEBERG_QTY);
		}

		if (!PjtUtil.g().isEmpty(STOP_LIMIT_TIME_IN_FORCE)) {
			params.put("stopLimitTimeInForce", STOP_LIMIT_TIME_IN_FORCE);
		}

		params.put("newOrderRespType", "FULL");

		if (!PjtUtil.g().isEmpty(RECV_WINDOW)) {
			params.put("recvWindow", RECV_WINDOW);
		}

		params.put("timestamp", TIMESTAMP);

		DataRow drRst = OUT_RST.addRow();
		drRst.setString("URL", URL);

		if (PjtUtil.g().isEmpty(SYMBOL)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "SYMBOL 이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(SIDE)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "SIDE 이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(QUANTITY)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "QUANTITY이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(PRICE)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "PRICE이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(STOP_PRICE)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "STOP_PRICE이 인풋으로 넘어오지 않았습니다.");
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
			drRst.setString("ERR_MSG", "TIMESTAMP-API키가 인풋으로 넘어오지 않았습니다.");
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

		String jsonOutString = "";

		HttpUtilBinance httpU = new HttpUtilBinance();
		ArrayList<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));
		try {
			jsonOutString = httpU.httpPostBiniance(BINANCE_API_KEY, BINANCE_API_SECRET, URL, queryString);
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
				DataRow DR_OUT_RSET = OUT_RSET.addRow();
				DR_OUT_RSET.setString("ORDER_LIST_ID", c.get("orderListId").toString());
				DR_OUT_RSET.setString("CONTINGENCY_TYPE", c.get("contingencyType").toString());
				DR_OUT_RSET.setString("LIST_STATUS_TYPE", c.get("listStatusType").toString());
				DR_OUT_RSET.setString("LIST_ORDER_STATUS", c.get("listOrderStatus").toString());
				DR_OUT_RSET.setString("LIST_CLIENT_ORDER_ID", c.get("listClientOrderId").toString());
				DR_OUT_RSET.setString("TRANSACTION_TIME", c.get("transactionTime").toString());
				DR_OUT_RSET.setString("SYMBOL", c.get("symbol").toString());
				if (c.get("orders") != null) {
					List<HashMap<String, Object>> al_orders = (List<HashMap<String, Object>>) c.get("orders");

					for (int j = 0; j < al_orders.size(); j++) {
						DataRow DR_OUT_RSET_ORDERS = OUT_RSET_ORDERS.addRow();
						HashMap<String, Object> tmp = al_orders.get(j);

						DR_OUT_RSET_ORDERS.setString("SYMBOL", tmp.get("symbol").toString());
						DR_OUT_RSET_ORDERS.setString("ORDER_LIST_ID", tmp.get("orderListId").toString());
						DR_OUT_RSET_ORDERS.setString("ORDER_ID", tmp.get("orderId").toString());
						DR_OUT_RSET_ORDERS.setString("CLIENT_ORDER_ID", tmp.get("clientOrderId").toString());
					}
				}

				if (c.get("orderReports") != null) {
					List<HashMap<String, Object>> al_orders_reports = (List<HashMap<String, Object>>) c
							.get("orderReports");
					for (int j = 0; j < al_orders_reports.size(); j++) {
						DataRow DR_OUT_RSET_ORDER_REPORTS = OUT_RSET_ORDER_REPORTS.addRow();
						HashMap<String, Object> tmp = al_orders_reports.get(j);

						DR_OUT_RSET_ORDER_REPORTS.setString("SYMBOL", tmp.get("symbol").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("ORDER_ID", tmp.get("orderId").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("ORDER_LIST_ID", tmp.get("orderListId").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("CLIENT_ORDER_ID", tmp.get("clientOrderId").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("TRANSACT_TIME", tmp.get("transactTime").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("PRICE", tmp.get("price").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("ORIG_QTY", tmp.get("origQty").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("EXECUTED_QTY", tmp.get("executedQty").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("CUMMULATIVE_QUOTE_QTY",
								tmp.get("cummulativeQuoteQty").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("STATUS", tmp.get("status").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("TIME_IN_FORCE", tmp.get("timeInForce").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("TYPE", tmp.get("type").toString());
						DR_OUT_RSET_ORDER_REPORTS.setString("SIDE", tmp.get("side").toString());

						if (tmp.get("stopPrice") != null) {
							DR_OUT_RSET_ORDER_REPORTS.setString("STOP_PRICE", tmp.get("stopPrice").toString());
						}
					}
				}

			}

			return OUT_DS;
		} catch (NoSuchAlgorithmException | URISyntaxException |

				IOException e) {
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

	}

}
