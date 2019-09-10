package com.msf.libsb.utils.helper;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.bouncycastle.util.encoders.Hex;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.msf.libsb.alert.AlertData;
import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.DBConstants;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.login.TokenGeneration;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.log.Logger;
import com.msf.utils.crypto.gibberish.Gibberish;
import com.msf.utils.helper.Helper;

/*SAMCO HELPER FUNCTIONS*/
public class SamcoHelper 
{

	public static String joinChar(JSONArray arr,char c) throws JSONException
	{
		StringBuilder sb=new StringBuilder();
		int len=arr.length();
		for(int i=0;i<len;i++)
		{
			sb.append(arr.getString(i)+c);
		}
		sb.setLength(sb.length()-1);
		return sb.toString();
	}
	
	public static String getIndianCurrencyFormat(Double amount)
	{
		if(amount==null) return "0.00";
		
		String negation="";
		if(amount<0) 
		{
			negation="-";
			amount*=-1;
		}
		
		String amt=String.format("%.2f", amount);
		int len=amt.length();
		if(len<=6)
		{
			return negation+amt;
		}
		int flag=0;
		len-=6;
		if(len%2==1) flag=1;

		StringBuilder sb=new StringBuilder();
		for(int i=0;i<len;i++)
		{
			sb.append(amt.charAt(i));
			if(++flag==2)
			{
				sb.append(",");
				flag=0;
			}
		}

		return negation+sb+amt.substring(len);
	}

	private static Logger log = Logger.getLogger(SamcoHelper.class);

	public static String generateSessionID(String request)
	{
		return Helper.MD5sum(request + System.currentTimeMillis());
	}

	public static String hashData(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(data.getBytes());
		String hashedData = Hex.toHexString(digest);
		return hashedData;
	}

	public static String hashPassword(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(data.getBytes());

		for (int i = 1; i <= 999; i++)
		{
			digest = md.digest(digest);
		}
		String hashedData = Hex.toHexString(digest);
		return hashedData;
	}

	public static boolean isHoliday(String strDate, String Holiday)
	{

		List<String> holidays = Arrays.asList(Holiday.split("\\s*,\\s*"));
		int n = holidays.size();

		for (int i = 0; i < n; i++)
		{

			if (strDate.equals(holidays.get(i)))
				return true;
		}

		return false;
	}

	public static boolean isAmoOrder() throws Exception
	{
		boolean isAmo = true;
		String sMarketTime = AppConfig.getConfigValue("market_time");
		JSONObject timeObj = new JSONObject(sMarketTime);
		int startTime = Integer.valueOf(timeObj.getString("startTime"));
		int endTime = Integer.valueOf(timeObj.getString("endTime"));

		int currTime = (new Date()).getHours();

		if (startTime <= currTime && endTime >= currTime)
			isAmo = false;

		return isAmo;
	}

	public static boolean isRetentionTypeAvailable(String retType) throws Exception 
	{
		boolean isAvailble = false;

		String sRetentionList = AppConfig.getConfigValue("retention_list");

		JSONObject retObj = new JSONObject(sRetentionList);
		JSONArray retentionList = retObj.getJSONArray("retention_list");

		for (int i = 0; i < retentionList.length(); i++) 
		{
			if (retType.equals(retentionList.getString(i))) 
			{
				isAvailble = true;
				break;
			}
		}

		return isAvailble;
	}

	public static String getDateString(String seconds)
	{
		String sFormatedDate = "";
		if (seconds == "")
			return sFormatedDate;

		else
		{
			long expSeconds = (Long.parseLong(seconds) + 315513000) * 1000;

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(expSeconds);

			Date date = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMMyyyy");
			sFormatedDate = simpleDateFormat.format(date);
			return sFormatedDate.toUpperCase();
		}
	}

	public static String getDateFromMilliseconds(String milliSeconds)
	{
		String sFormatedDate = "";
		if (milliSeconds == "")
			return sFormatedDate;

		else
		{
			long expSeconds = Long.parseLong(milliSeconds);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(expSeconds);

			Date date = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMMyyyy");
			sFormatedDate = simpleDateFormat.format(date);
			return sFormatedDate.toUpperCase();
		}
	}

	/** identify the search-symbols is like NIFTY800CE or NIFY800 */



	public static String formatDecimal(double d)
	{
		if (d == (long) d)
			return String.format("%d", (long) d);
		else
			return String.format("%s", d);
	}

	/**
	 * sends true if today is in expiry week. sends true if the expiryDate of
	 * the symbol is in the current expiry month
	 */
	public static boolean showAllProdType(String expiryDateString, String sNearMonthExpiry, String sExchange)
			throws ParseException, SamcoException, JSONException
	{
		if (sExchange.equalsIgnoreCase(APP_CONSTANT.MCX_SX_FO_EXCHANGE))
		{
			if (expiryDateString.equalsIgnoreCase(sNearMonthExpiry))
			{
				//log.debug("MFO Same expiry so returning true");
				return true;
			}
		}
		if (expiryDateString.equalsIgnoreCase("-")) 
		{
			return true;
		}
		DateFormat df = new SimpleDateFormat("dd MMM yy");

		boolean isSameMonth = false;

		Date today = df.parse(df.format(new Date()));
		Date lastThursdat = getLastThursday();

		isSameMonth = isExpiryOnCurrentMonth(expiryDateString);

		Date expiryDate = df.parse(expiryDateString);
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(today);
		cal2.setTime(expiryDate);

		int diffInDays = (int) ((lastThursdat.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

		if (diffInDays < 4 && diffInDays >= 0 || isSameMonth)
		{

			return true;

		} 
		else if ((lastThursdat.getTime() < today.getTime())
				&& ((cal1.get(Calendar.MONTH) + 1) == cal2.get(Calendar.MONTH)))
		{

			return true;

		} else 
		{

			return false;
		}

	}

	public static Date getLastThursday() 
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		int m = cal.get(Calendar.MONTH);
		int y = cal.get(Calendar.YEAR);
		cal.clear();
		cal.set(y, m + 1, 2);
		cal.add(Calendar.DAY_OF_MONTH, -(cal.get(Calendar.DAY_OF_WEEK) % 7 + 2));
		return cal.getTime();
	}

	public static String getExchangeFromSymbol(String symbol) throws SamcoException 
	{
		String sExchange = "";
		try 
		{

			//log.debug("Symbol in getExchange ::: " + symbol);
			String[] sym;

			// if(!symbol.contains("_"))
			// return "";

			sym = symbol.split("_");
			if (sym.length == 1) 
			{

				if (symbol.charAt(0) != '-')
				{
					return "";
				}
				else if (Integer.parseInt(symbol) < 0)
				{
					sExchange = APP_CONSTANT.INDICES_EXCHANGE;
				}

			}
			else if (sym.length == 2)
			{
				sExchange = sym[1];
			}
			else 
			{
				throw new SamcoException(INFO_IDS.INVALID_EXCHANGE, "Invalid Exchange");
			}

		} 
		catch (NumberFormatException e)
		{
			throw new SamcoException(INFO_IDS.INVALID_EXCHANGE, "Invalid Exchange");
		}

		return sExchange;
	}

	public static boolean isExpiryOnCurrentMonth(String expiryDateString) throws ParseException
	{

		DateFormat df = new SimpleDateFormat("dd MMM yyyy");
		DateFormat expiryDateDF = new SimpleDateFormat("dd MMM yy");
		Date expiryDate;
		expiryDate = expiryDateDF.parse(expiryDateString);

		Date today = df.parse(df.format(new Date()));
		/* Checking for same month expiry */
		Calendar cal = Calendar.getInstance();
		cal.setTime(expiryDate);
		int m = cal.get(Calendar.MONTH);

		int y = cal.get(Calendar.YEAR);

		cal.clear();
		cal.setTime(today);

		int mt = cal.get(Calendar.MONTH);

		int yt = cal.get(Calendar.YEAR);
		//log.debug("Todays year :: " + yt);
		if (yt == y && mt == m)
		{
			return true;
		} 
		else
		{
			return false;
		}
	}

	public static boolean isInSquareOffTimings(String exchange) throws ParseException, SamcoException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		Date before = null;
		Date after = null;

		Date today = new Date();
		String todayDateStr = sdf.format(today);
		String[] sqrOfTimes = AppConfig.getConfigValue(exchange + "_square_off_time").split("-");

		String sqrOffStartTime = todayDateStr + " " + sqrOfTimes[0];
		String sqrOffEndTime = todayDateStr + " " + sqrOfTimes[1];
		before = sdf2.parse(sqrOffStartTime);
		after = sdf2.parse(sqrOffEndTime);
		return (before.getTime() < today.getTime()) && after.getTime() > today.getTime();

	}

	public static void isValidCriteria(JSONObject criteriaObj) throws Exception
	{

		String alertCriteria = criteriaObj.getString(APP_CONSTANT.ALERT_CRITERIA_TYPE);

		if (!alertCriteria.equals(APP_CONSTANT.ALERT_LOWER_PRICE)
				&& !alertCriteria.equals(APP_CONSTANT.ALERT_GREATER_PRICE)
				&& !alertCriteria.equals(APP_CONSTANT.ALERT_SAME_PRICE)
				&& !alertCriteria.equals(APP_CONSTANT.ALERT_LOWER_VOLUME)
				&& !alertCriteria.equals(APP_CONSTANT.ALERT_GREATER_VOLUME)
				&& !alertCriteria.equals(APP_CONSTANT.ALERT_GREATER_PRICE_PERCENTAGE)
				&& !alertCriteria.equals(APP_CONSTANT.ALERT_LOWER_PRICE_PERCENTAGE) && !alertCriteria.equals("ll")
				&& !alertCriteria.equals("gl") && !alertCriteria.equals("sl"))
		{
			throw new InvalidRequestParameter(INFO_IDS.INVALID_REQUEST_PARAM);
		}

		double criteria_Value = Double
				.parseDouble(criteriaObj.getString(APP_CONSTANT.ALERT_CRITERIA_VALUE).replaceAll(",", ""));

		if (criteria_Value <= 0.0)
			throw new InvalidRequestParameter(INFO_IDS.INVALID_REQUEST_PARAM);

	}

	public static void isValidNotificationType(JSONArray notifyObj) throws Exception 
	{
		for (int i = 0; i < notifyObj.length(); i++)
		{
			if (!notifyObj.getString(i).equals("E") && !notifyObj.getString(i).equals("P"))
				throw new InvalidRequestParameter(INFO_IDS.INVALID_REQUEST_PARAM);
		}
	}

	public static void validateInteger(JSONArray arr) throws NumberFormatException, JSONException 
	{
		for (int i = 0; i < arr.length(); i++)
		{
			int value = Integer.parseInt(arr.getString(i));

			if (value == 0)
				throw new NumberFormatException("");
		}
	}

	public static String getDatabaseValue(String value) 
	{

		if (value == null)
			return "-";
		else
			return value;
	}

	public static String formatDatabaseValue(String value, int precision)
	{

		if (value == null)
			return "-";
		else
			return format(value, precision);
	}

	public static String formatChartDatabaseValue(String value, int precision)
	{

		if (value == null)
			return "-";
		else
			return chartFormat(value, precision);
	}

	public static String getBestBidAskValues(String value, int precision) 
	{

		if (value == null)
			return "-";
		else
		{
			String askBid = format(value.split(";")[0], precision + 1);
			double bAsk = Double.parseDouble(askBid);

			String result = new DecimalFormat("#0.##").format(bAsk);
			return format(result, precision);
		}
	}

	public static String format(String sNumber, int precision)
	{

		if (sNumber == null || sNumber.equals("--") || sNumber.equals("-") || sNumber.equals(APP_CONSTANT.IGNORE_VAL))
			return DBConstants.NULL_DATA;

		if (sNumber == null || sNumber.length() < 1 || sNumber.equals(APP_CONSTANT.NULL_DATA))
			return sNumber;

		sNumber = sNumber.replaceAll(",", "");

		sNumber = BigDecimal.valueOf(Double.parseDouble(sNumber)).toPlainString();

		String units = "";
		String output = sNumber;

		if (units.length() > 0 && precision == 0)
		{
			// Even if the precision is specified as zero giving 2 precision.
			precision = 2;
		}

		int dotIndex = output.indexOf(".");
		if (dotIndex > -1)
		{

			if (precision > 0)
			{

				int reqExtraPrecision = precision - (output.length() - dotIndex - 1);

				if (reqExtraPrecision < 0)
				{
					// Output contains extra decimals; So stripping off.
					int end = dotIndex + 1 + precision;
					output = output.substring(0, end);

				}
				else if (reqExtraPrecision > 0)
				{

					output += genZeros(reqExtraPrecision);
				}
			}
			else
			{
				output = output.substring(0, dotIndex);
			}
		} 
		else if (precision > 0)
		{
			output += "." + genZeros(precision);
		}

		return output + units;
		// return commaFormat(output) + units;
	}

	public static String chartFormat(String sNumber, int precision) 
	{

		if (sNumber == null || sNumber.equals("--") || sNumber.equals(APP_CONSTANT.IGNORE_VAL))
			return DBConstants.NULL_DATA;

		if (sNumber == null || sNumber.length() < 1 || sNumber.equals(APP_CONSTANT.NULL_DATA))
			return sNumber;

		sNumber = sNumber.replaceAll(",", "");

		sNumber = BigDecimal.valueOf(Double.parseDouble(sNumber)).toPlainString();

		String units = "";
		String output = sNumber;

		if (units.length() > 0 && precision == 0) 
		{
			precision = 2;
		}

		int dotIndex = output.indexOf(".");
		if (dotIndex > -1)
		{

			if (precision > 0) 
			{

				int reqExtraPrecision = precision - (output.length() - dotIndex - 1);

				if (reqExtraPrecision < 0)
				{
					// Output contains extra decimals; So stripping off.
					int end = dotIndex + 1 + precision;
					output = output.substring(0, end);

				} 
				else if (reqExtraPrecision > 0)
				{

					output += genZeros(reqExtraPrecision);
				}
			} 
			else
			{
				output = output.substring(0, dotIndex);
			}
		} 
		else if (precision > 0)
		{
			output += "." + genZeros(precision);
		}

		return output;
	}

	public static String genZeros(int nZeros)
	{

		String output = "";
		for (int i = 0; i < nZeros; i++)
		{
			output += "0";
		}
		return output;
	}

	public static String commaFormat(String input)
	{

		if (input.equalsIgnoreCase(APP_CONSTANT.NULL_DATA))
			return input;

		input = input.replaceAll(",", "");

		String output = "";

		char sign = input.charAt(0);
		if (sign == '-' || sign == '+')
		{
			input = input.substring(1, input.length());
			output += sign;
		}

		int _dotIndex = input.indexOf(".");

		int length = _dotIndex == -1 ? input.length() : _dotIndex;

		int firstIndex = length % 3;
		firstIndex = firstIndex == 0 ? 3 : firstIndex;

		int counter = 0;

		while (firstIndex < length)
		{
			output += input.substring(counter, firstIndex);
			output += ",";

			counter = firstIndex;
			firstIndex += 3;
		}

		if (counter == 0)
			return output + input;

		firstIndex -= 3;
		output += input.substring(firstIndex, input.length());

		return output;
	}

	public static String formatTime(Date d, String format) throws Exception 
	{

		if (d == null || format == null || format.equals(""))
			return APP_CONSTANT.NULL_DATA;

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);

	}

	public static String formatTimeInUTC(Date d, String format) throws Exception
	{

		if (d == null || format == null || format.equals(""))
			return APP_CONSTANT.NULL_DATA;

		SimpleDateFormat sdfUTC = new SimpleDateFormat(format);
		sdfUTC.setTimeZone(TimeZone.getTimeZone("GMT"));

		return sdfUTC.format(d);
	}

	public static Date formatString(String date, String format) throws Exception 
	{

		if (date == null || format == null || date.equals("") || format.equals(""))
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}

	public static String getTableName(String sExchange) 
	{
		String sTable = null;
		if ((sExchange.equalsIgnoreCase(APP_CONSTANT.NFO_EXCHANGE))
				|| (sExchange.equalsIgnoreCase(APP_CONSTANT.NSE_EXCHANGE)))
		{
			sTable = "NFO_QUOTE";
		}
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.BSE_EXCHANGE)) 
		{
			sTable = "BSE_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.NSE_CDS_EXCHANGE))
		{
			sTable = "CDS_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.CDS_EXCHANGE))
		{
			sTable = "CDS_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.MCX_EXCHANGE)) 
		{
			sTable = "MFO_QUOTE";
		}
		return sTable;
	}

	public static String getQuoteTableName(String sExchange) 
	{
		String sTable = null;
		if (sExchange.equalsIgnoreCase(APP_CONSTANT.NSE_EXCHANGE)) 
		{
			sTable = "NSE_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.BSE_EXCHANGE))
		{
			sTable = "BSE_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.NFO_EXCHANGE))
		{
			sTable = "NFO_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.NSE_CDS_EXCHANGE))
		{
			sTable = "CDS_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.CDS_EXCHANGE))
		{
			sTable = "CDS_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.MCX_SX_FO_EXCHANGE))
		{
			sTable = "MFO_QUOTE";
		}
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.MCX_EXCHANGE))
		{
			sTable = "MFO_QUOTE";
		}
		return sTable;
	}

	public static String getQuoteTableNameFromSymbol(String sSymbol) 
	{
		String sTable = null;
		sSymbol = sSymbol.toUpperCase();

		if (sSymbol.endsWith(APP_CONSTANT.NSE_EXCHANGE))
		{
			sTable = "NSE_QUOTE";
		}
		else if (sSymbol.endsWith(APP_CONSTANT.BSE_EXCHANGE))
		{
			sTable = "BSE_QUOTE";
		} 
		else if (sSymbol.endsWith(APP_CONSTANT.NFO_EXCHANGE)) 
		{
			sTable = "NFO_QUOTE";
		}
		else if (sSymbol.endsWith(APP_CONSTANT.NSE_CDS_EXCHANGE))
		{
			sTable = "CDS_QUOTE";
		}
		else if (sSymbol.endsWith(APP_CONSTANT.CDS_EXCHANGE))
		{
			sTable = "CDS_QUOTE";
		} 
		else if (sSymbol.endsWith(APP_CONSTANT.MCX_SX_FO_EXCHANGE))
		{
			sTable = "MFO_QUOTE";
		} 
		else if (sSymbol.endsWith(APP_CONSTANT.MCX_EXCHANGE))
		{
			sTable = "MFO_QUOTE";
		} 
		else
			sTable = "INDEX_QUOTE";
		return sTable;
	}

	public static String getUnderlyingTableName(String sExchange) 
	{
		String sTable = null;
		if ((sExchange.equalsIgnoreCase(APP_CONSTANT.NFO_EXCHANGE))
				|| (sExchange.equalsIgnoreCase(APP_CONSTANT.NSE_EXCHANGE)))
		{
			sTable = "NSE_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.BSE_EXCHANGE)) 
		{
			sTable = "BSE_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.NSE_CDS_EXCHANGE)) 
		{
			sTable = "CDS_QUOTE";
		} 
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.MCX_EXCHANGE)) 
		{
			sTable = "MFO_QUOTE";
		}
		return sTable;
	}

	public static void cleanPreparedStatmentAndResultSet(PreparedStatement pstmt, ResultSet res) throws SQLException 
	{
		if (pstmt != null)
			Helper.closeStatement(pstmt);
		if (res != null)
			Helper.closeResultSet(res);
	}

	public static long daysBetween(Calendar startDate, Calendar endDate)
	{
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) 
		{
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	public static String getDisplayExchange(String sExchange) 
	{
		String sDispExchange = "-";

		if (sExchange.equalsIgnoreCase(APP_CONSTANT.NSE_EXCHANGE))
			sDispExchange = APP_CONSTANT.NSE_EXCHANGE;
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.BSE_EXCHANGE))
			sDispExchange = APP_CONSTANT.BSE_EXCHANGE;
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.NFO_EXCHANGE))
			sDispExchange = APP_CONSTANT.NFO_EXCHANGE;
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.CDS_EXCHANGE))
			sDispExchange = APP_CONSTANT.NSE_CDS_EXCHANGE;
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.MCX_SX_FO_EXCHANGE))
			sDispExchange = APP_CONSTANT.MCX_EXCHANGE;
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.MCX_EXCHANGE))
			sDispExchange = APP_CONSTANT.MCX_EXCHANGE;

		return sDispExchange;

	}

	public static String getExchangeName(String symbol)
	{

		String exchangeName = "";

		symbol = symbol.toUpperCase();

		if (symbol.endsWith(APP_CONSTANT.NSE_EXCHANGE))
			exchangeName = APP_CONSTANT.NSE_EXCHANGE;
		else if (symbol.endsWith(APP_CONSTANT.BSE_EXCHANGE))
			exchangeName = APP_CONSTANT.BSE_EXCHANGE;
		else if (symbol.endsWith(APP_CONSTANT.NFO_EXCHANGE))
			exchangeName = APP_CONSTANT.NFO_EXCHANGE;
		else if (symbol.endsWith(APP_CONSTANT.CDS_EXCHANGE))
			exchangeName = APP_CONSTANT.CDS_EXCHANGE;
		else if (symbol.endsWith(APP_CONSTANT.MCX_SX_FO_EXCHANGE))
			exchangeName = APP_CONSTANT.MCX_EXCHANGE;
		else
			exchangeName = "";

		return exchangeName;
	}

	public static String getPrecision(String sExchange)
	{

		String sPrecision = "2";

		if (sExchange.equalsIgnoreCase(APP_CONSTANT.NFO_EXCHANGE))
			sPrecision = "2";
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.MCX_SX_FO_EXCHANGE))
			sPrecision = "2";
		else if (sExchange.equalsIgnoreCase(APP_CONSTANT.CDS_EXCHANGE))
			sPrecision = "4";

		return sPrecision;

	}

	public static void addAlertDetails(AlertData aData, boolean isAmend, DBPool pool, String sOrderID)
			throws Exception {

		String sqlQuery = "INSERT INTO ORDER_ALERTS(USER_ID, ORDER_ID, SYMBOL, PRICE, QUANTITY, ALERT_TYPE, ALERT_PRICE, ALERT_ID, STATUS, SIDE, SEGMENT, PRODUCT_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

		if (isAmend)
			sqlQuery = "Update ORDER_ALERTS set USER_ID = ?, ORDER_ID = ?, SYMBOL = ?, PRICE = ?, QUANTITY = ?, ALERT_TYPE = ?, ALERT_PRICE = ?, ALERT_ID = ?, STATUS = ?, SIDE = ? , PRODUCT_TYPE = ? where USER_ID = ? and ORDER_ID = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = pool.getConnection();
			pstmt = conn.prepareStatement(sqlQuery);

			pstmt.setString(1, aData.getsUserID());
			pstmt.setString(2, aData.getsOrderID());
			pstmt.setString(3, aData.getsSymbol());
			pstmt.setString(4, aData.getsPrice());
			pstmt.setString(5, aData.getsQuantity());
			pstmt.setString(6, aData.getAlertType());
			pstmt.setString(7, aData.getsAlertPrice());
			pstmt.setString(8, aData.getsAlertID());
			pstmt.setString(9, aData.getsStatus());

			if (!isAmend) {
				pstmt.setString(10, aData.getSide());
				pstmt.setString(11, aData.getSegment());
				pstmt.setString(12, aData.getProdType());
			}

			if (isAmend) {
				pstmt.setString(10, aData.getSide());
				pstmt.setString(11, aData.getProdType());
				pstmt.setString(12, aData.getsUserID());
				pstmt.setString(13, sOrderID);
			}

			//log.info("Query :: " + pstmt);
			int _result = pstmt.executeUpdate();

			/*if (_result > 0)
				//log.debug("Alert details updated successfully");
			else
				//log.debug("Error in updating alert details");*/
		} finally {
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

	}

	public static void deleteAlertDetails(String orderID, String userID, DBPool pool) throws Exception 
	{

		String sqlQuery = "delete from ORDER_ALERTS where USER_ID = ? and ORDER_ID = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try
		{
			conn = pool.getConnection();
			pstmt = conn.prepareStatement(sqlQuery);

			pstmt.setString(1, userID);
			pstmt.setString(2, orderID);

			pstmt.executeUpdate();

		} 
		finally
		{
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
	}

	public static double getLastPrice(String symbol, DBPool pool) throws Exception 
	{

		String tableName = "";
		double lastPrice = 0;
		String[] symExc = symbol.split("_");

		if (symExc[1].equals(APP_CONSTANT.NSE_EXCHANGE))
			tableName = "NSE_QUOTE";
		else if (symExc[1].equals(APP_CONSTANT.BSE_EXCHANGE))
			tableName = "BSE_QUOTE";
		else if (symExc[1].equals(APP_CONSTANT.NFO_EXCHANGE))
			tableName = "NFO_QUOTE";
		else if (symExc[1].equals(APP_CONSTANT.BFO_EXCHANGE))
			tableName = "BFO_QUOTE";
		else if (symExc[1].equals(APP_CONSTANT.CDS_EXCHANGE))
			tableName = "CDS_QUOTE";
		else if (symExc[1].equals(APP_CONSTANT.MCX_EXCHANGE)||symExc[1].equals(APP_CONSTANT.MCX_SX_FO_EXCHANGE))
			tableName = "MFO_QUOTE";
		else if (symExc[1].equals(APP_CONSTANT.BCD_EXCHANGE))
			tableName = "BCD_QUOTE";

		String query = "select LAST_PRICE from " + tableName + " where symbol = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;

		try
		{

			conn = pool.getQuoteDBConnection();
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, symbol);
			//log.debug("LTP = " + pstmt);
			res = pstmt.executeQuery();

			if (res.next())
				lastPrice = Double.parseDouble(res.getString(DBConstants.LAST_TRADED_PRICE));
		} 
		finally
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		return lastPrice;
	}

	public static String toCamelCase(String input)
	{
		String result = "";
		char firstChar = input.charAt(0);
		result = result + Character.toUpperCase(firstChar);
		for (int i = 1; i < input.length(); i++)
		{
			char currentChar = Character.toLowerCase(input.charAt(i));
			char previousChar = input.charAt(i - 1);
			if (previousChar == ' ')
			{
				result = result + Character.toUpperCase(currentChar);
			} 
			else 
			{
				result = result + currentChar;
			}
		}
		return result;
	}

	public static DecimalFormat retrieveDecimalFromat(String sExchange)
	{
		DecimalFormat dFormat;

		if ((sExchange.equalsIgnoreCase(APP_CONSTANT.CDS_EXCHANGE))
				|| (sExchange.equalsIgnoreCase(APP_CONSTANT.NSE_CDS_EXCHANGE)))
		{
			dFormat = new DecimalFormat("0.0000");
		} 
		else
		{
			dFormat = new DecimalFormat("0.00");
		}

		return dFormat;

	}

	public static String getDisplayTime(String sTime)
	{
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat to = new SimpleDateFormat("dd MMM yyyy, hh:mm:ss a");

		String pt = null;
		try
		{
			Date dd = from.parse(sTime);
			pt = to.format(dd);
		} 
		catch (Exception e)
		{

		}
		return pt;
	}

	public static String getInboxDisplayTime(Date dNow, String sTime) throws Exception 
	{
		Date dCreated = formatString(sTime, "yyyy-MM-dd HH:mm:ss");
		long diff = dNow.getTime() - dCreated.getTime();
		long sec = TimeUnit.MILLISECONDS.toSeconds(diff);
		long min = TimeUnit.MILLISECONDS.toMinutes(diff);
		long hour = TimeUnit.MILLISECONDS.toHours(diff);
		long day = TimeUnit.MILLISECONDS.toDays(diff);

		StringBuilder sb = new StringBuilder();
		if (day != 0)
		{

			if (day > 1)
				sb.append(day + " Days ago - ");
			else
				sb.append(day + " Day ago - ");

		} 
		else if (day == 0 && hour != 0) 
		{

			if (hour > 1)
				sb.append(hour + " Hours ago - ");
			else
				sb.append(hour + " Hour ago - ");

		} 
		else if (day == 0 && hour == 0 && min != 0)
		{

			if (min > 1)
				sb.append(min + " Mins ago - ");
			else
				sb.append(min + " Min ago - ");

		}
		else
		{

			if (sec > 1)
				sb.append(sec + " Secs ago - ");
			else
				sb.append(sec + " Sec ago - ");

		}

		SimpleDateFormat dispDate = new SimpleDateFormat("hh:mm:ss a");
		sb.append(dispDate.format(dCreated));
		return sb.toString();
	}

	public static String getDisplayQty(String sInputQty)
	{

		Double inputQty = Double.parseDouble(sInputQty);
		String sDisplayQty = "";
		DecimalFormat dFormat = new DecimalFormat("#.##");
		if (inputQty >= 100000 && inputQty < 10000000)
		{
			inputQty = (inputQty / 100000);
			sDisplayQty = dFormat.format(inputQty) + "L";

		} 
		else if (inputQty >= 10000000 && inputQty < 1000000000)
		{
			inputQty = (inputQty / 10000000);
			sDisplayQty = dFormat.format(inputQty) + "Cr";

		} 
		else 
		{
			inputQty = Math.floor(inputQty);
			sDisplayQty = inputQty.toString();

			sDisplayQty = sDisplayQty.substring(0, sDisplayQty.indexOf(".")) + "";
		}
		return sDisplayQty;

	}

	public static String getDisplayPrice(Double dPriceToCovert)
	{

		String sDisplayPrice = "";

		Double dPrice = Math.abs(dPriceToCovert);
		DecimalFormat dFormat = new DecimalFormat("0.##");
		if (dPrice >= 1000.00 && dPrice < 100000.00) 
		{
			dPrice = dPrice / 1000;
			sDisplayPrice = dFormat.format(dPrice).toString() + "K";
		} 
		else if (dPrice >= 100000.00 && dPrice < 10000000.00)
		{
			dPrice = dPrice / 100000;
			//log.debug("----> Price :: " + dPrice);
			sDisplayPrice = dFormat.format(dPrice).toString() + "L";
		}
		else if (dPrice >= 10000000.00 && dPrice < 1000000000.00)
		{
			dPrice = dPrice / 10000000;
			sDisplayPrice = dFormat.format(dPrice).toString() + "Cr";
		} 
		else
		{
			dPrice = Math.floor(dPrice);
			sDisplayPrice = dPrice.toString();
			sDisplayPrice = sDisplayPrice.substring(0, sDisplayPrice.indexOf(".")) + "";
		}
		if (dPriceToCovert < 0) 
		{
			sDisplayPrice = "-" + sDisplayPrice;
		}
		return sDisplayPrice;

	}

	public static String getPricewithSuffix(long count)
	{

		boolean isNeg = false;
		if (count < 0) 
		{
			count = count * -1;
			isNeg = true;
		}

		if (count < 1000)
			return "" + count;
		int exp = (int) (Math.log(count) / Math.log(1000));

		if (!isNeg)
			return String.format("%.1f %c", count / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
		else
			return "-" + String.format("%.1f %c", count / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
	}

	public static String getKLCFormatPrice(double dPriceToCovert) throws Exception 
	{

		String sDisplayPrice = "";
		DecimalFormat df = null;
		Double dPrice = Math.abs(dPriceToCovert);

		if (dPrice >= 1000.00 && dPrice < 100000.00)
		{
			dPrice = dPrice / 1000;
			sDisplayPrice = dPrice.toString();
			df = new DecimalFormat("#.##K");
			df.setRoundingMode(RoundingMode.DOWN);
			sDisplayPrice = df.format(dPrice);
		}
		else if (dPrice >= 100000.00 && dPrice < 10000000.00) 
		{
			dPrice = dPrice / 100000;
			sDisplayPrice = dPrice.toString();
			df = new DecimalFormat("#.###L");
			df.setRoundingMode(RoundingMode.DOWN);
			sDisplayPrice = df.format(dPrice);
		} 
		else if (dPrice >= 10000000.00 && dPrice < 1000000000.00)
		{
			dPrice = dPrice / 10000000;
			sDisplayPrice = dPrice.toString();
			df = new DecimalFormat("#.####Cr");
			df.setRoundingMode(RoundingMode.DOWN);
			sDisplayPrice = df.format(dPrice);
		} 
		else
		{
			dPrice = Math.floor(dPrice);
			sDisplayPrice = dPrice.toString();
			df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.DOWN);
			sDisplayPrice = df.format(dPrice);
		}
		if (dPriceToCovert < 0) 
		{
			sDisplayPrice = "-" + sDisplayPrice;
		}

		return sDisplayPrice;
	}

	public static void checkForDuplicateOrder(DBPool pool, String sSessionID, String params) throws Exception
	{

		if (SessionHelper.isDuplicateOrder(sSessionID, params, pool))
		{

			log.info("Duplicate Order logged");
			throw new SamcoException(INFO_IDS.DUPLICATE_ORDER);
		}
	}

	public static String getMISMultiplier(DBPool pool, String symbol) throws Exception
	{

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;

		String misMultiplier = "1";

		String tableName = getQuoteTableNameFromSymbol(symbol);

		String query = "select MIS_MULTIPLIER from " + tableName + " where symbol = ?";

		try {

			conn = pool.getQuoteDBConnection();
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, symbol);

			res = pstmt.executeQuery();

			if (res.next())
				misMultiplier = res.getString(DBConstants.MIS_MULTIPLIER);

		} 
		catch (Exception e)
		{

		}
		finally 
		{

			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		return misMultiplier;
	}

	public static String getISIN(DBPool pool, String symbol) throws Exception
	{

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;

		String sISIN = "-";

		String tableName = getQuoteTableNameFromSymbol(symbol);

		if ((tableName.equalsIgnoreCase("NSE_QUOTE")) || (tableName.equalsIgnoreCase("BSE_QUOTE")))
		{
			String query = "select ISIN from " + tableName + " where symbol = ?";

			try 
			{

				conn = pool.getQuoteDBConnection();
				pstmt = conn.prepareStatement(query);

				pstmt.setString(1, symbol);

				res = pstmt.executeQuery();

				if (res.next())
					sISIN = res.getString(DBConstants.ISIN);

			} 
			catch (Exception e)
			{

			}
			finally
			{

				Helper.closeResultSet(res);
				Helper.closeStatement(pstmt);
				Helper.closeConnection(conn);
			}
		}
		return sISIN;
	}

	public static JSONObject getProductAndOrderPreference(String sUid, String instrument, Connection conn)
			throws Exception
	{

		PreparedStatement ps = null;
		ResultSet res = null;

		JSONObject data = new JSONObject();

		String sType = "";

		if (instrument.contains("FUT"))
			sType = APP_CONSTANT.EQ_DERIV;
		else if (instrument.contains("OPT"))
			sType = APP_CONSTANT.EQ_OPTIONS;
		else
			sType = APP_CONSTANT.EQ_STOCK;

		String sQuery = "select product_type, order_type from samco.PRODUCT_ORDER_TYPES where user_id = ? and asset_class = ?";

		try
		{

			ps = conn.prepareStatement(sQuery);
			ps.setString(1, sUid);
			ps.setString(2, sType);
			res = ps.executeQuery();

			while (res.next())
			{
				data.put(APP_CONSTANT.FILTER_ORDER_TYPE_PREFERENCES, res.getString(DBConstants.ORDER_TYPE));
				data.put(APP_CONSTANT.FILTER_PRODUCT_TYPE_PREFERENCES, res.getString(DBConstants.PRODUCT_TYPE));
			}

		}
		finally
		{
			Helper.closeResultSet(res);
			Helper.closeStatement(ps);
		}
		return data;
	}

	public static JSONObject checkProductAndOrderPreference(String sUid, DBPool pool) throws Exception
	{

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;

		JSONObject dataObj = new JSONObject();
		String sQuery = "select asset_class, product_type, order_type from PRODUCT_ORDER_TYPES where user_id = ? ";

		try
		{
			conn = pool.getConnection();
			ps = conn.prepareStatement(sQuery);
			ps.setString(1, sUid);
			res = ps.executeQuery();

			JSONObject data = new JSONObject();
			String sOrderType = null;
			while (res.next())
			{

				String sType = res.getString(DBConstants.ASSET_CLASS);

				if (sType.equals(APP_CONSTANT.EQ_STOCK)) 
				{
					data.put(APP_CONSTANT.CASH, res.getString(DBConstants.PRODUCT_TYPE));
				} 
				else if (sType.equalsIgnoreCase(APP_CONSTANT.EQ_DERIV))
				{
					data.put(APP_CONSTANT.DERIVATIVES, res.getString(DBConstants.PRODUCT_TYPE));
				} 
				else if (sType.equalsIgnoreCase(APP_CONSTANT.EQ_OPTIONS))
				{
					data.put(APP_CONSTANT.OPTIONS, res.getString(DBConstants.PRODUCT_TYPE));
				}

				sOrderType = res.getString(DBConstants.ORDER_TYPE);
			}

			dataObj.put(APP_CONSTANT.FILTER_ORDER_TYPE_PREFERENCES, sOrderType != null ? sOrderType : "");
			dataObj.put(APP_CONSTANT.FILTER_PRODUCT_TYPE_PREFERENCES, data);
		} 
		finally
		{

			Helper.closeResultSet(res);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
		return dataObj;
	}

	public static int getLotSizeFromContracts(Connection conn, String sSymbol, String sExchange) throws Exception 
	{

		PreparedStatement pstmt = null;
		ResultSet res = null;
		String sQuery = null;
		int lts = 0;
		try
		{
			String sTableName = sExchange;
			//log.debug("---->sExchange :: " + sExchange);
			//log.debug("---->sSymbol :: " + sSymbol);

			if (sExchange.equalsIgnoreCase(APP_CONSTANT.MCX_EXCHANGE))
			{
				sTableName = "MFO";
			}
			if (sExchange.equalsIgnoreCase(APP_CONSTANT.NSE_CDS_EXCHANGE))
			{
				sTableName = "CDS";
			}
			if (sExchange.equalsIgnoreCase(APP_CONSTANT.NFO_EXCHANGE)) 
			{
				sTableName = "NFO";
			}

			sQuery = "select N.LOTSIZE as lotsize  from " + sTableName + "_QUOTE N where N.SYMBOL = ?";

			pstmt = conn.prepareStatement(sQuery);
			pstmt.setString(1, sSymbol);
			res = pstmt.executeQuery();
			while (res.next())
			{
				String sLotsize = res.getString("lotsize");
				lts = (sLotsize != null) ? Integer.valueOf(sLotsize) : 0;
			}
		} 
		finally
		{

			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);

		}
		return lts;
	}

	public static void checkForDefaultStockNoteFilter(DBPool pool, String sUserID) throws Exception 
	{
		Connection conn = null;
		CallableStatement cs = null;
		String sQuery = "{call stocknotes_addAllFilters(?, ?, ?)}";
		try
		{
			conn = pool.getConnection();
			cs = conn.prepareCall(sQuery);
			cs.setString(1, sUserID);
			cs.registerOutParameter(2, java.sql.Types.VARCHAR);
			cs.registerOutParameter(3, java.sql.Types.VARCHAR);
			cs.execute();

		} 
		finally
		{
			Helper.closeStatement(cs);
			Helper.closeConnection(conn);
		}
	}

	public static String decryptPassword(String password) throws Exception
	{

		Gibberish aesDec = new Gibberish(AppConfig.getConfigValue("login.enc_key"));

		return aesDec.decrypt(password);
	}

	public static void addStockNoteQuoteDetails(JSONObject jDetails, JSONArray jSymbols, JSONObject jObj, DBPool pool)
			throws Exception 
	{

		JSONArray jArray = new JSONArray();


		for (int i = 0; i < jArray.length(); i++) 
		{
			JSONObject jTemp = jArray.getJSONObject(i);
			if (jTemp.getString(APP_CONSTANT.SYMBOL).equals(jDetails.getString(APP_CONSTANT.SYMBOL)))
			{

				jDetails.put(APP_CONSTANT.EXCHANGE, jTemp.getString(APP_CONSTANT.EXCHANGE));
				jDetails.put(APP_CONSTANT.DISPLAY_EXPIRY_DATE, jTemp.getString(APP_CONSTANT.DISPLAY_EXPIRY_DATE));
				jDetails.put(APP_CONSTANT.INSTRUMENT, jTemp.getString(APP_CONSTANT.INSTRUMENT));
				jDetails.put(APP_CONSTANT.DISPLAY_INSTRUMENT_TYPE,
						jTemp.getString(APP_CONSTANT.DISPLAY_INSTRUMENT_TYPE));
				jDetails.put(APP_CONSTANT.OPTION_TYPE, jTemp.getString(APP_CONSTANT.OPTION_TYPE));
				jDetails.put(APP_CONSTANT.DISPLAY_STRIKE_PRICE, jTemp.getString(APP_CONSTANT.DISPLAY_STRIKE_PRICE));
				jDetails.put(APP_CONSTANT.LAST_TRADED_PRICE, jTemp.getString(APP_CONSTANT.LAST_TRADED_PRICE));
				jDetails.put(APP_CONSTANT.CHANGE, jTemp.getString(APP_CONSTANT.CHANGE));
				jDetails.put(APP_CONSTANT.CHANGE_PERCENTAGE, jTemp.getString(APP_CONSTANT.CHANGE_PERCENTAGE));
				jDetails.put(APP_CONSTANT.COMPANY_NAME, jTemp.getString(APP_CONSTANT.COMPANY_NAME));
			}
		}
		if (jObj.length() > 0) {
			Iterator<?> keys = (Iterator<?>) jObj.keys();
			Map<String, JSONArray> mMap = new HashMap<String, JSONArray>();

			while (keys.hasNext()) {

				JSONArray jResult = new JSONArray();
				String key = (String) keys.next();
				JSONArray jSym = jObj.getJSONArray(key);
				for (int i = 0; i < jSym.length(); i++) {
					String sSymbol = jSym.getString(i);
					for (int j = 0; j < jArray.length(); j++) {
						JSONObject jQdata = jArray.getJSONObject(j);
						String sQSym = jQdata.getString(APP_CONSTANT.SYMBOL);
						if (sQSym.equals(sSymbol)) {
							JSONObject jIndObj = new JSONObject();
							jIndObj.put(APP_CONSTANT.SYMBOL, sQSym);
							jIndObj.put(APP_CONSTANT.SYMBOL_NAME,
									jQdata.getString(APP_CONSTANT.TRADING_SYMBOL).replaceAll("-EQ", ""));
							jResult.put(jIndObj);
						}
					}
				}
				mMap.put(key, jResult);
			}

			String sName = jDetails.getString(APP_CONSTANT.SYMBOL);
			if (mMap.containsKey(sName))
				jDetails.put(APP_CONSTANT.SEC_CONTRACTS, mMap.get(sName));
		}
	}

	public static void addStockNoteQuoteDetails(JSONArray jNotes, JSONArray jSymbols, JSONObject jObj, DBPool pool)
			throws Exception {

		JSONArray jArray = new JSONArray();

		try {
			//--MultiQuotesRequest mqtReq = new MultiQuotesRequest(pool, jSymbols);
			//--MultiQuotesResponse mqtRes = (MultiQuotesResponse) mqtReq.postRequest();

			//--if (mqtRes != null) {
			//--jArray = mqtRes.getJSONArray(APP_CONSTANT.MULTI_QUOTES);
			//--}
		} catch (Exception e) {

		}

		for (int i = 0; i < jArray.length(); i++) {
			JSONObject jTemp = jArray.getJSONObject(i);
			String sQSymbol = jTemp.getString(APP_CONSTANT.SYMBOL);
			for (int j = 0; j < jNotes.length(); j++) {
				String sNSymbol = jNotes.getJSONObject(j).getString(APP_CONSTANT.SYMBOL);
				if (sQSymbol.equals(sNSymbol)) {
					// jNotes.getJSONObject(j).put(APP_CONSTANT.SYMBOL_NAME,
					// jTemp.getString(APP_CONSTANT.SYMBOL_NAME));
					jNotes.getJSONObject(j).put(APP_CONSTANT.EXCHANGE, jTemp.getString(APP_CONSTANT.EXCHANGE));
					jNotes.getJSONObject(j).put(APP_CONSTANT.DISPLAY_EXPIRY_DATE,
							jTemp.getString(APP_CONSTANT.DISPLAY_EXPIRY_DATE));
					jNotes.getJSONObject(j).put(APP_CONSTANT.INSTRUMENT, jTemp.getString(APP_CONSTANT.INSTRUMENT));
					jNotes.getJSONObject(j).put(APP_CONSTANT.DISPLAY_INSTRUMENT_TYPE,
							jTemp.getString(APP_CONSTANT.DISPLAY_INSTRUMENT_TYPE));
					jNotes.getJSONObject(j).put(APP_CONSTANT.OPTION_TYPE, jTemp.getString(APP_CONSTANT.OPTION_TYPE));
					jNotes.getJSONObject(j).put(APP_CONSTANT.DISPLAY_STRIKE_PRICE,
							jTemp.getString(APP_CONSTANT.DISPLAY_STRIKE_PRICE));
					jNotes.getJSONObject(j).put(APP_CONSTANT.LAST_TRADED_PRICE,
							jTemp.getString(APP_CONSTANT.LAST_TRADED_PRICE));
					jNotes.getJSONObject(j).put(APP_CONSTANT.CHANGE, jTemp.getString(APP_CONSTANT.CHANGE));
					jNotes.getJSONObject(j).put(APP_CONSTANT.CHANGE_PERCENTAGE,
							jTemp.getString(APP_CONSTANT.CHANGE_PERCENTAGE));
					if (jTemp.getString(APP_CONSTANT.COMPANY_NAME).equals("-")) {
						jNotes.getJSONObject(j).put(APP_CONSTANT.COMPANY_NAME,
								jNotes.getJSONObject(j).get(APP_CONSTANT.SYMBOL_NAME));
					} else {
						jNotes.getJSONObject(j).put(APP_CONSTANT.COMPANY_NAME,
								jTemp.getString(APP_CONSTANT.COMPANY_NAME));

					}
					if (jTemp.has(APP_CONSTANT.ISIN)) {
						jNotes.getJSONObject(j).put(APP_CONSTANT.ISIN, jTemp.getString(APP_CONSTANT.ISIN));
					} else {
						jNotes.getJSONObject(j).put(APP_CONSTANT.ISIN, "-");
					}
				}
			}
		}

		if (jObj.length() != 0) {
			Iterator<?> keys = (Iterator<?>) jObj.keys();
			Map<String, JSONArray> mMap = new HashMap<String, JSONArray>();

			while (keys.hasNext()) {

				JSONArray jResult = new JSONArray();
				String key = (String) keys.next();
				JSONArray jSym = jObj.getJSONArray(key);
				for (int i = 0; i < jSym.length(); i++) {
					String sSymbol = jSym.getString(i);
					for (int j = 0; j < jArray.length(); j++) {
						JSONObject jQdata = jArray.getJSONObject(j);
						String sQSym = jQdata.getString(APP_CONSTANT.SYMBOL);
						if (sQSym.equals(sSymbol)) {
							JSONObject jIndObj = new JSONObject();
							jIndObj.put(APP_CONSTANT.SYMBOL, sQSym);
							jIndObj.put(APP_CONSTANT.SYMBOL_NAME,
									jQdata.getString(APP_CONSTANT.TRADING_SYMBOL).replaceAll("-EQ", ""));
							if (jQdata.getString(APP_CONSTANT.COMPANY_NAME).equals("-")) {
								jIndObj.put(APP_CONSTANT.COMPANY_NAME,
										jQdata.getString(APP_CONSTANT.SYMBOL_NAME));
							} else {
								jIndObj.put(APP_CONSTANT.COMPANY_NAME,
										jQdata.getString(APP_CONSTANT.COMPANY_NAME));

							}
							jResult.put(jIndObj);
						}
					}
				}
				mMap.put(key, jResult);
			}

			for (int i = 0; i < jNotes.length(); i++) {
				JSONObject jNews = jNotes.getJSONObject(i);
				String sName = jNews.getString(APP_CONSTANT.NEWS_ID);
				if (mMap.containsKey(sName)) {
					jNotes.getJSONObject(i).put(APP_CONSTANT.SEC_CONTRACTS, mMap.get(sName));
				}
			}
		}
	}

	public static void addStockNoteCompanyNameDetails(JSONArray jNotes, JSONArray jSymbols, JSONObject jObj,
			DBPool pool) throws Exception {

		JSONArray jArray = new JSONArray();

		try {
			//--			MultiQuotesRequest mqtReq = new MultiQuotesRequest(pool, jSymbols);
			//--			MultiQuotesResponse mqtRes = (MultiQuotesResponse) mqtReq.postRequest();
			//--
			//--			if (mqtRes != null) {
			//--				jArray = mqtRes.getJSONArray(APP_CONSTANT.MULTI_QUOTES);
			//--			}
		} catch (Exception e) {

		}

		for (int i = 0; i < jArray.length(); i++) {
			JSONObject jTemp = jArray.getJSONObject(i);
			String sQSymbol = jTemp.getString(APP_CONSTANT.SYMBOL);
			for (int j = 0; j < jNotes.length(); j++) {
				String sNSymbol = jNotes.getJSONObject(j).getString(APP_CONSTANT.SYMBOL);
				if (!sNSymbol.equalsIgnoreCase("ipo")) {
					String sSymbolList[] = sNSymbol.split("_");
				}
				if (sQSymbol.equals(sNSymbol)) {
					jNotes.getJSONObject(j).put(APP_CONSTANT.COMPANY_NAME, jTemp.getString(APP_CONSTANT.COMPANY_NAME));
					jNotes.getJSONObject(j).put(APP_CONSTANT.SYMBOL_NAME, jTemp.getString(APP_CONSTANT.SYMBOL_NAME));
				}
			}
		}

	}

	public static int getFilterCount(DBPool pool, String sUserID) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;

		String query = "select count(*) as number from STOCKNOTES_FILTER_MASTER where id in "
				+ "(select fid from STOCKNOTES_USER_FILTERS where uid = ?) and is_console_enabled=1 and is_user_enabled = 1";

		//log.debug("Query :: " + query);

		int count = 0;

		try {

			conn = pool.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, sUserID);
			//log.equals("pstmt ... " + pstmt);

			res = pstmt.executeQuery();

			while (res.next())
				count = res.getInt(DBConstants.NUMBER);

		} catch (Exception e) {
			log.error("Error in getting count ", e);
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}

		return count;
	}

	public static JSONArray getAssociatedSymbolDetails(JSONArray sArray, DBPool pool) throws JSONException {

		JSONArray nseArray = new JSONArray();
		JSONArray bseArray = new JSONArray();

		JSONArray finalArray = new JSONArray();

		for (int i = 0; i < sArray.length(); i++) {

			String symbol = sArray.getString(i);

			if (symbol.endsWith(APP_CONSTANT.NSE_EXCHANGE))
				nseArray.put(symbol);
			else if (symbol.endsWith(APP_CONSTANT.BSE_EXCHANGE))
				bseArray.put(symbol);
			else {

				JSONObject obj = new JSONObject();

				obj.put(APP_CONSTANT.SYMBOL, symbol);
				obj.put(APP_CONSTANT.NSE_RELATED_BSE_SYMBOL, new JSONArray());

				finalArray.put(obj);
			}
		}

		StringBuffer sb = new StringBuffer();
		StringBuffer qBuilder = new StringBuffer();

		if (nseArray.length() > 0) {

			qBuilder.append(
					"select N.SYMBOL as UNDSYMBOL, B.SYMBOL as UND_REL_SYMBOL, B.SYMBOL_NAME as UND_REL_SYM_NAME "
							+ " from NSE_QUOTE N left join BSE_QUOTE B on N.ISIN = B.ISIN where N.SYMBOL in " + "(");

			for (int i = 0; i < nseArray.length(); i++) {
				sb.append("?,");
			}

			String queryMid1 = sb.toString().substring(0, sb.toString().length() - 1);

			qBuilder.append(queryMid1 + ")");

			String finalQuery = qBuilder.toString();

			getRelativeArray(finalQuery, nseArray, pool, finalArray);
		}

		qBuilder = new StringBuffer();
		sb = new StringBuffer();

		if (bseArray.length() > 0) {

			qBuilder.append(
					"select N.SYMBOL as UNDSYMBOL, B.SYMBOL as UND_REL_SYMBOL, B.SYMBOL_NAME as UND_REL_SYM_NAME "
							+ " from BSE_QUOTE N left join NSE_QUOTE B on N.ISIN = B.ISIN where N.SYMBOL in " + "(");

			for (int i = 0; i < bseArray.length(); i++) {
				sb.append("?,");
			}

			String queryMid1 = sb.toString().substring(0, sb.toString().length() - 1);

			qBuilder.append(queryMid1 + ")");

			String finalQuery = qBuilder.toString();

			getRelativeArray(finalQuery, bseArray, pool, finalArray);
		}

		return finalArray;
	}

	private static void getRelativeArray(String query, JSONArray inputSymArray, DBPool pool, JSONArray resArray) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;

		try {
			conn = pool.getQuoteDBConnection();
			pstmt = conn.prepareStatement(query);

			int index = 1;

			for (int i = 0; i < inputSymArray.length(); i++)
				pstmt.setString(index++, inputSymArray.getString(i));

			res = pstmt.executeQuery();

			while (res.next()) {

				String symbol = res.getString("UNDSYMBOL");
				String relSymbol = res.getString("UND_REL_SYMBOL");
				String relSymName = res.getString("UND_REL_SYM_NAME");

				if (relSymbol == null || relSymName == null) {

					JSONObject obj = new JSONObject();

					obj.put(APP_CONSTANT.SYMBOL, symbol);
					obj.put(APP_CONSTANT.NSE_RELATED_BSE_SYMBOL, new JSONArray());

					resArray.put(obj);
					continue;
				}

				JSONObject obj = new JSONObject();
				JSONObject assocObj = new JSONObject();

				obj.put(APP_CONSTANT.SYMBOL, symbol);

				assocObj.put(APP_CONSTANT.SYMBOL, relSymbol);
				assocObj.put(APP_CONSTANT.SYMBOL_NAME, relSymName);
				assocObj.put(APP_CONSTANT.EXCHANGE, relSymbol.split("_")[1]);

				obj.put(APP_CONSTANT.NSE_RELATED_BSE_SYMBOL, new JSONArray().put(assocObj));

				resArray.put(obj);
			}

		} catch (Exception e) {
			log.error("Error in fetching associated symbols :: ", e);
		} finally {
			Helper.closeResultSet(res);
			Helper.closeStatement(pstmt);
			Helper.closeConnection(conn);
		}
	}

	public static String generateSamcoToken(DBPool pool, String sUserID, String sAppID) throws Exception 
	{
		String sTokenURL = AppConfig.getConfigValue("samco_api_url");
		String sTokenAPIValue = AppConfig.getConfigValue("token.api");
		String sMerchantID = AppConfig.getConfigValue("merchant_id");
		String sMerchantPassword = AppConfig.getConfigValue("merchant_password");
		String sTokenFields = APIConstants.SAMCO_API + "=" + sTokenAPIValue + "&" + APIConstants.SAMCO_CLIENTID + "="
				+ sUserID + "&" + APIConstants.SAMCO_MERCHANT_ID + "=" + sMerchantID + "&"
				+ APIConstants.SAMCO_MERCHANT_PASSWORD + "=" + sMerchantPassword;

		TokenGeneration token = new TokenGeneration();
		token.setTokenGenerationURL(sTokenURL, sTokenFields);
		token.process();
		String sSamcoGeneratedToken = token.getToken();
		//log.debug("---> Samco Generated token :: " + sSamcoGeneratedToken);
		//		SessionHelper.updateSamcoTokenToDB(pool, sSamcoGeneratedToken, sAppID);
		SessionHelper.updateSamcoTokenToDB(pool, sSamcoGeneratedToken, sUserID);
		return sSamcoGeneratedToken;

	}
	
	 public static String titleCaseConversion(String inputString)
	    {
		 String result = "";
	        char firstChar = inputString.charAt(0);
	        result = result + Character.toUpperCase(firstChar);
	        for (int i = 1; i < inputString.length(); i++) {
	            char currentChar = inputString.charAt(i);
	            char previousChar = inputString.charAt(i - 1);
	            if (previousChar == ' ') {
	                result = result + Character.toUpperCase(currentChar);
	            } else {
	                result = result + currentChar;
	            }
	        }
	        return result;
	    }
}