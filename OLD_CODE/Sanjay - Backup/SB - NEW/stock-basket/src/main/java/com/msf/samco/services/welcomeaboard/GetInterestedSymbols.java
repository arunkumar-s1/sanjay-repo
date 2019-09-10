package com.msf.samco.services.welcomeaboard;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.welcome.WelcomeAboard;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class GetInterestedSymbols extends SessionServlet 
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{

		JSONArray jList = null;

		try 
		{
			JSONObject data = ssRequest.getData();
			jList = data.getJSONArray(APP_CONSTANT.TYPE);
		} 
		catch (JSONException e)
		{
			throw new InvalidRequestParameter("");
		}

		if (jList == null || jList.length() == 0)
			throw new InvalidRequestParameter("");

		WelcomeAboard welcome = new WelcomeAboard(DBConnection.getInstance());

		JSONObject resp = welcome.getInterestSymbols(jList);
		ssResponse.addToData(APP_CONSTANT.NUMBER_OF_RECORDS, Integer.toString(resp.getInt(APP_CONSTANT.NUMBER_OF_RECORDS)));
		resp.remove(APP_CONSTANT.NUMBER_OF_RECORDS);
		ssResponse.addToData(APP_CONSTANT.SYMBOLS, resp);

	}

	@Override
	protected String getSvcName() 
	{
		return "GetInterestedSymbols";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "Welcome";
	}

	@Override
	protected String getSvcVersion()
	{
		return "1.0.0";
	}

}
