package com.msf.samco.services.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;
import com.msf.utils.helper.Helper;

public class Config extends BaseServlet 
{
	private static final long serialVersionUID = 1L;
	//private static Logger log = Logger.getLogger(Config.class);
	
	private static final String MESSAGE = "message";
	private static final String CONFIG = "config";
	private static final String APP = "app";

	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		logRequest(ssRequest);
		JSONObject data=ssRequest.getData();
		String message =data.optString(MESSAGE,"0");		
		String app = data.optString(APP,"0");
		
		sendMessageConfig(ssRequest, ssResponse, Integer.parseInt(message));
		sendAppConfig(ssRequest, ssResponse,Integer.parseInt(app));
	}	

	private void sendMessageConfig( SSRequest ssRequest,SSResponse ssResponse, int reqMessageVersion) throws Exception 
	{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT CODE ,VALUE,ACTION_CODE FROM MESSAGE_CONFIG";
		try
		{
			connection = DBConnection.getInstance().getConnection();
			JSONArray config = new JSONArray();
			JSONObject message = new JSONObject();
			
			ps = connection.prepareStatement(query);	
			//ps.setString(1, ssRequest.getAppID());
			//ps.setInt(2, reqMessageVersion);
	
			rs = ps.executeQuery();
	
			while (rs.next()) 
			{
				JSONObject obj = new JSONObject();
				JSONObject value = new JSONObject();
	
				value.put("message", rs.getString("VALUE"));
				value.put("action", rs.getInt("ACTION_CODE"));
	
				obj.put("key", rs.getString("CODE"));
				obj.put("value", value);
	
				config.put(obj);
			}
			message.put(CONFIG, config);
			ssResponse.setInfoID(INFO_IDS.SUCCESS);
			ssResponse.addToData(MESSAGE, message);
		}
		finally 
		{
			Helper.closeResultSet(rs);
			Helper.closeStatement(ps);
			Helper.closeConnection(connection);
		}	
	}
	
	private void sendAppConfig( SSRequest ssRequest,SSResponse ssResponse, int reqAppVersion) throws Exception 
	{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		JSONArray config;
		String query = "SELECT CODE,VALUE FROM APP_CONFIG";

		try
		{
			connection = DBConnection.getInstance().getConnection();
			ps = connection.prepareStatement(query);
	
			//ps.setString(1, ssRequest.getAppID());
			//ps.setInt(2, reqAppVersion);
	
			res = ps.executeQuery();
			config = new JSONArray();
			while (res.next()) 
			{
				JSONObject obj = new JSONObject();
				obj.put("key", res.getString("CODE"));
				obj.put("value", res.getString("VALUE"));
	
				config.put(obj);
			}
		}
		finally 
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(ps);
			Helper.closeConnection(connection);
		}	
		JSONObject app = new JSONObject();
		app.put(CONFIG, config);

		ssResponse.setInfoID(INFO_IDS.SUCCESS);
		ssResponse.addToData(APP, app);
	}
	

	@Override
	protected String getSvcName() {
		return "Base";
	}

	@Override
	protected String getSvcGroup() {
		return "Config";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}

}