package com.msf.libsb.utils.exception;

import com.msf.libsb.constants.INFO_IDS;

public class SamcoException extends Exception
{

	private static final long serialVersionUID = 1L;

	private String errorCode = null;

	public SamcoException()
	{
		super("unknown status");
		this.errorCode = INFO_IDS.REQUEST_FAILED;

	}

	public SamcoException(String errorcode, String errormessage)
	{
		super(errormessage);
		this.errorCode = errorcode;
	}

	/**
	 * Helps to set INFO_ID to device response
	 * @param errorcode / INFO_ID
	 */
	public SamcoException(String errorcode)
	{
		this.errorCode = errorcode;
	}

	public void setErrorCode(String errorcode)
	{
		this.errorCode = errorcode;
	}

	public String getErrorCode()
	{
		return this.errorCode;
	}

}
