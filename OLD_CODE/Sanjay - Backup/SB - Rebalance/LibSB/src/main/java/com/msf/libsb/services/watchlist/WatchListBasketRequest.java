package com.msf.libsb.services.watchlist;

import com.msf.libsb.services.operations.WatchlistOperations;

public class WatchListBasketRequest
{	
	private String userID;
	private String basketName;
	
	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public String getBasketName()
	{
		return basketName;
	}

	public void setBasketName(String basketName)
	{
		this.basketName = basketName;
	}

	public WatchListBasketResponse addUserWatchListBasket() throws Exception 
	{
		return WatchlistOperations.addUserWatchListBasket(this);
	}
	public WatchListBasketResponse deleteUserWatchListBasket() throws Exception 
	{
		return WatchlistOperations.deleteUserWatchListBasket(this);
	}
}
