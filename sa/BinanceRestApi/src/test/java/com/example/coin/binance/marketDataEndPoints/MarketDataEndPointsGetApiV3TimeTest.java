package com.example.coin.binance.marketDataEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Time;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class MarketDataEndPointsGetApiV3TimeTest {

	@Test
	public void testGetApiV3Time() {
		MarketDataEndPointsGetApiV3Time tmp = new MarketDataEndPointsGetApiV3Time();

		DataSet OUT_DS;
		try {
			OUT_DS = tmp.GetApiV3Time(null, null, null);

			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
