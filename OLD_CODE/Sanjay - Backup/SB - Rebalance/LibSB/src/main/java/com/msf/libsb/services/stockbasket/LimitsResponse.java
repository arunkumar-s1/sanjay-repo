package com.msf.libsb.services.stockbasket;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;

public class LimitsResponse extends BaseResponse {
	
	public LimitsResponse(String sAPIResponse) throws Exception {
		super(sAPIResponse);
	}
	
	public String getOpeningBalance() throws Exception {
		return this.getString(APIConstants.LIMITS_OPEN_BAL);
	}
	
	public String getUtilisedAmount() throws Exception {
		return this.getString(APIConstants.LIMITS_USED_AMT);
	}
	
	public String getStockVal() throws Exception {
		return this.getString(APIConstants.LIMITS_STOCK_VAL);
	}
	
	public String getDirCollateralVal() throws Exception {
		return this.getString(APIConstants.LIMITS_DIR_COLL_VAL);
	}
	
	public String getAdhocMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_ADHOC_MARGIN);
	}
	
	public String getBranchAdhocMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_BRANCH_ADHOC);
	}
	
	public String getCredits() throws Exception {
		return this.getString(APIConstants.LIMITS_CREDITS);
	}
	
	public String getNotionalCash() throws Exception {
		return this.getString(APIConstants.LIMITS_NOTIONAL_CASH);
	}
	
	public String getPayinAmount() throws Exception {
		return this.getString(APIConstants.LIMITS_PAY_IN_AMT);
	}
	
	public String getCncVarMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_CNC_VAR_MARGIN);
	}
	
	public String getCncElmMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_CNC_ELM_MARGIN);
	}
	
	public String getBuyExpo() throws Exception {
		return this.getString(APIConstants.LIMITS_BUY_EXPO);
	}
	
	public String getSellExpo() throws Exception {
		return this.getString(APIConstants.LIMITS_SELL_EXPO);
	}
	
	public String getIpoAmount() throws Exception {
		return this.getString(APIConstants.LIMITS_IPO_AMT);
	}
	
	public String getPayoutAmount() throws Exception {
		return this.getString(APIConstants.LIMITS_PAYOUT_AMT);
	}
	
	public String getCategory() throws Exception {
		return this.getString(APIConstants.LIMITS_CATEGORY);
	}
	
	public String getTurnover() throws Exception {
		return this.getString(APIConstants.LIMITS_TURNOVER);
	}
	
	public String getMultiplier() throws Exception {
		return this.getString(APIConstants.LIMITS_MULTIPLIER);
	}
	
	public String getGrossExposureVal() throws Exception {
		return this.getString(APIConstants.LIMITS_GROSS_EXPO_VAL);
	}
	
	public String getELM() throws Exception {
		return this.getString(APIConstants.LIMITS_ELM);
	}
	
	public String getValueInDelivery() throws Exception {
		return this.getString(APIConstants.LIMITS_VAL_IN_DELIVERY);
	}
	
	public String getVarMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_VAR_MARGIN);
	}
	
	public String getSpanMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_SPAN_MARGIN);
	}
	
	public String getAdhocScripMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_ADHOC_SCRIP_MARGIN);
	}
	
	public String getScripBasketMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_SCRIP_MKT_MARGIN);
	}
	
	public String getExpoMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_EXPO_MARGIN);
	}
	
	public String getPremium() throws Exception {
		return this.getString(APIConstants.LIMITS_PREMIUM);
	}
	
	public String getRelMTOM() throws Exception {
		return this.getString(APIConstants.LIMITS_RE_MTOM);
	}

	public String getUnRelMTOM() throws Exception {
		return this.getString(APIConstants.LIMITS_UN_MTOM);
	}
	
	public String getMFAmount() throws Exception {
		return this.getString(APIConstants.LIMITS_MF_AMOUNT);
	}

	public String getDebits() throws Exception {
		return this.getString(APIConstants.LIMITS_DEBITS);
	}
	
	public String getCNCSellCredit() throws Exception {
		return this.getString(APIConstants.LIMITS_CNC_SELL_CREDIT);
	}
	
	public String getCNCMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_CNC_MARGIN);
	}
	
	public String getCNCBrokerage() throws Exception {
		return this.getString(APIConstants.LIMITS_CNC_BROKERAGE);
	}
	
	public String getCNCUnRelMTOM() throws Exception {
		return this.getString(APIConstants.LIMITS_CNC_UN_MTOM);
	}
	
	public String getCNCRelMTOM() throws Exception {
		return this.getString(APIConstants.LIMITS_CNC_RE_MTOM);
	}
	
	public String getMFSSAmount() throws Exception {
		return this.getString(APIConstants.LIMITS_MFSS_AMT);
	}
	
	public String getNFOSpread() throws Exception {
		return this.getString(APIConstants.LIMITS_NFO_SPREAD_BENEFIT);
	}

	public String getCDSSpread() throws Exception {
		return this.getString(APIConstants.LIMITS_CDS_SPREAD_BENEFIT);
	}
	
	public String getBrokerage() throws Exception {
		return this.getString(APIConstants.LIMITS_BROKERAGE);
	}

	public String getCOMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_CO_MARGIN_REQ);
	}
	
	public String getBOMargin() throws Exception {
		return this.getString(APIConstants.LIMITS_BO_MARGIN_REQ);
	}
	
	public String getSegment() throws Exception {
		return this.getString(APIConstants.LIMITS_SEGMENT);
	}
	
	public String getBookedPNL() throws Exception {
		return this.getString(APIConstants.LIMITS_BPNL);
	}
	
	public String getUnRelBookedPNL() throws Exception {
		return this.getString(APIConstants.LIMITS_UBPNL);
	}

	public String getBuyPower() throws Exception {
		return this.getString(APIConstants.LIMITS_BUYPOWER);
	}
	
	public String getAdhoc() throws Exception {
		return this.getString(APIConstants.LIMITS_ADHOC);
	}
	
	public String getNetCashAvailable() throws Exception {
		return this.getString(APIConstants.LIMITS_NET_CASH_AVAIL);
	}
	
	public double getTotalMarginAvailable() throws Exception{
		
		return Double.parseDouble(this.getOpeningBalance())
		+ Double.parseDouble(this.getStockVal())
		+ Double.parseDouble(this.getNotionalCash())
		+ Double.parseDouble(this.getPayinAmount())
		+ Double.parseDouble(this.getAdhocMargin());
	}

	public double getTotalMarginUsed() throws Exception{
		
		return Double.parseDouble(this.getUtilisedAmount())
				+ Double.parseDouble(this.getCNCSellCredit())
				+ Double.parseDouble(this.getVarMargin()) + Double.parseDouble(this.getELM())
				+ Double.parseDouble(this.getCncVarMargin())
				+ Double.parseDouble(this.getCncElmMargin())
				+ Double.parseDouble(this.getBuyExpo()) + Double.parseDouble(this.getSellExpo())
				+ Double.parseDouble(this.getGrossExposureVal())
				+ Double.parseDouble(this.getSpanMargin())
				+ Double.parseDouble(this.getExpoMargin()) + Double.parseDouble(this.getPremium())
				+ Double.parseDouble(this.getMultiplier());
	}
}