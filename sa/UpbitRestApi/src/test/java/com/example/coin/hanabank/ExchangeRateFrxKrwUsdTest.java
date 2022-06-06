package com.example.coin.hanabank;

import org.junit.Test;

import dataset.DataSet;
import dataset.converter.DotNetXmlDataSetConverter;

public class ExchangeRateFrxKrwUsdTest {

	@Test
	public void testExchangeRateFrxKrwUsdTest() {
		ExchangeRateFrxKrwUsd  tmp = new ExchangeRateFrxKrwUsd();
		
		DataSet IN_DS = new DataSet();
		DataSet OUT_DS;
		try {
			OUT_DS = tmp.GetFrxKrwUsd( IN_DS, null, null);
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
