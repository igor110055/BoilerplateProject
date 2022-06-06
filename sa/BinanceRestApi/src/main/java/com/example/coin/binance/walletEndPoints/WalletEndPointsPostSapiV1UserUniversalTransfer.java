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

public class WalletEndPointsPostSapiV1UserUniversalTransfer extends SAProxy {
	public DataSet PostSapiV1UserUniversalTransfer(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/sapi/v1/asset/transfer";
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

		OUT_RSET.addColumn("TRAN_ID", dataset.type.DataType.STRING, null, "tranId");

		DataTable dtKey = InDs.getTable("IN_KEY");
		DataTable dt = InDs.getTable("IN_PSET");

		String TYPE = null;
		String ASSET = null;
		String AMOUNT = null;
		String FROM_SYMBOL = null;
		String TO_SYMBOL = null;

		/*
		 * fromSymbol must be sent when type are ISOLATEDMARGIN_MARGIN and
		 * ISOLATEDMARGIN_ISOLATEDMARGIN toSymbol must be sent when type are
		 * MARGIN_ISOLATEDMARGIN and ISOLATEDMARGIN_ISOLATEDMARGIN
		 * 
		 * ENUM of transfer types:
		 * 
		 * MAIN_C2C Spot account transfer to C2C account MAIN_UMFUTURE Spot account
		 * transfer to USDⓈ-M Futures account MAIN_CMFUTURE Spot account transfer to
		 * COIN-M Futures account MAIN_MARGIN Spot account transfer to
		 * Margin（cross）account MAIN_MINING Spot account transfer to Mining account
		 * C2C_MAIN C2C account transfer to Spot account C2C_UMFUTURE C2C account
		 * transfer to USDⓈ-M Futures account C2C_MINING C2C account transfer to Mining
		 * account C2C_MARGIN C2C account transfer to Margin(cross) account
		 * UMFUTURE_MAIN USDⓈ-M Futures account transfer to Spot account UMFUTURE_C2C
		 * USDⓈ-M Futures account transfer to C2C account UMFUTURE_MARGIN USDⓈ-M Futures
		 * account transfer to Margin（cross）account CMFUTURE_MAIN COIN-M Futures account
		 * transfer to Spot account CMFUTURE_MARGIN COIN-M Futures account transfer to
		 * Margin(cross) account MARGIN_MAIN Margin（cross）account transfer to Spot
		 * account MARGIN_UMFUTURE Margin（cross）account transfer to USDⓈ-M Futures
		 * MARGIN_CMFUTURE Margin（cross）account transfer to COIN-M Futures MARGIN_MINING
		 * Margin（cross）account transfer to Mining account MARGIN_C2C
		 * Margin（cross）account transfer to C2C account MINING_MAIN Mining account
		 * transfer to Spot account MINING_UMFUTURE Mining account transfer to USDⓈ-M
		 * Futures account MINING_C2C Mining account transfer to C2C account
		 * MINING_MARGIN Mining account transfer to Margin(cross) account MAIN_PAY Spot
		 * account transfer to Pay account PAY_MAIN Pay account transfer to Spot account
		 * ISOLATEDMARGIN_MARGIN Isolated margin account transfer to Margin(cross)
		 * account MARGIN_ISOLATEDMARGIN Margin(cross) account transfer to Isolated
		 * margin account ISOLATEDMARGIN_ISOLATEDMARGIN Isolated margin account transfer
		 * to Isolated margin account
		 * 
		 */

		String RECV_WINDOW = null;
		String TIMESTAMP = null;

		String BINANCE_API_KEY = null;
		String BINANCE_API_SECRET = null;

		if (dt.getRowCount() > 0) {
			TYPE = dt.getRow(0).getStringNullToEmpty("TYPE");
			ASSET = dt.getRow(0).getStringNullToEmpty("ASSET");
			AMOUNT = dt.getRow(0).getStringNullToEmpty("AMOUNT");
			FROM_SYMBOL = dt.getRow(0).getStringNullToEmpty("FROM_SYMBOL");
			TO_SYMBOL = dt.getRow(0).getStringNullToEmpty("TO_SYMBOL");

			RECV_WINDOW = dt.getRow(0).getStringNullToEmpty("RECV_WINDOW");
			TIMESTAMP = dt.getRow(0).getStringNullToEmpty("TIMESTAMP");
		}

		if (dtKey.getRowCount() > 0) {
			BINANCE_API_KEY = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_KEY");
			BINANCE_API_SECRET = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_SECRET");
		}

		HashMap<String, String> params = new HashMap<>();
		params.put("type", TYPE);
		params.put("asset", ASSET);
		params.put("amount", AMOUNT);

		if (!PjtUtil.g().isEmpty(FROM_SYMBOL)) {
			params.put("fromSymbol", FROM_SYMBOL);
		}

		if (!PjtUtil.g().isEmpty(TO_SYMBOL)) {
			params.put("toSymbol", TO_SYMBOL);
		}

		if (!PjtUtil.g().isEmpty(TIMESTAMP)) {
			params.put("timestamp", TIMESTAMP);
		}

		DataRow drRst = OUT_RST.addRow();
		drRst.setString("URL", URL);

		if (PjtUtil.g().isEmpty(TYPE)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "TYPE 이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(ASSET)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "ASSET 이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(AMOUNT)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "AMOUNT 이 인풋으로 넘어오지 않았습니다.");
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
				DataRow dr = OUT_RSET.addRow();
				dr.setString("ID", c.get("id").toString());
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
