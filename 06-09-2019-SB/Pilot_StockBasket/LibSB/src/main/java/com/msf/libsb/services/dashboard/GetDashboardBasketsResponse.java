package com.msf.libsb.services.dashboard;

import org.json.me.JSONArray;

public class GetDashboardBasketsResponse {
	private JSONArray recommendedBasketArr;
	private JSONArray featuredBasketArr;
	private JSONArray popularBasketArr;
	private boolean initialInvestFlag;
	private String message;
	private String investMessage;
	private String imageurl;

	public JSONArray getPopularBasketArr() {
		return popularBasketArr;
	}

	public void setPopularBasketArr(JSONArray popularBasketArr) {
		this.popularBasketArr = popularBasketArr;
	}

	public JSONArray getRecommendedBasketArr() {
		return recommendedBasketArr;
	}

	public void setRecommendedBasketArr(JSONArray recommendedBasketArr) {
		this.recommendedBasketArr = recommendedBasketArr;
	}

	public JSONArray getFeaturedBasketArr() {
		return featuredBasketArr;
	}

	public void setFeaturedBasketArr(JSONArray featuredBasketArr) {
		this.featuredBasketArr = featuredBasketArr;
	}

	public void setInitialInvest(boolean initialInvestFlag) {
		this.initialInvestFlag = initialInvestFlag;
	}

	public boolean getInitialInvest() {
		return this.initialInvestFlag;
	}

	public void setMessage(String message) {
		this.message = message;

	}

	public String getMessage() {
		return message;
	}

	public String getInvestMessage() {
		return investMessage;
	}

	public void setInvestMessage(String investMessage) {
		this.investMessage = investMessage;
	}
	
	public String getImageUrl() {
		return imageurl;
	}

	public void setImageUrl(String sURL) {
		this.imageurl = sURL;
	}


}
