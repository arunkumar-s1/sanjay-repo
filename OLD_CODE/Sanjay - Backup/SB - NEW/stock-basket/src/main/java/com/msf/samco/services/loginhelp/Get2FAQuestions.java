package com.msf.samco.services.loginhelp;

import java.util.Map;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.loginhelpers.TwoFaHelper;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class Get2FAQuestions extends BaseServlet 
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		String userId = ssRequest.getFromData(APP_CONSTANT.USER_ID);

		if (userId == null || userId.length() == 0)
			throw new InvalidRequestParameter("");

		TwoFaHelper twoFaHelper = new TwoFaHelper(DBConnection.getInstance(), userId);
		JSONArray jQuestions = twoFaHelper.getQuestions();

		ssResponse.addToData(APP_CONSTANT.QUESTIONS, jQuestions);
	}

	@Override
	protected String getSvcName() 
	{
		return "Get2FAQuestions";
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
