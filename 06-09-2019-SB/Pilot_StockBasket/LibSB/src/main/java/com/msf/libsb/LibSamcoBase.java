package com.msf.libsb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.api_req_res.APIRequestResponse;
import com.msf.api_req_res.APITypes;
import com.msf.api_req_res.GetAPIReqRes;
import com.msf.connections.http.HTTPConnection;
import com.msf.connections.http.HTTPSConnection;

import com.msf.log.Logger;

/**
 * Base class for Samco services. It contains constructor to initialize
 * parameters, function to send webservice request and set API request and
 * response. It implements the GetAPIReqRes interface that is used to return the
 * API part.
 * 
 * @author Kailash
 * 
 */
public class LibSamcoBase implements GetAPIReqRes 
{
	private static Logger log = Logger.getLogger(LibSamcoBase.class);
	protected String url;
	protected Integer timeout;
	protected String method;
	protected String certName = "";
	protected APIRequestResponse apiReqRes;


	/**
	 * Constructor
	 * 
	 * @param url
	 * 
	 * @param timeout
	 * 
	 * 
	 */
	public LibSamcoBase(String url, Integer timeout, String certName,
			String method)
	{
		this.url = url;
		this.method = method;
		this.timeout = 3000;//timeout;
		this.certName = certName;
		apiReqRes = new APIRequestResponse();
	}

	/**
	 * Send Samco webservice request
	 * 
	 * @param requestStr
	 * @return
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 */
	protected String sendRequest(String requestStr,String fields) throws URISyntaxException,
			MalformedURLException, IOException, UnrecoverableKeyException,
			KeyManagementException, CertificateException, KeyStoreException,
			NoSuchAlgorithmException 
	{
		

		JSONObject obj = new JSONObject();
		try 
		{
			obj.put("url", this.url);
			obj.put("method", this.method);
			obj.put("req", fields);
		} 
		catch (JSONException e) 
		{
		}

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("msg", fields);
		
		this.apiReqRes.setReqValue(APITypes.WEBSERVICE, obj);

		if (this.url.indexOf("https") != -1)
		{
			HTTPSConnection connection = new HTTPSConnection(this.url+requestStr);
			connection.setConnectionTimeout(timeout);
			connection.setReadTimeout(timeout);
			
			/*if (!this.certName.isEmpty()) {
				SSLSocketFactory sslSocketFactory = HTTPHelper
						.getX509SSLSocketFactory(new File(this.certName), "");
				connection.setSslSocketFactory(sslSocketFactory);
			}*/
		
			String xmlRes = connection.post(fields);
			
			this.apiReqRes.setRestime();
			this.apiReqRes.setResValue(APITypes.STRING, xmlRes);

			return xmlRes;

			
		} 
		else 
		{

			HTTPConnection connection = new HTTPConnection(this.url+requestStr);
			connection.setConnectionTimeout(timeout);
			connection.setReadTimeout(timeout);
			//* this part is to retry on failed response*//
			int count = 0;
			int maxTries = 3;
			String jsonRes = "";		
			while(true)
			{
				try
				{
					jsonRes = connection.post(fields);
					log.debug("On Response: "+jsonRes);
					break;
					
				}
				catch(Exception e)
				{
					log.error("error while API REQUEST",e);
					if(++count == maxTries) 
					{
						 throw e;
					}
				}
			}
			this.apiReqRes.setRestime();
			this.apiReqRes.setResValue(APITypes.STRING, jsonRes);
			
			return jsonRes;
		}
	}
	@Override
	public JSONArray getAPIReqRes()
	{
		JSONArray api = new JSONArray();
		api.put(this.apiReqRes);
		return api;
	}

	public APIRequestResponse getAPIReqResObj() 
	{
		return this.apiReqRes;
	}
}
