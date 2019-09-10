package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.msf.libsb.services.pendingorders.RebalanceOrderRequest;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.libsb.utils.helper.Session;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class RebalanceOrderOperations {
	private static Logger log = Logger.getLogger(RebalanceOrderOperations.class);

	@SuppressWarnings("resource")
	public static PlaceBasketOrderResponse rebalanceBasketOrder(RebalanceOrderRequest rebalanceReq) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet res = null;

		Map<String, JSONObject> oldVersionMap = new HashMap<String, JSONObject>();
		Map<String, JSONObject> newVersionMap = new HashMap<String, JSONObject>();

		double rebalancePrice = 0.0;
		JSONArray orderNo = rebalanceReq.getOrderArr();
		String query = QueryConstants.REBALANCE_QUERY + "(" + ViewPendingOrderOperations.appendParams(orderNo) + ")";
		String query_qty = QueryConstants.BASKET_QTYS + "(" + ViewPendingOrderOperations.appendParams(orderNo) + ")";
		String basketName = rebalanceReq.getBasketName();
		int basketQty = 0;
		try {

			conn = DBConnection.getInstance().getStockBasketDBConnection();

			st = conn.prepareStatement(query_qty);
			int param = 1;

			for (int i = 0; i < orderNo.length(); i++) {
				st.setString(param++, orderNo.getString(i));
			}
			log.debug(st);

			res = st.executeQuery();

			while (res.next()) {
				basketQty += res.getInt(DBConstants.QTY);
			}

			st = conn.prepareStatement(query);
			st.setString(1, basketName);
			param = 2;
			for (int i = 0; i < orderNo.length(); i++) {
				st.setString(param++, orderNo.getString(i));
			}
			log.debug(st);
			res = st.executeQuery();

			if (res.next()) {
				do {
					JSONObject row = new JSONObject();
					row.put(APP_CONSTANT.BASKET_VERSION, res.getString(DBConstants.BASKET_VERSION));
					row.put(APP_CONSTANT.SYMBOL, res.getString(DBConstants.SYMBOL));
					row.put(APP_CONSTANT.QUANTITY, res.getInt(DBConstants.QTY));
					row.put(APP_CONSTANT.TRADING_SYMBOL, res.getString(DBConstants.TRADING_SYMBOL));
					row.put(APP_CONSTANT.EXCH, res.getString(DBConstants.EXCHANGE));
					row.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
					row.put(APP_CONSTANT.PRICE, res.getString(DBConstants.LAST_TRADED_PRICE));

					String symbol = res.getString(DBConstants.SYMBOL);
					if (!res.getBoolean(DBConstants.IS_LATEST)) {
						if (!oldVersionMap.containsKey(symbol)) {
							// oldVersionMap.get(symbol).put(APP_CONSTANT.QUANTITY,
							// (Integer)
							// oldVersionMap.get(symbol).get(APP_CONSTANT.QUANTITY)
							// + res.getInt(DBConstants.QTY));
							int qty = basketQty * (Integer) row.getInt(APP_CONSTANT.QUANTITY);
							row.put(APP_CONSTANT.QUANTITY, qty);
							oldVersionMap.put(symbol, row);
						} else {
							// oldVersionMap.put(symbol, row);
							continue;
						}
					} else {
						if (newVersionMap.containsKey(symbol)) {
							// newVersionMap.get(symbol).put(APP_CONSTANT.QUANTITY,
							// (Integer)
							// newVersionMap.get(symbol).get(APP_CONSTANT.QUANTITY)
							// + res.getInt(DBConstants.QTY));
							int qty = basketQty * (Integer) row.getInt(APP_CONSTANT.QUANTITY);
							row.put(APP_CONSTANT.QUANTITY, qty);
							newVersionMap.put(symbol, row);
						} else {
							// newVersionMap.put(symbol, row);
							continue;
						}

					}

				} while (res.next());
			}

		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(st);
			Helper.closeConnection(conn);
		}

		String latestBasketVersion = "";

		for (JSONObject row : newVersionMap.values()) {
			latestBasketVersion = row.getString(APP_CONSTANT.BASKET_VERSION);
			break;
		}

		Map<String, JSONObject> versionDiffMap = getVersionDiffMap(oldVersionMap, newVersionMap);

		for (JSONObject symbol : versionDiffMap.values()) {
			int qty = symbol.getInt(APP_CONSTANT.QUANTITY);
			log.debug("qty------>" + qty);
			if (qty < 0) {
				symbol.put(APP_CONSTANT.TYPE, APP_CONSTANT.SELL);
				symbol.put(APP_CONSTANT.QUANTITY, -qty);
			} else
				symbol.put(APP_CONSTANT.TYPE, APP_CONSTANT.BUY);

			rebalancePrice += qty * symbol.getDouble(APP_CONSTANT.PRICE);
		}

		Session session = rebalanceReq.getSession();
		HashMap<String, JSONObject> orderMap = new HashMap<String, JSONObject>();
		boolean etimatedFlag = true;
		HashMap<String, Double> netPrice = FixOrderOperations.placeOrder(orderMap, session, versionDiffMap.values(),
				basketQty, etimatedFlag);

		String newBasketOrderNo;
		String parentOrderNo = "";
		log.debug("orderNo--------->" + orderNo);
		for (int i = 0; i < orderNo.length(); i++) {
			parentOrderNo += orderNo.getString(i) + ",";
		}
		parentOrderNo = parentOrderNo.substring(0, parentOrderNo.length() - 1);

		log.debug("parentOrderNo--------->" + parentOrderNo);

		String userID = session.getUserId();
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			newBasketOrderNo = PlaceOrderOperations.generateUniqueOrderNo(conn, userID, APP_CONSTANT.BUY);
			PlaceOrderOperations.insertOrder(conn, newBasketOrderNo, userID, basketName, latestBasketVersion, basketQty,
					"REBALANCE", parentOrderNo);
			PlaceOrderOperations.insertOrderCons(conn, orderMap, newBasketOrderNo);
		} finally {
			Helper.closeConnection(conn);
		}

		String[] status = PlaceOrderOperations.pollStatus(orderMap);
		log.debug("Status of Rebalance :: " + status[0]);
		st = null;
		if (status[0].equalsIgnoreCase("Success")) {
			try {
				query = QueryConstants.MODIFY_REBALANCE_PARENT_ORDER + "("
						+ ViewPendingOrderOperations.appendParams(orderNo) + ")";
				conn = DBConnection.getInstance().getStockBasketDBConnection();
				st = conn.prepareStatement(query);
				int index = 1;
				for (int i = 0; i < orderNo.length(); i++) {
					st.setString(index++, orderNo.getString(i));
				}
				st.executeUpdate();

			} finally {
				Helper.closeStatement(st);
				Helper.closeConnection(conn);
			}
		} else {
			try {
				query = QueryConstants.MODIFY_REBALANCE_PARENT_INCOMPLETE_ORDER + "("
						+ ViewPendingOrderOperations.appendParams(orderNo) + ")";
				conn = DBConnection.getInstance().getStockBasketDBConnection();
				st = conn.prepareStatement(query);
				int index = 1;
				for (int i = 0; i < orderNo.length(); i++) {
					st.setString(index++, orderNo.getString(i));
				}
				st.executeUpdate();

			} finally {
				Helper.closeStatement(st);
				Helper.closeConnection(conn);
			}
		}

		int orderCount = orderMap.size();
		PlaceBasketOrderResponse orderRes = new PlaceBasketOrderResponse();
		orderRes.setOrderStatus("Order " + status[0]);
		orderRes.setMessage(status[1]);
		orderRes.setBasketOrderNo(newBasketOrderNo);
		orderRes.setTransactionType("REBALANCE");
		orderRes.setOriginalQty(orderCount);
		orderRes.setFilledQty(Integer.valueOf(status[2]));
		orderRes.setStocks(new JSONArray(new Vector<JSONObject>(orderMap.values())));

		// orderRes.setIconURL(iconURL);
		orderRes.setNetPrice(SamcoHelper.getIndianCurrencyFormat(netPrice.get("netPrice")));
		orderRes.setEstimatedTax(SamcoHelper.getIndianCurrencyFormat(netPrice.get("estimatedTax")));// 0.02%
																									// tax

		orderRes.setNetPrice(SamcoHelper.getIndianCurrencyFormat(basketQty * rebalancePrice));

		log.debug("net price==>" + netPrice);
		log.debug("rebalance price==>" + basketQty * rebalancePrice);
		return orderRes;
	}

	static Map<String, JSONObject> getVersionDiffMap(Map<String, JSONObject> oldVersionMap,
			Map<String, JSONObject> newVersionMap) throws Exception {
		Map<String, JSONObject> versionDiffMap = new HashMap<String, JSONObject>();
		for (JSONObject row : oldVersionMap.values()) {
			String symbol = row.getString(APP_CONSTANT.SYMBOL);
			int qty = row.getInt(APP_CONSTANT.QUANTITY);

			versionDiffMap.put(symbol, row.put(APP_CONSTANT.QUANTITY, -qty));
		}
		log.debug("versionDiffMap---------> " + versionDiffMap.values());

		for (JSONObject row : newVersionMap.values()) {
			String symbol = row.getString(APP_CONSTANT.SYMBOL);
			JSONObject oldVersionSymbolObj = versionDiffMap.get(symbol);

			if (oldVersionSymbolObj == null) {
				versionDiffMap.put(symbol, row);
				continue;
			}
			int newVersionQty = row.getInt(APP_CONSTANT.QUANTITY);
			int oldVersionQty = oldVersionSymbolObj.getInt(APP_CONSTANT.QUANTITY);
			log.debug("newVersionQty-------> " + newVersionQty);
			log.debug("oldVersionQty-------> " + oldVersionQty);
			int qty = oldVersionQty + newVersionQty;

			if (qty == 0)
				versionDiffMap.remove(symbol);
			else
				versionDiffMap.put(symbol, oldVersionSymbolObj.put(APP_CONSTANT.QUANTITY, qty));
		}
		log.debug("versionDiffMap---------> " + versionDiffMap.values());
		return versionDiffMap;
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
				dEstimatedBrokerage = (price * 0.002); // individual brokarage
														// of a stock

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

}
