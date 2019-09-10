package com.msf.libsb.userlogin;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;


public class ValidPwdRequest extends BaseRequest 
{
	/**
	 * For validating users password using Password Authentication API
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public ValidPwdRequest(String sAPI_URL) throws SamcoException
	{
		super(sAPI_URL);
	}

	public void setUserID(String sUID) throws Exception
	{
		this.put(APIConstants.USER_ID, sUID);
	}

	public void setPwd(String sHashedPwd) throws Exception 
	{
		this.put(APIConstants.VAL_PWD_HASHED_PWD, sHashedPwd);
	}

	public void setFirstLogin(String sFirst) throws Exception
	{
		this.put(APIConstants.VAL_PWD_FTL, sFirst);
	}

	public void setAPK(String sAPK) throws Exception 
	{
		this.put(APIConstants.VAL_PWD_APK, sAPK);
	}

	public void setIMEI(String sIMEI) throws Exception 
	{
		this.put(APIConstants.VAL_PWD_IMEI, sIMEI);
	}

	public void setDevice(String sDevice) throws Exception 
	{
		this.put(APIConstants.VAL_PWD_DEVICE, sDevice);
	}

	public void setSource(String sSource) throws Exception 
	{
		this.put(APIConstants.VAL_PWD_SOURCE, sSource);
	}

	@Override
	public BaseResponse postRequest() throws Exception 
	{
		String apiResponse = postToApi();

		return new ValidPwdResponse(apiResponse);
	}

}

