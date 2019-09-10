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
				Double price = qty * res.getDouble(DBConstants.LAST_TRADED_PRICE);

				order.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
				order.put(APP_CONSTANT.QUANTITY, qty);
				order.put(APP_CONSTANT.TYPE, res.getString(DBConstants.TRANS_TYPE));
				order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(price));
				if (qty > 0) {
					symbolArr.put(order);

					netPrice += price;
				}
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		double estimatedTax = netPrice * 0.0002;
		ViewPendingOrderResponse resp = new ViewPendingOrderResponse();
		resp.setSymbolArr(symbolArr);
		resp.setNetPrice(netPrice);
		resp.setEstimatedTax(estimatedTax);
		resp.setTotalPrice(netPrice + estimatedTax);
		return resp;
	}

	public static ViewPendingOrderResponse viewRebalanceOrder(ViewPendingOrderRequest viewReq) throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;

		Map<String, JSONObject> oldVersionMap = new HashMap<String, JSONObject>();
		Map<String, JSONObject> newVersionMap = new HashMap<String, JSONObject>();

		double rebalancePrice = 0.0;
		try {
			String basketName = viewReq.getBasketName();
			JSONArray orderNo = viewReq.getOrderNoArr();
			String query = QueryConstants.VIEW_REBALANCE_ORDER_QUERY + "(" + appendParams(orderNo)
					+ ") order by bc.IS_LATEST";

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketName);
			int param = 2;
			for (int i = 0; i < orderNo.length(); i++) {
				pstmt.setString(param++, orderNo.getString(i));
			}
			log.debug(pstmt);

			res = pstmt.executeQuery();

			while (res.next()) {
				JSONObject row = new JSONObject();
				row.put(APP_CONSTANT.SYMBOL, res.getString(DBConstants.SYMBOL));
				row.put(APP_CONSTANT.QUANTITY, res.getInt(DBConstants.QTY));
				row.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
				row.put(APP_CONSTANT.PRICE, res.getString(DBConstants.LAST_TRADED_PRICE));
				row.put(APP_CONSTANT.BASKET_VERSION, res.getString(DBConstants.BASKET_VERSION));
				row.put(APP_CONSTANT.IS_LATEST, res.getBoolean(DBConstants.IS_LATEST));

				String symbol = res.getString(DBConstants.SYMBOL);
				if (!res.getBoolean(DBConstants.IS_LATEST)) {
					if (oldVersionMap.containsKey(symbol)) {
						oldVersionMap.get(symbol).put(APP_CONSTANT.QUANTITY,
								(Integer) oldVersionMap.get(symbol).get(APP_CONSTANT.QUANTITY)
										+ res.getInt(DBConstants.QTY));
					} else
						oldVersionMap.put(symbol, row);
				} else {
					if (newVersionMap.containsKey(symbol)) {
						newVersionMap.get(symbol).put(APP_CONSTANT.QUANTITY,
								(Integer) newVersionMap.get(symbol).get(APP_CONSTANT.QUANTITY)
										+ res.getInt(DBConstants.QTY));
					} else
						newVersionMap.put(symbol, row);

				}

			}
		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		String latestBasketVersion = "";
		String oldBasketVersion = "";

		for (JSONObject row : oldVersionMap.values()) {
			oldBasketVersion = row.getString(APP_CONSTANT.BASKET_VERSION);
			break;
		}

		for (JSONObject row : newVersionMap.values()) {
			latestBasketVersion = row.getString(APP_CONSTANT.BASKET_VERSION);
			break;
		}

		log.debug("latestBasketVersion---------->" + latestBasketVersion);
		log.debug("oldBasketVersion------------->" + oldBasketVersion);
		log.debug("oldVersionMap----------->" + oldVersionMap.values());
		log.debug("newVersionMap----------->" + newVersionMap.values());

		Map<String, JSONObject> versionDiffMap = RebalanceOrderOperations.getVersionDiffMap(oldVersionMap,
				newVersionMap);

		for (JSONObject symbol : versionDiffMap.values()) {
			int qty = symbol.getInt(APP_CONSTANT.QUANTITY);
			if (qty < 0) {
				symbol.put(APP_CONSTANT.TYPE, APP_CONSTANT.SELL);
				symbol.put(APP_CONSTANT.QUANTITY, -qty);
			} else
				symbol.put(APP_CONSTANT.TYPE, APP_CONSTANT.BUY);

			double price = qty * symbol.getDouble(APP_CONSTANT.PRICE);
			symbol.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(Math.abs(price)));

			log.debug("Price------->" + price);

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

	static String appendParams(JSONArray json) {
		String s = "";
		for (int i = 0; i < json.length(); i++) {
			s += "?,";
		}
		return s.substring(0, s.length() - 1);
	}
}
