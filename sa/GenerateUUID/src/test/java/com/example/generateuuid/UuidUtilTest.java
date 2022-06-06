package com.example.generateuuid;

import org.junit.Test;
import dataset.DataSet;
import dataset.converter.DotNetXmlDataSetConverter;

public class UuidUtilTest {

    @Test
	public void testGetOrderedUUID() {
		UuidUtil  tmp = new UuidUtil();
		DataSet IN_DS = new DataSet();
        try {
            DataSet OUT_DS = tmp.getOrderedUUID(IN_DS, null, null);
            String  outString =DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
            System.out.println(outString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	}

}
