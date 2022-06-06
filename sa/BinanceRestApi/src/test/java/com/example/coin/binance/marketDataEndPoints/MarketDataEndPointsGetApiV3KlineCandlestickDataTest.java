package com.example.coin.binance.marketDataEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3KlineCandlestickData;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class MarketDataEndPointsGetApiV3KlineCandlestickDataTest {

	@Test
	public void testMarketDataEndPointsGetApiV3KlineCandlestickData() {
		MarketDataEndPointsGetApiV3KlineCandlestickData tmp = new MarketDataEndPointsGetApiV3KlineCandlestickData();

		DataSet OUT_DS;
		try {
			DataSet IN_DS = new DataSet();
			DataTable IN_PSET = IN_DS.addTable("IN_PSET");
			IN_PSET.addColumn("SYMBOL");
			IN_PSET.addColumn("INTERVAL"); // If startTime and endTime are not sent, the most recent klines are
											// returned.
			IN_PSET.addColumn("START_TIME");
			IN_PSET.addColumn("END_TIME");
			IN_PSET.addColumn("LIMIT"); // Default 500; max 1000.

			DataRow DR_IN_PSET = IN_PSET.addRow();
			DR_IN_PSET.setString("SYMBOL", "BNBBTC");
			DR_IN_PSET.setString("LIMIT", "100");
			DR_IN_PSET.setString("INTERVAL", "1m");

			/*
			 * m -> minutes; h -> hours; d -> days; w -> weeks; M -> months
			 * 
			 * 1m 3m 5m 15m 30m 1h 2h 4h 6h 8h 12h 1d 3d 1w 1M
			 */

			OUT_DS = tmp.GetApiV3KlineCandlestickData(IN_DS, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
