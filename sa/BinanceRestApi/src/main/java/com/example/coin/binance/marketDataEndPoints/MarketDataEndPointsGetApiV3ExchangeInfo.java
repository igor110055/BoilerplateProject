package com.example.coin.binance.marketDataEndPoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.coin.util.HttpUtilBinance;
import com.example.framework.utils.PjtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;

public class MarketDataEndPointsGetApiV3ExchangeInfo extends SAProxy {
	// https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
	public DataSet GetApiV3ExchangeInfo(DataSet InDs, String InDsNames, String outDsNames) throws Exception {
		String URL = "https://api.binance.com/api/v3/exchangeInfo";
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

		DataTable OUT_REST = OUT_DS.addTable("OUT_RSET");
		OUT_REST.addColumn("TIMEZONE", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("SERVER_TIME", dataset.type.DataType.STRING, null, "");
		OUT_REST.addColumn("EXCHANGE_FILTERS", dataset.type.DataType.STRING, null, "");

		DataTable OUT_RSET_RATE_LIMITS = OUT_DS.addTable("OUT_RSET_RATE_LIMITS");
		OUT_RSET_RATE_LIMITS.addColumn("RATE_LIMIT_TYPE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_RATE_LIMITS.addColumn("INTERVAL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_RATE_LIMITS.addColumn("INTERVAL_NUM", dataset.type.DataType.STRING, null, "");
		OUT_RSET_RATE_LIMITS.addColumn("LIMIT", dataset.type.DataType.STRING, null, "");
		/*
		 * //These are defined in the `ENUM definitions` section under `Rate Limiters
		 * (rateLimitType)`. //All limits are optional
		 */

		DataTable OUT_RSET_SYMBOLS = OUT_DS.addTable("OUT_RSET_SYMBOLS");
		OUT_RSET_SYMBOLS.addColumn("BASE_ASSET", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("BASE_ASSET_PRECISION", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("BASE_COMMISSION_PRECISION", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("ICEBERG_ALLOWED", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("IS_MARGIN_TRADING_ALLOWED", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("IS_SPOT_TRADING_ALLOWED", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("OCO_ALLOWED", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("QUOTE_ASSET", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("QUOTE_ASSET_PRECISION", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("QUOTE_COMMISSION_PRECISION", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("QUOTE_ORDER_QTY_MARKET_ALLOWED", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("QUOTE_PRECISION", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("STATUS", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");

		DataTable OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER = OUT_DS.addTable("OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER");
		OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER.addColumn("FILTER_TYPE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER.addColumn("MAX_PRICE", dataset.type.DataType.BIGDECIMAL, null, "");
		OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER.addColumn("MIN_PRICE", dataset.type.DataType.BIGDECIMAL, null, "");
		OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER.addColumn("TICK_SIZE", dataset.type.DataType.BIGDECIMAL, null, "");

		DataTable OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE = OUT_DS.addTable("OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE");
		OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE.addColumn("FILTER_TYPE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE.addColumn("MULTIPLIER_UP", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE.addColumn("MULTIPLIER_DOWN", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE.addColumn("AVG_PRICE_MINS", dataset.type.DataType.STRING, null, "");

		DataTable OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE = OUT_DS.addTable("OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE");
		OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE.addColumn("FILTER_TYPE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE.addColumn("MAX_QTY", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE.addColumn("MIN_QTY", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE.addColumn("STEP_SIZE", dataset.type.DataType.STRING, null, "");

		DataTable OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL = OUT_DS.addTable("OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL");
		OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL.addColumn("FILTER_TYPE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL.addColumn("MIN_NOTIONAL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL.addColumn("APPLY_TO_MARKET", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL.addColumn("AVG_PRICE_MINS", dataset.type.DataType.STRING, null, "");

		DataTable OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS = OUT_DS.addTable("OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS");
		OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS.addColumn("FILTER_TYPE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS.addColumn("LIMIT", dataset.type.DataType.STRING, null, "");

		DataTable OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE = OUT_DS.addTable("OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE");
		OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE.addColumn("FILTER_TYPE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE.addColumn("MAX_QTY", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE.addColumn("MIN_QTY", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE.addColumn("STEP_SIZE", dataset.type.DataType.STRING, null, "");

		DataTable OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS = OUT_DS.addTable("OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS");
		OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS.addColumn("FILTER_TYPE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS.addColumn("MAX_NUM_ORDERS", dataset.type.DataType.BIGDECIMAL, null, "");

		DataTable OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS = OUT_DS.addTable("OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS");
		OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS.addColumn("FILTER_TYPE", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS.addColumn("MAX_NUM_ALGO_ORDERS", dataset.type.DataType.BIGDECIMAL,null, "");

		DataTable OUT_RSET_SYMBOLS_ORDER_TYPES = OUT_DS.addTable("OUT_RSET_SYMBOLS_ORDER_TYPES");
		OUT_RSET_SYMBOLS_ORDER_TYPES.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_ORDER_TYPES.addColumn("ORDER_TYPE", dataset.type.DataType.STRING, null, "");

		DataTable OUT_RSET_SYMBOLS_PERMISSIONS = OUT_DS.addTable("OUT_RSET_SYMBOLS_PERMISSIONS");
		OUT_RSET_SYMBOLS_PERMISSIONS.addColumn("SYMBOL", dataset.type.DataType.STRING, null, "");
		OUT_RSET_SYMBOLS_PERMISSIONS.addColumn("PERMISSION", dataset.type.DataType.STRING, null, "");

		DataTable IN_PSET = InDs.getTable("IN_PSET");
		String QueryString = "";
		if (IN_PSET.getRowCount() == 0) {

		} else if (IN_PSET.getRowCount() == 1) {
			String SYMBOL = IN_PSET.getRow(0).getStringNullToEmpty("SYMBOL");
			if (!PjtUtil.g().isEmpty(SYMBOL)) {
				QueryString = "symbol=" + IN_PSET.getRow(0).getStringNullToEmpty("SYMBOL");
			}
		} else if (IN_PSET.getRowCount() > 1) {
			ArrayList<String> queryElements = new ArrayList<>();
			for (int i = 0; i < IN_PSET.getRowCount(); i++) {
				queryElements.add("\"" + IN_PSET.getRow(i).getStringNullToEmpty("SYMBOL") + "\"");
			}
			QueryString = "symbols="
					+ java.net.URLEncoder.encode("[" + String.join(",", queryElements.toArray(new String[0])) + "]");
		}

		DataRow drRst = OUT_RST.addRow();
		drRst.setString("URL", URL);
		drRst.setString("QUERY_STRING", QueryString);

		System.out.println(URL);
		System.out.println(QueryString);

		String jsonOutString = "";
		HttpUtilBinance httpU = new HttpUtilBinance();
		try {
			jsonOutString = httpU.httpGetBinianceGetUrlNoAuth(URL, QueryString);
		} catch (NoSuchAlgorithmException | URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			// System.out.println(exceptionAsString);
			// 출처: https://blog.miyam.net/81 [낭만 프로그래머]

			drRst.setString("JSON_OUT", "");
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", e.getMessage());
			drRst.setString("ERR_CODE", "100");
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
			return OUT_DS;
		}
		drRst.setString("JSON_OUT", jsonOutString);

		System.out.println(jsonOutString);

		HashMap<String, Object> c = null;
		try {
			c = PjtUtil.g().JsonStringToObject(jsonOutString, HashMap.class);

			if (c != null) {
				DataRow dr = OUT_REST.addRow();
				dr.setString("TIMEZONE", c.get("timezone").toString());
				dr.setBigDecimal("SERVER_TIME", new BigDecimal(Double.parseDouble(c.get("serverTime").toString())));
				dr.setString("EXCHANGE_FILTERS", c.get("exchangeFilters").toString());

				if (c.get("rateLimits") != null) {
					ArrayList<HashMap<String, Object>> al = (ArrayList<HashMap<String, Object>>) c.get("rateLimits");
					for (int i = 0; i < al.size(); i++) {
						HashMap<String, Object> tmp = al.get(i);
						DataRow OUT_RSET_RATE_LIMITS_dr = OUT_RSET_RATE_LIMITS.addRow();
						OUT_RSET_RATE_LIMITS_dr.setString("RATE_LIMIT_TYPE", tmp.get("rateLimitType").toString());
						OUT_RSET_RATE_LIMITS_dr.setString("INTERVAL", tmp.get("interval").toString());
						OUT_RSET_RATE_LIMITS_dr.setInt("INTERVAL_NUM",
								Integer.parseInt(tmp.get("intervalNum").toString()));
						OUT_RSET_RATE_LIMITS_dr.setInt("LIMIT", Integer.parseInt(tmp.get("limit").toString()));
					}
				}

				if (c.get("symbols") != null) {
					ArrayList<HashMap<String, Object>> al = (ArrayList<HashMap<String, Object>>) c.get("symbols");
					for (int i = 0; i < al.size(); i++) {
						HashMap<String, Object> tmp = al.get(i);
						DataRow OUT_RSET_SYMBOLS_dr = OUT_RSET_SYMBOLS.addRow();
						OUT_RSET_SYMBOLS_dr.setString("BASE_ASSET", tmp.get("baseAsset").toString());
						OUT_RSET_SYMBOLS_dr.setString("BASE_COMMISSION_PRECISION",tmp.get("baseCommissionPrecision").toString());
						OUT_RSET_SYMBOLS_dr.setString("BASE_ASSET_PRECISION",tmp.get("baseAssetPrecision").toString());
						OUT_RSET_SYMBOLS_dr.setString("ICEBERG_ALLOWED", tmp.get("icebergAllowed").toString());
						OUT_RSET_SYMBOLS_dr.setString("IS_MARGIN_TRADING_ALLOWED",tmp.get("isMarginTradingAllowed").toString());
						OUT_RSET_SYMBOLS_dr.setString("IS_SPOT_TRADING_ALLOWED",tmp.get("isSpotTradingAllowed").toString());
						OUT_RSET_SYMBOLS_dr.setString("OCO_ALLOWED", tmp.get("ocoAllowed").toString());
						OUT_RSET_SYMBOLS_dr.setString("QUOTE_ASSET", tmp.get("quoteAsset").toString());
						OUT_RSET_SYMBOLS_dr.setString("QUOTE_ASSET_PRECISION",tmp.get("quoteAssetPrecision").toString());
						OUT_RSET_SYMBOLS_dr.setString("QUOTE_COMMISSION_PRECISION",tmp.get("quoteCommissionPrecision").toString());
						OUT_RSET_SYMBOLS_dr.setString("QUOTE_ORDER_QTY_MARKET_ALLOWED",tmp.get("quoteOrderQtyMarketAllowed").toString());
						OUT_RSET_SYMBOLS_dr.setString("QUOTE_PRECISION",tmp.get("quotePrecision").toString());
						OUT_RSET_SYMBOLS_dr.setString("STATUS", tmp.get("status").toString());
						OUT_RSET_SYMBOLS_dr.setString("SYMBOL", tmp.get("symbol").toString());
						if (tmp.get("filters") != null) {
							ArrayList<HashMap<String, Object>> al_filter = (ArrayList<HashMap<String, Object>>) tmp
									.get("filters");
							for (int j = 0; j < al_filter.size(); j++) {
								HashMap<String, Object> tmp_filter = al_filter.get(j);
								if (tmp_filter.get("filterType").equals("PRICE_FILTER")) {
									DataRow OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER_dr = OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER.addRow();
									OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER_dr.setString("SYMBOL",tmp.get("symbol").toString());
									OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER_dr.setString("FILTER_TYPE",tmp_filter.get("filterType").toString());
									OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER_dr.setString("MAX_PRICE",tmp_filter.get("maxPrice").toString());
									OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER_dr.setString("MIN_PRICE",tmp_filter.get("minPrice").toString());
									OUT_RSET_SYMBOLS_FILTERS_PRICE_FILTER_dr.setString("TICK_SIZE",tmp_filter.get("tickSize").toString());
								}
								if (tmp_filter.get("filterType").equals("PERCENT_PRICE")) {
									DataRow OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE_dr = OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE.addRow();
									OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE_dr.setString("SYMBOL",tmp.get("symbol").toString());
									OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE_dr.setString("FILTER_TYPE",tmp_filter.get("filterType").toString());
									OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE_dr.setString("MULTIPLIER_UP",tmp_filter.get("multiplierUp").toString());
									OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE_dr.setString("MULTIPLIER_DOWN",tmp_filter.get("multiplierDown").toString());
									OUT_RSET_SYMBOLS_FILTERS_PERCENT_PRICE_dr.setString("AVG_PRICE_MINS",tmp_filter.get("avgPriceMins").toString());
								}
								if (tmp_filter.get("filterType").equals("LOT_SIZE")) {
									DataRow OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE_dr = OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE
											.addRow();
									OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE_dr.setString("SYMBOL",tmp.get("symbol").toString());
									OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE_dr.setString("FILTER_TYPE",tmp_filter.get("filterType").toString());
									OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE_dr.setString("MAX_QTY",tmp_filter.get("maxQty").toString());
									OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE_dr.setString("MIN_QTY",tmp_filter.get("minQty").toString());
									OUT_RSET_SYMBOLS_FILTERS_LOT_SIZE_dr.setString("STEP_SIZE",tmp_filter.get("stepSize").toString());
								}

								if (tmp_filter.get("filterType").equals("MIN_NOTIONAL")) {
									DataRow OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL_dr = OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL.addRow();
									OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL_dr.setString("SYMBOL",tmp.get("symbol").toString());
									OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL_dr.setString("FILTER_TYPE",tmp_filter.get("filterType").toString());
									OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL_dr.setString("MIN_NOTIONAL",tmp_filter.get("minNotional").toString());
									OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL_dr.setString("APPLY_TO_MARKET",tmp_filter.get("applyToMarket").toString());
									OUT_RSET_SYMBOLS_FILTERS_MIN_NOTIONAL_dr.setString("AVG_PRICE_MINS",tmp_filter.get("avgPriceMins").toString());
								}

								if (tmp_filter.get("filterType").equals("ICEBERG_PARTS")) {
									DataRow OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS_dr = OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS.addRow();
									OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS_dr.setString("SYMBOL",tmp.get("symbol").toString());
									OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS_dr.setString("FILTER_TYPE",tmp_filter.get("filterType").toString());
									OUT_RSET_SYMBOLS_FILTERS_ICEBERG_PARTS_dr.setString("LIMIT",tmp_filter.get("limit").toString());
								}

								if (tmp_filter.get("filterType").equals("MARKET_LOT_SIZE")) {
									DataRow OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE_dr = OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE.addRow();
									OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE_dr.setString("SYMBOL",tmp.get("symbol").toString());
									OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE_dr.setString("FILTER_TYPE",tmp_filter.get("filterType").toString());
									OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE_dr.setString("MAX_QTY", tmp_filter.get("maxQty").toString());
									OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE_dr.setString("MIN_QTY", tmp_filter.get("minQty").toString());
									OUT_RSET_SYMBOLS_FILTERS_MARKET_LOT_SIZE_dr.setString("STEP_SIZE",tmp_filter.get("stepSize").toString());
								}

								if (tmp_filter.get("filterType").equals("MAX_NUM_ORDERS")) {
									DataRow OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS_dr = OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS.addRow();
									OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS_dr.setString("SYMBOL",tmp.get("symbol").toString());
									OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS_dr.setString("FILTER_TYPE",tmp_filter.get("filterType").toString());
									OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ORDERS_dr.setString("MAX_NUM_ORDERS",tmp_filter.get("maxNumOrders").toString());
								}

								if (tmp_filter.get("filterType").equals("MAX_NUM_ALGO_ORDERS")) {
									DataRow OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS_dr = OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS.addRow();
									OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS_dr.setString("SYMBOL",tmp.get("symbol").toString());
									OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS_dr.setString("FILTER_TYPE",tmp_filter.get("filterType").toString());
									OUT_RSET_SYMBOLS_FILTERS_MAX_NUM_ALGO_ORDERS_dr.setString("MAX_NUM_ALGO_ORDERS",tmp_filter.get("maxNumAlgoOrders").toString());
								}
							}
						}
						if (tmp.get("orderTypes") != null) {
							ArrayList<String> al_order_types = (ArrayList<String>) tmp.get("orderTypes");
							for (int j = 0; j < al_order_types.size(); j++) {
								String order_type = al_order_types.get(j);

								DataRow OUT_RSET_SYMBOLS_ORDER_TYPES_dr = OUT_RSET_SYMBOLS_ORDER_TYPES.addRow();
								OUT_RSET_SYMBOLS_ORDER_TYPES_dr.setString("SYMBOL", tmp.get("symbol").toString());
								OUT_RSET_SYMBOLS_ORDER_TYPES_dr.setString("ORDER_TYPE", order_type);
							}
						}

						if (tmp.get("permissions") != null) {
							ArrayList<String> al_permissions = (ArrayList<String>) tmp.get("permissions");
							for (int j = 0; j < al_permissions.size(); j++) {
								String permission = al_permissions.get(j);

								DataRow OUT_RSET_SYMBOLS_PERMISSIONS_dr = OUT_RSET_SYMBOLS_PERMISSIONS.addRow();
								OUT_RSET_SYMBOLS_PERMISSIONS_dr.setString("SYMBOL", tmp.get("symbol").toString());
								OUT_RSET_SYMBOLS_PERMISSIONS_dr.setString("PERMISSION", permission);
							}
						}
					}
				}
			}
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

		return OUT_DS;
	}

}
