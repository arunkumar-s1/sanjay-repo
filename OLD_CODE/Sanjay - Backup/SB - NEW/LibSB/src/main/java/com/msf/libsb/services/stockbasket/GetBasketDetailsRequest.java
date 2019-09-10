package com.msf.libsb.services.stockbasket;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.operations.BasketOperations;

public class GetBasketDetailsRequest extends JSONObject 
{
	public void setBasketName(String sBasketName) throws JSONException 
	{
		this.put(APP_CONSTANT.BASKET_NAME, sBasketName);
	}
	
	public void setBasketVersion(String sBasketVersion) throws JSONException 
	{
		this.put(APP_CONSTANT.BASKET_VERSION, sBasketVersion);
	}
	
	public GetBasketDetailsResponse doOperations() throws Exception 
	{
		return BasketOperations.getBasketDetails(this);
	}

}
