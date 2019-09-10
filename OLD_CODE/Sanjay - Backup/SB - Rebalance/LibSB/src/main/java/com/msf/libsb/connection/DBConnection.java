package com.msf.libsb.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.jmx.Monitor;
import com.msf.libsb.jmx.SamcoServiceBean;
import com.msf.log.Logger;

//import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

public class DBConnection implements DBPool 
{

	private static Logger log = Logger.getLogger(DBConnection.class);

	private static DBConnection instance = null;
	private InitialContext context;
	private BasicDataSource datasource, quoteSource, pushSource, stockBasketSource, samcoSessionSource;
	//	private BasicDataSource marginSource;

	private DBConnection() 
	{
		try
		{
			context = new InitialContext();

			datasource = (BasicDataSource) context.lookup(APP_CONSTANT.CONNECTION_URL);
			quoteSource = (BasicDataSource) context.lookup(APP_CONSTANT.QUOTE_DB_URL);
			pushSource = (BasicDataSource) context.lookup(APP_CONSTANT.PUSH_DB_URL);
			//samcoSessionSource = (BasicDataSource) context.lookup(APP_CONSTANT.SAMCO_SESSION_URL);
			stockBasketSource = (BasicDataSource) context.lookup(APP_CONSTANT.STOCK_BASKET_DB_URL);
			//			marginSource = (BasicDataSource) context.lookup(APP_CONSTANT.MARGIN_DB_URL);

		} 
		catch (NamingException e) 
		{
			log.error("", e);
		}
	}

	public static DBConnection getInstance() 
	{
		if (instance == null)
			instance = new DBConnection();

		return instance;
	}

	/**
	 * function to create a connection to SAMCO database
	 * 
	 * @return {@link java.sql.Connection}
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException 
	{
		SamcoServiceBean dbBean = Monitor.getServiceBean(Monitor.MSF_DB);

		try 
		{
			Connection c = datasource.getConnection();
			log.debug("Opening datasource connection : " + System.identityHashCode(c));
			return c;
		} 
		catch (SQLException e) 
		{

			if (dbBean != null) 
			{
				dbBean.setFailure(e.getMessage());
			}

			throw e;
		}
	}

	/**
	 * function to create a connection to SAMCO database
	 * 
	 * @return {@link java.sql.Connection}
	 * @throws SQLException
	 */
	public Connection getSamcoConnection() throws SQLException 
	{

		SamcoServiceBean dbBean = Monitor.getServiceBean(Monitor.MSF_DB);

		try 
		{
			Connection c = samcoSessionSource.getConnection();
			log.debug("Opening samcoSessionSource connection : " + System.identityHashCode(c));
			return c;
		} 
		catch (SQLException e) 
		{

			if (dbBean != null) 
			{
				dbBean.setFailure(e.getMessage());
			}

			throw e;
		}
	}

	/**
	 * function to create a connection to QUOTE database
	 * 
	 * @return {@link java.sql.Connection}
	 * @throws SQLException
	 */
	public Connection getQuoteDBConnection() throws SQLException 
	{

		SamcoServiceBean dbBean = Monitor.getServiceBean(Monitor.MSF_DB);

		try 
		{
			Connection c = quoteSource.getConnection();
			log.debug("Opening quoteSource connection : " + System.identityHashCode(c));
			return c;
		} 
		catch (SQLException e) 
		{

			if (dbBean != null) 
			{
				dbBean.setFailure(e.getMessage());
			}

			throw e;
		}
	}

	/**
	 * function to create a connection to Push database
	 * 
	 * @return {@link java.sql.Connection}
	 * @throws SQLException
	 */
	public Connection getPushDBConnection() throws SQLException 
	{

		SamcoServiceBean dbBean = Monitor.getServiceBean(Monitor.MSF_DB);

		try {
			Connection c = pushSource.getConnection();
			log.debug("Opening pushSource connection : " + System.identityHashCode(c));
			return c;
		} 
		catch (SQLException e) 
		{

			if (dbBean != null) 
			{
				dbBean.setFailure(e.getMessage());
			}

			throw e;
		}
	}

	/**
	 * function to create a connection to STOCK_BASKET database
	 * 
	 * @return {@link java.sql.Connection}
	 * @throws SQLException
	 */
	public Connection getStockBasketDBConnection() throws SQLException 
	{

		SamcoServiceBean dbBean = Monitor.getServiceBean(Monitor.MSF_DB);

		try 
		{
			Connection c = stockBasketSource.getConnection();
			log.debug("Opening StockBasket connection : " + System.identityHashCode(c));
			return c;
		} 
		catch (SQLException e) 
		{

			if (dbBean != null) 
			{
				dbBean.setFailure(e.getMessage());
			}

			throw e;
		}
	}

	public void releasePool() {

		try {

			if (datasource != null)
				datasource.close();
			log.info("Closing datasource " + datasource);

		} 
		catch (SQLException e) 
		{
			log.warn("Closing datasource ", e);
		}

		try {

			if (samcoSessionSource != null)
				samcoSessionSource.close();
			log.info("Closing samcoSessionSource " + samcoSessionSource);

		} 
		catch (SQLException e) 
		{
			log.warn("Closing samcoSessionSource ", e);
		}

		try 
		{
			if (quoteSource != null)
				quoteSource.close();
			log.info("Closing quoteSource " + quoteSource);
		} 
		catch (SQLException e) 
		{
			log.warn("Closing quoteSource ", e);
		}

		try 
		{
			if (pushSource != null)
				pushSource.close();
			log.info("Closing pushSource " + pushSource);
		} 
		catch (SQLException e) 
		{
			log.warn("Closing pushSource ", e);
		}

		try 
		{
			if (stockBasketSource != null)
				stockBasketSource.close();
			log.info("Closing stockBasketSource " + stockBasketSource);
		} 
		catch (SQLException e) 
		{
			log.warn("Closing stockBasketSource ", e);
		}
	}
}
