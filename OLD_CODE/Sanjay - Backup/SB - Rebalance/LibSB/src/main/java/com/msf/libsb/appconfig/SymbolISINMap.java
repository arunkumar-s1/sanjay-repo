package com.msf.libsb.appconfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.DBConstants;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class SymbolISINMap 
{

	private static Logger log = Logger.getLogger(SymbolISINMap.class);
	private static HashMap<String, ArrayList<String>> isinSymbolMap = new HashMap<String, ArrayList<String>>();
	private static HashMap<String, String> symbolISINMap = new HashMap<String, String>();

	public static void loadMap(DBPool pool)
	{

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;

		try 
		{
			conn = pool.getQuoteDBConnection();
			String sQuery = "select symbol,isin from NSE_QUOTE where isin != '' and isin != 'na'";	
			ps = conn.prepareStatement(sQuery);
			log.debug(ps);
			res = ps.executeQuery();

			while (res.next()) 
			{
				if (isinSymbolMap.containsKey(res.getString(DBConstants.ISIN))) {
					isinSymbolMap.get(res.getString(DBConstants.ISIN)).add(res.getString(DBConstants.SYMBOL));
				} 
				else
				{
					ArrayList<String> arList = new ArrayList<>();
					arList.add(res.getString(DBConstants.SYMBOL));
					isinSymbolMap.put(res.getString(DBConstants.ISIN), arList);
				}

				symbolISINMap.put(res.getString(DBConstants.SYMBOL), res.getString(DBConstants.ISIN));
			}

			ps.close();
			res.close();

			sQuery = "select symbol,isin from BSE_QUOTE where isin != '' and isin != 'na'";
			ps = conn.prepareStatement(sQuery);

			res = ps.executeQuery();
			while (res.next())
			{
				if (isinSymbolMap.containsKey(res.getString(DBConstants.ISIN)))
				{
					isinSymbolMap.get(res.getString(DBConstants.ISIN)).add(res.getString(DBConstants.SYMBOL));
				} 
				else 
				{
					ArrayList<String> arList = new ArrayList<>();
					arList.add(res.getString(DBConstants.SYMBOL));
					isinSymbolMap.put(res.getString(DBConstants.ISIN), arList);
				}

				symbolISINMap.put(res.getString(DBConstants.SYMBOL), res.getString(DBConstants.ISIN));
			}

		} 
		catch (SQLException e) 
		{
			log.debug(e.getMessage());
		} 
		finally 
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}

	}

	public static boolean isValidSymbol(String symbol)
	{

		return symbolISINMap.containsKey(symbol);
	}

	public static boolean isValidISIN(String isin)
	{

		return isinSymbolMap.get(isin) != null && isinSymbolMap.get(isin).size() > 0;
	}

	public static boolean isISINWithDiffScrip(String isin, String sSymbol)
	{

		return isinSymbolMap.get(isin) != null && isinSymbolMap.get(isin).contains(sSymbol);
	}
	public static HashMap<String, ArrayList<String>> getIsinSymbolMap() 
	{
		return isinSymbolMap;
	}

	public static HashMap<String, String> getSymbolISINMap() 
	{
		return symbolISINMap;
	}
}
