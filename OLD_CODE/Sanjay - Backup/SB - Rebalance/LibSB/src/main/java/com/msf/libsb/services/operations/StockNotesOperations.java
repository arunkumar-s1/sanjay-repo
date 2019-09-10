package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.quote.stocknote.GetStocksRequest;
import com.msf.libsb.services.quote.stocknote.GetStocksResponse;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class StockNotesOperations 
{
	
	private static Logger log = Logger.getLogger(StockNotesOperations.class);
	
	public static GetStocksResponse getStockFeeds(GetStocksRequest stockNotesReq) throws Exception
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String basketName = stockNotesReq.getBasketName();
		ResultSet res= null;
		JSONArray symbolArr=new JSONArray();

		try 
		{	
			String Query=QueryConstants.GET_STOCKNOTES_QUERY;
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(Query);
			log.debug(pstmt);
			pstmt.setString(1,basketName);
			res=pstmt.executeQuery();

			while(res.next()) 
			{
				
				JSONObject symbol=new JSONObject();
				symbol.put(APP_CONSTANT.SYMBOL,res.getString(DBConstants.SYMBOL));
				symbol.put(APP_CONSTANT.CONTENT, res.getString(DBConstants.CONTENT));
				symbol.put(APP_CONSTANT.MEDIA_URL, res.getString(DBConstants.MEDIA_URL));
				symbol.put(APP_CONSTANT.MEDIA_TYPE, res.getString(DBConstants.MEDIA_TYPE));
				symbol.put(APP_CONSTANT.MEDIA_ICON, res.getString(DBConstants.MEDIA_ICON));
				symbol.put(APP_CONSTANT.SEO_URL, res.getString(DBConstants.SEO_URL));
				
				symbolArr.put(symbol);
			}	
		}  
		
		finally
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		
		GetStocksResponse stockNotesRes = new GetStocksResponse();
		stockNotesRes.setStockArr(symbolArr);
		return stockNotesRes;
	}

	public static GetStocksResponse getStocks(GetStocksRequest stocksReq) throws SQLException, JSONException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String basketName = stocksReq.getBasketName();
		ResultSet res= null;
		JSONArray stockArr=new JSONArray();

		try 
		{	
			String Query=QueryConstants.GET_STOCKS_QUERY;
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(Query);		
			pstmt.setString(1,basketName);
			log.debug(pstmt);
			
			res=pstmt.executeQuery();

			while(res.next()) 
			{		
				JSONObject stocks=new JSONObject();
				stocks.put(APP_CONSTANT.DESCRIPTION,res.getString(DBConstants.DESCRIPTION));
				stocks.put(APP_CONSTANT.RATING, res.getString(DBConstants.RATING));
				stocks.put(APP_CONSTANT.QUANTITY, res.getString(DBConstants.QTY));	
				stockArr.put(stocks);
			}	
		}  
		
		finally
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		
		GetStocksResponse stocksRes = new GetStocksResponse();
		stocksRes.setStockArr(stockArr);
		return stocksRes;
	}


}
