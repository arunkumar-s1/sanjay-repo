package com.msf.samco.services.pendingorder;

import org.json.me.JSONArray;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.libsb.services.pendingorders.RebalanceOrderRequest;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class RebalanceOrder extends SessionServlet {
	private static final long serialVersionUID = 1L;

	// private static Logger log = Logger.getLogger(FixBasketOrder.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception {

		JSONArray orderArr = ssRequest.getArrayFromData("orderArr");
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);

		RebalanceOrderRequest rebalanceReq = new RebalanceOrderRequest();
		rebalanceReq.setSession(ssRequest.getSession());
		rebalanceReq.setBasketName(basketName);
		rebalanceReq.setOrderArr(orderArr);

		PlaceBasketOrderResponse rebalanceRes = rebalanceReq.doOperations();

		ssResponse.addToData(APP_CONSTANT.BASKET_NAME, basketName);
		// ssResponse.addToData(APP_CONSTANT.ICON_URL, fixRes.getIconURL());
		ssResponse.addToData(APP_CONSTANT.ORDER_STATUS, rebalanceRes.getOrderStatus());
		ssResponse.addToData(APP_CONSTANT.MESSAGE, rebalanceRes.getMessage());
		ssResponse.addToData(APP_CONSTANT.NET_PRICE, rebalanceRes.getNetPrice());
		ssResponse.addToData(APP_CONSTANT.ESTIMATED_TAX, rebalanceRes.getEstimatedTax());
		ssResponse.addToData(APP_CONSTANT.BASKET_ORDER_NO, rebalanceRes.getBasketOrderNo());
		ssResponse.addToData(APP_CONSTANT.TRANSACTION_TYPE, rebalanceRes.getTransactionType());
		ssResponse.addToData(APP_CONSTANT.ORIGINAL_QTY, rebalanceRes.getOriginalQty());
		ssResponse.addToData(APP_CONSTANT.FILLED_QTY, rebalanceRes.getFilledQty());
		ssResponse.addToData(APP_CONSTANT.STOCKS, rebalanceRes.getStocks());

	}

	@Override
	protected String getSvcName() {
		return "RebalanceOrder";
	}

	@Override
	protected String getSvcGroup() {
		return "PendingOrder";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}
}
