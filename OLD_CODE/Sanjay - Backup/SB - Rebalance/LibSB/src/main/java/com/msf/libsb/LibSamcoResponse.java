package com.msf.libsb;

/**
 * Base class for Omnesys response classes. It contains errorCode and errorDesc,
 * common to all responses.
 * 
 * @author Kailash
 * 
 */
public class LibSamcoResponse 
{
	private String sErrorCode = "";
	private String sErrorMessage = "";
	private boolean isSuccess = true;
	private boolean isValidSession = true;
	private String successMessage;
	private String accountID;
	private String broker;

	public String getAccountID() 
	{
		return accountID;
	}

	public void setAccountID(String accountID)
	{
		this.accountID = accountID;
	}

	public String getBroker() 
	{
		return broker;
	}

	public void setBroker(String broker) 
	{
		this.broker = broker;
	}

	public String getSession()
	{
		return session;
	}

	public void setSession(String session)
	{
		this.session = session;
	}

	private String session;

	private String responseStr;

	public String getResponseStr() 
	{
		return responseStr;
	}

	public void setResponseStr(String responseStr)
	{
		this.responseStr = responseStr;
	}

	public void setErrorCode(String errorCode)
	{
		this.sErrorCode = errorCode;
	}

	public void setErrorMessage(String errorDesc) 
	{
		this.sErrorMessage = errorDesc;
	}

	public String getsErrorCode()
	{
		return sErrorCode;
	}

	public String getsErrorMessage() 
	{
		return sErrorMessage;
	}

	public boolean isSuccess() 
	{
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) 
	{
		this.isSuccess = isSuccess;
	}

	public String getSuccessMessage()
	{
		return successMessage;
	}

	public void setSuccessMessage(String successMessage)
	{
		this.successMessage = successMessage;
	}

	public boolean isValidSession()
	{
		return isValidSession;
	}

	public void setValidSession(boolean isInvalidSession)
	{
		this.isValidSession = isInvalidSession;
	}
}
