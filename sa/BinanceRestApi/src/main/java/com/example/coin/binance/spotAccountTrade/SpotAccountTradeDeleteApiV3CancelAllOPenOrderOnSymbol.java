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

public class SpotAccountTradeDeleteApiV3CancelAllOPenOrderOnSymbol extends SAProxy {
	public DataSet DeleteApiV3CancelAllOPenOrderOnSymbol(DataSet InDs, String InDsNames, String outDsNames)
			throws Exception {
		// The error code "Error -2011" means "CANCEL_REJECTED" according to this page.

		String URL = "https://api.binance.com/api/v3/openOrders";
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
		OUT_RSET.addColumn("ORIG_CLIENT_ORDER_ID", dataset.type.DataType.STRING, null, "origClientOrderId");
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

		DataTable OUT_RSET_ORDER_LIST_ID = OUT_DS.addTable("OUT_RSET_ORDER_LIST_ID");
		OUT_RSET_ORDER_LIST_ID.addColumn("ORDER_LIST_ID", dataset.type.DataType.STRING, null, "orderListId");
		OUT_RSET_ORDER_LIST_ID.addColumn("CONTINGENCY_TYPE", dataset.type.DataType.STRING, null, "contingencyType");
		OUT_RSET_ORDER_LIST_ID.addColumn("LIST_STATUS_TYPE", dataset.type.DataType.STRING, null, "listStatusType");
		OUT_RSET_ORDER_LIST_ID.addColumn("LIST_ORDER_STATUS", dataset.type.DataType.STRING, null, "listOrderStatus");
		OUT_RSET_ORDER_LIST_ID.addColumn("LIST_CLIENT_ORDER_ID", dataset.type.DataType.STRING, null,
				"listClientOrderId");
		OUT_RSET_ORDER_LIST_ID.addColumn("TRANSACTION_TIME", dataset.type.DataType.STRING, null, "transactionTime");
		OUT_RSET_ORDER_LIST_ID.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol");

		DataTable OUT_RSET_ORDER_LIST_ID_ORDERS = OUT_DS.addTable("OUT_RSET_ORDER_LIST_ID_ORDERS");
		OUT_RSET_ORDER_LIST_ID_ORDERS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol");

		OUT_RSET_ORDER_LIST_ID_ORDERS.addColumn("ORDER_LIST_ID", dataset.type.DataType.STRING, null, "orderListId");
		OUT_RSET_ORDER_LIST_ID_ORDERS.addColumn("ORDER_ID", dataset.type.DataType.STRING, null, "orderId");
		OUT_RSET_ORDER_LIST_ID_ORDERS.addColumn("CLIENT_ORDER_ID", dataset.type.DataType.STRING, null, "clientOrderId");

		DataTable OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS = OUT_DS.addTable("OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("ORIG_CLIENT_ORDER_ID", dataset.type.DataType.STRING, null,
				"origClientOrderId");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("ORDER_ID", dataset.type.DataType.STRING, null, "orderId");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("ORDER_LIST_ID", dataset.type.DataType.STRING, null,
				"orderListId");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("PRICE", dataset.type.DataType.STRING, null, "price");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("ORIG_QTY", dataset.type.DataType.STRING, null, "origQty");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("EXECUTED_QTY", dataset.type.DataType.STRING, null,
				"executedQty");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("CUMMULATIVE_QUOTE_QTY", dataset.type.DataType.STRING, null,
				"cummulativeQuoteQty");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("STATUS", dataset.type.DataType.STRING, null, "status");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("TIME_IN_FORCE", dataset.type.DataType.STRING, null,
				"timeInForce");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("TYPE", dataset.type.DataType.STRING, null, "type");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("SIDE", dataset.type.DataType.STRING, null, "side");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("STOP_PRICE", dataset.type.DataType.STRING, null, "stopPrice");
		OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.addColumn("ICEBERG_QTY", dataset.type.DataType.STRING, null, "icebergQty");

		DataTable dtKey = InDs.getTable("IN_KEY");
		DataTable dt = InDs.getTable("IN_PSET");

		String SYMBOL = null;

		String RECV_WINDOW = null;
		String TIMESTAMP = null;

		String BINANCE_API_KEY = null;
		String BINANCE_API_SECRET = null;

		if (dt.getRowCount() > 0) {
			SYMBOL = dt.getRow(0).getStringNullToEmpty("SYMBOL");

			RECV_WINDOW = dt.getRow(0).getStringNullToEmpty("RECV_WINDOW");
			TIMESTAMP = dt.getRow(0).getStringNullToEmpty("TIMESTAMP");
		}

		if (dtKey.getRowCount() > 0) {
			BINANCE_API_KEY = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_KEY");
			BINANCE_API_SECRET = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_SECRET");
		}

		HashMap<String, String> params = new HashMap<>();
		params.put("symbol", SYMBOL);

		if (!PjtUtil.g().isEmpty(RECV_WINDOW)) {
			params.put("recvWindow", RECV_WINDOW);
		}

		if (!PjtUtil.g().isEmpty(TIMESTAMP)) {
			params.put("timestamp", TIMESTAMP);
		}

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
			jsonOutString = httpU.httpDeleteBiniance(BINANCE_API_KEY, BINANCE_API_SECRET, URL, queryString);
			drRst.setString("JSON_OUT", jsonOutString);

			System.out.println(jsonOutString);

			List<HashMap<String, Object>> al = null;
			try {
				al = PjtUtil.g().JsonStringToObject(jsonOutString, List.class);
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

			for (int i = 0; i < al.size(); i++) {
				HashMap<String, Object> c = al.get(i);
				if (c.get("orderListId").toString().equals("-1")) {
					DataRow DR_OUT_RSET = OUT_RSET.addRow();
					DR_OUT_RSET.setString("SYMBOL", c.get("symbol").toString());
					DR_OUT_RSET.setString("ORIG_CLIENT_ORDER_ID", c.get("origClientOrderId").toString());
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
				} else {

					DataRow DR_OUT_RSET_ORDER_LIST_ID = OUT_RSET_ORDER_LIST_ID.addRow();
					DR_OUT_RSET_ORDER_LIST_ID.setString("ORDER_LIST_ID", c.get("orderListId").toString());
					DR_OUT_RSET_ORDER_LIST_ID.setString("CONTINGENCY_TYPE", c.get("contingencyType").toString());
					DR_OUT_RSET_ORDER_LIST_ID.setString("LIST_STATUS_TYPE", c.get("listStatusType").toString());
					DR_OUT_RSET_ORDER_LIST_ID.setString("LIST_ORDER_STATUS", c.get("listOrderStatus").toString());
					DR_OUT_RSET_ORDER_LIST_ID.setString("LIST_CLIENT_ORDER_ID", c.get("listClientOrderId").toString());
					DR_OUT_RSET_ORDER_LIST_ID.setString("TRANSACTION_TIME", c.get("transactionTime").toString());
					DR_OUT_RSET_ORDER_LIST_ID.setString("SYMBOL", c.get("symbol").toString());
					if (c.get("orders") != null) {
						List<HashMap<String, Object>> al_orders = (List<HashMap<String, Object>>) c.get("orders");

						for (int j = 0; j < al_orders.size(); j++) {
							DataRow DR_OUT_RSET_ORDER_LIST_ID_ORDERS = OUT_RSET_ORDER_LIST_ID_ORDERS.addRow();
							HashMap<String, Object> tmp = al_orders.get(j);

							DR_OUT_RSET_ORDER_LIST_ID_ORDERS.setString("SYMBOL", tmp.get("symbol").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDERS.setString("ORDER_LIST_ID",
									tmp.get("orderListId").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDERS.setString("ORDER_ID", tmp.get("orderId").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDERS.setString("CLIENT_ORDER_ID",
									tmp.get("clientOrderId").toString());
						}
					}

					if (c.get("orderReports") != null) {
						List<HashMap<String, Object>> al_orders_reports = (List<HashMap<String, Object>>) c
								.get("orderReports");

						for (int j = 0; j < al_orders_reports.size(); j++) {
							DataRow DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS = OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS
									.addRow();
							HashMap<String, Object> tmp = al_orders_reports.get(j);

							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("SYMBOL", tmp.get("symbol").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("ORIG_CLIENT_ORDER_ID",
									tmp.get("origClientOrderId").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("ORDER_ID",
									tmp.get("orderId").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("ORDER_LIST_ID",
									tmp.get("orderListId").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("CLIENT_ORDER_ID",
									tmp.get("clientOrderId").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("PRICE", tmp.get("price").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("ORIG_QTY",
									tmp.get("origQty").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("EXECUTED_QTY",
									tmp.get("executedQty").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("CUMMULATIVE_QUOTE_QTY",
									tmp.get("cummulativeQuoteQty").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("STATUS", tmp.get("status").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("TIME_IN_FORCE",
									tmp.get("timeInForce").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("TYPE", tmp.get("type").toString());
							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("SIDE", tmp.get("side").toString());

							if (tmp.get("stopPrice") != null) {
								DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("STOP_PRICE",
										tmp.get("stopPrice").toString());
							}

							DR_OUT_RSET_ORDER_LIST_ID_ORDER_REPORTS.setString("ICEBERG_QTY",
									tmp.get("icebergQty").toString());
						}
					}

				}

			}

			return OUT_DS;
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

	}

}
