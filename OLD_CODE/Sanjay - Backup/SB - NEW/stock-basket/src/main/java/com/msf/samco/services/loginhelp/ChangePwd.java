package com.msf.samco.services.loginhelp;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.appconfig.InfoMessage;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.loginhelpers.ChangePasswordRequest;
import com.msf.libsb.loginhelpers.ChangePasswordResponse;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.Session;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class ChangePwd extends SessionServlet 
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void logRequest(SSRequest ssRequest) 
	{
		try 
		{
			ssRequest.maskValueInData(APP_CONSTANT.OLD_PASSWORD);
			ssRequest.maskValueInData(APP_CONSTANT.NEW_PASSWORD);
		} 
		catch (JSONException e){}
		super.logRequest(ssRequest);
	}

	@Override
	protected String getSvcName()
	{
		return "ChangePassword";
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
	
	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		String sUserID = "";
		String sOldPwd = "";
		String sNewPwd = "";

		try 
		{

			JSONObject dataObject = ssRequest.getData();

			sUserID = ssRequest.getSession().getUserId();

			sOldPwd = dataObject.getString(APP_CONSTANT.OLD_PASSWORD);
			sNewPwd = dataObject.getString(APP_CONSTANT.NEW_PASSWORD);

		} 
		catch (JSONException e) 
		{
			throw new InvalidRequestParameter(e.getMessage());
		}

		if (sUserID.equals("") || sOldPwd.equals("") || sNewPwd.equals(""))
			throw new InvalidRequestParameter("");

		Session session = ssRequest.getSession();
		String sURL = AppConfig.getConfigValue("change_pwd_url");
		ChangePasswordRequest chPwdReq = new ChangePasswordRequest(sURL);
		chPwdReq.setSession(session.getJsession());
		chPwdReq.setKey(session.getJkey());
		chPwdReq.setUserID(sUserID);
		chPwdReq.setOldPwd(sOldPwd);
		chPwdReq.setNewPwd(sNewPwd);
		chPwdReq.setIsTxChange("NO");
		chPwdReq.setOldTxPwd("");
		chPwdReq.setSetNewTxPwd("");

		ChangePasswordResponse chPwdRes = (ChangePasswordResponse) chPwdReq.postRequest();

		if (chPwdRes.isSuccessResponse())
		{
			ssResponse.addToData(APP_CONSTANT.MESSAGE,InfoMessage.getInfoMSG("loginhelp.password.change_password_success"));
		} 
		else 
		{
			throw new SamcoException(chPwdRes.getErrorID());
		}
	}
}
