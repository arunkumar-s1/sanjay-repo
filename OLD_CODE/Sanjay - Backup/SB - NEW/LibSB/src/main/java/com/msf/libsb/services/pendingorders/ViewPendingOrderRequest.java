package com.msf.libsb.services.pendingorders;

import com.msf.libsb.services.operations.ViewPendingOrderOperations;

public class ViewPendingOrderRequest  
{
	private String orderNo;
	private String basketName;

	public String getBasketName()
	{
		return basketName;
	}

	public void setBasketName(String basketName)
	{
		this.basketName = basketName;
	}

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public ViewPendingOrderResponse doOperations() throws Exception 
	{
		return ViewPendingOrderOperations.viewBrokenOrder(this);
	}

	public ViewPendingOrderResponse postRequest() throws Exception 
	{
		return ViewPendingOrderOperations.viewRebalanceOrder(this);
	}
}
