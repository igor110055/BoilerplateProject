package com.example.coin.binance.marketDataEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Ping;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class MarketDataEndPointsGetApiV3PingTest {

	@Test
	public void testGetApiV3Ping() {
		MarketDataEndPointsGetApiV3Ping tmp = new MarketDataEndPointsGetApiV3Ping();

		DataSet OUT_DS;
		try {
			OUT_DS = tmp.GetApiV3Ping(null, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
