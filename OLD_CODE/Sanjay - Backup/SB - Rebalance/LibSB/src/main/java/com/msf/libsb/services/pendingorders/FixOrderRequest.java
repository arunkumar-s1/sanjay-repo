package com.msf.libsb.services.pendingorders;

import com.msf.libsb.services.operations.FixOrderOperations;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.libsb.utils.helper.Session;

public class FixOrderRequest 
{
	private String orderNo;
	private Session session;

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public Session getSession()
	{
		return session;
	}

	public void setSession(Session session)
	{
		this.session = session;
	}
	
	public PlaceBasketOrderResponse doOperations() throws Exception 
	{
		return FixOrderOperations.fixBasketOrder(this);
	}
		
	public FixAllOrderResponse postRequest() throws Exception 
	{
		return FixOrderOperations.fixAllOrder(this);
	}
}
