package com.msf.libsb.constants;

public class QueryConstants {
	public static final String INSERT_BASKET_DETAILS = "INSERT INTO BASKET_DETAILS (BASKET_NAME, BASKET_VERSION,"
			+ "INVEST_TYPE, INITIAL_FEE, REBALANCE_FEE, MONITOR_FEE, IS_LATEST,TRENDING_FLAG, RECOMMENDED_FLAG,FEATURED_FLAG,"
			+ "RISK_FLAG, STRENGTH,VALUE_FOR_MONEY,CATEGORY,SUB_CATEGORY,DESCRIPTION,RATIONALE,DISCLAIMER,INDEX_VALUE,"
			+ "BENCHMARK,IMAGE_URL,ICON_URL,ONE_MONTH_RET,THREE_MONTH_RET,SIX_MONTH_RET,ONE_YEAR_RET,"
			+ "THREE_YEAR_RET,FIVE_YEAR_RET)" + " VALUES(?,?,?,?,?,?,1,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // 27
																													// ?
																													// marks

	public static final String MY_INVESTMENT_MULTIPLE_QUERY = "select bo.CREATED_AT, boc.AVG_PRICE, boc.BASKET_ORDER_NO as BASKET_ORDER_NO, bo.REMAINING_QTY as BASKET_QTY, boc.SYMBOL as SYMBOL, boc.TRANS_TYPE as TRANS_TYPE, boc.PL_VALUE,boc.PL_PERCENT,bse.ISIN,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION, ((boc.FILLED_QTY)-((bo.QTY-bo.REMAINING_QTY) * boc.ORIGINAL_QTY/bo.QTY)) as ORIGINAL_QTY,"
			+ "coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE, bo.ORDER_NO as ORDER_NO from BASKET_ORDERS bo"
			+ " inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
			+ " left join quote_data.NSE_QUOTE nse on  boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on  boc.SYMBOL=bse.SYMBOL" + " where bo.ORDER_NO IN (";

	public static final String INSERT_BASKET_DETAILS2 = "INSERT INTO BASKET_DETAILS (BASKET_NAME, BASKET_VERSION,IS_LATEST,"
			+ "INVEST_TYPE, INITIAL_FEE, REBALANCE_FEE, MONITOR_FEE, TRENDING_FLAG, RECOMMENDED_FLAG,FEATURED_FLAG,"
			+ "RISK_FLAG, STRENGTH,VALUE_FOR_MONEY,CATEGORY,SUB_CATEGORY,DESCRIPTION,RATIONALE,DISCLAIMER,INDEX_VALUE,"
			+ "BENCHMARK,IMAGE_URL,ICON_URL,POPULARITY,ONE_MONTH_RET,THREE_MONTH_RET,SIX_MONTH_RET,ONE_YEAR_RET,THREE_YEAR_RET,"
			+ "FIVE_YEAR_RET) "
			+ "SELECT ?,?,1,INVEST_TYPE,INITIAL_FEE,REBALANCE_FEE,MONITOR_FEE,TRENDING_FLAG,RECOMMENDED_FLAG,FEATURED_FLAG,"
			+ "RISK_FLAG,STRENGTH,VALUE_FOR_MONEY,CATEGORY,SUB_CATEGORY,DESCRIPTION,RATIONALE,DISCLAIMER,INDEX_VALUE,"
			+ "BENCHMARK,IMAGE_URL,ICON_URL,POPULARITY,ONE_MONTH_RET,THREE_MONTH_RET,SIX_MONTH_RET,ONE_YEAR_RET,THREE_YEAR_RET,"
			+ "FIVE_YEAR_RET from BASKET_DETAILS where BASKET_NAME=? and BASKET_VERSION=?";

	public static final String INSERT_BASKET_CONTITUENTS = "INSERT INTO BASKET_CONSTITUENTS (BASKET_NAME,"
			+ "BASKET_VERSION,IS_LATEST, ISIN, EXCHANGE, RATING, QTY,SYMBOL) VALUES(?,?,1,?,?,?,?,?)";

	public static final String FETCH_LATEST_INDEX_VALUE = "select INDEX_VALUE from INDEX_HISTORY where BASKET_NAME=? order by DATE desc limit 2";

	public static final String UPDATE_BASKET_DETAILS_UPDATEOLDVERSION = "update BASKET_DETAILS set IS_LATEST =0 where BASKET_NAME = ? and IS_LATEST =1";

	public static final String UPDATE_BASKET_CONSTITUENTS_UPDATEOLDVERSION = "update BASKET_CONSTITUENTS set IS_LATEST =0 where BASKET_NAME = ? and IS_LATEST =1";

	public static final String INSERT_USER_WATCHLIST_BASKET = "INSERT INTO USER_WATCHLIST_BASKET (USER_ID,BASKET_NAME) VALUES (?,?) on duplicate key update UPDATED_AT=now()";

	public static final String DELETE_USER_WATCHLIST_BASKET = "DELETE FROM USER_WATCHLIST_BASKET WHERE USER_ID = ? and BASKET_NAME =?";

	public static final String SELECT_BASKET_NAME_USER_WATCHLIST = "SELECT BASKET_NAME FROM USER_WATCHLIST_BASKET WHERE USER_ID = ?";

	public static final String SELECT_MUL_BASKET_CONSTITUENTS = "select ISIN,QTY from BASKET_CONSTITUENTS where BASKET_NAME=? and IS_LATEST=1";

	public static final String INSERT_BASKET_ORDERS = "insert into BASKET_ORDERS(ORDER_NO,USER_ID,BASKET_NAME,BASKET_VERSION,QTY,REMAINING_QTY,ORDER_TYPE,PARENT_ORDER_NO) values(?,?,?,?,?,?,?,?)";

	public static final String SELECT_ORDER_NO_BASKET_ORDERS = "select ORDER_NO from BASKET_ORDERS where USER_ID = ? order by CREATED_AT desc limit 1";

	public static final String SELECT_BASKET_VERSION_BASKET_DETAILS = "select BASKET_VERSION from BASKET_DETAILS where BASKET_NAME= ? and IS_LATEST=1";

	public static final String SELECT_EXCHANGE_LATEST_BASKET_DETAILS = "select distinct Exchange from BASKET_CONSTITUENTS where BASKET_NAME= ? and IS_LATEST=1";

	public static final String DELETE_BASKET_DETAILS = "DELETE FROM BASKET_DETAILS WHERE BASKET_NAME=? and BASKET_VERSION=?";

	public static final String FETCH_BASKET_QUERY = "select SQL_CALC_FOUND_ROWS bd.BASKET_NAME,w.USER_ID is not null as WL_FLAG,bd.DESCRIPTION,bd.POPULARITY,"
			+ "bd.IMAGE_URL,bd.STRENGTH, bd.VALUE_FOR_MONEY,bd.INDEX_VALUE,"
			+ "sum(bc.QTY*coalesce(nse.LAST_PRICE,bse.LAST_PRICE)) as NET_PRICE,bd.ONE_MONTH_RET,bd.THREE_MONTH_RET,"
			+ "bd.SIX_MONTH_RET,bd.ONE_YEAR_RET,bd.THREE_YEAR_RET,bd.FIVE_YEAR_RET from BASKET_DETAILS bd"
			+ " inner join BASKET_CONSTITUENTS bc on bd.BASKET_NAME=bc.BASKET_NAME and bd.BASKET_VERSION=bc.BASKET_VERSION"
			+ " left join USER_WATCHLIST_BASKET w on (bd.BASKET_NAME=w.BASKET_NAME and w.USER_ID=?)"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bd.IS_LATEST=1";

	public static final String SEARCH_BASKET_QUERY = "select bd.BASKET_NAME,w.USER_ID is not null as WL_FLAG,bd.CATEGORY, bd.DESCRIPTION,bd.POPULARITY,bd.IMAGE_URL,"
			+ "MAX(nse_bse.flag) as flag, bd.STRENGTH, bd.VALUE_FOR_MONEY, bd.INDEX_VALUE,"
			+ "SUM(nse_bse.PRICE) as NET_PRICE,bd.ONE_MONTH_RET,bd.THREE_MONTH_RET,bd.SIX_MONTH_RET,bd.ONE_YEAR_RET,"
			+ "bd.THREE_YEAR_RET,bd.FIVE_YEAR_RET from BASKET_DETAILS bd"
			+ " left join USER_WATCHLIST_BASKET w on (bd.BASKET_NAME=w.BASKET_NAME and w.USER_ID=?)"
			+ " left join (select bc.BASKET_NAME, coalesce(nse.SYMBOL_NAME,bse.SYMBOL_NAME) like ? as flag,"
			+ "bc.QTY*coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as PRICE from BASKET_CONSTITUENTS bc"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL where bc.IS_LATEST=1 ) nse_bse on bd.BASKET_NAME=nse_bse.BASKET_NAME"
			+ " where bd.IS_LATEST=1";

	public static final String SELECT_FLAG_BASKET_DETAILS = "select (SELECT FEATURED_FLAG FROM BASKET_DETAILS order by FEATURED_FLAG desc limit 1) as FEATURED, (SELECT RECOMMENDED_FLAG FROM BASKET_DETAILS order by RECOMMENDED_FLAG desc limit 1) as RECOMMENDED";

	public static final String SELECT_WATCHLIST_DETAILS = "select bc.BASKET_NAME,bd.DESCRIPTION,bd.POPULARITY,bd.IMAGE_URL,bd.STRENGTH, bd.VALUE_FOR_MONEY,"
			+ "bd.INDEX_VALUE,sum(bc.QTY*coalesce(nse.LAST_PRICE,bse.LAST_PRICE)) as NET_PRICE,bd.ONE_MONTH_RET,"
			+ "bd.THREE_MONTH_RET,bd.SIX_MONTH_RET,bd.ONE_YEAR_RET,bd.THREE_YEAR_RET,bd.FIVE_YEAR_RET from BASKET_DETAILS bd"
			+ " inner join USER_WATCHLIST_BASKET w on bd.BASKET_NAME=w.BASKET_NAME"
			+ " left join BASKET_CONSTITUENTS bc on bd.BASKET_NAME=bc.BASKET_NAME and bd.BASKET_VERSION=bc.BASKET_VERSION"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where w.USER_ID=? and bd.IS_LATEST=1 group by w.BASKET_NAME order by w.UPDATED_AT desc";

	public static final String PLACE_BASKET_ORDER_QUERY = "select bd.ICON_URL,bd.BASKET_VERSION,bc.QTY,coalesce(nse.SYMBOL,bse.SYMBOL) as SYMBOL,"
			+ "coalesce(nse.TRADING_SYMBOL,bse.TRADING_SYMBOL) as TRADING_SYMBOL,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION,"
			+ "coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE from BASKET_DETAILS bd"
			+ " left join BASKET_CONSTITUENTS bc on bd.BASKET_NAME=bc.BASKET_NAME and bd.BASKET_VERSION=bc.BASKET_VERSION "
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bd.BASKET_NAME=? and bd.IS_LATEST=1";

	public static final String FIX_ORDER_QUERY = "select bo.BASKET_NAME,boc.NEST_ORDER_NO,boc.SYMBOL,boc.TRANS_TYPE,"
			+ "coalesce(nse.TRADING_SYMBOL,bse.TRADING_SYMBOL) as TRADING_SYMBOL,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION,"
			+ "coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE,"
			+ "boc.ORIGINAL_QTY-sum(boc.FILLED_QTY) as QTY from BASKET_ORDERS bo"
			+ " inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
			+ " left join quote_data.NSE_QUOTE nse on boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on boc.SYMBOL=bse.SYMBOL"
			+ " where bo.ORDER_NO=? and boc.NEST_ORDER_STATUS in ('rejected','cancelled','open','pending','complete') group by boc.SYMBOL";

	public static final String REBALANCE_QUERY = "select bc.BASKET_VERSION,bc.IS_LATEST,bc.SYMBOL,"
			+ "bo.REMAINING_QTY as BASKET_QTY,bc.QTY,coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE,"
			+ "coalesce(nse.TRADING_SYMBOL,bse.TRADING_SYMBOL) as TRADING_SYMBOL,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION,bc.EXCHANGE from BASKET_CONSTITUENTS bc"
			+ " left join BASKET_ORDERS bo on bo.BASKET_NAME=bc.BASKET_NAME and (bo.BASKET_VERSION=bc.BASKET_VERSION or bc.IS_LATEST=1)"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bc.BASKET_NAME=? and bo.ORDER_NO in";

	public static final String EXIT_BASKET_QUERY = "select bo.BASKET_NAME,bo.BASKET_VERSION,boc.SYMBOL,boc.TRANS_TYPE,"
			+ "coalesce(nse.TRADING_SYMBOL,bse.TRADING_SYMBOL) as TRADING_SYMBOL,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION,"
			+ "coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE,sum(boc.FILLED_QTY) as QTY from BASKET_ORDERS bo"
			+ " inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
			+ " left join quote_data.NSE_QUOTE nse on boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on boc.SYMBOL=bse.SYMBOL"
			+ " where bo.ORDER_NO = ? group by boc.SYMBOL";

	public static final String EXIT_BASKET_MUTILPLE_QUERY = "select bo.BASKET_NAME,bo.BASKET_VERSION,boc.SYMBOL,boc.TRANS_TYPE,"
			+ "coalesce(nse.TRADING_SYMBOL,bse.TRADING_SYMBOL) as TRADING_SYMBOL,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION,"
			+ "coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE,boc.FILLED_QTY as QTY from BASKET_ORDERS bo"
			+ " inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
			+ " left join quote_data.NSE_QUOTE nse on boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on boc.SYMBOL=bse.SYMBOL" + " where bo.ORDER_NO IN (";

	public static final String AMO_ORDER_QUERY = "select bd.ICON_URL,bd.BASKET_VERSION,bc.QTY,coalesce(nse.SYMBOL,bse.SYMBOL) as SYMBOL,"
			+ "coalesce(nse.TRADING_SYMBOL,bse.TRADING_SYMBOL) as TRADING_SYMBOL,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION,"
			+ "coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE from BASKET_DETAILS bd"
			+ " left join BASKET_CONSTITUENTS bc on bd.BASKET_NAME=bc.BASKET_NAME and bd.BASKET_VERSION=bc.BASKET_VERSION"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bd.BASKET_NAME=? and bd.IS_LATEST=1";

	public static final String ORDER_CONSTITUENTS_INSERT_QUERY = "INSERT INTO BASKET_ORDER_CONSTITUENTS (NEST_ORDER_NO,"
			+ "BASKET_ORDER_NO,SYMBOL,TRANS_TYPE,PRODUCT_CODE,ORIGINAL_QTY,FILLED_QTY,PRICE) VALUES";

	public static final String FIX_ORDER_CONSTITUENTS_INSERT_QUERY = "INSERT INTO BASKET_ORDER_CONSTITUENTS (NEST_ORDER_NO,"
			+ "BASKET_ORDER_NO,SYMBOL,TRANS_TYPE,PRODUCT_CODE,ORIGINAL_QTY,FILLED_QTY,OLD_NEST_ORDER_NO) VALUES (?,?,?,?,?,?,?,?)";

	public static final String AMO_ORDER_INSERT_QUERY = "INSERT INTO AMO (USER_ID,BASKET_ORDER_NO,SYMBOL,"
			+ "TRADING_SYMBOL, SCRIP, EXCHANGE,QTY) VALUES";

	public static final String FIX_ALL_ORDER_QUERY = "select bo.ORDER_NO,boc.NEST_ORDER_NO,boc.SYMBOL,"
			+ "coalesce(nse.TRADING_SYMBOL,bse.TRADING_SYMBOL) as TRADING_SYMBOL,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION,"
			+ "boc.TRANS_TYPE,coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE,"
			+ "boc.ORIGINAL_QTY-boc.FILLED_QTY as QTY from BASKET_ORDERS bo inner join BASKET_ORDER_CONSTITUENTS boc"
			+ " on bo.ORDER_NO=boc.BASKET_ORDER_NO" + " left join quote_data.NSE_QUOTE nse on boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on boc.SYMBOL=bse.SYMBOL"
			+ " where bo.USER_ID=? and boc.NEST_ORDER_STATUS in ('rejected','cancelled','open','pending')";

	public static final String MODIFY_ORDER = "update BASKET_ORDER_CONSTITUENTS set NEST_ORDER_NO=?,"
			+ "FILLED_QTY=(FILLED_QTY+?),PRICE=? where NEST_ORDER_NO=?";

	public static final String MODIFY_FIX_ORDER = "update BASKET_ORDER_CONSTITUENTS set NEST_ORDER_STATUS=? "
			+ "where NEST_ORDER_NO=?";

	public static final String MODIFY_BASKET_ORDER = "update BASKET_ORDERS set STATUS=? " + "where ORDER_NO=?";

	public static final String MODIFY_REBALANCE_PARENT_ORDER = "update BASKET_ORDERS set IS_REBALANCE_DONE=1 "
			+ "where ORDER_NO=?";

	public static final String MODIFY_REBALANCE_PARENT_INCOMPLETE_ORDER = "update BASKET_ORDERS set IS_REBALANCE_DONE=2 "
			+ "where ORDER_NO=?";

	public static final String QUANITY_PARENT_ORDER = "select QTY FROM BASKET_ORDERS " + "where ORDER_NO=?";

	public static final String DELETE_ORDER_CONS = "DELETE FROM BASKET_ORDER_CONSTITUENTS " + "where NEST_ORDER_NO=?";

	/*
	 * public static final String GET_FIX_REBALANCE_COUNT_QUERY =
	 * "select (select count(distinct bo.ORDER_NO)" +
	 * " from BASKET_ORDERS bo inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
	 * +
	 * " where bo.USER_ID=? and bo.STATUS != 'Rejected' and bo.STATUS != 'Cancelled' and boc.NEST_ORDER_STATUS in ('rejected','cancelled','open','pending')) as FIX_COUNT, "
	 * + "(select count(bo.ORDER_NO)  from BASKET_ORDERS bo " +
	 * "inner join BASKET_DETAILS bd on bo.BASKET_NAME=bd.BASKET_NAME and bo.BASKET_VERSION=bd.BASKET_VERSION "
	 * +
	 * "where bo.USER_ID=? and bd.IS_LATEST=0 and bo.STATUS='complete' and bo.IS_REBALANCE_DONE = '0' AND bo.ORDER_TYPE = 'BUY' and bo.IS_SOLD = '0') as REBALANCE_COUNT"
	 * ;
	 */

	public static final String GET_FIX_REBALANCE_COUNT_QUERY = "select (select count(distinct bo.ORDER_NO)"
			+ " from BASKET_ORDERS bo inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
			+ " where bo.USER_ID=? and bo.STATUS != 'Complete' and boc.NEST_ORDER_STATUS in ('rejected','cancelled','open','pending')) as FIX_COUNT";

	public static final String GET_REBALANCE_COUNT_QUERY = "select count(*) from BASKET_ORDERS bo inner join BASKET_DETAILS bd on bo.BASKET_NAME=bd.BASKET_NAME and bo.BASKET_VERSION=bd.BASKET_VERSION where bo.USER_ID=? and bd.IS_LATEST=0 and bo.STATUS='complete' and bo.IS_REBALANCE_DONE = '0' AND bo.ORDER_TYPE = 'BUY' and bo.IS_SOLD = '0' group by bo.BASKET_NAME, bo.BASKET_VERSION";

	public static final String GET_FIX_ORDERS_QUERY = "select bo.BASKET_NAME,bo.CREATED_AT,bo.ORDER_NO,bd.ICON_URL,"
			+ "((boc.ORIGINAL_QTY-sum(boc.FILLED_QTY))*coalesce(nse.LAST_PRICE,bse.LAST_PRICE)) as PRICE from BASKET_ORDERS bo"
			+ " inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
			+ " left join BASKET_DETAILS bd on bd.BASKET_NAME=bo.BASKET_NAME and bd.BASKET_VERSION=bo.BASKET_VERSION"
			+ " left join quote_data.NSE_QUOTE nse on boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on boc.SYMBOL=bse.SYMBOL"
			+ " where bo.USER_ID=? and bo.STATUS != 'complete' and boc.NEST_ORDER_STATUS in ('rejected','cancelled','open','pending') group by bo.ORDER_NO ORDER BY CREATED_AT DESC";

	public static final String GET_REBALANCE_ORDERS_QUERY = "select bd.BASKET_NAME,bd.BASKET_VERSION,tmp.ORDER_NO,"
			+ "bd.IS_LATEST,bd.CREATED_AT,bd.ICON_URL,sum(tmp.QTY*bc.QTY*coalesce(nse.LAST_PRICE,bse.LAST_PRICE)) as PRICE from"
			+ " (select bd.BASKET_NAME, bd.BASKET_VERSION, bo.ORDER_NO, bo.REMAINING_QTY as QTY from BASKET_DETAILS bd"
			+ " inner join BASKET_ORDERS bo on bd.BASKET_NAME=bo.BASKET_NAME and bd.BASKET_VERSION=bo.BASKET_VERSION"
			+ " where bo.USER_ID=? and bo.STATUS='complete' and bd.IS_LATEST=0 and bo.IS_REBALANCE_DONE = '0' AND bo.ORDER_TYPE = 'BUY' and bo.IS_SOLD = '0') tmp left join BASKET_DETAILS bd"
			+ " on bd.BASKET_NAME=tmp.BASKET_NAME and (bd.BASKET_VERSION=tmp.BASKET_VERSION or bd.IS_LATEST=1)"
			+ " left join BASKET_CONSTITUENTS bc on bd.BASKET_NAME=bc.BASKET_NAME and bd.BASKET_VERSION=bc.BASKET_VERSION"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " group by tmp.ORDER_NO,bd.IS_LATEST";

	public static final String VIEW_FIX_ORDER_QUERY = "select coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION,"
			+ "boc.ORIGINAL_QTY-sum(boc.FILLED_QTY) as QTY,"
			+ "coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE, boc.TRANS_TYPE as TRANS_TYPE, boc.ORIGINAL_QTY as ORIGINAL_QTY"
			+ " from BASKET_ORDER_CONSTITUENTS boc" + " left join quote_data.NSE_QUOTE nse on boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on boc.SYMBOL=bse.SYMBOL"
			+ " where boc.BASKET_ORDER_NO=? and boc.NEST_ORDER_STATUS in ('rejected','cancelled','open','pending','complete') group by boc.SYMBOL";

	public static final String VIEW_REBALANCE_ORDER_QUERY = "select bc.IS_LATEST,bo.REMAINING_QTY*bc.QTY as QTY,bc.SYMBOL,"
			+ "bc.BASKET_VERSION,coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION from BASKET_CONSTITUENTS bc"
			+ " left join BASKET_ORDERS bo on bc.BASKET_NAME=bo.BASKET_NAME and (bc.BASKET_VERSION=bo.BASKET_VERSION or bc.IS_LATEST=1)"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bc.BASKET_NAME=? and bo.ORDER_NO in";

	public static final String ORDER_HISTORY_QUERY = "select distinct bo.BASKET_NAME,bd.IMAGE_URL from BASKET_ORDERS bo"
			+ " left join BASKET_DETAILS bd on bo.BASKET_NAME=bd.BASKET_NAME and bo.BASKET_VERSION=bd.BASKET_VERSION"
			+ " where USER_ID=? order by bo.CREATED_AT desc";

	public static final String ORDER_HISTORY_DETAILS_QUERY = "select bo.ORDER_TYPE,boc.AVG_PRICE,boc.FILLED_QTY,"
			+ "boc.ORIGINAL_QTY,bo.STATUS, bo.CREATED_AT,bo.ORDER_NO,boc.TRANS_TYPE,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION from BASKET_ORDER_CONSTITUENTS boc"
			+ " inner join BASKET_ORDERS bo on boc.BASKET_ORDER_NO=bo.ORDER_NO"
			+ " left join quote_data.NSE_QUOTE nse on boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on boc.SYMBOL=bse.SYMBOL"
			+ " where bo.USER_ID=? and bo.BASKET_NAME=? order by bo.CREATED_AT desc";

	public static final String GET_STOCKS_QUERY = "select bc.RATING,"
			+ "bc.QTY,coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION from BASKET_CONSTITUENTS bc"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bc.BASKET_NAME=? and bc.IS_LATEST=1";

	public static final String QUOTE_OVERVIEW_QUERY = "select bd.RATIONALE,bd.RECOMMENDED_FLAG,bd.FEATURED_FLAG,"
			+ "bd.DESCRIPTION,w.USER_ID is not null as WL_FLAG,bd.POPULARITY,bd.IMAGE_URL,bd.STRENGTH, bd.VALUE_FOR_MONEY,bd.INDEX_VALUE,"
			+ "sum(bc.QTY*coalesce(nse.LAST_PRICE,bse.LAST_PRICE)) as NET_PRICE from BASKET_DETAILS bd"
			+ " left join BASKET_CONSTITUENTS bc on bd.BASKET_NAME=bc.BASKET_NAME and bd.BASKET_VERSION=bc.BASKET_VERSION"
			+ " left join USER_WATCHLIST_BASKET w on (bd.BASKET_NAME=w.BASKET_NAME and w.USER_ID=?)"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bd.BASKET_NAME=? and bd.IS_LATEST=1";

	public static final String MY_INVESTMENTS_QUERY = "select bo.ORDER_NO,bo.PARENT_ORDER_NO,bo.ORDER_TYPE,bo.IS_REBALANCE_DONE,bo.BASKET_NAME,bo.BASKET_VERSION,bd.ICON_URL,"
			+ "bo.REMAINING_QTY,sum(boc.AVG_PRICE*boc.FILLED_QTY) as PRICE,bo.BASKET_PL_VALUE,bo.BASKET_PL_PERCENT,"
			+ "sum(boc.FILLED_QTY*coalesce(nse.LAST_PRICE,bse.LAST_PRICE)) as LAST_PRICE, boc.TRANS_TYPE as TRANS_TYPE from BASKET_ORDERS bo"
			+ " inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
			+ " left join BASKET_DETAILS bd on bd.BASKET_NAME=bo.BASKET_NAME and bd.BASKET_VERSION=bo.BASKET_VERSION"
			+ " left join quote_data.NSE_QUOTE nse on  boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on  boc.SYMBOL=bse.SYMBOL"
			+ " where USER_ID=? and (bo.STATUS = 'complete' AND (bo.ORDER_TYPE = 'BUY' OR bo.ORDER_TYPE = 'REBALANCE') AND IS_SOLD = '0') group by bo.ORDER_NO order by bo.CREATED_AT DESC";

	public static final String MY_INVESTMENT_QUERY = "select bo.CREATED_AT,boc.AVG_PRICE as AVG_PRICE, boc.BASKET_ORDER_NO as BASKET_ORDER_NO, bo.REMAINING_QTY as BASKET_QTY, boc.TRANS_TYPE as TRANS_TYPE, boc.SYMBOL as SYMBOL, boc.PL_VALUE,boc.PL_PERCENT,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION,(sum(boc.FILLED_QTY)-((bo.QTY-bo.REMAINING_QTY) * boc.ORIGINAL_QTY/bo.QTY)) AS ORIGINAL_QTY,"
			+ "coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE from BASKET_ORDERS bo"
			+ " inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
			+ " left join quote_data.NSE_QUOTE nse on  boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on  boc.SYMBOL=bse.SYMBOL"
			+ " where bo.ORDER_NO=? and boc.FILLED_QTY != 0 group by boc.SYMBOL";

	public static final String DASHBOARD_BASKETS_QUERY = "select bd.BASKET_NAME,bd.RECOMMENDED_FLAG,bd.FEATURED_FLAG,bd.POPULARITY,bd.ICON_URL,"
			+ "sum(bc.QTY*coalesce(nse.LAST_PRICE,bse.LAST_PRICE)) as NET_PRICE,bd.ONE_MONTH_RET,bd.ONE_YEAR_RET from BASKET_DETAILS bd"
			+ " left join BASKET_CONSTITUENTS bc on bd.BASKET_NAME=bc.BASKET_NAME and bd.BASKET_VERSION=bc.BASKET_VERSION"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bd.IS_LATEST=1 and (RECOMMENDED_FLAG=1 or POPULARITY in (2,3)) group by bd.BASKET_NAME";

	public static final String GET_STOCKNOTES_QUERY = "select bc.SYMBOL,sn.content,sn.media_url,sn.media_type,sn.media_icon,sn.SEO_URL from BASKET_CONSTITUENTS bc"
			+ " left join samco.STOCKNOTES_NEWS sn on bc.SYMBOL=sn.symbol where bc.BASKET_NAME = ? and bc.IS_LATEST=1 and"
			+ " sn.updated_at = ( SELECT MAX(updated_at)FROM samco.STOCKNOTES_NEWS WHERE symbol = sn.symbol)"
			+ " group by bc.SYMBOL";

	public static final String MARGIN_VALIDATOR_QUERY = "select bc.QTY* sum(coalesce(nse.LAST_PRICE,bse.LAST_PRICE)) as NET_PRICE from BASKET_CONSTITUENTS bc"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bc.BASKET_NAME=?  and IS_LATEST=1";

	public static final String ORDER_REVIEW_QUERY = "select bd.ICON_URL,bc.QTY,coalesce(nse.LAST_PRICE,bse.LAST_PRICE) as LAST_PRICE,"
			+ "coalesce(nse.DESCRIPTION,bse.DESCRIPTION) as DESCRIPTION from BASKET_DETAILS bd"
			+ " inner join BASKET_CONSTITUENTS bc on bd.BASKET_NAME=bc.BASKET_NAME and bd.BASKET_VERSION=bc.BASKET_VERSION"
			+ " left join quote_data.NSE_QUOTE nse on bc.EXCHANGE='NSE' and bc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on bc.EXCHANGE='BSE' and bc.SYMBOL=bse.SYMBOL"
			+ " where bd.BASKET_NAME=?";

	public static final String INVESTMENT_DETAILS_QUERY = "select bo.ORDER_NO,bo.BASKET_NAME,bo.BASKET_VERSION,bd.ICON_URL,"
			+ " bo.QTY,sum(boc.PRICE*boc.FILLED_QTY) as PRICE,"
			+ " sum(boc.ORIGINAL_QTY*coalesce(nse.LAST_PRICE,bse.LAST_PRICE)) as LAST_PRICE from BASKET_ORDERS bo"
			+ " inner join BASKET_ORDER_CONSTITUENTS boc on bo.ORDER_NO=boc.BASKET_ORDER_NO"
			+ " left join BASKET_DETAILS bd on bd.BASKET_NAME=bo.BASKET_NAME and bd.BASKET_VERSION=bo.BASKET_VERSION"
			+ " left join quote_data.NSE_QUOTE nse on  boc.SYMBOL=nse.SYMBOL"
			+ " left join quote_data.BSE_QUOTE bse on  boc.SYMBOL=bse.SYMBOL"
			+ " where USER_ID=? and bo.ORDER_TYPE='BUY' and bo.STATUS='complete' and bo.IS_SOLD=0 group by bo.ORDER_NO order by bo.CREATED_AT desc";

	public static final String INITIAL_INVEST = "select ORDER_NO from BASKET_ORDERS where USER_ID=?";

	public static final String GET_STOCK_COUNT = "SELECT SYMBOL FROM BASKET_ORDER_CONSTITUENTS WHERE BASKET_ORDER_NO=? AND NEST_ORDER_STATUS <> 'complete' GROUP BY SYMBOL";

	public static final String SALES_FEES = "select SALES_FEE from BASKET_DETAILS where BASKET_NAME=? AND BASKET_VERSION=?";

	public static final String BASKET_QTYS = "select QTY from BASKET_ORDERS WHERE ORDER_NO IN";
}
