package com.msf.samco.services.quote.stocknote;

import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.services.quote.stocknote.GetStocksRequest;
import com.msf.libsb.services.quote.stocknote.GetStocksResponse;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;

public class GetStockNotes extends SessionServlet
{

	private static final long serialVersionUID = 1L;

	// private static Logger log = Logger.getLogger(SearchBasket.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception
	{

		String basketName = ssRequest.getFromData(APP_CONSTANT.BASKET_NAME);

		GetStocksRequest stockNotesReq = new GetStocksRequest();
		stockNotesReq.setBasketName(basketName);

		GetStocksResponse stocksRes = stockNotesReq.getStockFeeds();
		ssResponse.addToData(APP_CONSTANT.SYMBOLS, stocksRes.getStockArr());
	}

	@Override
	protected String getSvcName()
	{
		return "GetStockNotes";
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
