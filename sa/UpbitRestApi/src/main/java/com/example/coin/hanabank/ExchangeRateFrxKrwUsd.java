package com.example.coin.hanabank;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;
public class ExchangeRateFrxKrwUsd extends SAProxy {

    public DataSet GetFrxKrwUsd(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	String  URL="https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD";
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
    	OUT_RSET.addColumn("CODE", dataset.type.DataType.STRING,null,"FRX.KRWUSD");
    	OUT_RSET.addColumn("CURRENCY_CODE", dataset.type.DataType.STRING,null,"USD");
    	OUT_RSET.addColumn("CURRENCY_NAME", dataset.type.DataType.STRING,null,"달러");
    	OUT_RSET.addColumn("COUNTRY", dataset.type.DataType.STRING,null,"미국");
    	OUT_RSET.addColumn("NAME", dataset.type.DataType.STRING,null,"미국 (KRW/USD)");
    	OUT_RSET.addColumn("DATE", dataset.type.DataType.STRING,null,"2021-11-22");
    	OUT_RSET.addColumn("TIME", dataset.type.DataType.STRING,null,"20:03:36");
    	OUT_RSET.addColumn("BASE_PRICE", dataset.type.DataType.STRING,null,"1187.00");       

	    DataRow drRst = OUT_RST.addRow();
	    drRst.setString("URL", URL);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL);
        httpGet.addHeader("Content-type", "application/json; charset=UTF-8");
        URIBuilder uriBuilder = new URIBuilder(httpGet.getURI());
        URI uri =uriBuilder.build();
        System.out.println("uri = "+uri.toString());
        HttpRequestBase req =httpGet;
        req.setURI(uri);

        String jsonOutString="";
        try (CloseableHttpResponse response = client.execute(httpGet)){
            StatusLine tmp_status= response.getStatusLine();
            int status_code = tmp_status.getStatusCode();
            System.out.println("URL:"+URL);
            System.out.println("request.getURI().getRawQuery():"+req.getURI().getRawQuery());
            jsonOutString = EntityUtils.toString(response.getEntity(), "UTF-8");
        }


        ArrayList<HashMap<String,Object>> al = null;
		try {
			al = PjtUtil.g().JsonStringToObject(jsonOutString, ArrayList.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();

            drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			return OUT_DS;
		}
		drRst.setString("STATUS", "S");

		for (HashMap<String, Object> c : al) {
			DataRow dr = OUT_RSET.addRow();

			dr.setString("CODE",c.get("code").toString());
			dr.setString("CURRENCY_CODE",c.get("currencyCode").toString());
			dr.setString("CURRENCY_NAME",c.get("currencyName").toString());
			dr.setString("COUNTRY",c.get("country").toString());
			dr.setString("NAME",c.get("name").toString());
            dr.setString("DATE",c.get("date").toString());
            dr.setString("TIME",c.get("time").toString());
            dr.setString("BASE_PRICE",c.get("basePrice").toString());
		}
        return OUT_DS;
    }

}
