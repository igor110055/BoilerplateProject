package com.lgcns.bizactor.servlet;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BigDecmailConvertTest {

	@Test
	public void testBigDecmailConvertTest() {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
		//.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		HashMap tmp=  new HashMap<String,Object>();
		//tmp.put("aa", new BigDecimal("0.0000000005"));
		tmp.put("aa", new BigDecimal("5E-8"));
		try {
			System.out.println(mapper.writeValueAsString(tmp));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
