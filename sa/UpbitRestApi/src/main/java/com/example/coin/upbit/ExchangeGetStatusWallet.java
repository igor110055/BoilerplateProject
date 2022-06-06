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
public class ExchangeGetStatusWallet extends SAProxy {

    public DataSet GetStatusWallet(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	String  URL="https://api.upbit.com/v1/status/wallet";
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

    	DataTable OUT_REST = OUT_DS.addTable("OUT_RSET");

    	OUT_REST.addColumn("CURRENCY");//화폐를 의미하는 영문 대문자 코드
		/*
		입출금 상태
- working : 입출금 가능
- withdraw_only : 출금만 가능
- deposit_only : 입금만 가능
- paused : 입출금 중단
- unsupported : 입출금 미지원
		
		*/
    	OUT_REST.addColumn("WALLET_STATE");//입출금 상태
		/*
		블록 상태
- normal : 정상
- delayed : 지연
- inactive : 비활성 (점검 등)		
		*/
    	OUT_REST.addColumn("BLOCK_STATE");//블록 상태
    	OUT_REST.addColumn("BLOCK_HEIGHT");//블록 높이
    	OUT_REST.addColumn("BLOCK_UPDATED_AT");//블록 갱신 시각
    	

	    DataRow drRst = OUT_RST.addRow();
	    drRst.setString("URL", URL);
	    
	    DataTable dtKey =InDs.getTable("IN_KEY");
    	String UPBIT_OPEN_API_ACCESS_KEY =null;
    	String UPBIT_OPEN_API_SECRET_KEY =null;
    	if(dtKey.getRowCount()>0) {
    		UPBIT_OPEN_API_ACCESS_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_ACCESS_KEY");
    		UPBIT_OPEN_API_SECRET_KEY = dtKey.getRow(0).getStringNullToEmpty("UPBIT_OPEN_API_SECRET_KEY");
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
			jsonOutString = httpU.httpGetUpbitExchangeApi(UPBIT_OPEN_API_ACCESS_KEY,UPBIT_OPEN_API_SECRET_KEY,URL);
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

			drRst.setString("JSON_OUT", jsonOutString);
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			return OUT_DS;
		}
        drRst.setString("JSON_OUT", jsonOutString);

        ArrayList<HashMap<String,Object>> al = null;
		try {
			al = PjtUtil.g().JsonStringToObject(jsonOutString, ArrayList.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			//System.out.println(exceptionAsString);
			//출처: https://blog.miyam.net/81 [낭만 프로그래머]

			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);

			return OUT_DS;
		}
		drRst.setString("STATUS", "S");
		
		for(int i=0;i<al.size();i++) {
			HashMap<String,Object>  c= al.get(i);
			DataRow dr = OUT_REST.addRow();
			dr.setString("CURRENCY",c.get("currency").toString());
			dr.setString("WALLET_STATE",c.get("wallet_state").toString());
            if(c.get("block_state")!=null) {
                dr.setString("BLOCK_STATE",c.get("block_state").toString());
            } 
			
			if(c.get("block_height")!=null) {
				dr.setString("BLOCK_HEIGHT",c.get("block_height").toString());	
			}			
            if(c.get("block_updated_at")!=null) {
                dr.setString("BLOCK_UPDATED_AT",c.get("block_updated_at").toString());			
            }
			
		}

        return OUT_DS;
    }

}
