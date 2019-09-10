package com.msf.libsb.userlogin;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;

public class ValidAnsResponse extends BaseResponse
{

	public ValidAnsResponse(String sAPIResponse) throws Exception
	{
		super(sAPIResponse);
	}

	public String getToken() throws Exception
	{
		return this.getString(APIConstants.VAL_ANS_TOKEN);
	}

	public String getSession() throws Exception
	{
		return this.getString(APIConstants.VAL_ANS_SESSION);
	}

	public String getPwdRest() throws Exception
	{
		return this.getString(APIConstants.VAL_ANS_IS_RESET);
	}

	public String getTData() throws Exception
	{
		return this.getString(APIConstants.TDATA);
	}

	@Override
	public String getErrorID()
	{
		if (!this.isSuccess)
		{
			if (this.errorMessage.equalsIgnoreCase("User Blocked Contact System Administrator"))
			{
				return INFO_IDS.USER_BLOCKED;
			} 

			else if(this.errorMessage.equalsIgnoreCase("Session Expired"))
			{
				return INFO_IDS.INVALID_SESSION;
			}

			else if(this.errorMessage.equalsIgnoreCase("Invalid password for login"))
			{
				return INFO_IDS.INVALID_PASSWORD;
			}

			else 
			{
				return INFO_IDS.LOGIN_FAILED;
			}
		}

		return INFO_IDS.SUCCESS;
	}
}