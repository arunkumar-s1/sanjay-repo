package com.msf.samco.services.loginhelp;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.appconfig.InfoMessage;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.loginhelpers.ForgotPasswordRequest;
import com.msf.libsb.loginhelpers.ForgotPasswordResponse;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.keygeneration.KeyGenerator;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class ForgotPassword extends BaseServlet 
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		
			String sUserID = ssRequest.getFromData(APP_CONSTANT.USER_ID).toUpperCase();
			String sEmailID = ssRequest.getFromData(APP_CONSTANT.EMAIL_ID);
			String sPanNumber = ssRequest.getFromData(APP_CONSTANT.PAN_NUMBER).toUpperCase();


		if (sUserID.equals("") || sEmailID.equals("") || sPanNumber.equals(""))
			throw new InvalidRequestParameter("");

		KeyGenerator keyGen = new KeyGenerator(sUserID);
		String sJsession = keyGen.getJSession();
		String sJkey = keyGen.getJKey();
		String sURL = AppConfig.getConfigValue("forgot_password_url");

		ForgotPasswordRequest forgotPwdReq = new ForgotPasswordRequest(sURL);
		forgotPwdReq.setSession(sJsession);
		forgotPwdReq.setKey(sJkey);
		forgotPwdReq.setUserID(sUserID);
		forgotPwdReq.setEmail(sEmailID);
		forgotPwdReq.setPan(sPanNumber);

		ForgotPasswordResponse ForgotPwdRes = (ForgotPasswordResponse) forgotPwdReq.postRequest();

		if (ForgotPwdRes.isSuccessResponse()) 
		{
			ssResponse.addToData(APP_CONSTANT.MESSAGE,InfoMessage.getInfoMSG("loginhelp.password.forget_password_success"));
		} 
		else 
		{
			throw new SamcoException(ForgotPwdRes.getErrorID());
		}
	}

	@Override
	protected String getSvcName() 
	{
		return "ForgotPassword";
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
