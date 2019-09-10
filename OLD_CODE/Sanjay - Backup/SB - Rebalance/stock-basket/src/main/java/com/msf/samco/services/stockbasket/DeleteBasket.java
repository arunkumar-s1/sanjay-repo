package com.msf.samco.services.stockbasket;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.stockbasket.BasketResponse;
import com.msf.libsb.services.stockbasket.DeleteBasketRequest;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class DeleteBasket extends BaseServlet 
{

	private static final long serialVersionUID = 1L;
	
	//private static Logger log = Logger.getLogger(DeleteBasket.class);

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{	
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);
		String basketVersion = ssRequest.getFromData(APP_CONSTANT.BASKET_VERSION);	
		
		DeleteBasketRequest deleteBasketReq = new DeleteBasketRequest();
		deleteBasketReq.setBasketName(basketName);
		deleteBasketReq.setBasketVersion(basketVersion);
		
		BasketResponse deleteResp = deleteBasketReq.doOperations();
		ssResponse.addToData(APP_CONSTANT.STATUS, deleteResp.getStatus());
		ssResponse.addToData(APP_CONSTANT.MESSAGE, deleteResp.getMessage());
	}

	@Override
	protected String getSvcName() {
		return "DeleteBasket";
	}

	@Override
	protected String getSvcGroup() {
		return "StockBasket";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}

}
