package com.example.framework.utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
public class PjtUtil {
	PjtUtil() {
	
	}
	public static PjtUtil g() {
	    return LazyHolder.INSTANCE;
	}
	  
	private static class LazyHolder {
	    private static final PjtUtil INSTANCE = new PjtUtil();  
	}
	
	private String  Serverfilepath="";
	private int  Delaysleep=0; 
	

	DateFormat dateformat1 = new SimpleDateFormat("yyyyMMddHHmmss");
	public String getYyyyMMddHHMMSS(java.util.Date inDate) {
		
		return dateformat1.format(inDate);
	}
	DateFormat dateformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public  String getYyyy_MM_dd_HHMMSS(java.util.Date inDate) {
		if(inDate==null){
			return null;
		}

		return dateformat2.format(inDate);
	}

	public  <T> T JsonStringToObject(String JsonInString, Class<T> valueType)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper omOut = new ObjectMapper();
		//느리니까 정리도 하지 말자 
		//omOut.enable(SerializationFeature.INDENT_OUTPUT);
//		System.out.println(JsonInString);
//		System.out.println(valueType);
		//https://answer-id.com/ko/52045806
		omOut.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);///변환활 클래스없는 필드는 json  필드 무시
		
		return omOut.readValue(JsonInString, valueType);
	}

	public  String ObjectToJsonString(Object value) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		///느리니까 정리도 하지 말자 
		///om.enable(SerializationFeature.INDENT_OUTPUT);
		return om.writeValueAsString(value);
	}

	public  boolean isEmpty(String tmp) {
		if (tmp == null) {
			return true;
		}
		if (tmp.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public  String str(Object tmp) {
		if (tmp == null) {
			return "";
		}
		String tmp2 = tmp.toString();
		return tmp2;
	}
	public  String strTrim(Object tmp) {
		if (tmp == null) {
			return "";
		}
		String tmp2 = tmp.toString().trim();
		return tmp2;
	}

	public  String nvl(String first, String second) {
		if (isEmpty(first)) {
			return second;
		}

		return first;
	}
	/* https://stackoverflow.com/questions/22271099/convert-exception-to-json */

	public  String convertExceptionToJSON(Throwable e) {
		JSONObject responseBody = new JSONObject();
		JSONObject errorTag = new JSONObject();
		try {
			responseBody.put("error", errorTag);
			JSONArray detailList = new JSONArray();
			errorTag.put("details", detailList);

			Throwable nextRunner = e;
			while (nextRunner != null) {
				Throwable runner = nextRunner;
				nextRunner = runner.getCause();

				JSONObject detailObj = new JSONObject();
				detailObj.put("code", runner.getClass().getName());
				String msg = runner.toString();
				detailObj.put("message", msg);

				detailList.put(detailObj);
			}

			JSONArray stackList = new JSONArray();
			for (StackTraceElement ste : e.getStackTrace()) {
				stackList.put(ste.getFileName() + ": " + ste.getMethodName() + ": " + ste.getLineNumber());
			}
			errorTag.put("stack", stackList);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return responseBody.toString();
	}
	
	public String doubleToString(Double d) {
	    if (d == null)
	        return null;
	    if (d.isNaN() || d.isInfinite())
	        return d.toString();

	    // Pre Java 8, a value of 0 would yield "0.0" below
	    if (d.doubleValue() == 0)
	        return "0";
	    return new BigDecimal(d.toString()).stripTrailingZeros().toPlainString();
	}
	

}
