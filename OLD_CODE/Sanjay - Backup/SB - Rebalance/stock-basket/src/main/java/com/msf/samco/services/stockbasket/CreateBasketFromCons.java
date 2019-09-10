package com.msf.samco.services.stockbasket;

import org.json.me.JSONArray;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.operations.BasketOperations;
import com.msf.libsb.services.stockbasket.BasketResponse;
import com.msf.libsb.services.stockbasket.CreateBasketRequest;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class CreateBasketFromCons extends BaseServlet 
{

	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(CreateBasket.class);

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		JSONArray constituents=ssRequest.getArrayFromData(APP_CONSTANT.CONSTITUENTS);		
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);
		BasketOperations.checkConstituentsExists(basketName,constituents);	
			
		CreateBasketRequest createBasketReq = new CreateBasketRequest();
		createBasketReq.setBasketName(basketName);
		createBasketReq.setConstituents(constituents);
		
		BasketResponse createResp = createBasketReq.postRequest();
		ssResponse.addToData(APP_CONSTANT.STATUS, createResp.getStatus());
		ssResponse.addToData(APP_CONSTANT.MESSAGE, createResp.getMessage());
	}

	@Override
	protected String getSvcName() 
	{
		return "CreateBasketFromCons";
	}

	@Override
	protected String getSvcGroup() 
	{
		return "StockBasket";
	}

	@Override
	protected String getSvcVersion() 
	{
		return "1.0.0";
	}
}
