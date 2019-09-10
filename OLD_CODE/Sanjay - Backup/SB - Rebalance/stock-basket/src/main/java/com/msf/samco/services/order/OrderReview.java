package com.msf.samco.services.order;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.order.OrderReviewRequest;
import com.msf.libsb.services.order.OrderReviewResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class OrderReview extends SessionServlet 
{

	private static final long serialVersionUID = 1L;

//	private static Logger log = Logger.getLogger(QuoteOverview.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);
		String basketVersion=ssRequest.getData().optString(APP_CONSTANT.BASKET_VERSION);
		int qty=ssRequest.getIntFromData(APP_CONSTANT.QUANTITY);
		
		OrderReviewRequest orderReviewRequest=new OrderReviewRequest();
		orderReviewRequest.setBasketName(basketName);
		orderReviewRequest.setQty(qty);
		orderReviewRequest.setBasketVersion(basketVersion);
		
		OrderReviewResponse orderReviewResponse=orderReviewRequest.doOperations();
		
		ssResponse.addToData(APP_CONSTANT.BASKET_NAME,basketName);
		ssResponse.addToData(APP_CONSTANT.ICON_URL,orderReviewResponse.getIconURL());
		ssResponse.addToData(APP_CONSTANT.SYMBOLS,orderReviewResponse.getSymbolArray());
		ssResponse.addToData(APP_CONSTANT.NET_PRICE,SamcoHelper.getIndianCurrencyFormat(orderReviewResponse.getNetPrice()));
		ssResponse.addToData(APP_CONSTANT.ESTIMATED_TAX,SamcoHelper.getIndianCurrencyFormat(orderReviewResponse.getEstimatedTax()));
		ssResponse.addToData(APP_CONSTANT.ESTIMATED_FEE,SamcoHelper.getIndianCurrencyFormat(orderReviewResponse.getEstimatedFees()));
		ssResponse.addToData(APP_CONSTANT.TOTAL_PRICE,SamcoHelper.getIndianCurrencyFormat(orderReviewResponse.getTotalPrice()));
		ssResponse.addToData(APP_CONSTANT.MIN_AMT, SamcoHelper.getIndianCurrencyFormat(orderReviewResponse.getMinAmount()));
	}

	@Override
	protected String getSvcName() 
	{
		return "OrderReview";
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
