package com.msf.libsb.services.stockbasket;

import com.msf.libsb.services.operations.BasketOperations;

public class ModifyBasketRequest 
{
	private String basketName;
	private int investType;
	private Double initialFee;
	private Double rebalanceFee;
	private Double monitorFee;
	private String description;
	private int trendingFlag;
	private int recommendedFlag;
	private int featuredFlag;
	private int riskFlag;
	private String strength;
	private String valueForMoney;
	private String category;
	private String subCategory;
	private String rationale;
	private String disclaimer;
	private Double indexValue;
	private String benchMark;
	private String imageURL;
	private String iconURL;
	private Double oneMonthRet;
	private Double threeMonthRet;
	private Double sixMonthRet;
	private Double oneYearRet;
	private Double threeYearRet;
	private Double fiveYearRet;
	
	public String getBasketName()
	{
		return basketName;
	}

	public void setBasketName(String basketName)
	{
		this.basketName = basketName;
	}
	
//	public String getBasketVersion()
//	{
//		return basketVersion;
//	}
//
//	public void setBasketVersion(String basketVersion)
//	{
//		this.basketVersion = basketVersion;
//	}
	
	public String getImageURL()
	{
		return imageURL;
	}

	public void setImageURL(String imageURL)
	{
		this.imageURL = imageURL;
	}
	
	public String getIconURL() 
	{
		return iconURL;
	}

	public void setIconURL(String iconURL) 
	{
		this.iconURL = iconURL;
	}

	public int getInvestType()
	{
		return investType;
	}
	
	public int getTrendingFlag()
	{
		return trendingFlag;
	}

	public void setTrendingFlag(int trendingFlag)
	{
		this.trendingFlag = trendingFlag;
	}

	public Double getOneMonthRet()
	{
		return oneMonthRet;
	}

	public void setOneMonthRet(Double oneMonthRet)
	{
		this.oneMonthRet = oneMonthRet;
	}

	public Double getThreeMonthRet()
	{
		return threeMonthRet;
	}

	public void setThreeMonthRet(Double threeMonthRet)
	{
		this.threeMonthRet = threeMonthRet;
	}

	public Double getSixMonthRet()
	{
		return sixMonthRet;
	}

	public void setSixMonthRet(Double sixMonthRet)
	{
		this.sixMonthRet = sixMonthRet;
	}

	public Double getOneYearRet()
	{
		return oneYearRet;
	}

	public void setOneYearRet(Double oneYearRet)
	{
		this.oneYearRet = oneYearRet;
	}

	public Double getThreeYearRet()
	{
		return threeYearRet;
	}

	public void setThreeYearRet(Double threeYearRet)
	{
		this.threeYearRet = threeYearRet;
	}

	public Double getFiveYearRet()
	{
		return fiveYearRet;
	}

	public void setFiveYearRet(Double fiveYearRet)
	{
		this.fiveYearRet = fiveYearRet;
	}

	public void setInvestType(int investType)
	{
		this.investType = investType;
	}

	public Double getInitialFee()
	{
		return initialFee;
	}

	public void setInitialFee(Double initialFee)
	{
		this.initialFee = initialFee;
	}

	public Double getRebalanceFee()
	{
		return rebalanceFee;
	}

	public void setRebalanceFee(Double rebalanceFee)
	{
		this.rebalanceFee = rebalanceFee;
	}

	public Double getMonitorFee()
	{
		return monitorFee;
	}

	public void setMonitorFee(Double monitorFee)
	{
		this.monitorFee = monitorFee;
	}

	public int getRecommendedFlag()
	{
		return recommendedFlag;
	}

	public void setRecommendedFlag(int recommendedFlag)
	{
		this.recommendedFlag = recommendedFlag;
	}

	public int getFeaturedFlag()
	{
		return featuredFlag;
	}

	public void setFeaturedFlag(int featuredFlag)
	{
		this.featuredFlag = featuredFlag;
	}

	public int getRiskFlag()
	{
		return riskFlag;
	}

	public void setRiskFlag(int riskFlag)
	{
		this.riskFlag = riskFlag;
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

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getSubCategory()
	{
		return subCategory;
	}

	public void setSubCategory(String subCategory)
	{
		this.subCategory = subCategory;
	}

	public String getRationale()
	{
		return rationale;
	}

	public void setRationale(String rationale)
	{
		this.rationale = rationale;
	}

	public String getDisclaimer()
	{
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer)
	{
		this.disclaimer = disclaimer;
	}

	public Double getIndexValue()
	{
		return indexValue;
	}

	public void setIndexValue(Double indexValue)
	{
		this.indexValue = indexValue;
	}

	public String getBenchMark()
	{
		return benchMark;
	}

	public void setBenchMark(String benchMark)
	{
		this.benchMark = benchMark;
	}
	
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public BasketResponse doOperations() throws Exception 
	{
		return BasketOperations.modifyBasket(this);
	}
}
