package com.example.coin.upbit;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class QuotationGetCandlesDaysTest {

	@Test
	public void testQuotationGetCandlesDays() {
		QuotationGetCandlesDays  tmp = new QuotationGetCandlesDays();
		
		DataSet IN_DS = new DataSet();
    	DataTable IN_PSET = IN_DS.addTable("IN_PSET");
    	IN_PSET.addColumn("MARKET");
    	IN_PSET.addColumn("TO");
    	IN_PSET.addColumn("COUNT");
    	IN_PSET.addColumn("CONVERTING_PRICE_UNIT");
    	
    	DataRow DR_IN_PSET = IN_PSET.addRow();
    	DR_IN_PSET.setString("MARKET", "KRW-BTC");
    	DR_IN_PSET.setString("TO", "");  //마지막 캔들 시각 (exclusive). 포맷 : yyyy-MM-dd'T'HH:mm:ss'Z' or yyyy-MM-dd HH:mm:ss. 비워서 요청시 가장 최근 캔들
    	DR_IN_PSET.setString("COUNT", "100");  //캔들 개수
    	DR_IN_PSET.setString("CONVERTING_PRICE_UNIT", "USDT");
    	
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
			OUT_DS = tmp.GetCandlesDays( IN_DS, null, null);
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
