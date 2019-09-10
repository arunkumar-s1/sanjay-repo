package com.msf.libsb.alert;

import org.json.me.JSONException;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APP_CONSTANT;

public class AddAlertResponse extends BaseResponse 
{
	private String alertId="";
	public AddAlertResponse() 
	{
		super();
	}

	public void setAlertID(String alertId) throws JSONException
	{	
		this.alertId=alertId;
	}
	
	public String getAlertID() throws JSONException
	{		
		return alertId;
	}
}
