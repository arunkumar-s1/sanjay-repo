package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.quote.QuoteOverviewResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class QuoteOperations {
	private static Logger log = Logger.getLogger(QuoteOperations.class);

	public static QuoteOverviewResponse quoteOverView(String basketName, String userId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ResultSet res1 = null;
		DecimalFormat dec = new DecimalFormat("0.00");
		PreparedStatement pstmt1 = null;
		ArrayList<Double> indexList = new ArrayList<Double>();
		QuoteOverviewResponse resp = new QuoteOverviewResponse();
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(QueryConstants.QUOTE_OVERVIEW_QUERY);
			pstmt.setString(1, userId);
			pstmt.setString(2, basketName);
			log.debug(pstmt);

			
			res = pstmt.executeQuery();

			
			if (res.next()) {
				pstmt1 = conn.prepareStatement(QueryConstants.FETCH_LATEST_INDEX_VALUE);
				pstmt1.setString(1, basketName);
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
				double change = 0.0;
				double changePer = 0.0;

				// log.debug("indexList----->" + indexList);
				change = dFirstValue - dSecondValue;
				log.debug("change-------->" + change);

				changePer = (dSecondValue == 0 ? 0.00 : change / dSecondValue);
				log.debug("changePer-------->" + changePer);

				int strength = res.getInt(DBConstants.STRENGTH);
				int valueForMoney = res.getInt(DBConstants.VALUE_FOR_MONEY);
				int popularity = res.getInt(DBConstants.POPULARITY);

								String a = "-0.00";
				String formatChange = dec.format(change);
				String formatChangePer = dec.format(changePer);
				String changeNew = (formatChange.equals(a) ? "0.00" : formatChange);
				String changePerNew = (formatChangePer.equals(a) ? "0.00" : formatChangePer);
				String indexValue = dFirstValue + "";
				indexList.clear();

				log.debug("changeNew--------->" + changeNew);
				log.debug("changePerNew--------->" + changePerNew);

				resp.setBasketDescription(res.getString(DBConstants.DESCRIPTION));
				resp.setMinAmt(SamcoHelper.getIndianCurrencyFormat(res.getDouble(DBConstants.NET_PRICE)));
				resp.setIndexValue(indexValue);
				resp.setChange(changeNew);
				resp.setChangePer(changePerNew);

				resp.setPopularity(FetchBasketOperations.popularityArr[popularity == 0 ? 0 : popularity - 1]);
				resp.setStrength(FetchBasketOperations.strengthArr[strength == 0 ? 0 : strength - 1]);
				resp.setValueForMoney(
						FetchBasketOperations.valueForMoneyArr[valueForMoney == 0 ? 0 : valueForMoney - 1]);
				resp.setUserWLFlag(res.getBoolean(DBConstants.WL_FLAG));
				resp.setFeatured(res.getBoolean(DBConstants.FEATURED_FLAG));
				resp.setRecommended(res.getBoolean(DBConstants.RECOMMENDED_FLAG));
				resp.setRationale(res.getString(DBConstants.RATIONALE));
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeResultSet(res1);
			Helper.closeStatement(pstmt1);
			Helper.closeConnection(conn);
		}

		return resp;
	}
}
