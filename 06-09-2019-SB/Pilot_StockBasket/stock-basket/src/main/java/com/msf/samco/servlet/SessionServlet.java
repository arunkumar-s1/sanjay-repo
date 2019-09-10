package com.msf.samco.servlet;

import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.Session;
import com.msf.libsb.utils.helper.SessionHelper;
import com.msf.log.Logger;
import com.msf.samco.objects.SSAuditObject;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;

public abstract class SessionServlet extends BaseServlet 
{
	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(SessionServlet.class);

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		JSONObject dataObj = ssRequest.getData();
		String sessionID;

		if (dataObj.has("sessionID")) 
		{
			sessionID = (String) ssRequest.getData().get("sessionID");
		} 
		else 
		{
			sessionID = ssRequest.getSessionID(false);
		}
		if (sessionID == null)
		{
			throw new SamcoException(INFO_IDS.INVALID_SESSION);

		}

		if (isValidSession(sessionID, ssRequest)) 
		{
			Session dataSession = ssRequest.getSession();

			if (isValidUserStage(getStage(), dataSession.getUserStage())) 
			{
				ssRequest.setUserType(APP_CONSTANT.SAMCO_USER);
				doPostProcessRequest(ssRequest, ssResponse);
			} 
			else 
			{
				SessionHelper.logoutUser(DBConnection.getInstance(), sessionID);
				throw new SamcoException(INFO_IDS.INVALID_SESSION);
			}

		} 
		else 
		{
			throw new SamcoException(INFO_IDS.INVALID_SESSION);
		}

	}

	private boolean isValidUserStage(String[] stage, String userStage) 
	{
		boolean isValidStage = false;

		for (String s : stage) 
		{
			if (s.equalsIgnoreCase(userStage))
			{
				isValidStage = true;
				break;
			}
		}

		return isValidStage;
	}

	protected boolean isValidSession(String jSessionID, SSRequest ssRequest) throws Exception 
	{
		Session session = SessionHelper.validateSessAndAppID(DBConnection.getInstance(), jSessionID,
				ssRequest.getAppID());

		if (session != null) 
		{
			ssRequest.setSession(session);
			return true;
		}
		else 
		{
			return false;
		}
	}

	protected boolean isValidAppID(SSRequest ssRequest, SSAuditObject auditPlatform) 
	{
		return true;
	}

	abstract protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception;
}