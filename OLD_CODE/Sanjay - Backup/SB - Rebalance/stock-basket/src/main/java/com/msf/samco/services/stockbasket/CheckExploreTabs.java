package com.msf.samco.services.stockbasket;

import org.json.me.JSONObject;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.operations.FetchBasketOperations;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class CheckExploreTabs extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(SearchBasket.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
				
		JSONObject flags =FetchBasketOperations.checkFlag();
		ssResponse.addToData(APP_CONSTANT.RECOMMENDED,flags.getBoolean(APP_CONSTANT.RECOMMENDED));
		ssResponse.addToData(APP_CONSTANT.FEATURED,flags.getBoolean(APP_CONSTANT.FEATURED));
	}

	@Override
	protected String getSvcName() 
	{
		return "CheckExploreTabs";
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
