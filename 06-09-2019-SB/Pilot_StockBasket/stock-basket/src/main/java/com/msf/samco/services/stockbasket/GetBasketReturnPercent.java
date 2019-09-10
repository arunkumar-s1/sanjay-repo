package com.msf.samco.services.stockbasket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.log.Logger;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.SessionServlet;
import com.msf.utils.helper.Helper;

public class GetBasketReturnPercent extends SessionServlet 
{
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(GetBasketReturnPercent.class);

	@Override
	protected void doPostProcessRequest(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		JSONObject dataObject = null;
		String sBasketName = null;	
		String version =null;

		try 
		{
			dataObject = ssRequest.getData();
			sBasketName = dataObject.getString(APP_CONSTANT.BASKET_NAME);	
			version = dataObject.getString(APP_CONSTANT.BASKET_VERSION);
			
		} 
		catch (JSONException e) 
		{
			throw new InvalidRequestParameter("Parameter missing");
		}
		
		
		DBConnection pool=DBConnection.getInstance();
		getReturnPercent(pool,sBasketName,version,ssResponse);
		
	}
	public void getReturnPercent(DBConnection pool,String basketName,String version,SSResponse ssResponse) throws SQLException, JSONException
	{
		ResultSet res = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		//String returnPer="";
		JSONObject returnPercentage=new JSONObject();
		
		try {
		conn=pool.getStockBasketDBConnection();
		//returnPer=getColumnName(retPercent);
		String sQuery="SELECT ONE_MONTH_RET_PER,THREE_MONTH_RET_PER,SIX_MONTH_RET_PER,ONE_YEAR_RET_PER,THREE_YEAR_RET_PER,FIVE_YEAR_RET_PER FROM BASKET_DETAILS WHERE BASKET_NAME = ? AND BASKET_VERSION = ?";
		pstmt = conn.prepareStatement(sQuery);
		pstmt.setString(1, basketName);
		pstmt.setString(2, version);
		log.debug(pstmt);
		res = pstmt.executeQuery();
		if(res.next()) {
			 String m1=res.getString("ONE_MONTH_RET_PER");
			 String m3=res.getString("THREE_MONTH_RET_PER");
			 String m6=res.getString("SIX_MONTH_RET_PER");
			 String y1=res.getString("ONE_YEAR_RET_PER");
			 String y3=res.getString("THREE_YEAR_RET_PER");
			 String y5=res.getString("FIVE_YEAR_RET_PER");
			 returnPercentage.put(APP_CONSTANT.M1, m1==null?"null":m1);
			 returnPercentage.put(APP_CONSTANT.M3, m3==null?"null":m3);
			 returnPercentage.put(APP_CONSTANT.M6, m6==null?"null":m6);
			 returnPercentage.put(APP_CONSTANT.Y1, y1==null?"null":y1);
			 returnPercentage.put(APP_CONSTANT.Y3, y3==null?"null":y3);
			 returnPercentage.put(APP_CONSTANT.Y5, y5==null?"null":y5);
		}
		}
		catch (SQLException e) 
		{
			throw e;
		} 
		finally 
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}	
		ssResponse.addToData(APP_CONSTANT.STATUS, "success");
		ssResponse.addToData(APP_CONSTANT.RETURN_PERCENTAGE, returnPercentage);
	}

//	private String getColumnName(String retPercent) {
//		String Percent="";
//		if(retPercent.equals("1M"))
//			Percent =new String("ONE_MONTH_RET_PER");
//		else if(retPercent.equals("3M"))
//			Percent =new String("THREE_MONTH_RET_PER");
//		else if(retPercent.equals("6M"))
//			Percent =new String("SIX_MONTH_RET_PER");
//		else if(retPercent.equals("1Y"))
//			Percent =new String("ONE_YEAR_RET_PER");
//		else if(retPercent.equals("3Y"))
//			Percent =new String("THREE_YEAR_RET_PER");
//		else if(retPercent.equals("5Y"))
//			Percent =new String("FIVE_YEAR_RET_PER");
//		return Percent;
//	}
	@Override
	protected String getSvcGroup() {
		return "StockBasket";
	}
	
	@Override
	protected String getSvcName() {
		return "GetBasketReturnPercent";
	}

	@Override
	protected String getSvcVersion() {
		return "1.0.0";
	}

}
