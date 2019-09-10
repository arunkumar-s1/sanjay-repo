package com.msf.libsb.services.watchlist;

import org.json.me.JSONArray;

import com.msf.libsb.base.BaseResponse;

public class GetUserWatchListResponse extends BaseResponse 
{
	private JSONArray watchListArr;

	public JSONArray getWatchListArr()
	{
		return watchListArr;
	}

	public void setWatchListArr(JSONArray watchListArr)
	{
		this.watchListArr = watchListArr;
	}
}
