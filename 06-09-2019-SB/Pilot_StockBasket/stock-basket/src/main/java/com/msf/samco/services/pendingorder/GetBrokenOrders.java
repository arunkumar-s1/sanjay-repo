package com.msf.samco.services.pendingorder;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.operations.PendingOrdersOperations;
import com.msf.libsb.services.pendingorders.GetPendingOrdersResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class GetBrokenOrders extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(FixBasketOrder.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		GetPendingOrdersResponse res = PendingOrdersOperations.getBrokenOrders(ssRequest.getSession().getUserId());
		ssResponse.addToData(APP_CONSTANT.ORDER_ARR, res.getOrderArr());
	}

	@Override
	protected String getSvcName() 
	{
		return "GetBrokenOrders";
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
