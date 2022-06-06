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

public class WalletEndPointsGetSapiV1DepositHistory extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetSapiV1DepositHistory(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/sapi/v1/capital/deposit/hisrec";
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
		OUT_RSET.addColumn("AMOUNT", dataset.type.DataType.STRING, null, "amount");
		OUT_RSET.addColumn("COIN", dataset.type.DataType.STRING, null, "coin");
		OUT_RSET.addColumn("NETWORK", dataset.type.DataType.STRING, null, "network");
		OUT_RSET.addColumn("STATUS", dataset.type.DataType.STRING, null, "status");
		OUT_RSET.addColumn("ADDRESS", dataset.type.DataType.STRING, null, "address");
		OUT_RSET.addColumn("ADDRESS_TAG", dataset.type.DataType.STRING, null, "addressTag");
		OUT_RSET.addColumn("TX_ID", dataset.type.DataType.STRING, null, "txId");
		OUT_RSET.addColumn("INSERT_TIME", dataset.type.DataType.STRING, null, "insertTime");
		OUT_RSET.addColumn("TRANSFER_TYPE", dataset.type.DataType.STRING, null, "transferType");
		OUT_RSET.addColumn("CONFIRM_TIMES", dataset.type.DataType.STRING, null, "confirmTimes");

		DataTable dtKey = InDs.getTable("IN_KEY");
		DataTable dt = InDs.getTable("IN_PSET");

		String COIN = null;
		String STATUS = null;// 0(0:pending,6: credited but cannot withdraw, 1:success)
		String START_TIME = null; // Default: 90 days from current timestamp
		String END_TIME = null; // Default: present timestamp
		String OFFSET = null; // Default:0
		String LIMIT = null; // Default:1000, Max:1000
		String RECV_WINDOW = null;
		String TIMESTAMP = null;

		String BINANCE_API_KEY = null;
		String BINANCE_API_SECRET = null;

		if (dt.getRowCount() > 0) {
			COIN = dt.getRow(0).getStringNullToEmpty("COIN");
			STATUS = dt.getRow(0).getStringNullToEmpty("STATUS");
			START_TIME = dt.getRow(0).getStringNullToEmpty("START_TIME");
			END_TIME = dt.getRow(0).getStringNullToEmpty("END_TIME");
			OFFSET = dt.getRow(0).getStringNullToEmpty("OFFSET");
			LIMIT = dt.getRow(0).getStringNullToEmpty("LIMIT");
			RECV_WINDOW = dt.getRow(0).getStringNullToEmpty("RECV_WINDOW");
			TIMESTAMP = dt.getRow(0).getStringNullToEmpty("TIMESTAMP");
		}

		if (dtKey.getRowCount() > 0) {
			BINANCE_API_KEY = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_KEY");
			BINANCE_API_SECRET = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_SECRET");
		}
		HashMap<String, String> params = new HashMap<>();
		if (!PjtUtil.g().isEmpty(COIN)) {
			params.put("coin", COIN);
		}
		if (!PjtUtil.g().isEmpty(STATUS)) {
			params.put("status", STATUS);
		}
		if (!PjtUtil.g().isEmpty(START_TIME)) {
			params.put("startTime", START_TIME);
		}
		if (!PjtUtil.g().isEmpty(END_TIME)) {
			params.put("endTime", END_TIME);
		}

		if (!PjtUtil.g().isEmpty(OFFSET)) {
			params.put("offset", OFFSET);
		}

		if (!PjtUtil.g().isEmpty(LIMIT)) {
			params.put("limit", LIMIT);
		}

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
		for (int i = 0; i < al.size(); i++) {
			HashMap<String, Object> c = al.get(i);
			DataRow dr = OUT_RSET.addRow();
			dr.setString("AMOUNT", c.get("amount").toString());
			dr.setString("COIN", c.get("coin").toString());
			dr.setString("NETWORK", c.get("network").toString());
			dr.setString("STATUS", c.get("status").toString());
			dr.setString("ADDRESS", c.get("address").toString());
			dr.setString("ADDRESS_TAG", c.get("addressTag").toString());
			dr.setString("TX_ID", c.get("txId").toString());
			dr.setString("INSERT_TIME", c.get("insertTime").toString());
			dr.setString("TRANSFER_TYPE", c.get("transferType").toString());
			dr.setString("CONFIRM_TIMES", c.get("confirmTimes").toString());
		}

		return OUT_DS;
	}

}
