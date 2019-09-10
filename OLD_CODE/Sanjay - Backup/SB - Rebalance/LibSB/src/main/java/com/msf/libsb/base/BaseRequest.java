package com.msf.libsb.base;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.jmx.Monitor;
import com.msf.libsb.jmx.SamcoServiceBean;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;

public abstract class BaseRequest extends JSONObject 
{

	public static Logger log = Logger.getLogger(BaseRequest.class);

	protected HttpHelper httpHelper;
	protected String apiUrl = "";
	protected String sSession = "";
	protected String sKey = "";

	public BaseRequest() 
	{
		super();
	}

	public BaseRequest(String apiUrl) throws SamcoException
	{
		super();

		this.apiUrl = apiUrl;
		httpHelper = new HttpHelper();

	}

	public void setSession(String sSession)
	{
		this.sSession = sSession;
	}

	public void setKey(String skey)
	{
		this.sKey = skey;
	}

	public String getFields() throws Exception 
	{
		return "";
	}

	/**
	 * Thomson Reuters API uses fields appended with url, and keeps fields as
	 * empty
	 * 
	 * @return String
	 * @throws Exception
	 */
	private String TRFieldsAppender() throws Exception 
	{
		String sFields = "";

		log.debug("URL-DATA Appender : " + this.toString());

		if (this.length() != 0) 
		{
			sFields = "jsessionid=" + this.sSession + "&jData=" + URLEncoder.encode(this.toString(), "UTF-8") + "&jKey="
					+ this.sKey + "";
		} 
		else if (this.sKey.length() != 0) 
		{
			log.debug("API WITH EMPTY DATA + VALID JKEY & JSESSION");
			sFields = "jsessionid=" + this.sSession + "&jData=" + URLEncoder.encode(this.toString(), "UTF-8") + "&jKey="
					+ this.sKey + "";
		}
		return sFields;
	}

	/**
	 * Makes a post call to specified API
	 * 
	 * @return API response as String
	 * @throws SocketTimeoutException
	 * @throws SamcoException
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws Exception
	 */
	protected String postToApi() throws SocketTimeoutException, SamcoException, MalformedURLException,
	URISyntaxException, IOException, Exception
	{

		SamcoServiceBean trAPIBean = Monitor.getServiceBean(Monitor.TR_API);

		try
		{
			String sURLAppender = this.TRFieldsAppender();
			if (sURLAppender != null && sURLAppender != "")
				httpHelper.postRequest(apiUrl + "?" + sURLAppender, this.getFields());
			else
				httpHelper.postRequest(apiUrl, this.getFields());
		} 
		catch (Exception e) 
		{

			if (trAPIBean != null)
			{
				trAPIBean.setFailure(e.getMessage());
			}

			throw e;
		}

		if (httpHelper.getResponse() != null)
		{
			return httpHelper.getResponse();
		} 
		else
		{
			log.debug("@@@@@@ " + httpHelper.getReturnCode());
			if (httpHelper.getReturnCode() == 500) 
			{
				throw new SamcoException(INFO_IDS.INVALID_SESSION);
			}

			throw new SamcoException(INFO_IDS.RESPONSE_FAILURE);
		}

	}

	public String getRequestData() 
	{
		return this.toString();
	}

	public String getRequestToAPI()
			throws SocketTimeoutException, SamcoException, MalformedURLException, URISyntaxException, IOException
	{

		SamcoServiceBean trAPIBean = Monitor.getServiceBean(Monitor.TR_API);

		try 
		{

			httpHelper.getRequest(apiUrl);
		}
		catch (Exception e)
		{

			if (trAPIBean != null)
			{
				trAPIBean.setFailure(e.getMessage());
			}

			throw e;
		}

		if (httpHelper.getResponse() != null)
		{
			return httpHelper.getResponse();
		} 
		else
		{
			throw new SamcoException(INFO_IDS.RESPONSE_FAILURE);
		}
	}

	public abstract BaseResponse postRequest() throws Exception;

	public BaseResponse doOperations() throws Exception
	{
		return null;
	}

	public BaseArrayResponse doPostRequest() throws Exception
	{
		return null;
	}

	public BaseResponse doGetRequest() throws SocketTimeoutException, MalformedURLException, SamcoException,
	URISyntaxException, IOException, JSONException 
	{

		return null;
	}
}
