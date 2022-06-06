package com.example.coin.binance.spotAccountTrade;

import com.example.coin.binance.marketDataEndPoints.MarketDataEndPointsGetApiV3Time;

import org.junit.Test;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;

public class SpotAccountTradePostApiV3TestNewOrderTest {

	@Test
	public void testPostApiV3TestNewOrder() {
		SpotAccountTradePostApiV3TestNewOrder tmp = new SpotAccountTradePostApiV3TestNewOrder();

		DataSet IN_DS = new DataSet();
		DataTable IN_PSET = IN_DS.addTable("IN_PSET");
		IN_PSET.addColumn("SYMBOL");
		IN_PSET.addColumn("SIDE");
		IN_PSET.addColumn("TYPE");
		IN_PSET.addColumn("TIME_IN_FORCE");
		IN_PSET.addColumn("QUANTITY");

		IN_PSET.addColumn("QUOTE_ORDER_QTY");
		IN_PSET.addColumn("PRICE");
		IN_PSET.addColumn("NEW_CLIENT_ORDER_ID");
		IN_PSET.addColumn("STOP_PRICE");
		IN_PSET.addColumn("ICEBERG_QTY");
		IN_PSET.addColumn("NEW_ORDER_RESP_TYPE");

		IN_PSET.addColumn("RECV_WINDOW");
		IN_PSET.addColumn("TIMESTAMP");

		MarketDataEndPointsGetApiV3Time st = new MarketDataEndPointsGetApiV3Time();
		DataSet DS_ST = null;
		String serverTime = "";
		try {
			DS_ST = st.GetApiV3Time(new DataSet(), null, null);
			DataTable dt = DS_ST.getTable("OUT_RSET");
			if (dt.getRowCount() > 0) {
				serverTime = dt.getRow(0).getString("SERVER_TIME");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DataRow DR_IN_PSET = IN_PSET.addRow();
		DR_IN_PSET.setString("SYMBOL", "BTCUSDT");
		DR_IN_PSET.setString("SIDE", "BUY");
		DR_IN_PSET.setString("TYPE", "LIMIT");
		DR_IN_PSET.setString("TIME_IN_FORCE", "GTC");
		//DR_IN_PSET.setString("QUANTITY", "173744.7441822899");
        //DR_IN_PSET.setString("QUANTITY", "173744.744182289");  //통과 안됨
        //DR_IN_PSET.setString("QUANTITY", "173744.74418228");  //자릿수가 8개까지 되는구나. exchange_info에 base_asset_precision 같다.
        //DR_IN_PSET.setString("QUANTITY", "173744.7"); //이것도 LOT_SIZE 오류가 낫다.
        DR_IN_PSET.setString("QUANTITY", "0.04390");  //이건 통과했다.
          //그다음 에러 발생하는 것이 LOT_SIZE 인데
         // 이돈에 LOT_SIZE는 MIN_QTY는 1 이고  MAX_QTY는 92141578.00000000 이다.

          

                               // TICK_SIZE==>   0.00000100
		// DR_IN_PSET.setString("QUOTE_ORDER_QTY", "");
	    DR_IN_PSET.setString("PRICE", "47279.98");
		// DR_IN_PSET.setString("NEW_CLIENT_ORDER_ID", "");
		// DR_IN_PSET.setString("STOP_PRICE", "");
		// DR_IN_PSET.setString("ICEBERG_QTY", "");
		// DR_IN_PSET.setString("NEW_ORDER_RESP_TYPE", "");

		DR_IN_PSET.setString("RECV_WINDOW", "");
		DR_IN_PSET.setString("TIMESTAMP", serverTime);

		/* 상태 */
		DataTable IN_KEY = IN_DS.addTable("IN_KEY");
		IN_KEY.addColumn("BINANCE_API_KEY");
		IN_KEY.addColumn("BINANCE_API_SECRET");

		String accessKey = System.getenv("BINANCE-API-KEY");
		String secretKey = System.getenv("BINANCE-API-SECRET");

        System.out.println("accessKey=>"+accessKey);
        System.out.println("secretKey=>"+secretKey);

		DataRow drKey = IN_KEY.addRow();
		drKey.setString("BINANCE_API_KEY", accessKey);
		drKey.setString("BINANCE_API_SECRET", secretKey);

        /*https://systemtraders.tistory.com/1001
        binance api 에러 설명이 잘되어있는 사이트


        "Filter failure: MIN_NOTIONAL" ===>  입력한 가격 * 수량에 의한 주문 총액이 너무 작은 규모임


        알고보니 ExchangeInfo api를 호출해서
        FilterType들의 배열에  여러가지 정보들이 있었다
        PRICE_FILTER    ==>입력한 가격이 너무 낮/높거나 symbol의 exchange tick size에 맞지 않음
        PERCENT_PRRICE         
        MIN_NOTIONAL    ==>  입력한 가격 * 수량에 의한 주문 총액이 너무 작은 규모임
        ICEBER_PARTS
        MARKET_LOT_SIZE

        MAX_NUM_ORDERS
        MAX_NUM_ALGO_ORDERS  ==>옆에와 같은 호출을 5번만 할수있다는 것 같다.  "Algo" orders are STOP_LOSS, STOP_LOSS_LIMIT, TAKE_PROFIT, and TAKE_PROFIT_LIMIT orders.

        등등 이중 MIN_NOTIONAL의 경우 최소 주문수량에 관련된것이었다.
        https://systemtraders.tistory.com/1001


        {"timezone":"UTC","serverTime":1638112344703,
        "rateLimits":[
            {"rateLimitType":"REQUEST_WEIGHT"
            ,"interval":"MINUTE"
            ,"intervalNum":1
            ,"limit":1200
        },
        {"rateLimitType":"ORDERS",
            "interval":"SECOND"
            ,"intervalNum":10
            ,"limit":50
        },
        
        {"rateLimitType":"ORDERS"
            ,"interval":"DAY"
            ,"intervalNum":1
            ,"limit":160000
        }
        ,{
            "rateLimitType":"RAW_REQUESTS"
            ,"interval":"MINUTE"
            ,"intervalNum":5
            ,"limit":6100
        }]
        ,"exchangeFilters":[]
        ,"symbols":[
            {"symbol":"STRAXUSDT","status":"TRADING","baseAsset":"STRAX","baseAssetPrecision":8,"quoteAsset":"USDT","quotePrecision":8,"quoteAssetPrecision":8,"baseCommissionPrecision":8,"quoteCommissionPrecision":8,"orderTypes":["LIMIT","LIMIT_MAKER","MARKET","STOP_LOSS_LIMIT","TAKE_PROFIT_LIMIT"],"icebergAllowed":true,"ocoAllowed":true,"quoteOrderQtyMarketAllowed":true,"isSpotTradingAllowed":true,"isMarginTradingAllowed":false
            ,"filters":[
                    {"filterType":"PRICE_FILTER","minPrice":"0.00100000","maxPrice":"1000.00000000","tickSize":"0.00100000"}
                    ,{"filterType":"PERCENT_PRICE","multiplierUp":"5","multiplierDown":"0.2","avgPriceMins":5}
                    ,{"filterType":"LOT_SIZE","minQty":"0.10000000","maxQty":"900000.00000000","stepSize":"0.10000000"}
                    ,{"filterType":"MIN_NOTIONAL","minNotional":"10.00000000","applyToMarket":true,"avgPriceMins":5}
                    ,{"filterType":"ICEBERG_PARTS","limit":10}
                    ,{"filterType":"MARKET_LOT_SIZE","minQty":"0.00000000","maxQty":"69273.47032661","stepSize":"0.00000000"}
                    ,{"filterType":"MAX_NUM_ORDERS","maxNumOrders":200}
                    ,{"filterType":"MAX_NUM_ALGO_ORDERS","maxNumAlgoOrders":5}
            ]
            ,"permissions":["SPOT"]}]
    
    }

        */

		DataSet OUT_DS;
		try {
			OUT_DS = tmp.PostApiV3TestNewOrder(IN_DS, null, null);
			String inString = DotNetXmlDataSetConverter.convertFromDataSet(IN_DS);
			System.out.println(inString);
			String outString = DotNetXmlDataSetConverter.convertFromDataSet(OUT_DS);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
