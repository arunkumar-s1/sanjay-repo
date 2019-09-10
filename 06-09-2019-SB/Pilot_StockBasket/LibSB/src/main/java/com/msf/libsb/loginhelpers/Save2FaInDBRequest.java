package com.msf.libsb.loginhelpers;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.me.JSONArray;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.utils.helper.Helper;

public class Save2FaInDBRequest extends BaseRequest
{
	private DBPool pool = null;

	//private static Logger log = Logger.getLogger(Save2FaInDBRequest.class);

	public Save2FaInDBRequest(DBPool pool)
	{
		this.pool = pool;
	}

	public void setUser(String sUid) throws Exception
	{
		this.put(APP_CONSTANT.USER_ID, sUid);
	}

	public void setAnswers(JSONArray data) throws Exception
	{
		this.put(APP_CONSTANT.ANSWERS, data);
	}

	@Override
	public BaseResponse postRequest() throws Exception 
	{
		Connection conn = null;
		PreparedStatement ps = null;

		try
		{
			StringBuilder sb = new StringBuilder();
			String userId=this.getString(APP_CONSTANT.USER_ID);
			String sQuery = "update USERS_2FA set answer = ? where user_id = '"+userId+"' and value = ?";
				
			JSONArray answers=this.getJSONArray(APP_CONSTANT.ANSWERS);
			conn = this.pool.getSamcoConnection();
			ps = conn.prepareStatement(sQuery);
			for (int i = 0; i < answers.length(); i++)
			{
				String qid=answers.getJSONObject(i).getString(APP_CONSTANT.QUESTION_ID);
				String answer=answers.getJSONObject(i).getString(APP_CONSTANT.ANSWER).toLowerCase();
				ps.setString(1, answer);
				ps.setString(2,qid);
				ps.addBatch();
				sb.append("'" + qid + "',");
			}
			
			sb.setLength(sb.length()-1);
			ps.executeBatch();
			ps.close();
			
			sQuery = "delete from USERS_2FA where user_id = ? and value not in (" + sb.toString() + ")";
			ps = conn.prepareStatement(sQuery);
			ps.setString(1,userId);
			ps.execute();		
		} 
		finally
		{
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
		
		Save2FaInDBResponse rep = new Save2FaInDBResponse();
		rep.put(APP_CONSTANT.MESSAGE, "Successfully updated your 2fa answers");
		return rep;
	}
}
