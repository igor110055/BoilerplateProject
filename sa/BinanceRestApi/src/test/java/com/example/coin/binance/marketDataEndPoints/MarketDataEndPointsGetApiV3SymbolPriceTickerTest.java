package com.example.coin.binance.marketDataEndPoints;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;
import com.example.coin.binance.marketDataEndPoints.*;

public class MarketDataEndPointsGetApiV3SymbolPriceTickerTest {

	@Test
	public void testMarketDataEndPointsGetApiV3SymbolPriceTicker() {
		MarketDataEndPointsGetApiV3SymbolPriceTicker tmp = new MarketDataEndPointsGetApiV3SymbolPriceTicker();

		DataSet OUT_DS;
		try {
			DataSet IN_DS = new DataSet();
			DataTable IN_PSET = IN_DS.addTable("IN_PSET");
			IN_PSET.addColumn("SYMBOL");

			DataRow DR_IN_PSET = IN_PSET.addRow();
			// DR_IN_PSET.setString("SYMBOL", "BNBBTC");

			OUT_DS = tmp.GetApiV3SymbolPriceTicker(IN_DS, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
