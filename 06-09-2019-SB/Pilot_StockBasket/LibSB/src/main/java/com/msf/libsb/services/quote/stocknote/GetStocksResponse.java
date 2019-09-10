package com.msf.libsb.services.quote.stocknote;

import org.json.me.JSONArray;

public class GetStocksResponse
{
	private JSONArray stockArr;

	public JSONArray getStockArr()
	{
		return stockArr;
	}

	public void setStockArr(JSONArray stockArr)
	{
		this.stockArr = stockArr;
	}
}
