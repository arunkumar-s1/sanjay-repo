package com.msf.libsb.userlogin;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.INFO_IDS;

public class SaveAnsResponse extends BaseResponse
{

	/**
	 * Not accesssing any response parameters except for status field
	 * @param sAPIResponse
	 * @throws Exception
	 */

	public SaveAnsResponse(String sAPIResponse) throws Exception
	{
		super(sAPIResponse);
	}

	@Override
	public String getErrorID() 
	{
		if (!this.isSuccess)
		{
			if (this.errorMessage.equalsIgnoreCase("Not able to Retrieve Save Answers")) 
			{
				return INFO_IDS.UNABLE_TO_PROCESS_LOGIN;
			} 

			else if(this.errorMessage.equalsIgnoreCase("Session Expired"))
			{
				return INFO_IDS.INVALID_SESSION;
			}

			else if(this.errorMessage.equalsIgnoreCase("Not able to save Questions and Answers"))
			{
				return INFO_IDS.UNABLE_TO_PROCESS_LOGIN;
			}

			else
			{
				return INFO_IDS.LOGIN_FAILED;
			}
		}

		return INFO_IDS.SUCCESS;
	}
}






