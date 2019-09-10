package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.libsb.services.order.PlaceOrderRequest;
import com.msf.libsb.services.order.PlaceOrderResponse;
import com.msf.libsb.services.pendingorders.FixAllOrderResponse;
import com.msf.libsb.services.pendingorders.FixOrderRequest;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.libsb.utils.helper.Session;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class FixOrderOperations {
	private static Logger log = Logger.getLogger(FixOrderOperations.class);

	public static FixAllOrderResponse fixAllOrder(FixOrderRequest fixOrderRequest) throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;

		Map<String, JSONObject> orderMap = new HashMap<String, JSONObject>();
		try {
			String userId = fixOrderRequest.getSession().getUserId();
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(QueryConstants.FIX_ALL_ORDER_QUERY);

			pstmt.setString(1, userId);
			log.debug(pstmt);

			res = pstmt.executeQuery();

			while (res.next()) {
				String orderNo = res.getString(DBConstants.ORDER_NO);
				JSONObject orderDetails = orderMap.get(orderNo);

				if (orderDetails == null) {
					orderDetails = new JSONObject();
					orderDetails.put(APP_CONSTANT.BASKET_ORDER_NO, orderNo);
					// orderDetails.put(APP_CONSTANT.ORDER_TYPE,
					// res.getString(DBConstants.ORDER_TYPE));
					orderDetails.put(APP_CONSTANT.STOCKS, new ArrayList<JSONObject>());

					orderMap.put(orderNo, orderDetails);
				}

				JSONObject stock = new JSONObject();
				stock.put(APP_CONSTANT.QUANTITY, res.getInt(DBConstants.QTY));
				stock.put(APP_CONSTANT.SYMBOL, res.getString(DBConstants.SYMBOL));
				stock.put(APP_CONSTANT.TRADING_SYMBOL, res.getString(DBConstants.TRADING_SYMBOL));
				stock.put("oldOrderNo", res.getString(DBConstants.NEST_ORDER_NO));
				stock.put(APP_CONSTANT.TYPE, res.getString(DBConstants.TRANS_TYPE));
				stock.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
				stock.put(APP_CONSTANT.PRICE, res.getDouble(DBConstants.LAST_TRADED_PRICE));

				((List<JSONObject>) orderDetails.get(APP_CONSTANT.STOCKS)).add(stock);
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		Session session = fixOrderRequest.getSession();
		int completedOrderCount = 0;
		for (Entry<String, JSONObject> orderEntry : orderMap.entrySet()) {
			HashMap<String, JSONObject> currentOrderMap = new HashMap<String, JSONObject>();
			String basketOrderNo = orderEntry.getKey();
			JSONObject orderDetails = orderEntry.getValue();
			List<JSONObject> orderList = (List<JSONObject>) orderDetails.get(APP_CONSTANT.STOCKS);
			placeOrder(currentOrderMap, session, orderList, 1, false);

			try {
				conn = DBConnection.getInstance().getStockBasketDBConnection();
				modifyOrderCons(conn, currentOrderMap, basketOrderNo);
			} finally {
				Helper.closeConnection(conn);
			}

			String[] status = PlaceOrderOperations.pollStatus(currentOrderMap);
			if (status[0].equals("Success")) {
				completedOrderCount++;
			}
		}

		FixAllOrderResponse resp = new FixAllOrderResponse();
		resp.setCompletedOrderCount(completedOrderCount);
		resp.setTotalOrderCount(orderMap.size());
		return resp;
	}

	public static PlaceBasketOrderResponse fixBasketOrder(FixOrderRequest fixOrderRequest) throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ArrayList<JSONObject> stockList = new ArrayList<JSONObject>();
		String basketOrderNo = fixOrderRequest.getOrderNo();
		String transactionType, nestTransactionType;
		String iconURL = "", basketName = "";

		try {
			String query = QueryConstants.FIX_ORDER_QUERY;

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketOrderNo);
			log.debug(pstmt);

			res = pstmt.executeQuery();
			if (res.next()) {
				// iconURL=res.getString(DBConstants.ICON_URL);
				basketName = res.getString(DBConstants.BASKET_NAME);
				do {
					JSONObject stock = new JSONObject();
					stock.put(APP_CONSTANT.TRADING_SYMBOL, res.getString(DBConstants.TRADING_SYMBOL));
					stock.put(APP_CONSTANT.SYMBOL, res.getString(DBConstants.SYMBOL));
					stock.put(APP_CONSTANT.QUANTITY, res.getInt(DBConstants.QTY));
					stock.put("oldOrderNo", res.getString(DBConstants.NEST_ORDER_NO));
					stock.put(APP_CONSTANT.TYPE, res.getString(DBConstants.TRANS_TYPE));

					stock.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
					log.debug("----> Price fill :: " + res.getDouble(DBConstants.LAST_TRADED_PRICE));
					stock.put(APP_CONSTANT.PRICE, res.getDouble(DBConstants.LAST_TRADED_PRICE));
					if(res.getInt(DBConstants.QTY) > 0) {
						stockList.add(stock);
					}

				} while (res.next());
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		Session session = fixOrderRequest.getSession();
		HashMap<String, JSONObject> orderMap = new HashMap<String, JSONObject>();
		HashMap<String, Double> netPrice = placeOrder(orderMap, session, stockList, 1, true);

		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			insertOrderCons(conn, orderMap, basketOrderNo);
		} finally {
			Helper.closeConnection(conn);
		}

		String[] status = PlaceOrderOperations.pollStatus(orderMap);
		Double dNetPrice = 0.0;
		Double dNetBrokerage = 0.0;
		Double dNetExpenses = 0.0;
		
		for (Entry<String, JSONObject> entry : orderMap.entrySet()) {
			JSONObject order = entry.getValue();
			Double price = order.getDouble(APP_CONSTANT.PRICE);
			int nQty = order.getInt("filledQty");
			log.debug("---> Price :: " + price + " :: " + nQty);
			dNetPrice += (price * nQty);
			
		}

		log.debug("-------Status :: " + status[0]);
		if(status[0].equalsIgnoreCase("Success")) {
			try {
				conn = DBConnection.getInstance().getStockBasketDBConnection();				
				modifyOrderCons(conn, orderMap, basketOrderNo);
			} finally {
				Helper.closeConnection(conn);
			}

		} 
		
		int orderCount = orderMap.size();
		PlaceBasketOrderResponse orderRes = new PlaceBasketOrderResponse();
		orderRes.setBasketName(basketName);
		orderRes.setOrderStatus("Order " + status[0]);
		orderRes.setMessage(status[1]);
		orderRes.setBasketOrderNo(basketOrderNo);
		orderRes.setTransactionType("FIX");
		orderRes.setOriginalQty(orderCount);
		orderRes.setFilledQty(Integer.valueOf(status[2]));
		orderRes.setStocks(new JSONArray(new Vector<JSONObject>(orderMap.values())));

		//orderRes.setNetPrice(SamcoHelper.getIndianCurrencyFormat(netPrice.get("netPrice")));
		orderRes.setNetPrice(SamcoHelper.getIndianCurrencyFormat(dNetPrice));
		if (status[0].equalsIgnoreCase("Failed")) {
			Double dEstimatedBrokerage = (dNetPrice * 0.002); // individual brokerage of a stock

			Double dEstimatedExpenses = (dNetPrice * 0.0015); // indv expense

			dNetBrokerage += dEstimatedBrokerage;
			dNetExpenses += dEstimatedExpenses;
		
			if (dNetBrokerage >= 20) {
				dNetBrokerage = 20.0;
			}
			Double dEstimatedTax = dNetBrokerage + dNetExpenses;
			orderRes.setEstimatedTax(SamcoHelper.getIndianCurrencyFormat(dEstimatedTax));
		} else {
			orderRes.setEstimatedTax(SamcoHelper.getIndianCurrencyFormat(netPrice.get("estimatedTax")));// 0.02% tax
		}
		return orderRes;
	}

	static HashMap<String, Double> placeOrder(HashMap<String, JSONObject> orderMap, Session session,
			Collection<JSONObject> stocks, int basketQty, boolean estimatedFlag) throws Exception {
		double netPrice = 0.0;

		HashMap<String, Double> tax = new HashMap<>();

		List<Double> list = new ArrayList<>();
		double dEstimatedBrokerage = 0.00;
		double dEstimatedExpenses = 0.0;
		double netBrokerage = 0.0;
		double netexpenses = 0.0;
		String place_order_url = AppConfig.getConfigValue("place_order_url");
		PlaceOrderRequest placeRequest = new PlaceOrderRequest(place_order_url);

		String userID = session.getUserId();
		placeRequest.setProductAlias(session.getProductAlias());
		placeRequest.setUserID(userID);
		placeRequest.setAccountID(session.getAccountID());
		placeRequest.setSession(session.getJsession());
		placeRequest.setKey(session.getJkey());

		placeRequest.setRetention("IOC");
		placeRequest.setPriceType(APIConstants.MARKET_ORDER);
		placeRequest.setDisclosedQty("0");
		placeRequest.setMarketProtection("10");
		placeRequest.setTriggerPrice("0");
		placeRequest.setProductCode("SB");
		placeRequest.setDateDays("NA");
		placeRequest.setAMO("NO");
		placeRequest.setMinQty("0");
		placeRequest.setPrice("0");
		placeRequest.setPositionSquare("N");

		for (JSONObject stock : stocks) {
			String tradingSymbol = stock.getString(APP_CONSTANT.TRADING_SYMBOL);
			String symbol = stock.getString(APP_CONSTANT.SYMBOL);
			log.debug("symbol==>" + symbol);
			String[] symbolArr = symbol.split("_");
			String exchange = symbolArr[1];
			String tokenNo = symbolArr[0];
			int qty = basketQty * stock.getInt(APP_CONSTANT.QUANTITY);
			String type = stock.getString(APP_CONSTANT.TYPE);
			String nestTransactionType = type.equals(APP_CONSTANT.BUY) ? APIConstants.BUY : APIConstants.SELL;

			placeRequest.setTradingSymbol(tradingSymbol);
			placeRequest.setExchange(exchange);
			placeRequest.setQuantity(String.valueOf(qty));
			placeRequest.setScripToken(tokenNo);
			placeRequest.setTransactionType(nestTransactionType);

			PlaceOrderResponse pResponse = (PlaceOrderResponse) placeRequest.postRequest();
			log.debug("is success? " + pResponse.isSuccessResponse());
			if (!pResponse.isSuccessResponse()) {
				throw new SamcoException(pResponse.getErrorID());
			} else {
				double price = qty * stock.getDouble(APP_CONSTANT.PRICE);
				log.debug("----> Price :: " + price);
				stock.put(APP_CONSTANT.PRICE, price);
				log.debug("----> qty in order map :: " + qty);
				stock.put(APP_CONSTANT.QUANTITY, qty);
				stock.put(APP_CONSTANT.PRODUCT_CODE, "SB");

				orderMap.put(pResponse.getOrderNo(), stock);

				if (estimatedFlag)
					list.add(price);

				netPrice += price;
			}
		}

		if (estimatedFlag) {
			for (double price : list) {
				dEstimatedBrokerage = (price * 0.002); // individual brokarage of a stock

				dEstimatedExpenses = (price * 0.0015); // indv expense

				netBrokerage += dEstimatedBrokerage;
				netexpenses += dEstimatedExpenses;
			}

			if (netBrokerage >= 20) {
				netBrokerage = 20.0;
			}
			tax.put("estimatedTax", netBrokerage + netexpenses);
		}
		tax.put("netPrice", netPrice);

		return tax;
	}

	static void insertOrderCons(Connection conn, HashMap<String, JSONObject> orderMap, String basketOrderNo)
			throws Exception {
		PreparedStatement pstmt = null;
		String querySb = QueryConstants.FIX_ORDER_CONSTITUENTS_INSERT_QUERY;
		
		try {
			pstmt = conn.prepareStatement(querySb);
			for (Entry<String, JSONObject> entry : orderMap.entrySet()) {
				JSONObject order = entry.getValue();
				log.debug("Order :: " + order.getInt(APP_CONSTANT.QUANTITY));
				pstmt.setString(1, entry.getKey());
				pstmt.setString(2, basketOrderNo);
				pstmt.setString(3, order.getString(APP_CONSTANT.SYMBOL));
				pstmt.setString(4, "BUY");
				pstmt.setString(5, "SB");
				pstmt.setString(6, Integer.toString(order.getInt(APP_CONSTANT.QUANTITY)));
				pstmt.setString(7, Integer.toString(order.getInt(APP_CONSTANT.QUANTITY)));
				//pstmt.setString(8, SamcoHelper.getIndianCurrencyFormat(order.getDouble(APP_CONSTANT.PRICE)));
				pstmt.setString(8, order.getString("oldOrderNo"));
				Double price = order.getDouble(APP_CONSTANT.PRICE);
				log.debug("----> PS log :: " + pstmt);
				pstmt.addBatch();
				order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(price));
			}
			log.debug(pstmt);
			pstmt.executeBatch();
		} finally {
			Helper.closeStatement(pstmt);
		}
		/*try {

			for (Entry<String, JSONObject> entry : orderMap.entrySet()) {
				JSONObject order = entry.getValue();
				Double price = order.getDouble(APP_CONSTANT.PRICE);
				querySb.append("('" + entry.getKey() + "','" + basketOrderNo + "','"
						+ order.getString(APP_CONSTANT.SYMBOL) + "','" + order.getString(APP_CONSTANT.TYPE) + "','"
						+ order.getString(APP_CONSTANT.PRODUCT_CODE) + "'," + order.getInt(APP_CONSTANT.QUANTITY)
						+ ",0," + price + "),");
				order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(price));
			}

			querySb.setLength(querySb.length() - 1);
			pstmt = conn.prepareStatement(querySb.toString());
			log.debug(pstmt);
			pstmt.execute();
		} finally {
			Helper.closeStatement(pstmt);
		}*/
	}

	private static void modifyOrderCons(Connection conn, HashMap<String, JSONObject> orderMap, String basketOrderNo)
			throws Exception {
		PreparedStatement pstmt = null;
		String query = QueryConstants.MODIFY_FIX_ORDER;
		try {
			pstmt = conn.prepareStatement(query);
			for (Entry<String, JSONObject> entry : orderMap.entrySet()) {
				JSONObject order = entry.getValue();
				pstmt.setString(1, "complete");
				pstmt.setString(2, order.getString("oldOrderNo"));
				log.debug("----> PS log :: " + pstmt);
				pstmt.addBatch();
				//order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(price));
			}
			log.debug(pstmt);
			pstmt.executeBatch();
		} finally {
			Helper.closeStatement(pstmt);
		}
		query = QueryConstants.MODIFY_BASKET_ORDER;
		try {
			pstmt = conn.prepareStatement(query);
			for (Entry<String, JSONObject> entry : orderMap.entrySet()) {
				JSONObject order = entry.getValue();
				pstmt.setString(1, "complete");
				pstmt.setString(2, basketOrderNo);
				log.debug("----> PS log :: " + pstmt);
				pstmt.addBatch();
				//order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(price));
			}
			log.debug(pstmt);
			pstmt.executeBatch();
		} finally {
			Helper.closeStatement(pstmt);
		}
	}
	
	private static void deleteOrderCons(Connection conn, HashMap<String, JSONObject> orderMap, String basketOrderNo)
			throws Exception {
		PreparedStatement pstmt = null;
		String query = QueryConstants.DELETE_ORDER_CONS;
		try {
			pstmt = conn.prepareStatement(query);
			for (Entry<String, JSONObject> entry : orderMap.entrySet()) {
				pstmt.setString(1, entry.getKey());
				log.debug("----> PS log :: " + pstmt);
				pstmt.addBatch();
				//order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(price));
			}
			log.debug(pstmt);
			pstmt.executeBatch();
		} finally {
			Helper.closeStatement(pstmt);
		}
	}
}
