package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.pendingorders.GetPendingOrdersResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class PendingOrdersOperations 
{
	private static Logger log = Logger.getLogger(PendingOrdersOperations.class);

	public static GetPendingOrdersResponse getBrokenOrders(String userId) throws Exception
	{
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		ResultSet res1 = null;
		int stockCount = 0;

		JSONArray orderArr = new JSONArray();
		
		try {
			String query = QueryConstants.GET_FIX_ORDERS_QUERY;

			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			log.debug(pstmt);

			res = pstmt.executeQuery();
			
			while (res.next()) {
				stockCount = 0;
				JSONObject order = new JSONObject();
				order.put(APP_CONSTANT.ORDER_NO, res.getString(DBConstants.ORDER_NO));
				order.put(APP_CONSTANT.BASKET_NAME, res.getString(DBConstants.BASKET_NAME));
				order.put(APP_CONSTANT.ICON_URL, res.getString(DBConstants.ICON_URL));
				order.put(APP_CONSTANT.CREATED_AT, res.getDate(DBConstants.CREATED_AT));
				log.debug("---> Price :: " + res.getDouble(DBConstants.PRICE));
				Double dPrice = res.getDouble(DBConstants.PRICE);
				order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(dPrice));

				String consQuery = QueryConstants.GET_STOCK_COUNT;
				
				pstmt1 = conn.prepareStatement(consQuery);
				pstmt1.setString(1, res.getString(DBConstants.ORDER_NO));
				log.debug(pstmt1);
				res1 = pstmt1.executeQuery();
				while (res1.next()) {
					stockCount++;
				}

				order.put(APP_CONSTANT.STOCK_COUNT, stockCount);

				orderArr.put(order);
				

			}
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeResultSet(res1);
			Helper.closeStatement(pstmt1);
			Helper.closeConnection(conn);
		}

		GetPendingOrdersResponse resp = new GetPendingOrdersResponse();
		resp.setOrderArr(orderArr);
		return resp;	
	}

	public static GetPendingOrdersResponse getRebalanceOrders(String userId) throws Exception
	{

		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;

		Map<String,JSONObject> orderMap=new HashMap<String,JSONObject>();
		try 
		{	
			String query=QueryConstants.GET_REBALANCE_ORDERS_QUERY;
			
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);		
			log.debug(pstmt);

			res = pstmt.executeQuery();
			JSONArray orderArray = new JSONArray();
			while(res.next())
			{	
				String orderNo=res.getString(DBConstants.ORDER_NO);
				double price=res.getDouble(APP_CONSTANT.PRICE);
				boolean isLatest=res.getBoolean(DBConstants.IS_LATEST);
				JSONObject order=orderMap.get(orderNo);
				
				if(order==null)
				{
					order=new JSONObject();
					order.put(APP_CONSTANT.ORDER_NO, orderNo);
					order.put(APP_CONSTANT.BASKET_NAME, res.getString(DBConstants.BASKET_NAME));
					order.put(APP_CONSTANT.ICON_URL, res.getString(DBConstants.ICON_URL));
						
					if(isLatest)
					{
						order.put(APP_CONSTANT.PENDING_SINCE, res.getDate(DBConstants.CREATED_AT));
						order.put(APP_CONSTANT.PRICE, price);
					}
					else {
						order.put(APP_CONSTANT.PRICE, -price);
					
					}
					log.debug("!!!!! Order :: " + order.toString());
					log.debug("----> BASKET_NAME :: " + res.getString(DBConstants.BASKET_NAME) + " :: " + orderNo);
					if(orderMap.containsKey(res.getString(DBConstants.BASKET_NAME))) {
						JSONObject tempObject = (JSONObject)orderMap.get(res.getString(DBConstants.BASKET_NAME));
						orderArray = tempObject.getJSONArray("orderArr");
						if(isLatest) {
							orderArray.put(orderNo);
						}
						order.put("orderArr", orderArray);
						
						orderMap.put(res.getString(DBConstants.BASKET_NAME), order);
						log.debug("---> If Order Map :: " + orderMap.toString());
					} else {
						orderArray = new JSONArray();
						if(isLatest) {
							orderArray.put(orderNo);
						}
						order.put("orderArr", orderArray);
						orderMap.put(res.getString(DBConstants.BASKET_NAME), order);
						log.debug("---> Else Order Map :: " + orderMap.toString());
					}
				}
				else 
				{
					log.debug("---> Order :: " + order.toString());
					double existingPrice=order.getDouble(APP_CONSTANT.PRICE);
					if(isLatest)
					{
						order.put(APP_CONSTANT.PENDING_SINCE, res.getDate(DBConstants.CREATED_AT));
						order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(price+existingPrice));
					}
					else { 
						order.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(existingPrice-price));
					}
				}	
			}
		}
		finally 
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		
		GetPendingOrdersResponse resp=new GetPendingOrdersResponse();
		resp.setOrderArr(new JSONArray(new Vector<JSONObject>(orderMap.values())));
		log.debug("----> Response get ord array :: " + resp.getOrderArr().toString());
		return resp;
	}
}

