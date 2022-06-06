package com.example.coin.upbit;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.coin.util.HttpUtilUpbit;
import com.example.framework.exception.BizException;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;
public class ExchangePostWithdrawsCoin extends SAProxy {

    public DataSet PostWithdrawsCoin(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	String  URL="https://api.upbit.com/v1/withdraws/coin";
    	DataSet OUT_DS = new DataSet();
    	/*상태 */
    	DataTable OUT_RST = OUT_DS.addTable("OUT_RST");
    	OUT_RST.addColumn("URL");
    	OUT_RST.addColumn("QUERY_STRING");
    	OUT_RST.addColumn("JSON_OUT");
    	OUT_RST.addColumn("STATUS");  // E에러   S 성공
    	OUT_RST.addColumn("ERR_MSG");
    	OUT_RST.addColumn("ERR_CODE");  //100  외부 api 오류  200 내부오류
    	OUT_RST.addColumn("ERR_STACK_TRACE");

    	DataTable OUT_RSET = OUT_DS.addTable("OUT_RSET");
    	OUT_RSET.addColumn("TYPE");//입출금 종류
    	OUT_RSET.addColumn("UUID");//출금의 고유 아이디
    	OUT_RSET.addColumn("CURRENCY");//화폐를 의미하는 영문 대문자 코드
    	OUT_RSET.addColumn("TXID");//출금의 트랜잭션 아이디
    	OUT_RSET.addColumn("STATE");//출금 상태
    	OUT_RSET.addColumn("CREATED_AT");//출금 생성 시간
    	OUT_RSET.addColumn("DONE_AT");//출금 완료 시간
    	OUT_RSET.addColumn("AMOUNT");//출금 금액/수량
    	OUT_RSET.addColumn("FEE");//출금 수수료
    	OUT_RSET.addColumn("TRANSACTION_TYPE");//출금 유형
    	
    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String CURRENCY =null;
    	String AMOUNT =null;
    	String ADDRESS =null;
    	String SECONDARY_ADDRESS =null;
    	String TRANSACTION_TYPE =null;
    	if(IN_PSET.getRowCount()>0) {
    		CURRENCY = IN_PSET.getRow(0).getStringNullToEmpty("CURRENCY");
    		AMOUNT = IN_PSET.getRow(0).getStringNullToEmpty("AMOUNT");
    		ADDRESS = IN_PSET.getRow(0).getStringNullToEmpty("ADDRESS");
    		SECONDARY_ADDRESS = IN_PSET.getRow(0).getStringNullToEmpty("SECONDARY_ADDRESS");
    		TRANSACTION_TYPE = IN_PSET.getRow(0).getStringNullToEmpty("TRANSACTION_TYPE");
    	}

    	HashMap<String, String> params = new HashMap<>();
        params.put("currency", CURRENCY);
        params.put("amount", AMOUNT);
        params.put("address", ADDRESS);
        params.put("secondary_address", SECONDARY_ADDRESS);
        params.put("transaction_type", TRANSACTION_TYPE);
		
		ArrayList<String> queryElements = new ArrayList<>();
	    for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));


    	DataTable dtKey =InDs.getTable("IN_KEY");
    	String UPBIT_OPEN_API_ACCESS_KEY =null;
    	String UPBIT_OPEN_API_SECRET_KEY =null;
    	if(dtKey.getRowCount()>0) {
    		UPBIT_OPEN_API_ACCESS_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_ACCESS_KEY");
    		UPBIT_OPEN_API_SECRET_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_SECRET_KEY");
    	}

	    DataRow drRst = OUT_RST.addRow();
	    drRst.setString("URL", URL);
	    drRst.setString("QUERY_STRING", queryString);
	    
	    if(PjtUtil.g().isEmpty(TRANSACTION_TYPE)){
	        //에러처리
	    	drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "TRANSACTION_TYPE가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");

			return OUT_DS;
	    }
	    /*
	     * 출금 유형
default : 일반출금
internal : 바로출금
	     * */
	    
	    if(
	    		!(TRANSACTION_TYPE.equals("default")|| TRANSACTION_TYPE.equals("internal"))
	    		){
	        //에러처리
	    	drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "TRANSACTION_TYPE값은  default,internal이어야 합니다."+TRANSACTION_TYPE);
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");

			return OUT_DS;
	    }
	    

	    if(PjtUtil.g().isEmpty(UPBIT_OPEN_API_ACCESS_KEY)){
            //에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "UPBIT_OPEN_API_ACCESS_KEY-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");

			return OUT_DS;
	    }

	    if(PjtUtil.g().isEmpty(UPBIT_OPEN_API_SECRET_KEY)){
	        //에러처리
	    	drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "UPBIT_OPEN_API_SECRET_KEY-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");

			return OUT_DS;
	    }
	    String jsonOutString = "";
    	HttpUtilUpbit  httpU = new HttpUtilUpbit();
    	
    	
        try {
			jsonOutString = httpU.httpPostUpbitExchangeApi(UPBIT_OPEN_API_ACCESS_KEY,UPBIT_OPEN_API_SECRET_KEY,URL,params);
		} catch (NoSuchAlgorithmException | URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			//System.out.println(exceptionAsString);
			//출처: https://blog.miyam.net/81 [낭만 프로그래머]

			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "100");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			return OUT_DS;
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			//System.out.println(exceptionAsString);
			//출처: https://blog.miyam.net/81 [낭만 프로그래머]
			String err = e.getMessage().replaceAll(":","->");	
			drRst.setString("JSON_OUT", jsonOutString);
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", err);
			drRst.setString("ERR_CODE", "201");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			return OUT_DS;
		}
        drRst.setString("JSON_OUT", jsonOutString);

        HashMap<String,Object> c = null;
		try {
			c = PjtUtil.g().JsonStringToObject(jsonOutString, HashMap.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			//System.out.println(exceptionAsString);
			//출처: https://blog.miyam.net/81 [낭만 프로그래머]
			String err = e.getMessage().replaceAll(":","->");
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_CODE", "202");
			drRst.setString("ERR_MSG",err);
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);

			return OUT_DS;
		}
		drRst.setString("STATUS", "S");
		
		if(c!=null){				
			DataRow dr = OUT_RSET.addRow();
			dr.setString("TYPE",c.get("type").toString());
			dr.setString("UUID",c.get("uuid").toString());
			dr.setString("CURRENCY",c.get("currency").toString());
			dr.setString("TXID",c.get("txid").toString());
			dr.setString("STATE",c.get("state").toString());
			dr.setString("CREATED_AT",c.get("created_at").toString());
			dr.setString("DONE_AT",c.get("done_at").toString());
			dr.setString("AMOUNT",c.get("amount").toString());
			dr.setString("FEE",c.get("fee").toString());
			dr.setString("TRANSACTION_TYPE",c.get("transaction_type").toString());
		}
        return OUT_DS;
    }

}
