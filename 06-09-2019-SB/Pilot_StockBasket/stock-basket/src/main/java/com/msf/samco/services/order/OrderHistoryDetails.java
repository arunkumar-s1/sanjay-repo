package com.msf.samco.services.order;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.operations.OrderHistoryOperations;
import com.msf.libsb.services.order.OrderHistoryDetailsRequest;
import com.msf.libsb.services.order.OrderHistoryDetailsResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class OrderHistoryDetails extends SessionServlet
{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{				
		String basketName=ssRequest.getData().getString(APP_CONSTANT.BASKET_NAME);
		
		OrderHistoryDetailsRequest orderHisDetReq =new OrderHistoryDetailsRequest();
		orderHisDetReq.setBasketName(basketName);
		orderHisDetReq.setUserId(ssRequest.getSession().getUserId());
		
		OrderHistoryDetailsResponse orderHisDetRes = OrderHistoryOperations.orderHistoryDetails(orderHisDetReq);
		ssResponse.addToData(APP_CONSTANT.ORDER_HISTORY_ARR, orderHisDetRes.getOrderArr());		
	}

	@Override
	protected String getSvcName()
	{
		return "OrderHistoryDetails";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "Order";
	}
	
	@Override	
	protected String getSvcVersion() 
	{
		return "1.0.0";
	}
}
