package com.example.coin.binance.walletEndPoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
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

public class WalletEndPointsGetSapiV1AccountApiTradingStatus extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetSapiV1AccountApiTradingStatus(DataSet InDs, String InDsNames, String outDsNames)
			throws Exception {
		String URL = "https://api.binance.com/sapi/v1/account/apiTradingStatus";
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
		OUT_RSET.addColumn("IS_LOCKED", dataset.type.DataType.STRING, null, "isLocked"); // API trading function is
																							// locked or not
		OUT_RSET.addColumn("PLANNED_RECOVER_TIME", dataset.type.DataType.STRING, null, "plannedRecoverTime"); // If API
																												// trading
																												// function
																												// is
																												// locked,
																												// this
																												// is
																												// the
																												// planned
																												// recover
																												// time
		OUT_RSET.addColumn("TRIGGER_CONDITION__GCR", dataset.type.DataType.STRING, null, "triggerCondition__GCR"); // Number
																													// of
																													// GTC
																													// orders
		OUT_RSET.addColumn("TRIGGER_CONDITION__IFER", dataset.type.DataType.STRING, null, "triggerCondition__IFER"); // Number
																														// of
																														// FOK/IOC
																														// orders
		OUT_RSET.addColumn("TRIGGER_CONDITION__UFR", dataset.type.DataType.STRING, null, "triggerCondition__UFR"); // Number
																													// of
																													// orders
		OUT_RSET.addColumn("UPDATE_TIME", dataset.type.DataType.STRING, null, "updateTime");

		DataTable OUT_RSET_INDICATORS = OUT_DS.addTable("OUT_RSET_INDICATORS"); // The indicators updated every 30
																				// seconds
		OUT_RSET_INDICATORS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol"); // The symbol
		OUT_RSET_INDICATORS.addColumn("UFR_INDICATOR", dataset.type.DataType.STRING, null, "Unfilled Ratio (UFR)");
		OUT_RSET_INDICATORS.addColumn("UFR_COUNT", dataset.type.DataType.STRING, null, "Count of all orders");
		OUT_RSET_INDICATORS.addColumn("UFR_CURRENT", dataset.type.DataType.STRING, null, "Current UFR value");
		OUT_RSET_INDICATORS.addColumn("UFR_TRIGGER", dataset.type.DataType.STRING, null, "Trigger UFR value");

		OUT_RSET_INDICATORS.addColumn("IFER_INDICATOR", dataset.type.DataType.STRING, null,
				"IOC/FOK Expiration Ratio (IFER)");
		OUT_RSET_INDICATORS.addColumn("IFER_COUNT", dataset.type.DataType.STRING, null, "Count of FOK/IOC orders");
		OUT_RSET_INDICATORS.addColumn("IFER_CURRENT", dataset.type.DataType.STRING, null, "Current IFER value");
		OUT_RSET_INDICATORS.addColumn("IFER_TRIGGER", dataset.type.DataType.STRING, null, "Trigger IFER value");

		OUT_RSET_INDICATORS.addColumn("GCR_INDICATOR", dataset.type.DataType.STRING, null,
				"GTC Cancellation Ratio (GCR)");
		OUT_RSET_INDICATORS.addColumn("GCR_COUNT", dataset.type.DataType.STRING, null, "Count of GTC orders");
		OUT_RSET_INDICATORS.addColumn("GCR_CURRENT", dataset.type.DataType.STRING, null, "Current GCR value");
		OUT_RSET_INDICATORS.addColumn("GCR_TRIGGER", dataset.type.DataType.STRING, null, "Trigger GCR value");

		DataTable dtKey = InDs.getTable("IN_KEY");
		DataTable dt = InDs.getTable("IN_PSET");

		String RECV_WINDOW = null;
		String TIMESTAMP = null;

		String BINANCE_API_KEY = null;
		String BINANCE_API_SECRET = null;

		if (dt.getRowCount() > 0) {
			RECV_WINDOW = dt.getRow(0).getStringNullToEmpty("RECV_WINDOW");
			TIMESTAMP = dt.getRow(0).getStringNullToEmpty("TIMESTAMP");
		}

		if (dtKey.getRowCount() > 0) {
			BINANCE_API_KEY = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_KEY");
			BINANCE_API_SECRET = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_SECRET");
		}
		HashMap<String, String> params = new HashMap<>();
		if (!PjtUtil.g().isEmpty(RECV_WINDOW)) {
			params.put("recvWindow", RECV_WINDOW);
		}

		if (!PjtUtil.g().isEmpty(TIMESTAMP)) {
			params.put("timestamp", TIMESTAMP);
		}

		DataRow drRst = OUT_RST.addRow();
		drRst.setString("URL", URL);

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
			DataRow dr = OUT_RSET.addRow();
			if (c.get("data") != null) {
				HashMap<String, Object> d = (HashMap<String, Object>) c.get("data");
				dr.setString("IS_LOCKED", d.get("isLocked").toString());
				dr.setString("PLANNED_RECOVER_TIME", d.get("plannedRecoverTime").toString());

				if (d.get("triggerCondition") != null) {
					HashMap<String, Object> t = (HashMap<String, Object>) d.get("triggerCondition");
					dr.setString("TRIGGER_CONDITION__GCR", t.get("GCR").toString());
					dr.setString("TRIGGER_CONDITION__IFER", t.get("IFER").toString());
					dr.setString("TRIGGER_CONDITION__UFR", t.get("UFR").toString());
				}
				dr.setString("UPDATE_TIME", d.get("updateTime").toString());
				if (d.get("indicators") != null) {
					HashMap<String, Object> h = (HashMap<String, Object>) c.get("indicators");
					for (Map.Entry<String, Object> entry : h.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();

						System.out.println(key);
						System.out.println(value);
						DataRow DR_OUT_RSET_INDICATORS = OUT_RSET_INDICATORS.addRow();
						DR_OUT_RSET_INDICATORS.setString("SYMBOL", key);
						ArrayList<HashMap<String, Object>> al = (ArrayList<HashMap<String, Object>>) value;
						if (al.size() > 0) {
							HashMap<String, Object> ufr = al.get(0);
							DR_OUT_RSET_INDICATORS.setString("UFR_INDICATOR", ufr.get("i").toString());
							DR_OUT_RSET_INDICATORS.setString("UFR_COUNT", ufr.get("c").toString());
							DR_OUT_RSET_INDICATORS.setString("UFR_CURRENT", ufr.get("v").toString());
							DR_OUT_RSET_INDICATORS.setString("UFR_TRIGGER", ufr.get("t").toString());

							HashMap<String, Object> ifer = al.get(1);
							DR_OUT_RSET_INDICATORS.setString("IFER_INDICATOR", ifer.get("i").toString());
							DR_OUT_RSET_INDICATORS.setString("IFER_COUNT", ifer.get("c").toString());
							DR_OUT_RSET_INDICATORS.setString("IFER_CURRENT", ifer.get("v").toString());
							DR_OUT_RSET_INDICATORS.setString("IFER_TRIGGER", ifer.get("t").toString());

							HashMap<String, Object> gcr = al.get(2);
							DR_OUT_RSET_INDICATORS.setString("GCR_INDICATOR", gcr.get("i").toString());
							DR_OUT_RSET_INDICATORS.setString("GCR_COUNT", gcr.get("c").toString());
							DR_OUT_RSET_INDICATORS.setString("GCR_CURRENT", gcr.get("v").toString());
							DR_OUT_RSET_INDICATORS.setString("GCR_TRIGGER", gcr.get("t").toString());
						}
					}
				}
			}
		}

		return OUT_DS;
	}

}
