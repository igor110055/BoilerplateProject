package com.example.coin.binance.spotAccountTrade;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
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

public class SpotAccountTradeGetApiV3AccountInformation extends SAProxy {
	public DataSet GetApiV3AccountInformation(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/api/v3/account";
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
		OUT_RSET.addColumn("MAKER_COMMISSION", dataset.type.DataType.STRING, null, "makerCommission");
		OUT_RSET.addColumn("TAKER_COMMISSION", dataset.type.DataType.STRING, null, "takerCommission");
		OUT_RSET.addColumn("BUYER_COMMISSION", dataset.type.DataType.STRING, null, "buyerCommission");
		OUT_RSET.addColumn("SELLER_COMMISSION", dataset.type.DataType.STRING, null, "sellerCommission");
		OUT_RSET.addColumn("CAN_TRADE", dataset.type.DataType.STRING, null, "canTrade");
		OUT_RSET.addColumn("CAN_WITHDRAW", dataset.type.DataType.STRING, null, "canWithdraw");
		OUT_RSET.addColumn("CAN_DEPOSIT", dataset.type.DataType.STRING, null, "canDeposit");
		OUT_RSET.addColumn("UPDATE_TIME", dataset.type.DataType.STRING, null, "updateTime");
		OUT_RSET.addColumn("ACCOUNT_TYPE", dataset.type.DataType.STRING, null, "accountType");

		DataTable OUT_RSET_BALANCES = OUT_DS.addTable("OUT_RSET_BALANCES");
		OUT_RSET_BALANCES.addColumn("ASSET", dataset.type.DataType.STRING, null, "asset");
		OUT_RSET_BALANCES.addColumn("FREE", dataset.type.DataType.BIGDECIMAL, null, "free");
		OUT_RSET_BALANCES.addColumn("LOCKED", dataset.type.DataType.BIGDECIMAL, null, "locked");

		DataTable OUT_RSET_PERMISSIONS = OUT_DS.addTable("OUT_RSET_PERMISSIONS");
		OUT_RSET_PERMISSIONS.addColumn("TYPE", dataset.type.DataType.STRING, null, "spot");

		DataTable dtKey = InDs.getTable("IN_KEY");
		DataTable dt = InDs.getTable("IN_PSET");

		String RECV_WINDOW = null;// The value cannot be greater than 60000
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

		params.put("timestamp", TIMESTAMP);

		DataRow drRst = OUT_RST.addRow();
		drRst.setString("URL", URL);

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

		Map<String, Object> c = null;
		try {
			c = PjtUtil.g().JsonStringToObject(jsonOutString, Map.class);
			drRst.setString("STATUS", "S");
			if (c != null) {
				DataRow DR_OUT_RSET = OUT_RSET.addRow();
				DR_OUT_RSET.setString("MAKER_COMMISSION", c.get("makerCommission").toString());
				DR_OUT_RSET.setString("TAKER_COMMISSION", c.get("takerCommission").toString());
				DR_OUT_RSET.setString("BUYER_COMMISSION", c.get("buyerCommission").toString());
				DR_OUT_RSET.setString("SELLER_COMMISSION", c.get("sellerCommission").toString());
				DR_OUT_RSET.setString("CAN_TRADE", c.get("canTrade").toString());
				DR_OUT_RSET.setString("CAN_WITHDRAW", c.get("canWithdraw").toString());
				DR_OUT_RSET.setString("CAN_DEPOSIT", c.get("canDeposit").toString());
				DR_OUT_RSET.setString("UPDATE_TIME", c.get("updateTime").toString());
				DR_OUT_RSET.setString("ACCOUNT_TYPE", c.get("accountType").toString());

				if (c.get("balances") != null) {
					List<HashMap<String, Object>> al_balances = (List<HashMap<String, Object>>) c.get("balances");

					for (int j = 0; j < al_balances.size(); j++) {
						DataRow DR_OUT_RSET_BALANCES = OUT_RSET_BALANCES.addRow();
						HashMap<String, Object> tmp = al_balances.get(j);
						DR_OUT_RSET_BALANCES.setString("ASSET", tmp.get("asset").toString());
                        DR_OUT_RSET_BALANCES.setBigDecimal("FREE", new BigDecimal(tmp.get("free").toString()));
                        DR_OUT_RSET_BALANCES.setBigDecimal("LOCKED", new BigDecimal(tmp.get("locked").toString()));
					}
				}

				if (c.get("permissions") != null) {
					List<String> al_permissions = (List<String>) c.get("permissions");

					for (int j = 0; j < al_permissions.size(); j++) {
						DataRow DR_OUT_RSET_PERMISSIONS = OUT_RSET_PERMISSIONS.addRow();
						String tmp = al_permissions.get(j);
						DR_OUT_RSET_PERMISSIONS.setString("TYPE", tmp);
					}
				}
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
