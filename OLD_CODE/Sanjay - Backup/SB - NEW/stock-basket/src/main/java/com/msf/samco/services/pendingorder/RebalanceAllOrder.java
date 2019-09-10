package com.msf.samco.services.pendingorder;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.pendingorders.RebalanceOrderRequest;
import com.msf.libsb.services.pendingorders.RebalanceOrderResponse;
import com.msf.libsb.utils.helper.Session;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class RebalanceAllOrder extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(FixBasketOrder.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		String orderNo = ssRequest.getFromData(APP_CONSTANT.BASKET_ORDER_NO);
		String basketName=ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);
		
		RebalanceOrderRequest rebalanceReq = new RebalanceOrderRequest();
		rebalanceReq.setSession(ssRequest.getSession());
		rebalanceReq.setBasketName(basketName);
		rebalanceReq.setOrderNo(orderNo);
		
//		RebalanceOrderResponse rebalanceRes = (RebalanceOrderResponse)rebalanceReq.doOperations();
//		ssResponse.addToData("versionDiff", rebalanceRes.getVersionDiff());
//		ssResponse.addToData("rebalanceAmt", rebalanceRes.getRebalanceAmt());

	}

	@Override
	protected String getSvcName() 
	{
		return "RebalanceAllOrder";
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
