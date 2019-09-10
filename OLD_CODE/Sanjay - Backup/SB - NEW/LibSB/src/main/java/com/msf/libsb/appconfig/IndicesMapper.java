package com.msf.libsb.appconfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.msf.libsb.connection.DBPool;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class IndicesMapper 
{

	private static Logger log = Logger.getLogger(IndicesMapper.class);
	private static HashMap<String, String> indMap = new HashMap<>();

	public static void loadMap(DBPool pool)
	{

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;

		try 
		{
			conn = pool.getQuoteDBConnection();
			String sQuery = "select CODE,VALUE from APP_CONFIG where build_name = 'android-phone' and code in ('NSE_INDICES', 'BSE_INDICES') and is_deleted is NULL";
			ps = conn.prepareStatement(sQuery);
			res = ps.executeQuery();

			String sExchange = null;
			while (res.next()) 
			{
				String sCode = res.getString("CODE");
				if (sCode.contains("NSE"))
					sExchange = "NSE";
				else if (sCode.contains("BSE"))
					sExchange = "BSE";
				else
					sExchange = "-";

				String list = res.getString("VALUE");
				String [] values = list.split(",");
				for (String key : values) 
				{
					String[] sPipes = key.split("\\|");
					indMap.put(sPipes[0], sExchange);
				}
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

	public static String getExchangeForIndex(String sIndex)
	{
		return indMap.get(sIndex);
	}

}
