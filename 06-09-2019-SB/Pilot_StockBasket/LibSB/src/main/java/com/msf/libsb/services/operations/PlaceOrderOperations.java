package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.order.PlaceAMOOrderResponse;
import com.msf.libsb.services.order.PlaceBasketOrderRequest;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.libsb.services.order.PlaceOrderRequest;
import com.msf.libsb.services.order.PlaceOrderResponse;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.libsb.utils.helper.Session;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class PlaceOrderOperations {
	private static Logger log = Logger.getLogger(PlaceOrderOperations.class);

	public static PlaceBasketOrderResponse placeBasketOrder(PlaceBasketOrderRequest orderReq) throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		String basketOrderNo;
		HashMap<String, JSONObject> orderMap = new HashMap<String, JSONObject>();
		String iconURL = "";
		ArrayList<JSONObject> rows = new ArrayList<JSONObject>();
		String basketName = orderReq.getBasketName();
		String basketVersion = "";
		try {
//	String[] queries=BasketOperations.generateQuery(basketName);
//	String query="select bd.ICON_URL,bd.BASKET_VERSION,bc.QTY,"+queries[0].replace("LAST_PRICE", "SYMBOL")+" as SYMBOL,"
//	+queries[0].replace("LAST_PRICE", "TRADING_SYMBOL")+" as TRADING_SYMBOL,"
//	+queries[0].replace("LAST_PRICE", "DESCRIPTION")+" as DESCRIPTION,"
//	+queries[0] +" as LAST_PRICE"
//	+" from BASKET_DETAILS bd left join BASKET_CONSTITUENTS bc on bd.BASKET_NAME=bc.BASKET_NAME and bd.BASKET_VERSION=bc.BASKET_VERSION "
//	+queries[1]+" where bd.BASKET_NAME=? and bd.IS_LATEST=1";

			String query = QueryConstants.PLACE_BASKET_ORDER_QUERY;

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketName);
			log.debug(pstmt);

			res = pstmt.executeQuery();
			if (res.next()) {
				basketVersion = res.getString(DBConstants.BASKET_VERSION);
				iconURL = res.getString(DBConstants.ICON_URL);
				do {
					JSONObject row = new JSONObject();
					row.put(APP_CONSTANT.TRADING_SYMBOL, res.getString(DBConstants.TRADING_SYMBOL));
					row.put(APP_CONSTANT.SYMBOL, res.getString(DBConstants.SYMBOL));
					row.put(APP_CONSTANT.QUANTITY, res.getInt(DBConstants.QTY));
					row.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
					row.put(APP_CONSTANT.PRICE, res.getDouble(DBConstants.LAST_TRADED_PRICE));

					rows.add(row);

				} while (res.next());
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		int basketQty = orderReq.getQty();

		String transactionType = orderReq.getTransactionType();
		Session session = orderReq.getSession();
		String userID = session.getUserId();

//boolean etimatedFlag = true;

		HashMap<String, Double> netPrice = placeOrder(orderMap, session, rows, basketQty, transactionType, true);

		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			basketOrderNo = generateUniqueOrderNo(conn, userID, transactionType);
			insertOrder(conn, basketOrderNo, userID, basketName, basketVersion, basketQty, transactionType, null);
			log.debug("=== after insert order");
			insertOrderCons(conn, orderMap, basketOrderNo);
		} finally {
			Helper.closeConnection(conn);
		}

		String[] status = pollStatus(orderMap);
		if ((status[0].equalsIgnoreCase("Failed")) || status[0].equalsIgnoreCase("Incomplete")) {
			status = pollStatus(orderMap);
		}

		Double dNetPrice = 0.0;
		Double dNetBrokerage = 0.0;
		Double dNetExpenses = 0.0;
		
		for (Entry<String, JSONObject> entry : orderMap.entrySet()) {
			JSONObject order = entry.getValue();
			Double price = order.getDouble(APP_CONSTANT.PRICE);
			int nQty = order.getInt("filledQty");
			dNetPrice += (price * nQty);
			
		}

		int orderCount = orderMap.size();
		PlaceBasketOrderResponse orderRes = new PlaceBasketOrderResponse();
		orderRes.setIconURL(iconURL);
		log.debug("Status[0] :: " + status[0]);
		orderRes.setOrderStatus("Order " + status[0]);
		orderRes.setMessage(status[1]);
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
		orderRes.setBasketOrderNo(basketOrderNo);
		orderRes.setTransactionType(transactionType);
		orderRes.setOriginalQty(orderCount);
		orderRes.setFilledQty(Integer.valueOf(status[2]));
		orderRes.setStocks(new JSONArray(new Vector<JSONObject>(orderMap.values())));
		return orderRes;
	}

	static HashMap<String, Double> placeOrder(HashMap<String, JSONObject> orderMap, Session session,
			ArrayList<JSONObject> stocks, int basketQty, String transactionType, boolean estimatedFlag)
			throws Exception {
		double netPrice = 0.0;
		List<Double> list = new ArrayList<>();
		double dEstimatedBrokerage = 0.00;
		double dEstimatedExpenses = 0.0;
		double netBrokerage = 0.0;
		double netexpenses = 0.0;

		HashMap<String, Double> tax = new HashMap<>();

		String place_order_url = AppConfig.getConfigValue("place_order_url");
		PlaceOrderRequest placeRequest = new PlaceOrderRequest(place_order_url);
		Double result = 0.0;
		String userID = session.getUserId();
		placeRequest.setProductAlias(session.getProductAlias());
		placeRequest.setUserID(userID);
		placeRequest.setAccountID(session.getAccountID());
		placeRequest.setSession(session.getJsession());
		placeRequest.setKey(session.getJkey());

		String nestTransactionType = transactionType.equals(APP_CONSTANT.BUY) ? APIConstants.BUY : APIConstants.SELL;
		placeRequest.setTransactionType(nestTransactionType);
		placeRequest.setRetention("IOC");
		placeRequest.setPriceType(APIConstants.MARKET_ORDER);
		placeRequest.setDisclosedQty("0");
		placeRequest.setMarketProtection("10");
		placeRequest.setTriggerPrice("0");
		placeRequest.setProductCode("MTF");
		placeRequest.setDateDays("NA");
		placeRequest.setAMO(isAMO() ? "YES" : "NO");
		placeRequest.setMinQty("0");
		placeRequest.setPrice("0");
		placeRequest.setPositionSquare("N");
		placeRequest.setOrderSource("NEST_REST_SB");

		for (JSONObject stock : stocks) {
			String tradingSymbol = stock.getString(APP_CONSTANT.TRADING_SYMBOL);
			String symbol = stock.getString(APP_CONSTANT.SYMBOL);
			String[] symbolArr = symbol.split("_");
			String exchange = symbolArr[1];
			String tokenNo = symbolArr[0];
			int qty = basketQty * stock.getInt(APP_CONSTANT.QUANTITY);

			placeRequest.setTradingSymbol(tradingSymbol);
			placeRequest.setExchange(exchange);
			placeRequest.setQuantity(String.valueOf(qty));
			placeRequest.setScripToken(tokenNo);

			PlaceOrderResponse pResponse = (PlaceOrderResponse) placeRequest.postRequest();
			log.debug("is success? " + pResponse.isSuccessResponse());
			if (!pResponse.isSuccessResponse()) {
				throw new SamcoException(pResponse.getErrorID());
			} else {
				double price = qty * stock.getDouble(APP_CONSTANT.PRICE);
				stock.put(APP_CONSTANT.PRICE, price);
				stock.put(APP_CONSTANT.QUANTITY, qty);
				stock.put(APP_CONSTANT.TYPE, transactionType);
				stock.put(APP_CONSTANT.PRODUCT_CODE, "MTF");
				if (estimatedFlag)
					list.add(price);

				orderMap.put(pResponse.getOrderNo(), stock);

				netPrice += price;
			}
		}

		if (estimatedFlag) {
			for (double price : list) {
				dEstimatedBrokerage = (price * 0.002); // individual brokerage of a stock

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

	public static boolean isAMO() throws Exception {
		String amoTimeStr = AppConfig.getConfigValue("amo_time");
		LocalTime amoTime = LocalTime.parse(amoTimeStr);
		LocalTime now = LocalTime.now();
		if (now.isAfter(amoTime)) {
			return true;
		}
		return false;
	}

	static String[] pollStatus(HashMap<String, JSONObject> orderMap) throws Exception {
		Thread.sleep(8000);

		Connection conn = null;
		ResultSet res = null;
		PreparedStatement pstmt = null;
		int completeOrderCount = 0;
		int openOrderCount = 0;
		int cancelledOrderCount = 0;
		int totalOrderCount = orderMap.size();
		String basketOrderNo = "";
		double result = 0.0;
		try {
			String query = "select BASKET_ORDER_NO,NEST_ORDER_NO,NEST_ORDER_STATUS, AVG_PRICE, ORIGINAL_QTY, FILLED_QTY from BASKET_ORDER_CONSTITUENTS"
					+ " where NEST_ORDER_NO in (" + getNestOrderNos(orderMap.keySet()) + ")";
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			log.debug(pstmt);
			res = pstmt.executeQuery();

			while (res.next()) {
				basketOrderNo = res.getString(DBConstants.BASKET_ORDER_NO);
				String nestOrderNo = res.getString(DBConstants.NEST_ORDER_NO);
				log.debug("-----> Nest Order no :: " + nestOrderNo + "::" + res.getInt(DBConstants.ORIGINAL_QTY) + "::"
						+ res.getInt(DBConstants.FILLED_QTY));
				String nestOrderStatus = res.getString(DBConstants.NEST_ORDER_STATUS);
				log.debug("----> Nest Order status :: " + nestOrderStatus);
				JSONObject order = orderMap.get(nestOrderNo);
				order.put(APP_CONSTANT.NEST_ORDER_STATUS, nestOrderStatus);
				order.put(APP_CONSTANT.PRICE, res.getString("AVG_PRICE"));
				order.put("filledQty", res.getInt("FILLED_QTY"));
				order.put("originalQty", res.getInt("ORIGINAL_QTY"));
				orderMap.put(nestOrderNo, order);
				if (nestOrderStatus.contains("open")) {
					openOrderCount++;
				}
				if (nestOrderStatus.equals("complete")) {
					completeOrderCount++;
				}
				if (nestOrderStatus.equals("cancelled")) {
					cancelledOrderCount++;
				}
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		String[] status = new String[3];
		String s = totalOrderCount > 1 ? "s" : "";
		log.debug("---> complete order count :: " + completeOrderCount);
		log.debug("---> Total order count :: " + totalOrderCount);
		log.debug("---> Cancelled order count :: " + cancelledOrderCount);
		log.debug("---> Open order count :: " + openOrderCount);
		if (completeOrderCount == totalOrderCount) {
			status[0] = "Success";
			status[1] = totalOrderCount + " order" + s + " have been placed successfully";
		} else if (openOrderCount == totalOrderCount) {
			status[0] = "Pending";
			status[1] = totalOrderCount + " order" + (s.length() > 0 ? "s are" : " is") + " pending";
		}

		else if (completeOrderCount == 0) {

			double req1 = 0;
			double aval1 = 0;

			String query = "select REJECT_REASON from BASKET_ORDER_CONSTITUENTS where BASKET_ORDER_NO = ?";

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketOrderNo);
			log.debug(pstmt);

			res = pstmt.executeQuery();

			status[0] = "Failed";
			boolean reasonFlag = false;

			while (res.next()) {

				String msg = res.getString("REJECT_REASON");
				if (msg != null) {
					String pattern = "RMS:Margin Exceeds.*across exchange across segment across product.*";
					if (pattern.matches(msg)) {

						reasonFlag = true;

						List<Double> list1 = new ArrayList<>();
						List<Double> list2 = new ArrayList<>();

						String amt = msg.substring(msg.indexOf("Required"), msg.indexOf("for"));

						String r = amt.substring(amt.indexOf(":") + 1, amt.indexOf(","));
						double req = Double.parseDouble(r);
						list1.add(req);

						String a = amt.substring(amt.lastIndexOf(":") + 1);
						double aval = Double.parseDouble(a);
						list2.add(aval);

						for (int i = 0; i < list1.size(); i++) {
							req1 += list1.get(i);
						}

						for (int j = 0; j < list1.size(); j++) {
							aval1 += list2.get(j);
						}

					}

//				
				}

			}

			if (!reasonFlag)
				status[1] = totalOrderCount + " order" + s + " have been rejected due to an unexpected error";
			else {
				result = (req1 - aval1);
				log.debug(result);

				status[1] = totalOrderCount + " order" + s
						+ " have been rejected due to lack of funds. You need to add" + result
						+ "to complete your order";
			}

			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);

		} else {
			status[0] = "Incomplete";
			status[1] = totalOrderCount - completeOrderCount + " out of " + totalOrderCount + " order" + s
					+ " have not been placed due to an unexpected error";
		}
		status[2] = String.valueOf(completeOrderCount);
		return status;
	}

	private static String getNestOrderNos(Set<String> set) throws JSONException {
		StringBuilder sb = new StringBuilder();
		for (String nestOrderNo : set) {
			sb.append("'" + nestOrderNo + "',");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	static String generateUniqueOrderNo(Connection conn, String userID, String transactionType) throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		String current = formatter.format(new Date());
		String newOrderNo = current + userID + transactionType.charAt(0);
		String query = QueryConstants.SELECT_ORDER_NO_BASKET_ORDERS;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userID);
			log.debug(pstmt);
			res = pstmt.executeQuery();
			if (res.next()) {
				Matcher m = Pattern.compile("[BS](\\d+)$").matcher(res.getString("order_no"));
				if (m.find()) {
					log.debug(newOrderNo + (Integer.parseInt(m.group(1)) + 1));
					return newOrderNo + (Integer.parseInt(m.group(1)) + 1);
				}
			}
		} catch (SQLException e) {
			throw e;
		}

		catch (Exception e) {
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
		}
		return newOrderNo + 1;
	}

	static void insertOrder(Connection conn, String basketOrderNo, String userID, String basketName, String version,
			int basketQty, String transactionType, String sParentOrderNo) throws Exception {
		PreparedStatement pstmt = null;
		String query = QueryConstants.INSERT_BASKET_ORDERS;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketOrderNo);
			pstmt.setString(2, userID);
			pstmt.setString(3, basketName);
			pstmt.setString(4, version);
			pstmt.setInt(5, basketQty);
			pstmt.setInt(6, basketQty);
			pstmt.setString(7, transactionType);
			if (transactionType.equalsIgnoreCase("REBALANCE")) {
				pstmt.setString(8, sParentOrderNo);
			} else {
				pstmt.setString(8, null);
			}
			log.debug(pstmt);
			pstmt.executeUpdate();
		} finally {
			Helper.closeStatement(pstmt);
		}
	}

	static void insertOrderCons(Connection conn, HashMap<String, JSONObject> orderMap, String basketOrderNo)
			throws Exception {
		PreparedStatement pstmt = null;
		StringBuilder querySb = new StringBuilder(QueryConstants.ORDER_CONSTITUENTS_INSERT_QUERY);
		try {

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
		}
	}

	public static PlaceAMOOrderResponse placeAMOOrder(PlaceBasketOrderRequest orderReq) throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		String basketName = orderReq.getBasketName();
		Session session = orderReq.getSession();
		String userID = session.getUserId();
		int basketQty = orderReq.getQty();
		String query = QueryConstants.AMO_ORDER_QUERY;
		StringBuilder amoInsertQuery = new StringBuilder(QueryConstants.AMO_ORDER_INSERT_QUERY);
		PlaceAMOOrderResponse amoOrderRes = new PlaceAMOOrderResponse();
		String basketOrderNo = null;
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			basketOrderNo = generateUniqueOrderNo(conn, userID, APP_CONSTANT.BUY);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketName);
			log.debug(pstmt);
			res = pstmt.executeQuery();

			while (res.next()) {
				String symbol = res.getString(DBConstants.SYMBOL);
				String symbolArr[] = symbol.split("_");
				String scrip = symbolArr[0];
				String exch = symbolArr[1];
				amoInsertQuery.append("('" + userID + "','" + basketOrderNo + "','" + symbol + "','"
						+ res.getString(DBConstants.TRADING_SYMBOL) + "','" + scrip + "','" + exch + "',"
						+ basketQty * res.getInt(DBConstants.QTY) + "),");
			}
			SamcoHelper.cleanPreparedStatmentAndResultSet(pstmt, res);
			amoInsertQuery.setLength(amoInsertQuery.length() - 1);
			pstmt = conn.prepareStatement(amoInsertQuery.toString());
			log.debug(pstmt);
			int affectedRowCount = pstmt.executeUpdate();
			if (affectedRowCount > 0) {
				amoOrderRes.setMessage("Orders will be placed Tomorrow");
			} else {
			}
		} finally {
			if (res != null)
				Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		return amoOrderRes;
	}

}