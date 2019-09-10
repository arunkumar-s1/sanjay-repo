package com.msf.samco.listeners;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.appconfig.IndicesMapper;
import com.msf.libsb.appconfig.InfoMessage;
import com.msf.libsb.appconfig.SymbolISINMap;
import com.msf.libsb.appconfig.SymbolTranslation;
import com.msf.libsb.appconfig.UnitTestingProperties;
import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.jmx.Monitor;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class ApplicationContextListener implements ServletContextListener 
{

	private static Logger log = Logger.getLogger(ApplicationContextListener.class);

	public void contextInitialized(ServletContextEvent event) 
	{

		ServletContext ctx = event.getServletContext();

		String classFolder = ctx.getRealPath("/")
				/* + System.getProperty("file.separator") */ + "WEB-INF" + System.getProperty("file.separator")
				+ "classes" + System.getProperty("file.separator");

		Properties JSLogProperties = new Properties();
		try 
		{

			JSLogProperties.load(new FileInputStream(classFolder + "jslog.properties"));

			Logger.setLogger(JSLogProperties);
			log = Logger.getLogger(ApplicationContextListener.class);

		} 
		catch (Exception e) 
		{
			System.out.println("Cannot load jslog.properties");
			System.out.println(e.getMessage());
		}
		/* loading appConfig file to access urls etc */
		try 
		{
			log.info("Config file path : " + classFolder + "config.properties");
			AppConfig.loadFile(classFolder + "config.properties");
			UnitTestingProperties.setUnitTestingFile(classFolder + "unit_testing.properties");
			InfoMessage.loadFile(classFolder + "Info_msg.properties");
			AppConfig.loadAlertCriteiaTypes();
			AppConfig.loadMCXEnergyIndices();
			AppConfig.loadMCXAgriIndices();
			AppConfig.loadMCXComdexIndices();
			AppConfig.loadMCXMetalIndices();
			AppConfig.loadtradeableIndices();

		} 
		catch (Exception e) 
		{
			log.error(" Exception : " + e);
		}
		/* loading symbol map */
		try 
		{
			SymbolTranslation.loadMap(AppConfig.getConfigValue("symbolFolderPath"));
		} 
		catch (Exception e) 
		{
			log.error("Error for symbol map " + e.getMessage());
		}
		/* loading isin map */
		try {
			SymbolISINMap.loadMap(DBConnection.getInstance());
		} 
		catch (Exception e) 
		{
			log.error("Error for isin map " + e.getMessage());
		}

		/* index map */
		try
		{
			IndicesMapper.loadMap(DBConnection.getInstance());
		}
		catch (Exception e) 
		{
			log.error("Error for index maper " + e.getMessage());
		}

		// Registering JMX Beans for the application
		try 
		{
			log.info("Registering JMX Beans");
			Monitor.setServiceBeans();
		} 
		catch (Exception e)
		{
			log.error("Error in registering JMX Beans", e);
		}
		try 
		{
			log.info("Checking for database connection");
			Helper.closeConnection(DBConnection.getInstance().getConnection());
			Helper.closeConnection(DBConnection.getInstance().getConnection());
			Helper.closeConnection(DBConnection.getInstance().getQuoteDBConnection());
		} catch (Exception e) 
		{
			log.error("Error in creating sql connection ", e);
		}

	}

	public void contextDestroyed(ServletContextEvent arg0) 
	{
		// Releasing the mysql connections pool
		try 
		{
			log.info("Closing database connection");
			DBConnection.getInstance().releasePool();
		} 
		catch (Exception e)
		{
			log.error("", e);
		}

		// Releasing the JMX Connection
		try 
		{
			log.info("Releasing JMX Beans");
			Monitor.release();
		} 
		catch (Exception e) 
		{
			log.error("Error in releasing JMX", e);
		}

		// Removing the connection to central logger
		try
		{
			log.close();
		} 
		catch (Exception e) 
		{

		}
	}
}