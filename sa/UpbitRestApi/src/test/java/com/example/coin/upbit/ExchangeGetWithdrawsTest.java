package com.example.coin.upbit;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class ExchangeGetWithdrawsTest {

	@Test
	public void testExchangeGetWithdrawsTest() {
		ExchangeGetWithdraws  tmp = new ExchangeGetWithdraws();
		
		DataSet IN_DS = new DataSet();
    	DataTable IN_PSET = IN_DS.addTable("IN_PSET");
    	IN_PSET.addColumn("CURRENCY");
    	IN_PSET.addColumn("STATE");
    	IN_PSET.addColumn("LIMIT");
    	IN_PSET.addColumn("PAGE");
    	IN_PSET.addColumn("ORDER_BY");

    	DataTable IN_TXID = IN_DS.addTable("IN_TXID");
    	IN_TXID.addColumn("TXID");

    	DataTable IN_UUID = IN_DS.addTable("IN_UUID");
    	IN_UUID.addColumn("UUID");
    	
    	DataRow DR_IN_PSET = IN_PSET.addRow();
    	DR_IN_PSET.setString("ORDER_BY", "desc");
    	//DR_IN_PSET.setString("TXID", "TXID");
    	//DR_IN_PSET.setString("CURRENCY", "CURRENCY");
    	
    	
    	/*상태 */
    	DataTable IN_KEY = IN_DS.addTable("IN_KEY");
    	IN_KEY.addColumn("UPBIT_OPEN_API_ACCESS_KEY");
    	IN_KEY.addColumn("UPBIT_OPEN_API_SECRET_KEY");
    	
    	String accessKey = System.getenv("UPBIT_OPEN_API_ACCESS_KEY");
        String secretKey = System.getenv("UPBIT_OPEN_API_SECRET_KEY");
        
        DataRow drKey = IN_KEY.addRow();
        drKey.setString("UPBIT_OPEN_API_ACCESS_KEY", accessKey);
        drKey.setString("UPBIT_OPEN_API_SECRET_KEY", secretKey);
		
		
		DataSet OUT_DS;
		try {
			OUT_DS = tmp.GetWithdraws( IN_DS, null, null);
			String inString =DotNetXmlDataSetConverter.convertFromDataSet(IN_DS);
			System.out.println(inString);
			String outString =DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
