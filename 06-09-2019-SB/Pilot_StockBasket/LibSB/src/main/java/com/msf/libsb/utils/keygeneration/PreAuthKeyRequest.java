package com.msf.libsb.utils.keygeneration;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.utils.exception.SamcoException;

public class PreAuthKeyRequest extends BaseRequest
{
	/**
	 * For Pre Auth Key request jData is middleware processed public key 2
	 * and jKey is SHA256 encoded public key 1, so appending these values to config url and setting it directly with ctor
	 * 
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public PreAuthKeyRequest(String sAPI_URL) throws SamcoException 
	{
		super(sAPI_URL);
	}
	
	@Override
	public BaseResponse postRequest() throws Exception
	{
		String apiResponse = postToApi();
		
		return new PreAuthKeyResponse(apiResponse);
	}

}
