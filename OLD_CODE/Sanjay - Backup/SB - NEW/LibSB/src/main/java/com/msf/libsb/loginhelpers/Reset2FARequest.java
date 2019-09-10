package com.msf.libsb.loginhelpers;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;

public class Reset2FARequest extends BaseRequest
{
	/**
	 * In case account gets blocked
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public Reset2FARequest(String sAPI_URL) throws SamcoException
	{
		super(sAPI_URL);
	}
	
	public void setUserID(String sUID) throws Exception
	{
		this.put(APIConstants.USER_ID, sUID);
	}
	
	public void setEmail(String sEmail) throws Exception
	{
		this.put(APIConstants.FP_EMAIL, sEmail);
	}
	
	public void setPan(String sPan) throws Exception
	{
		this.put(APIConstants.FP_PAN_NUMBER, sPan);
	}
	
	@Override
	public BaseResponse postRequest() throws Exception 
	{
		String apiResponse = postToApi();
		
		return new Reset2FAResponse(apiResponse);
	}

}