
package com.msf.libsb;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;

import com.msf.libsb.utils.exception.InvalidRequestParameter;

// @author: vijayakanth_ms

public class Validator 
{
	public static org.apache.log4j.Logger log = Logger.getLogger(Validator.class);

	/**
	 * For types without any parent static fields for the type should only contain
	 * standard param values. This method validates an OrderParam of Class<type>
	 * `orderParam` returns a reformed value or an exception
	 */
	public static String validate(Class<?> fieldClass, String value) throws Exception 
	{

		// TODO : 1. other static fields
		// 2. child classes.

		try
		{

			for (Field f : fieldClass.getDeclaredFields())
			{
				String c = (String) f.get(null) ; 
				if (c.equalsIgnoreCase(value)) 
				{
					return (String) f.get(null);
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			log.debug("Please check the class definition for the paramter : " + fieldClass.getSimpleName());
		}
		throw new Exception(String.format("Wrong value %s passed for %s", value, fieldClass.getSimpleName()));

	}	

	public static String validateInSet(String value,String error,  String... values ) throws Exception
	{
		for ( String v: values) 
		{
			if ( v.equalsIgnoreCase(value))
				return v; 
		}

		throw new Exception(String.format("Validation Error: %s", error));

	}

	public static void validPrice(String price,String error ) throws Exception 
	{

		double prcValue = Float.parseFloat(price);

		if (prcValue <= 0)
		{
			throw new Exception(String.format("Validation Error: %s", error)) ;
		}

	}

	public static String checkLength(String sParameter) throws Exception
	{

		int nLength = sParameter.length();

		if (nLength == 0) 
		{
			throw new InvalidRequestParameter("Validation Error :: Parameter length cannot be zero");
		}
		return sParameter;

	}

	public static JSONArray checkDelimiter(JSONArray jParameter) throws Exception
	{

		int nLength = jParameter.toString().length();

		if (nLength == 0) 
		{
			throw new InvalidRequestParameter("Validation Error :: Parameter length cannot be zero");
		}
		if (jParameter.toString().contains("|"))
		{
			throw new InvalidRequestParameter("Validation Error :: Parameter should not have |");
		}
		return jParameter;

	}
}
