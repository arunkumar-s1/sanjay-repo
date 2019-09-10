package com.msf.libsb.services.payments;

import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.Session;
import com.msf.log.Logger;

public class PayingRequest extends BaseRequest 
{
	
	private Logger log = Logger.getLogger(PayingRequest.class);
	
	private JSONObject jPayinObj = null;
	private Session session = null;
	
	public PayingRequest(Session session) throws Exception 
	{
		this.session = session;
		String sPayinObj = this.session.getPayinObj();
		log.debug(sPayinObj);
		jPayinObj = new JSONObject(sPayinObj);
	}

	@Override
	public BaseResponse postRequest() throws Exception 
	{	
		String sTokenUrl = AppConfig.getConfigValue("get_om_session_token_url");
		GetOMSessionTokenRequest omTokenReq = new GetOMSessionTokenRequest(sTokenUrl);
		omTokenReq.setUserID(session.getUserId());
		omTokenReq.setKey(session.getJkey());
		omTokenReq.setSession(session.getJsession());
		
		GetOMSessionTokenResponse omTokenRes = (GetOMSessionTokenResponse)omTokenReq.postRequest();
		
		if (!omTokenRes.isSuccessResponse()) 
		{
			throw new SamcoException(omTokenRes.getErrorID());
		}
		
		String sToken = omTokenRes.getToken();
		String payInURL = jPayinObj.getString(APIConstants.URL) + "?sLoginId=" + session.getUserId() + 
				"&sAccountId=" + session.getAccountID() + "&token=" + sToken;
		
		PayingResponse resp = new PayingResponse();
		resp.setPayInURL(payInURL);
		return resp;
	}
	
	

}
