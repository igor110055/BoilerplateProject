package com.example.coin.upbit;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
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
public class ExchangeGetWithdrawsChance extends SAProxy {

    public DataSet GetWithdrawsChance(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	String  URL="https://api.upbit.com/v1/withdraws/chance";
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

    	DataTable OUT_DATA_MEMBER_LEVEL = OUT_DS.addTable("OUT_DATA_MEMBER_LEVEL");
    	OUT_DATA_MEMBER_LEVEL.addColumn("SECURITY_LEVEL");//사용자의 보안등급
    	OUT_DATA_MEMBER_LEVEL.addColumn("FEE_LEVEL");//사용자의 수수료등급
    	OUT_DATA_MEMBER_LEVEL.addColumn("EMAIL_VERIFIED");//사용자의 이메일 인증 여부
    	OUT_DATA_MEMBER_LEVEL.addColumn("IDENTITY_AUTH_VERIFIED");//사용자의 실명 인증 여부
    	OUT_DATA_MEMBER_LEVEL.addColumn("BANK_ACCOUNT_VERIFIED");//사용자의 계좌 인증 여부
    	OUT_DATA_MEMBER_LEVEL.addColumn("KAKAO_PAY_AUTH_VERIFIED");//사용자의 카카오페이 인증 여부
    	OUT_DATA_MEMBER_LEVEL.addColumn("LOCKED");//사용자의 계정 보호 상태
    	OUT_DATA_MEMBER_LEVEL.addColumn("WALLET_LOCKED");//사용자의 출금 보호 상태
    	
    	DataTable OUT_DATA_CURRENCY = OUT_DS.addTable("OUT_DATA_CURRENCY");
    	OUT_DATA_CURRENCY.addColumn("CODE");//화폐를 의미하는 영문 대문자 코드
    	OUT_DATA_CURRENCY.addColumn("WITHDRAW_FEE",dataset.type.DataType.BIGDECIMAL,null,"해당 화폐의 출금 수수료");//해당 화폐의 출금 수수료
    	OUT_DATA_CURRENCY.addColumn("IS_COIN");//화폐의 코인 여부
    	OUT_DATA_CURRENCY.addColumn("WALLET_STATE");//해당 화폐의 지갑 상태
    	OUT_DATA_CURRENCY.addColumn("WALLET_SUPPORT");//해당 화폐가 지원하는 입출금 정보
    	
    	DataTable OUT_DATA_ACCOUNT = OUT_DS.addTable("OUT_DATA_ACCOUNT");
    	OUT_DATA_ACCOUNT.addColumn("CURRENCY");//화폐를 의미하는 영문 대문자 코드
    	OUT_DATA_ACCOUNT.addColumn("BALANCE",dataset.type.DataType.BIGDECIMAL,null,"주문가능 금액/수량");//주문가능 금액/수량
    	OUT_DATA_ACCOUNT.addColumn("IS_COIN");//화폐의 코인 여부
    	OUT_DATA_ACCOUNT.addColumn("LOCKED");//주문 중 묶여있는 금액/수량
    	OUT_DATA_ACCOUNT.addColumn("AVG_BUY_PRICE");//매수평균가
    	OUT_DATA_ACCOUNT.addColumn("AVG_BUY_PRICE_MODIFIED");//매수평균가 수정 여부
    	OUT_DATA_ACCOUNT.addColumn("UNIT_CURRENCY");//평단가 기준 화폐

    	DataTable OUT_DATA_WITHDRAW_LIMIT = OUT_DS.addTable("OUT_DATA_WITHDRAW_LIMIT");
    	OUT_DATA_WITHDRAW_LIMIT.addColumn("CURRENCY");//화폐를 의미하는 영문 대문자 코드
    	OUT_DATA_WITHDRAW_LIMIT.addColumn("MINIMUM");//출금 최소 금액/수량
    	OUT_DATA_WITHDRAW_LIMIT.addColumn("ONETIME");//1회 출금 한도
    	OUT_DATA_WITHDRAW_LIMIT.addColumn("DAILY");//1일 출금 한도
    	OUT_DATA_WITHDRAW_LIMIT.addColumn("REMAINING_DAILY");//1일 잔여 출금 한도
    	OUT_DATA_WITHDRAW_LIMIT.addColumn("REMAINING_DAILY_KRW");//통합 1일 잔여 출금 한도
    	OUT_DATA_WITHDRAW_LIMIT.addColumn("FIXED");//출금 금액/수량 소수점 자리 수
    	OUT_DATA_WITHDRAW_LIMIT.addColumn("CAN_WITHDRAW");//출금 지원 여부

    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String CURRENCY =null;
    	if(IN_PSET.getRowCount()>0) {
    		CURRENCY = IN_PSET.getRow(0).getStringNullToEmpty("CURRENCY");
    	}

    	HashMap<String, String> params = new HashMap<>();
		params.put("currency",CURRENCY);
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
			jsonOutString = httpU.httpGetUpbitExchangeApi(UPBIT_OPEN_API_ACCESS_KEY,UPBIT_OPEN_API_SECRET_KEY,URL,queryString);
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

			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);

			return OUT_DS;
		}
		drRst.setString("STATUS", "S");
		
		if(c!=null){				
			HashMap<String,Object>  member_level = (HashMap<String, Object>) c.get("member_level");
			HashMap<String,Object>  currency = (HashMap<String, Object>) c.get("currency");
			HashMap<String,Object>  account = (HashMap<String, Object>) c.get("account");
			HashMap<String,Object>  withdraw_limit = (HashMap<String, Object>) c.get("withdraw_limit");
			if(member_level!=null) {
				DataRow dr = OUT_DATA_MEMBER_LEVEL.addRow();
				dr.setString("SECURITY_LEVEL",member_level.get("security_level").toString());
				dr.setString("FEE_LEVEL",member_level.get("fee_level").toString());
				dr.setString("EMAIL_VERIFIED",member_level.get("email_verified").toString());
				dr.setString("IDENTITY_AUTH_VERIFIED",member_level.get("identity_auth_verified").toString());
				dr.setString("BANK_ACCOUNT_VERIFIED",member_level.get("bank_account_verified").toString());
				dr.setString("KAKAO_PAY_AUTH_VERIFIED",member_level.get("kakao_pay_auth_verified").toString());
				dr.setString("LOCKED",member_level.get("locked").toString());
				dr.setString("WALLET_LOCKED",member_level.get("wallet_locked").toString());
			}

			if(currency!=null) {
				DataRow dr = OUT_DATA_CURRENCY.addRow();
				dr.setString("CODE",currency.get("code").toString());
                dr.setBigDecimal("WITHDRAW_FEE",new BigDecimal(Double.parseDouble(currency.get("withdraw_fee").toString())));
				dr.setString("IS_COIN",currency.get("is_coin").toString());
				dr.setString("WALLET_STATE",currency.get("wallet_state").toString());
				dr.setString("WALLET_SUPPORT",currency.get("wallet_support").toString());
			}

			if(account!=null) {
				DataRow dr = OUT_DATA_ACCOUNT.addRow();
				dr.setString("CURRENCY",account.get("currency").toString());
                dr.setBigDecimal("BALANCE",new BigDecimal(Double.parseDouble(account.get("balance").toString())));
				dr.setString("LOCKED",account.get("locked").toString());
				dr.setString("AVG_BUY_PRICE",account.get("avg_buy_price").toString());
				dr.setString("AVG_BUY_PRICE_MODIFIED",account.get("avg_buy_price_modified").toString());
				dr.setString("UNIT_CURRENCY",account.get("unit_currency").toString());
			}

			if(withdraw_limit!=null) {
				DataRow dr = OUT_DATA_WITHDRAW_LIMIT.addRow();
				dr.setString("CURRENCY",withdraw_limit.get("currency").toString());
				dr.setString("MINIMUM",withdraw_limit.get("minimum").toString());
				if(withdraw_limit.get("onetime")!=null){
					dr.setString("ONETIME",withdraw_limit.get("onetime").toString());
				}
				if(withdraw_limit.get("daily")!=null){
					dr.setString("DAILY",withdraw_limit.get("daily").toString());
				}
				dr.setString("REMAINING_DAILY",withdraw_limit.get("remaining_daily").toString());
				dr.setString("REMAINING_DAILY_KRW",withdraw_limit.get("remaining_daily_krw").toString());
                if(withdraw_limit.get("fixed")!=null){
				    dr.setString("FIXED",withdraw_limit.get("fixed").toString());
                }
				dr.setString("CAN_WITHDRAW",withdraw_limit.get("can_withdraw").toString());
			}
		}

        return OUT_DS;
    }

}
