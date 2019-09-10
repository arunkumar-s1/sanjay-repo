package com.msf.libsb.loginhelpers;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;

public class Reset2FAResponse extends BaseResponse
{
	
	public Reset2FAResponse(String sAPIResponse) throws Exception
	{
		super(sAPIResponse);
	}
	
	public String getResult() throws Exception 
	{
		return this.getString(APIConstants.RESULT);
	}
	
	@Override
	public String getErrorID() 
	{
		if (!this.isSuccess)
		{
			if (this.errorMessage.equalsIgnoreCase("Not able to Retrieve Reset2FA"))
			{
				return INFO_IDS.RESET_2FA_FAILED;
			} 
			
			else if(this.errorMessage.equalsIgnoreCase("Invalid User"))
			{
				return INFO_IDS.INVALID_USER;
			}
			
			else if (this.errorMessage.equalsIgnoreCase("Email ID does not match"))
			{
				return INFO_IDS.INVALID_EMAILID;
			} 
			
			else if (this.errorMessage.equalsIgnoreCase("PAN number does not match")) 
			{
				return INFO_IDS.INVALID_PAN;
			}
			
			else if (this.errorMessage.equalsIgnoreCase("Failed to reset user. Kindly try again later."))
			{
				return INFO_IDS.RESET_2FA_FAILED;
			}

			else
			{
				return INFO_IDS.RESET_2FA_FAILED;
			}
		}
		return INFO_IDS.SUCCESS;
	}
}

