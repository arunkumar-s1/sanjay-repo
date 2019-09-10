package com.msf.libsb.welcome;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.utils.helper.Helper;

public class WelcomeAboard
{

	//	private static Logger log = Logger.getLogger(WelcomeAboard.class);

	private DBPool pool = null;

	public WelcomeAboard(DBPool pool)
	{
		this.pool = pool;
	}

	public JSONObject getInterestSymbols(JSONArray jList) throws Exception 
	{
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet res = null;

		int iEqs = 0;
		int iEqd = 0;
		int iCom = 0;
		int iInd = 0;
		int iCur = 0;

		for (int i = 0; i < jList.length(); i++)
		{
			String sSymbol = jList.getString(i);
			if (sSymbol.equalsIgnoreCase(APP_CONSTANT.EQ_STOCK))
				iEqs = 1;
			else if (sSymbol.equalsIgnoreCase(APP_CONSTANT.EQ_DERIV))
				iEqd = 1;
			else if (sSymbol.equalsIgnoreCase(APP_CONSTANT.COMMODITY))
				iCom = 1;
			else if (sSymbol.equalsIgnoreCase(APP_CONSTANT.INDEX))
				iInd = 1;
			else if (sSymbol.equalsIgnoreCase(APP_CONSTANT.CURRENCY))
				iCur = 1;
		}

		HashMap<String, ArrayList<String>> mSymbols = new HashMap<String, ArrayList<String>>();
		ArrayList<String> arEqs = new ArrayList<String>();
		ArrayList<String> arEqd = new ArrayList<String>();
		ArrayList<String> arCom = new ArrayList<String>();
		ArrayList<String> arInd = new ArrayList<String>();
		ArrayList<String> arCur = new ArrayList<String>();

		try
		{

			conn = this.pool.getConnection();
			String sQuery = "{call welcome_get_symbols(?,?,?,?,?)}";
			cs = conn.prepareCall(sQuery);
			cs.setInt(1, iEqs);
			cs.setInt(2, iEqd);
			cs.setInt(3, iCom);
			cs.setInt(4, iInd);
			cs.setInt(5, iCur);

			//log.debug(cs);
			cs.executeQuery();

			do
			{
				res = cs.getResultSet();

				while (res.next()) 
				{
					String type = res.getString(DBConstants.TYPE);
					//log.debug(type);

					if (type.equalsIgnoreCase(DBConstants.EQUITY) && !arEqs.contains(res.getString(DBConstants.SYMBOL)))
						arEqs.add(res.getString(DBConstants.SYMBOL));
					else if (type.equalsIgnoreCase(DBConstants.EQ_DERIVATIVE) && !arEqd.contains(res.getString(DBConstants.SYMBOL)))
						arEqd.add(res.getString(DBConstants.SYMBOL));
					else if (type.equalsIgnoreCase(DBConstants.INDICES) && !arInd.contains(res.getString(DBConstants.SYMBOL)))
						arInd.add(res.getString(DBConstants.SYMBOL));
					else if (type.equalsIgnoreCase(DBConstants.CURRENCY) && !arCur.contains(res.getString(DBConstants.SYMBOL)))
						arCur.add(res.getString(DBConstants.SYMBOL));
					else if (type.equalsIgnoreCase(DBConstants.COMMODITY) && !arCom.contains(res.getString(DBConstants.SYMBOL)))
						arCom.add(res.getString(DBConstants.SYMBOL));
				}

			}
			while (cs.getMoreResults());

			mSymbols.put(APP_CONSTANT.EQ_STOCK, arEqs);
			mSymbols.put(APP_CONSTANT.EQ_DERIV, arEqd);
			mSymbols.put(APP_CONSTANT.COMMODITY, arCom);
			mSymbols.put(APP_CONSTANT.INDEX, arInd);
			mSymbols.put(APP_CONSTANT.CURRENCY, arCur);

		} 
		finally 
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(cs);
			Helper.closeConnection(conn);
		}

		return addQuote(mSymbols);
	}

	private JSONObject addQuote(HashMap<String, ArrayList<String>> mSymbols) throws Exception 
	{
		JSONArray jSymbols = new JSONArray();

		for (Map.Entry<String, ArrayList<String>> entry : mSymbols.entrySet())
		{
			ArrayList<String> arList = entry.getValue();
			for (String symbol : arList)
			{
				jSymbols.put(symbol);
			}
		}

		JSONArray jQuotes = new JSONArray();
		int totCount = 0;
		JSONObject jFinal = new JSONObject();
		for (Map.Entry<String, ArrayList<String>> entry : mSymbols.entrySet())
		{

			ArrayList<String> arList = entry.getValue();
			JSONArray jType = new JSONArray();

			for (String symbol : arList)
			{
				for (int i = 0; i < jQuotes.length(); i++) {
					if (jQuotes.getJSONObject(i).getString(APP_CONSTANT.SYMBOL).equals(symbol)) 
					{
						JSONObject data = new JSONObject();
						data.put(APP_CONSTANT.SYMBOL, jQuotes.getJSONObject(i).getString(APP_CONSTANT.SYMBOL));
						data.put(APP_CONSTANT.SYMBOL_NAME,
								jQuotes.getJSONObject(i).getString(APP_CONSTANT.SYMBOL_NAME));
						data.put(APP_CONSTANT.STRIKE_PRICE,
								jQuotes.getJSONObject(i).getString(APP_CONSTANT.STRIKE_PRICE));
						data.put(APP_CONSTANT.DISPLAY_STRIKE_PRICE,
								jQuotes.getJSONObject(i).getString(APP_CONSTANT.DISPLAY_STRIKE_PRICE));
						data.put(APP_CONSTANT.EXPIRY, jQuotes.getJSONObject(i).getString(APP_CONSTANT.EXPIRY));
						data.put(APP_CONSTANT.DISPLAY_EXPIRY_DATE,
								jQuotes.getJSONObject(i).getString(APP_CONSTANT.DISPLAY_EXPIRY_DATE));
						data.put(APP_CONSTANT.OPTION_TYPE,
								jQuotes.getJSONObject(i).getString(APP_CONSTANT.OPTION_TYPE));
						data.put(APP_CONSTANT.TRADING_SYMBOL,
								jQuotes.getJSONObject(i).getString(APP_CONSTANT.TRADING_SYMBOL));
						data.put(APP_CONSTANT.COMPANY_NAME,
								jQuotes.getJSONObject(i).getString(APP_CONSTANT.COMPANY_NAME));
						data.put(APP_CONSTANT.INSTRUMENT, jQuotes.getJSONObject(i).getString(APP_CONSTANT.INSTRUMENT));
						data.put(APP_CONSTANT.DISPLAY_INSTRUMENT_TYPE,
								jQuotes.getJSONObject(i).getString(APP_CONSTANT.DISPLAY_INSTRUMENT_TYPE));

						data.put(APP_CONSTANT.IS_ENABLED, false);
						jType.put(data);
						totCount++;
					}
				}
			}
			jFinal.put(entry.getKey(), jType);

		}
		jFinal.put(APP_CONSTANT.NUMBER_OF_RECORDS, totCount);
		return jFinal;

	}

	public JSONObject setProfile(String sUserID, String sType) throws Exception
	{

		Connection conn = null;
		CallableStatement cs = null;

		try
		{
			String sQuery = "{call setInvestorProfile(?, ?)}";
			conn = this.pool.getConnection();
			cs = conn.prepareCall(sQuery);
			cs.setString(1, sUserID);
			cs.setString(2, sType);
			//log.debug(cs);
			cs.executeQuery();

			JSONObject data = SamcoHelper.checkProductAndOrderPreference(sUserID, pool);
			return data;
		} 
		finally
		{
			Helper.closeStatement(cs);
			Helper.closeConnection(conn);
		}
	}
}
