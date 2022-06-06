package com.example.coin.binance.spotAccountTrade;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.coin.util.HttpUtilBinance;
import com.example.framework.exception.BizException;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;

public class SpotAccountTradePostApiV3TestNewOrder extends SAProxy {
	public DataSet PostApiV3TestNewOrder(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/api/v3/order/test";
		DataSet OUT_DS = new DataSet();
		/* 상태 */
		DataTable OUT_RST = OUT_DS.addTable("OUT_RST");
		OUT_RST.addColumn("URL");
		OUT_RST.addColumn("QUERY_STRING");
		OUT_RST.addColumn("JSON_OUT");
		OUT_RST.addColumn("STATUS"); // E에러 S 성공
		OUT_RST.addColumn("ERR_MSG");
		OUT_RST.addColumn("ERR_CODE"); // 100 외부 api 오류 200 내부오류
		OUT_RST.addColumn("ERR_STACK_TRACE");
		OUT_RST.addColumn("BINANCE_API_KEY");
		OUT_RST.addColumn("BINANCE_API_SECRET");

		DataTable OUT_RSET = OUT_DS.addTable("OUT_RSET");

		OUT_RSET.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol");
		OUT_RSET.addColumn("ORDER_ID", dataset.type.DataType.STRING, null, "orderId");
		OUT_RSET.addColumn("ORDER_LIST_ID", dataset.type.DataType.STRING, null, "orderListId");
		OUT_RSET.addColumn("CLIENT_ORDER_ID", dataset.type.DataType.STRING, null, "clientOrderId");
		OUT_RSET.addColumn("TRANSACT_TIME", dataset.type.DataType.STRING, null, "transactTime");
		OUT_RSET.addColumn("PRICE", dataset.type.DataType.STRING, null, "price");
		OUT_RSET.addColumn("ORIG_QTY", dataset.type.DataType.STRING, null, "origQty");
		OUT_RSET.addColumn("EXECUTED_QTY", dataset.type.DataType.STRING, null, "executedQty");
		OUT_RSET.addColumn("CUMMULATIVE_QUOTE_QTY", dataset.type.DataType.STRING, null, "cummulativeQuoteQty");
		OUT_RSET.addColumn("STATUS", dataset.type.DataType.STRING, null, "status");
		OUT_RSET.addColumn("TIME_IN_FORCE", dataset.type.DataType.STRING, null, "timeInForce");
		OUT_RSET.addColumn("TYPE", dataset.type.DataType.STRING, null, "type");
		OUT_RSET.addColumn("SIDE", dataset.type.DataType.STRING, null, "side");

		DataTable OUT_RSET_FILLS = OUT_DS.addTable("OUT_RSET_FILLS");

		OUT_RSET_FILLS.addColumn("ORDER_ID", dataset.type.DataType.STRING, null, "orderId");
		OUT_RSET_FILLS.addColumn("ORDER_LIST_ID", dataset.type.DataType.STRING, null, "orderListId");
		OUT_RSET_FILLS.addColumn("CLIENT_ORDER_ID", dataset.type.DataType.STRING, null, "clientOrderId");
		OUT_RSET_FILLS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "symbol");
		OUT_RSET_FILLS.addColumn("PRICE", dataset.type.DataType.STRING, null, "price");
		OUT_RSET_FILLS.addColumn("QTY", dataset.type.DataType.STRING, null, "qty");
		OUT_RSET_FILLS.addColumn("COMMISSION", dataset.type.DataType.STRING, null, "commission");
		OUT_RSET_FILLS.addColumn("COMMISSION_ASSET", dataset.type.DataType.STRING, null, "commissionAsset");
        OUT_RSET_FILLS.addColumn("SIDE", dataset.type.DataType.STRING, null, "side");

		DataTable dtKey = InDs.getTable("IN_KEY");
		DataTable dt = InDs.getTable("IN_PSET");

		// https://academy.binance.com/ko/articles/binance-api-series-pt-1-spot-trading-with-postman
		// symbol – 이전에 우연히 이를 본 적이 있습니다. 이는 여러분이 거래하고자 하는 쌍입니다.
		String SYMBOL = null;
		// side – 여기서는 여러분의 매수 또는 매도 여부를 결정합니다. BTCUSDT 쌍에서 BUY는 USDT로 BTC를 구매하고 싶다는
		// 것이며, SELL은 BTC를 USDT로 판매하고 싶다는 것입니다.
		String SIDE = null; // BUY,SELL
		// type – 여러분이 제출하고자 하는 주문 유형입니다. 가능한 값은 다음과 같습니다(여기에 자세히 설명되어 있음):
		String TYPE = null; // LIMIT,MARKET,STOP_LOSS,STOP_LOSS_LIMIT,TAKE_PROFIT,TAKE_PROFIT_LIMIT,LIMIT_MARKET
		String TIME_IN_FORCE = null; // GTC,FOK,IOC
		/*
		 * GTC (good till canceled) – 가장 널리 사용되는 설정 중 하나일 GTC는 주문이 체결되거나 이를 취소하기 전까지 유효한
		 * 주문입니다.
		 */
		/*
		 * FOK (fill or kill) – FOK 거래소에게 한 번에 주문을 실행하도록 지시합니다. 만약 그럴 수 없다면, 주문이 즉시
		 * 취소됩니다.
		 */
		/*
		 * IOC (immediate or cancel) – 전부 혹은 일부 주문이 즉시 실행되거나 취소됩니다. FOK와 달리, 일부만 체겨될 수
		 * 있다면 주문은 취소되지 않습니다.
		 */

		String QUANTITY = null;
		/*
		 * quantity – 여러분이 매수 또는 매도하고자 하는 자산 수량입니다.
		 */
		String QUOTE_ORDER_QTY = null;
		String PRICE = null;
		/*
		 * price – 여러분이 매도하고자 하는 가격입니다. BTCUSDT 쌍에서는 USDT로 표시됩니다.
		 */
		String NEW_CLIENT_ORDER_ID = null;
		/*
		 * newClientOrderId – 주문 식별자입니다. 필수 영역은 아니지만, 나중에 쿼리를 쉽게 처리할 수 있도록 식별자로 설정할 수
		 * 있습니다. 그렇지 않은 경우, 거래소에 의해 임의로 생성됩니다.
		 */

		String STOP_PRICE = null;
		String ICEBERG_QTY = null; // 주문을 여러개로 나눠서 내는 기능

		String RECV_WINDOW = null;
		String TIMESTAMP = null;

		String BINANCE_API_KEY = null;
		String BINANCE_API_SECRET = null;

		if (dt.getRowCount() > 0) {
			// https://academy.binance.com/ko/articles/binance-api-series-pt-1-spot-trading-with-postman
			SYMBOL = dt.getRow(0).getStringNullToEmpty("SYMBOL");
			SIDE = dt.getRow(0).getStringNullToEmpty("SIDE"); // BUY,SELL
			TYPE = dt.getRow(0).getStringNullToEmpty("TYPE");
			/*
			 * LIMIT MARKET STOP_LOSS STOP_LOSS_LIMIT TAKE_PROFIT TAKE_PROFIT_LIMIT
			 * LIMIT_MAKER
			 */
			TIME_IN_FORCE = dt.getRow(0).getStringNullToEmpty("TIME_IN_FORCE");
			/*
			 * GTC (good till canceled) – 가장 널리 사용되는 설정 중 하나일 GTC는 주문이 체결되거나 이를 취소하기 전까지 유효한
			 * 주문입니다.
			 */
			/*
			 * FOK (fill or kill) – FOK 거래소에게 한 번에 주문을 실행하도록 지시합니다. 만약 그럴 수 없다면, 주문이 즉시
			 * 취소됩니다.
			 */
			/*
			 * IOC (immediate or cancel) – 전부 혹은 일부 주문이 즉시 실행되거나 취소됩니다. FOK와 달리, 일부만 체겨될 수
			 * 있다면 주문은 취소되지 않습니다.
			 * 
			 */
			QUANTITY = dt.getRow(0).getStringNullToEmpty("QUANTITY");

			QUOTE_ORDER_QTY = dt.getRow(0).getStringNullToEmpty("QUOTE_ORDER_QTY");
			PRICE = dt.getRow(0).getStringNullToEmpty("PRICE");
			NEW_CLIENT_ORDER_ID = dt.getRow(0).getStringNullToEmpty("NEW_CLIENT_ORDER_ID");
			STOP_PRICE = dt.getRow(0).getStringNullToEmpty("STOP_PRICE");
			ICEBERG_QTY = dt.getRow(0).getStringNullToEmpty("ICEBERG_QTY");

			RECV_WINDOW = dt.getRow(0).getStringNullToEmpty("RECV_WINDOW");
			TIMESTAMP = dt.getRow(0).getStringNullToEmpty("TIMESTAMP");
		}

		if (dtKey.getRowCount() > 0)

		{
			BINANCE_API_KEY = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_KEY");
			BINANCE_API_SECRET = dtKey.getRow(0).getStringNullToEmpty("BINANCE_API_SECRET");
		}

		HashMap<String, String> params = new HashMap<>();
		params.put("symbol", SYMBOL);
		params.put("side", SIDE);
		params.put("type", TYPE);

		if (!PjtUtil.g().isEmpty(TIME_IN_FORCE)) {
			params.put("timeInForce", TIME_IN_FORCE);
		}

		if (!PjtUtil.g().isEmpty(QUANTITY)) {
			params.put("quantity", QUANTITY);
		}

		if (!PjtUtil.g().isEmpty(QUOTE_ORDER_QTY)) {
			params.put("quoteOrderQty", QUOTE_ORDER_QTY);
		}

		if (!PjtUtil.g().isEmpty(PRICE)) {
			params.put("price", PRICE);
		}

		if (!PjtUtil.g().isEmpty(NEW_CLIENT_ORDER_ID)) {
			params.put("newClientOrderId", NEW_CLIENT_ORDER_ID);
		}

		if (!PjtUtil.g().isEmpty(STOP_PRICE)) {
			params.put("stopPrice", STOP_PRICE);
		}

		if (!PjtUtil.g().isEmpty(ICEBERG_QTY)) {
			params.put("icebergQty", ICEBERG_QTY);
		}

		params.put("newOrderRespType", "FULL");
		/*
		 * 응답 JSON을 설정합니다. ACK, RESULT, 또는 FULL; MARKET및 LIMIT주문 유형의 기본값은 FULL이고 다른 모든
		 * 주문의 기본값은 ACK입니다.
		 */

		if (!PjtUtil.g().isEmpty(RECV_WINDOW)) {
			params.put("recvWindow", RECV_WINDOW);
		}

		if (!PjtUtil.g().isEmpty(TIMESTAMP)) {
			params.put("timestamp", TIMESTAMP);
		}

		DataRow drRst = OUT_RST.addRow();
		drRst.setString("URL", URL);

		if (PjtUtil.g().isEmpty(SYMBOL)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "SYMBOL 이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(SIDE)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "SIDE 이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(TYPE)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "TYPE 이 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		} else {
			switch (TYPE) {
				case "LIMIT":
					if (PjtUtil.g().isEmpty(TIME_IN_FORCE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 LIMIT일때  TIME_IN_FORCE 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}

					if (PjtUtil.g().isEmpty(QUANTITY)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 LIMIT일때  QUANTITY 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}

					if (PjtUtil.g().isEmpty(PRICE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 LIMIT일때  PRICE 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					break;
				case "MARKET":
					if (PjtUtil.g().isEmpty(QUANTITY) && PjtUtil.g().isEmpty(QUOTE_ORDER_QTY)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 MARKET일때  QUANTITY 또는 QUOTE_ORDER_QTY 값중 하나는 입력되어야 합니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					break;
				case "STOP_LOSS":
					if (PjtUtil.g().isEmpty(STOP_PRICE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 STOP_LOSS일때  STOP_PRICE 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					if (PjtUtil.g().isEmpty(QUANTITY)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 STOP_LOSS일때  QUANTITY 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					break;
				case "STOP_LOSS_LIMIT":
					if (PjtUtil.g().isEmpty(TIME_IN_FORCE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 STOP_LOSS_LIMIT일때  TIME_IN_FORCE(ENUM) 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					if (PjtUtil.g().isEmpty(QUANTITY)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 STOP_LOSS_LIMIT일때  QUANTITY 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					if (PjtUtil.g().isEmpty(PRICE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 STOP_LOSS_LIMIT일때  PRICE 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					if (PjtUtil.g().isEmpty(STOP_PRICE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 STOP_LOSS_LIMIT일때  STOP_PRICE 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					break;
				case "TAKE_PROFIT":

					if (PjtUtil.g().isEmpty(QUANTITY)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 TAKE_PROFIT일때  QUANTITY 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}

					if (PjtUtil.g().isEmpty(STOP_PRICE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 TAKE_PROFIT일때  STOP_PRICE 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					break;
				case "TAKE_PROFIT_LIMIT":
					if (PjtUtil.g().isEmpty(TIME_IN_FORCE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 TAKE_PROFIT_LIMIT일때  TIME_IN_FORCE(ENUM) 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					if (PjtUtil.g().isEmpty(QUANTITY)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 TAKE_PROFIT_LIMIT일때  QUANTITY 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					if (PjtUtil.g().isEmpty(PRICE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 TAKE_PROFIT_LIMIT일때  PRICE 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					if (PjtUtil.g().isEmpty(STOP_PRICE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 TAKE_PROFIT_LIMIT일때  STOP_PRICE 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					break;
				case "LIMIT_MAKER":

					if (PjtUtil.g().isEmpty(QUANTITY)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 LIMIT_MAKER일때  QUANTITY 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}
					if (PjtUtil.g().isEmpty(PRICE)) {
						drRst.setString("JSON_OUT", "");
						drRst.setString("STATUS", "E");
						drRst.setString("ERR_MSG", "TYPE값이 LIMIT_MAKER일때  PRICE 값은 필수입니다.");
						drRst.setString("ERR_CODE", "200");
						drRst.setString("ERR_STACK_TRACE", "");
						drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
						drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
						return OUT_DS;
					}

					break;
				default:
					break;

			}

		}

		if (!PjtUtil.g().isEmpty(ICEBERG_QTY)) {
			if (!TIME_IN_FORCE.equals("GTC")) {
				drRst.setString("JSON_OUT", "");
				drRst.setString("STATUS", "E");
				drRst.setString("ERR_MSG", "ICEBERG_QTY를 사용한다면  TIME_IN_FORCE값은 GTC이어야 합니다.");
				drRst.setString("ERR_CODE", "200");
				drRst.setString("ERR_STACK_TRACE", "");
				drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
				drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

				return OUT_DS;
			}
		}

		if (PjtUtil.g().isEmpty(TIMESTAMP))

		{
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "TIMESTAMP-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(BINANCE_API_KEY)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "BINANCE_API_KEY-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		if (PjtUtil.g().isEmpty(BINANCE_API_SECRET)) {
			// 에러처리
			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "BINANCE_API_SECRET-API키가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);

			return OUT_DS;
		}

		String jsonOutString = "";

		HttpUtilBinance httpU = new HttpUtilBinance();
		ArrayList<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));
		try {
			jsonOutString = httpU.httpPostBiniance(BINANCE_API_KEY, BINANCE_API_SECRET, URL, queryString);
			drRst.setString("JSON_OUT", jsonOutString);

			System.out.println(jsonOutString);

			HashMap<String, Object> c = null;
			try {
				c = PjtUtil.g().JsonStringToObject(jsonOutString, HashMap.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				// System.out.println(exceptionAsString);
				// 출처: https://blog.miyam.net/81 [낭만 프로그래머]

				drRst.setString("JSON_OUT", "");
				drRst.setString("STATUS", "E");
				drRst.setString("ERR_CODE", "200");
				drRst.setString("ERR_MSG", e.getMessage());
				drRst.setString("ERR_STACK_TRACE", exceptionAsString);

				return OUT_DS;
			}
			drRst.setString("STATUS", "S");

			if (c != null) {
                System.out.println(c);
                System.out.println(c.size());
                if(c.size()>0){
                    DataRow DR_OUT_RSET = OUT_RSET.addRow();
                    DR_OUT_RSET.setString("SYMBOL", c.get("symbol").toString());
    
                    DR_OUT_RSET.setString("ORDER_ID", c.get("orderId").toString());
                    DR_OUT_RSET.setString("ORDER_LIST_ID", c.get("orderListId").toString());
                    DR_OUT_RSET.setString("CLIENT_ORDER_ID", c.get("clientOrderId").toString());
                    DR_OUT_RSET.setString("TRANSACT_TIME", c.get("transactTime").toString());
                    DR_OUT_RSET.setString("PRICE", c.get("price").toString());
                    DR_OUT_RSET.setString("ORIG_QTY", c.get("origQty").toString());
                    DR_OUT_RSET.setString("EXECUTED_QTY", c.get("executedQty").toString());
    
                    DR_OUT_RSET.setString("CUMMULATIVE_QUOTE_QTY", c.get("cummulativeQuoteQty").toString());
                    DR_OUT_RSET.setString("STATUS", c.get("status").toString());
                    DR_OUT_RSET.setString("TIME_IN_FORCE", c.get("timeInForce").toString());
    
                    DR_OUT_RSET.setString("TYPE", c.get("type").toString());
                    DR_OUT_RSET.setString("SIDE", c.get("side").toString());
    
                    if (c.get("fills") != null) {
                        List<Map<String, Object>> al = (List<Map<String, Object>>) c.get("fills");
                        for (int i = 0; i < al.size(); i++) {
                            Map<String, Object> tmp = al.get(i);
                            DataRow DR_OUT_RSET_FILLS = OUT_RSET_FILLS.addRow();
                            
                            DR_OUT_RSET_FILLS.setString("SYMBOL", c.get("symbol").toString());
                            DR_OUT_RSET_FILLS.setString("ORDER_ID", c.get("orderListId").toString());
                            DR_OUT_RSET_FILLS.setString("ORDER_LIST_ID", c.get("orderListId").toString());
                            DR_OUT_RSET_FILLS.setString("CLIENT_ORDER_ID", c.get("clientOrderId").toString());
                            DR_OUT_RSET_FILLS.setString("SIDE", c.get("side").toString());
                            DR_OUT_RSET_FILLS.setString("PRICE", tmp.get("price").toString());
                            DR_OUT_RSET_FILLS.setString("QTY", tmp.get("qty").toString());
                            DR_OUT_RSET_FILLS.setString("COMMISSION", tmp.get("commission").toString());
                            DR_OUT_RSET_FILLS.setString("COMMISSION_ASSET", tmp.get("commissionAsset").toString());
                        }
                    }
                }  
			}

			return OUT_DS;
		} catch (NoSuchAlgorithmException | URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();

			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "100");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
			return OUT_DS;
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();

			drRst.setString("JSON_OUT", jsonOutString);
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			drRst.setString("BINANCE_API_KEY", BINANCE_API_KEY);
			drRst.setString("BINANCE_API_SECRET", BINANCE_API_SECRET);
			return OUT_DS;
		}

	}

}
