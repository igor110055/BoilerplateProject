package com.example.coin.binance.marketDataEndPoints;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;
import com.example.coin.binance.marketDataEndPoints.*;

public class MarketDataEndPointsGetApiV3AvgPriceTest {

	@Test
	public void testMarketDataEndPointsGetApiV3AvgPrice() {
		MarketDataEndPointsGetApiV3AvgPrice tmp = new MarketDataEndPointsGetApiV3AvgPrice();

		DataSet OUT_DS;
		try {
			DataSet IN_DS = new DataSet();
			DataTable IN_PSET = IN_DS.addTable("IN_PSET");
			IN_PSET.addColumn("SYMBOL");

			DataRow DR_IN_PSET = IN_PSET.addRow();
			DR_IN_PSET.setString("SYMBOL", "BNBBTC");

			OUT_DS = tmp.GetApiV3AvgPrice(IN_DS, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
