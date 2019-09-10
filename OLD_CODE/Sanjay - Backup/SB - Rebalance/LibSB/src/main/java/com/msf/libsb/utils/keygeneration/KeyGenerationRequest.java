package com.msf.libsb.utils.keygeneration;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.utils.exception.SamcoException;


public class KeyGenerationRequest extends BaseRequest
{
	/**
	 * As it is the first step of connection with samco-servers, it does not have any fields
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public KeyGenerationRequest(String sAPI_URL) throws SamcoException
	{
		super(sAPI_URL);
	}

	@Override
	public BaseResponse postRequest() throws Exception
	{
		String apiResponse = postToApi();

		return new KeyGenerationResponse(apiResponse);
	}

}
