package com.msf.samco.services.fetchbasket;

import org.json.me.JSONObject;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.fetchbasket.SearchBasketRequest;
import com.msf.libsb.services.fetchbasket.SearchBasketResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class SearchBasket extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(SearchBasket.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		JSONObject dataObject=ssRequest.getData();
		JSONObject filterBy=dataObject.getJSONObject(APP_CONSTANT.FILTER_BY);
		JSONObject sort=dataObject.getJSONObject(APP_CONSTANT.SORT);
		
		String searchStr= ssRequest.getFromData(APP_CONSTANT.SEARCH_STRING);	
		String fetchBy= ssRequest.getFromData(APP_CONSTANT.FETCH_BY);
		
		SearchBasketRequest searchBasketReq = new SearchBasketRequest();
		searchBasketReq.setSearchStr(searchStr);	
		searchBasketReq.setfetchBy(fetchBy);
		searchBasketReq.setSort(sort);
		searchBasketReq.setFilterBy(filterBy);
		searchBasketReq.setUserId(ssRequest.getSession().getUserId());
		
		SearchBasketResponse searchRes = searchBasketReq.doOperations();
		ssResponse.addToData(APP_CONSTANT.BASKET_NAME, searchRes.getBasketArr());
		ssResponse.addToData(APP_CONSTANT.BASKET_COUNT,searchRes.getBasketCount());
	}

	@Override
	protected String getSvcName() 
	{
		return "SearchBasket";
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
