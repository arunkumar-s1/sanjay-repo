package com.msf.libsb.userlogin;

import org.json.me.JSONArray;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;

public class DefaultLoginResponse extends BaseResponse
{

	public DefaultLoginResponse(String sAPIResponse) throws Exception 
	{
		super(sAPIResponse);
	}

	public String getAccountID() throws Exception 
	{
		return this.getString(APIConstants.DEF_ACC_ID);
	}

	public String getAccountName() throws Exception 
	{
		return this.getString(APIConstants.DEF_ACC_NAME);
	}

	public String getBroker() throws Exception
	{
		return this.getString(APIConstants.DEF_BROKER);
	}

	public String getBranch() throws Exception 
	{
		return this.getString(APIConstants.DEF_BRANCH);
	}

	public JSONArray getExcArray() throws Exception 
	{
		return this.getJSONArray(APIConstants.DEF_EXCHANGES);
	}

	public JSONArray getProductArray() throws Exception
	{
		return this.getJSONArray(APIConstants.DEF_PRODUCTS);
	}

	public JSONArray getOrderTypeArray() throws Exception
	{
		return this.getJSONArray(APIConstants.DEF_ORDER_TYPES);
	}

	public String getDefMktWatch() throws Exception
	{
		return this.getString(APIConstants.DEF_MKT_WATCH);
	}

	public String getProductAlias() throws Exception 
	{
		return this.getString(APIConstants.DEF_PROD_ALIAS);
	}

	public JSONArray getWebLink() throws Exception 
	{
		return this.getJSONArray(APIConstants.DEF_WEBLINK);
	}

	@Override
	public String getErrorID() 
	{
		if (!this.isSuccess) 
		{
			if (this.errorMessage.equalsIgnoreCase("Not able to Retrieve DefaultLogin")) {
				return INFO_IDS.LOGIN_FAILED;

			} 

			else if(this.errorMessage.equalsIgnoreCase("Session Expired"))
			{
				return INFO_IDS.INVALID_SESSION;
			}

			else if(this.errorMessage.equalsIgnoreCase("Not able to set LotWeight"))
			{
				return INFO_IDS.LOGIN_FAILED;
			}

			else
			{
				return INFO_IDS.LOGIN_FAILED;
			}
		}

		return INFO_IDS.SUCCESS;
	}
}
