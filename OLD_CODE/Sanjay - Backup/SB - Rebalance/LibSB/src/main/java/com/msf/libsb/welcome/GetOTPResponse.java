package com.msf.libsb.welcome;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APP_CONSTANT;

public class GetOTPResponse extends BaseResponse
{
	public GetOTPResponse(String response) throws Exception
	{
		super(response);
	}

	public boolean isSucess() throws Exception
	{
		if (this.getString(APP_CONSTANT.STATUS).equalsIgnoreCase(APP_CONSTANT.SUCCESS))
			return true;
		return false;
	}
	
	public String getOTPNumber() throws Exception 
	{
		return this.getString(APP_CONSTANT.DATA);
	}
}
