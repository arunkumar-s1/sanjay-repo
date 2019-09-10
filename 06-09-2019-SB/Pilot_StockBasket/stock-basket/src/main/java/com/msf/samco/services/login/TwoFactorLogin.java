package com.msf.samco.services.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.login.TokenGeneration;
import com.msf.libsb.userlogin.ValidPwd;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.libsb.utils.helper.Session;
import com.msf.libsb.utils.helper.SessionHelper;
import com.msf.libsb.utils.keygeneration.KeyGenerator;
import com.msf.log.Logger;
import com.msf.samco.objects.SSAuditObject;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;
import com.msf.utils.helper.Helper;

public class TwoFactorLogin extends BaseServlet 
{
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(TwoFactorLogin.class);

	@Override
	protected void logRequest(SSRequest ssRequest) 
	{
		try 
		{
			ssRequest.maskValueInData(APP_CONSTANT.PASSWORD);
		} 
		catch (JSONException e) {}
		super.logRequest(ssRequest);
	}

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		JSONObject dataObject = null;

		SSAuditObject auditPlatform = ssRequest.getAuditObj();
		auditPlatform.setNeedAudit(true);

		String appID = ssRequest.getAppID();
		dataObject = ssRequest.getData();

		String userID = ssRequest.getFromData(APP_CONSTANT.USER_ID).toUpperCase();
		String password = ssRequest.getFromData(APP_CONSTANT.PASSWORD);
		String yob = ssRequest.getFromData(APP_CONSTANT.DOB);

		if (userID.equals("") || password.equals("") || yob.equals(""))
		{
			throw new InvalidRequestParameter("");
		}
		
		String sPan = null;
		String sEmail = null;
		String sSamcoGeneratedToken=null;
		boolean flag=false;
		JSONObject userDetailsObj = getUserDetails(DBConnection.getInstance(), userID);
		if (userDetailsObj!=null) 
		{
			String dbYOB=userDetailsObj.getString("yob");
			
			if ( dbYOB!= null && dbYOB.length() > 0) 
			{
				if (!dbYOB.equals(yob))
				{
					throw new SamcoException(INFO_IDS.INVALID_DOB);
				}				
				sPan = userDetailsObj.getString("pan");
				sEmail = userDetailsObj.getString("email");
				flag=true;
			} 
		} 
		if(!flag)
		{
			String sTokenURL = AppConfig.getConfigValue("samco_api_url");
			String sAPIValue = AppConfig.getConfigValue("token.api");
			String sMerchantID = AppConfig.getConfigValue("merchant_id");
			String sMerchantPassword = AppConfig.getConfigValue("merchant_password");
			String sTokenFields = APIConstants.SAMCO_API + "=" + sAPIValue + "&" + APIConstants.SAMCO_CLIENTID + "="
					+ userID + "&" + APIConstants.SAMCO_MERCHANT_ID + "=" + sMerchantID + "&"
					+ APIConstants.SAMCO_MERCHANT_PASSWORD + "=" + sMerchantPassword;

			TokenGeneration token = new TokenGeneration();
			token.setTokenGenerationURL(sTokenURL, sTokenFields);
			token.process();
			sSamcoGeneratedToken = token.getToken();
			if (!token.getDOB().equals(yob))
				throw new SamcoException(INFO_IDS.INVALID_DOB);
			sPan = token.getPanNumber();
			sEmail = token.getMailID();

		}
		log.debug("userid= "+userID+";pan= "+sPan+";email= "+sEmail);
		/* GETTING JKEY AND JSESSION */
		KeyGenerator keyGen = new KeyGenerator(userID);
		String jsession = keyGen.getJSession();
		String jKey = keyGen.getJKey();

		
		/* BEGIN OF VALIDPWD, SAVE ANS, VALIDATE ANS + DEFAULT LOGIN */
		ValidPwd validateDetails = new ValidPwd(DBConnection.getInstance(), userID, password, jsession, jKey, sPan, sEmail, dataObject);

		String key4 = validateDetails.getPublicKey4();

		JSONObject jLoginDetails = validateDetails.getUserDetails();
		String sAccountID = jLoginDetails.getString(APIConstants.DEF_ACC_ID);
		String sAccountName = formatCamelCase(jLoginDetails.getString(APIConstants.DEF_ACC_NAME));
		String sBrokerName = jLoginDetails.getString(APIConstants.DEF_BROKER);
		String sBranchID = jLoginDetails.getString(APIConstants.DEF_BRANCH);
		JSONArray exchangeArray = jLoginDetails.getJSONArray(APIConstants.DEF_EXCHANGES);
		JSONArray productArray = jLoginDetails.getJSONArray(APIConstants.DEF_PRODUCTS);
		JSONArray orderTypeArray = jLoginDetails.getJSONArray(APIConstants.DEF_ORDER_TYPES);
		String prod_alias = jLoginDetails.getString(APIConstants.DEF_PROD_ALIAS);
		String sPasswordResetFlag = jLoginDetails.getString(APIConstants.VAL_ANS_IS_RESET);
		JSONArray jWeblink = jLoginDetails.getJSONArray(APIConstants.DEF_WEBLINK);

		String sessionID = ssRequest.getSessionID(true);

		Session session = new Session();

		session.setSessionID(sessionID);
		session.setUserId(userID);
		session.setAppID(appID);
		session.setUserStage(APP_CONSTANT.STAGE_NORMAL);
		session.setJsession(jsession);
		session.setJkey(key4);
		session.setProductAlias(prod_alias);
		session.setAccountID(sAccountID);
		session.setBranchID(sBranchID);
		session.setBrokerName(sBrokerName);
		session.setSamcoToken(sSamcoGeneratedToken);
		session.setProductType(productArray.toString());
		session.setjWeblinkURL(jWeblink.toString());
		session.setUserType(APP_CONSTANT.SAMCO_USER.toUpperCase());
		session.setExchangeArray(exchangeArray.toString());
		SessionHelper.updateSessionToDB(DBConnection.getInstance(), session);

		ServletContext servletContext = getServletContext();
		String contextPath = servletContext.getContextPath();

		ssResponse.setSession(sessionID, contextPath);

		ssResponse.addToData(APP_CONSTANT.ACCOUNT_ID, sAccountID);
		ssResponse.addToData(APP_CONSTANT.LOGIN_SESSION_ID, sessionID);
		ssResponse.addToData(APP_CONSTANT.ACCOUNT_NAME, sAccountName);
		ssResponse.addToData(APP_CONSTANT.BROKER_NAME, sBrokerName);
		ssResponse.addToData(APP_CONSTANT.BRANCH_ID, sBranchID);
		ssResponse.addToData(APP_CONSTANT.EXCHANGE_ARRAY, exchangeArray);
		ssResponse.addToData(APP_CONSTANT.PRODUCT_ARRAY, productArray);
		ssResponse.addToData(APP_CONSTANT.ORDER_TYPE_ARRAY, orderTypeArray);
		ssResponse.addToData(APP_CONSTANT.PASSWORD_RESET_FLAG, sPasswordResetFlag);
		ssResponse.addToData(APP_CONSTANT.SESSION_ID, sessionID);

		// Getting order preferences from the database.

		JSONObject jPreference = SamcoHelper.checkProductAndOrderPreference(userID, DBConnection.getInstance());
		if (jPreference.getJSONObject(APP_CONSTANT.FILTER_PRODUCT_TYPE_PREFERENCES).length() == 0)
			ssResponse.addToData(APP_CONSTANT.IS_WELCOME_ABOARD, true);
		else 
			ssResponse.addToData(APP_CONSTANT.IS_WELCOME_ABOARD, false);

	}

	private JSONObject getUserDetails(DBPool pool, String sUserID) throws Exception 
	{		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		JSONObject jDetailsObj =null;

		try 
		{
			String sQuery = "select yob,pan_no,email_id from USER_DETAILS where user_id = ?";
			conn = pool.getSamcoConnection();
			ps = conn.prepareStatement(sQuery);
			ps.setString(1, sUserID);

			res = ps.executeQuery();
			if(res.next()) 
			{
				jDetailsObj=new JSONObject();
				jDetailsObj.put("yob", res.getString("yob"));
				jDetailsObj.put("pan", res.getString("pan_no"));
				jDetailsObj.put("email", res.getString("email_id"));
			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
		return jDetailsObj;
	}

	private String formatCamelCase(String s) {
		String name = "";
		for (String a : s.split(" ")) {
			name += a.substring(0, 1).toUpperCase() + a.substring(1).toLowerCase();
			name += " ";
		}
		return name;
	}

	@Override
	protected String getSvcName() {
		return "TwoFactorLogin";
	}

	@Override
	protected String getSvcGroup() {
		return "Login";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}
}
