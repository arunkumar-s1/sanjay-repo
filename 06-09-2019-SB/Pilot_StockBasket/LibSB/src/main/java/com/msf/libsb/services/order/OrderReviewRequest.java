package com.msf.libsb.services.order;

import com.msf.libsb.services.operations.OrderReviewOperations;

public class OrderReviewRequest 
{
	
	private String basketName;
	private int qty;
	private String basketVersion;
	private String transactionType;
	
	public String getBasketVersion()
	{
		return basketVersion;
	}

	public void setBasketVersion(String basketVersion)
	{
		this.basketVersion = basketVersion;
	}

	public String getBasketName() 
	{
		return basketName;
	}
	
	public void setBasketName(String basketName) 
	{
		this.basketName = basketName;
	}
	
	public int getQty() 
	{
		return qty;
	}
	
	public void setQty(int qty) 
	{
		this.qty = qty;
	}
	
	public OrderReviewResponse doOperations() throws Exception 
	{	
		return OrderReviewOperations.orderReview(this);
	}

}
