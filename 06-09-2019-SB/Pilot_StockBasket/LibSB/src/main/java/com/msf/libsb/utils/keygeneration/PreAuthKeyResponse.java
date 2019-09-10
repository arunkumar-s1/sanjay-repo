package com.msf.libsb.utils.keygeneration;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;

public class PreAuthKeyResponse extends BaseResponse
{
	
	public PreAuthKeyResponse(String sAPIResponse) throws Exception
	{
		super(sAPIResponse);
	}
	
	public String getPublicKey3() throws Exception 
	{
		return this.getString(APIConstants.PAK_PUBLICKEY_3);
	}
}
