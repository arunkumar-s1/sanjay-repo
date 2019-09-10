package com.msf.samco.services.order;

import org.json.me.JSONArray;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.operations.ExitBasketOperations;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class ExitBasket extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(PlaceBasketOrder.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		JSONArray orderNo = ssRequest.getArrayFromData(APP_CONSTANT.ORDERS);
		int sellQty=ssRequest.getIntFromData(APP_CONSTANT.SELLQTY);
		
		PlaceBasketOrderResponse orderRes = ExitBasketOperations.exitOrder(ssRequest.getSession(),orderNo,sellQty);
		//ssResponse.addToData(APP_CONSTANT.BASKET_NAME, basketName);
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
		return "ExitBasket";
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
