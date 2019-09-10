package com.msf.libsb.services.payments;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;

public class PayingResponse extends BaseResponse 
{

	private String payInURL;

	public String getPayInURL()
	{
		return payInURL;
	}

	public void setPayInURL(String payInURL)
	{
		this.payInURL = payInURL;
	}

}
