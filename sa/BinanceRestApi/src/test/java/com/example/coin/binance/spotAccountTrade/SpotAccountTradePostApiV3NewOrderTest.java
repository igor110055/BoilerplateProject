package com.example.coin.binance.spotAccountTrade;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Time;
import com.example.coin.binance.walletEndPoints.WalletEndPointsPostSapiV1UserUniversalTransfer;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class SpotAccountTradePostApiV3NewOrderTest {

	@Test
	public void testPostApiV3NewOrder() {
		SpotAccountTradePostApiV3NewOrder tmp = new SpotAccountTradePostApiV3NewOrder();

		DataSet IN_DS = new DataSet();
		DataTable IN_PSET = IN_DS.addTable("IN_PSET");
		IN_PSET.addColumn("SYMBOL");
		IN_PSET.addColumn("SIDE");
		IN_PSET.addColumn("TYPE");
		IN_PSET.addColumn("TIME_IN_FORCE");
		IN_PSET.addColumn("QUANTITY");

		IN_PSET.addColumn("QUOTE_ORDER_QTY");
		IN_PSET.addColumn("PRICE");
		IN_PSET.addColumn("NEW_CLIENT_ORDER_ID");
		IN_PSET.addColumn("STOP_PRICE");
		IN_PSET.addColumn("ICEBERG_QTY");
		IN_PSET.addColumn("NEW_ORDER_RESP_TYPE");

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
		DR_IN_PSET.setString("SIDE", "LIMIT");
		DR_IN_PSET.setString("TYPE", "LIMIT");
		// DR_IN_PSET.setString("TIME_IN_FORCE", "");
		// DR_IN_PSET.setString("QUANTITY", "");
		// DR_IN_PSET.setString("QUOTE_ORDER_QTY", "");
		// DR_IN_PSET.setString("PRICE", "");
		// DR_IN_PSET.setString("NEW_CLIENT_ORDER_ID", "");
		// DR_IN_PSET.setString("STOP_PRICE", "");
		// DR_IN_PSET.setString("ICEBERG_QTY", "");
		// DR_IN_PSET.setString("NEW_ORDER_RESP_TYPE", "");

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
			OUT_DS = tmp.PostApiV3NewOrder(IN_DS, null, null);
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
