package com.msf.libsb.services.watchlist;

import com.msf.libsb.services.operations.WatchlistOperations;

public class GetUserWatchListRequest 
{	
	private String userId;
	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public GetUserWatchListResponse doOperations() throws Exception 
	{
		return WatchlistOperations.getUserWatchList(this);	
	}


}
