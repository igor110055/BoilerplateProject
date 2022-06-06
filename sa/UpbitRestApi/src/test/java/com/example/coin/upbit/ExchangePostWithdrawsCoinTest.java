package com.example.coin.upbit;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class ExchangePostWithdrawsCoinTest {

	@Test
	public void testExchangePostWithdrawsCoinTest() {
		ExchangePostWithdrawsCoin  tmp = new ExchangePostWithdrawsCoin();
		
		DataSet IN_DS = new DataSet();
    	DataTable IN_PSET = IN_DS.addTable("IN_PSET");
    	IN_PSET.addColumn("CURRENCY");
    	IN_PSET.addColumn("AMOUNT");
    	IN_PSET.addColumn("ADDRESS");
    	IN_PSET.addColumn("SECONDARY_ADDRESS");
    	IN_PSET.addColumn("TRANSACTION_TYPE");
    	
    	DataRow DR_IN_PSET = IN_PSET.addRow();
    	DR_IN_PSET.setString("CURRENCY", "BTG");
    	DR_IN_PSET.setString("AMOUNT", "20.949486");
    	DR_IN_PSET.setString("ADDRESS", "GcESd6XfyNgJXwdGnMkiZyozFiA5TrZExZ");
    	DR_IN_PSET.setString("SECONDARY_ADDRESS", "");
    	DR_IN_PSET.setString("TRANSACTION_TYPE", "default");
        /*amount=20.949486
        &address=GcESd6XfyNgJXwdGnMkiZyozFiA5TrZExZ
        &secondary_address=
        &currency=BTG
        &transaction_type=default */
    	
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
			OUT_DS = tmp.PostWithdrawsCoin( IN_DS, null, null);
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
