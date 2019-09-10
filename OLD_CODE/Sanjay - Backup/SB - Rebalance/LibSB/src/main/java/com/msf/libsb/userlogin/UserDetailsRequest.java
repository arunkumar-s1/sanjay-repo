package com.msf.libsb.userlogin;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;

public class UserDetailsRequest extends BaseRequest
{

	public UserDetailsRequest(String sUrl) throws Exception 
	{
		super(sUrl);
	}

	public void setAccID(String sUid) throws Exception
	{
		this.put(APIConstants.UD_REQ_ACC, sUid);
	}

	@Override
	public BaseResponse postRequest() throws Exception
	{
		String response = postToApi();
		UserDetailsResponse detailsResponse = new UserDetailsResponse(response);
		return detailsResponse;
	}

}
