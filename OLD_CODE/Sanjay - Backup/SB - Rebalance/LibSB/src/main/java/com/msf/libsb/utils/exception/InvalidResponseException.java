package com.msf.libsb.utils.exception;

public class InvalidResponseException extends SamcoException 
{

	public InvalidResponseException( String errorCode )
	{
		super(errorCode);
	}
}
