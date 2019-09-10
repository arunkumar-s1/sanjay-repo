package com.msf.samco.services.logout;


import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.logout.LogOutRequest;
import com.msf.libsb.logout.LogOutResponse;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.Session;
import com.msf.libsb.utils.helper.SessionHelper;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class LogOut extends SessionServlet 
{

	private static final long serialVersionUID = 1L;

	//private static Logger log = Logger.getLogger(LogOut.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		Session session = ssRequest.getSession();

		String sURL = AppConfig.getConfigValue("logout_url");
		LogOutRequest logoutReq = new LogOutRequest(sURL);
		logoutReq.setSession(session.getJsession());
		logoutReq.setKey(session.getJkey());
		logoutReq.setUserID(session.getUserId());

		LogOutResponse logoutRes = (LogOutResponse) logoutReq.postRequest();

		if (logoutRes.isSuccessResponse())
		{
			SessionHelper.logoutUser(DBConnection.getInstance(), session.getSessionID());
			ssResponse.addToData(APP_CONSTANT.MESSAGE, "User has successfully logged out");
		} 
		else 
		{
			throw new SamcoException(logoutRes.getErrorID());
		}
	}

	@Override
	protected String getSvcName()
	{
		return "EndSession";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "Logout";
	}

	@Override
	protected String getSvcVersion()
	{
		return "1.0.0";
	}

}