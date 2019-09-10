package com.msf.libsb.services.fetchbasket;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.services.operations.FetchBasketOperations;

public class FetchBasketsRequest
{	
	private String fetchBy;
	private int pageNo;
	private JSONObject filterBy;
	private JSONObject sort;
	
	public JSONObject getSort()
	{
		return sort;
	}

	public void setSort(JSONObject sort)
	{
		this.sort = sort;
	}

	private String userId;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getFetchBy() throws JSONException
	{
		return fetchBy;
	}
	
	public void setFetchBy(String fetchBy) 
	{
		this.fetchBy=fetchBy;
	}
	
	public int getPageNo()  throws JSONException
	{
		return pageNo;
	}
	
	public void setPageNo(int pageNo) 
	{
		this.pageNo=pageNo;
	}
	
	public JSONObject getFilterBy()  throws JSONException
	{
		return filterBy;
	}
	
	public void setFilterBy(JSONObject filterBy)
	{		
		this.filterBy=filterBy;
	}
	
	public FetchBasketsResponse doOperations() throws Exception 
	{
		return FetchBasketOperations.fetchBaskets(this);
	}


}
