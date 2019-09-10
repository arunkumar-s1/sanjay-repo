
package com.msf.samco.services.order;

import java.text.DecimalFormat;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.services.operations.PlaceOrderOperations;
import com.msf.libsb.services.stockbasket.LimitsRequest;
import com.msf.libsb.services.stockbasket.LimitsResponse;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.libsb.utils.helper.Session;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class MarginValidator extends SessionServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception {
		Session session = ssRequest.getSession();
		String apiURL = AppConfig.getConfigValue("limits_url");

		String requiredValue = ssRequest.getFromData(APP_CONSTANT.REQUIRED_VALUE);
		if (requiredValue == null || requiredValue.equals(""))
			requiredValue = "0";

		double requiredMarginValue = Double.valueOf(requiredValue.replace(",", ""));
		// price*1.02+price*0.0002; //2% buffer , 0.02% tax & brokerage

		requiredMarginValue *= 1.02;

		LimitsRequest lRequest = new LimitsRequest(apiURL);
		lRequest.setKey(session.getJkey());
		lRequest.setSession(session.getJsession());
		lRequest.setUserID(session.getUserId());
		lRequest.setAccount(session.getAccountID());
		lRequest.setSegment("ALL");

		LimitsResponse limitResponse = (LimitsResponse) lRequest.postRequest();

		if (!limitResponse.isSuccessResponse()) {
			throw new SamcoException(limitResponse.getErrorID());
		}

		double availabledMarginValue = Double.parseDouble(limitResponse.getNetCashAvailable());

//		ssResponse.addToData(APP_CONSTANT.MARGIN_AVAILABLE, SamcoHelper.getIndianCurrencyFormat(availabledMarginValue));
//		ssResponse.addToData(APP_CONSTANT.MARGIN_REQUIRED, SamcoHelper.getIndianCurrencyFormat(requiredMarginValue));
		// ssResponse.addToData("tradeFlag", availabledMarginValue>requiredMarginValue);
		if (availabledMarginValue < requiredMarginValue) {

			double marginDifference = -(availabledMarginValue - requiredMarginValue);
			DecimalFormat dec = new DecimalFormat("0.00");

			ssResponse.addToData(APP_CONSTANT.AMOUNT_VALUE,
					SamcoHelper.getIndianCurrencyFormat(Double.parseDouble(dec.format(marginDifference)))
							+ " to your account to place this order successfully");
			ssResponse.setInfoID(INFO_IDS.INSUFFICIENT_FUNDS);
//			ssResponse.setInfoMsg(String.valueOf(dec));

		}
		ssResponse.addToData(APP_CONSTANT.IS_AMO, PlaceOrderOperations.isAMO());
	}

	@Override
	protected String getSvcName() {
		return "MarginValidator";
	}

	@Override
	protected String getSvcGroup() {
		return "Order";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}

}
