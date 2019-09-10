package com.msf.libsb.logout;
import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;

public class LogOutRequest extends BaseRequest 
{
	/**
	 * For user session clean up at TR, logout
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public LogOutRequest(String sAPI_URL) throws SamcoException
	{
		super(sAPI_URL);
	}
	
	public void setUserID(String sUID) throws Exception 
	{
		this.put(APIConstants.USER_ID, sUID);
	}
	
	@Override
	public BaseResponse postRequest() throws Exception 
	{
		String apiResponse = postToApi();		
		return new LogOutResponse(apiResponse);
	}

}


