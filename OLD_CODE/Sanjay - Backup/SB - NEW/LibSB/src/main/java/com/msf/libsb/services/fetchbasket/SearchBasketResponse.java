package com.msf.libsb.services.fetchbasket;

import org.json.me.JSONArray;

public class SearchBasketResponse
{
	
	private JSONArray basketArr;
	private int basketCount;
	
	public JSONArray getBasketArr()
	{
		return basketArr;
	}
	
	public void setBasketArr(JSONArray basketArr) 
	{
		this.basketArr = basketArr;
	}
	
	public int getBasketCount() 
	{
		return basketCount;
	}
	
	public void setBasketCount(int basketCount)
	{
		this.basketCount = basketCount;
	}

}
