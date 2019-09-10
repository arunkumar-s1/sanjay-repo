package com.msf.libsb.services.order;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.services.operations.PlaceOrderOperations;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.utils.helper.Session;

public class PlaceBasketOrderRequest  
{
	private String basketName;
	private int qty;
	private String transactionType;
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
	
	public int getQty()
	{
		return qty;
	}

	public void setQty(int qty)
	{
		this.qty = qty;
	}
	
	public String getTransactionType()
	{
		return transactionType;
	}

	public void setTransactionType(String transactionType) throws Exception
	{
		
		if( !(transactionType.equals(APP_CONSTANT.BUY) || transactionType.equals(APP_CONSTANT.SELL)) )
		{
			throw new InvalidRequestParameter(INFO_IDS.INVALID_REQUEST_PARAM);
		}
		this.transactionType=transactionType;
	}

	public PlaceBasketOrderResponse doOperations() throws Exception 
	{
		return PlaceOrderOperations.placeBasketOrder(this);
	}
	
	public PlaceAMOOrderResponse placeAMOOrder() throws Exception 
	{
		return PlaceOrderOperations.placeAMOOrder(this);
	}

}
