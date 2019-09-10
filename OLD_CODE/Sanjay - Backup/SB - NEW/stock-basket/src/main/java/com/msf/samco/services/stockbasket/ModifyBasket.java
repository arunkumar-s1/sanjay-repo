package com.msf.samco.services.stockbasket;

import org.json.me.JSONObject;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.stockbasket.BasketResponse;
import com.msf.libsb.services.stockbasket.ModifyBasketRequest;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class ModifyBasket extends BaseServlet 
{

	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(ModifyBasket.class);

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);
		//String basketVersion=ssRequest.getFromData(APP_CONSTANT.BASKET_VERSION);
		int investType = ssRequest.optIntFromData(APP_CONSTANT.INVEST_TYPE,-1);
		Double initialFee = ssRequest.optDoubleFromData(APP_CONSTANT.INITIAL_FEE);
		Double rebalanceFee = ssRequest.optDoubleFromData(APP_CONSTANT.REBALANCE_FEE);
		Double monitorFee = ssRequest.optDoubleFromData(APP_CONSTANT.MONITORFEE);
		int trendingFlag = ssRequest.optIntFromData(APP_CONSTANT.TRENDING_FLAG,-1);
		int recommendedFlag = ssRequest.optIntFromData(APP_CONSTANT.RECOMMENDED_FLAG,-1);
		int featuredFlag = ssRequest.optIntFromData(APP_CONSTANT.FEATURED_FLAG,-1);
		int riskFlag = ssRequest.optIntFromData(APP_CONSTANT.RISK_FLAG,-1);
		String strength = ssRequest.optString(APP_CONSTANT.STRENGTH);
		String valueForMoney = ssRequest.optString(APP_CONSTANT.VALUE_FOR_MONEY);
		JSONObject dataObject=ssRequest.getData();
		String category=dataObject.optString(APP_CONSTANT.CATEGORY);
		
		String subCategory = dataObject.optString(APP_CONSTANT.SUB_CATEGORY);
		String description = dataObject.optString(APP_CONSTANT.DESCRIPTION);
		String rationale = dataObject.optString(APP_CONSTANT.RATIONALE);
		String imageURL = dataObject.optString(APP_CONSTANT.IMAGE_URL);
		String iconURL = dataObject.optString(APP_CONSTANT.ICON_URL);
		String disclaimer = dataObject.optString(APP_CONSTANT.DISCLAIMER);
		Double indexValue = ssRequest.optDoubleFromData(APP_CONSTANT.INDEX_VALUE);
		String benchMark=dataObject.optString(APP_CONSTANT.BENCHMARK);
		Double oneMonthRet=ssRequest.optDoubleFromData(APP_CONSTANT.M1);
		Double threeMonthRet=ssRequest.optDoubleFromData(APP_CONSTANT.M3);
		Double sixMonthRet=ssRequest.optDoubleFromData(APP_CONSTANT.M6);
		Double oneYearRet=ssRequest.optDoubleFromData(APP_CONSTANT.Y1);
		Double threeYearRet=ssRequest.optDoubleFromData(APP_CONSTANT.Y3);
		Double fiveYearRet=ssRequest.optDoubleFromData(APP_CONSTANT.Y5);
		
		ModifyBasketRequest modifyBasketReq = new ModifyBasketRequest();
		modifyBasketReq.setBasketName(basketName);
		//modifyBasketReq.setBasketVersion(basketVersion);
		modifyBasketReq.setInvestType(investType);
		modifyBasketReq.setInitialFee(initialFee);
		modifyBasketReq.setRebalanceFee(rebalanceFee);
		modifyBasketReq.setMonitorFee(monitorFee);
		modifyBasketReq.setDescription(description);
		modifyBasketReq.setRecommendedFlag(recommendedFlag);
		modifyBasketReq.setFeaturedFlag(featuredFlag);
		modifyBasketReq.setRiskFlag(riskFlag);
		modifyBasketReq.setStrength(strength);
		modifyBasketReq.setValueForMoney(valueForMoney);;
		modifyBasketReq.setCategory(category);
		modifyBasketReq.setSubCategory(subCategory);
		modifyBasketReq.setRationale(rationale);
		modifyBasketReq.setDisclaimer(disclaimer);
		modifyBasketReq.setIndexValue(indexValue);
		modifyBasketReq.setBenchMark(benchMark);
		modifyBasketReq.setImageURL(imageURL);
		modifyBasketReq.setIconURL(iconURL);
		modifyBasketReq.setTrendingFlag(trendingFlag);
		modifyBasketReq.setOneMonthRet(oneMonthRet);
		modifyBasketReq.setThreeMonthRet(threeMonthRet);
		modifyBasketReq.setSixMonthRet(sixMonthRet);
		modifyBasketReq.setOneYearRet(oneYearRet);
		modifyBasketReq.setThreeYearRet(threeYearRet);
		modifyBasketReq.setFiveYearRet(fiveYearRet);
		
		BasketResponse modifyResp = modifyBasketReq.doOperations();
		ssResponse.addToData(APP_CONSTANT.STATUS, modifyResp.getStatus());
		ssResponse.addToData(APP_CONSTANT.MESSAGE, modifyResp.getMessage());
	}

	@Override
	protected String getSvcName() {
		return "ModifyBasket";
	}

	@Override
	protected String getSvcGroup() {
		return "StockBasket";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}

}
