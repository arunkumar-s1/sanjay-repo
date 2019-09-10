package com.msf.libsb.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.msf.connections.http.HTTPConnection;
import com.msf.connections.http.HTTPSConnection;
import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;

public class HttpHelper
{

	public static Logger log = Logger.getLogger(HttpHelper.class);

	public static Integer READ_TIMEOUT = 1000;
	public static Integer CONNECTION_TIMEOUT = 1000;

	private long sApiTime, eApiTime;
	String certName = "";

	private HTTPSConnection httpsconnection;
	private HTTPConnection httpConnection;

	private String response;
	private int returnCode;

	public HttpHelper() throws SamcoException
	{

		READ_TIMEOUT = Integer.parseInt(AppConfig.getConfigValue("httpconn.read_timeout")) * 1000;
		CONNECTION_TIMEOUT = Integer.parseInt(AppConfig.getConfigValue("httpconn.conn_timeout")) * 1000;

	}

	public void postRequest(String url, String fields) throws Exception
	{

		try 
		{
			sApiTime = System.currentTimeMillis();
			if (url.indexOf("https") != -1) 
			{
				httpsconnection = new HTTPSConnection(url);

				httpsconnection.setConnectionTimeout(CONNECTION_TIMEOUT);
				httpsconnection.setReadTimeout(READ_TIMEOUT);

				if (!this.certName.isEmpty()) 
				{
					SSLSocketFactory sslSocketFactory = getX509SSLSocketFactory(new File(this.certName), "");
					httpsconnection.setSslSocketFactory(sslSocketFactory);
				}

				response = httpsconnection.post(fields);
				returnCode = httpsconnection.getReturnCode();
			} 
			else
			{
				httpConnection = new HTTPConnection(url);

				httpConnection.setReadTimeout(READ_TIMEOUT);
				httpConnection.setConnectionTimeout(CONNECTION_TIMEOUT);

				response = httpConnection.post(fields);
				returnCode = httpConnection.getReturnCode();
			}

			log.debug("Response :" + response);

		} 
		finally
		{
			eApiTime = System.currentTimeMillis();
		}
	}

	public void getRequest(String url)
			throws URISyntaxException, MalformedURLException, SocketTimeoutException, IOException
	{

		HTTPConnection httpConnection = null;
		try
		{
			sApiTime = System.currentTimeMillis();
			Map<String, String> header = new HashMap<String, String>();

			if (url.indexOf("https") != -1)
			{
				httpsconnection = new HTTPSConnection(url);
				httpsconnection.setHeaders(header);
				log.debug(url);
				httpsconnection.setReadTimeout(READ_TIMEOUT);
				httpsconnection.setConnectionTimeout(CONNECTION_TIMEOUT);
				returnCode = httpsconnection.getReturnCode();
				response = httpsconnection.get();
				log.debug("Response : " + response);

			} 
			else 
			{
				httpConnection = new HTTPConnection(url);
				httpConnection.setHeaders(header);
				log.debug("Request URL::" + url);
				httpConnection.setReadTimeout(READ_TIMEOUT);
				httpConnection.setConnectionTimeout(CONNECTION_TIMEOUT);
				response = httpConnection.get();
				returnCode = httpConnection.getReturnCode();
				log.debug("Response :" + response);
			}
		} 
		finally
		{
			eApiTime = System.currentTimeMillis();
		}
	}

	public String getResponse()
	{

		return response;
	}

	public static SSLSocketFactory getX509SSLSocketFactory(File pKeyFile, String pKeyPassword)
			throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException,
			UnrecoverableKeyException, KeyManagementException 
	{

		CertificateFactory factory2 = CertificateFactory.getInstance("X.509");
		Certificate generateCertificate = factory2.generateCertificate(new FileInputStream(pKeyFile));

		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);
		keyStore.setCertificateEntry("myServer", generateCertificate);

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(keyStore);

		TrustManager[] tm = tmf.getTrustManagers();
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, tm, null);

		return sslContext.getSocketFactory();
	}

	public static SSLSocketFactory getTrustAllHttpsSSLSocketFactory() throws Exception 
	{

		// Create a trust manager that does not validate certificate chains:

		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];

		javax.net.ssl.TrustManager tm = new BlindlyTrustAllHttpCert();

		trustAllCerts[0] = tm;

		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);

		return sc.getSocketFactory();
	}

	public static String urlEncode(String inputStr) throws UnsupportedEncodingException 
	{
		return URLEncoder.encode(inputStr, "UTF-8").replace("+", "%20");
	}

	private static class BlindlyTrustAllHttpCert implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager 
	{

		public java.security.cert.X509Certificate[] getAcceptedIssuers() 
		{
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs)
		{
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) 
		{
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException
		{
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException
		{
			return;
		}
	}

	public int getReturnCode()
	{
		return this.returnCode;
	}
}
