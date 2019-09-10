package com.msf.libsb.services.fetchbasket;

import org.json.me.JSONObject;

import com.msf.libsb.services.operations.FetchBasketOperations;

public class SearchBasketRequest
{
	private String searchStr;
	private String fetchBy;
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

	public String getSearchStr() 
	{
		return searchStr;
	}

	public void setSearchStr(String searchStr) 
	{
		this.searchStr = searchStr;
	}

	public String getfetchBy() 
	{
		return fetchBy;
	}

	public void setfetchBy(String fetchBy) 
	{
		this.fetchBy= fetchBy;
	}
	
	public String getFetchBy()
	{
		return fetchBy;
	}

	public void setFetchBy(String fetchBy)
	{
		this.fetchBy = fetchBy;
	}

	public JSONObject getFilterBy()
	{
		return filterBy;
	}

	public void setFilterBy(JSONObject filterBy)
	{
		this.filterBy = filterBy;
	}

	public SearchBasketResponse doOperations() throws Exception 
	{	
		return FetchBasketOperations.searchBasket(this);
	}
}
