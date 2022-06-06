package com.example.coin.binance.walletEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Time;
import com.example.coin.binance.walletEndPoints.WalletEndPointsPostSapiV1Withdraw;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class WalletEndPointsPostSapiV1WithdrawTest {

	@Test
	public void testPostSapiV1Withdraw() {
		WalletEndPointsPostSapiV1Withdraw tmp = new WalletEndPointsPostSapiV1Withdraw();

		DataSet IN_DS = new DataSet();
		DataTable IN_PSET = IN_DS.addTable("IN_PSET");
		IN_PSET.addColumn("COIN");
		IN_PSET.addColumn("WITHDRAW_ORDER_ID");
		IN_PSET.addColumn("NETWORK");
		IN_PSET.addColumn("ADDRESS");
		IN_PSET.addColumn("ADDRESS_TAG");
		IN_PSET.addColumn("AMOUNT");
		IN_PSET.addColumn("TRANSACTION_FEE_FLAG");
		IN_PSET.addColumn("NAME");

		IN_PSET.addColumn("RECV_WINDOW");
		IN_PSET.addColumn("TIMESTAMP");

		MarketDataEndPointsGetApiV3Time st = new MarketDataEndPointsGetApiV3Time();
		DataSet DS_ST = null;
		String serverTime = "";
		try {
			DS_ST = st.GetApiV3Time(new DataSet(), null, null);
			DataTable dt = DS_ST.getTable("OUT_RSET");
			if (dt.getRowCount() > 0) {
				serverTime = dt.getRow(0).getString("SERVER_TIME");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DataRow DR_IN_PSET = IN_PSET.addRow();
		DR_IN_PSET.setString("COIN", "");
		// DR_IN_PSET.setString("WITHDRAW_ORDER_ID", "");
		// DR_IN_PSET.setString("NETWORK", "");
		// DR_IN_PSET.setString("ADDRESS", "");
		// DR_IN_PSET.setString("ADDRESS_TAG", "");
		DR_IN_PSET.setString("AMOUNT", "");
		// DR_IN_PSET.setString("TRANSACTION_FEE_FLAG", "");
		// DR_IN_PSET.setString("NAME", "");

		DR_IN_PSET.setString("RECV_WINDOW", "");
		DR_IN_PSET.setString("TIMESTAMP", serverTime);

		/* 상태 */
		DataTable IN_KEY = IN_DS.addTable("IN_KEY");
		IN_KEY.addColumn("BINANCE_API_KEY");
		IN_KEY.addColumn("BINANCE_API_SECRET");

		String accessKey = System.getenv("BINANCE-API-KEY");
		String secretKey = System.getenv("BINANCE-API-SECRET");

		DataRow drKey = IN_KEY.addRow();
		drKey.setString("BINANCE_API_KEY", accessKey);
		drKey.setString("BINANCE_API_SECRET", secretKey);

		DataSet OUT_DS;
		try {
			OUT_DS = tmp.PostSapiV1Withdraw(IN_DS, null, null);
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
