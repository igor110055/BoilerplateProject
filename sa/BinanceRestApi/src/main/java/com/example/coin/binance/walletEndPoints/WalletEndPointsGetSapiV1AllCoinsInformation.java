package com.example.coin.binance.walletEndPoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

public class WalletEndPointsGetSapiV1AllCoinsInformation extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetSapiV1AllCoinsInformation(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/sapi/v1/capital/config/getall";
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

		DataTable OUT_REST = OUT_DS.addTable("OUT_RSET");
		OUT_REST.addColumn("COIN");
		OUT_REST.addColumn("DEPOSIT_ALL_ENABLE");
		OUT_REST.addColumn("FREE");
		OUT_REST.addColumn("FREEZE");
		OUT_REST.addColumn("IPOABLE");
		OUT_REST.addColumn("IPOING");
		OUT_REST.addColumn("IS_LEGAL_MONEY");
		OUT_REST.addColumn("LOCKED");
		OUT_REST.addColumn("NAME");
		OUT_REST.addColumn("STORAGE");
		OUT_REST.addColumn("TRADING");
		OUT_REST.addColumn("WITHDRAW_ALL_ENABLE");
		OUT_REST.addColumn("WITHDRAWING");

		DataTable OUT_RSET_NETWORK_LIST = OUT_DS.addTable("OUT_RSET_NETWORK_LIST");
		OUT_RSET_NETWORK_LIST.addColumn("ADDRESS_REGEX");
		OUT_RSET_NETWORK_LIST.addColumn("COIN");
		OUT_RSET_NETWORK_LIST.addColumn("DEPOSIT_DESC");
		OUT_RSET_NETWORK_LIST.addColumn("DEPOSIT_ENABLE");
		OUT_RSET_NETWORK_LIST.addColumn("IS_DEFAULT");
		OUT_RSET_NETWORK_LIST.addColumn("MEMO_REGEX");
		OUT_RSET_NETWORK_LIST.addColumn("MIN_CONFIRM");
		OUT_RSET_NETWORK_LIST.addColumn("NAME");
		OUT_RSET_NETWORK_LIST.addColumn("NETWORK");
		OUT_RSET_NETWORK_LIST.addColumn("RESET_ADDRESS_STATUS");
		OUT_RSET_NETWORK_LIST.addColumn("SPECIAL_TIPS");
		OUT_RSET_NETWORK_LIST.addColumn("UN_LOCK_CONFIRM");
		OUT_RSET_NETWORK_LIST.addColumn("WITHDRAW_DESC");
		OUT_RSET_NETWORK_LIST.addColumn("WITHDRAW_ENABLE");
		OUT_RSET_NETWORK_LIST.addColumn("WITHDRAW_FEE");
		OUT_RSET_NETWORK_LIST.addColumn("WITHDRAW_INTEGER_MULTIPLE");
        OUT_RSET_NETWORK_LIST.addColumn("WITHDRAW_MAX");
		OUT_RSET_NETWORK_LIST.addColumn("WITHDRAW_MIN");
        OUT_RSET_NETWORK_LIST.addColumn("SAME_ADDRESS");

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
			// System.out.println(exceptionAsString);
			// 출처: https://blog.miyam.net/81 [낭만 프로그래머]

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
			// System.out.println(exceptionAsString);
			// 출처: https://blog.miyam.net/81 [낭만 프로그래머]

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
			DataRow dr = OUT_REST.addRow();
			dr.setString("COIN", c.get("coin").toString());
			dr.setString("DEPOSIT_ALL_ENABLE", c.get("depositAllEnable").toString());
			dr.setString("FREE", c.get("free").toString());
			dr.setString("FREEZE", c.get("freeze").toString());
			dr.setString("IPOABLE", c.get("ipoable").toString());
			dr.setString("IPOING", c.get("ipoing").toString());
			dr.setString("IS_LEGAL_MONEY", c.get("isLegalMoney").toString());
			dr.setString("LOCKED", c.get("locked").toString());
			dr.setString("NAME", c.get("name").toString());
			dr.setString("STORAGE", c.get("storage").toString());
			dr.setString("TRADING", c.get("trading").toString());
			dr.setString("WITHDRAW_ALL_ENABLE", c.get("withdrawAllEnable").toString());
			dr.setString("WITHDRAWING", c.get("withdrawing").toString());
			if (c.get("networkList") != null) {
				ArrayList<HashMap<String, Object>> al_sub = (ArrayList<HashMap<String, Object>>) c.get("networkList");
				for (int j = 0; j < al_sub.size(); j++) {
					DataRow DR_OUT_DATA_SUB_ROW = OUT_RSET_NETWORK_LIST.addRow();
					HashMap<String, Object> tmp_sub = al_sub.get(j);
					DR_OUT_DATA_SUB_ROW.setString("ADDRESS_REGEX", tmp_sub.get("addressRegex").toString());
					DR_OUT_DATA_SUB_ROW.setString("COIN", tmp_sub.get("coin").toString());
					if (tmp_sub.get("depositDesc") != null) {
						DR_OUT_DATA_SUB_ROW.setString("DEPOSIT_DESC", tmp_sub.get("depositDesc").toString());
					}

					DR_OUT_DATA_SUB_ROW.setString("DEPOSIT_ENABLE", tmp_sub.get("depositEnable").toString());
					DR_OUT_DATA_SUB_ROW.setString("IS_DEFAULT", tmp_sub.get("isDefault").toString());
					DR_OUT_DATA_SUB_ROW.setString("MEMO_REGEX", tmp_sub.get("memoRegex").toString());

					DR_OUT_DATA_SUB_ROW.setString("MIN_CONFIRM", tmp_sub.get("minConfirm").toString());
					DR_OUT_DATA_SUB_ROW.setString("NAME", tmp_sub.get("name").toString());
					DR_OUT_DATA_SUB_ROW.setString("NETWORK", tmp_sub.get("network").toString());
					DR_OUT_DATA_SUB_ROW.setString("RESET_ADDRESS_STATUS", tmp_sub.get("resetAddressStatus").toString());

					if (tmp_sub.get("specialTips") != null) {
						DR_OUT_DATA_SUB_ROW.setString("SPECIAL_TIPS", tmp_sub.get("specialTips").toString());
					}

					DR_OUT_DATA_SUB_ROW.setString("UN_LOCK_CONFIRM", tmp_sub.get("unLockConfirm").toString());

					if (tmp_sub.get("withdrawDesc") != null) {
						DR_OUT_DATA_SUB_ROW.setString("WITHDRAW_DESC", tmp_sub.get("withdrawDesc").toString());
					}

					DR_OUT_DATA_SUB_ROW.setString("WITHDRAW_ENABLE", tmp_sub.get("withdrawEnable").toString());
					DR_OUT_DATA_SUB_ROW.setString("WITHDRAW_FEE", tmp_sub.get("withdrawFee").toString());
					DR_OUT_DATA_SUB_ROW.setString("WITHDRAW_INTEGER_MULTIPLE",
							tmp_sub.get("withdrawIntegerMultiple").toString());
					DR_OUT_DATA_SUB_ROW.setString("WITHDRAW_MIN", tmp_sub.get("withdrawMin").toString());
                    DR_OUT_DATA_SUB_ROW.setString("WITHDRAW_MAX", tmp_sub.get("withdrawMax").toString());
                    DR_OUT_DATA_SUB_ROW.setString("SAME_ADDRESS", tmp_sub.get("sameAddress").toString());
				}
			}
		}
		return OUT_DS;
	}

}
