package com.msf.samco.services.fetchbasket;

import org.json.me.JSONObject;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.fetchbasket.FetchBasketsRequest;
import com.msf.libsb.services.fetchbasket.FetchBasketsResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class FetchBaskets extends SessionServlet 
{
	private static final long serialVersionUID = 1L;

	//private static Logger log = Logger.getLogger(FetchBaskets.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{	
		JSONObject filterBy=ssRequest.getJSONFromData(APP_CONSTANT.FILTER_BY);
		JSONObject sort=ssRequest.getJSONFromData(APP_CONSTANT.SORT);

		String fetchBy=ssRequest.getFromData(APP_CONSTANT.FETCH_BY);
		int pageNo=ssRequest.optIntFromData(APP_CONSTANT.PAGE_NUMBER, 1);
		
		FetchBasketsRequest fetchReq = new FetchBasketsRequest();
		fetchReq.setFetchBy(fetchBy);
		fetchReq.setPageNo(pageNo);
		fetchReq.setSort(sort);
		fetchReq.setFilterBy(filterBy);
		fetchReq.setUserId(ssRequest.getSession().getUserId());

		FetchBasketsResponse fetchRes = fetchReq.doOperations();
		ssResponse.addToData(APP_CONSTANT.BASKET_NAME, fetchRes.getBasketArr());
		ssResponse.addToData(APP_CONSTANT.END_FLAG, fetchRes.isEndFlag());
		ssResponse.addToData(APP_CONSTANT.BASKET_COUNT,fetchRes.getBasketCount());

	}

	@Override
	protected String getSvcName()
	{
		return "FetchBaskets";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "StockBasket";
	}
	
	@Override	
	protected String getSvcVersion() 
	{
		return "1.0.0";
	}
}
