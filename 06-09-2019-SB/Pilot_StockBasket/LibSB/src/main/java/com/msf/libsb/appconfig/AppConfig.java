package com.msf.libsb.appconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;
import com.msf.utils.constants.UtilsConstants;

public class AppConfig 
{

	private static AppConfig instance = null;

	private static Logger log = Logger.getLogger(AppConfig.class);

	private int DATASOURCE_ADAPTER;

	public static HashMap<String, String> alertCriteiaType = new HashMap<String, String>();
	public static HashMap<String, String> idxFutNameMap = new HashMap<String, String>();
	public static HashMap<String, String> idxFutNameSymbolMap = new HashMap<String, String>();
	public static HashMap<String, String> futNameIndexMap = new HashMap<String, String>();
	public static List<Object> mcxEnergyIndex;
	public static List<Object> mcxComdexIndex;
	public static List<Object> mcxAgriIndex;
	public static List<Object> mcxMetalIndex;
	public static List<Object> tradeableIndex;
	public static List<Object> validNotificationType;
	public static ArrayList<Object> supportedBuilds = null;

	private static List<Object> nonUnderlyingSymbols = null;

	public static PropertiesConfiguration config = null;

	public static String getConfigValue(String key) throws SamcoException 
	{

		if (null != config )
			return config.getString(key);
		else
			throw new SamcoException(INFO_IDS.NO_DATA_FOUND);//(INFO_IDS.KEY_NOT_FOUND);

	}

	private static AppConfig getInstance() 
	{
		if (null == instance)
			instance = new AppConfig();
		return instance;
	}

	public static int getDatasourceAdapter()
	{
		return getInstance().DATASOURCE_ADAPTER;
	}

	public static void loadFile(String fileName) throws ConfigurationException
	{

		config = new PropertiesConfiguration(fileName);
		String dataSourceAdapter = config.getString("datasource.adapter");

		supportedBuilds = (ArrayList<Object>) config.getList("supported.build");
		validNotificationType = (ArrayList<Object>) config.getList("notification.valid_type");
		nonUnderlyingSymbols = (ArrayList<Object>) config.getList("non_underlying_symbols");

		loadIndexFutureNameMapping();
		loadIndexFutureUnderLyingSymbolMapping();

		if (dataSourceAdapter == null)
		{
			log.error("DataSource adapter not specfying in config file.");
		} 
		else if (dataSourceAdapter.equalsIgnoreCase("mysql")) 
		{
			getInstance().DATASOURCE_ADAPTER = UtilsConstants.MYSQL;
		} 
		else if (dataSourceAdapter.equalsIgnoreCase("oracle")) 
		{
			getInstance().DATASOURCE_ADAPTER = UtilsConstants.ORACLE;
		} 
		else if (dataSourceAdapter.equalsIgnoreCase("mssql")) 
		{
			getInstance().DATASOURCE_ADAPTER = UtilsConstants.MSSQL;
		} 
		else 
		{
			log.error("invalid Adapter : " + dataSourceAdapter);
		}


	}

	private static void loadIndexFutureNameMapping()
	{

		ArrayList<Object> indexName = (ArrayList<Object>) config.getList("tradable_index");
		ArrayList<Object> futureName = (ArrayList<Object>) config.getList("tradable_index.base_name");

		for( int i=0; i < futureName.size(); i++ )
		{
			String futName = futureName.get(i).toString();
			String idxName = indexName.get(i).toString();
			idxFutNameMap.put(futName, idxName);
			futNameIndexMap.put(idxName, futName);
		}
	}

	private static void loadIndexFutureUnderLyingSymbolMapping()
	{

		ArrayList<Object> symbol = (ArrayList<Object>) config.getList("index_symbol");
		ArrayList<Object> futureName = (ArrayList<Object>) config.getList("future_index.base_name");

		for( int i=0; i < futureName.size(); i++ )
		{
			String sFutureBaseSymbol = futureName.get(i).toString();
			String sSymbol = symbol.get(i).toString();
			//log.debug("---> Future Base Symbol :: " + sFutureBaseSymbol + "::" + sSymbol);

			idxFutNameSymbolMap.put(sSymbol, sFutureBaseSymbol);
		}
	}

	public static String getIndexFutureName(String indexName)
	{

		return idxFutNameMap.get(indexName) != null ? idxFutNameMap.get(indexName) : null;
	}

	public static String getIndexBaseSymbol(String indexName)
	{

		return futNameIndexMap.get(indexName) != null ? futNameIndexMap.get(indexName) : null;
	}

	public static String getFutureBaseSymbol(String sSymbol)
	{

		return idxFutNameSymbolMap.get(sSymbol) != null ? idxFutNameSymbolMap.get(sSymbol) : null;
	}


	public static void loadAlertCriteiaTypes() throws Exception
	{
		ArrayList<Object> alertCriteriaKey = (ArrayList<Object>) config.getList("alert.key");
		ArrayList<Object> alertCriteriaValue = (ArrayList<Object>) config.getList("alert.value");

		for(int i=0; i< alertCriteriaKey.size(); i++)
		{
			String criteriaKey = alertCriteriaKey.get(i).toString();
			String criteriaValue = alertCriteriaValue.get(i).toString();
			alertCriteiaType.put(criteriaKey, criteriaValue);	
		}
	}

	public static String getAlertCriteria(String alertKey)
	{

		return alertCriteiaType.get(alertKey);		
	}

	public static void loadMCXEnergyIndices() throws Exception
	{
		mcxEnergyIndex = new ArrayList<>();
		mcxEnergyIndex = config.getList("mcx_energy_index");
	}

	public static List<Object> getMCXEnergyIndex() throws Exception
	{
		return mcxEnergyIndex;
	}

	public static void loadMCXAgriIndices() throws Exception
	{
		mcxAgriIndex = new ArrayList<>();
		mcxAgriIndex = config.getList("mcx_agri_index");
	}

	public static List<Object> getMCXAgriIndex() throws Exception 
	{
		return mcxAgriIndex;
	}

	public static void loadMCXMetalIndices() throws Exception
	{
		mcxMetalIndex = new ArrayList<>();
		mcxMetalIndex = config.getList("mcx_metal_index");
	}

	public static List<Object> getMCXMetalIndex() throws Exception 
	{
		return mcxMetalIndex;
	}

	public static void loadMCXComdexIndices() throws Exception
	{
		mcxComdexIndex = new ArrayList<>();
		mcxComdexIndex = config.getList("mcx_comdex_index");
	}

	public static List<Object> getMCXComdexIndex() throws Exception
	{
		return mcxComdexIndex;
	}

	public static void loadtradeableIndices() throws Exception
	{
		tradeableIndex = new ArrayList<>();
		tradeableIndex = config.getList("tradable_index");
	}

	public static List<Object> getTradeableIndices() throws Exception
	{
		return tradeableIndex;
	}

	public static boolean isValidNotificationType(String notifyType) 
	{

		return ( validNotificationType != null && validNotificationType.contains(notifyType));
	}

	public static boolean isNonUnderlyingSymbol(String symbol)
	{

		return ( nonUnderlyingSymbols != null && nonUnderlyingSymbols.contains(symbol));
	}
}
