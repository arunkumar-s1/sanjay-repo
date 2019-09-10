package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.appconfig.InfoMessage;
import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.watchlist.GetUserWatchListRequest;
import com.msf.libsb.services.watchlist.GetUserWatchListResponse;
import com.msf.libsb.services.watchlist.GetWatchListDetailsRequest;
import com.msf.libsb.services.watchlist.GetWatchListDetailsResponse;
import com.msf.libsb.services.watchlist.WatchListBasketRequest;
import com.msf.libsb.services.watchlist.WatchListBasketResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class WatchlistOperations {
	private static Logger log = Logger.getLogger(WatchlistOperations.class);

	public static WatchListBasketResponse addUserWatchListBasket(WatchListBasketRequest addReq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = QueryConstants.INSERT_USER_WATCHLIST_BASKET;

		try {
			String userID = addReq.getUserID();
			String basketName = addReq.getBasketName();

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userID);
			pstmt.setString(2, basketName);
			log.debug(query);

			pstmt.execute();
		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		WatchListBasketResponse addRes = new WatchListBasketResponse();

		addRes.setMessage(InfoMessage.getInfoMSG("watchlist.add_basket_success"));
		return addRes;
	}

	public static GetUserWatchListResponse getUserWatchList(GetUserWatchListRequest getReq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sQuery = QueryConstants.SELECT_BASKET_NAME_USER_WATCHLIST;
		ResultSet res = null;
		JSONArray watchListArr = new JSONArray();
		try {

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(sQuery);
			String sUserID = getReq.getUserId();
			pstmt.setString(1, sUserID);

			log.debug(pstmt);
			res = pstmt.executeQuery();
			while (res.next()) {
				watchListArr.put(res.getString(DBConstants.BASKET_NAME));
			}

		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		GetUserWatchListResponse getRes = new GetUserWatchListResponse();
		getRes.setWatchListArr(watchListArr);
		return getRes;
	}

	public static WatchListBasketResponse deleteUserWatchListBasket(WatchListBasketRequest deleteReq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String basketName = deleteReq.getBasketName();
		String userId = deleteReq.getUserID();
		String query = QueryConstants.DELETE_USER_WATCHLIST_BASKET;
		int affectedRowCount = 0;
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			pstmt.setString(2, basketName);
			log.debug(pstmt);
			affectedRowCount = pstmt.executeUpdate();
		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		WatchListBasketResponse deleteRes = new WatchListBasketResponse();

		if (affectedRowCount > 0)
			deleteRes.setMessage(InfoMessage.getInfoMSG("watchlist.delete_basket_success"));
		else
			deleteRes.setMessage(InfoMessage.getInfoMSG("watchlist.delete_basket_failed"));
		return deleteRes;
	}

	public static GetWatchListDetailsResponse getWatchListDetails(GetWatchListDetailsRequest getReq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ResultSet res1 = null;
		DecimalFormat dec = new DecimalFormat("0.00");
		PreparedStatement pstmt1 = null;
		List<Double> indexList = new ArrayList<>();
		JSONArray watchListArr = new JSONArray();
		try {
			double change = 0.0;
			double changePer = 0.0;
			String userId = getReq.getUserId();
			String query = QueryConstants.SELECT_WATCHLIST_DETAILS;

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			log.debug(pstmt);
			res = pstmt.executeQuery();

			
			while (res.next()) {
				JSONObject basket = new JSONObject();
				int strength = res.getInt(DBConstants.STRENGTH);
				int valueForMoney = res.getInt(DBConstants.VALUE_FOR_MONEY);
				int popularity = res.getInt(DBConstants.POPULARITY);

				basket.put(APP_CONSTANT.BASKET_NAME, res.getString(DBConstants.BASKET_NAME));
				pstmt1 = conn.prepareStatement(QueryConstants.FETCH_LATEST_INDEX_VALUE);
				pstmt1.setString(1, res.getString(DBConstants.BASKET_NAME));
				log.debug(pstmt1);
				res1 = pstmt1.executeQuery();
				Double dFirstValue = 0.0;
				Double dSecondValue = 0.0;
				boolean bFirstValue = false;
				if (!res1.next()) {
					log.debug("---> Inside if");
					dFirstValue = 100.0;
					dSecondValue = 100.0;

				} else {
					log.debug("---> Inside else");
					do {
						if (!bFirstValue) {
							dFirstValue = res1.getDouble(DBConstants.INDEX_VALUE);
							bFirstValue = true;
						} else {
							dSecondValue = res1.getDouble(DBConstants.INDEX_VALUE);
						}
					} while(res1.next());
				}
				// log.debug("indexList----->" + indexList);
				change = dSecondValue - dFirstValue;
				log.debug("change-------->" + change);

				changePer = (dSecondValue == 0 ? 0.00 : ((change / dSecondValue)) * 100);
				log.debug("changePer-------->" + changePer);

				String sIndexValue = dec.format(dFirstValue);

				basket.put(APP_CONSTANT.STRENGTH, FetchBasketOperations.strengthArr[strength == 0 ? 0 : strength]);
				basket.put(APP_CONSTANT.VALUE_FOR_MONEY,
						FetchBasketOperations.valueForMoneyArr[valueForMoney == 0 ? 0 : valueForMoney]);
				basket.put(APP_CONSTANT.POPULARITY,
						FetchBasketOperations.popularityArr[popularity == 0 ? 0 : popularity]);
				basket.put(APP_CONSTANT.INDEX_VALUE, sIndexValue);
				basket.put(APP_CONSTANT.MIN_AMT,
						SamcoHelper.getIndianCurrencyFormat(res.getDouble(DBConstants.NET_PRICE)));
				basket.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
				basket.put(APP_CONSTANT.CHANGE, dec.format(change));
				basket.put(APP_CONSTANT.CHANGE_PERCENT, dec.format(changePer));
				indexList.clear();

				basket.put(APP_CONSTANT.IMAGE_URL, res.getString(DBConstants.IMAGE_URL));

				JSONObject retPercent = new JSONObject();
				retPercent.put(APP_CONSTANT.M1, res.getString(DBConstants.ONE_MONTH_RET));
				retPercent.put(APP_CONSTANT.M3, res.getString(DBConstants.THREE_MONTH_RET));
				retPercent.put(APP_CONSTANT.M6, res.getString(DBConstants.SIX_MONTH_RET));
				retPercent.put(APP_CONSTANT.Y1, res.getString(DBConstants.ONE_YEAR_RET));
				retPercent.put(APP_CONSTANT.Y3, res.getString(DBConstants.THREE_YEAR_RET));
				retPercent.put(APP_CONSTANT.Y5, res.getString(DBConstants.FIVE_YEAR_RET));
				basket.put(APP_CONSTANT.RETURN_PERCENTAGE, retPercent);

				watchListArr.put(basket);
			}
		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
			Helper.closeStatement(pstmt1);
			Helper.closeResultSet(res1);
		}

		GetWatchListDetailsResponse getRes = new GetWatchListDetailsResponse();
		getRes.setWatchlistArr(watchListArr);

		return getRes;
	}
}
