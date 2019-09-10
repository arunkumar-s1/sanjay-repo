package com.msf.samco.services.limits;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.stockbasket.LimitsRequest;
import com.msf.libsb.services.stockbasket.LimitsResponse;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.libsb.utils.helper.Session;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class ViewLimits extends SessionServlet {

	private static final long serialVersionUID = 1L;

	// private static Logger log = Logger.getLogger(ViewLimits.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception {
		Session session = ssRequest.getSession();
		String segment = "ALL";
		String exchange = ssRequest.getData().optString(APP_CONSTANT.EXCHANGE);

		if (exchange.equalsIgnoreCase(APP_CONSTANT.MCX_SX_FO_EXCHANGE)
				|| exchange.equalsIgnoreCase(APP_CONSTANT.MCX_EXCHANGE)) {
			segment = "COM";
		}

		LimitsRequest lRequest = new LimitsRequest(AppConfig.getConfigValue("limits_url"));
		lRequest.setKey(session.getJkey());
		lRequest.setSession(session.getJsession());
		lRequest.setUserID(session.getUserId());
		lRequest.setAccount(session.getAccountID());
		lRequest.setSegment(segment);

		LimitsResponse lResponse = (LimitsResponse) lRequest.postRequest();

		if (!lResponse.isSuccessResponse()) {
			throw new SamcoException(lResponse.getErrorID());
		}
		ssResponse.addToData(APP_CONSTANT.MARGIN_AVAILABLE,
				SamcoHelper.getIndianCurrencyFormat(Double.parseDouble(lResponse.getNetCashAvailable())));
		ssResponse.addToData(APP_CONSTANT.UTILIZED_AMOUNT, lResponse.getUtilisedAmount());
	}

	@Override
	protected String getSvcName() {
		return "ViewLimits";
	}

	@Override
	protected String getSvcGroup() {
		return "Limits";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}

}
