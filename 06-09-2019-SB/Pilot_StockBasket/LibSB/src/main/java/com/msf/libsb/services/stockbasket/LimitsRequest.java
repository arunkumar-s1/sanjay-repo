package com.msf.libsb.services.stockbasket;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;


public class LimitsRequest extends BaseRequest {
	
	public LimitsRequest(String sAPI_URL) throws SamcoException {
		super(sAPI_URL);
	}
	
	public void setUserID(String sUID) throws Exception {
		this.put(APIConstants.USER_ID, sUID);
	}
	
	public void setAccount(String sAccount) throws Exception {
		this.put(APIConstants.O_ACCOUNT_ID, sAccount);
	}
	
	public void setSegment(String sSegment) throws Exception {
		this.put(APIConstants.LIMITS_SEGMENT, sSegment);
	}
	
	@Override
	public BaseResponse postRequest() throws Exception {
		String apiResponse = postToApi();
		
		return new LimitsResponse(apiResponse);
	}

}