package com.msf.libsb.alert;

import java.util.Calendar;

import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.base.HttpHelper;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.jmx.Monitor;
import com.msf.libsb.jmx.SamcoServiceBean;
import com.msf.libsb.utils.exception.SamcoException;

public class AlertEngineRequest extends BaseRequest {

	private JSONObject requestObject = new JSONObject();

	private String apiURL = "";
	protected HttpHelper httpHelper;

	public AlertEngineRequest(String apiUrl) throws SamcoException {
		super();

		this.apiURL = apiUrl;
		httpHelper = new HttpHelper();
	}

	@Override
	public BaseResponse postRequest() throws Exception {
		String apiResponse = postToAlertEngine();

		return new AlertEngineResponse(apiResponse);
	}

	// public void addToHeader(String key, String value) {
	//
	// if (httpHelper != null)
	// httpHelper.addToHeader(key, value);
	// }

	public void setRequestObj(JSONObject dataObj) throws Exception {
		this.requestObject.put(APP_CONSTANT.DATA, dataObj);
		this.put("request", requestObject);
	}

	public void setAction(String action) throws Exception {
		this.requestObject.put("msg", action);
	}

	private String postToAlertEngine() throws Exception {

		SamcoServiceBean alertsBean = Monitor.getServiceBean(Monitor.ALERTS_API);

		try {
			httpHelper.postRequest(apiURL, this.toString());

		} catch (Exception e) {

			if (alertsBean != null)
				alertsBean.setFailure(e.getMessage());

			throw e;
		}

		if (httpHelper.getResponse() != null) {
			return httpHelper.getResponse();
		} else {
			
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
				//log.debug("During shutdown time ::: ");
				return "";
			} else {
				throw new SamcoException(INFO_IDS.RESPONSE_FAILURE);
			}

		}
	}
}
