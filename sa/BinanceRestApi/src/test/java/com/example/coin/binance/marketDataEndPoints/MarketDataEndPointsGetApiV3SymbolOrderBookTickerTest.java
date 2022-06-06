package com.example.coin.binance.marketDataEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3SymbolOrderBookTicker;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class MarketDataEndPointsGetApiV3SymbolOrderBookTickerTest {

	@Test
	public void testMarketDataEndPointsGetApiV3SymbolOrderBookTicker() {
		MarketDataEndPointsGetApiV3SymbolOrderBookTicker tmp = new MarketDataEndPointsGetApiV3SymbolOrderBookTicker();

		DataSet OUT_DS;
		try {
			DataSet IN_DS = new DataSet();
			DataTable IN_PSET = IN_DS.addTable("IN_PSET");
			IN_PSET.addColumn("SYMBOL");

			DataRow DR_IN_PSET = IN_PSET.addRow();
			// DR_IN_PSET.setString("SYMBOL", "BNBBTC");

			OUT_DS = tmp.GetApiV3SymbolOrderBookTicker(IN_DS, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
