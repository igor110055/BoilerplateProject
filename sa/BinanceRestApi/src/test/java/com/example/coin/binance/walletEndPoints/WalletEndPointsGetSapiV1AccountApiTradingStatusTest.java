package com.example.coin.binance.walletEndPoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Time;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1AccountApiTradingStatus;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class WalletEndPointsGetSapiV1AccountApiTradingStatusTest {

	@Test
	public void testGetSapiV1AccountApiTradingStatus() {
		WalletEndPointsGetSapiV1AccountApiTradingStatus tmp = new WalletEndPointsGetSapiV1AccountApiTradingStatus();

		DataSet IN_DS = new DataSet();
		DataTable IN_PSET = IN_DS.addTable("IN_PSET");
		IN_PSET.addColumn("RECV_WINDOW");
		IN_PSET.addColumn("TIMESTAMP");

		MarketDataEndPointsGetApiV3Time st = new MarketDataEndPointsGetApiV3Time();
		DataSet DS_ST = null;
		String serverTime = "";
		try {
			DS_ST = st.GetApiV3Time(new DataSet(), null, null);
			DataTable dt = DS_ST.getTable("OUT_RSET");
			if (dt.getRowCount() > 0) {
				serverTime = dt.getRow(0).getString("SERVER_TIME");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DataRow DR_IN_PSET = IN_PSET.addRow();
		DR_IN_PSET.setString("RECV_WINDOW", "");
		DR_IN_PSET.setString("TIMESTAMP", serverTime);

		/* 상태 */
		DataTable IN_KEY = IN_DS.addTable("IN_KEY");
		IN_KEY.addColumn("BINANCE_API_KEY");
		IN_KEY.addColumn("BINANCE_API_SECRET");

		String accessKey = System.getenv("BINANCE-API-KEY");
		String secretKey = System.getenv("BINANCE-API-SECRET");

		DataRow drKey = IN_KEY.addRow();
		drKey.setString("BINANCE_API_KEY", accessKey);
		drKey.setString("BINANCE_API_SECRET", secretKey);

		DataSet OUT_DS;
		try {
			OUT_DS = tmp.GetSapiV1AccountApiTradingStatus(IN_DS, null, null);
			String inString = DotNetXmlDataSetConverter.convertFromDataSet(IN_DS);
			System.out.println(inString);
			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testMakeHashMap() {
		System.out.println("testMakeHashMap");
		String jsonStr = MakeJson();

		System.out.println(jsonStr);

		HashMap<String, Object> c = null;
		try {
			c = PjtUtil.g().JsonStringToObject(jsonStr, HashMap.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c != null) {
			HashMap<String, Object> h = (HashMap<String, Object>) c.get("indicators");
			for (Map.Entry<String, Object> entry : h.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				System.out.println(key);
				System.out.println(value);
			}
		}
	}

	public String MakeJson() {
		HashMap<String, Object> f = new HashMap<String, Object>();
		HashMap<String, Object> c = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("i", "UFR");
		h.put("c", "20");
		h.put("v", "0.995");
		al.add(h);
		h = new HashMap<String, Object>();
		h.put("i", "IFER");
		h.put("c", "0.99");
		h.put("v", "0.99");
		al.add(h);
		h = new HashMap<String, Object>();
		h.put("i", "GCR");
		h.put("c", "0.99");
		h.put("v", "0.99");
		al.add(h);
		c.put("BTCUSDT", al);

		al = new ArrayList<HashMap<String, Object>>();
		h = new HashMap<String, Object>();
		h.put("i", "UFR");
		h.put("c", "20");
		h.put("v", "0.995");
		al.add(h);
		h = new HashMap<String, Object>();
		h.put("i", "IFER");
		h.put("c", "0.99");
		h.put("v", "0.99");
		al.add(h);
		h = new HashMap<String, Object>();
		h.put("i", "GCR");
		h.put("c", "0.99");
		h.put("v", "0.99");
		al.add(h);
		c.put("ETHUSDT", al);

		f.put("indicators", c);

		ObjectMapper mapper = new ObjectMapper();

		String jsonStr = null;
		try {
			jsonStr = mapper.writeValueAsString(f);
			System.out.println(jsonStr);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}

}
