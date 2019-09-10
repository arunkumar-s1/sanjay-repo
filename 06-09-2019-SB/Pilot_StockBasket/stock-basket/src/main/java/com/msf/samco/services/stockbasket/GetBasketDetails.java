package com.msf.samco.services.stockbasket;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.stockbasket.GetBasketDetailsRequest;
import com.msf.libsb.services.stockbasket.GetBasketDetailsResponse;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.log.Logger;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class GetBasketDetails extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(GetBasketDetails.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		JSONObject dataObject = null;
		String sBasketName = null;		

		try 
		{
			dataObject = ssRequest.getData();
			sBasketName = dataObject.getString(APP_CONSTANT.BASKET_NAME);		
		} 
		catch (JSONException e) 
		{
			throw new InvalidRequestParameter("Parameter missing");
		}
		
		GetBasketDetailsRequest basketdetailsReq = new GetBasketDetailsRequest();
		basketdetailsReq.setBasketName(sBasketName);
		
		GetBasketDetailsResponse basketdetailsRes = (GetBasketDetailsResponse)basketdetailsReq.doOperations();
		ssResponse.addToData(APP_CONSTANT.STATUS, basketdetailsRes.getString(APP_CONSTANT.STATUS));
		ssResponse.addToData(APP_CONSTANT.BASKET_DETAILS, basketdetailsRes.getJSONArray(APP_CONSTANT.BASKET_DETAILS));
		ssResponse.addToData(APP_CONSTANT.NET_AMOUNT, basketdetailsRes.getString(APP_CONSTANT.NET_AMOUNT));
		log.debug("Net amt ="+basketdetailsRes.getString(APP_CONSTANT.NET_AMOUNT));
	}

	@Override
	protected String getSvcGroup() {
		return "StockBasket";
	}
	
	@Override
	protected String getSvcName() {
		return "GetBasketDetails";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}

}
