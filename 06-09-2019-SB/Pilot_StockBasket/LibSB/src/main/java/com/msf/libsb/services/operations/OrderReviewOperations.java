package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.order.OrderReviewRequest;
import com.msf.libsb.services.order.OrderReviewResponse;
import com.msf.libsb.utils.helper.SamcoHelper;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class OrderReviewOperations
{
	private static Logger log = Logger.getLogger(OrderReviewOperations.class);

	public static OrderReviewResponse orderReview(OrderReviewRequest orderReviewRequest) throws Exception
	{
		Connection conn = null;
		ResultSet res = null;
		PreparedStatement pstmt = null;

		JSONArray symbolArray = new JSONArray();
		Double netPrice = 0.0;
		Double estimatedTax = 0.0;
		String basketName = orderReviewRequest.getBasketName();
		String basketVersion = orderReviewRequest.getBasketVersion();
		int basketQuantity = orderReviewRequest.getQty();
		String iconURL = null;
		String query = QueryConstants.ORDER_REVIEW_QUERY;
		String type = APP_CONSTANT.BUY;
		double dEstimatedBrokerage = 0.00;
		double dEstimatedExpenses = 0.0;
		double dEstimatedFees = 0.0;
		double dMultiples = 0.0;
		double totalExp = 0.0;
		double netBrokerage = 0.0;
		double netexpenses = 0.0;
		double minAmount = 0.0;
		double minNetBrokerage = 0.0;
		double minNetexpenses = 0.0;
		
		try
		{
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			if (basketVersion.isEmpty())
				query = query + " and bd.IS_LATEST=1";

			else
			{
				query = query + " and bd.BASKET_VERSION='" + basketVersion + "'";
				type = APP_CONSTANT.SELL;
				String sSalesQuery = QueryConstants.SALES_FEES;
				pstmt = conn.prepareStatement(sSalesQuery);
				pstmt.setString(1, basketName);
				pstmt.setString(2, basketVersion);
				log.debug(pstmt);
				res = pstmt.executeQuery();
				if(res.next()) {
					do {
						dEstimatedFees = res.getDouble("SALES_FEE") * 1.18;
					} while (res.next());
				}
			}

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, basketName);
			log.debug(pstmt);
			res = pstmt.executeQuery();
			if (res.next())
			{
				iconURL = res.getString(DBConstants.ICON_URL);
				do
				{
					int stockQuantity = basketQuantity * res.getInt(DBConstants.QTY);// stock quantity * basket quantity

					double price = res.getDouble(DBConstants.LAST_TRADED_PRICE);// price of individual stock

					dMultiples = Double.valueOf(stockQuantity) * price; // Q*P

					dEstimatedBrokerage = (dMultiples * 0.002); // individual brokarage of a stock
					if(dEstimatedBrokerage >= 20) {
						dEstimatedBrokerage = 20.0;
					}

					dEstimatedExpenses = (dMultiples * 0.0015); // indv expense
					
					double minPrice = res.getInt(DBConstants.QTY) * price;
					
					double minEstimatedBrokerage = minPrice * 0.002;
					if(minEstimatedBrokerage >= 20) {
						minEstimatedBrokerage = 20.0;
					}
					double minEstimatedExpenses = minPrice * 0.0015;

					JSONObject symbol = new JSONObject();
					symbol.put(APP_CONSTANT.QUANTITY, stockQuantity);
					symbol.put(APP_CONSTANT.PRICE, SamcoHelper.getIndianCurrencyFormat(price));
					symbol.put(APP_CONSTANT.DESCRIPTION, res.getString(DBConstants.DESCRIPTION));
					symbol.put(APP_CONSTANT.TYPE, type);
					symbolArray.put(symbol);
					netPrice += dMultiples;
					netBrokerage += dEstimatedBrokerage;
					log.debug("Estimated Brokerage :: " + dEstimatedBrokerage);
					log.debug("Net brokerage :: " + netBrokerage);
					netexpenses += dEstimatedExpenses;
					log.debug("Estimated expenses :: " + dEstimatedExpenses);
					log.debug("Net Expenses :: " + netexpenses);
					
					minAmount += minPrice;
					minNetBrokerage += minEstimatedBrokerage;
					minNetexpenses += minEstimatedExpenses;

				} while (res.next());

			}
		} finally
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		estimatedTax = netBrokerage + netexpenses;
		log.debug("Estimated tax :: " + estimatedTax);
		OrderReviewResponse orderReviewResponse = new OrderReviewResponse();
		orderReviewResponse.setIconURL(iconURL);
		orderReviewResponse.setSymbolArray(symbolArray);
		orderReviewResponse.setNetPrice(netPrice);// change
		orderReviewResponse.setEstimatedTax(estimatedTax);
		orderReviewResponse.setEstimatedFees(dEstimatedFees);
		orderReviewResponse.setTransactionType(type);
		if(type.equalsIgnoreCase(APP_CONSTANT.BUY)) {
			orderReviewResponse.setTotalPrice(netPrice + estimatedTax);// change
		} else {
			
			orderReviewResponse.setTotalPrice(netPrice - estimatedTax - dEstimatedFees);// change
		}
		
		orderReviewResponse.setMinAmount(minAmount + minNetBrokerage + minNetexpenses);
		return orderReviewResponse;
	}
}
