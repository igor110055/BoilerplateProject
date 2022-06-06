package com.example.telegram;
import org.junit.Test;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class TelegramBotSendToMeTest {

	@Test
	public void testExchangePostOrders() {
		TelegramBotSendToMe  tmp = new TelegramBotSendToMe();
		
		DataSet IN_DS = new DataSet();
    	DataTable IN_PSET = IN_DS.addTable("IN_PSET");
    	IN_PSET.addColumn("TEXT");
        IN_PSET.addColumn("CHAT_ID");
        IN_PSET.addColumn("TELEGRAM_API_ACCESS_KEY");

        String TELEGRAM_API_ACCESS_KEY = System.getenv("TELEGRAM_API_ACCESS_KEY");
        String CHAT_ID = System.getenv("CHAT_ID");
    	DataRow DR_IN_PSET = IN_PSET.addRow();
    	DR_IN_PSET.setString("TEXT", "테스트 가으자");
    	DR_IN_PSET.setString("CHAT_ID", CHAT_ID);
        DR_IN_PSET.setString("TELEGRAM_API_ACCESS_KEY", TELEGRAM_API_ACCESS_KEY);

		DataSet OUT_DS;
		try {
			OUT_DS = tmp.BotSendToMe( IN_DS, null, null);
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
