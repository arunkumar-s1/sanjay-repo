package com.msf.libsb.services.payments;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;

public class GetOMSessionTokenResponse extends BaseResponse
{

	public GetOMSessionTokenResponse(String sResponse) throws Exception
	{
		super(sResponse);
	}

	public String getToken() throws Exception
	{
		return this.getString(APIConstants.OM_TOKEN);
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
				return INFO_IDS.REQUEST_FAILED;
			}
		}
		return INFO_IDS.SUCCESS;
	}

}
