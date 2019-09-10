package com.msf.samco.services.dashboard;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.dashboard.GetDashboardBasketsResponse;
import com.msf.libsb.services.operations.DashboardOperations;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class GetDashboardBaskets extends SessionServlet {
	private static final long serialVersionUID = 1L;

	// private static Logger log = Logger.getLogger(GetDashboardBaskets.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception {

		String userId = ssRequest.getSession().getUserId();

		GetDashboardBasketsResponse resp = DashboardOperations.getDashboardBaskets(userId);
		ssResponse.addToData(APP_CONSTANT.RECOMMENDED_BASKETS, resp.getRecommendedBasketArr());
		ssResponse.addToData(APP_CONSTANT.FEATURED_BASKETS, resp.getFeaturedBasketArr());
		ssResponse.addToData(APP_CONSTANT.POPULAR_BASKETS, resp.getPopularBasketArr());
		ssResponse.addToData(APP_CONSTANT.INITIAL_INVEST, resp.getInitialInvest());
		ssResponse.addToData(APP_CONSTANT.INVESTMENT_MESSAGE, resp.getMessage());
		ssResponse.addToData(APP_CONSTANT.FUND_MESSAGE, resp.getInvestMessage());
		ssResponse.addToData(APP_CONSTANT.IMAGE_URL, resp.getImageUrl());

	}

	@Override
	protected String getSvcName() {
		return "GetDashboardBaskets";
	}

	@Override
	protected String getSvcGroup() {
		return "Dashboard";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}
}
