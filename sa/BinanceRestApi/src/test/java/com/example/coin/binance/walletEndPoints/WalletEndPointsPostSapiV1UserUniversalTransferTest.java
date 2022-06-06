package com.example.coin.binance.walletEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Time;

import org.junit.Test;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class WalletEndPointsPostSapiV1UserUniversalTransferTest {

	@Test
	public void testPostSapiV1UserUniversalTransfer() {
		WalletEndPointsPostSapiV1UserUniversalTransfer tmp = new WalletEndPointsPostSapiV1UserUniversalTransfer();

		DataSet IN_DS = new DataSet();
		DataTable IN_PSET = IN_DS.addTable("IN_PSET");
		IN_PSET.addColumn("TYPE");
		IN_PSET.addColumn("ASSET");
		IN_PSET.addColumn("AMOUNT");
		IN_PSET.addColumn("FROM_SYMBOL");
		IN_PSET.addColumn("TO_SYMBOL");

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
		DR_IN_PSET.setString("TYPE", "");
		DR_IN_PSET.setString("RECV_WINDOW", "");
		DR_IN_PSET.setString("RECV_WINDOW", "");
		DR_IN_PSET.setString("RECV_WINDOW", "");

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
			OUT_DS = tmp.PostSapiV1UserUniversalTransfer(IN_DS, null, null);
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
