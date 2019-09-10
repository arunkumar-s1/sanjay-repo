package com.msf.samco.services.welcomeaboard;

import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.libsb.utils.helper.Session;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class GetProductOrderPreference extends SessionServlet 
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{

		Session session = ssRequest.getSession();
		String sUserID = session.getUserId();
		JSONObject jPreference = SamcoHelper.checkProductAndOrderPreference(sUserID, DBConnection.getInstance());
		if (jPreference.getJSONObject(APP_CONSTANT.FILTER_PRODUCT_TYPE_PREFERENCES).length() == 0)
			ssResponse.addToData(APP_CONSTANT.IS_WELCOME_ABOARD, true);
		else 
			ssResponse.addToData(APP_CONSTANT.IS_WELCOME_ABOARD, false);

		ssResponse.addToData(APP_CONSTANT.FILTER_ORDER_TYPE_PREFERENCES, jPreference.getString(APP_CONSTANT.FILTER_ORDER_TYPE_PREFERENCES));
		ssResponse.addToData(APP_CONSTANT.FILTER_PRODUCT_TYPE_PREFERENCES, jPreference.getJSONObject(APP_CONSTANT.FILTER_PRODUCT_TYPE_PREFERENCES));
	}

	@Override
	protected String getSvcName() 
	{
		return "GetProductOrderPreference";
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
