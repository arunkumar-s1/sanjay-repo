package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.pendingorders.ViewPendingOrderRequest;
import com.msf.libsb.services.pendingorders.ViewPendingOrderResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class ViewPendingOrderOperations {
	private static Logger log = Logger.getLogger(ViewPendingOrderOperations.class);

	public static ViewPendingOrderResponse viewBrokenOrder(ViewPendingOrderRequest viewReq) throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		Double netPrice = 0.0;
		JSONArray symbolArr = new JSONArray();
		String sTransactionType = null;
		double dEstimatedBrokerage = 0.00;
		double dEstimatedExpenses = 0.0;
		double dMultiples = 0.0;
		double totalExp = 0.0;
		double netBrokerage = 0.0;
		double netexpenses = 0.0;
		double minAmount = 0.0;
		double minNetBrokerage = 0.0;
		double minNetexpenses = 0.0;
		try {
			String orderNo = viewReq.getOrderNo();
			String query = QueryConstants.VIEW_FIX_ORDER_QUERY;

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, orderNo);
			log.debug(pstmt);

			res = pstmt.executeQuery();

			while (res.next()) {
				JSONObject order = new JSONObject();
				int qty = res.getInt(DBConstants.QTY);
				//Double price = qty * res.getDouble(DBConstants.LAST_TRADED_PRICE);
				Double price = res.getDouble(DBConstants.LAST_TRADED_PRICE);

				order.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
				order.put(APP_CONSTANT.QUANTITY, qty);
				sTransactionType = res.getString(DBConstants.TRANS_TYPE);
				order.put(APP_CONSTANT.TYPE, res.getString(DBConstants.TRANS_TYPE));
				order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(price));
				order.put(APP_CONSTANT.ORIGINAL_QTY, res.getInt(DBConstants.ORIGINAL_QTY));
				dMultiples = qty * price; // Q*P

				dEstimatedBrokerage = (dMultiples * 0.002); // individual brokarage of a stock
				if(dEstimatedBrokerage >= 20) {
					dEstimatedBrokerage = 20.0;
				}

				dEstimatedExpenses = (dMultiples * 0.0015); // indv expense
				
				double minPrice = res.getInt(DBConstants.QTY) * price;
				
				double minEstimatedBrokerage = minPrice * 0.002;
				if(minEstimatedBrokerage >= 20) {
					minEstimatedBrokerage = 20.0;
				}
				double minEstimatedExpenses = minPrice * 0.0015;

				if (qty > 0) {
					symbolArr.put(order);
					//netPrice += (price * qty);
					netPrice += dMultiples;
					netBrokerage += dEstimatedBrokerage;
					log.debug("Estimated Brokerage :: " + dEstimatedBrokerage);
					log.debug("Net brokerage :: " + netBrokerage);
					netexpenses += dEstimatedExpenses;
					log.debug("Estimated expenses :: " + dEstimatedExpenses);
					log.debug("Net Expenses :: " + netexpenses);
					
					minAmount += minPrice;
					minNetBrokerage += minEstimatedBrokerage;
					minNetexpenses += minEstimatedExpenses;


				}
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		double estimatedTax = netBrokerage + netexpenses;
		//double estimatedTax = netPrice * 0.0002;
		ViewPendingOrderResponse resp = new ViewPendingOrderResponse();
		resp.setSymbolArr(symbolArr);
		resp.setNetPrice(netPrice);
		resp.setEstimatedTax(estimatedTax);
		if(sTransactionType.equalsIgnoreCase("BUY")) {
			resp.setTotalPrice(netPrice + estimatedTax);
		}
		else {
			resp.setTotalPrice(netPrice - estimatedTax);
		}
		return resp;
	}

	public static ViewPendingOrderResponse viewRebalanceOrder(ViewPendingOrderRequest viewReq) throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;

		List<JSONObject> oldVersionList = new ArrayList<JSONObject>();
		List<JSONObject> newVersionList = new ArrayList<JSONObject>();

		double rebalancePrice = 0.0;
		try {
			String basketName = viewReq.getBasketName();
			String orderNo = viewReq.getOrderNo();
			String query = QueryConstants.VIEW_REBALANCE_ORDER_QUERY;

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketName);
			pstmt.setString(2, orderNo);
			log.debug(pstmt);

			res = pstmt.executeQuery();

			while (res.next()) {
				JSONObject row = new JSONObject();
				row.put(APP_CONSTANT.SYMBOL, res.getString(DBConstants.SYMBOL));
				row.put(APP_CONSTANT.QUANTITY, res.getInt(DBConstants.QTY));
				row.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
				row.put(APP_CONSTANT.PRICE, res.getString(DBConstants.LAST_TRADED_PRICE));
				row.put(APP_CONSTANT.BASKET_VERSION, res.getString(DBConstants.BASKET_VERSION));

				if (res.getBoolean(DBConstants.IS_LATEST))
					newVersionList.add(row);

				else
					oldVersionList.add(row);
			}
		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		String latestBasketVersion = newVersionList.get(0).getString(APP_CONSTANT.BASKET_VERSION);
		String oldBasketVersion = oldVersionList.get(0).getString(APP_CONSTANT.BASKET_VERSION);

		Map<String, JSONObject> versionDiffMap = RebalanceOrderOperations.getVersionDiffMap(oldVersionList,
				newVersionList);

		for (JSONObject symbol : versionDiffMap.values()) {
			int qty = symbol.getInt(APP_CONSTANT.QUANTITY);
			if (qty < 0) {
				symbol.put(APP_CONSTANT.TYPE, APP_CONSTANT.SELL);
				symbol.put(APP_CONSTANT.QUANTITY, -qty);
			} else
				symbol.put(APP_CONSTANT.TYPE, APP_CONSTANT.BUY);

			double price = qty * symbol.getDouble(APP_CONSTANT.PRICE);
			symbol.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(Math.abs(price)));

			rebalancePrice += price;
		}

		double estimatedTax = Math.abs(rebalancePrice) * 0.0002;

		ViewPendingOrderResponse resp = new ViewPendingOrderResponse();
		resp.setSymbolArr(new JSONArray(new Vector<JSONObject>(versionDiffMap.values())));
		resp.setNetPrice(rebalancePrice);
		resp.setEstimatedTax(estimatedTax);
		resp.setTotalPrice(rebalancePrice + estimatedTax);
		resp.setPendingVersions(getPendingVersionsArr(oldBasketVersion, latestBasketVersion));
		return resp;
	}

	private static JSONArray getPendingVersionsArr(String oldBasketVersion, String latestBasketVersion)
			throws JSONException {
		JSONArray pendingVersionsArr = new JSONArray();
		int oldVersion = Integer.parseInt(oldBasketVersion.replace(".", "")) + 1;
		int latestVersion = Integer.parseInt(latestBasketVersion.replace(".", ""));
		while (oldVersion <= latestVersion) {
			pendingVersionsArr.put(getVersion(oldVersion));
			oldVersion++;
		}
		log.debug("---->pendingversion array :: " + pendingVersionsArr.toString());
		JSONArray latestPendingVersionArray = new JSONArray();
		log.debug("---> Pending version array length :: " + pendingVersionsArr.length() + " :: "
				+ pendingVersionsArr.getString(pendingVersionsArr.length() - 1));
		latestPendingVersionArray.put(pendingVersionsArr.getString(pendingVersionsArr.length() - 1));
		log.debug("---->pendingversion array after assign :: " + latestPendingVersionArray.toString());
		return latestPendingVersionArray;
	}

	private static String getVersion(int version) {
		int patch = version % 10;
		version /= 10;
		int minor = version % 10;
		version /= 10;

		return version + "." + minor + "." + patch;
	}
}
