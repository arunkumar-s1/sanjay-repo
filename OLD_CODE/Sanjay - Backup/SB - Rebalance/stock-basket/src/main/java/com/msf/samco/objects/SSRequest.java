package com.msf.samco.objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.utils.helper.Session;
import com.msf.log.Logger;
import com.msf.objects.MSFRequest;
import com.msf.utils.helper.Helper;

public class SSRequest extends MSFRequest
{

	private String userID = null;
	private Session session;// msf session object
	private String loginType = null;
	private HttpServletRequest httpRequest;
	private SSAuditObject auditObj;
	private String userType = null;

	private static Logger log = Logger.getLogger(SSRequest.class);

	public String getSessionID(boolean isActive) throws Exception
	{

		String jSessionID = null;

		if (httpRequest != null) 
		{
			Cookie[] cookies = httpRequest.getCookies();

			if (cookies != null)
			{
				for (Cookie c : cookies)
				{
					if (c.getName()
							.equalsIgnoreCase(APP_CONSTANT.SESSION_ID)) 
					{
						jSessionID = c.getValue();
						break;
					}
				}
			}
		}
		if (isActive) 
		{
			jSessionID = Helper.MD5sum(this.toS() + System.currentTimeMillis());
		}

		if(jSessionID==null)
		{
			jSessionID = this.getJSessionID();
			log.debug("sessionID from request"+jSessionID);
		}

		if (jSessionID == null || jSessionID.length() == 0)
			return null;

		return jSessionID;
	}

	public SSRequest(String request) throws JSONException 
	{
		super(request);
	}

	public HttpServletRequest getHttpRequest() 
	{
		return httpRequest;
	}

	public void setHttpRequest(HttpServletRequest httpRequest) 
	{
		this.httpRequest = httpRequest;
	}

	public SSAuditObject getAuditObj()
	{
		return auditObj;
	}

	public void setAuditObj(SSAuditObject auditObj) 
	{
		this.auditObj = auditObj;
	}

	public String getUserID() 
	{
		return userID;
	}

	public void setUserID(String userID) 
	{
		this.userID = userID;
	}

	public void setSession(Session session)
	{
		this.session = session;
	}

	public Session getSession()
	{
		return session;
	}

	public String getLoginType() 
	{
		return loginType;
	}

	public boolean isValidAppID(String app_id)
	{
		if (app_id != null)
			return this.getAppID().equals(app_id);
		else
			return false;
	}

	public boolean isValidUserID(String userID) 
	{
		if (userID != null)
			return this.userID.equals(userID);
		else
			return false;
	}

	public void setLoginType(String loginType)
	{
		this.loginType = loginType;
	}

	public String getJSessionID() throws JSONException 
	{
		//this is implementing for web, taking sessionID from request
		String jsessionID = this.getRequest().optString(APP_CONSTANT.LOGIN_SESSION_ID,null);
		return jsessionID;
	}

	public void setUserType(String type) 
	{
		this.userType = type;
	}

	public String getUserType()
	{
		return userType;
	}
	public String getFromData(String key) throws InvalidRequestParameter
	{
		try
		{
			return super.getData().getString(key);
		}
		catch (JSONException e) 
		{
			throw new InvalidRequestParameter(key+" key not found in request");
		}
	}
	public Double getDoubleFromData(String key) throws InvalidRequestParameter
	{
		try
		{
			return super.getData().getDouble(key);
		}
		catch (JSONException e) 
		{
			throw new InvalidRequestParameter(key+" key not found in request");
		}
	}
	public JSONArray getArrayFromData(String key) throws InvalidRequestParameter
	{
		try
		{
			return super.getData().getJSONArray(key);
		}
		catch (JSONException e) 
		{
			throw new InvalidRequestParameter(key+" key not found in request");
		}
	}
	public JSONObject getJSONFromData(String key) throws InvalidRequestParameter
	{
		try
		{
			return super.getData().getJSONObject(key);
		}
		catch (JSONException e) 
		{
			throw new InvalidRequestParameter(key+" key not found in request");
		}
	}
	public int getIntFromData(String key) throws InvalidRequestParameter
	{
		try
		{
			return super.getData().getInt(key);
		}
		catch (JSONException e) 
		{
			throw new InvalidRequestParameter(key+" key not found in request");
		}
	}

	public int optIntFromData(String key,int defaultValue) throws InvalidRequestParameter
	{
		try
		{
			return super.getData().optInt(key,defaultValue);
		}
		catch (Exception e) 
		{
			throw new InvalidRequestParameter(key+" key invalid in request");
		}
	}
	public Double optDoubleFromData(String key) throws InvalidRequestParameter
	{
		try
		{
			return (Double) super.getData().opt(key);
		}
		catch (Exception e) 
		{
			throw new InvalidRequestParameter(key+" key invalid in request");
		}
	}
}
