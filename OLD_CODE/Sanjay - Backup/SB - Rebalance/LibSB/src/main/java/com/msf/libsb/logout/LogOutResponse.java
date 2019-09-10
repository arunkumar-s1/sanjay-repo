package com.msf.libsb.logout;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.INFO_IDS;

public class LogOutResponse extends BaseResponse {
	
	public LogOutResponse(String sAPIResponse) throws Exception 
	{
		super(sAPIResponse);
	}
	
	@Override
	public String getErrorID() 
	{
		if (!this.isSuccess) 
		{
			
			if(this.errorMessage.equalsIgnoreCase("Session Expired"))
			{
				return INFO_IDS.INVALID_SESSION;
			}

			else if(this.errorMessage.contains("Not able to Retrieve Logout"))
			{
				return INFO_IDS.LOGOUT_FAILED;
			}
			
			else 
			{
				return INFO_IDS.LOGOUT_FAILED;
			}
		}
		return INFO_IDS.SUCCESS;
	}
}
