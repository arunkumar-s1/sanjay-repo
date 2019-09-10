package com.msf.libsb.loginhelpers;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;


public class ChangePasswordRequest extends BaseRequest 
{
	/**
	 * For Changing users password
	 * @param sAPI_URL
	 * @throws SamcoException
	 */
	public ChangePasswordRequest(String sAPI_URL) throws SamcoException
	{
		super(sAPI_URL);
	}
	
	public void setUserID(String sUID) throws Exception 
	{
		this.put(APIConstants.USER_ID, sUID);
	}
	
	public void setOldPwd(String sOldP) throws Exception
	{
		this.put(APIConstants.CP_OLD_PWD, sOldP);
	}
	
	public void setNewPwd(String sNewP) throws Exception
	{
		this.put(APIConstants.CP_NEW_PWD, sNewP);
	}
	
	public void setOldTxPwd(String sOldTP) throws Exception
	{
		this.put(APIConstants.CP_OLD_TX_PWD, sOldTP);
	}
	
	public void setSetNewTxPwd(String sNewTP) throws Exception
	{
		this.put(APIConstants.CP_NEW_TX_PWD, sNewTP);
	}
	
	public void setIsTxChange(String isTX) throws Exception
	{
		this.put(APIConstants.CP_IS_TX, isTX);
	}
	
	@Override
	public BaseResponse postRequest() throws Exception
	{
		String apiResponse = postToApi();
		
		return new ChangePasswordResponse(apiResponse);
	}

}


