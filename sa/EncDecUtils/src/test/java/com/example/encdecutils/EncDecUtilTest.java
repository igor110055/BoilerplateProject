package com.example.encdecutils;

import org.junit.Test;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class EncDecUtilTest {
	@Test
	public void testExchangePostOrders() {
		EncDecUtil  tmp = new EncDecUtil();
		
		DataSet IN_DS = new DataSet();
    	DataTable IN_PSET = IN_DS.addTable("IN_PSET");
    	IN_PSET.addColumn("PWD");
        IN_PSET.addColumn("SALT");

        String salt="";
        try {
            DataSet OUT_DS2 = tmp.getSalt(IN_DS, null, null);
            salt=OUT_DS2.getTable("OUT_RSET").getRow(0).getString("SALT");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println(salt);

    	DataRow DR_IN_PSET = IN_PSET.addRow();
    	DR_IN_PSET.setString("PWD", salt);


		DataSet OUT_DS;
		try {
			OUT_DS = tmp.setEncrypt( IN_DS, null, null);
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
