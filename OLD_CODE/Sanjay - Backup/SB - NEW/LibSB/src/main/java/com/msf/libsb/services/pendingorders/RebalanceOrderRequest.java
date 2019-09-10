package com.msf.libsb.services.pendingorders;

import com.msf.libsb.services.operations.RebalanceOrderOperations;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.libsb.utils.helper.Session;

public class RebalanceOrderRequest 
{
	private String basketName;
	private String orderNo;
	private Session session;
	
	public Session getSession()
	{
		return session;
	}

	public void setSession(Session session)
	{
		this.session = session;
	}

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

	public PlaceBasketOrderResponse doOperations() throws Exception 
	{
		return RebalanceOrderOperations.rebalanceBasketOrder(this);
	}

}
