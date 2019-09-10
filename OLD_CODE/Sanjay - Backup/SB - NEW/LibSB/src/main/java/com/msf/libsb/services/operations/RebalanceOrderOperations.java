package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.libsb.services.pendingorders.RebalanceOrderRequest;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.libsb.utils.helper.Session;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class RebalanceOrderOperations {
	private static Logger log = Logger.getLogger(RebalanceOrderOperations.class);

	public static PlaceBasketOrderResponse rebalanceBasketOrder(RebalanceOrderRequest rebalanceReq) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet res = null;

		List<JSONObject> oldVersionList = new ArrayList<JSONObject>();
		List<JSONObject> newVersionList = new ArrayList<JSONObject>();

		double rebalancePrice = 0.0;
		String query = QueryConstants.REBALANCE_QUERY;
		String basketName = rebalanceReq.getBasketName();
		int basketQty = 0;
		String orderNo = null;
		try {
			orderNo = rebalanceReq.getOrderNo();

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			st = conn.prepareStatement(query);
			st.setString(1, basketName);
			st.setString(2, orderNo);

			log.debug(st);
			res = st.executeQuery();

			if (res.next()) {
				basketQty = res.getInt("BASKET_QTY");
				do {
					JSONObject row = new JSONObject();
					row.put(APP_CONSTANT.BASKET_VERSION, res.getString(DBConstants.BASKET_VERSION));
					row.put(APP_CONSTANT.SYMBOL, res.getString(DBConstants.SYMBOL));
					row.put(APP_CONSTANT.QUANTITY, res.getInt(DBConstants.QTY));
					row.put(APP_CONSTANT.TRADING_SYMBOL, res.getString(DBConstants.TRADING_SYMBOL));
					row.put(APP_CONSTANT.EXCH, res.getString(DBConstants.EXCHANGE));
					row.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
					row.put(APP_CONSTANT.PRICE, res.getString(DBConstants.LAST_TRADED_PRICE));

					if (res.getBoolean(DBConstants.IS_LATEST))
						newVersionList.add(row);

					else
						oldVersionList.add(row);

				} while (res.next());
			}

		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(st);
			Helper.closeConnection(conn);
		}
		String latestBasketVersion = newVersionList.get(0).getString(APP_CONSTANT.BASKET_VERSION);

		Map<String, JSONObject> versionDiffMap = getVersionDiffMap(oldVersionList, newVersionList);

		for (JSONObject symbol : versionDiffMap.values()) {
			int qty = symbol.getInt(APP_CONSTANT.QUANTITY);
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
		String userID = session.getUserId();
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			newBasketOrderNo = PlaceOrderOperations.generateUniqueOrderNo(conn, userID, APP_CONSTANT.BUY);
			PlaceOrderOperations.insertOrder(conn, newBasketOrderNo, userID, basketName, latestBasketVersion, basketQty,
					"REBALANCE", orderNo);
			PlaceOrderOperations.insertOrderCons(conn, orderMap, newBasketOrderNo);
		} finally {
			Helper.closeConnection(conn);
		}

		String[] status = PlaceOrderOperations.pollStatus(orderMap);
		log.debug("Status of Rebalance :: " + status[0]);
		st = null;
		if (status[0].equalsIgnoreCase("Success")) {
			try {
				query = QueryConstants.MODIFY_REBALANCE_PARENT_ORDER;
				conn = DBConnection.getInstance().getStockBasketDBConnection();
				st = conn.prepareStatement(query);
				st.setString(1, orderNo);
				st.executeUpdate();

			} finally {
				Helper.closeStatement(st);
				Helper.closeConnection(conn);
			}
		}
		else if (status[0].equalsIgnoreCase("Incomplete")) {
			try {
				query = QueryConstants.MODIFY_REBALANCE_PARENT_INCOMPLETE_ORDER;
				conn = DBConnection.getInstance().getStockBasketDBConnection();
				st = conn.prepareStatement(query);
				st.setString(1, orderNo);
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
		orderRes.setEstimatedTax(SamcoHelper.getIndianCurrencyFormat(netPrice.get("estimatedTax")));// 0.02% tax

		orderRes.setNetPrice(SamcoHelper.getIndianCurrencyFormat(basketQty * rebalancePrice));

		log.debug("net price==>" + netPrice);
		log.debug("rebalance price==>" + basketQty * rebalancePrice);
		return orderRes;
	}

	static Map<String, JSONObject> getVersionDiffMap(List<JSONObject> oldVersionList, List<JSONObject> newVersionList)
			throws Exception {
		Map<String, JSONObject> versionDiffMap = new HashMap<String, JSONObject>();
		for (JSONObject row : oldVersionList) {
			String symbol = row.getString(APP_CONSTANT.SYMBOL);
			int oldVersionQty = row.getInt(APP_CONSTANT.QUANTITY);

			versionDiffMap.put(symbol, row.put(APP_CONSTANT.QUANTITY, -oldVersionQty));
		}

		for (JSONObject row : newVersionList) {
			String symbol = row.getString(APP_CONSTANT.SYMBOL);
			JSONObject oldVersionSymbolObj = versionDiffMap.get(symbol);

			if (oldVersionSymbolObj == null) {
				versionDiffMap.put(symbol, row);
				continue;
			}
			int newVersionQty = row.getInt(APP_CONSTANT.QUANTITY);
			int oldVersionQty = oldVersionSymbolObj.getInt(APP_CONSTANT.QUANTITY);
			int qty = oldVersionQty + newVersionQty;

			if (qty == 0)
				versionDiffMap.remove(symbol);
			else
				versionDiffMap.put(symbol, oldVersionSymbolObj.put(APP_CONSTANT.QUANTITY, qty));
		}
		return versionDiffMap;
	}
}
