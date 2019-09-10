package com.msf.libsb.constants;

import java.util.Vector;

public class DBConstants
{
	 public static final String STATUS = "STATUS";

	public static final String NSE_INTRADAY_TABLE = "nse";

	public static final String BSE_INTRADAY_TABLE = "bse";

	public static final String NFO_INTRADAY_TABLE = "nfo";

	public static final String CDS_INTRADAY_TABLE = "cds";

	public static final String MFO_INTRADAY_TABLE = "mfo";

	public static final String BCD_INTRADAY_TABLE = "bcd";

	public static final String BFO_INTRADAY_TABLE = "bfo";

	public static final String INDICES_INTRADAY_TABLE = "index";


	public static final String NSE_HISTORY_TABLE = "nse_history_day";

	public static final String BSE_HISTORY_TABLE = "bse_history_day";

	public static final String NFO_HISTORY_TABLE = "nfo_history_day";

	public static final String CDS_HISTORY_TABLE = "cds_history_day";

	public static final String MCX_SX_HISTORY_TABLE = "mfo_history_day";

	public static final String INDICES_HISTORY_TABLE = "index_history_day";

	public static final int DUPLICATE_KEY = 1062;

	public static final String NULL_DATA = "NA";

	// User session related constants

	public static final String USER_ID = "user_id";
	public static final String BUILD = "build";
	public static final String APP_ID = "app_id";
	public static final String USER_TYPE = "user_type";
	public static final String OMNESYS_JSESSION = "jsession";
	public static final String OMNESYS_JKEY = "jkey";
	public static final String USER_STAGE = "user_stage";
	public static final String PRODUCT_ALIAS = "product_alias";
	public static final String IS_ENABLE_TRANSACTION_PASSWORD = "is_enable_transPass";
	public static final String ACCOUNT_ID = "account_id";
	public static final String BRANCH_ID = "branch_id";
	public static final String BROKER_NAME = "broker_name";
	public static final String DEFAULT_MARKET_WATCH = "default_market_watch";
	public static final String SESSION_ID = "session_id";

	public static final String SN_FAV_GRP_ID = "fav_sym_group_id";
	public static final String SN_WL_GRP_ID = "watch_sym_group_id";
	public static final String SAMCO_TOKEN = "samco_token";
	public static final String PRODUCT_TYPE = "product_type";
	public static final String WEBLINK = "weblink";
	public static final String ORDER_TYPE = "ORDER_TYPE";
	public static final String EXCHANGE_ARRAY = "exchange_array";
	public static final String SYNC_WATCHLIST = "sync_watchlist";

	/* DB Column keys */
	public static final String CHANGE = "CHANGE";
	public static final String CHANGE_PERCENTAGE = "CHANGE_PER";
	public static final String PREVIOUS_CLOSE_PRICE = "PRE_CLOSE_PRICE";
	public static final String LAST_TRADED_PRICE = "LAST_PRICE";
	public static final String SYMBOL_NAME = "SYMBOL_NAME";
	public static final String STRIKE_PRICE = "STRIKE_PRICE";
	public static final String SYMBOL = "SYMBOL";
	public static final String ASSOCIATEDSYMBOL = "ASSOCIATEDSYMBOL";
	public static final String INDUSTRY = "INDUSTRY";
	public static final String SECTOR = "SECTOR";
	public static final String FIFTY_TWO_WEEK_HIGH = "52_WK_HIGH";
	public static final String FIFTY_TWO_WEEK_LOW = "52_WK_LOW";
	public static final String NO_OF_SHARES = "NO_OF_SHARES";
	public static final String TOTAL_TRADED_VALUE = "TOTAL_TRADED_VALUE";
	public static final String MARKET_LOT = "LOTSIZE";
	public static final String VAR_MARGIN = "VAR_MAR";
	public static final String AVG_PRICE = "AVG_PRICE";
	public static final String TOTAL_BUY_QTY = "TOTAL_BUY_QTY";
	public static final String TOTAL_SELL_QTY = "TOTAL_SELL_QTY";
	public static final String TRADING_SYMBOL = "TRADING_SYMBOL";
	public static final String EXPIRY_DATE = "EXPIRY_DATE";
	public static final String INSTRUMENT_TYPE = "INSTRUMENT_TYPE";
	public static final String BASE_SYMBOL = "BASE_SYMBOL";
	public static final String TOTAL_VOLUME = "TOTAL_VOLUME";
	public static final String NSE_SYMBOL = "NSE_SYMBOL";
	public static final String OPTION_TYPE = "OPTION_TYPE";
	public static final String EXPIRY_IN_SECONDS = "EXPIRY_IN_SECONDS";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String EXCHANGE = "EXCHANGE";
	public static final String OPEN_PRICE = "OPEN_PRICE";
	public static final String HIGH_PRICE = "HIGH_PRICE";
	public static final String LOW_PRICE = "LOW_PRICE";
	public static final String CLOSE_PRICE = "CLOSE_PRICE";
	public static final String LAST_TRADED_TIME = "LAST_TRADED_TIME";
	public static final String TICKSIZE = "TICKSIZE";
	public static final String LAST_TRADED_VOLUME = "LAST_TRADED_VOLUME";
	public static final String OPENINTEREST = "OPENINTEREST";
	public static final String TOP5ASKPRICE = "TOP5ASKPRICE";
	public static final String TOP5ASKVALUE = "TOP5ASKVALUE";
	public static final String TOP5BIDPRICE = "TOP5BIDPRICE";
	public static final String TOP5BIDVALUE = "TOP5BIDVALUE";
	public static final String MIS_MULTIPLIER = "MIS_MULTIPLIER";
	public static final String OI_CHANGE = "OI_CHANGE";
	public static final String PREV_OI = "PREV_OPENINTEREST";
	public static final String MUTLIPLIER = "MULTIPLIER";
	public static final String NRML_MUTLIPLIER = "NRML_MULTIPLIER";
	public static final String CO_MARGIN = "CO_MARGIN";
	public static final String LAST_UPDATED_TIME = "LAST_FEED_TIME";

	// Filter related constants
	public static final String ID = "id";
	public static final String MAIN_FILTER = "main_filter";
	public static final String SUB_FILTER = "sub_filter";


	// Corporate action related constants
	public static final String EVENT = "event";
	public static final String COMMENT = "comment";
	public static final String EXP_DATE = "date";

	// Stock note settings
	public static final String SETTINGS_ID = "id";
	public static final String SETTINGS_GROUP = "settings_group";
	public static final String SETTINGS_MAIN = "main_settings";
	public static final String SETTINGS_SUB = "sub_settings";

	//Inbox constants
	public static final String PUSH_MSG = "PUSH_MESSAGE";
	public static final String MESSAGE = "MSG";
	public static final String EXTRA = "EXTRA_INFO";
	public static final String NOTIFICATION_ID =  "NOTIFICATION_ID";
	public static final String NEWS_ID = "NEWS_ID";
	public static final String IS_READ = "IS_READ";
	public static final String ALERT_ID = "ALERT_ID";
	public static final String TYPE = "TYPE";
	public static final String CREATED_AT = "CREATED_AT";
	//Stock note settings
	public static final String FEED_TYPE_FAV_WAT = "feed_type_fw";
	public static final String FEED_TYPE_TREN_TOP = "feed_type_tta";
	public static final String FEED_TYPE_ALL = "feed_type_all";
	public static final String VOLUME = "volume";
	public static final String EMAIL = "email";

	// Stock notes constants
	public static final String PRIMARY_CONTRACT = "p_contract";
	public static final String ENGAGEMENT_SCORE = "ENGAGEMENT_SCORE";
	public static final String FINAL_SCORE = "SCORE_FINAL";

	public static final String NEWS_ALGORITHM = "algorithm";
	public static final String NEWS_TYPE_NEWS = "news";
	public static final String NEWS_TYPE_QUOTE = "quote";
	public static final String NEWS_TYPE_RELATED = "related";
	public static final String NEWS_TYPE_FAVOURITE = "watchlist";
	public static final String NEWS_TYPE_WATCHLIST = "favourite";


	// Equities search related constants
	public static final String EQUITY_SEARCH_NSE = "search_nse";
	public static final String EQUITY_SEARCH_BSE = "search_bse";
	public static final String EQUITY_SEARCH_INDICES = "search_ind";
	public static final String EQUITY_TOP_GAINER_NSE = "top_gainer_nse";
	public static final String EQUITY_TOP_LOSER_NSE = "top_loser_nse";
	public static final String EQUITY_52_WEEK_HIGH_NSE = "52_week_high_nse";
	public static final String EQUITY_52_WEEK_LOW_NSE = "52_week_low_nse";
	public static final String EQUITY_TOP_GAINER_BSE = "top_gainer_bse";
	public static final String EQUITY_TOP_LOSER_BSE = "top_loser_bse";
	public static final String EQUITY_52_WEEK_HIGH_BSE = "52_week_high_bse";
	public static final String EQUITY_52_WEEK_LOW_BSE = "52_week_low_bse";
	public static final String EQUITY_HOLDINGS_NSE = "holdings_nse";
	public static final String EQUITY_HOLDINGS_BSE = "holdings_bse";
	public static final String EQUITY_MARKET_CAP_NSE = "mktcap_nse";
	public static final String EQUITY_MARKET_CAP_BSE = "mktcap_bse";

	/* Account summary */
	public static final String MARGIN_BLOCKED = "margin_blocked";
	public static final String HOLDINGS = "holdings";
	public static final String CASH_AVAILABLE = "cash_available";
	public static final String NET_OPTION = "net_option";
	public static final String TOTAL_EQUITY = "total_equity";
	public static final String TOTAL_COMMODITY = "total_commodity";

	public static final String ISIN = "ISIN";
	public static final String VALIDITY = "VALIDITY";

	public static final String NUMBER = "number";

	/* Welcome Aboard */
	public static final String EQUITY = "equity";
	public static final String EQ_DERIVATIVE = "equity_derivatives";
	public static final String INDICES = "indices";
	public static final String CURRENCY = "currency";
	public static final String COMMODITY = "commodity";

	public static final String ASSET_CLASS = "asset_class";
	public static final String ANSWER = "answer";
	public static final String VALUE = "value";
	public static final String QUESTION = "question";
	
	/* Stock Basket Constants */
	public static final String BASKET_NAME="BASKET_NAME";
	public static final String STRENGTH="STRENGTH";
	public static final String VALUE_FOR_MONEY ="VALUE_FOR_MONEY";
	public static final String POPULARITY ="POPULARITY";
	public static final String INDEX_VALUE="INDEX_VALUE";
	public static final String NET_PRICE="NET_PRICE";
	public static final String IMAGE_URL="IMAGE_URL";
	public static final String ONE_MONTH_RET="ONE_MONTH_RET";
	public static final String THREE_MONTH_RET="THREE_MONTH_RET";
	public static final String SIX_MONTH_RET="SIX_MONTH_RET";
	public static final String ONE_YEAR_RET="ONE_YEAR_RET";
	public static final String THREE_YEAR_RET="THREE_YEAR_RET";
	public static final String FIVE_YEAR_RET="FIVE_YEAR_RET";
	public static final String CATEGORY="CATEGORY";
	public static final String RECOMMENDED="RECOMMENDED";
	public static final String FEATURED="FEATURED";
	public static final String FLAG ="flag";
	public static final String BASKET_VERSION="BASKET_VERSION";
	public static final String ICON_URL="ICON_URL";
	public static final String QTY="QTY";
	public static final String PRICE="PRICE";
	public static final String IS_LATEST="IS_LATEST";
	public static final String RATIONALE="RATIONALE";
	public static final String TARGET_INDEX="TARGET_INDEX";
	public static final String TARGET_INDEX_PERCENTAGE="TARGET_INDEX_CHANGE_PER";
	public static final String TARGET_VALUE="TARGET_VALUE";
	public static final String TARGET_DATE="TARGET_DATE";
	
	
	/*Stock Note Constants*/
	public static final String CONTENT="content";
	public static final String MEDIA_URL="media_url";
	public static final String MEDIA_TYPE="media_type";
	public static final String MEDIA_ICON="media_icon";
	public static final String SEO_URL="SEO_URL";
	
	/*BASKET ORDER CONSTANTS*/
	public static final String NEST_ORDER_NO="NEST_ORDER_NO";
	public static final String NEST_ORDER_STATUS="NEST_ORDER_STATUS";
	public static final String FILLED_QTY = "FILLED_QTY";
	public static final String ORIGINAL_QTY = "ORIGINAL_QTY";
	public static final String TRANS_TYPE = "TRANS_TYPE";
	public static final String BASKET_ORDER_NO = "BASKET_ORDER_NO";
	public static final String ORDER_NO = "ORDER_NO";

	public static final String REBALANCE_COUNT = "REBALANCE_COUNT";
	public static final String FIX_COUNT = "FIX_COUNT";
	public static final String WL_FLAG = "WL_FLAG";
	public static final String RATING = "RATING";
	public static final String FEATURED_FLAG = "FEATURED_FLAG";
	public static final String RECOMMENDED_FLAG = "RECOMMENDED_FLAG";

	public static final String BASKET_PL_VALUE = "BASKET_PL_VALUE";
	public static final String BASKET_PL_PERCENT = "BASKET_PL_PERCENT";

	public static final String PL_VALUE = "PL_VALUE";
	public static final String PL_PERCENT = "PL_PERCENT";	
	
}