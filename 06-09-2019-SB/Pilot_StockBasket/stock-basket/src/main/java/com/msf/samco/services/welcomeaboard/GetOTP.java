package com.msf.samco.services.welcomeaboard;

import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.welcome.GetOTPRequest;
import com.msf.libsb.welcome.GetOTPResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class GetOTP extends BaseServlet
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		JSONObject jDataObject = null;
		try 
		{
			jDataObject = ssRequest.getData();
			String sOTPAPINumber = "18";
			String sOTPToken = "Stk_!2_Sam";
			String sMobileNumber = jDataObject.getString(APP_CONSTANT.MOBILE);

			String sURL = AppConfig.getConfigValue("samco_otp_url") + "?api=" + sOTPAPINumber + "&otp_token=" + sOTPToken
					+ "&mob_no=" + sMobileNumber;

			GetOTPRequest request = new GetOTPRequest(sURL);
			GetOTPResponse response = (GetOTPResponse) request.postRequest();

			if (response.isSucess()) 
			{
				ssResponse.addToData(APP_CONSTANT.STATUS, "success");
				ssResponse.addToData(APP_CONSTANT.OTP, response.getOTPNumber());
			} 
			else 
			{
				throw new SamcoException(INFO_IDS.REQUEST_FAILED);
			}
		} 
		catch (Exception e)
		{
			throw new SamcoException(INFO_IDS.REQUEST_FAILED);
		}
	}

	@Override
	protected String getSvcName() 
	{
		return "GetOTP";
	}

	@Override
	protected String getSvcGroup()
	{
		return "Guest";
	}

	@Override
	protected String getSvcVersion() 
	{
		return "1.0.0";
	}

}
