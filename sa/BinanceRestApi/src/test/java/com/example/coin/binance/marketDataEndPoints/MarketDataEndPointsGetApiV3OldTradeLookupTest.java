package com.example.coin.binance.marketDataEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3OldTradeLookup;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class MarketDataEndPointsGetApiV3OldTradeLookupTest {

	@Test
	public void testMarketDataEndPointsGetApiV3OldTradesLookup() {
		MarketDataEndPointsGetApiV3OldTradeLookup tmp = new MarketDataEndPointsGetApiV3OldTradeLookup();

		DataSet OUT_DS;
		try {
			DataSet IN_DS = new DataSet();
			DataTable IN_PSET = IN_DS.addTable("IN_PSET");
			IN_PSET.addColumn("SYMBOL");
			IN_PSET.addColumn("LIMIT"); // Default 500; max 1000.
			IN_PSET.addColumn("FROM_ID");
			/*
			 * Trade id to fetch from. Default gets most recent trades.
			 */

			DataRow DR_IN_PSET = IN_PSET.addRow();
			DR_IN_PSET.setString("SYMBOL", "BNBBTC");
			// DR_IN_PSET.setString("LIMIT", "");
			// DR_IN_PSET.setString("FROM_ID", "");

			/* 상태 */
			DataTable IN_KEY = IN_DS.addTable("IN_KEY");
			IN_KEY.addColumn("BINANCE_API_KEY");
			IN_KEY.addColumn("BINANCE_API_SECRET");

			String accessKey = System.getenv("BINANCE-API-KEY");
			String secretKey = System.getenv("BINANCE-API-SECRET");

			DataRow drKey = IN_KEY.addRow();
			drKey.setString("BINANCE_API_KEY", accessKey);
			drKey.setString("BINANCE_API_SECRET", secretKey);

			OUT_DS = tmp.GetApiV3OldTradeLookup(IN_DS, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
