package com.msf.libsb.services.order;

import org.json.me.JSONArray;

import com.msf.libsb.base.BaseResponse;

public class OrderReviewResponse 
{
	
	private String iconURL;
	private JSONArray symbolArray;
	private Double netPrice;
	private Double estimatedTax;
	private Double estimatedFees;
	private Double totalPrice;
	private Double minAmount;
	public String getIconURL()
	{
		return iconURL;
	}
	public void setIconURL(String iconURL)
	{
		this.iconURL = iconURL;
	}
	public JSONArray getSymbolArray()
	{
		return symbolArray;
	}
	public void setSymbolArray(JSONArray symbolArray)
	{
		this.symbolArray = symbolArray;
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
	public Double getEstimatedFees()
	{
		return estimatedFees;
	}
	public void setEstimatedFess(Double estimatedFees)
	{
		this.estimatedFees = estimatedFees;
	}
	
	public Double getTotalPrice()
	{
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice)
	{
		this.totalPrice = totalPrice;
	}
	public Double getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}
}
