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

public class WalletEndPointsGetSapiV1DustLog extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetSapiV1DustLog(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/sapi/v1/asset/dribblet";
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
		OUT_RSET.addColumn("TOTAL", dataset.type.DataType.STRING, null, "total");

		DataTable OUT_RSET_USER_ASSET_DRIBBLETS = OUT_DS.addTable("OUT_RSET_USER_ASSET_DRIBBLETS");
		OUT_RSET_USER_ASSET_DRIBBLETS.addColumn("SEQ", dataset.type.DataType.STRING, null, "SEQ");
		OUT_RSET_USER_ASSET_DRIBBLETS.addColumn("OPERATE_TIME", dataset.type.DataType.STRING, null, "operateTime");
		OUT_RSET_USER_ASSET_DRIBBLETS.addColumn("TOTAL_TRANSFERED_AMOUNT", dataset.type.DataType.STRING, null,
				"totalTransferedAmount");
		OUT_RSET_USER_ASSET_DRIBBLETS.addColumn("TOTAL_SERVICE_CHARGE_AMOUNT", dataset.type.DataType.STRING, null,
				"totalServiceChargeAmount");
		OUT_RSET_USER_ASSET_DRIBBLETS.addColumn("TRANS_ID", dataset.type.DataType.STRING, null, "transId");

		DataTable OUT_RSET_USER_ASSET_DRIBBLET_DETAILS = OUT_DS.addTable("OUT_RSET_USER_ASSET_DRIBBLET_DETAILS");
		OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.addColumn("SEQ", dataset.type.DataType.STRING, null, "SEQ");
		OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.addColumn("TRANS_ID", dataset.type.DataType.STRING, null, "transId");
		OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.addColumn("SERVICE_CHARGE_AMOUNT", dataset.type.DataType.STRING, null,
				"serviceChargeAmount");
		OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.addColumn("AMOUNT", dataset.type.DataType.STRING, null, "amount");
		OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.addColumn("OPERATE_TIME", dataset.type.DataType.STRING, null,
				"operateTime");
		OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.addColumn("TRANSFERED_AMOUNT", dataset.type.DataType.STRING, null,
				"transferedAmount");
		OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.addColumn("FROM_ASSET", dataset.type.DataType.STRING, null, "fromAsset");

		DataTable dtKey = InDs.getTable("IN_KEY");
		DataTable dt = InDs.getTable("IN_PSET");

		String START_TIME = null;
		String END_TIME = null;
		String RECV_WINDOW = null;
		String TIMESTAMP = null;

		String BINANCE_API_KEY = null;
		String BINANCE_API_SECRET = null;

		if (dt.getRowCount() > 0) {
			START_TIME = dt.getRow(0).getStringNullToEmpty("START_TIME");
			END_TIME = dt.getRow(0).getStringNullToEmpty("END_TIME");
			RECV_WINDOW = dt.getRow(0).getStringNullToEmpty("RECV_WINDOW");
			TIMESTAMP = dt.getRow(0).getStringNullToEmpty("TIMESTAMP");
		}

		if (dtKey.getRowCount() > 0) {
			BINANCE_API_KEY = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_KEY");
			BINANCE_API_SECRET = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_SECRET");
		}
		HashMap<String, String> params = new HashMap<>();
		if (!PjtUtil.g().isEmpty(START_TIME)) {
			params.put("startTime", START_TIME);
		}

		if (!PjtUtil.g().isEmpty(END_TIME)) {
			params.put("endTime", END_TIME);
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
			if (c.get("total") != null) {
				dr.setString("TOTAL", c.get("total").toString());
			} else {
				dr.setString("TOTAL", "0");
			}

			if (c.get("userAssetDribblets") != null) {
				ArrayList<HashMap<String, Object>> al = (ArrayList<HashMap<String, Object>>) c
						.get("userAssetDribblets");
				for (int i = 0; i < al.size(); i++) {
					HashMap<String, Object> d = al.get(i);
					DataRow DR_OUT_RSET_USER_ASSET_DRIBBLETS = OUT_RSET_USER_ASSET_DRIBBLETS.addRow();
					DR_OUT_RSET_USER_ASSET_DRIBBLETS.setString("OPERATE_TIME", d.get("operateTime").toString());
					DR_OUT_RSET_USER_ASSET_DRIBBLETS.setString("TOTAL_TRANSFERED_AMOUNT",
							d.get("totalTransferedAmount").toString());
					DR_OUT_RSET_USER_ASSET_DRIBBLETS.setString("TOTAL_SERVICE_CHARGE_AMOUNT",
							d.get("totalServiceChargeAmount").toString());
					DR_OUT_RSET_USER_ASSET_DRIBBLETS.setString("TRANS_ID", d.get("transId").toString());
					DR_OUT_RSET_USER_ASSET_DRIBBLETS.setString("SEQ", i + "");

					if (d.get("userAssetDribbletDetails") != null) {
						ArrayList<HashMap<String, Object>> al2 = (ArrayList<HashMap<String, Object>>) c
								.get("userAssetDribbletDetails");
						for (int j = 0; j < al2.size(); j++) {
							HashMap<String, Object> e = al2.get(j);
							DataRow DR_OUT_RSET_USER_ASSET_DRIBBLET_DETAILS = OUT_RSET_USER_ASSET_DRIBBLET_DETAILS
									.addRow();
							DR_OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.setString("SEQ", i + "");
							DR_OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.setString("TRANS_ID", e.get("transId").toString());
							DR_OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.setString("SERVICE_CHARGE_AMOUNT",
									e.get("serviceChargeAmount").toString());
							DR_OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.setString("AMOUNT", e.get("amount").toString());
							DR_OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.setString("OPERATE_TIME",
									e.get("operateTime").toString());
							DR_OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.setString("TRANSFERED_AMOUNT",
									e.get("transferedAmount").toString());
							DR_OUT_RSET_USER_ASSET_DRIBBLET_DETAILS.setString("FROM_ASSET",
									e.get("fromAsset").toString());
						}
					}
				}
			}
		}

		return OUT_DS;
	}

}
