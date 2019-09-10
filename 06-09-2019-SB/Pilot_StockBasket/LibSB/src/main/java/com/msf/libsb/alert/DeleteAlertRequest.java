package com.msf.libsb.alert;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.me.JSONArray;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.utils.helper.Helper;

public class DeleteAlertRequest extends BaseRequest {

private DBPool pool;
	
	public DeleteAlertRequest(DBPool pool) {
		super();
		this.pool = pool;
	}
	
	public void setAlertIDs( JSONArray alertList ) throws Exception
	{
		this.put(APP_CONSTANT.ALERT_ID, alertList);
	}

	@Override
	public BaseResponse postRequest() throws Exception {
		DeleteAlertResponse alertResponse = new DeleteAlertResponse();
		
		if( addRecord() && deleteAlert())
		{
			String infoID = sendToAlertEngine();
			if ( infoID.equals("0"))
				alertResponse.setResponseCode(APIConstants.SUCCESS_CODE);
			else {
				alertResponse.setResponseCode(0);
				alertResponse.setErrorMessage("Unable to process your request");
			}
		}
		else
		{
			alertResponse.setResponseCode(0);
			alertResponse.setErrorMessage("alert does not exist");
		}
		return alertResponse;
	}

	public String sendToAlertEngine() throws Exception 
	{
		String alertEngineAPIURL = AppConfig.getConfigValue("DeleteAlert_url");

		AlertEngineRequest alertEngineRequest = new AlertEngineRequest(
				alertEngineAPIURL);

		alertEngineRequest.setRequestObj(this);
		
		alertEngineRequest.setAction("del");

		AlertEngineResponse alertEngineResponse = (AlertEngineResponse) alertEngineRequest
				.postRequest();

		String respID = alertEngineResponse.getInfoID();

		return respID;
	}
	
	public boolean addRecord() throws Exception
	{
		Connection con = null;
		PreparedStatement pstmt = null;

		JSONArray alertList = this.getJSONArray(APP_CONSTANT.ALERT_ID);
		
		String Query = "INSERT INTO Alert.DELETEDALERT( USER_ID, SYM, CRITERIA_TYPE, CRITERIA_VALUE, "
				+ " APP_ID) SELECT USER_ID, SYM, CRITERIA_TYPE, CRITERIA_VALUE, APP_ID "
				+ " FROM Alert.ADDALERT WHERE ALERT_ID IN (";
		
		boolean foundMore = false;
		for( int i = 0; i< alertList.length(); i++ )
		{
			if( foundMore )
				Query += ",";
			else
				foundMore = true;
			
			Query += " ? ";
		}
		
			Query += " );";
		
		//log.info("SQL Query : " + Query);

		boolean isSuccess = false;

		try {
			con = pool.getConnection();
			pstmt = con.prepareStatement(Query);

			for( int i = 0; i < alertList.length(); i++)
				pstmt.setString(i+1, alertList.getString(i));

			if (pstmt.executeUpdate() > 0) 
				isSuccess = true;
			
		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(con);
		}
	
		return isSuccess;
	}
	
	public boolean deleteAlert() throws Exception
	{
		Connection con = null;
		PreparedStatement pstmt = null;

		JSONArray alertList = this.getJSONArray(APP_CONSTANT.ALERT_ID);
		
		String Query = "DELETE FROM Alert.ADDALERT WHERE ALERT_ID IN ( ";
		
		boolean foundMore = false;
		for( int i = 0; i< alertList.length(); i++ )
		{
			if( foundMore )
				Query += ",";
			else
				foundMore = true;
			
			Query += " ? ";
		}
		
			Query += " );";
		
		//log.info("SQL Query : " + Query);

		boolean isSuccess = false;

		try {
			con = pool.getConnection();
			pstmt = con.prepareStatement(Query);

			for( int i = 0; i < alertList.length(); i++)
				pstmt.setString(i+1, alertList.getString(i));
		
			if (pstmt.executeUpdate() > 0) 
				isSuccess = true;
			
		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(con);
		}
		
		return isSuccess;
		
	}
}
