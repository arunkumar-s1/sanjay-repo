package com.msf.samco.services.loginhelp;

import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.loginhelpers.ForgotuserID;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class ForgotUserID extends BaseServlet 
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		String sEmailID;
		String sPanNumber;
		try 
		{
			JSONObject dataObject = ssRequest.getData();
			sEmailID = dataObject.getString("emailID");
			sPanNumber = dataObject.getString("panNo").toUpperCase();	
		} 
		catch (Exception e) 
		{
			throw new InvalidRequestParameter(e.getMessage());
		}

		ForgotuserID forgotID = new ForgotuserID();
		String sAuthURL = AppConfig.getConfigValue("forgot_user_id_auth");
		String sUserURL = AppConfig.getConfigValue("forgot_user_id_clientid");

		String sAuthFields = "user=" + "msfuser" + "&" + "pass=" + "MSFSamco";
		String sUserIDFields = "email=" + sEmailID + "&" + "pan=" + sPanNumber + "&" + "token=";

		forgotID.setAuthenticationURL(sAuthURL, sAuthFields);
		forgotID.setUserURL(sUserURL, sUserIDFields);

		forgotID.process();

		String sClientID = forgotID.getUserID();
		ssResponse.addToData(APP_CONSTANT.USER_ID, sClientID);
	}

	@Override
	protected String getSvcName() 
	{
		return "ForgotUserID";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "LoginHelp";
	}

	@Override
	protected String getSvcVersion()
	{
		return "1.0.0";
	}
}
