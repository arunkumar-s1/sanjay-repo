package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.Map.Entry;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.libsb.utils.helper.Session;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class ExitBasketOperations {
	private static Logger log = Logger.getLogger(ExitBasketOperations.class);

	public static PlaceBasketOrderResponse exitOrder(Session session, JSONArray orders, int basketQty)
			throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		PreparedStatement pQuantityStmt = null;
		Connection conn = null;
		ArrayList<JSONObject> stockList = new ArrayList<JSONObject>();
		String basketOrderNo = orders.getJSONObject(0).getString(APP_CONSTANT.ORDER_NO);
		String transactionType = APP_CONSTANT.SELL;
		String iconURL = "", basketName = "";
		String basketVersion = "";
		try {
			String query = QueryConstants.EXIT_BASKET_QUERY;

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketOrderNo);
			log.debug(pstmt);

			res = pstmt.executeQuery();
			if (res.next()) {
				basketName = res.getString(DBConstants.BASKET_NAME);
				basketVersion = res.getString(DBConstants.BASKET_VERSION);
				Map<String, Integer> symbolMap = new HashMap<>();
				ResultSet qtyRes = null;
				try
				{
					String sQuantityQuery = "SELECT * from BASKET_CONSTITUENTS WHERE BASKET_NAME = ? AND BASKET_VERSION = ?";
					pQuantityStmt = conn.prepareStatement(sQuantityQuery);
					pQuantityStmt.setString(1, basketName);
					pQuantityStmt.setString(2, basketVersion);
					log.debug(pQuantityStmt);
					qtyRes = pQuantityStmt.executeQuery();
					while(qtyRes.next()) {
						symbolMap.put(qtyRes.getString("SYMBOL"), qtyRes.getInt("QTY"));
					}
				} finally {
					Helper.closeResultSet(qtyRes);
					Helper.closeStatement(pQuantityStmt);
				}
				
				do {
					String stockTransType = res.getString(DBConstants.TRANS_TYPE);
					if (stockTransType.equals(APP_CONSTANT.SELL)) {
						continue;
					}

					JSONObject stock = new JSONObject();
					stock.put(APP_CONSTANT.TRADING_SYMBOL, res.getString(DBConstants.TRADING_SYMBOL));
					stock.put(APP_CONSTANT.SYMBOL, res.getString(DBConstants.SYMBOL));
					log.debug("Qty to be sent in the request :: " + symbolMap.get(res.getString(DBConstants.SYMBOL)));
					stock.put(APP_CONSTANT.QUANTITY, symbolMap.get(res.getString(DBConstants.SYMBOL)));
					stock.put(APP_CONSTANT.TYPE, stockTransType);
					stock.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
					stock.put(APP_CONSTANT.PRICE, res.getDouble(DBConstants.LAST_TRADED_PRICE));

					stockList.add(stock);

				} while (res.next());
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		String userID = session.getUserId();
		HashMap<String, JSONObject> orderMap = new HashMap<String, JSONObject>();
		HashMap<String, Double> netPrice = PlaceOrderOperations.placeOrder(orderMap, session, stockList, basketQty,
				transactionType, true);
		try {
			
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			basketOrderNo = PlaceOrderOperations.generateUniqueOrderNo(conn, userID, transactionType);
			PlaceOrderOperations.insertOrder(conn, basketOrderNo, userID, basketName, basketVersion, basketQty,
					transactionType, null);
			PlaceOrderOperations.insertOrderCons(conn, orderMap, basketOrderNo);
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
			dNetPrice += (price * nQty);
			
		}

		int filledQty = Integer.valueOf(status[2]);
		

		/*for (Entry<String, JSONObject> entry : orderMap.entrySet()) {
			JSONObject order = entry.getValue();
			Double price = order.getDouble(APP_CONSTANT.PRICE);
			int nQty = order.getInt(APP_CONSTANT.QUANTITY);
			dNetPrice += (price * nQty);
		}*/
		
		int orderCount = orderMap.size();
		PlaceBasketOrderResponse orderRes = new PlaceBasketOrderResponse();
		orderRes.setBasketName(basketName);
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
		orderRes.setFilledQty(filledQty);
		orderRes.setStocks(new JSONArray(new Vector<JSONObject>(orderMap.values())));
		
		fetchBuyOrderList(userID, basketName, basketVersion, basketQty);
		return orderRes;
	}

	private static void fetchBuyOrderList(String sUserID, String sBasketName, String sBasketVersion, int nInputBasketQty) throws Exception {
		String sQuery = "SELECT ORDER_NO, BASKET_NAME, BASKET_VERSION, STATUS, REMAINING_QTY as QTY FROM BASKET_ORDERS WHERE ORDER_TYPE = ? AND STATUS = 'complete' AND USER_ID = ? AND BASKET_NAME = ? AND BASKET_VERSION = ? AND IS_SOLD = '0' ORDER BY CREATED_AT ASC";
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		Queue<JSONObject> jQueue = new LinkedList<>();
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(sQuery);
			pstmt.setString(1, "BUY");
			pstmt.setString(2, sUserID);
			pstmt.setString(3, sBasketName);
			pstmt.setString(4, sBasketVersion);
			log.debug(pstmt);

			res = pstmt.executeQuery();
			while(res.next()) {
				log.debug("---> Basket Order No :: " + res.getString("ORDER_NO") + "::" + res.getString("QTY"));
				JSONObject tempObject = new JSONObject();
				tempObject.put(APP_CONSTANT.BASKET_ORDER_NO, res.getString("ORDER_NO"));
				tempObject.put(APP_CONSTANT.BASKET_QTY, res.getString("QTY"));
				tempObject.put(APP_CONSTANT.STATUS, res.getString("STATUS"));
				jQueue.add(tempObject);
			}
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			
			log.debug("----> Queue contents :: " + jQueue);
			log.debug("----> Queue size :: " + jQueue.size());
			log.debug("----> Queue peek :: " + jQueue.peek());
			Iterator iter = jQueue.iterator();
			log.debug("First basket qty :: " + nInputBasketQty);
			while(iter.hasNext()) {
				JSONObject temp = (JSONObject) iter.next();
				int nHoldingQty = Integer.parseInt(temp.getString(APP_CONSTANT.BASKET_QTY));
				log.debug("---> Holdings qty :: " + nHoldingQty);
				int nSellQty = nInputBasketQty;
				log.debug("---> Sell qty :: " + nSellQty);
				log.debug("----> Input basket qty :: " + nInputBasketQty);
				if(nHoldingQty > nInputBasketQty) {
					nHoldingQty = nHoldingQty - nInputBasketQty;
					nInputBasketQty = nInputBasketQty - nSellQty;
					if(nHoldingQty > nSellQty) {
						log.debug("Partially sold :: " + nHoldingQty);
						sQuery = "UPDATE BASKET_ORDERS SET IS_SOLD = ?, REMAINING_QTY = ? WHERE ORDER_NO = ?";
						pstmt = conn.prepareStatement(sQuery);
						pstmt.setInt(1, 0);
						pstmt.setInt(2, nHoldingQty);
						pstmt.setString(3, temp.getString(APP_CONSTANT.BASKET_ORDER_NO));
						log.debug(pstmt);
						pstmt.executeUpdate();
						Helper.closeResultSet(res);
						Helper.closeStatement(pstmt);
						break;
					} else if(nHoldingQty < nSellQty){
						log.debug("Partially sold :: " + nHoldingQty);
						sQuery = "UPDATE BASKET_ORDERS SET IS_SOLD = ?, REMAINING_QTY = ? WHERE ORDER_NO = ?";
						pstmt = conn.prepareStatement(sQuery);
						pstmt.setInt(1, 0);
						pstmt.setInt(2, nHoldingQty);
						pstmt.setString(3, temp.getString(APP_CONSTANT.BASKET_ORDER_NO));
						log.debug(pstmt);
						pstmt.executeUpdate();
						Helper.closeResultSet(res);
						Helper.closeStatement(pstmt);
						
						break;
					} else if(nInputBasketQty == 0) {
						sQuery = "UPDATE BASKET_ORDERS SET IS_SOLD = ?, REMAINING_QTY = ? WHERE ORDER_NO = ?";
						pstmt = conn.prepareStatement(sQuery);
						pstmt.setInt(1, 0);
						pstmt.setInt(2, nHoldingQty);
						pstmt.setString(3, temp.getString(APP_CONSTANT.BASKET_ORDER_NO));
						log.debug(pstmt);
						pstmt.executeUpdate();
						Helper.closeResultSet(res);
						Helper.closeStatement(pstmt);
						log.debug("---> Break case");
						break;
					}
					
				} else if(nHoldingQty <= nInputBasketQty) {
					nInputBasketQty = nInputBasketQty - nHoldingQty;
					sQuery = "UPDATE BASKET_ORDERS SET IS_SOLD = ?, REMAINING_QTY = ? WHERE ORDER_NO = ?";
					pstmt = conn.prepareStatement(sQuery);
					pstmt.setInt(1, 1);
					pstmt.setInt(2, 0);
					pstmt.setString(3, temp.getString(APP_CONSTANT.BASKET_ORDER_NO));
					log.debug(pstmt);
					pstmt.executeUpdate();
					Helper.closeResultSet(res);
					Helper.closeStatement(pstmt);
					
					log.debug("Completely sold :: mark 1");
					if(nInputBasketQty == 0) {
						break;
					}
				}
				log.debug("Rem Qty :: " + nInputBasketQty); 
			}
			
		}
		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
	}
	
	private static void updateBuyOrders(List<JSONObject> orderList, int basketQty) throws Exception {
		int qty = 0;
		for (JSONObject order : orderList) {
			qty += order.getInt(APP_CONSTANT.QUANTITY);
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		JSONObject topOrder = orderList.get(0);
		int extraQty = qty - basketQty;
		if (extraQty > 0) {
			orderList.remove(0);
		}
		String query = "update BASKET_ORDERS set IS_SOLD=1 where ORDER_NO in (" + join(orderList) + ")";
		conn = DBConnection.getInstance().getStockBasketDBConnection();

		try {
			pstmt = conn.prepareStatement(query);
			log.debug(pstmt);
			pstmt.executeUpdate();
			Helper.closeStatement(pstmt);
			if (extraQty > 0) {
				query = "update BASKET_ORDERS set QTY=? where ORDER_NO =?";
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, extraQty);
				pstmt.setString(2, topOrder.getString(APP_CONSTANT.ORDER_NO));
				log.debug(pstmt);
				pstmt.executeUpdate();
			}
		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
	}

	private static String join(List<JSONObject> orderList) throws Exception {
		StringBuilder ordersSb = new StringBuilder();
		log.debug("----> Orders :: " + orderList.toString());
		for (JSONObject order : orderList) {
			ordersSb.append("'" + order.getString(APP_CONSTANT.ORDER_NO) + "',");
		}
		ordersSb.setLength(ordersSb.length() - 1);
		return ordersSb.toString();
	}

	private static void sort(JSONArray orders) throws Exception {
		int len = orders.length();
		for (int i = 0; i < len; i++) {
			JSONObject order = orders.getJSONObject(i);
			int qty = order.getInt(APP_CONSTANT.QUANTITY);
			int j = i - 1;
			while (j >= 0 && qty < orders.getJSONObject(j).getInt(APP_CONSTANT.QUANTITY)) {
				orders.put(j + 1, orders.getJSONObject(j));
				j--;
			}
			orders.put(j + 1, order);
		}
	}

	private static List<JSONObject> chooseBuyOrdersToSell(int sellQty, JSONArray orders) throws Exception {
		List<JSONObject> mainList = new ArrayList<JSONObject>();
		int maxLen = orders.length() + 1;
		for (int j = orders.length() - 1; j >= 0; j--) {
			List<JSONObject> orderList = new ArrayList<JSONObject>();
			int sum = 0;
			for (int i = j; i >= 0; i--) {
				JSONObject currentOrder = orders.getJSONObject(i);
				int currentQty = currentOrder.getInt(APP_CONSTANT.QUANTITY);
				if (sum + currentQty > sellQty)
					continue;
				sum += currentQty;
				orderList.add(currentOrder);
				if (sum == sellQty) {
					int currentLen = orderList.size();
					if (currentLen == 1)
						return orderList;
					if (currentLen < maxLen) {
						mainList = new ArrayList<JSONObject>(orderList);
						maxLen = currentLen;
					}
					orderList.clear();
				}
			}
		}

		if (mainList.size() == 0)
			return chooseBuyOrdersToSell(sellQty + 1, orders);
		return mainList;
	}
}
