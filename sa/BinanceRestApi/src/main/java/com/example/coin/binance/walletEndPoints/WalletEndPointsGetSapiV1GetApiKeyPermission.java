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

public class WalletEndPointsGetSapiV1GetApiKeyPermission extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetSapiV1GetApiKeyPermission(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/sapi/v1/account/apiRestrictions";
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

		OUT_RSET.addColumn("IS_RESTRICT", dataset.type.DataType.STRING, null, "ipRestrict");
		OUT_RSET.addColumn("CREATE_TIME", dataset.type.DataType.STRING, null, "createTime");
		// This option allows you to withdraw via API. You must apply the IP Access
		// Restriction filter in order to enable withdrawals
		OUT_RSET.addColumn("ENABLE_WITHDRAWALS", dataset.type.DataType.STRING, null, "enableWithdrawals");
		// This option authorizes this key to transfer funds between your master account
		// and your sub account instantly
		OUT_RSET.addColumn("ENABLE_INTERNAL_TRANSFER", dataset.type.DataType.STRING, null, "enableInternalTransfer");
		// Authorizes this key to be used for a dedicated universal transfer API to
		// transfer multiple supported currencies. Each business's own transfer API
		// rights are not affected by this authorization
		OUT_RSET.addColumn("PERMITS_UNIVERSAL_TRANSFER", dataset.type.DataType.STRING, null,
				"permitsUniversalTransfer");
		// Authorizes this key to Vanilla options trading
		OUT_RSET.addColumn("ENABLE_VANILLA_OPTIONS", dataset.type.DataType.STRING, null, "enableVanillaOptions");
		OUT_RSET.addColumn("ENABLE_READING", dataset.type.DataType.STRING, null, "enableReading");
		// API Key created before your futures account opened does not support futures
		// API service
		OUT_RSET.addColumn("ENABLE_FUTURES", dataset.type.DataType.STRING, null, "enableFutures");
		// This option can be adjusted after the Cross Margin account transfer is
		// completed
		OUT_RSET.addColumn("ENABLE_MARGIN", dataset.type.DataType.STRING, null, "enableMargin");
		// Spot and margin trading
		OUT_RSET.addColumn("ENABLE_SPOT_AND_MARGIN_TRADING", dataset.type.DataType.STRING, null,
				"enableSpotAndMarginTrading");
		// Expiration time for spot and margin trading permission
		OUT_RSET.addColumn("TRADING_AUTHORITY_EXPIRATION_TIME", dataset.type.DataType.STRING, null,
				"tradingAuthorityExpirationTime");

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

		Map<String, Object> c = null;
		try {
			c = PjtUtil.g().JsonStringToObject(jsonOutString, Map.class);
			drRst.setString("STATUS", "S");
			if (c != null) {
				DataRow dr = OUT_RSET.addRow();
				dr.setString("IS_RESTRICT", c.get("ipRestrict").toString());
				dr.setString("CREATE_TIME", c.get("createTime").toString());
				dr.setString("ENABLE_WITHDRAWALS", c.get("enableWithdrawals").toString());
				dr.setString("ENABLE_INTERNAL_TRANSFER", c.get("enableInternalTransfer").toString());
				dr.setString("PERMITS_UNIVERSAL_TRANSFER", c.get("permitsUniversalTransfer").toString());
				dr.setString("ENABLE_VANILLA_OPTIONS", c.get("enableVanillaOptions").toString());
				dr.setString("ENABLE_READING", c.get("enableReading").toString());
				dr.setString("ENABLE_FUTURES", c.get("enableFutures").toString());
				dr.setString("ENABLE_MARGIN", c.get("enableMargin").toString());
				dr.setString("ENABLE_SPOT_AND_MARGIN_TRADING", c.get("enableSpotAndMarginTrading").toString());
				if (c.get("tradingAuthorityExpirationTime") != null) {
					dr.setString("TRADING_AUTHORITY_EXPIRATION_TIME",
							c.get("tradingAuthorityExpirationTime").toString());
				}
			}

			return OUT_DS;
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

	}

}
