package com.msf.samco.services.order;

import org.json.me.JSONArray;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.services.operations.OrderHistoryOperations;
import com.msf.libsb.services.order.OrderHistoryResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class OrderHistory extends SessionServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{

		OrderHistoryResponse orderHisRes = OrderHistoryOperations.orderHistory(ssRequest.getSession().getUserId());
		JSONArray basketNames = orderHisRes.getBasketNames();
		if (basketNames.length() == 0)
			ssResponse.setInfoID(INFO_IDS.NO_DATA_FOUND);
		else
			ssResponse.addToData(APP_CONSTANT.ORDER_HISTORY_ARR, basketNames);
//		ssResponse.addToData(APP_CONSTANT.ORDER_HISTORY_ARR, orderHisRes.getBasketNames());
	}

	@Override
	protected String getSvcName()
	{
		return "OrderHistory";
	}

	@Override
	protected String getSvcGroup()
	{
		return "Order";
	}

	@Override
	protected String getSvcVersion()
	{
		return "1.0.0";
	}
}
