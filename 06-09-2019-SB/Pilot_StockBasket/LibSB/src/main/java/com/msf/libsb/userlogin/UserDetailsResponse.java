package com.msf.libsb.userlogin;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;

public class UserDetailsResponse extends BaseResponse
{

	public UserDetailsResponse(String response) throws Exception 
	{
		super(response);
	}

	@Override
	public String getErrorID()
	{
		if (!this.isSuccess)
		{

			if (this.errorMessage.equalsIgnoreCase("Session Expired"))
			{
				return INFO_IDS.INVALID_SESSION;
			}
			else 
			{
				return INFO_IDS.USER_DETAILS_FAILED;
			}
		}
		return INFO_IDS.SUCCESS;
	}

	public String getMobile() throws Exception
	{
		return this.getString(APIConstants.UD_MOBILE);
	}

	public String getEmail() throws Exception
	{
		return this.getString(APIConstants.UD_EMAIL);
	}

	public String getDOB() throws Exception
	{
		return this.getString(APIConstants.UD_DOB);
	}

	public String getPAN() throws Exception 
	{
		return this.getString(APIConstants.UD_PAN_NO);
	}

	public String getName() throws Exception 
	{
		return this.getString(APIConstants.UD_NAME);
	}

	public String getDPName() throws Exception 
	{
		return this.getString(APIConstants.UD_DP_NAME);
	}
}
