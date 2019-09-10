package com.msf.libsb.services.pendingorders;

import org.json.me.JSONArray;

public class GetPendingOrdersResponse 
{
	private JSONArray orderArr;

	public JSONArray getOrderArr()
	{
		return orderArr;
	}

	public void setOrderArr(JSONArray orderArr)
	{
		this.orderArr = orderArr;
	}
	
}