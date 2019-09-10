package com.msf.libsb.loginhelpers;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.INFO_IDS;

public class ForgotPasswordResponse extends BaseResponse 
{
	
	public ForgotPasswordResponse(String sAPIResponse) throws Exception
	{
		super(sAPIResponse);
	}
	
	@Override
	public String getErrorID() 
	{
		if (!this.isSuccess) 
		{
			if (this.errorMessage.equalsIgnoreCase("Not able to Retrieve ForgotPassword")) 
			{
				return INFO_IDS.FORGOT_PASSWORD_FAILED;
			} 
			
			else if(this.errorMessage.equalsIgnoreCase("Session Expired"))
			{
				return INFO_IDS.INVALID_SESSION;
			}
			
			else if (this.errorMessage.equalsIgnoreCase("Please enter correct PAN number")) 
			{
				return INFO_IDS.INVALID_PAN;
			} 
			
			else if (this.errorMessage.equalsIgnoreCase("email id does not match"))
			{
				return INFO_IDS.INVALID_EMAILID;
			}
			
			else if (this.errorMessage.equalsIgnoreCase("User Does Not Exist")) 
			{
				return INFO_IDS.INVALID_USER;
			}
			
			else 
			{
				return INFO_IDS.FORGOT_PASSWORD_FAILED;
			}
		}
		return INFO_IDS.SUCCESS;
	}
}
