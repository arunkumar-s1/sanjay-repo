package com.msf.libsb.services.watchlist;

import com.msf.libsb.services.operations.WatchlistOperations;

public class GetWatchListDetailsRequest
{
	private String userId;
	
	public void setUserId(String userId) 
	{
		this.userId=userId;
	}
	
	public String getUserId() 
	{
		return userId;
	}
	
	public GetWatchListDetailsResponse doOperations() throws Exception 
	{
		return WatchlistOperations.getWatchListDetails(this);
	}

}
