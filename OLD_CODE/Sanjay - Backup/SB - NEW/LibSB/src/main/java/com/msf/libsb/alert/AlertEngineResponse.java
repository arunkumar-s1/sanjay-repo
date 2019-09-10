package com.msf.libsb.alert;

import java.util.Calendar;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.base.BaseResponse;

public class AlertEngineResponse extends BaseResponse {

	protected String infoID = null;
	protected String infoMsg = null;

	private JSONObject responseObj = null;

	public AlertEngineResponse(String apiResponse) throws JSONException, Exception {
			
		String alertEngineStopTiming = AppConfig.getConfigValue("alertEngineStopTiming");
		String alertEngineStartTiming = AppConfig.getConfigValue("alertEngineStartTiming");

		//log.debug("Alert Timing :: " + alertEngineStopTiming + " ---- " + alertEngineStartTiming);

		String stop[] = alertEngineStopTiming.split(":");
		Calendar stopCal = Calendar.getInstance();

		stopCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(stop[0]));
		stopCal.set(Calendar.MINUTE, Integer.parseInt(stop[1]));
		stopCal.set(Calendar.SECOND, 0);
		
		String start[] = alertEngineStartTiming.split(":");
		Calendar startCal = Calendar.getInstance();

		startCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(start[0]));
		startCal.set(Calendar.MINUTE, Integer.parseInt(start[1]));
		startCal.set(Calendar.SECOND, 0);
		
		if (Calendar.getInstance().after(stopCal) && Calendar.getInstance().before(startCal)) {
			infoID = "0";
			infoMsg = "";
		} else {
			//log.debug("Alert Engine working.-->");
			JSONObject obj = new JSONObject(apiResponse);
			responseObj = obj.getJSONObject("response");
			infoID = responseObj.getString("infoID");
			infoMsg = responseObj.optString("infoMsg");
		}
	}
	
	public String getInfoID()
	{
		return infoID;
	}

}
