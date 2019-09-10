package com.msf.libsb.services.order;

import org.json.me.JSONArray;

public class PlaceBasketOrderResponse 
{
	private String basketName;
	private String iconURL;
	private String orderStatus;
	private String message;
	private String netPrice;
	private String estimatedTax;
	private String basketOrderNo;
	private String transactionType;
	private int orderCount;
	private int filledQty;
	private JSONArray stocks;
	
	public String getBasketName()
	{
		return basketName;
	}
	public void setBasketName(String basketName)
	{
		this.basketName = basketName;
	}
	
	public String getIconURL()
	{
		return iconURL;
	}
	public void setIconURL(String iconURL)
	{
		this.iconURL = iconURL;
	}
	public String getOrderStatus()
	{
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus)
	{
		this.orderStatus = orderStatus;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getNetPrice()
	{
		return netPrice;
	}
	public void setNetPrice(String netPrice)
	{
		this.netPrice = netPrice;
	}
	public String getEstimatedTax()
	{
		return estimatedTax;
	}
	public void setEstimatedTax(String estimatedTax)
	{
		this.estimatedTax = estimatedTax;
	}
	public String getBasketOrderNo()
	{
		return basketOrderNo;
	}
	public void setBasketOrderNo(String basketOrderNo)
	{
		this.basketOrderNo = basketOrderNo;
	}
	public String getTransactionType()
	{
		return transactionType;
	}
	public void setTransactionType(String transactionType)
	{
		this.transactionType = transactionType;
	}
	public int getOriginalQty()
	{
		return orderCount;
	}
	public void setOriginalQty(int orderCount)
	{
		this.orderCount = orderCount;
	}
	public int getFilledQty()
	{
		return filledQty;
	}
	public void setFilledQty(int filledQty)
	{
		this.filledQty = filledQty;
	}
	public JSONArray getStocks()
	{
		return stocks;
	}
	public void setStocks(JSONArray stocks)
	{
		this.stocks = stocks;
	}
}