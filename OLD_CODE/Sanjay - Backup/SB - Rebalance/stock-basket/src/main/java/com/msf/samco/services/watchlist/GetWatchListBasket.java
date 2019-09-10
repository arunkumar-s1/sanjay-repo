package com.msf.samco.services.watchlist;


import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.watchlist.GetUserWatchListRequest;
import com.msf.libsb.services.watchlist.GetUserWatchListResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class GetWatchListBasket extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(GetWatchListBasket.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		GetUserWatchListRequest getWatchListReq = new GetUserWatchListRequest();
		getWatchListReq.setUserId(ssRequest.getSession().getUserId());
		
		GetUserWatchListResponse getRes =getWatchListReq.doOperations();
		ssResponse.addToData(APP_CONSTANT.WATCHLIST_ARR, getRes.getWatchListArr());
	}

	@Override
	protected String getSvcGroup() 
	{
		return "StockBasket";
	}
	
	@Override
	protected String getSvcName() 
	{
		return "GetWatchListBasket";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}

}
