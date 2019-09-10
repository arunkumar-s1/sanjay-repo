package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.stockbasket.BasketResponse;
import com.msf.libsb.services.stockbasket.CreateBasketRequest;
import com.msf.libsb.services.stockbasket.DeleteBasketRequest;
import com.msf.libsb.services.stockbasket.GetBasketDetailsRequest;
import com.msf.libsb.services.stockbasket.GetBasketDetailsResponse;
import com.msf.libsb.services.stockbasket.ModifyBasketRequest;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class BasketOperations {
	private static Logger log = Logger.getLogger(BasketOperations.class);

	public static Map<String, Integer> sortOrderMap = new HashMap<String, Integer>();
	static {
		sortOrderMap.put("ultra low", 0);
		sortOrderMap.put("low", 1);
		sortOrderMap.put("moderate", 2);
		sortOrderMap.put("high", 3);
		sortOrderMap.put("excellent", 4);

		sortOrderMap.put("poor", 0);
		sortOrderMap.put("fair", 2);
		sortOrderMap.put("good", 3);
		sortOrderMap.put("great", 4);

		sortOrderMap.put("least popular", 0);
		sortOrderMap.put("popular", 1);
		sortOrderMap.put("most popular", 2);
	}

	public static BasketResponse createBasket(CreateBasketRequest createReq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sQuery = QueryConstants.INSERT_BASKET_DETAILS;
		String basketVersion;
		String basketName;
		JSONArray constituents;
		HashMap<String, JSONObject> isinStockMap;
		try {
			constituents = createReq.getConstituents();
			isinStockMap = getISINStockMap(constituents);
			basketName = createReq.getBasketName();
			basketVersion = getPrevNewVersion(basketName)[1];
			int investType = createReq.getInvestType();
			Double initialFee = createReq.getInitialFee();
			Double rebalanceFee = createReq.getRebalanceFee();
			Double monitorFee = createReq.getMonitorFee();
			int trendingFlag = createReq.getTrendingFlag();
			int recommendedFlag = createReq.getRecommendedFlag();
			int featuredFlag = createReq.getFeaturedFlag();
			int riskFlag = createReq.getRiskFlag();
			Integer strength = sortOrderMap.get(createReq.getStrength());
			Integer valueForMoney = sortOrderMap.get(createReq.getValueForMoney());
			String category = createReq.getCategory();
			String subCategory = createReq.getSubCategory();
			String description = createReq.getDescription();
			String rationale = createReq.getRationale();
			String disclaimer = createReq.getDisclaimer();
			String imageURL = createReq.getImageURL();
			String iconURL = createReq.getIconURL();
			Double indexValue = createReq.getIndexValue();
			String benchMark = createReq.getBenchMark();
			Double oneMonthRet = createReq.getOneMonthRet();
			Double threeMonthRet = createReq.getThreeMonthRet();
			Double sixMonthRet = createReq.getSixMonthRet();
			Double oneYearRet = createReq.getOneYearRet();
			Double threeYearRet = createReq.getThreeYearRet();
			Double fiveYearRet = createReq.getFiveYearRet();
			updateOldVersion(basketName);

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(sQuery);
			pstmt.setString(1, basketName);
			pstmt.setString(2, basketVersion);
			pstmt.setInt(3, investType);
			pstmt.setDouble(4, initialFee);
			pstmt.setDouble(5, rebalanceFee);
			pstmt.setDouble(6, monitorFee);
			pstmt.setInt(7, trendingFlag);
			pstmt.setInt(8, recommendedFlag);
			pstmt.setInt(9, featuredFlag);
			pstmt.setInt(10, riskFlag);
			pstmt.setInt(11, strength == null ? 2 : strength);
			pstmt.setInt(12, valueForMoney == null ? 2 : valueForMoney);
			pstmt.setString(13, category);
			pstmt.setString(14, subCategory);
			pstmt.setString(15, fromHtmlTOString(description));
			pstmt.setString(16, fromHtmlTOString(rationale));
			pstmt.setString(17, fromHtmlTOString(disclaimer));
			pstmt.setDouble(18, indexValue);
			pstmt.setString(19, benchMark);
			pstmt.setString(20, imageURL);
			pstmt.setString(21, iconURL);
			pstmt.setDouble(22, oneMonthRet);
			pstmt.setDouble(23, threeMonthRet);
			pstmt.setDouble(24, sixMonthRet);
			pstmt.setDouble(25, oneYearRet);
			pstmt.setDouble(26, threeYearRet);
			pstmt.setDouble(27, fiveYearRet);

			log.debug(pstmt);
			pstmt.execute();

		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		String sConstituentsQuery = QueryConstants.INSERT_BASKET_CONTITUENTS;
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(sConstituentsQuery);

			for (int i = 0; i < constituents.length(); i++) {
				JSONObject jObj = constituents.getJSONObject(i);
				pstmt.setString(1, basketName);
				pstmt.setString(2, basketVersion);
				String isin = jObj.getString(APP_CONSTANT.ISIN);
				Double rating = jObj.getDouble(APP_CONSTANT.RATING);
				String qty = jObj.getString(APP_CONSTANT.QUANTITY);
				pstmt.setString(3, isin);
				JSONObject stock = isinStockMap.get(isin);
				pstmt.setString(4, stock.getString(APP_CONSTANT.EXCH));
				pstmt.setDouble(5, rating);
				pstmt.setString(6, qty);
				pstmt.setString(7, stock.getString(APP_CONSTANT.SYMBOL));
				pstmt.addBatch();
				log.debug("-->>" + pstmt);
			}
			pstmt.executeBatch();
		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		// getBasketVersionDiff(pool, "sample", "1.0.0", "1.0.5");
		BasketResponse addRes = new BasketResponse();
		addRes.setStatus(APP_CONSTANT.SUCCESS);
		addRes.setMessage("Basket " + basketName + " version " + basketVersion + " is created successfully");
		return addRes;
	}

	public static BasketResponse createBasketFromCons(CreateBasketRequest createReq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sQuery = QueryConstants.INSERT_BASKET_DETAILS2;
		String[] versions;
		String basketName;
		JSONArray constituents;
		HashMap<String, JSONObject> isinStockMap;
		try {
			basketName = createReq.getBasketName();
			versions = getPrevNewVersion(basketName);
			if (versions[0] == null) {
				throw new SamcoException("Basket " + basketName + " does not exist");
			}
			constituents = createReq.getConstituents();
			isinStockMap = getISINStockMap(constituents);
			updateOldVersion(basketName);

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(sQuery);
			pstmt.setString(1, basketName);
			pstmt.setString(2, versions[1]); // new version to insert
			pstmt.setString(3, basketName);
			pstmt.setString(4, versions[0]); // old existing version

			log.debug(pstmt);
			pstmt.execute();

		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		String constituentsQuery = QueryConstants.INSERT_BASKET_CONTITUENTS;
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(constituentsQuery);

			for (int i = 0; i < constituents.length(); i++) {
				JSONObject jObj = constituents.getJSONObject(i);
				pstmt.setString(1, basketName);
				pstmt.setString(2, versions[1]);
				String isin = jObj.getString(APP_CONSTANT.ISIN);
				Double rating = jObj.getDouble(APP_CONSTANT.RATING);
				String qty = jObj.getString(APP_CONSTANT.QUANTITY);
				pstmt.setString(3, isin);
				JSONObject stock = isinStockMap.get(isin);
				pstmt.setString(4, stock.getString(APP_CONSTANT.EXCH));
				pstmt.setDouble(5, rating);
				pstmt.setString(6, qty);
				pstmt.setString(7, stock.getString(APP_CONSTANT.SYMBOL));
				pstmt.addBatch();
				log.debug("-->>" + pstmt);
			}
			pstmt.executeBatch();
		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		// getBasketVersionDiff(pool, "sample", "1.0.0", "1.0.5");
		BasketResponse addRes = new BasketResponse();
		addRes.setStatus(APP_CONSTANT.SUCCESS);
		addRes.setMessage("Basket " + basketName + " version " + versions[1] + " is created successfully");
		return addRes;
	}

	public static void checkConstituentsExists(String basketName, JSONArray newConstituents) throws Exception {
		if (newConstituents.length() == 0) {
			throw new SamcoException("Basket constituents should not be empty");
		}
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet res = null;
		HashMap<String, Integer> existingConstituentsMap = new HashMap<String, Integer>();
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			st = conn.prepareStatement(QueryConstants.SELECT_MUL_BASKET_CONSTITUENTS);
			st.setString(1, basketName);
			res = st.executeQuery();
			while (res.next()) {
				existingConstituentsMap.put(res.getString(DBConstants.ISIN), res.getInt(DBConstants.QTY));
			}
		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(st);
			Helper.closeConnection(conn);
		}

		if (existingConstituentsMap.size() == newConstituents.length()) {
			int len = newConstituents.length();
			for (int i = 0; i < len; i++) {
				String newISIN = newConstituents.getJSONObject(i).getString(APP_CONSTANT.ISIN);
				int newQty = newConstituents.getJSONObject(i).getInt(APP_CONSTANT.QUANTITY);
				Integer existingQty = existingConstituentsMap.get(newISIN);

				if (existingQty == null || existingQty != newQty) {
					return;
				}
			}

			throw new SamcoException("Basket " + basketName + " already exist with the same constituents");
		}
	}

	private static HashMap<String, JSONObject> getISINStockMap(JSONArray cons) throws Exception {
		StringBuilder consStr = new StringBuilder();
		for (int i = 0; i < cons.length(); i++) {
			String isin = cons.getJSONObject(i).getString(APP_CONSTANT.ISIN);
			consStr.append("'" + isin + "',");
		}
		consStr.setLength(consStr.length() - 1);
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet res = null;
		HashMap<String, JSONObject> isinStockMap = null;
		try {
			conn = DBConnection.getInstance().getQuoteDBConnection();
			st = conn.prepareStatement("select 'NSE' as EXCHANGE,ISIN,SYMBOL from NSE_QUOTE where ISIN in ("
					+ consStr.toString() + ") union all select 'BSE' as exch,ISIN,SYMBOL from BSE_QUOTE where ISIN in ("
					+ consStr.toString() + ")");
			res = st.executeQuery();
			isinStockMap = new HashMap<String, JSONObject>();
			while (res.next()) {
				String exch = res.getString(DBConstants.EXCHANGE);
				String symbol = res.getString(DBConstants.SYMBOL);
				String isin = res.getString(DBConstants.ISIN);

				JSONObject stock = new JSONObject();
				stock.put(APP_CONSTANT.EXCH, exch);
				stock.put(APP_CONSTANT.SYMBOL, symbol);

				JSONObject existingStock = isinStockMap.get(isin);
				if (existingStock == null) {
					isinStockMap.put(isin, stock);
				} else {
					if (exch.equals("NSE"))
						isinStockMap.put(isin, stock);
				}
			}
		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(st);
			Helper.closeConnection(conn);
		}
		return isinStockMap;
	}

	private static String[] getPrevNewVersion(String basketName) throws SQLException {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet res = null;
		String newVersion = "1.0.0";
		String prevVersion = null;
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			st = conn.prepareStatement(QueryConstants.SELECT_BASKET_VERSION_BASKET_DETAILS);
			st.setString(1, basketName);
			res = st.executeQuery();
			if (res.next()) {
				prevVersion = res.getString(DBConstants.BASKET_VERSION);
				int version = Integer.parseInt(prevVersion.replace(".", "")) + 1;
				int patch = version % 10;
				version /= 10;
				int minor = version % 10;
				version /= 10;
				newVersion = version + "." + minor + "." + patch;
			}
		}

		finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(st);
			Helper.closeConnection(conn);
		}
		return new String[] { prevVersion, newVersion };
	}

	private static void updateOldVersion(String basketName) throws SQLException {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			st = conn.prepareStatement(QueryConstants.UPDATE_BASKET_DETAILS_UPDATEOLDVERSION);
			st.setString(1, basketName);
			st.execute();
			Helper.closeStatement(st);
			st = conn.prepareStatement(QueryConstants.UPDATE_BASKET_CONSTITUENTS_UPDATEOLDVERSION);
			st.setString(1, basketName);
			st.execute();
		} finally {
			Helper.closeStatement(st);
			Helper.closeConnection(conn);
		}
	}

	public static GetBasketDetailsResponse getBasketDetails(GetBasketDetailsRequest bdReq) throws Exception {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		JSONArray basketDetails = new JSONArray();
		Connection conn = null;
		Double netAmt = 0.0;

		try {
			String basketName = bdReq.getString(APP_CONSTANT.BASKET_NAME);
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			String[] queries = generateQuery(basketName);
			String sQuery = "select bc.EXCHANGE,bc.QTY,bc.QTY*(" + queries[0] + ") as PRICE, "
					+ queries[0].replace("LAST_PRICE", "SYMBOL_NAME") + " as SYMBOL_NAME from BASKET_CONSTITUENTS bc "
					+ queries[1] + " where bc.BASKET_NAME=? and bc.IS_LATEST=1";
			pstmt = conn.prepareStatement(sQuery);
			pstmt.setString(1, basketName);
			log.debug(pstmt);

			res = pstmt.executeQuery();
			while (res.next()) {
				JSONObject stockDetails = new JSONObject();
				String symbolName = res.getString(DBConstants.SYMBOL_NAME);
				int qty = res.getInt(DBConstants.QTY);

				Double lastPrice = res.getDouble(DBConstants.PRICE);
				stockDetails.put(APP_CONSTANT.SYMBOL_NAME, symbolName);
				stockDetails.put(APP_CONSTANT.QUANTITY, qty);
				Double price = qty * lastPrice;
				stockDetails.put(APP_CONSTANT.PRICE, price);
				basketDetails.put(stockDetails);
				netAmt += price;
			}

		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		GetBasketDetailsResponse bdRes = new GetBasketDetailsResponse();
		bdRes.put(APP_CONSTANT.STATUS, APP_CONSTANT.SUCCESS);
		// bdRes.put(APP_CONSTANT.BASKET_DETAILS, basketDetails);
		bdRes.put(APP_CONSTANT.NET_AMOUNT, netAmt);
		return bdRes;
	}

	static String[] generateQuery(String basketName) throws SQLException {
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;

		ArrayList<String> exchList = new ArrayList<String>();
		String query = QueryConstants.SELECT_EXCHANGE_LATEST_BASKET_DETAILS;

		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketName);
			log.debug(pstmt);
			res = pstmt.executeQuery();

			while (res.next()) {
				exchList.add(res.getString(DBConstants.EXCHANGE));
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		String[] queries = new String[2];
		if (exchList.size() > 1) {
			int t = 1;
			for (String exch : exchList) {
				String table = exch + "_QUOTE ";
				String tempTable = "t" + (t++);
				sb.append(tempTable + ".LAST_PRICE,");
				sb1.append(" left join quote_data." + table + tempTable + " on bc.EXCHANGE='" + exch
						+ "' and bc.SYMBOL=" + tempTable + ".SYMBOL");
			}

			sb.setLength(sb.length() - 1);
			queries[0] = "coalesce(" + sb.toString() + ")";
			queries[1] = sb1.toString();
		} else {
			String table = exchList.get(0) + "_QUOTE ";
			queries[0] = "t.LAST_PRICE";
			queries[1] = " left join quote_data." + table + "t on bc.SYMBOL=t.SYMBOL";
		}

		return queries;
	}

	public static BasketResponse deleteBasket(DeleteBasketRequest deleteReq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int affectedRowCount = 0;
		String basketName, basketVersion;
		try {
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(QueryConstants.DELETE_BASKET_DETAILS);
			basketName = deleteReq.getBasketName();
			basketVersion = deleteReq.getBasketVersion();
			pstmt.setString(1, basketName);
			pstmt.setString(2, basketVersion);
			log.debug(pstmt);
			affectedRowCount = pstmt.executeUpdate();
		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		BasketResponse deleteRes = new BasketResponse();
		if (affectedRowCount > 0) {
			deleteRes.setStatus(APP_CONSTANT.SUCCESS);
			deleteRes.setMessage("Basket " + basketName + ", version " + basketVersion + " is deleted successfully");
		} else {
			deleteRes.setStatus("failed");
			deleteRes.setMessage("Failed to delete basket " + basketName + ", version " + basketVersion);
		}
		return deleteRes;
	}

	public static BasketResponse modifyBasket(ModifyBasketRequest modifyReq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String basketName;
		int affectedRowCount = 0;
		try {
			basketName = modifyReq.getBasketName();
			// basketVersion=modifyReq.getBasketVersion();
			int investType = modifyReq.getInvestType();
			Double initialFee = modifyReq.getInitialFee();
			Double rebalanceFee = modifyReq.getRebalanceFee();
			Double monitorFee = modifyReq.getMonitorFee();
			int trendingFlag = modifyReq.getTrendingFlag();
			int recommendedFlag = modifyReq.getRecommendedFlag();
			int featuredFlag = modifyReq.getFeaturedFlag();
			int riskFlag = modifyReq.getRiskFlag();
			String strength = modifyReq.getStrength();
			String valueForMoney = modifyReq.getValueForMoney();

			String category = modifyReq.getCategory();
			String subCategory = modifyReq.getSubCategory();
			String description = modifyReq.getDescription();
			String rationale = modifyReq.getRationale();
			String disclaimer = modifyReq.getDisclaimer();
			Double indexValue = modifyReq.getIndexValue();
			String benchMark = modifyReq.getBenchMark();
			String imageURL = modifyReq.getImageURL();
			String iconURL = modifyReq.getIconURL();
			Double oneMonthRet = modifyReq.getOneMonthRet();
			Double threeMonthRet = modifyReq.getThreeMonthRet();
			Double sixMonthRet = modifyReq.getSixMonthRet();
			Double oneYearRet = modifyReq.getOneYearRet();
			Double threeYearRet = modifyReq.getThreeYearRet();
			Double fiveYearRet = modifyReq.getFiveYearRet();

			StringBuilder updateColums = new StringBuilder();
			if (investType >= 0)
				updateColums.append("INVEST_TYPE=" + investType + ",");
			if (initialFee != null)
				updateColums.append("INITIAL_FEE=" + initialFee + ",");
			if (rebalanceFee != null)
				updateColums.append("REBALANCE_FEE=" + rebalanceFee + ",");
			if (monitorFee != null)
				updateColums.append("MONITOR_FEE=" + monitorFee + ",");
			if (trendingFlag >= 0)
				updateColums.append("TRENDING_FLAG=" + trendingFlag + ",");
			if (recommendedFlag >= 0)
				updateColums.append("RECOMMENDED_FLAG=" + recommendedFlag + ",");
			if (featuredFlag >= 0)
				updateColums.append("FEATURED_FLAG=" + featuredFlag + ",");
			if (riskFlag >= 0)
				updateColums.append("RISK_FLAG=" + riskFlag + ",");
			if (!strength.isEmpty())
				updateColums.append("STRENGTH=" + strength + ",");
			if (!valueForMoney.isEmpty())
				updateColums.append("VALUE_FOR_MONEY=" + valueForMoney + ",");

			if (!category.isEmpty())
				updateColums.append("CATEGORY='" + category + "',");
			if (!subCategory.isEmpty())
				updateColums.append("SUB_CATEGORY='" + subCategory + "',");
			if (!description.isEmpty())
				updateColums.append("DESCRIPTION='" + fromHtmlTOString(description) + "',");
			if (!rationale.isEmpty())
				updateColums.append("RATIONALE='" + fromHtmlTOString(rationale) + "',");
			if (!disclaimer.isEmpty())
				updateColums.append("DISCLAIMER='" + fromHtmlTOString(disclaimer) + "',");
			if (indexValue != null)
				updateColums.append("INDEX_VALUE=" + indexValue + ",");
			if (!benchMark.isEmpty())
				updateColums.append("BENCHMARK='" + benchMark + "',");
			if (!imageURL.isEmpty())
				updateColums.append("IMAGE_URL='" + imageURL + "',");
			if (!iconURL.isEmpty())
				updateColums.append("ICON_URL='" + iconURL + "',");
			if (oneMonthRet != null)
				updateColums.append("ONE_MONTH_RET=" + oneMonthRet + ",");
			if (threeMonthRet != null)
				updateColums.append("THREE_MONTH_RET=" + threeMonthRet + ",");
			if (sixMonthRet != null)
				updateColums.append("SIX_MONTH_RET=" + sixMonthRet + ",");
			if (oneYearRet != null)
				updateColums.append("ONE_YEAR_RET=" + oneYearRet + ",");
			if (threeYearRet != null)
				updateColums.append("THREE_YEAR_RET=" + threeYearRet + ",");
			if (fiveYearRet != null)
				updateColums.append("FIVE_YEAR_RET=" + fiveYearRet + ",");

			updateColums.setLength(updateColums.length() - 1);
			updateColums.append(" where BASKET_NAME=? and IS_LATEST=1");
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement("update BASKET_DETAILS set " + updateColums.toString());
			pstmt.setString(1, basketName);

			log.debug(pstmt);
			affectedRowCount = pstmt.executeUpdate();

		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		BasketResponse modifyRes = new BasketResponse();
		if (affectedRowCount > 0) {
			modifyRes.setStatus(APP_CONSTANT.SUCCESS);
			modifyRes.setMessage("Basket " + basketName + " is modified successfully");
		} else {
			modifyRes.setStatus("failed");
			modifyRes.setMessage("Failed to modify basket " + basketName);
		}
		return modifyRes;
	}

	public static String fromHtmlTOString(String s) {
		if (s == null || !s.contains("&lt"))
			return s;

		final int firstIndex = 3;
		final int lastIndex = 6;
		String check = "&lt";
		String result = "";

		for (int i = 0; i < s.length(); i++) {
			boolean flag = true;
			if (s.charAt(i) == 'g' && s.charAt(i + 1) == 't' && s.length() - i > check.length()
					&& (!s.substring(i + firstIndex, i + lastIndex).equals(check))) {
				int j = i + 3;
				for (; flag; j++) {
					if (s.charAt(j + 1) == '&')
						flag = false;
					result += s.charAt(j);

				}
				i = j;
				result += " ";
			}
		}

		return result;
	}
}
