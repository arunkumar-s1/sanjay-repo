package com.msf.libsb.services.order;

import com.msf.libsb.base.BaseRequest;
import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.utils.exception.SamcoException;

public class PlaceOrderRequest extends BaseRequest 
{

	public PlaceOrderRequest(String apiUrl) throws SamcoException 
	{
		super(apiUrl);
	}

	public void setProductAlias( String sProdAlias ) throws Exception
	{
		this.put(APIConstants.PRODUCT_ALIAS, sProdAlias);
	}
	
	public void setUserID( String sUserID ) throws Exception
	{
		this.put(APIConstants.USER_ID, sUserID);
	}
	
	public void setAccountID( String sAccountID ) throws Exception
	{
		this.put(APIConstants.O_ACCOUNT_ID, sAccountID);
	}
	
	public void setTradingSymbol( String tradingSymbol ) throws Exception
	{
		this.put(APIConstants.TRADING_SYMBOL, tradingSymbol);
	}

	public void setExchange( String sExchange ) throws Exception
	{
		this.put(APIConstants.O_EXCHANGE, sExchange);
	}

	public void setTransactionType( String sTranType ) throws Exception
	{
		this.put(APIConstants.TRANSACTION_TYPE, sTranType);
	}
	
	public void setRetention( String sRetention ) throws Exception
	{
		this.put(APIConstants.O_RETENTION, sRetention);
	}

	public void setPriceType( String sPriceType ) throws Exception
	{
		this.put(APIConstants.PRICE_TYPE, sPriceType);
	}
	
	public void setQuantity( String sQuantity ) throws Exception
	{
		this.put(APIConstants.QUANTITY, sQuantity);
	}
	
	public void setDisclosedQty( String sDisClosedQty ) throws Exception
	{
		this.put(APIConstants.DISCLOSED_QUANTITY, sDisClosedQty);
	}
	
	public void setScripToken( String scripToken ) throws Exception
	{
		this.put(APIConstants.O_SCRIP_CODE, scripToken);
	}

	public void setMarketProtection( String marketProtection ) throws Exception
	{
		this.put(APIConstants.O_MARKET_PROTECTION, marketProtection);
	}
	
	public void setPrice( String sPrice ) throws Exception
	{
		this.put(APIConstants.O_PRICE, sPrice);
	}

	public void setTriggerPrice( String sTriggerPrice) throws Exception
	{		
		this.put(APIConstants.TRIGGER_PRICE, sTriggerPrice);
	}
	
	public void setProductCode( String sProductCode) throws Exception
	{		
		this.put(APIConstants.PRODUCT_CODE, sProductCode);
	}
	
	public void setDateDays( String sDateDays) throws Exception
	{		
		this.put(APIConstants.DATE_DAYS, sDateDays);
	}
	
	public void setAMO( String sAMO) throws Exception
	{		
		this.put(APIConstants.AMO_ORDER, sAMO);
	}
	
	public void setPositionSquare( String sPositionSquare) throws Exception
	{		
		this.put(APIConstants.POSITION_SQUARE_FLAG, sPositionSquare);
	}
	
	public void setMinQty( String sMinQty) throws Exception
	{		
		this.put(APIConstants.MIN_QUANTITY, sMinQty);
	}
	
	public void setVendorCode( String sVendorCode) throws Exception
	{		
		this.put(APIConstants.VENDOR_CODE, sVendorCode);
	}
	
	public void setOrderSource( String sSource) throws Exception
	{		
		this.put("orderSource", sSource);
	}
	
	public void setUserTag( String sTag) throws Exception
	{		
		this.put("userTag", sTag);
	}
	
	@Override
	public BaseResponse postRequest() throws Exception 
	{
		String apiResponse = postToApi();
		return new PlaceOrderResponse( apiResponse);
	}

}
