package com.msf.samco.services.watchlist;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.watchlist.GetWatchListDetailsRequest;
import com.msf.libsb.services.watchlist.GetWatchListDetailsResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class GetWatchListDetails extends SessionServlet 
{
	private static final long serialVersionUID =1L;
	@Override
	
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		GetWatchListDetailsRequest getWatchListDetailsReq=new GetWatchListDetailsRequest();
		getWatchListDetailsReq.setUserId(ssRequest.getSession().getUserId());
		
		GetWatchListDetailsResponse getRes=getWatchListDetailsReq.doOperations();
		ssResponse.addToData(APP_CONSTANT.WATCHLIST_ARR, getRes.getWatchlistArr());
	}

	@Override
	protected String getSvcName() 
	{
		return "GetWatchListDetails";
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
