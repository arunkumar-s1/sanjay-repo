package com.msf.libsb.services.pendingorders;

import org.json.me.JSONArray;

import com.msf.libsb.services.operations.RebalanceOrderOperations;
import com.msf.libsb.services.order.PlaceBasketOrderResponse;
import com.msf.libsb.utils.helper.Session;

public class RebalanceOrderRequest {
	private String basketName;
	private JSONArray orderArr;
	private Session session;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getBasketName() {
		return basketName;
	}

	public void setBasketName(String basketName) {
		this.basketName = basketName;
	}

	public JSONArray getOrderArr() {
		return orderArr;
	}

	public void setOrderArr(JSONArray orderArr) {
		this.orderArr = orderArr;
	}

	public PlaceBasketOrderResponse doOperations() throws Exception {
		return RebalanceOrderOperations.rebalanceBasketOrder(this);
	}

}
