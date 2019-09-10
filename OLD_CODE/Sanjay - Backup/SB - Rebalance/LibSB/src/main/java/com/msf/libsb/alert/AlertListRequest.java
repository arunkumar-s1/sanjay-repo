package com.msf.libsb.alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.utils.helper.Helper;

public class AlertListRequest extends BaseRequest {

	public static SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static SimpleDateFormat df1 = new SimpleDateFormat(
			"dd MMM yyyy, HH:mm a");

	private DBPool pool;
	private ArrayList<String> arSymbols = new ArrayList<>();

	public AlertListRequest(DBPool pool) {
		super();
		this.pool = pool;
	}

	public void setUserID(String userID) throws Exception {
		this.put(APP_CONSTANT.USER_ID, userID);
	}

	@Override
	public BaseResponse postRequest() throws Exception {

		JSONArray alertList = new JSONArray();

		alertList = getStockAlertList();

		AlertListResponse alertResponse = new AlertListResponse();

		alertResponse.put(APP_CONSTANT.ALERT_LIST, alertList);

		return alertResponse;
	}

	public JSONArray getStockAlertList() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;

		String userID = this.getString(APP_CONSTANT.USER_ID);

		String Query = " SELECT ALERT_ID, SYM, CRITERIA_TYPE, CRITERIA_VALUE, "
				+ " TRIGGERED_AT,  NOTIFICATION_TYPE, TRIGGER_PRICE, TRIGGER_QUANTITY, PRODUCT_CODE, TRIGGER_SIDE, VALIDITY FROM Alert.ADDALERT   "
				+ " WHERE USER_ID = ? AND (VALID_TILL-NOW()) > 0 AND COMMENT = 'USER_ALERT' order by created_at desc";

		//log.info("SQL Query : " + Query);

		JSONArray alertList = new JSONArray();

		try {

			con = pool.getConnection();

			pstmt = con.prepareStatement(Query);

			pstmt.setString(1, userID);

			res = pstmt.executeQuery();

			while (res.next()) {
				String symbol = res.getString("SYM");
				if (!arSymbols.contains(symbol))
					arSymbols.add(symbol);

				JSONObject alertObj = new JSONObject();
				JSONObject trigCriteria = new JSONObject();
				
				String sTrigPrc=res.getString("TRIGGER_PRICE");
				String sTrigQty=res.getString("TRIGGER_QUANTITY");
				String sProdCode = res.getString("PRODUCT_CODE");
				String sTrigSide = res.getString("TRIGGER_SIDE");
				
//				trigCriteria.put(APP_CONSTANT.TRIGGER_PRICE, sTrigPrc!=null?sTrigPrc:"");
//				trigCriteria.put(APP_CONSTANT.TRIGGER_QTY, sTrigQty!=null?sTrigQty:"");
				
				alertObj.put(APP_CONSTANT.TRIGGER_PRICE, sTrigPrc!=null?sTrigPrc:"");
				alertObj.put(APP_CONSTANT.TRIGGER_QTY, sTrigQty!=null?sTrigQty:"");
				alertObj.put(APP_CONSTANT.PRODUCT_CODE, sProdCode != null? sProdCode:"");
				alertObj.put(APP_CONSTANT.TRIGGER_TYPE, sTrigSide != null ? sTrigSide:"");
								
				alertObj.put(APP_CONSTANT.ALERT_ID, res.getString("ALERT_ID"));
				alertObj.put(APP_CONSTANT.SYMBOL, symbol);
				alertObj.put(APP_CONSTANT.EXCHANGE, symbol.substring(symbol.indexOf("_")+1, symbol.length()));

				String sCriteriaType=res.getString("CRITERIA_TYPE");
				String displayCriteriaType = AppConfig.getAlertCriteria(sCriteriaType);
				
				alertObj.put(APP_CONSTANT.ALERT_CRITERIA_TYPE, displayCriteriaType.toLowerCase());
				
				String sAlertType="";
				if(sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_GREATER_PRICE)||sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_LOWER_PRICE)||sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_SAME_PRICE))
					sAlertType="Price";
				
				else if(sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_GREATER_VOLUME)||sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_LOWER_VOLUME))
					sAlertType="Volume";
				
				else if(sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_GREATER_PRICE_PERCENTAGE)||sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_LOWER_PRICE_PERCENTAGE))
					sAlertType="Daily % Chg";
				
				else if(sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_LOWER_FROM_LTP)||sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_GREATER_FROM_LTP)||sCriteriaType.equalsIgnoreCase(APP_CONSTANT.ALERT_SAME_FROM_LTP))
					sAlertType="From LTP";
				
				alertObj.put(APP_CONSTANT.ALERT_TYPE, sAlertType);

				String displayCriteriaValue = res.getString("CRITERIA_VALUE");
				
				if( !sAlertType.equalsIgnoreCase("Volume") )
					displayCriteriaValue = SamcoHelper.chartFormat(displayCriteriaValue, 2);
				else{
					if( displayCriteriaValue.contains("."))
						displayCriteriaValue = displayCriteriaValue.substring(0, displayCriteriaValue.indexOf('.'));
				}
				
				String displayAlertCriteria = "";
				
				String[] critAr = displayCriteriaType.split("\\s+");
				displayAlertCriteria = getDisplayAlertCriteria(critAr[critAr.length-1]) + " " + displayCriteriaValue;
				
				alertObj.put(APP_CONSTANT.ALERT_CRITERIA_VALUE, displayCriteriaValue);
				alertObj.put(APP_CONSTANT.DISPLAY_ALERT_CRITERIA, displayAlertCriteria);

				String sTime = res.getString("TRIGGERED_AT");
				String triggeredAt = "";
				if (sTime != null) {
					Date dt = df.parse(sTime);
					triggeredAt = df1.format(dt);
				}

				alertObj.put(APP_CONSTANT.ALERT_TRIGGERED_AT, triggeredAt);
				alertObj.put(APP_CONSTANT.IS_TRIGGERED, !(triggeredAt.equals("")) );
				alertObj.put(APP_CONSTANT.ALERT_NOTIFICATION_TYPE,
						res.getString("NOTIFICATION_TYPE"));

				String sValidity = res.getString(DBConstants.VALIDITY);
				alertObj.put(APP_CONSTANT.VALIDITY, sValidity != null ? sValidity : "Day");
				alertList.put(alertObj);
			}

		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(con);
		}

		return alertList;
	}
	
	private String getDisplayAlertCriteria(String string) {

		if( string.equalsIgnoreCase("above"))
			return ">";
		else if(string.equalsIgnoreCase("below"))
			return "<";
		else if(string.equalsIgnoreCase("to"))
			return "=";
		
		return string;
	}

	public JSONArray getSymbols() throws Exception {
		JSONArray jSymbols = new JSONArray();
		for (int i = 0; i < arSymbols.size(); i++) {
			jSymbols.put(arSymbols.get(i));
		}
		
		return jSymbols;
	}
}
