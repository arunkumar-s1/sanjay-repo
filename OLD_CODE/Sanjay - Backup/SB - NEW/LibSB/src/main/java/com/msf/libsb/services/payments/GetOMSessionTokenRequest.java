package com.msf.libsb.services.payments;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;

public class GetOMSessionTokenRequest extends BaseRequest
{
	
	public GetOMSessionTokenRequest(String url) throws Exception 
	{
		super(url);
	}
	
	public void setUserID(String uid) throws Exception 
	{
		this.put(APIConstants.USER_ID, uid);
	}
	
	@Override
	public BaseResponse postRequest() throws Exception 
	{
		String sResponse = postToApi();
		return new GetOMSessionTokenResponse(sResponse);
	}

}
