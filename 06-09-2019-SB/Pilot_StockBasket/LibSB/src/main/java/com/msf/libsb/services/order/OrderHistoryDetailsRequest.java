package com.msf.libsb.services.order;

public class OrderHistoryDetailsRequest
{
	private String basketName="";
	private String userId="";
	
	public String getBasketName()
	{
		return basketName;
	}
	public void setBasketName(String basketName)
	{
		this.basketName = basketName;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	

}
