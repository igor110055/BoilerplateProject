package com.example.coin.binance.marketDataEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3CompressedAggreateTradeList;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class MarketDataEndPointsGetApiV3CompressedAggreateTradeListTest {

	@Test
	public void testMarketDataEndPointsGetApiV3CompressedAggreateTradesList() {
		MarketDataEndPointsGetApiV3CompressedAggreateTradeList tmp = new MarketDataEndPointsGetApiV3CompressedAggreateTradeList();

		DataSet OUT_DS;
		try {
			DataSet IN_DS = new DataSet();
			DataTable IN_PSET = IN_DS.addTable("IN_PSET");
			IN_PSET.addColumn("SYMBOL");
			IN_PSET.addColumn("LIMIT"); // Default 500; max 1000.
			IN_PSET.addColumn("FROM_ID");
			IN_PSET.addColumn("START_TIME");
			IN_PSET.addColumn("END_TIME");

			DataRow DR_IN_PSET = IN_PSET.addRow();
			DR_IN_PSET.setString("SYMBOL", "BNBBTC");
			DR_IN_PSET.setString("LIMIT", "100");
			DR_IN_PSET.setString("FROM_ID", "");

			OUT_DS = tmp.GetApiV3CompressedAggreateTradeList(IN_DS, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
