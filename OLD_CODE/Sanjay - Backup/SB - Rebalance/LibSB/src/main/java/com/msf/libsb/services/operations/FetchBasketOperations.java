package com.msf.libsb.services.operations;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.fetchbasket.FetchBasketsRequest;
import com.msf.libsb.services.fetchbasket.FetchBasketsResponse;
import com.msf.libsb.services.fetchbasket.SearchBasketRequest;
import com.msf.libsb.services.fetchbasket.SearchBasketResponse;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class FetchBasketOperations {
	private static Logger log = Logger.getLogger(FetchBasketOperations.class);

	static final String[] strengthArr = new String[] { "Ultra Low", "Low", "Moderate", "High", "Excellent" };
	static final String[] valueForMoneyArr = new String[] { "Poor", "Low", "Fair", "Good", "Great" };
	static final String[] popularityArr = new String[] { "Least Popular", "Popular", "Most Popular" };

	public static FetchBasketsResponse fetchBaskets(FetchBasketsRequest fetchBaskets) throws Exception {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet res = null;
		JSONArray basketArr = new JSONArray();
		String userId = fetchBaskets.getUserId();
		int pageNo = fetchBaskets.getPageNo();
		int totalBasketCount = 0;

		DecimalFormat dec = new DecimalFormat("0.00");

		PreparedStatement pstmt1 = null;
		ResultSet res1 = null;

		ArrayList<Double> indexList = new ArrayList<Double>();
		double change = 0.0;
		double changePer = 0.0;
		FetchBasketsResponse resp = new FetchBasketsResponse();
		try {
			String mainQuery = getQuery(fetchBaskets).replace("?", "'" + userId + "'");

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			cstmt = conn.prepareCall("{call QUERY_EXECUTOR(?,?)}");
			cstmt.setString(1, mainQuery);
			cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
			log.debug(cstmt);
			res = cstmt.executeQuery();

			totalBasketCount = cstmt.getInt(2);
			if (totalBasketCount == 0) {
				throw new SamcoException(INFO_IDS.NO_DATA_FOUND);
			}

			while (res.next()) {
				JSONObject basket = new JSONObject();
				int strength = res.getInt(DBConstants.STRENGTH);
				int valueForMoney = res.getInt(DBConstants.VALUE_FOR_MONEY);
				int popularity = res.getInt(DBConstants.POPULARITY);

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

				String a = "-0.00";
				String formatChange = dec.format(change);
				String formatChangePer = dec.format(changePer);
				String changeNew = (formatChange.equals(a) ? "0.00" : formatChange);
				String changePerNew = (formatChangePer.equals(a) ? "0.00" : formatChangePer);
				String indexValue = dFirstValue + "";
				indexList.clear();
				log.debug("changeNew--------->" + changeNew);
				log.debug("changePerNew--------->" + changePerNew);

				basket.put(APP_CONSTANT.BASKET_NAME, res.getString(DBConstants.BASKET_NAME));
				basket.put(APP_CONSTANT.STRENGTH, strengthArr[strength]);
				basket.put(APP_CONSTANT.VALUE_FOR_MONEY, valueForMoneyArr[valueForMoney]);
				basket.put(APP_CONSTANT.POPULARITY, popularityArr[popularity]);
				basket.put(APP_CONSTANT.INDEX_VALUE, indexValue);
				basket.put(APP_CONSTANT.MIN_AMT,
						SamcoHelper.getIndianCurrencyFormat(res.getDouble(DBConstants.NET_PRICE)));
				basket.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
				basket.put(APP_CONSTANT.CHANGE, changeNew);
				basket.put(APP_CONSTANT.CHANGE_PERCENT, changePerNew);
				basket.put(APP_CONSTANT.IMAGE_URL, res.getString(DBConstants.IMAGE_URL));
				basket.put(APP_CONSTANT.USER_WL_FLAG, res.getBoolean(DBConstants.WL_FLAG));

				JSONObject retPercent = new JSONObject();
				retPercent.put(APP_CONSTANT.M1, res.getString(DBConstants.ONE_MONTH_RET));
				retPercent.put(APP_CONSTANT.M3, res.getString(DBConstants.THREE_MONTH_RET));
				retPercent.put(APP_CONSTANT.M6, res.getString(DBConstants.SIX_MONTH_RET));
				retPercent.put(APP_CONSTANT.Y1, res.getString(DBConstants.ONE_YEAR_RET));
				retPercent.put(APP_CONSTANT.Y3, res.getString(DBConstants.THREE_YEAR_RET));
				retPercent.put(APP_CONSTANT.Y5, res.getString(DBConstants.FIVE_YEAR_RET));
				basket.put(APP_CONSTANT.RETURN_PERCENTAGE, retPercent);

				basketArr.put(basket);
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(cstmt);
			Helper.closeResultSet(res1);
			Helper.closeStatement(pstmt1);
			Helper.closeConnection(conn);
		}
		int basketCount = basketArr.length();
		if (basketCount == 0) {
			throw new SamcoException(INFO_IDS.NO_DATA_FOUND);
		}
		resp.setBasketArr(basketArr);
		resp.setBasketCount(totalBasketCount);
		resp.setEndFlag(totalBasketCount <= pageNo * 10);
		log.debug("-----------" + String.valueOf(basketCount) + "------------");
		log.debug("-----------" + String.valueOf(totalBasketCount) + "---------");

		return resp;
	}

	public static SearchBasketResponse searchBasket(SearchBasketRequest searchReq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		JSONArray basketArray = new JSONArray();
		JSONArray categoryArray = new JSONArray();
		JSONArray descriptionArray = new JSONArray();
		JSONArray stockArray = new JSONArray();
		String searchStr = searchReq.getSearchStr();
		String userId = searchReq.getUserId();

		DecimalFormat dec = new DecimalFormat("0.00");

		PreparedStatement pstmt1 = null;
		ResultSet res1 = null;

		ArrayList<Double> indexList = new ArrayList<Double>(2);
		double change = 0.0;
		double changePer = 0.0;

		boolean isBasketFound = false;
		try {
			String query = getQuery(searchReq);
			JSONArray temp;
			Pattern pattern = Pattern.compile(searchStr, Pattern.CASE_INSENSITIVE);
			Pattern pattern2 = Pattern.compile("\\b" + searchStr, Pattern.CASE_INSENSITIVE);

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			pstmt.setString(2, searchStr + "%");
			log.debug(pstmt);

			res = pstmt.executeQuery();

			// pstmt1 =
			// conn.prepareStatement(QueryConstants.FETCH_LATEST_INDEX_VALUE);
			// log.debug(pstmt1);

			while (res.next()) {
				String basketName = res.getString(DBConstants.BASKET_NAME);
				String description = res.getString(DBConstants.DESCRIPTION);

				if (pattern.matcher(basketName).find())
					temp = basketArray;

				else if (pattern2.matcher(res.getString(DBConstants.CATEGORY)).find())
					temp = categoryArray;

				else if (pattern2.matcher(description).find())
					temp = descriptionArray;

				else if (res.getBoolean(DBConstants.FLAG))
					temp = stockArray;

				else
					continue;

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
				// log.debug("indexList----->" + indexList);
				change = dFirstValue - dSecondValue;
				log.debug("change-------->" + change);

				changePer = (dSecondValue == 0 ? 0.00 : change / dSecondValue);
				log.debug("changePer-------->" + changePer);

				while (res1.next()) {
					indexList.add(res1.getDouble(DBConstants.INDEX_VALUE));
				}
				log.debug("indexList----->" + indexList);
				if (!indexList.isEmpty()) {
					change = indexList.get(0) - indexList.get(1);
					log.debug("change-------->" + change);

					changePer = (indexList.get(1) == 0 ? 0.00 : change / indexList.get(1));
					log.debug("changePer-------->" + changePer);
				}

				String a = "-0.00";
				String formatChange = dec.format(change);
				String formatChangePer = dec.format(changePer);
				String changeNew = (formatChange.equals(a) ? "0.00" : formatChange);
				String changePerNew = (formatChangePer.equals(a) ? "0.00" : formatChangePer);
				String indexValue = dFirstValue + "";
				indexList.clear();

				log.debug("changeNew--------->" + changeNew);
				log.debug("changePerNew--------->" + changePerNew);

				JSONObject basket = new JSONObject();
				int strength = res.getInt(DBConstants.STRENGTH);
				int valueForMoney = res.getInt(DBConstants.VALUE_FOR_MONEY);
				int popularity = res.getInt(DBConstants.POPULARITY);
				basket.put(APP_CONSTANT.BASKET_NAME, basketName);
				basket.put(APP_CONSTANT.STRENGTH, strengthArr[strength]);
				basket.put(APP_CONSTANT.VALUE_FOR_MONEY, valueForMoneyArr[valueForMoney]);
				basket.put(APP_CONSTANT.POPULARITY, popularityArr[popularity]);
				basket.put(APP_CONSTANT.INDEX_VALUE, indexValue);
				basket.put(APP_CONSTANT.MIN_AMT,
						SamcoHelper.getIndianCurrencyFormat(res.getDouble(DBConstants.NET_PRICE)));
				basket.put(APP_CONSTANT.DESCRIPTION, description);
				basket.put(APP_CONSTANT.CHANGE, changeNew);
				basket.put(APP_CONSTANT.CHANGE_PERCENT, changePerNew);
				basket.put(APP_CONSTANT.IMAGE_URL, res.getString(DBConstants.IMAGE_URL));
				basket.put(APP_CONSTANT.USER_WL_FLAG, res.getBoolean(DBConstants.WL_FLAG));

				JSONObject retPercent = new JSONObject();
				retPercent.put(APP_CONSTANT.M1, res.getString(DBConstants.ONE_MONTH_RET));
				retPercent.put(APP_CONSTANT.M3, res.getString(DBConstants.THREE_MONTH_RET));
				retPercent.put(APP_CONSTANT.M6, res.getString(DBConstants.SIX_MONTH_RET));
				retPercent.put(APP_CONSTANT.Y1, res.getString(DBConstants.ONE_YEAR_RET));
				retPercent.put(APP_CONSTANT.Y3, res.getString(DBConstants.THREE_YEAR_RET));
				retPercent.put(APP_CONSTANT.Y5, res.getString(DBConstants.FIVE_YEAR_RET));
				basket.put(APP_CONSTANT.RETURN_PERCENTAGE, retPercent);

				temp.put(basket);
				isBasketFound = true;
			}
		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		if (!isBasketFound) {
			throw new SamcoException(INFO_IDS.NO_DATA_FOUND);
		}

		sortBasketArray(basketArray, searchStr);
		mergeArray(new JSONArray[] { basketArray, categoryArray, descriptionArray, stockArray });

		SearchBasketResponse searchRes = new SearchBasketResponse();
		searchRes.setBasketArr(basketArray);
		searchRes.setBasketCount(basketArray.length());

		return searchRes;
	}

	private static void sortBasketArray(JSONArray basketArray, String searchStr) throws JSONException {
		int left = 0;
		int right = basketArray.length() - 1;
		Pattern pattern = Pattern.compile("^" + searchStr, Pattern.CASE_INSENSITIVE);
		while (left < right) {
			JSONObject current = basketArray.getJSONObject(left);
			String basketName = current.getString(APP_CONSTANT.BASKET_NAME);

			if (pattern.matcher(basketName).find())
				left++;
			else {
				basketArray.put(left, basketArray.getJSONObject(right));
				basketArray.put(right, current);
				right--;
			}
		}
	}

	private static void mergeArray(JSONArray[] basketArrays) throws JSONException {
		for (int i = 1; i < 4; i++) {
			int len = basketArrays[i].length();
			for (int j = 0; j < len; j++) {
				basketArrays[0].put(basketArrays[i].get(j));
			}
		}
	}

	private static String getQuery(FetchBasketsRequest fetchReq) throws JSONException {
		JSONObject filterBy = fetchReq.getFilterBy();
		JSONObject sort = fetchReq.getSort();
		String fetchBy = fetchReq.getFetchBy();
		int pageNo = fetchReq.getPageNo();

		StringBuilder filterQuery = new StringBuilder();
		StringBuilder priceFilterQuery = new StringBuilder();
		StringBuilder sortQuery = new StringBuilder();

		generateQuery(filterBy, sort, fetchBy, filterQuery, priceFilterQuery, sortQuery);

		int offset = (pageNo - 1) * 10;

		return QueryConstants.FETCH_BASKET_QUERY + filterQuery + priceFilterQuery + sortQuery + " limit 10 offset "
				+ offset;
	}

	private static String getQuery(SearchBasketRequest searchReq) throws JSONException {
		JSONObject filterBy = searchReq.getFilterBy();
		JSONObject sort = searchReq.getSort();
		String fetchBy = searchReq.getFetchBy();

		StringBuilder filterQuery = new StringBuilder();
		StringBuilder priceFilterQuery = new StringBuilder();
		StringBuilder sortQuery = new StringBuilder();

		generateQuery(filterBy, sort, fetchBy, filterQuery, priceFilterQuery, sortQuery);

		return QueryConstants.SEARCH_BASKET_QUERY + filterQuery + priceFilterQuery + sortQuery;
	}

	private static void generateQuery(JSONObject filterBy, JSONObject sort, String fetchBy, StringBuilder filterQuery,
			StringBuilder priceFilterQuery, StringBuilder sortQuery) throws JSONException {
		JSONArray strength = filterBy.optJSONArray(APP_CONSTANT.STRENGTH);
		JSONArray valueForMoney = filterBy.optJSONArray(APP_CONSTANT.VALUE_FOR_MONEY);
		JSONArray popularity = filterBy.optJSONArray(APP_CONSTANT.POPULARITY);
		JSONArray priceLimit = filterBy.optJSONArray(APP_CONSTANT.PRICE_LIMIT);

		int strengthLen = strength == null ? 0 : strength.length();
		int valueForMoneyLen = valueForMoney == null ? 0 : valueForMoney.length();
		int popularityLen = popularity == null ? 0 : popularity.length();
		int priceLimitLen = priceLimit == null ? 0 : priceLimit.length();

		if (fetchBy.equalsIgnoreCase("Trending"))
			filterQuery.append(" AND bd.TRENDING_FLAG=1");

		else if (fetchBy.equalsIgnoreCase("Recent"))
			filterQuery.append(" AND bd.CREATED_AT >= (NOW() - INTERVAL 2 MONTH)");

		else if (fetchBy.equalsIgnoreCase("Recommended"))
			filterQuery.append(" AND bd.RECOMMENDED_FLAG=1");

		else if (fetchBy.equalsIgnoreCase("Featured"))
			filterQuery.append(" AND bd.FEATURED_FLAG=1");

		if (strengthLen > 0 && strengthLen <= 5)
			filterQuery.append(" AND bd.STRENGTH in (" + toIntMap(strength) + ")");

		if (valueForMoneyLen > 0 && valueForMoneyLen <= 5)
			filterQuery.append(" AND bd.VALUE_FOR_MONEY in (" + toIntMap(valueForMoney) + ")");

		if (popularityLen > 0 && popularityLen <= 3)
			filterQuery.append(" AND bd.POPULARITY in (" + toIntMap(popularity) + ")");

		filterQuery.append(" group by BASKET_NAME ");

		if (priceLimitLen <= 5) {
			TreeSet<Integer> treeSet = new TreeSet<Integer>();
			boolean exceptionalFlag = false;
			for (int i = 0; i < priceLimitLen; i++) {
				String price = priceLimit.getString(i);
				if (price.equals("50,001+")) {
					exceptionalFlag = true;
					continue;
				}

				String[] s = price.replace(",", "").split("-");

				Integer left = Integer.valueOf(s[0].trim());
				Integer right = Integer.valueOf(s[1].trim());

				if (treeSet.contains(left - 1))
					treeSet.remove(left - 1);
				else
					treeSet.add(left);

				if (treeSet.contains(right + 1))
					treeSet.remove(right + 1);

				else
					treeSet.add(right);
			}

			if (exceptionalFlag) {
				if (treeSet.remove(50000))
					;
				else
					treeSet.add(50001);
			}

			boolean startFlag = true;
			priceFilterQuery.append("having");
			StringBuilder temp = new StringBuilder();
			for (Integer price : treeSet) {
				if (startFlag) {
					if (price > 0)
						temp.append(" NET_PRICE >= " + price);
				}

				else {
					if (temp.length() > 0)
						temp.append(" AND");

					priceFilterQuery.append(" (" + temp + " NET_PRICE <= " + price + ") or");
					temp = new StringBuilder();
				}
				startFlag = !startFlag; // flipping
			}

			if (temp.length() > 0)
				priceFilterQuery.append(" (" + temp + ")");
			int len = priceFilterQuery.length();
			if (priceFilterQuery.toString().endsWith("or"))
				priceFilterQuery.setLength(len - 2);

			else if (len == 6)
				priceFilterQuery.setLength(0);
		}
		log.debug("sort------->" + sort.get("sortBy"));
		if (sort.get("sortBy").equals(""))
			priceFilterQuery.append(" ORDER BY bd.CREATED_AT desc ");

		if (!sort.get("sortBy").equals("")) {
			String sortBy = sort.getString(APP_CONSTANT.SORT_BY);
			if (!sortBy.isEmpty()) {
				String orderBy = sort.getString(APP_CONSTANT.ORDER_BY);

				if (sortBy.equalsIgnoreCase("Investment Amount")) {
					sortQuery.append("ORDER BY NET_PRICE");
				}

				else if (sortBy.equalsIgnoreCase("Strength") && (strength == null || strength.length() != 1)) {
					sortQuery.append("ORDER BY bd.STRENGTH");
				}

				else if (sortBy.equalsIgnoreCase("Value for Money")
						&& (valueForMoney == null || valueForMoney.length() != 1)) {
					sortQuery.append("ORDER BY bd.VALUE_FOR_MONEY");
				}

				else if (sortBy.equalsIgnoreCase("Popularity") && (popularity == null || popularity.length() != 1)) {
					sortQuery.append("ORDER BY bd.POPULARITY");
				} else if (sortBy.equalsIgnoreCase("Index Value")) {
					sortQuery.append("ORDER BY bd.INDEX_VALUE");
				} else if (sortBy.equalsIgnoreCase("Returns")) {
					sortQuery.append("ORDER BY bd.ONE_YEAR_RET");
				}

				if (!orderBy.equalsIgnoreCase("asc"))
					sortQuery.append(" desc");
			}
		}

	}

	public static String toIntMap(JSONArray arr) throws JSONException {
		log.debug("arr-------->" + arr);
		Map<String, Integer> map = BasketOperations.sortOrderMap;
		log.debug("Map------------->" + map.values());
		StringBuilder sb = new StringBuilder();
		int len = arr.length();
		log.debug("Length----------->" + len);
		for (int i = 0; i < len; i++) {
			Integer value = map.get(arr.getString(i).toLowerCase());
			log.debug("value---------->" + value);
			if (value != null)
				sb.append(value + ",");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	public static JSONObject checkFlag() throws SQLException, JSONException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;

		JSONObject flags = new JSONObject();

		try {
			String query = QueryConstants.SELECT_FLAG_BASKET_DETAILS;
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			log.debug(pstmt);
			res = pstmt.executeQuery();

			if (res.next()) {
				flags.put(APP_CONSTANT.RECOMMENDED, res.getBoolean(DBConstants.RECOMMENDED));
				flags.put(APP_CONSTANT.FEATURED, res.getBoolean(DBConstants.FEATURED));
			}
		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		return flags;
	}
}
