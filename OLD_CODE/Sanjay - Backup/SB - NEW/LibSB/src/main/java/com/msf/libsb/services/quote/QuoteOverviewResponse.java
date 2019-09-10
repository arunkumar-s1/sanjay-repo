package com.msf.libsb.services.quote;

public class QuoteOverviewResponse
{
	private String basketDescription;
	private String minAmt;
	private String indexValue;
	private String change;
	private String changePer;
	private String popularity;
	private String strength;
	private String valueForMoney;
	private boolean isUserWLFlag;
	private boolean isRecommended;
	private boolean isFeatured;
	private String rationale;
	
	public String getRationale()
	{
		return rationale;
	}
	public void setRationale(String rationale)
	{
		this.rationale = rationale;
	}
	public boolean isRecommended()
	{
		return isRecommended;
	}
	public void setRecommended(boolean isRecommended)
	{
		this.isRecommended = isRecommended;
	}
	public boolean isFeatured()
	{
		return isFeatured;
	}
	public void setFeatured(boolean isFeatured)
	{
		this.isFeatured = isFeatured;
	}
	public boolean isUserWLFlag()
	{
		return isUserWLFlag;
	}
	public void setUserWLFlag(boolean isUserWLFlag)
	{
		this.isUserWLFlag = isUserWLFlag;
	}
	public String getBasketDescription()
	{
		return basketDescription;
	}
	public void setBasketDescription(String basketDescription)
	{
		this.basketDescription = basketDescription;
	}
	public String getMinAmt()
	{
		return minAmt;
	}
	public void setMinAmt(String minAmt)
	{
		this.minAmt = minAmt;
	}
	public String getIndexValue()
	{
		return indexValue;
	}
	public void setIndexValue(String indexValue)
	{
		this.indexValue = indexValue;
	}
	public String getChange()
	{
		return change;
	}
	public void setChange(String change)
	{
		this.change = change;
	}
	public String getChangePer()
	{
		return changePer;
	}
	public void setChangePer(String changePer)
	{
		this.changePer = changePer;
	}
	public String getPopularity()
	{
		return popularity;
	}
	public void setPopularity(String popularity)
	{
		this.popularity = popularity;
	}
	public String getStrength()
	{
		return strength;
	}
	public void setStrength(String strength)
	{
		this.strength = strength;
	}
	public String getValueForMoney()
	{
		return valueForMoney;
	}
	public void setValueForMoney(String valueForMoney)
	{
		this.valueForMoney = valueForMoney;
	}
}
