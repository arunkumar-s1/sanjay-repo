package com.msf.libsb.loginhelpers;

import org.json.me.JSONObject;

import com.msf.libsb.base.HttpHelper;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;

public class ForgotuserID 
{
	
	private static final Logger log = Logger.getLogger(ForgotuserID.class);
	
	private String sAuthURL = null;
	private String sAuthField = null;
	private String sIDUrl = null;
	private String sIDField = null;
	private String sUID = null;
	
	private HttpHelper httpHelper;
	
	public void setAuthenticationURL(String sURL, String sField)
	{
		sAuthURL = sURL;
		sAuthField = sField;
	}
	
	public void setUserURL(String sURL, String sField) 
	{
		sIDUrl = sURL;
		sIDField = sField;
	}
	
	public String getUserID()
	{
		return sUID;
	}
	
	public void process() throws SamcoException 
	{
		if (sAuthURL == null || sIDUrl == null) 
			throw new SamcoException(INFO_IDS.INVALID_REQUEST_PARAM);
		
		httpHelper = new HttpHelper();
		AuthenticateUser auth = new AuthenticateUser();
		String sToken = auth.getToken();
		
		if (sToken != null) 
		{
			FetchID fetchID = new FetchID(sToken);
			sUID = fetchID.getClientID();
			
			if (sUID == null) 
			{
				throw new SamcoException(INFO_IDS.FORGOT_USERID_FAILED);
			}			
		} 
		else 
		{
			throw new SamcoException(INFO_IDS.FORGOT_USERID_FAILED);
		}		
	}
	
	private String postRequest(String sURL, String sFields) throws Exception
	{
		log.debug("Posting = " + sURL + " Fields = " + sFields);
		httpHelper.postRequest(sURL, sFields);
		String sResponse = httpHelper.getResponse();
		return sResponse;
	}
	
	private class AuthenticateUser
	{
		
		private String token = null;
		
		public AuthenticateUser() 
		{
			
			try
			{
				String authResponse = postRequest(sAuthURL, sAuthField);
				
				if(authResponse.contains("<pre>"))
				{
					authResponse = authResponse.replaceAll("<pre>", "");
				}
				
				JSONObject jAuth = new JSONObject(authResponse);
				token = jAuth.getString("token");
			} 
			catch (Exception e)
			{
				log.debug(e.getMessage());
			}
		}
		
		private String getToken() 
		{
			return token;
		}
		
	}
	
	private class FetchID 
	{
		
		private String userID = null;
		
		public FetchID(String sToken) 
		{
			try
			{
				String sIDRes = postRequest(sIDUrl, sIDField + sToken);
				
				if(sIDRes.contains("<pre>"))
				{
					sIDRes = sIDRes.replaceAll("<pre>", "");
				}
				
				JSONObject jObj = new JSONObject(sIDRes);
				String sStatus = jObj.getString("status");
				if (sStatus.equalsIgnoreCase("Success"))
				{
					userID = jObj.getString("ClntId");
				}
				
			} 
			catch (Exception e)
			{
				log.debug(e.getMessage());
			}
		}
		
		private String getClientID() 
		{
			return userID;
		}
	}
}
