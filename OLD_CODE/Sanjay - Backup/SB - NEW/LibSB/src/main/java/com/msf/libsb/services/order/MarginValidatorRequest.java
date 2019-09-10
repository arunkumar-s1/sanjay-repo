package com.msf.libsb.services.order;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.services.operations.MarginOperations;

public class MarginValidatorRequest
{
	private String basketName;
	private int qty;
	private String transactionType;
	
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

	public void setTransactionType(String transactionType)
	{
		this.transactionType = transactionType;
	}

	public double getRequiredMarginValue() throws Exception
	{
		return MarginOperations.getRequiredMarginValue(this);
	}

}
