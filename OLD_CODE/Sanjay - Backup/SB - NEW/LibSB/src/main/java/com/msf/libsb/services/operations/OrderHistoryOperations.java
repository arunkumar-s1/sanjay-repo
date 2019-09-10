package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.order.OrderHistoryDetailsRequest;
import com.msf.libsb.services.order.OrderHistoryDetailsResponse;
import com.msf.libsb.services.order.OrderHistoryResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class OrderHistoryOperations {

	private static Logger log = Logger.getLogger(OrderHistoryOperations.class);

	public static OrderHistoryResponse orderHistory(String userId) throws JSONException, SQLException {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		String query = QueryConstants.ORDER_HISTORY_QUERY;
		JSONArray basketArr = new JSONArray();

		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pst = conn.prepareStatement(query);

			pst.setString(1, userId);
			res = pst.executeQuery();

			while (res.next()) {
				JSONObject basket = new JSONObject();
				basket.put(APP_CONSTANT.BASKET_NAME, res.getString(DBConstants.BASKET_NAME));
				basket.put(APP_CONSTANT.IMAGE_URL, res.getString(DBConstants.IMAGE_URL));
				basketArr.put(basket);
			}

		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pst);
			Helper.closeConnection(conn);
		}
		OrderHistoryResponse historyResp = new OrderHistoryResponse();
		historyResp.setBasketNames(basketArr);
		return historyResp;
	}

	public static OrderHistoryDetailsResponse orderHistoryDetails(OrderHistoryDetailsRequest orderHisDetReq)
			throws SQLException, JSONException {
		String userId = orderHisDetReq.getUserId();
		String basketName = orderHisDetReq.getBasketName();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		String query = QueryConstants.ORDER_HISTORY_DETAILS_QUERY;

		Map<String, JSONObject> orderMap = new LinkedHashMap<String, JSONObject>();

		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pst = conn.prepareStatement(query);
			pst.setString(1, userId);
			pst.setString(2, basketName);
			log.debug(pst);

			res = pst.executeQuery();
			JSONObject orderDetails = null;
			while (res.next()) {
				String orderNo = res.getString(DBConstants.ORDER_NO);
				orderDetails = orderMap.get(orderNo);

				if (orderDetails == null) {
					orderDetails = new JSONObject();
					orderDetails.put(APP_CONSTANT.ORDER_TYPE, res.getString(DBConstants.ORDER_TYPE));
					orderDetails.put(APP_CONSTANT.ORDER_NUMBER, res.getString(DBConstants.ORDER_NO));
					orderDetails.put(APP_CONSTANT.CREATED_AT, res.getString(DBConstants.CREATED_AT));
					orderDetails.put(APP_CONSTANT.ORDER_STATUS, res.getString(DBConstants.STATUS));
					orderDetails.put(APP_CONSTANT.STOCKS, new JSONArray());
					orderMap.put(orderNo, orderDetails);
				}

				JSONObject stock = new JSONObject();

				stock.put(APP_CONSTANT.FILLED_QTY, res.getString(DBConstants.FILLED_QTY));
				stock.put(APP_CONSTANT.ORIGINAL_QTY, res.getString(DBConstants.ORIGINAL_QTY));
				stock.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
				stock.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(res.getDouble(DBConstants.AVG_PRICE)));
				stock.put(APP_CONSTANT.TYPE, res.getString(DBConstants.ORDER_TYPE));
//				stock.put(APP_CONSTANT.STATUS, res.getString(DBConstants.STATUS));

				orderDetails.getJSONArray(APP_CONSTANT.STOCKS).put(stock);
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pst);
			Helper.closeConnection(conn);
		}

		OrderHistoryDetailsResponse historyDetResp = new OrderHistoryDetailsResponse();
		historyDetResp.setOrderArr(new JSONArray(new Vector<JSONObject>(orderMap.values())));
		return historyDetResp;
	}
}
