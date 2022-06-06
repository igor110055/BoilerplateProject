package com.example.coin.binance.walletEndPoints;

import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1SystemStatus;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class WalletEndPointsGetSapiV1SystemStatusTest {

	@Test
	public void testWalletEndPointsGetSapiV1SystemStatus() {
		WalletEndPointsGetSapiV1SystemStatus tmp = new WalletEndPointsGetSapiV1SystemStatus();

		DataSet IN_DS = new DataSet();

		DataSet OUT_DS;
		try {
			OUT_DS = tmp.GetSapiV1SystemStatus(IN_DS, null, null);
			String inString = DotNetXmlDataSetConverter.convertFromDataSet(IN_DS);
			System.out.println(inString);
			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
