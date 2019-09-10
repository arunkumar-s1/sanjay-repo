package com.msf.libsb.services.quote.stocknote;

import java.sql.SQLException;

import org.json.me.JSONException;

import com.msf.libsb.services.operations.StockNotesOperations;

public class GetStocksRequest
{
	private String basketName;

	public String getBasketName()
	{
		return basketName;
	}

	public void setBasketName(String basketName)
	{
		this.basketName = basketName;
	}
	
	public GetStocksResponse getStocks() throws SQLException, JSONException 
	{
		return StockNotesOperations.getStocks(this);	
	}
	public GetStocksResponse getStockFeeds() throws Exception 
	{
		return StockNotesOperations.getStockFeeds(this);
		
	}
}
