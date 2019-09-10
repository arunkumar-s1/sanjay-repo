package com.msf.libsb.base;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;

public class BaseResponse extends JSONObject 
{

	public static Logger log = Logger.getLogger(BaseResponse.class);

	protected boolean isSuccess = false;
	protected String errorMessage;
	protected String statusMessage;
	protected int responseCode;

	public BaseResponse()
	{
	}

	public BaseResponse(String apiResponse) throws Exception 
	{
		super(apiResponse);
		this.parse();
	}

	protected void parse() throws SamcoException, JSONException
	{

		if (this.has(APIConstants.STATUS))
			statusMessage = this.getString(APIConstants.STATUS);

		else
			statusMessage = APIConstants.SUCCESS_STATUS;

		if (statusMessage.equalsIgnoreCase(APIConstants.FAILURE_STATUS)) 
		{
			if (this.has(APIConstants.ERROR_MESSAGE))
				errorMessage = this.getString(APIConstants.ERROR_MESSAGE);
			else if (this.has(APIConstants.ERROR_MESSAGE2))
				errorMessage = this.getString(APIConstants.ERROR_MESSAGE2);
			else if (this.has(APIConstants.ERROR_MESSAGE3))
				errorMessage = this.getString(APIConstants.ERROR_MESSAGE3);
			else if (this.has(APIConstants.RESULT))
				errorMessage = this.getString(APIConstants.RESULT);

		} 
		else if (statusMessage.equalsIgnoreCase(APIConstants.SUCCESS_STATUS)) 
		{
			this.isSuccess = true;
		}

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

	public void setResponseCode(int responseCode)
	{

		this.responseCode = responseCode;

		if (this.responseCode == 200)
			isSuccess = true;
		else
			isSuccess = false;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * get an error code to throw inside samco exception
	 * 
	 * @param no
	 *            parameter required
	 * @return returns an INFO_ID
	 */
	public String getErrorID() {

		if (errorMessage.equalsIgnoreCase("Session Expired"))
		{
			return INFO_IDS.INVALID_SESSION;
		} else if (errorMessage.equalsIgnoreCase("No data available for selected scrip")) 
		{
			return INFO_IDS.NO_DATA_FOUND;
		} else if (errorMessage.equalsIgnoreCase("No_data"))
		{
			return INFO_IDS.NO_DATA_FOUND;
		} else if (errorMessage.equalsIgnoreCase("No Data")) 
		{
			return INFO_IDS.NO_DATA_FOUND;
		} else if (errorMessage.startsWith("Scrip  is already present"))
		{
			return INFO_IDS.SYMBOL_ALREADY_PRESENT;
		} else if (errorMessage.startsWith("No_MarketWatch ")) 
		{
			return INFO_IDS.NO_MARKET_WATCH;
		}

		return INFO_IDS.RESPONSE_FAILURE;
	}

}
