package com.msf.libsb;

import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.json.me.JSONObject;

/**
 * Base class for Samco response classes. It contains errorCode and
 * errorDesc, common to all responses.
 * 
 * @author Tiljo
 * 
 */
public class LibSamcoRequest 
{
	private static Logger log = Logger.getLogger(LibSamcoRequest.class);
//	private String sessionID;
	private String userID;
    private String accountID;
    private String broker;
    
    private String password;
    private String jsessionid;
    private String jKey;
    private String jData;
    private String appId;
    
    public LibSamcoRequest() 
    {		
	}

    public String frameRequest (String session, String jkey,JSONObject data) throws Exception
    {
 
    	String fields = "jsessionid=" + session + "&jData=" + URLEncoder.encode(data.toString(), "UTF-8") + "&jKey=" + jkey +"";
    	return fields;
    }
	public String getSessionID() 
	{
		return jsessionid;
	}

	public void setSessionID(String sessionID)
	{
		this.jsessionid = sessionID;
	}

	public String getKey()
	{
		return jKey;
	}

	public void setKey(String key)
	{
		this.jKey = key;
	}
	
	public String getAppId() 
	{
		return appId;
	}

	public void setAppId(String appid)
	{

		this.appId = appid;
	}

	public void setUserID(String user)
	{
		this.userID = user;
	}
	public String getUserID()
	{
		return userID;
	}

	public void setPassword(String pwd)
	{
		this.password = pwd;
	}
	public String getPassword()
	{
		return password;
	}
	
	public String getAccountID()
	{
		return accountID;
	}

	public void setAccountID(String accountID)
	{
		this.accountID = accountID;
	}

	public String getBroker() 
	{
		return broker;
	}

	public void setBroker(String broker)
	{
		this.broker = broker;
	}

}
