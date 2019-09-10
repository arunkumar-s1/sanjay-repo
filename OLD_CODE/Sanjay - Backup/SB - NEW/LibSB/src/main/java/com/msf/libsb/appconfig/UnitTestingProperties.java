package com.msf.libsb.appconfig;

import org.apache.commons.configuration.PropertiesConfiguration;

public class UnitTestingProperties 
{

	private static String fileName = "";

	public static void setUnitTestingFile(String filePath)
	{

		fileName = filePath;
	}

	public static String getUnitTestingResponse(String key)
	{

		PropertiesConfiguration unitTestingResponse = null;

		try 
		{

			unitTestingResponse = new PropertiesConfiguration(fileName);

			if (unitTestingResponse != null && unitTestingResponse.getString(key) != null)
				return unitTestingResponse.getString(key);

		} 
		catch (Exception e) 
		{

		}
		return "";
	}

}
