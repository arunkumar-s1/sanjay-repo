package com.msf.samco.services.welcomeaboard;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.utils.helper.Session;
import com.msf.libsb.welcome.WelcomeAboard;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class SetUserProfile extends SessionServlet
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		Session session = ssRequest.getSession();
		String sUid = session.getUserId();
		String sType = null;

		try
		{
			JSONObject data = ssRequest.getData();
			sType = data.getString(APP_CONSTANT.TYPE);
		} 
		catch (JSONException e)
		{
			throw new InvalidRequestParameter("");
		}

		if (sType == null || sType.length() == 0)
			throw new InvalidRequestParameter("");

		WelcomeAboard welcome = new WelcomeAboard(DBConnection.getInstance());
		JSONObject jPreference = welcome.setProfile(sUid, sType);

		ssResponse.addToData(APP_CONSTANT.FILTER_ORDER_TYPE_PREFERENCES, jPreference.getString(APP_CONSTANT.FILTER_ORDER_TYPE_PREFERENCES));
		ssResponse.addToData(APP_CONSTANT.FILTER_PRODUCT_TYPE_PREFERENCES, jPreference.getJSONObject(APP_CONSTANT.FILTER_PRODUCT_TYPE_PREFERENCES));
	}

	@Override
	protected String getSvcName() 
	{
		return "SetUserProfile";
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
