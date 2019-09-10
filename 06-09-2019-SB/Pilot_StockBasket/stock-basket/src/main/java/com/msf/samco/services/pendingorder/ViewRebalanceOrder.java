package com.msf.samco.services.pendingorder;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.pendingorders.ViewPendingOrderRequest;
import com.msf.libsb.services.pendingorders.ViewPendingOrderResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class ViewRebalanceOrder extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(FixBasketOrder.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		String basketName=ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);
		String orderNo = ssRequest.getFromData(APP_CONSTANT.BASKET_ORDER_NO);
		
		ViewPendingOrderRequest viewReq = new ViewPendingOrderRequest();
		viewReq.setOrderNo(orderNo);
		viewReq.setBasketName(basketName);
		
		ViewPendingOrderResponse viewRes = viewReq.postRequest();
		ssResponse.addToData("pendingVersions",viewRes.getPendingVersions());
		ssResponse.addToData(APP_CONSTANT.SYMBOLS,viewRes.getSymbolArr());
		ssResponse.addToData(APP_CONSTANT.NET_PRICE,SamcoHelper.getIndianCurrencyFormat(viewRes.getNetPrice()));
		ssResponse.addToData(APP_CONSTANT.ESTIMATED_TAX,SamcoHelper.getIndianCurrencyFormat(viewRes.getEstimatedTax()));
		ssResponse.addToData(APP_CONSTANT.TOTAL_PRICE,SamcoHelper.getIndianCurrencyFormat(viewRes.getTotalPrice()));

	}

	@Override
	protected String getSvcName() 
	{
		return "ViewRebalanceOrder";
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
