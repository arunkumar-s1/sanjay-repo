package com.msf.samco.services.pendingorder;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.operations.FixRebalanceCountOperations;
import com.msf.libsb.services.order.FixRebalanceCountResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class FixRebalanceCount extends SessionServlet
{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{	

		FixRebalanceCountResponse fixRebalRes = FixRebalanceCountOperations.fixRebalanceCount(ssRequest.getSession().getUserId());
		ssResponse.addToData(APP_CONSTANT.FIX_COUNT, fixRebalRes.getFixCount());
		ssResponse.addToData(APP_CONSTANT.REBALANCE_COUNT, fixRebalRes.getRebalanceCount());
		
	}

	@Override
	protected String getSvcName()
	{
		return "FixRebalanceCount";
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
