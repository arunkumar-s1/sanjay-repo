package com.msf.samco.services.welcomeaboard;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;
import com.msf.utils.helper.Helper;

public class GetOTPVerificationStatus extends BaseServlet 
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		JSONObject jDataObject = null;
		try
		{
			jDataObject = ssRequest.getData();
			String sAppID = ssRequest.getAppID();
			String sMobileNumber = jDataObject.getString(APP_CONSTANT.MOBILE);
			String sLoginID = jDataObject.getString("loginID");
			boolean isOTPVerified = false;
			isOTPVerified = jDataObject.getBoolean(APP_CONSTANT.IS_OTP_VERIFIED);
			if (isOTPVerified)
			{
				ssResponse.addToData(APP_CONSTANT.STATUS, "success");
				UpdateMobileNumberOTP(DBConnection.getInstance(), sMobileNumber, sLoginID);
			} 
			else 
			{
				throw new SamcoException(INFO_IDS.REQUEST_FAILED);
			}
		} 
		catch (Exception e) 
		{
			throw new SamcoException(INFO_IDS.REQUEST_FAILED);
		}

	}

	private void UpdateMobileNumberOTP(DBPool pool, String sMobileNumber, String sLoginID) throws Exception
	{
		
		Connection conn = null;
		PreparedStatement ps = null;
		String sQuery = "";

		try
		{

			conn = pool.getConnection();
			sQuery = "UPDATE GUEST_USER_SESSION SET otp_needed = ?, mobile_number = ? WHERE login_id = ?";
			ps = conn.prepareStatement(sQuery);
			ps.setString(1, "NO");
			ps.setString(2, sMobileNumber);
			ps.setString(3, sLoginID);
			ps.executeUpdate();

		} 
		finally
		{
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);

		}
	}

	@Override
	protected String getSvcName()
	{
		return "GetOTPVerificationStatus";
	}

	@Override
	protected String getSvcGroup()
	{
		return "Guest";
	}

	@Override
	protected String getSvcVersion() 
	{
		return "1.0.0";
	}

}
