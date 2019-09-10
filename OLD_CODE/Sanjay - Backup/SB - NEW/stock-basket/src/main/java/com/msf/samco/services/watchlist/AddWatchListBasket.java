package com.msf.samco.services.watchlist;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.watchlist.WatchListBasketRequest;
import com.msf.libsb.services.watchlist.WatchListBasketResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class AddWatchListBasket extends SessionServlet 
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);

		WatchListBasketRequest addWatchListReq = new WatchListBasketRequest();
		addWatchListReq.setBasketName(basketName);
		addWatchListReq.setUserID(ssRequest.getSession().getUserId());

		WatchListBasketResponse addWatchListRes = addWatchListReq.addUserWatchListBasket();
		ssResponse.addToData(APP_CONSTANT.MESSAGE, addWatchListRes.getMessage());
	}

	@Override
	protected String getSvcName() 
	{
		return "AddWatchListBasket";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "WatchList";
	}

	@Override
	protected String getSvcVersion()
	{
		return "1.0.0";
	}

}
