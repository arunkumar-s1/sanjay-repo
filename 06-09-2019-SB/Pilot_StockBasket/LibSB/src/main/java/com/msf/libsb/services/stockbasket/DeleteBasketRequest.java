package com.msf.libsb.services.stockbasket;

import com.msf.libsb.services.operations.BasketOperations;

public class DeleteBasketRequest
{
	private String basketName;
	private String basketVersion;

	public String getBasketName()
	{
		return basketName;
	}

	public void setBasketName(String basketName)
	{
		this.basketName = basketName;
	}

	public String getBasketVersion()
	{
		return basketVersion;
	}

	public void setBasketVersion(String basketVersion)
	{
		this.basketVersion = basketVersion;
	}

	public BasketResponse doOperations() throws Exception 
	{
		return BasketOperations.deleteBasket(this);
	}
}
