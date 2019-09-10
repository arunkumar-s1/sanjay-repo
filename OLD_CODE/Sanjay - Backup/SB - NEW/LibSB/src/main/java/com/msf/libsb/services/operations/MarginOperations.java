package com.msf.libsb.services.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.QueryConstants;
import com.msf.libsb.services.order.MarginValidatorRequest;
import com.msf.log.Logger;
import com.msf.utils.helper.Helper;

public class MarginOperations 
{
	private static Logger log = Logger.getLogger(MarginOperations.class);

	public static double getRequiredMarginValue(MarginValidatorRequest marginValidatorRequest) throws Exception
	{
		Connection conn = null;
		ResultSet res = null;
		PreparedStatement pstmt = null;

		String basketName=marginValidatorRequest.getBasketName();
		int basketQuantity=marginValidatorRequest.getQty();
		double price=0.0;
		
		try
		{
			conn = DBConnection.getInstance().getStockBasketDBConnection();
			pstmt=conn.prepareStatement(QueryConstants.MARGIN_VALIDATOR_QUERY);
			pstmt.setString(1, basketName);
			log.debug(pstmt);
			res = pstmt.executeQuery();
		
			if(res.next())
			{
				price= basketQuantity*res.getDouble(DBConstants.NET_PRICE);	
			}
		
		}
		finally 
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
		
		return price*1.02+price*0.0002;  //2% buffer , 0.02% tax & brokerage
	}
}

