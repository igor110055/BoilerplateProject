package com.example.coin.binance.marketDataEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3TickerPriceChangeStatistics24;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class MarketDataEndPointsGetApiV3TickerPriceChangeStatistics24Test {

	@Test
	public void testMarketDataEndPointsGetApiV3TickerPriceChangeStatistics24() {
		MarketDataEndPointsGetApiV3TickerPriceChangeStatistics24 tmp = new MarketDataEndPointsGetApiV3TickerPriceChangeStatistics24();

		DataSet OUT_DS;
		try {
			DataSet IN_DS = new DataSet();
			DataTable IN_PSET = IN_DS.addTable("IN_PSET");
			IN_PSET.addColumn("SYMBOL");

			DataRow DR_IN_PSET = IN_PSET.addRow();
			DR_IN_PSET.setString("SYMBOL", "BNBBTC");

			OUT_DS = tmp.GetApiV3TickerPriceChangeStatistics24(IN_DS, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
