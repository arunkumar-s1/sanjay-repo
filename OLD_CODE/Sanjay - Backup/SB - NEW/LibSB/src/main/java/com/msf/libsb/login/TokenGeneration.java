package com.msf.libsb.login;

import org.json.me.JSONObject;

import com.msf.libsb.base.HttpHelper;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;

public class TokenGeneration 
{

	private static final Logger log = Logger.getLogger(TokenGeneration.class);

	private String sTokenGenerationURL = null;
	private String sTokenGenerationField = null;
	private HttpHelper httpHelper;
	String sToken = null;
	String sDob = null;
	String sPan = null;
	String sEmail = null;

	public void setTokenGenerationURL(String sURL, String sField) 
	{
		sTokenGenerationURL = sURL;
		sTokenGenerationField = sField;
	}

	public String getUserID() 
	{
		return "";
	}

	public void process() throws Exception 
	{
		if (sTokenGenerationURL == null)
			throw new SamcoException(INFO_IDS.INVALID_REQUEST_PARAM);

		httpHelper = new HttpHelper();
		getTokenFromAPI();
		
		//log.debug("Generated token from samco = " + sToken);
	}

	private String postRequest(String sURL, String sFields) throws Exception
	{

		log.debug("Posting = " + sURL + " Fields = " + sFields);
		httpHelper.postRequest(sURL, sFields);
		String sResponse = httpHelper.getResponse();
		return sResponse;
	}

	public String getToken()
	{
		return sToken;
	}

	public String getDOB()
	{
		return sDob;
	}

	public String getMailID()
	{
		return sEmail;
	}

	public String getPanNumber()
	{
		return sPan;
	}

	public void getTokenFromAPI() throws Exception
	{

		String sTokenResponse = postRequest(sTokenGenerationURL, sTokenGenerationField);
		if (sTokenResponse.contains("<pre>"))
		{
			sTokenResponse = sTokenResponse.replaceAll("<pre>", "");
		}

		JSONObject jToken = new JSONObject(sTokenResponse);

		if (jToken.has("status"))
		{

			if (jToken.getString("status").equalsIgnoreCase("Error"))
				throw new SamcoException(INFO_IDS.INVALID_USER);

		}

		sToken = jToken.getString("data");
		sDob = jToken.getString("YOB");
		sPan = jToken.getString("PAN");
		sEmail = jToken.getString("EMAIL");

	}

}
