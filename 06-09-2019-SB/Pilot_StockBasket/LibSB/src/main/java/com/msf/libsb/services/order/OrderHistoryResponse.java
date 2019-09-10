package com.msf.libsb.services.order;

import org.json.me.JSONArray;

public class OrderHistoryResponse
{
	private JSONArray basketNames;

	public JSONArray getBasketNames()
	{
		return basketNames;
	}

	public void setBasketNames(JSONArray basketNames)
	{
		this.basketNames = basketNames;
	}

	
}
