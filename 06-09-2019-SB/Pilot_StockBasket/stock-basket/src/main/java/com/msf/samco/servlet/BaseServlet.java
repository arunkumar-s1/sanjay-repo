package com.msf.samco.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.audit.AuditTrans;
import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.appconfig.UnitTestingProperties;
import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.utils.exception.InvalidResponseException;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.log.Logger;
import com.msf.samco.objects.SSAuditObject;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.utils.helper.Helper;

public abstract class BaseServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BaseServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		long reqTime = System.currentTimeMillis();

		SSAuditObject auditPlatform = null;
		SSRequest ssRequest = null;
		SSResponse ssResponse = null;

		try 
		{

			String reqString = getReqString(request);

			if (reqString == null || reqString.equals(""))
			{
				reqString = "{}";
			}

			ssRequest = new SSRequest(reqString);
			ssResponse = new SSResponse(ssRequest);
			auditPlatform = new SSAuditObject(ssRequest, ssResponse);

			auditPlatform.setReqTime(SamcoHelper.formatTime(new Date(reqTime), APP_CONSTANT.DEFAULT_DATE_TIME_FORMAT));

			// log the request received
			logRequest(ssRequest);

			ssResponse.setHttpResponse(response);
			ssResponse.getResponseObj().put("appID", ssRequest.getAppID());
			ssResponse.setSvcGroup(getSvcGroup());
			ssResponse.setSvcName(getSvcName());

			ssRequest.setAuditObj(auditPlatform);
			ssRequest.setHttpRequest(request);

			try 
			{
				if (isValidAppID(ssRequest, auditPlatform))
				{
					boolean isUnitTestingEnabled = AppConfig.getConfigValue("unit_testing.enabled")
							.equalsIgnoreCase("true");

					if (isUnitTestingEnabled) 
					{
						String testResponse = UnitTestingProperties.getUnitTestingResponse(
								"/" + getSvcGroup() + "/" + getSvcName() + "/" + getSvcVersion());

						if (testResponse != null && !testResponse.equals(""))
						{
							JSONObject respObject = new JSONObject(testResponse);
							ssResponse.put("response", respObject);
						}
					} 
					else
						process(ssRequest, ssResponse);
				} 
				else 
				{
					ssResponse.setInfoID(INFO_IDS.INVALID_APPID);
				}

			}
			catch (SQLException e)
			{
				log.error("SQL Exception : ", e);
				ssResponse.setInfoID(INFO_IDS.REQUEST_FAILED);
				ssResponse.clearData();

			} 
			catch (InvalidRequestParameter e)
			{
				log.error("Invalid Request parameter Exception : ", e);
				ssResponse.setInfoID(INFO_IDS.INVALID_REQUEST_PARAM);
				ssResponse.clearData();

			}
			catch (InvalidResponseException e) 
			{
				log.error("Invalid Response Exception : ", e);
				ssResponse.setInfoID(INFO_IDS.RESPONSE_FAILURE);
				ssResponse.clearData();

			} 
			catch (SamcoException e)
			{
				log.error("Samco Exception : ", e);
				ssResponse.setInfoID(e.getErrorCode());
				ssResponse.clearData();

			} 
			catch (JSONException e) 
			{
				log.error("FailedResponse Exception : ", e);
				ssResponse.setInfoID(INFO_IDS.RESPONSE_FAILURE);
				ssResponse.clearData();

			} 
			catch (Exception e)
			{
				log.error("Exception : ", e);
				ssResponse.setInfoID(INFO_IDS.REQUEST_FAILED);
				ssResponse.clearData();

			}

			/*
			 * Send response back to device auditing response and request
			 */
			log.debug(System.currentTimeMillis() - reqTime);

			auditPlatform.setRespTime(SamcoHelper.formatTime(new Date(), APP_CONSTANT.DEFAULT_DATE_TIME_FORMAT));
			auditPlatform.setSvcVersion(getSvcVersion());
			auditPlatform.setSrcIP(request.getHeader("X-Forwarded-For"));

			auditPlatform.addColumnNameValue("MSG_ID", ssRequest.getMsgID());
			auditPlatform.addColumnNameValue("SVC_GROUP", getSvcGroup());
			auditPlatform.addColumnNameValue("SVC_NAME", getSvcName());
			auditPlatform.addColumnNameValue("INFOID", ssResponse.getInfoID());
			auditPlatform.addColumnNameValue("INFO_MSG", ssResponse.getInfoMsg());
			auditPlatform.addColumnNameValue("APP_ID", ssRequest.getAppID());

			//sendConfig(ssResponse);
			sendResponse(ssResponse, response, reqTime);

			doAudit(auditPlatform);

		} 
		catch (Exception e)
		{
			log.error("Error in request --->> ", e);

			try 
			{

				ssResponse = new SSResponse();

				ssResponse.setHttpResponse(response);
				ssResponse.getResponseObj().put("appID", ssRequest.getAppID());
				ssResponse.setSvcGroup(getSvcGroup());
				ssResponse.setSvcName(getSvcName());

				ssResponse.setInfoID(INFO_IDS.REQUEST_FAILED);

				sendResponse(ssResponse, response, reqTime);

			} 
			catch (Exception e1)
			{
				log.error("Error in sending response", e1);
			}
		}
	}

	private void sendConfig(SSResponse ssResponse) throws Exception
	{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject config = new JSONObject();

		String query = "SELECT TYPE ,MAX(VERSION) as VERSION " + "FROM VERSION_MASTER " + "group by TYPE";

		log.debug("query :: " + query);

		try 
		{
			connection = DBConnection.getInstance().getConnection();

			if (connection != null)
				ps = connection.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) 
			{
				// add type and version to config block
				config.put(rs.getString("TYPE"), rs.getInt("VERSION"));
			}

		} 
		finally 
		{
			Helper.closeResultSet(rs);
			Helper.closeStatement(ps);
			Helper.closeConnection(connection);
		}
		ssResponse.put("config", config);
	}

	private void sendResponse(SSResponse ssResponse, HttpServletResponse response, long reqTime) {

		String responseStr = ssResponse.toString();

		try
		{
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().print(responseStr);

			logResponse(ssResponse, reqTime);

		} 
		catch (IOException e) 
		{
			log.error("ERROR - -- ", e);
		}
	}

	/**
	 * Function to validate the appID
	 * 
	 * @param ssRequest
	 * @param auditPlatform
	 * @return true if appID is valid else false
	 * @throws SQLException
	 */
	protected boolean isValidAppID(SSRequest ssRequest, SSAuditObject auditPlatform) throws SQLException
	{

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isValidAppID = false;

		log.debug("---> In isValidAppID case session null...");

		try
		{
			conn = DBConnection.getInstance().getConnection();
			String query = "SELECT 1 FROM APP_INFO WHERE APP_ID = ?";

			// APIRequestResponse apiReqResp = auditPlatform.getAPIReqRespObj();
			// apiReqResp.setReqValue(APITypes.DB_QUERY, query);

			ps = conn.prepareStatement(query);
			ps.setString(1, ssRequest.getAppID());
			rs = ps.executeQuery();
			
			//if resultset has some rows, valid appid
			isValidAppID = rs.isBeforeFirst();
		}
		finally 
		{
			Helper.closeResultSet(rs);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}

		return isValidAppID;
	}

	private String getReqString(HttpServletRequest request)
	{
		StringBuffer jb = new StringBuffer();

		try
		{
			BufferedReader reader = request.getReader();

			char[] chars = new char[4 * 1024];
			int len;
			while ((len = reader.read(chars)) >= 0)
			{
				jb.append(chars, 0, len);
			}

		}
		catch (Exception e)
		{ 
		}
		return jb.toString();
	}

	protected void doAudit(SSAuditObject ssAuditObject) throws SamcoException
	{

		if (!(AppConfig.getConfigValue("audit.enabled").equalsIgnoreCase("true") && ssAuditObject.isNeedAudit()))
			return;

		Connection con = null;

		try
		{
			con = DBConnection.getInstance().getConnection();

			AuditTrans auditTrans = new AuditTrans();
			auditTrans.addTrans(con, ssAuditObject);

		}
		catch (Exception e)
		{
			log.error("AUDIT - FAILED", e);

		} 
		finally 
		{
			Helper.closeConnection(con);
		}

	}

	protected String[] getStage() 
	{

		String[] validStages = {"NORMAL", "GUEST"};
		return validStages;
	}

	abstract protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception;

	abstract protected String getSvcName();

	abstract protected String getSvcGroup();

	abstract protected String getSvcVersion();

	protected void logRequest(SSRequest ssRequest) 
	{

		log.info("/" + getSvcGroup() + "/" + getSvcName() + "/" + getSvcVersion() + " --  request received -- "
				+ ssRequest.toS());
	}

	protected void logResponse(SSResponse ssResponse, long reqTime)
	{

		String responseStr = ssResponse.toString();
		long timeTaken = System.currentTimeMillis() - reqTime;

		log.info("/" + getSvcGroup() + "/" + getSvcName() + "/" + getSvcVersion() + " -- time taken " + timeTaken
				+ " -- response sent -- " + ssResponse.getAppID() + " -- " + responseStr);
	}
}