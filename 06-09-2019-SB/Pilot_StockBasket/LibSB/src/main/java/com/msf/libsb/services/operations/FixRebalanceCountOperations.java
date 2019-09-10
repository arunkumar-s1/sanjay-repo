package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.me.JSONException;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.order.FixRebalanceCountResponse;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class FixRebalanceCountOperations
{
	private static Logger log = Logger.getLogger(FixRebalanceCountOperations.class);
	
	public static FixRebalanceCountResponse fixRebalanceCount(String userId) throws JSONException, SQLException
	{
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		String query=QueryConstants.GET_FIX_REBALANCE_COUNT_QUERY;
		int rebalanceCount=0;
		int fixCount=0;
	
		try
		{
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pst = conn.prepareStatement(query);
			
			pst.setString(1, userId);
			//pst.setString(2, userId);
			log.debug("---> pst :: " + pst);
			res = pst.executeQuery();
	
			if (res.next())			
			{
				//rebalanceCount=res.getInt(DBConstants.REBALANCE_COUNT);
				fixCount=res.getInt(DBConstants.FIX_COUNT);
			}
			
		}
		finally
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(pst);
			Helper.closeConnection(conn);
		}
		query = QueryConstants.GET_REBALANCE_COUNT_QUERY;
		try
		{
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pst = conn.prepareStatement(query);
			
			pst.setString(1, userId);
		
			log.debug("---> pst :: " + pst);
			res = pst.executeQuery();
	
			while (res.next())			
			{
				++rebalanceCount;
				
			}
			
		}
		finally
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(pst);
			Helper.closeConnection(conn);
		}
		
	
		FixRebalanceCountResponse fixRebalResp=new FixRebalanceCountResponse();
		fixRebalResp.setFixCount(fixCount);
		fixRebalResp.setRebalanceCount(rebalanceCount);
		return fixRebalResp;
	}
}
