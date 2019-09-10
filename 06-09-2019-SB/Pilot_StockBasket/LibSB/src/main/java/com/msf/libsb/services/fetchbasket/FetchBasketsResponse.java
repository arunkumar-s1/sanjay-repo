package com.msf.libsb.services.fetchbasket;

import org.json.me.JSONArray;

public class FetchBasketsResponse
{
	private JSONArray basketArr;
	private boolean endFlag;
	private int basketCount;
	
	public JSONArray getBasketArr()
	{
		return basketArr;
	}
	public void setBasketArr(JSONArray basketArr)
	{
		this.basketArr = basketArr;
	}
	public boolean isEndFlag()
	{
		return endFlag;
	}
	public void setEndFlag(boolean endFlag)
	{
		this.endFlag = endFlag;
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
