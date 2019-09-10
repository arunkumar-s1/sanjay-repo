package com.msf.libsb.userlogin;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;

public class ValidAnsRequest extends BaseRequest 
{
	/**
	 * For validating answers
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public ValidAnsRequest(String sAPI_URL) throws SamcoException 
	{
		super(sAPI_URL);
	}

	public void setUserID(String sUID) throws Exception 
	{
		this.put(APIConstants.USER_ID, sUID);
	}

	public void setCount(String sCount) throws Exception
	{
		this.put(APIConstants.VAL_ANS_COUNT, sCount);
	}

	public void setIS(String val) throws Exception
	{
		this.put(APIConstants.VAL_ANS_IS, val);
	}

	public void setAS(String val) throws Exception
	{
		this.put(APIConstants.VAL_ANS_AS, val);
	}

	public void setTime(String val) throws Exception
	{
		this.put(APIConstants.VAL_ANS_TIME, val);
	}

	@Override
	public BaseResponse postRequest() throws Exception 
	{
		String apiResponse = postToApi();

		return new ValidAnsResponse(apiResponse);
	}

}

