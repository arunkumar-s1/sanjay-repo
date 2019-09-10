package com.msf.samco.objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.objects.MSFResponse;

public class SSResponse extends MSFResponse
{

	private HttpServletResponse httpResponse;

	public HttpServletResponse getHttpResponse() 
	{
		return httpResponse;
	}

	public void setHttpResponse(HttpServletResponse httpResponse)
	{
		this.httpResponse = httpResponse;
	}

	public SSResponse() throws JSONException 
	{
		super();
	}

	public SSResponse(SSRequest ssRequest) throws JSONException 
	{
		super(ssRequest);
		setAppID(ssRequest.getAppID());
		setServerTime(System.currentTimeMillis() + "");
	}

	public boolean infoIDEquals(String infoID)
	{
		return getInfoID().equals(infoID);
	}

	public Object getFromData(String key) throws JSONException 
	{
		return this.dataObj.get(key);
	}

	public JSONObject getDataObj()
	{
		return this.dataObj;
	}

	public JSONObject getResponseObj() 
	{
		try
		{
			return this.getJSONObject("response");
		} 
		catch (Exception e) 
		{
			return (new JSONObject());// This Scnerio will not come
		}

	}

	public void setSession(String jSessionID, String path) 
	{

		Cookie sessionCookie = new Cookie(APP_CONSTANT.SESSION_ID, jSessionID);
		sessionCookie.setPath(path + "/");
		httpResponse.addCookie(sessionCookie);
	}
}
