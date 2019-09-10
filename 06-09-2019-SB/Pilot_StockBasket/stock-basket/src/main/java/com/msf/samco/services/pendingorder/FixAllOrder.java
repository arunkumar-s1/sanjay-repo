package com.msf.samco.services.pendingorder;

import com.msf.libsb.services.pendingorders.FixAllOrderResponse;
import com.msf.libsb.services.pendingorders.FixOrderRequest;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class FixAllOrder extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(FixBasketOrder.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		FixOrderRequest fixReq = new FixOrderRequest();
		fixReq.setSession(ssRequest.getSession());
		
		FixAllOrderResponse fixRes = fixReq.postRequest();
		ssResponse.addToData("completedOrderCount", fixRes.getCompletedOrderCount());
		ssResponse.addToData("totalOrderCount", fixRes.getTotalOrderCount());
	}

	@Override
	protected String getSvcName() 
	{
		return "FixAllOrder";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "PendingOrder";
	}

	@Override
	protected String getSvcVersion() 
	{
		return "1.0.0";
	}
}
