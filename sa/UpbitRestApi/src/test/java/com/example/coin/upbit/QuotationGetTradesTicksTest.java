package com.example.coin.upbit;

import org.junit.Test;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class QuotationGetTradesTicksTest {

	@Test
	public void testQuotationGetTradesTicks() {
		QuotationGetTradesTicks  tmp = new QuotationGetTradesTicks();
		
		DataSet IN_DS = new DataSet();
    	DataTable IN_PSET = IN_DS.addTable("IN_PSET");
    	IN_PSET.addColumn("MARKET");  //마켓 코드 (ex. KRW-BTC)
    	IN_PSET.addColumn("TO"); //마지막 체결 시각. 형식 : [HHmmss 또는 HH:mm:ss]. 비워서 요청시 가장 최근 데이터
    	IN_PSET.addColumn("COUNT");  //체결 개수
    	IN_PSET.addColumn("CURSOR");  //페이지네이션 커서 (sequentialId)
    	IN_PSET.addColumn("DAYS_AGO");
    	/*
    	daysAgo
    	int32
    	최근 체결 날짜 기준 7일 이내의 이전 데이터 조회 가능. 비워서 요청 시 가장 최근 체결 날짜 반환. (범위: 1 ~ 7))
    	*/
    	
    	DataRow DR_IN_PSET = IN_PSET.addRow();
    	DR_IN_PSET.setString("MARKET", "KRW-BTC");
    	DR_IN_PSET.setString("TO", "");  //마지막 캔들 시각 (exclusive). 포맷 : yyyy-MM-dd'T'HH:mm:ss'Z' or yyyy-MM-dd HH:mm:ss. 비워서 요청시 가장 최근 캔들
    	DR_IN_PSET.setString("COUNT", "100");  //캔들 개수
    	
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
			OUT_DS = tmp.GetTradesTicks( IN_DS, null, null);
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
