package com.msf.samco.services.quote;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.operations.QuoteOperations;
import com.msf.libsb.services.quote.QuoteOverviewResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class Overview extends SessionServlet
{
	private static final long serialVersionUID = 1L;

	// private static Logger log = Logger.getLogger(SearchBasket.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);

		QuoteOverviewResponse resp=QuoteOperations.quoteOverView(basketName,ssRequest.getSession().getUserId());
		ssResponse.addToData(APP_CONSTANT.DESCRIPTION, resp.getBasketDescription());
		ssResponse.addToData(APP_CONSTANT.MIN_AMT, resp.getMinAmt());
		ssResponse.addToData(APP_CONSTANT.INDEX_VALUE, resp.getIndexValue());
		ssResponse.addToData(APP_CONSTANT.CHANGE, resp.getChange());
		ssResponse.addToData(APP_CONSTANT.CHANGE_PERCENT, resp.getChangePer());
		ssResponse.addToData(APP_CONSTANT.POPULARITY, SamcoHelper.toCamelCase(resp.getPopularity()));
		ssResponse.addToData(APP_CONSTANT.STRENGTH, SamcoHelper.toCamelCase(resp.getStrength()));
		ssResponse.addToData(APP_CONSTANT.VALUE_FOR_MONEY, SamcoHelper.toCamelCase(resp.getValueForMoney()));
		ssResponse.addToData(APP_CONSTANT.RECOMMENDED_FLAG, resp.isRecommended());
		ssResponse.addToData(APP_CONSTANT.FEATURED_FLAG, resp.isFeatured());
		ssResponse.addToData(APP_CONSTANT.USER_WL_FLAG, resp.isUserWLFlag());
		ssResponse.addToData(APP_CONSTANT.RATIONALE, resp.getRationale());
	}

	@Override
	protected String getSvcName()
	{
		return "Overview";
	}

	@Override
	protected String getSvcGroup()
	{
		return "Quote";
	}

	@Override
	protected String getSvcVersion()
	{
		return "1.0.0";
	}
}
