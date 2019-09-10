package com.msf.libsb.services.pendingorders;

import org.json.me.JSONArray;

public class RebalanceOrderResponse 
{
	private String iconURL;
	private String orderStatus;
	private String message;
	private double netPrice;
	private double estimatedTax;
	private String basketOrderNo;
	private String transactionType;
	private int orderCount;
	private int filledQty;
	private JSONArray stocks;
	private JSONArray versionDiff;
	private double rebalanceAmt;
	
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
	public double getNetPrice()
	{
		return netPrice;
	}
	public void setNetPrice(double netPrice)
	{
		this.netPrice = netPrice;
	}
	public double getEstimatedTax()
	{
		return estimatedTax;
	}
	public void setEstimatedTax(double estimatedTax)
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
	public JSONArray getVersionDiff()
	{
		return versionDiff;
	}
	public void setVersionDiff(JSONArray versionDiff)
	{
		this.versionDiff = versionDiff;
	}
	public double getRebalanceAmt()
	{
		return rebalanceAmt;
	}
	public void setRebalanceAmt(double rebalanceAmt)
	{
		this.rebalanceAmt = rebalanceAmt;
	}
	
}