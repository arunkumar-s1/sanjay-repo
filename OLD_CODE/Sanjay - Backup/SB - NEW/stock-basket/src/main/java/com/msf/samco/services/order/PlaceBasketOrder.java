package com.msf.samco.services.order;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.order.PlaceBasketOrderRequest;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class PlaceBasketOrder extends SessionServlet
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

		PlaceBasketOrderResponse orderRes = orderReq.doOperations();
		ssResponse.addToData(APP_CONSTANT.BASKET_NAME, basketName);
		ssResponse.addToData(APP_CONSTANT.ICON_URL, orderRes.getIconURL());
		ssResponse.addToData(APP_CONSTANT.ORDER_STATUS, orderRes.getOrderStatus());
		ssResponse.addToData(APP_CONSTANT.MESSAGE, orderRes.getMessage());
		ssResponse.addToData(APP_CONSTANT.NET_PRICE, orderRes.getNetPrice());
		ssResponse.addToData(APP_CONSTANT.ESTIMATED_TAX, orderRes.getEstimatedTax());
		ssResponse.addToData(APP_CONSTANT.BASKET_ORDER_NO, orderRes.getBasketOrderNo());
		ssResponse.addToData(APP_CONSTANT.TRANSACTION_TYPE, orderRes.getTransactionType());
		ssResponse.addToData(APP_CONSTANT.ORIGINAL_QTY, orderRes.getOriginalQty());
		ssResponse.addToData(APP_CONSTANT.FILLED_QTY, orderRes.getFilledQty());
		ssResponse.addToData(APP_CONSTANT.STOCKS, orderRes.getStocks());
	}

	@Override
	protected String getSvcName()
	{
		return "PlaceBasketOrder";
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
