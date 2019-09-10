package com.msf.libsb.services.pendingorders;

public class FixAllOrderResponse 
{
	private int completedOrderCount;
	private int totalOrderCount;
	public int getCompletedOrderCount()
	{
		return completedOrderCount;
	}
	public void setCompletedOrderCount(int completedOrderCount)
	{
		this.completedOrderCount = completedOrderCount;
	}
	public int getTotalOrderCount()
	{
		return totalOrderCount;
	}
	public void setTotalOrderCount(int totalOrderCount)
	{
		this.totalOrderCount = totalOrderCount;
	}
	
}