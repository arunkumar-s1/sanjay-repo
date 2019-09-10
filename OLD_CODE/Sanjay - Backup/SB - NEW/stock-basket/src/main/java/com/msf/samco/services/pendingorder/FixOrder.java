package com.msf.samco.services.pendingorder;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.libsb.services.pendingorders.FixOrderRequest;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class FixOrder extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(FixBasketOrder.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		String orderNo = ssRequest.getFromData(APP_CONSTANT.BASKET_ORDER_NO);
		
		FixOrderRequest fixReq = new FixOrderRequest();
		fixReq.setSession( ssRequest.getSession());
		fixReq.setOrderNo(orderNo);
		
		PlaceBasketOrderResponse fixRes = fixReq.doOperations();
		
		ssResponse.addToData(APP_CONSTANT.BASKET_NAME, fixRes.getBasketName());
		//ssResponse.addToData(APP_CONSTANT.ICON_URL, fixRes.getIconURL());
		ssResponse.addToData(APP_CONSTANT.ORDER_STATUS, fixRes.getOrderStatus());
		ssResponse.addToData(APP_CONSTANT.MESSAGE, fixRes.getMessage());
		ssResponse.addToData(APP_CONSTANT.NET_PRICE, fixRes.getNetPrice());
		ssResponse.addToData(APP_CONSTANT.ESTIMATED_TAX, fixRes.getEstimatedTax());
		ssResponse.addToData(APP_CONSTANT.BASKET_ORDER_NO, fixRes.getBasketOrderNo());
		ssResponse.addToData(APP_CONSTANT.TRANSACTION_TYPE, fixRes.getTransactionType());	
		ssResponse.addToData(APP_CONSTANT.ORIGINAL_QTY, fixRes.getOriginalQty());
		ssResponse.addToData(APP_CONSTANT.FILLED_QTY, fixRes.getFilledQty());
		ssResponse.addToData(APP_CONSTANT.STOCKS, fixRes.getStocks());
	}

	@Override
	protected String getSvcName() 
	{
		return "FixOrder";
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
