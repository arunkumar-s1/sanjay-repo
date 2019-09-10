package com.msf.libsb.utils.keygeneration;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;

public class KeyGenerationResponse extends BaseResponse
{

	public KeyGenerationResponse(String sAPIResponse) throws Exception
	{
		super(sAPIResponse);
	}

	public String getPublicKey() throws Exception
	{
		return this.getString(APIConstants.GIK_PUBLIC_KEY);
	}

	public String getTomcatCount() throws Exception 
	{
		return this.getString(APIConstants.GIK_TOMCAT_COUNT);
	}
}
