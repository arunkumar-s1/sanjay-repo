package com.msf.samco.services.watchlist;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.watchlist.WatchListBasketRequest;
import com.msf.libsb.services.watchlist.WatchListBasketResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class DeleteWatchListBasket extends SessionServlet 
{
	private static final long serialVersionUID = 1L;

	//private static Logger log = Logger.getLogger(DeleteWatchListBasket.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);

		WatchListBasketRequest deleteWatchListReq = new WatchListBasketRequest();
		deleteWatchListReq.setBasketName(basketName);
		deleteWatchListReq.setUserID(ssRequest.getSession().getUserId());

		WatchListBasketResponse deleteWatchListRes = deleteWatchListReq.deleteUserWatchListBasket();
		ssResponse.addToData(APP_CONSTANT.MESSAGE, deleteWatchListRes.getMessage());
	}

	@Override
	protected String getSvcName() {
		return "DeleteWatchListBasket";
	}

	@Override
	protected String getSvcGroup() {
		return "WatchList";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}

}
