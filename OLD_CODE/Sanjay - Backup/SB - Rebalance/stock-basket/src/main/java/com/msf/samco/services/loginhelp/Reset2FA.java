package com.msf.samco.services.loginhelp;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.loginhelpers.Reset2FARequest;
import com.msf.libsb.loginhelpers.Reset2FAResponse;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.keygeneration.KeyGenerator;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class Reset2FA extends BaseServlet 
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		
		String sUserID = ssRequest.getFromData(APP_CONSTANT.USER_ID).toUpperCase();
		String sEmailID = ssRequest.getFromData(APP_CONSTANT.EMAIL_ID);
		String sPanNumber = ssRequest.getFromData(APP_CONSTANT.PAN_NUMBER).toUpperCase();

		KeyGenerator keyGen = new KeyGenerator(sUserID);
		String sJsession = keyGen.getJSession();
		String sJkey = keyGen.getJKey();

		String sURL = AppConfig.getConfigValue("reset_2fa_url");
		Reset2FARequest resetReq = new Reset2FARequest(sURL);
		resetReq.setSession(sJsession);
		resetReq.setKey(sJkey);
		resetReq.setUserID(sUserID);
		resetReq.setEmail(sEmailID);
		resetReq.setPan(sPanNumber);

		Reset2FAResponse resetRes = (Reset2FAResponse) resetReq.postRequest();
		
		if (!resetRes.isSuccessResponse()) throw new SamcoException(resetRes.getErrorID());
		
		ssResponse.addToData(APP_CONSTANT.MESSAGE, "2fa Resetted sucessfully.");
	}

	@Override
	protected String getSvcName() 
	{
		return "Reset2FA";
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

