package com.example.coin.upbit;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class ExchangePostOrdersTest {

	@Test
	public void testExchangePostOrders() {
		ExchangePostOrders  tmp = new ExchangePostOrders();
		
		DataSet IN_DS = new DataSet();
    	DataTable IN_PSET = IN_DS.addTable("IN_PSET");
    	IN_PSET.addColumn("MARKET");
    	IN_PSET.addColumn("VOLUME");
    	IN_PSET.addColumn("ORD_TYPE");
    	IN_PSET.addColumn("SIDE");
        IN_PSET.addColumn("PRICE");
        IN_PSET.addColumn("IDENTIFIER");
        

    	
    	DataRow DR_IN_STATE = IN_PSET.addRow();
    	DR_IN_STATE.setString("MARKET", "KRW-ADA");
    	DR_IN_STATE.setString("VOLUME", "3.21981424");
        DR_IN_STATE.setString("ORD_TYPE", "market");
        DR_IN_STATE.setString("SIDE", "ask");
        DR_IN_STATE.setString("IDENTIFIER", "UPS202101050015");
        
        //수수료가 0.05%이다.
        //DR_IN_STATE.setString("PRICE", "1");
        //DR_IN_STATE.setString("PRICE", "316");
/*bid	KRW-ARDR		905652	price*/
    	
    	
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
			OUT_DS = tmp.PostOrders( IN_DS, null, null);
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
