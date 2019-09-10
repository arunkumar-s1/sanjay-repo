package com.msf.libsb.services.order;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;

public class PlaceOrderResponse extends BaseResponse
{

	public PlaceOrderResponse(String apiResponse) throws Exception
	{
		super(apiResponse);
	}

	public String getOrderNo() throws Exception
	{
		String orderNumberString = this.getString(APIConstants.ORDER_NO);
		String[] orderNum = orderNumberString.split("-");
		return orderNum[0];
	}

	@Override
	public String getErrorID() 
	{
		if (!this.isSuccess) 
		{
			if (this.errorMessage.contains("Not able to Retrieve  PlaceOrder ")) 
			{
				return INFO_IDS.PLACEORDER_UNABLE_TO_FETCH_SERVICE;
			} 

			else if(this.errorMessage.contains("Session Expired"))
			{
				return INFO_IDS.INVALID_SESSION;
			}

			else 
			{
				return INFO_IDS.PLACEORDER_ERROR;
			}
		}
		return INFO_IDS.SUCCESS;
	}
}
