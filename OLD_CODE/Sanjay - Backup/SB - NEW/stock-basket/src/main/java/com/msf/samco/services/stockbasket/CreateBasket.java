package com.msf.samco.services.stockbasket;

import org.json.me.JSONArray;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.operations.BasketOperations;
import com.msf.libsb.services.stockbasket.CreateBasketRequest;
import com.msf.libsb.services.stockbasket.BasketResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class CreateBasket extends BaseServlet 
{

	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(CreateBasket.class);

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		JSONArray constituents=ssRequest.getArrayFromData(APP_CONSTANT.CONSTITUENTS);		
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);
		BasketOperations.checkConstituentsExists(basketName,constituents);
		
		int investType = ssRequest.getIntFromData(APP_CONSTANT.INVEST_TYPE);
		Double initialFee = ssRequest.getDoubleFromData(APP_CONSTANT.INITIAL_FEE);
		Double rebalanceFee = ssRequest.getDoubleFromData(APP_CONSTANT.REBALANCE_FEE);
		Double monitorFee = ssRequest.getDoubleFromData(APP_CONSTANT.MONITORFEE);
		int trendingFlag = ssRequest.getIntFromData(APP_CONSTANT.TRENDING_FLAG);
		int recommendedFlag = ssRequest.getIntFromData(APP_CONSTANT.RECOMMENDED_FLAG);
		int featuredFlag = ssRequest.getIntFromData(APP_CONSTANT.FEATURED_FLAG);
		int riskFlag = ssRequest.getIntFromData(APP_CONSTANT.RISK_FLAG);
		String strength = ssRequest.getFromData(APP_CONSTANT.STRENGTH);
		String valueForMoney = ssRequest.getFromData(APP_CONSTANT.VALUE_FOR_MONEY);
		String category=ssRequest.getFromData(APP_CONSTANT.CATEGORY);		
		String subCategory = ssRequest.getData().optString(APP_CONSTANT.SUB_CATEGORY,null);
		String description = ssRequest.getFromData(APP_CONSTANT.DESCRIPTION);
		String rationale = ssRequest.getFromData(APP_CONSTANT.RATIONALE);
		String imageURL = ssRequest.getFromData(APP_CONSTANT.IMAGE_URL);
		String iconURL = ssRequest.getFromData(APP_CONSTANT.ICON_URL);
		String disclaimer = ssRequest.getFromData(APP_CONSTANT.DISCLAIMER);
		Double indexValue = ssRequest.getDoubleFromData(APP_CONSTANT.INDEX_VALUE);
		String benchMark=ssRequest.getFromData(APP_CONSTANT.BENCHMARK);
		Double oneMonthRet=ssRequest.getDoubleFromData(APP_CONSTANT.M1);
		Double threeMonthRet=ssRequest.getDoubleFromData(APP_CONSTANT.M3);
		Double sixMonthRet=ssRequest.getDoubleFromData(APP_CONSTANT.M6);
		Double oneYearRet=ssRequest.getDoubleFromData(APP_CONSTANT.Y1);
		Double threeYearRet=ssRequest.getDoubleFromData(APP_CONSTANT.Y3);
		Double fiveYearRet=ssRequest.getDoubleFromData(APP_CONSTANT.Y5);
			
		CreateBasketRequest createBasketReq = new CreateBasketRequest();
		createBasketReq.setBasketName(basketName);
		createBasketReq.setInvestType(investType);
		createBasketReq.setInitialFee(initialFee);
		createBasketReq.setRebalanceFee(rebalanceFee);
		createBasketReq.setMonitorFee(monitorFee);
		createBasketReq.setDescription(description);
		createBasketReq.setRecommendedFlag(recommendedFlag);
		createBasketReq.setFeaturedFlag(featuredFlag);
		createBasketReq.setRiskFlag(riskFlag);
		createBasketReq.setStrength(strength);
		createBasketReq.setValueForMoney(valueForMoney);;
		createBasketReq.setCategory(category);
		createBasketReq.setSubCategory(subCategory);
		createBasketReq.setRationale(rationale);
		createBasketReq.setDisclaimer(disclaimer);
		createBasketReq.setIndexValue(indexValue);
		createBasketReq.setBenchMark(benchMark);;
		createBasketReq.setConstituents(constituents);
		createBasketReq.setImageURL(imageURL);
		createBasketReq.setIconURL(iconURL);
		createBasketReq.setTrendingFlag(trendingFlag);
		createBasketReq.setOneMonthRet(oneMonthRet);
		createBasketReq.setThreeMonthRet(threeMonthRet);
		createBasketReq.setSixMonthRet(sixMonthRet);
		createBasketReq.setOneYearRet(oneYearRet);
		createBasketReq.setThreeYearRet(threeYearRet);
		createBasketReq.setFiveYearRet(fiveYearRet);
		
		
		BasketResponse createResp = createBasketReq.doOperations();
		ssResponse.addToData(APP_CONSTANT.STATUS, createResp.getStatus());
		ssResponse.addToData(APP_CONSTANT.MESSAGE, createResp.getMessage());
	}

	@Override
	protected String getSvcName() {
		return "CreateBasket";
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
