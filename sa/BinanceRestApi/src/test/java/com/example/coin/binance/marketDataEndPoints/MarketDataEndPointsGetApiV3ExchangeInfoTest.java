package com.example.coin.binance.marketDataEndPoints;

import org.junit.Test;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class MarketDataEndPointsGetApiV3ExchangeInfoTest {

	@Test
	public void testGetApiV3Ping() {
		MarketDataEndPointsGetApiV3ExchangeInfo tmp = new MarketDataEndPointsGetApiV3ExchangeInfo();

		DataSet OUT_DS;
		try {
			DataSet IN_DS = new DataSet();
			DataTable IN_PSET = IN_DS.addTable("IN_PSET");
			IN_PSET.addColumn("SYMBOL");

			DataRow DR_IN_PSET = IN_PSET.addRow();
			DR_IN_PSET.setString("SYMBOL", "BTGUSDT");

			OUT_DS = tmp.GetApiV3ExchangeInfo(IN_DS, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			//System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
