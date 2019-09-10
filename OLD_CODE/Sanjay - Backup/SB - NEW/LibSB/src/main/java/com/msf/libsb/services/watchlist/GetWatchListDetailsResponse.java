package com.msf.libsb.services.watchlist;

import org.json.me.JSONArray;

public class GetWatchListDetailsResponse
{
	private JSONArray watchlistArr;

	public JSONArray getWatchlistArr()
	{
		return watchlistArr;
	}

	public void setWatchlistArr(JSONArray watchlistArr)
	{
		this.watchlistArr = watchlistArr;
	}
}
