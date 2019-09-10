package com.msf.libsb.utils.helper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class SessionHelper
{

	private static Logger log = Logger.getLogger(SessionHelper.class);

	public static boolean logoutUser(DBPool pool, String sessionID) throws SQLException, Exception 
	{

		String logoutUserQuery = "DELETE from USER_SESSION where session_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;

		try 
		{
			conn = pool.getConnection();
			ps = conn.prepareStatement(logoutUserQuery);
			ps.setString(1, sessionID);

			int _result = ps.executeUpdate();

			if (_result > 0) 
			{
				return true;
			} 
			else 
			{
				throw new SamcoException(INFO_IDS.INVALID_SESSION, "Invalid session from db");
			}

		} 
		finally 
		{
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}

	}

	public static void logoutUserUsingUserID(DBPool pool, String userID) throws Exception 
	{

		String logoutUserQuery = "DELETE from USER_SESSION where user_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;

		try 
		{
			conn = pool.getConnection();
			ps = conn.prepareStatement(logoutUserQuery);
			ps.setString(1, userID);
			ps.executeUpdate();
		} 
		finally 
		{
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
	}

	public static String getMd5Sum(String request) throws Exception
	{

		String md5Sum = "";
		md5Sum = Helper.MD5sum(request);

		return md5Sum;
	}

	public static boolean isDuplicateOrder(String jSessionID, String request, DBPool pool) throws Exception
	{

		boolean isDuplicateOrd = true;

		Connection conn = null;
		CallableStatement cs = null;

		int orderCount = Integer.parseInt(request);

		String query = "{call order_validator(?,?,?)}";

		try 
		{

			conn = pool.getConnection();
			cs = conn.prepareCall(query);

			cs.setString(1, jSessionID);
			cs.setInt(2, orderCount);
			cs.registerOutParameter(3, java.sql.Types.INTEGER);

			cs.executeUpdate();

			int _result = cs.getInt(3);

			if (_result == 1)
				isDuplicateOrd = false;

		} 
		finally 
		{
			Helper.closeStatement(cs);
			Helper.closeConnection(conn);
		}

		return isDuplicateOrd;
	}

	public static String getBuildName(DBPool pool, String sAppID) throws SQLException, Exception
	{

		String sQuery = "select BUILD from APP_INFO where APP_ID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		String sBuildName = null;

		try 
		{
			conn = pool.getConnection();
			ps = conn.prepareStatement(sQuery);
			ps.setString(1, sAppID);

			// log.debug("select build ::::" + ps);
			result = ps.executeQuery();

			if (result.next())
				sBuildName = result.getString("BUILD");

		} 
		finally
		{
			Helper.closeResultSet(result);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}

		return sBuildName;
	}

	public static boolean updateSessionToDB(DBPool pool, Session session) throws SQLException, Exception 
	{

		logoutUserUsingUserID(pool, session.getUserId());

		String sBuildName = getBuildName(pool, session.getAppID());

		String Query = "INSERT INTO USER_SESSION (user_id, session_id, app_id,build, created_at, action, user_type,"
				+ "last_active, user_stage, jsession, jkey,product_alias,is_enable_transPass, account_id,"
				+ "branch_id, broker_name,samco_token, product_type, weblink, exchange_array) "
				+ "VALUES(?, ?, ?, ?," + "NOW(), ?, ?, UNIX_TIMESTAMP(), ?,?,?,?,'1',?,?,?,?,?,?,?)"
				+ "ON DUPLICATE KEY UPDATE session_id = VALUES( session_id ),app_id = VALUES(app_id),created_at = NOW(),"
				+ "user_stage= VALUES(user_stage),jsession = VALUES(jsession), jkey = VALUES(jkey),"
				+ "account_id = VALUES(account_id), branch_id = VALUES(branch_id),broker_name = VALUES(broker_name),"
				+ "samco_token = VALUES (samco_token), product_type = VALUES (product_type), weblink = values (weblink),"
				+ "exchange_array = VALUES (exchange_array)";

		Connection conn = null;
		PreparedStatement ps = null;

		boolean isSessionUpdated = false;

		try
		{
			conn = pool.getConnection();
			ps = conn.prepareStatement(Query);

			ps.setString(1, session.getUserId());
			ps.setString(2, session.getSessionID());
			ps.setString(3, session.getAppID());
			ps.setString(4, sBuildName);
			ps.setString(5, APP_CONSTANT.LOGIN_TYPE);
			ps.setString(6, session.getUserType());
			ps.setString(7, session.getUserStage());
			ps.setString(8, session.getJsession());
			ps.setString(9, session.getJkey());
			ps.setString(10, session.getProductAlias());
			ps.setString(11, session.getAccountID());
			ps.setString(12, session.getBranchID());
			ps.setString(13, session.getBrokerName());
			ps.setString(14, session.getSamcoToken());
			ps.setString(15, session.getProductType());
			ps.setString(16, session.getjWeblinkURL());
			ps.setString(17, session.getExchangeArray());

			int _result = ps.executeUpdate();

			if (_result > 0)
				isSessionUpdated = true;

		}
		finally
		{
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}

		return isSessionUpdated;
	}

	public static boolean updateSamcoTokenToDB(DBPool pool, String sSamcoToken, String sUserID)
			throws SQLException, Exception
	{

		String Query = "UPDATE USER_SESSION SET samco_token = ? where user_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;

		boolean isSamcoTokenUpdated = false;

		try
		{
			conn = pool.getConnection();
			ps = conn.prepareStatement(Query);

			ps.setString(1, sSamcoToken);
			ps.setString(2, sUserID);


			int _result = ps.executeUpdate();

			if (_result > 0)
				isSamcoTokenUpdated = true;

		} 
		finally
		{
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}

		return isSamcoTokenUpdated;
	}

	public static Session getSession(DBPool pool, String sessionID) throws SQLException, SamcoException, JSONException
	{

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet res = null;

		String query = "{call session_validation(?,?,?)}";

		Session session = null;

		try
		{
			conn = pool.getConnection();
			cstmt = conn.prepareCall(query);
			cstmt.setString(1, sessionID);
			cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);

			cstmt.executeUpdate();

			if (cstmt.getInt(2) == 0)
			{

				res = cstmt.getResultSet();

				while (res.next())
				{

					session = new Session();

					session.setSessionID(sessionID);
					session.setUserId(res.getString(DBConstants.USER_ID));
					session.setBuild(res.getString(DBConstants.BUILD));
					session.setAppID(res.getString(DBConstants.APP_ID));
					session.setUserType(res.getString(DBConstants.USER_TYPE));
					session.setJsession(res.getString(DBConstants.OMNESYS_JSESSION));
					session.setJkey(res.getString(DBConstants.OMNESYS_JKEY));
					session.setUserStage(res.getString(DBConstants.USER_STAGE));
					session.setProductAlias(res.getString(DBConstants.PRODUCT_ALIAS));
					session.setIsEnableTransPass(res.getString(DBConstants.IS_ENABLE_TRANSACTION_PASSWORD));
					session.setAccountID(res.getString(DBConstants.ACCOUNT_ID));
					session.setBranchID(res.getString(DBConstants.BRANCH_ID));
					session.setBrokerName(res.getString(DBConstants.BROKER_NAME));
					session.setSamcoToken(res.getString(DBConstants.SAMCO_TOKEN));
					session.setProductType(res.getString(DBConstants.PRODUCT_TYPE));
					session.setExchangeArray(res.getString(DBConstants.EXCHANGE_ARRAY));		

					String weblink = res.getString(DBConstants.WEBLINK);
					try
					{
						JSONArray jLinks = new JSONArray(weblink);
						for (int i = 0; i < jLinks.length(); i++)
						{
							JSONObject jTemp = jLinks.getJSONObject(i);
							String sName = jTemp.getString("name");
							if (sName.equalsIgnoreCase("payin")) 
							{
								session.setPaying(jTemp.toString());
							} else if (sName.equalsIgnoreCase("payout"))
							{
								session.setPayout(jTemp.toString());
							} else if (sName.equalsIgnoreCase("backoffice"))
							{
								session.setBackoffice(jTemp.toString());
							}
						}

					}
					catch (JSONException e)
					{
						log.debug(e);
					}
				}
			} 
			else 
			{
				log.debug(cstmt.getString(3));
				return null;
			}

		} 
		finally 
		{

			Helper.closeResultSet(res);
			Helper.closeStatement(cstmt);
			Helper.closeConnection(conn);
		}

		return session;
	}

	public static Session validateSession(DBPool pool, String sessionID) throws Exception
	{

		Session session = getSession(pool, sessionID);

		if (session == null) 
		{
			log.debug("==> Invalid session");
		}

		return session;
	}

	public static Session validateSessAndAppID(DBPool pool, String sessionID, String appID) throws Exception 
	{
		Session session = validateSession(pool, sessionID);

		if (session != null && (false == session.getAppID().equals(appID)))
		{
			logoutUser(pool, sessionID);
			throw new SamcoException(INFO_IDS.INVALID_SESSION);
		}

		return session;
	}

}