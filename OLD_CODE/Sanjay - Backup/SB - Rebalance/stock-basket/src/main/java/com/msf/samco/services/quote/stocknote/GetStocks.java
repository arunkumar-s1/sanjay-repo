package com.msf.samco.services.quote.stocknote;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.quote.stocknote.GetStocksRequest;
import com.msf.libsb.services.quote.stocknote.GetStocksResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class GetStocks extends SessionServlet
{

	private static final long serialVersionUID = 1L;

	// private static Logger log = Logger.getLogger(SearchBasket.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{
		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);

		GetStocksRequest stocksReq = new GetStocksRequest();
		stocksReq.setBasketName(basketName);

		GetStocksResponse stocksRes = stocksReq.getStocks();
		ssResponse.addToData(APP_CONSTANT.STOCKS, stocksRes.getStockArr());
	}

	@Override
	protected String getSvcName()
	{
		return "GetStocks";
	}

	@Override
	protected String getSvcGroup()
	{
		return "Quote";
	}

	@Override
	protected String getSvcVersion()
	{
		return "1.0.0";
	}

}
