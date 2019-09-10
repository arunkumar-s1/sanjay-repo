package com.msf.libsb.appconfig;

public class Symbol
{
	String symbolNameHindi;
	String symbolNameEng;
	String companyNameHindi;
	String companyNameEng;
	
	public Symbol(String symbolNameHindi, String symbolNameEng, String companyNameHindi, String companyNameEng)
	{
		this.symbolNameHindi = symbolNameHindi;
		this.symbolNameEng = symbolNameEng;
		this.companyNameHindi = companyNameHindi;
		this.companyNameEng = companyNameEng;
	}
	
	public  String getSymbolName(String lang) 
	{
		if(lang.equalsIgnoreCase("hindi"))
		{
			return symbolNameHindi;
		}
		return symbolNameEng;
		
	}
	public  String getCompanyName(String lang) 
	{
		if(lang.equalsIgnoreCase("hindi"))
		{
			return companyNameHindi;
		}
		return companyNameEng;

	}

}
