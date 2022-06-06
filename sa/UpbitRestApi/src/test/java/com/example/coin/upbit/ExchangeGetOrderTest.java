package com.example.coin.upbit;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class ExchangeGetOrderTest {

	@Test
	public void testExchangeGetOrders() {
		ExchangeGetOrder  tmp = new ExchangeGetOrder();
		
		DataSet IN_DS = new DataSet();
    	DataTable IN_PSET = IN_DS.addTable("IN_PSET");
    	IN_PSET.addColumn("UUID");
    	IN_PSET.addColumn("IDENTIFIER");
    	


    	
    	DataRow DR_IN_PSET = IN_PSET.addRow();
    	DR_IN_PSET.setString("UUID", "96796bf1-e726-4d34-9680-cbd52e2ef3d1");
    	//DR_IN_PSET.setString("IDENTIFIER", "IDENTIFIER");
    	

    	DataTable IN_STATE = IN_DS.addTable("IN_STATE");
    	IN_STATE.addColumn("STATE");
     	DataRow DR_IN_STATE = IN_STATE.addRow();
    	DR_IN_STATE.setString("STATE", "cancel");
    	DR_IN_STATE.setString("STATE", "done");
    	
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
			OUT_DS = tmp.GetOrder( IN_DS, null, null);
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
