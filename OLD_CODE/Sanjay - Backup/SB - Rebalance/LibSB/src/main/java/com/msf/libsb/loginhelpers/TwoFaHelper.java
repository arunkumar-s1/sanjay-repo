package com.msf.libsb.loginhelpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class TwoFaHelper
{

	private DBPool pool = null;
	private static Logger log = Logger.getLogger(TwoFaHelper.class);
	private String[] indexes = null;
	private String[] questions = null;
	private String[] hashes = null;
	String sUserID = null;

	public TwoFaHelper(DBPool pool, String sUid) 
	{
		this.pool = pool;
		this.sUserID = sUid;
	}

	public void setActualIndex(String sIndex)
	{
		this.indexes = sIndex.split("\\|");
		this.hashes = new String[this.indexes.length];
		int i = 0;
		for (String s: this.indexes) 
		{

			this.hashes[i++] = Integer.toString(s.hashCode());
		}
	}

	public void setActualQuestions(String sQuestions)
	{
		this.questions = sQuestions.split("\\|");
	}

	public void updateQuestionsInDB() throws Exception 
	{
		Connection conn = null;
		PreparedStatement ps = null;

		try
		{
			conn = this.pool.getConnection();

			String sQuery = "delete from samco.USERS_2FA where user_id = ?";
			ps = conn.prepareStatement(sQuery);
			ps.setString(1, this.sUserID);
			ps.execute();
			ps.close();

			sQuery = "insert into samco.USERS_2FA (user_id, question, id, value) values (?,?,?,?)";
			ps = conn.prepareStatement(sQuery);

			for (int i =0; i < this.indexes.length; i++)
			{

				ps.setString(1, this.sUserID);
				ps.setString(2, this.questions[i]);
				ps.setString(3, this.indexes[i]);
				ps.setString(4, this.hashes[i]);
				ps.addBatch();
			}

			ps.executeBatch();
		} 
		finally
		{
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
	}

	public boolean checkAnswerInDb(Map<String, String> mAns) throws Exception
	{		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;

		try
		{
			String sQuery = "select id,answer from samco.USERS_2FA where user_id = ?";
			conn = this.pool.getConnection();
			ps = conn.prepareStatement(sQuery);
			ps.setString(1, this.sUserID);

			res = ps.executeQuery();
			while (res.next())
			{
				mAns.put(res.getString(DBConstants.ID), res.getString(DBConstants.ANSWER));
			}

			if (mAns.size() == 0)
				return false;
		} 
		finally 
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
		return true;
	}

	public JSONArray getQuestions() throws Exception
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		JSONArray jQuestions = new JSONArray();

		try
		{
			String sQuery = "select value,question from samco.USERS_2FA where user_id = ?";
			
			conn = this.pool.getConnection();
			ps = conn.prepareStatement(sQuery);
			ps.setString(1, this.sUserID);

			res = ps.executeQuery();
			
			while (res.next())
			{
				JSONObject temp = new JSONObject();
				temp.put(APP_CONSTANT.QUESTION_ID, res.getString(DBConstants.VALUE));
				temp.put(APP_CONSTANT.QUESTION, res.getString(DBConstants.QUESTION));
				temp.put(APP_CONSTANT.IS_ENABLED, false);
				jQuestions.put(temp);
			}

		} 
		finally 
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}

		if (jQuestions.length() == 0 || jQuestions.length() == 5)
			throw new SamcoException(INFO_IDS.SAVE_2FA_ALREADY_SET);

		return jQuestions;
	}

	public String getAnswersFromDB() throws Exception
	{

		StringBuilder sb = new StringBuilder();
		for (String sid : this.indexes) 
		{
			sb.append("'" + sid + "',");
		}
		sb.setLength(sb.length()-1);
		String sQuery = "select id,answer from samco.USERS_2FA where user_id = ? and id in (" + sb.toString() + ")";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;

		try 
		{
			conn = this.pool.getConnection();
			ps = conn.prepareStatement(sQuery);
			ps.setString(1, this.sUserID);
			//log.debug(ps);
			res = ps.executeQuery();
			StringBuilder sblist = new StringBuilder();

			Map<String, String> mAns = new HashMap<>();
			while (res.next()) 
			{
				mAns.put(res.getString(DBConstants.ID), res.getString(DBConstants.ANSWER));
			}

			if (mAns.size() != this.indexes.length)
				throw new SamcoException(INFO_IDS.SAVE_2FA_ALREADY_SET);

			for (String sid : this.indexes)
			{
				sblist.append(mAns.get(sid));
				sblist.append("-");
			}

			sblist.setLength(sblist.length()-1);

			return sblist.toString();
		} 
		finally 
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
	}

	public String getQuestionIndex() throws Exception 
	{
		StringBuilder sb = new StringBuilder();
		for (String s : this.indexes) 
		{
			sb.append(s);
			sb.append("-");
		}
		sb.setLength(sb.length()-1);
		return sb.toString();
	}

}
