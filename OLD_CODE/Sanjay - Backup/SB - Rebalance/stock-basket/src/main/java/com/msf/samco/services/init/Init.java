package com.msf.samco.services.init;
import java.sql.Connection;
import java.sql.SQLException;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.FailedResponseException;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.samco.objects.SSAuditObject;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;
import com.msf.utils.constants.UtilsConstants;
import com.msf.utils.exception.AdapterNotSupportedException;
import com.msf.utils.helper.Helper;
import com.msf.utils.init.AppData;
public class Init extends BaseServlet 
{
	private static final long serialVersionUID = 1L;
	private static final String APPLICATION = "app";
	private static final String SOFTWARE = "software";

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		try 
		{
			JSONObject dataObject = ssRequest.getData()	;
			JSONObject appJsonObject = dataObject.getJSONObject(APPLICATION);
			JSONObject softwareJsonObject = dataObject.getJSONObject(SOFTWARE);

			String appID = ssRequest.getAppID();
			if("0".equals(appID))
			{
				appID = com.msf.utils.helper.Helper.generateAPPID(ssRequest.toString());
			}
			Connection connection = null;
			try 
			{
				AppData appData = new AppData();

				appData.setAppID(appID);
				appData.setAppName(appJsonObject.getString("name"));
				appData.setAppVersion(appJsonObject.getString("version"));
				appData.setChannel(appJsonObject.getString("channel"));
				appData.setBuild(appJsonObject.getString("build"));
				appData.setOsType(softwareJsonObject.optString("osType", null));
				appData.setOsVersion(softwareJsonObject.optString("osVersion", null));
				appData.setOsName(softwareJsonObject.optString("osName", null));
				appData.setOsVendor(softwareJsonObject.optString("osVendor", null));
				appData.setVendor(softwareJsonObject.optString("vendor", null));
				appData.setIMEI(softwareJsonObject.optString("imei", null));
				appData.setModel(softwareJsonObject.optString("model", null));
				appData.setScreen(softwareJsonObject.optString("screen", null));
				appData.setKeyboard(softwareJsonObject.optString("keyboard", null));
				appData.setDeviceType(softwareJsonObject.optString("deviceType", null));
				appData.setGps(softwareJsonObject.optString("gps", null));
				appData.setIMSI(softwareJsonObject.optString("imsi", null));
				appData.setCellular(softwareJsonObject.optString("cellular", null));

				com.msf.utils.init.Init init = null;

				init = new com.msf.utils.init.Init(UtilsConstants.MYSQL);
				connection = DBConnection.getInstance().getConnection();
				int result = init.insertAppData(connection, appData);
				if (result==0 || result==-2) // success
				{
					ssResponse.addToData(APP_CONSTANT.APP_ID, appID);
					ssResponse.setAppID(appID);
					ssResponse.setInfoID(INFO_IDS.SUCCESS);
					return;
				} 
				else
				{
					ssResponse.setInfoID(INFO_IDS.INVALID_BUILD);
					return;
				}
			} 
			catch (AdapterNotSupportedException e) 
			{
				throw e;
			}
			catch (SQLException sq) 
			{
				throw sq;
			} 
			finally 
			{
				Helper.closeConnection(connection);
			}
		} 
		catch (JSONException e)
		{
			throw new InvalidRequestParameter(e.getMessage());
		} 
		catch (Exception e) 
		{
			throw new FailedResponseException(e.getMessage());
		}
	}

	@Override
	protected boolean isValidAppID(SSRequest ssRequest,SSAuditObject ssAuditObject) throws SQLException 
	{
		return true;
	}

	@Override
	protected String getSvcName()
	{
		return "Base";
	}

	@Override
	protected String getSvcGroup()
	{
		return "Init";
	}

	@Override
	protected String getSvcVersion()
	{
		return "1.0.0";
	}
}
