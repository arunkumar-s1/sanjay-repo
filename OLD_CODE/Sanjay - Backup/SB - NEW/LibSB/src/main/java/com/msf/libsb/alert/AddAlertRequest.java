package com.msf.libsb.alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.utils.helper.Helper;

public class AddAlertRequest extends BaseRequest 
{
	private String DAY = "Day";//"D";
	private String ONE_WEEK = "1 Week";//"oneWk";
	private String ONE_MONTH = "1 Month";//"oneM";
	private String THREE_MONTHS = "3 Months";//"threeM";
	private String SIX_MONTHS = "6 Months";//"sixM";
	
	private String userID;
	private String symbol;
	private JSONObject criteria;
	private JSONArray notifyObj;
	private String appID;
	private int alertID;
	private String validity;
	private JSONObject triggerCriteria;
	private String tradingSymbol;
	private String comment;
	
	public AddAlertRequest() 
	{
		super();
	}
	
	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public JSONObject getCriteria()
	{
		return criteria;
	}

	public void setCriteria(JSONObject criteria)
	{
		this.criteria = criteria;
	}

	public JSONArray getNotifyObj()
	{
		return notifyObj;
	}

	public void setNotifyObj(JSONArray notifyObj)
	{
		this.notifyObj = notifyObj;
	}

	public String getAppID()
	{
		return appID;
	}

	public void setAppID(String appID)
	{
		this.appID = appID;
	}

	public int getAlertID()
	{
		return alertID;
	}

	public void setAlertID(int alertID)
	{
		this.alertID = alertID;
	}

	public String getValidity()
	{
		return validity;
	}

	public void setValidity(String validity)
	{
		this.validity = validity;
	}

	public JSONObject getTriggerCriteria()
	{
		return triggerCriteria;
	}

	public void setTriggerCriteria(JSONObject triggerCriteria)
	{
		this.triggerCriteria = triggerCriteria;
	}

	public String getTradingSymbol()
	{
		return tradingSymbol;
	}

	public void setTradingSymbol(String tradingSymbol)
	{
		this.tradingSymbol = tradingSymbol;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	@Override
	public BaseResponse postRequest() throws Exception {
		return addAlert();
	}

	public BaseResponse addAlert() throws Exception {
		AddAlertResponse alertResponse = new AddAlertResponse();

		int alertID = insertToDB();
		log.debug("AlertID : " + alertID);
		if (alertID > 0) {
			setAlertID(alertID);

			this.remove(APP_CONSTANT.APP_ID);
			this.remove(APP_CONSTANT.VALIDITY);
			//this.remove(APP_CONSTANT.TRIGGER_CRITERIA);

			String infoID = sendToAlertEngine();
			log.debug("Alert Engine InfoID : " + infoID);
			if (infoID.equals("0")){
				alertResponse.setResponseCode(APIConstants.SUCCESS_CODE);
				alertResponse.setAlertID(String.valueOf(alertID));
			}
			else {
				alertResponse.setResponseCode(0);
				alertResponse.setErrorMessage("Unable to Process your request");
			}
		} else {
			alertResponse.setResponseCode(0);
			alertResponse.setErrorMessage("Unable to Process your request");
		}

		return alertResponse;
	}

	// TODO
	public String sendToAlertEngine() throws Exception 
	{
		String alertEngineAPIURL = AppConfig.getConfigValue("AddAlert_url");

		log.debug("Add Alert url : " + alertEngineAPIURL);

		AlertEngineRequest alertEngineRequest = new AlertEngineRequest(alertEngineAPIURL);

		alertEngineRequest.setRequestObj(this);
		alertEngineRequest.setAction("add");

		AlertEngineResponse alertEngineResponse = (AlertEngineResponse) alertEngineRequest.postRequest();

		String respID = alertEngineResponse.getInfoID();

		return respID;
	}

	public int insertToDB() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;

		String userID = this.getString(APP_CONSTANT.USER_ID);
		String symbol = this.getString(APP_CONSTANT.SYMBOL);
		String validity = this.getString(APP_CONSTANT.VALIDITY);
		String validTill = null;
		String comment = this.getString(APP_CONSTANT.COMMENT);
		
    	Calendar cal = Calendar.getInstance();
    	
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);

    	


		if (validity != null) {

			DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			if (validity.equalsIgnoreCase(DAY)) {
		    	cal.add(Calendar.DATE, 1);
			} else if (validity.equalsIgnoreCase(ONE_WEEK)) {
				cal.add(Calendar.DATE, 7);
			} else if (validity.equalsIgnoreCase(ONE_MONTH)) {
				cal.add(Calendar.MONTH, 1);
			} else if (validity.equalsIgnoreCase(THREE_MONTHS)) {
				cal.add(Calendar.MONTH, 3);
			} else if (validity.equalsIgnoreCase(SIX_MONTHS)) {
				cal.add(Calendar.MONTH, 6);
			} else {
				cal.add(Calendar.DATE, 1);
			}
		
	    	Date dateWithoutTime = cal.getTime();
	    	validTill = SamcoHelper.formatTime(dateWithoutTime, "yyyy-MM-dd HH:mm:ss");

		}

		JSONObject jTrigCriteria = this.getJSONObject(APP_CONSTANT.TRIGGER_CRITERIA);

		String trigPrice = null;
		String trigQty = null;
		String prodCode = null;
		String trigSide = null;

		if (jTrigCriteria.has(APP_CONSTANT.PRICE))
			trigPrice = jTrigCriteria.getString(APP_CONSTANT.PRICE);

		if (jTrigCriteria.has(APP_CONSTANT.QUANTITY))
			trigQty = jTrigCriteria.getString(APP_CONSTANT.QUANTITY);

		if (jTrigCriteria.has(APP_CONSTANT.PRODUCT_CODE))
			prodCode = jTrigCriteria.getString(APP_CONSTANT.PRODUCT_CODE);
		
		if (jTrigCriteria.has(APP_CONSTANT.SIDE))
			trigSide = jTrigCriteria.getString(APP_CONSTANT.SIDE);
		
		JSONObject cObj = this.getJSONObject(APP_CONSTANT.ALERT_CRITERIA);

		String criteriaType = cObj.getString(APP_CONSTANT.ALERT_CRITERIA_TYPE);
		String criteriaValue = cObj.getString(APP_CONSTANT.ALERT_CRITERIA_VALUE);

		JSONArray notifyObj = this.getJSONArray(APP_CONSTANT.ALERT_NOTIFICATION_TYPE);
		String appID = this.getString(APP_CONSTANT.APP_ID);

		String sAlertID[] = { "ALERT_ID" };
		String Query = "INSERT INTO Alert.ADDALERT(USER_ID,SYM,CRITERIA_TYPE,CRITERIA_VALUE,"
				+ "NOTIFICATION_TYPE,APP_ID,VALID_TILL,TRIGGER_PRICE,TRIGGER_QUANTITY, SYM_NAME, PRODUCT_CODE, COMMENT, TRIGGER_SIDE, VALIDITY) VALUES ( ?, ?, ?, ?, '";

		boolean foundMore = false;
		for (int i = 0; i < notifyObj.length(); i++) {
			if (foundMore)
				Query += ",";
			else
				foundMore = true;

			Query += notifyObj.getString(i);
		}

		Query += "' , ? , ?, ?, ?, ?, ?, ?, ?, ?); ";

		//log.info("SQL Query : " + Query);

		int alertID = 0;

		try {
			con = DBConnection.getInstance().getConnection();
			pstmt = con.prepareStatement(Query, sAlertID);

			pstmt.setString(1, userID);
			pstmt.setString(2, symbol);
			pstmt.setString(3, criteriaType);
			pstmt.setString(4, criteriaValue);
			pstmt.setString(5, appID);
			pstmt.setString(6, validTill);
			pstmt.setString(7, trigPrice);
			pstmt.setString(8, trigQty);
			pstmt.setString(9, this.optString(APP_CONSTANT.TRADING_SYMBOL, ""));
			pstmt.setString(10, prodCode);
			pstmt.setString(11, comment);
			pstmt.setString(12, trigSide);
			pstmt.setString(13, validity);
			log.debug("ADDALERT QURY  "+pstmt);

			if (pstmt.executeUpdate() > 0) {
				res = pstmt.getGeneratedKeys();
				if (res.next()) {
					alertID = res.getInt(1);
				}
			}else{
				log.debug("sql qury failed");
			}

		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(con);
		}

		return alertID;
	}
}
