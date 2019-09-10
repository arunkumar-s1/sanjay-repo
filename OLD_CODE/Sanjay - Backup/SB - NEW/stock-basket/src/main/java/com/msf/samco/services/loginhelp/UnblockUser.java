package com.msf.samco.services.loginhelp;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.loginhelpers.UnblockUserRequest;
import com.msf.libsb.loginhelpers.UnblockUserResponse;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.keygeneration.KeyGenerator;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class UnblockUser extends BaseServlet
{
	private static final long serialVersionUID = 1L;

	//private static Logger log = Logger.getLogger(UnblockUser.class);

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{
		String sUserID = ssRequest.getFromData(APP_CONSTANT.USER_ID).toUpperCase();
		String sEmailID = ssRequest.getFromData(APP_CONSTANT.EMAIL_ID);
		String sPanNumber = ssRequest.getFromData(APP_CONSTANT.PAN_NUMBER).toUpperCase();

		KeyGenerator keyGen = new KeyGenerator(sUserID);
		String sJsession = keyGen.getJSession();
		String sJkey = keyGen.getJKey();

		String sURL = AppConfig.getConfigValue("unblock_user_url");
		UnblockUserRequest unblockReq = new UnblockUserRequest(sURL);
		unblockReq.setSession(sJsession);
		unblockReq.setKey(sJkey);
		unblockReq.setUserID(sUserID);
		unblockReq.setEmail(sEmailID);
		unblockReq.setPan(sPanNumber);

		UnblockUserResponse unblockRes = (UnblockUserResponse) unblockReq.postRequest();
		if (!unblockRes.isSuccessResponse()) throw new SamcoException(unblockRes.getErrorID());
		
		ssResponse.addToData(APP_CONSTANT.MESSAGE, unblockRes.getResult());	}

	@Override
	protected String getSvcName() 
	{
		return "UnblockUser";
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
