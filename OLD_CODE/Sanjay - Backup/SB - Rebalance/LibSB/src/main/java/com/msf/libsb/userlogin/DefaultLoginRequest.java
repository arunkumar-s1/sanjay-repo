package com.msf.libsb.userlogin;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;


public class DefaultLoginRequest extends BaseRequest
{
	/**
	 * For default login
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public DefaultLoginRequest(String sAPI_URL) throws SamcoException 
	{
		super(sAPI_URL);
	}
	
	public void setUserID(String sUID) throws Exception 
	{
		this.put(APIConstants.USER_ID, sUID);
	}
	
	public void setIMEI(String sVal) throws Exception
	{
		this.put(APIConstants.DEF_LOGIN_IMEI, sVal);
	}
	
	@Override
	public BaseResponse postRequest() throws Exception 
	{
		String apiResponse = postToApi();
		
		return new DefaultLoginResponse(apiResponse);
	}

}
