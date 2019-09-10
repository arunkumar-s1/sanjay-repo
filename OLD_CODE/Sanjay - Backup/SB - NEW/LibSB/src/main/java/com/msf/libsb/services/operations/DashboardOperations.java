package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.dashboard.GetDashboardBasketsResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class DashboardOperations {
	private static Logger log = Logger.getLogger(DashboardOperations.class);

	public static GetDashboardBasketsResponse getDashboardBaskets(String userID) throws Exception {

		Connection conn = null;

		PreparedStatement pstmt = null;
		ResultSet res = null;

		DecimalFormat dec = new DecimalFormat("0.00");

		PreparedStatement pstmt1 = null;
		ResultSet res1 = null;

		List<Double> indexList = new ArrayList<>();

		PreparedStatement pstmt2 = null;
		ResultSet res2 = null;

		int initialInvestLength = 0;

		JSONArray recommendedBasketArr = new JSONArray();
		JSONArray featuredBasketArr = new JSONArray();
		JSONArray popularBasketArr = new JSONArray();
		try {
			double change = 0.0;
			double changePer = 0.0;

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(QueryConstants.DASHBOARD_BASKETS_QUERY);
			log.debug(pstmt);


			pstmt2 = conn.prepareStatement(QueryConstants.INITIAL_INVEST);
			pstmt2.setString(1, userID);
			log.debug(pstmt2);
			res2 = pstmt2.executeQuery();

			while (res2.next()) {
				initialInvestLength++;

			}

			res = pstmt.executeQuery();
			while (res.next()) {
				int popularity = res.getInt(DBConstants.POPULARITY);
				boolean recommendedFlag = res.getBoolean(DBConstants.RECOMMENDED_FLAG);
				boolean featuredFlag = res.getBoolean(DBConstants.FEATURED_FLAG);

				JSONObject basket = new JSONObject();
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
					while (res1.next()) {
						if (!bFirstValue) {
							dFirstValue = res1.getDouble(DBConstants.INDEX_VALUE);
							bFirstValue = true;
						} else {
							dSecondValue = res1.getDouble(DBConstants.INDEX_VALUE);
						}
					}
				}
				// log.debug("indexList----->" + indexList);
				change = dFirstValue - dSecondValue;
				log.debug("change-------->" + change);

				changePer = (dSecondValue == 0 ? 0.00 : change / dSecondValue);
				log.debug("changePer-------->" + changePer);



				basket.put(APP_CONSTANT.ICON_URL, res.getString(DBConstants.ICON_URL));
				basket.put(APP_CONSTANT.MIN_AMT,
						SamcoHelper.getIndianCurrencyFormat(res.getDouble(DBConstants.NET_PRICE)));
				basket.put(APP_CONSTANT.CHANGE, dec.format(change));
				basket.put(APP_CONSTANT.CHANGE_PERCENT, dec.format(changePer));
				indexList.clear();
				basket.put(APP_CONSTANT.POPULARITY,
						FetchBasketOperations.popularityArr[popularity == 0 ? 0 : popularity - 1]);
				basket.put(APP_CONSTANT.M1, res.getString(DBConstants.ONE_MONTH_RET));
				basket.put(APP_CONSTANT.Y1, res.getString(DBConstants.ONE_YEAR_RET));

				if (recommendedFlag)
					recommendedBasketArr.put(basket);
				else
					popularBasketArr.put(basket);
			}

		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
			Helper.closeStatement(pstmt1);
			Helper.closeResultSet(res1);
			Helper.closeStatement(pstmt2);
			Helper.closeResultSet(res2);

		}

		GetDashboardBasketsResponse resp = new GetDashboardBasketsResponse();

		resp.setRecommendedBasketArr(recommendedBasketArr);
		resp.setFeaturedBasketArr(featuredBasketArr);
		resp.setPopularBasketArr(popularBasketArr);
		if (initialInvestLength != 0) {
			resp.setInitialInvest(true);
			resp.setMessage("Increase your stock basket investment");
			resp.setInvestMessage("Explore Baskets and Invest");
			resp.setImageUrl("https://cdn.stockbasket.com/tmp/IMG-20190716-WA0008.jpg");
		} else {
			resp.setMessage("Let's make your first stock basket investment");
			resp.setInvestMessage("Pick a StockBasket and begin");
			resp.setImageUrl("https://cdn.stockbasket.com/tmp/IMG-20190716-WA0007.jpg");
		}

		return resp;
	}
}
