package com.msf.libsb.loginhelpers;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;

public class ForgotPasswordRequest extends BaseRequest
{
	/**
	 * In case user forgets his password
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public ForgotPasswordRequest(String sAPI_URL) throws SamcoException
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
	
	public void setDOB(String sDOB) throws Exception 
	{
		this.put(APIConstants.FP_DOB, sDOB);
	}
	
	@Override
	public BaseResponse postRequest() throws Exception 
	{
		String apiResponse = postToApi();
		
		return new ForgotPasswordResponse(apiResponse);
	}

}


