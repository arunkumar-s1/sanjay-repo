package com.msf.libsb.services.pendingorders;

import org.json.me.JSONArray;

import com.msf.libsb.services.operations.ViewPendingOrderOperations;

public class ViewPendingOrderRequest {
	private JSONArray orderNoArr;
	private String basketName;
	private String orderNo;

	public String getBasketName() {
		return basketName;
	}

	public void setBasketName(String basketName) {
		this.basketName = basketName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public JSONArray getOrderNoArr() {
		return orderNoArr;
	}

	public void setOrderNoArr(JSONArray orderNoArr) {
		this.orderNoArr = orderNoArr;
	}

	public ViewPendingOrderResponse doOperations() throws Exception {
		return ViewPendingOrderOperations.viewBrokenOrder(this);
	}

	public ViewPendingOrderResponse postRequest() throws Exception {
		return ViewPendingOrderOperations.viewRebalanceOrder(this);
	}
}
