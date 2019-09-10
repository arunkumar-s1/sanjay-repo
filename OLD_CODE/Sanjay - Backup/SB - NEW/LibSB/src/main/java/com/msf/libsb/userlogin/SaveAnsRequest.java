package com.msf.libsb.userlogin;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;


public class SaveAnsRequest extends BaseRequest 
{
	/**
	 * For 1st time login, saving answers
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public SaveAnsRequest(String sAPI_URL) throws SamcoException
	{
		super(sAPI_URL);
	}

	public void setUserID(String sUID) throws Exception 
	{
		this.put(APIConstants.USER_ID, sUID);
	}

	public void setIndexAns(String sSource) throws Exception
	{
		this.put(APIConstants.SAVE_ANS_QA, sSource);
	}

	@Override
	public BaseResponse postRequest() throws Exception
	{
		String apiResponse = postToApi();

		return new SaveAnsResponse(apiResponse);
	}

}