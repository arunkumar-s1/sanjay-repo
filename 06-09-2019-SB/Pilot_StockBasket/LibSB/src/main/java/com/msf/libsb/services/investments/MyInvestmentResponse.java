package com.msf.libsb.services.investments;

import java.util.Date;

import org.json.me.JSONArray;

public class MyInvestmentResponse
{
	private String totalInvestment;
	private String overallCurrentValue;
	private String overallProfitOrLossValue;
	private String overallProfitOrLossPercent;
	private JSONArray investmentArr;
	private Date lastInvestedOn;
	
	public Date getLastInvestedOn()
	{
		return lastInvestedOn;
	}
	public void setLastInvestedOn(Date lastInvestedOn)
	{
		this.lastInvestedOn = lastInvestedOn;
	}
	public String getTotalInvestment()
	{
		return totalInvestment;
	}
	public void setTotalInvestment(String totalInvestment)
	{
		this.totalInvestment = totalInvestment;
	}
	public String getOverallCurrentValue()
	{
		return overallCurrentValue;
	}
	public void setOverallCurrentValue(String overallCurrentValue)
	{
		this.overallCurrentValue = overallCurrentValue;
	}
	public String getOverallProfitOrLossValue()
	{
		return overallProfitOrLossValue;
	}
	public void setOverallProfitOrLossValue(String overallProfitOrLossValue)
	{
		this.overallProfitOrLossValue = overallProfitOrLossValue;
	}
	public String getOverallProfitOrLossPercent()
	{
		return overallProfitOrLossPercent;
	}
	public void setOverallProfitOrLossPercent(String overallProfitOrLossPercent)
	{
		this.overallProfitOrLossPercent = overallProfitOrLossPercent;
	}
	public JSONArray getInvestmentArr()
	{
		return investmentArr;
	}
	public void setInvestmentArr(JSONArray investmentArr)
	{
		this.investmentArr = investmentArr;
	}
	
	
}
