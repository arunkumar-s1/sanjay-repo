package com.msf.libsb.services.order;

public class FixRebalanceCountResponse 
{
	private int fixCount;
	private int rebalanceCount;
	public int getFixCount()
	{
		return fixCount;
	}
	public void setFixCount(int fixCount)
	{
		this.fixCount = fixCount;
	}
	public int getRebalanceCount()
	{
		return rebalanceCount;
	}
	public void setRebalanceCount(int rebalanceCount)
	{
		this.rebalanceCount = rebalanceCount;
	}
}
