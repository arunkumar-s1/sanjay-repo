package com.msf.libsb.appconfig;

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class InfoMessage 
{

	private static Hashtable<String, String> INFO_MSG_MAP = new Hashtable<String, String>();

	public static void loadFile(String fileName) throws ConfigurationException
	{

		PropertiesConfiguration config = null;

		config = new PropertiesConfiguration(fileName);

		loadInfoMessages(config);
	}

	private static void loadInfoMessages(PropertiesConfiguration config)
	{

		Iterator<String> keys = config.getKeys();

		while (keys.hasNext())
		{

			String key = keys.next();

			INFO_MSG_MAP.put(key, config.getString(key));

			//log.debug("key : " + key + " msg : " + config.getString(key));
		}
	}

	public static String getInfoMSG(String infoCode)
	{
		return INFO_MSG_MAP.get(infoCode);
	}
}
