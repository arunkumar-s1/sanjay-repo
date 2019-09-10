package com.msf.samco.services.order;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.order.PlaceAMOOrderResponse;
import com.msf.libsb.services.order.PlaceBasketOrderRequest;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class PlaceAMOOrder extends SessionServlet
{

	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(PlaceBasketOrder.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);
		int qty=ssRequest.getIntFromData(APP_CONSTANT.QUANTITY);
		String transactionType=ssRequest.getFromData(APP_CONSTANT.TRANSACTION_TYPE);
		
		PlaceBasketOrderRequest orderReq = new PlaceBasketOrderRequest();
		orderReq.setSession( ssRequest.getSession());
		orderReq.setBasketName(basketName);
		orderReq.setQty(qty);
		orderReq.setTransactionType(transactionType);
		
		PlaceAMOOrderResponse orderRes = orderReq.placeAMOOrder();		
		ssResponse.addToData(APP_CONSTANT.MESSAGE, orderRes.getMessage());
	}

	@Override
	protected String getSvcName() 
	{
		return "PlaceAMOOrder";
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
