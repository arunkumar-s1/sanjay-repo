package com.msf.libsb.base;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;

import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.InvalidResponseException;
import com.msf.log.Logger;

public class BaseArrayResponse extends JSONArray
{

	public static Logger log = Logger.getLogger(BaseResponse.class);

	protected boolean isSuccess = false;
	protected String errorMessage;
	protected String statusMessage;

	public BaseArrayResponse() 
	{
	}

	public BaseArrayResponse(JSONTokener x) throws JSONException 
	{
		super(x);
	}

	public BaseArrayResponse(String apiResponse) throws Exception 
	{
		super(apiResponse);
		parse();
	}

	public BaseArrayResponse(Vector collection) 
	{
		super(collection);
	}

	public void parse() throws Exception 
	{

		int len = this.length();

		if (len == 0)
			throw new InvalidResponseException(INFO_IDS.RESPONSE_FAILURE);
		else if (len == 1) 
		{
			JSONObject dataObj = this.getJSONObject(0);

			statusMessage = APIConstants.SUCCESS_STATUS;

			if (dataObj.has(APIConstants.STATUS))
				statusMessage = dataObj.getString(APIConstants.STATUS);

			if (statusMessage.equalsIgnoreCase(APIConstants.FAILURE_STATUS)) 
			{

				if (dataObj.has(APIConstants.ERROR_MESSAGE))
					errorMessage = dataObj.getString(APIConstants.ERROR_MESSAGE);
				else if (dataObj.has(APIConstants.ERROR_MESSAGE2))
					errorMessage = dataObj.getString(APIConstants.ERROR_MESSAGE2);
				else if (dataObj.has(APIConstants.ERROR_MESSAGE3))
					errorMessage = dataObj.getString(APIConstants.ERROR_MESSAGE3);

			} 
			else if (statusMessage.equalsIgnoreCase(APIConstants.SUCCESS_STATUS))
				this.isSuccess = true;
		} 
		else
			this.isSuccess = true;
	}

	public boolean isSuccessResponse()
	{

		return isSuccess;
	}

	public String getErrorMessage()
	{

		return errorMessage;
	}

	public String getRespMessage()
	{
		return errorMessage;
	}

	public String getStatusMessage() 
	{

		return statusMessage;
	}

	public String getErrorID()
	{

		if (errorMessage.equalsIgnoreCase("Session Expired")) 
		{
			return INFO_IDS.INVALID_SESSION;
		} 
		else if (errorMessage.equalsIgnoreCase("No data available for selected scrip")) 
		{
			return INFO_IDS.NO_DATA_FOUND;
		} 
		else if ((errorMessage.equalsIgnoreCase("No_data")) || (errorMessage.equalsIgnoreCase("No Data"))) 
		{
			return INFO_IDS.NO_DATA_FOUND;
		} 
		else if (errorMessage.startsWith("Scrip  is already present")) 
		{
			return INFO_IDS.SYMBOL_ALREADY_PRESENT;
		}

		return INFO_IDS.RESPONSE_FAILURE;
	}

}
