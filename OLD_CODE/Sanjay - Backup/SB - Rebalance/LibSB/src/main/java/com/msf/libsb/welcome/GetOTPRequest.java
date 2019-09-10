package com.msf.libsb.welcome;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;

public class GetOTPRequest extends BaseRequest 
{

	public GetOTPRequest(String sUrl) throws Exception
	{
		super(sUrl);
	}

	@Override
	public BaseResponse postRequest() throws Exception 
	{
		String sResponse = postToApi();
		GetOTPResponse resp = new GetOTPResponse(sResponse);
		return resp;
	}
}
