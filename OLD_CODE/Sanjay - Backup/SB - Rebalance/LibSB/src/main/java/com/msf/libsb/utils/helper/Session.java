package com.msf.libsb.utils.helper;

import java.util.Hashtable;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

public class Session 
{

	private String sessionID;
	private String Jsession;
	private String appID;
	private String jKey;
	private String userid;
	private String build;
	private String userType;
	private String userStage;
	private String product_alias;
	private String is_enable_transPass;
	private String accountID;
	private String branchID;
	private String defaultMarketWatch;
	private String brokerName;
	private String samco_token;
	private String product_type;
	private String paying_url;
	private String payout_url;
	private String backoffice_url;
	private String jWeblinkURL;
	private String exchange_array;
	private String sSyncNeeded;
	private String sPosSyncNeeded;
	private JSONArray openPositions;
	private JSONArray cashPlusPositions;

	public String getAccountID()
	{
		return accountID;
	}

	public void setAccountID(String accountID)
	{
		this.accountID = accountID;
	}

	public String getBranchID() 
	{
		return branchID;
	}

	public void setBranchID(String branchID) 
	{
		this.branchID = branchID;
	}

	public String getDefaultMarketWatch() 
	{
		return defaultMarketWatch;
	}

	public void setDefaultMarketWatch(String defaultMarketWatch)
	{
		this.defaultMarketWatch = defaultMarketWatch;
	}

	public void setJsession(String jsession) 
	{
		this.Jsession = jsession;
	}

	public String getJsession()
	{
		return this.Jsession;
	}

	public void setProductAlias(String product_alias) 
	{
		this.product_alias = product_alias;
	}

	public String getProductAlias() 
	{
		return this.product_alias;
	}

	public void setIsEnableTransPass(String is_enable_transPass) 
	{
		this.is_enable_transPass = is_enable_transPass;
	}

	public String getIsEnableTransPass()
	{
		return this.is_enable_transPass;
	}

	public void setUserId(String userId)
	{
		this.userid = userId;
	}

	public String getUserId()
	{
		return userid;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public String getAppID()
	{
		return appID;
	}

	public String getJkey() 
	{
		return jKey;
	}

	public String getBuild()
	{
		return build;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setSessionID(String sessionID) 
	{
		this.sessionID = sessionID;
	}

	public void setAppID(String appID)
	{
		this.appID = appID;
	}

	public void setJkey(String jkey)
	{
		this.jKey = jkey;
	}

	public void setBuild(String build)
	{
		this.build = build;
	}

	public void setUserType(String userType) 
	{
		this.userType = userType;
	}

	public String getUserStage() 
	{
		return userStage;
	}

	public void setUserStage(String userStage) 
	{
		this.userStage = userStage;
	}

	public String getBrokerName()
	{
		return brokerName;
	}

	public void setBrokerName(String brokerName)
	{
		this.brokerName = brokerName;
	}

	public void setSamcoToken(String samcoToken)
	{
		this.samco_token = samcoToken;
	}

	public String getSamcoToken() 
	{
		return this.samco_token;
	}

	public void setProductType(String productType) 
	{
		this.product_type = productType;
	}

	public String getProductType() 
	{
		return this.product_type;
	}

	public void setPaying(String urlObj)
	{
		this.paying_url = urlObj;
	}

	public void setPayout(String urlObj)
	{
		this.payout_url = urlObj;
	}

	public void setBackoffice(String urlObj)
	{
		this.backoffice_url = urlObj;
	}

	public String getPayinObj()
	{
		return this.paying_url;
	}

	public String getPayoutObj()
	{
		return this.payout_url;
	}

	public String getBackofficeObj() 
	{
		return this.backoffice_url;
	}

	public String getjWeblinkURL() 
	{
		return jWeblinkURL;
	}

	public void setjWeblinkURL(String jWeblinkURL)
	{
		this.jWeblinkURL = jWeblinkURL;
	}

	public void setExchangeArray(String exchange)
	{
		this.exchange_array = exchange;
	}

	public String getExchangeArray()
	{
		return exchange_array;
	}

	public void setSyncWatchlist(String sSync)
	{
		this.sSyncNeeded = sSync;
	}

	public String getSyncWatchlistStatus()
	{
		return sSyncNeeded;
	}

	public void setSyncPositions(String sSync)
	{
		this.sPosSyncNeeded = sSync;
	}

	public String getSyncPositionStatus()
	{
		return sPosSyncNeeded;
	}

	public void setOpenPositionsData(JSONArray jOpenArray)
	{
		this.openPositions = jOpenArray;
	}

	public JSONArray getOpenPositionData()
	{
		return openPositions;
	}

	public void setCashPlusPositionsData(JSONArray jCashArray) 
	{
		this.cashPlusPositions = jCashArray;
	}

	public JSONArray getCashPlusPositionData()
	{
		return cashPlusPositions;
	}

}
