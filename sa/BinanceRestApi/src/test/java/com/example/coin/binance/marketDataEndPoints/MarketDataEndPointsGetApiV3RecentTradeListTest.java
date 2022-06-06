package com.example.coin.binance.marketDataEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3RecentTradeList;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class MarketDataEndPointsGetApiV3RecentTradeListTest {

	@Test
	public void testGetApiV3RecentTradeList() {
		MarketDataEndPointsGetApiV3RecentTradeList tmp = new MarketDataEndPointsGetApiV3RecentTradeList();

		DataSet OUT_DS;
		try {
			DataSet IN_DS = new DataSet();
			DataTable IN_PSET = IN_DS.addTable("IN_PSET");
			IN_PSET.addColumn("SYMBOL");
			IN_PSET.addColumn("LIMIT");

			DataRow DR_IN_PSET = IN_PSET.addRow();
			DR_IN_PSET.setString("SYMBOL", "BNBBTC");
			DR_IN_PSET.setString("LIMIT", "100");

			OUT_DS = tmp.GetApiV3RecentTradeList(IN_DS, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
