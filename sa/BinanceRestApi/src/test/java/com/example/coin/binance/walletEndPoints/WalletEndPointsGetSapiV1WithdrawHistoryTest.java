package com.example.coin.binance.walletEndPoints;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Time;
import com.example.coin.binance.walletEndPoints.WalletEndPointsGetSapiV1WithdrawHistory;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class WalletEndPointsGetSapiV1WithdrawHistoryTest {

	@Test
	public void testPostSapiV1Withdraw() {
		WalletEndPointsGetSapiV1WithdrawHistory tmp = new WalletEndPointsGetSapiV1WithdrawHistory();

		DataSet IN_DS = new DataSet();
		DataTable IN_PSET = IN_DS.addTable("IN_PSET");
		IN_PSET.addColumn("COIN");
		IN_PSET.addColumn("WITHDRAW_ORDER_ID");
		IN_PSET.addColumn("STATUS"); // 0(0:Email Sent,1:Cancelled 2:Awaiting Approval 3:Rejected 4:Processing
										// 5:Failure 6:Completed)
		IN_PSET.addColumn("OFFSET"); // Default:0
		IN_PSET.addColumn("LIMIT"); // Default:1000, Max:1000

		IN_PSET.addColumn("START_TIME"); // Default: 90 days from current timestamp
		IN_PSET.addColumn("END_TIME"); // Default: present timestamp

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
		// DR_IN_PSET.setString("COIN", "");
		// DR_IN_PSET.setString("STATUS", "");
		// DR_IN_PSET.setString("START_TIME", "");
		// DR_IN_PSET.setString("END_TIME", "");
		// DR_IN_PSET.setString("OFFSET", "");
		// DR_IN_PSET.setString("LIMIT", "");

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
			OUT_DS = tmp.GetSapiV1WithdrawHistory(IN_DS, null, null);
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
