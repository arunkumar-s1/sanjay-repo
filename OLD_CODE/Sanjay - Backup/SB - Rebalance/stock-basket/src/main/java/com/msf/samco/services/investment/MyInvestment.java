package com.msf.samco.services.investment;

import org.json.me.JSONArray;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.investments.MyInvestmentResponse;
import com.msf.libsb.services.operations.InvestmentOperations;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class MyInvestment extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(FixBasketOrder.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		JSONArray orders=ssRequest.getArrayFromData(APP_CONSTANT.ORDERS);

		MyInvestmentResponse resp = InvestmentOperations.getInvestment(orders, ssRequest.getSession().getUserId(), ssRequest.getFromData(APP_CONSTANT.BASKET_NAME), ssRequest.getFromData(APP_CONSTANT.BASKET_VERSION));
		
		ssResponse.addToData(APP_CONSTANT.ORDERS, orders);
		ssResponse.addToData(APP_CONSTANT.LAST_INVESTED_ON, resp.getLastInvestedOn());
		ssResponse.addToData(APP_CONSTANT.OVERALL_CURRENT_VALUE, resp.getOverallCurrentValue());
		ssResponse.addToData(APP_CONSTANT.TOTAL_INVESTMENT, resp.getTotalInvestment());
		ssResponse.addToData(APP_CONSTANT.OVERALL_PL_VALUE, resp.getOverallProfitOrLossValue());
		ssResponse.addToData(APP_CONSTANT.OVERALL_PL_PERCENT, resp.getOverallProfitOrLossPercent());
		ssResponse.addToData(APP_CONSTANT.INVESTMENT_ARR, resp.getInvestmentArr());
	}

	@Override
	protected String getSvcName() 
	{
		return "MyInvestment";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "Investment";
	}

	@Override
	protected String getSvcVersion() 
	{
		return "1.0.0";
	}
}
