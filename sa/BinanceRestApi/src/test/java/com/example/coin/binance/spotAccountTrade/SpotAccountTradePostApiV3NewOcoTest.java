package com.example.coin.binance.spotAccountTrade;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Time;

import org.junit.Test;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class SpotAccountTradePostApiV3NewOcoTest {

	@Test
	public void testPostApiV3NewOrder() {
		SpotAccountTradePostApiV3NewOco tmp = new SpotAccountTradePostApiV3NewOco();

		DataSet IN_DS = new DataSet();
		DataTable IN_PSET = IN_DS.addTable("IN_PSET");
		IN_PSET.addColumn("SYMBOL");
		IN_PSET.addColumn("LIST_CLIENT_ORDER_ID"); // A unique Id for the entire orderList
		IN_PSET.addColumn("SIDE");
		IN_PSET.addColumn("QUANTITY");

		IN_PSET.addColumn("LIMIT_CLIENT_ORDER_ID"); // A unique Id for the limit order
		IN_PSET.addColumn("PRICE");
		IN_PSET.addColumn("LIMIT_ICEBERG_QTY");
		IN_PSET.addColumn("STOP_CLIENT_ORDER_ID"); // A unique Id for the stop loss/stop loss limit leg
		IN_PSET.addColumn("STOP_PRICE");
		IN_PSET.addColumn("STOP_LIMIT_PRICE"); // If provided, stopLimitTimeInForce is required.
		IN_PSET.addColumn("STOP_ICEBERG_QTY");

		IN_PSET.addColumn("STOP_LIMIT_TIME_IN_FORCE");// Valid values are GTC/FOK/IOC
		IN_PSET.addColumn("NEW_ORDER_RESP_TYPE"); // Set the response JSON.

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
		DR_IN_PSET.setString("SYMBOL", "BTCUSDT");
		// DR_IN_PSET.setString("LIST_CLIENT_ORDER_ID", "LIST_CLIENT_ORDER_ID");
		DR_IN_PSET.setString("SIDE", "SIDE");
		DR_IN_PSET.setString("QUANTITY", "QUANTITY");
		// DR_IN_PSET.setString("LIMIT_CLIENT_ORDER_ID", "LIMIT_CLIENT_ORDER_ID");
		DR_IN_PSET.setString("PRICE", "PRICE");
		// DR_IN_PSET.setString("LIMIT_ICEBERG_QTY", "LIMIT_ICEBERG_QTY");
		// DR_IN_PSET.setString("STOP_CLIENT_ORDER_ID", "STOP_CLIENT_ORDER_ID");
		DR_IN_PSET.setString("STOP_PRICE", "STOP_PRICE");
		// DR_IN_PSET.setString("STOP_LIMIT_PRICE", "STOP_LIMIT_PRICE");
		// DR_IN_PSET.setString("STOP_ICEBERG_QTY", "STOP_ICEBERG_QTY");
		// DR_IN_PSET.setString("STOP_LIMIT_TIME_IN_FORCE", "STOP_LIMIT_TIME_IN_FORCE");
		// DR_IN_PSET.setString("NEW_ORDER_RESP_TYPE", "NEW_ORDER_RESP_TYPE");

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
			OUT_DS = tmp.PostApiV3NewOco(IN_DS, null, null);
			String inString = DotNetXmlDataSetConverter.convertFromDataSet(IN_DS);
			System.out.println(inString);
			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
