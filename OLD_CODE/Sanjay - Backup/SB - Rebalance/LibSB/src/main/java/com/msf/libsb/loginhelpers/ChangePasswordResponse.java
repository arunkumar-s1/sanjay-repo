package com.msf.libsb.loginhelpers;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.INFO_IDS;

public class ChangePasswordResponse extends BaseResponse
{

	public ChangePasswordResponse(String sAPIResponse) throws Exception
	{
		super(sAPIResponse);
	}

	@Override
	public String getErrorID()
	{
		if (!this.isSuccess)
		{
			if (this.errorMessage.equalsIgnoreCase("Not able to Retrieve Change password"))
			{
				return INFO_IDS.CHANGE_PASSWORD_FAILED;
			}

			else if (this.errorMessage.equalsIgnoreCase("Session Expired"))
			{
				return INFO_IDS.INVALID_SESSION;
			}

			else if (this.errorMessage.equalsIgnoreCase(new String(
					"Error while changing Login Password and transaction password , Password could not be changed. Password entered was among previous passwords."))) 
			{
				return INFO_IDS.RESET_PASSWORD_FAILED;
			}
			else if (this.errorMessage.equalsIgnoreCase(new String(
					"Error while changing Login Password , Password could not be changed. Password entered was among previous passwords."))) 
			{
				return INFO_IDS.RESET_PASSWORD_FAILED;
			}

			else if (this.errorMessage.equalsIgnoreCase(new String(
					"Error while changing Login Password and transaction password , API ERROR , ErrorCode : 1640"))) 
			{
				return INFO_IDS.RESET_PASSWORD_FAILED;
			}

			else if (this.errorMessage.contains("Error while changing Login Password")) 
			{
				return INFO_IDS.CHANGE_PASSWORD_FAILED;
			}

			else if (this.errorMessage.contains("New password doesnot satisfy the password policy."))
			{
				return INFO_IDS.CHANGE_PASSWORD_POLICY_FAILED;
			} 
			else 
			{
				return INFO_IDS.CHANGE_PASSWORD_FAILED;
			}
		}
		
		return INFO_IDS.SUCCESS;
	}
}
