package com.msf.libsb.services.pendingorders;

import org.json.me.JSONArray;

public class ViewPendingOrderResponse 
{
	private JSONArray symbolArr;
	private Double netPrice;
	private Double estimatedTax;
	private Double totalPrice;
	private JSONArray pendingVersions;
	
	public JSONArray getPendingVersions()
	{
		return pendingVersions;
	}
	public void setPendingVersions(JSONArray pendingVersions)
	{
		this.pendingVersions = pendingVersions;
	}
	public JSONArray getSymbolArr()
	{
		return symbolArr;
	}
	public void setSymbolArr(JSONArray symbolArr)
	{
		this.symbolArr = symbolArr;
	}
	public Double getNetPrice()
	{
		return netPrice;
	}
	public void setNetPrice(Double netPrice)
	{
		this.netPrice = netPrice;
	}
	public Double getEstimatedTax()
	{
		return estimatedTax;
	}
	public void setEstimatedTax(Double estimatedTax)
	{
		this.estimatedTax = estimatedTax;
	}
	public Double getTotalPrice()
	{
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice)
	{
		this.totalPrice = totalPrice;
	}
}