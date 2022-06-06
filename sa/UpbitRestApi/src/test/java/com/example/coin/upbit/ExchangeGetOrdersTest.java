package com.example.coin.upbit;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class ExchangeGetOrdersTest {

	@Test
	public void testExchangeGetOrders() {
		ExchangeGetOrders  tmp = new ExchangeGetOrders();
		
		DataSet IN_DS = new DataSet();
    	DataTable IN_PSET = IN_DS.addTable("IN_PSET");
    	IN_PSET.addColumn("MARKET");
    	IN_PSET.addColumn("STATE");
    	IN_PSET.addColumn("PAGE");
    	IN_PSET.addColumn("LIMIT");
    	IN_PSET.addColumn("ORDER_BY");

    	DataTable IN_STATE = IN_DS.addTable("IN_STATE");
    	IN_STATE.addColumn("STATE");

    	DataTable IN_UUID = IN_DS.addTable("IN_UUID");
    	IN_UUID.addColumn("UUID");
    	
    	DataTable IN_IDENTIFIER = IN_DS.addTable("IN_IDENTIFIER");
    	IN_IDENTIFIER.addColumn("IDENTIFIER");
    	
    	//DataRow DR_IN_PSET = IN_PSET.addRow();
    	//DR_IN_PSET.setString("STATE", "done");
    	//DR_IN_PSET.setString("TXID", "TXID");
    	//DR_IN_PSET.setString("CURRENCY", "CURRENCY");
    	
    	DataRow DR_IN_STATE = IN_STATE.addRow();
    	DR_IN_STATE.setString("STATE", "done");
    	//DR_IN_STATE.setString("STATE", "cancel");
    	
    	
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
			OUT_DS = tmp.GetOrders( IN_DS, null, null);
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
