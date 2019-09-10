package com.msf.samco.services.payments;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.payments.PayingRequest;
import com.msf.libsb.services.payments.PayingResponse;
import com.msf.log.Logger;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;


public class PayinService extends SessionServlet
{

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(PayinService.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{

		PayingRequest payinReq = new PayingRequest(ssRequest.getSession());
		PayingResponse payinResp = (PayingResponse)payinReq.postRequest();

		ssResponse.addToData(APP_CONSTANT.PAY_IN_URL, payinResp.getPayInURL());
	}

	@Override
	protected String getSvcName() 
	{
		return "Payin";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "Payments";
	}

	@Override
	protected String getSvcVersion()
	{
		return "1.0.0";
	}

}
