package com.msf.libsb.constants;

public class INFO_IDS 
{
	public static final String INVALID_APPID = "EGN0001";
	public static final String REQ_FAILED_ON_SCREEN ="EGN0002";
	public static final String INVALID_REQUEST_PARAM="EGN0003";
	public static final String RESPONSE_FAILURE = "EGN0004";
	public static final String DYNAMIC_MSG = "EGN0005";
	public static final String INVALID_EXCHANGE = "EGN0006";
	public static final String INVALID_SESSION = "EGN0007";
	public static final String NO_DATA_FOUND = "EGN0008";
	public static final String UNKNOWN = "EGN0009";
	public static final String REQUEST_FAILED = "EGN0010";
	public static final String DUPLICATE_ORDER = "EGN0011";
	public static final String NO_DATA_AVAILABLE_FOR_FILTER = "EGN0012";

	public static final String SUCCESS = "0";
	public static final String INSUFFICIENT_FUNDS="EMV0001";
	public static final String INVALID_BUILD = "EIT0001";
	public static final String UNABLE_TO_CONNECT_TO_SQLSERVER = "EIT0002";

	public static final String LOGIN_FAILED = "ELG0001";
	public static final String USER_BLOCKED = "ELG0002";
	public static final String LOGOUT_FAILED = "ELG0003";
	public static final String INVALID_PASSWORD = "ELG0004";
	public static final String INVALID_DOB = "ELG0005";
	public static final String UNABLE_TO_PROCESS_LOGIN = "ELG0006";

	public static final String UNBLOCK_USER_FAILED = "ELH0001";
	public static final String FORGOT_PASSWORD_FAILED = "ELH0002";
	public static final String FORGOT_USERID_FAILED = "ELH0003";
	public static final String CHANGE_PASSWORD_FAILED = "ELH0004";
	public static final String INVALID_EMAILID = "ELH0005";
	public static final String INVALID_PAN = "ELH0006";
	public static final String INVALID_USER = "ELH0007";
	public static final String RESET_PASSWORD_FAILED="ELH0008";
	public static final String TRANSACTION_PASSWORD_FAILED = "ELH0009";
	public static final String CHANGE_PASSWORD_POLICY_FAILED = "ELH0010";
	public static final String USER_DETAILS_FAILED = "ELH0011";
	public static final String RESET_2FA_FAILED = "ELH0012";

	public static final String NEW_GROUP_CREATION_FAILED = "EWL0001";
	public static final String GROUP_DELETION_FAILED = "EWL0002";
	public static final String ADD_SYMBOL_FAILED = "EWL0003";
	public static final String SYMBOL_DELETION_FAILED = "EWL0004";
	public static final String MARKETWATCHLIST_RETRIEVAL_FAILED="EWL0005";
	public static final String SYMBOLS_RETRIEVAL_FAILED="EWL0006";
	public static final String SET_DEFAULTGROUP_FAILED="EWL0007";
	public static final String PREDEFINED_GROUPS_RETRIEVAL_FAILED = "EWL0008";
	public static final String PREDEFINED_SYMBOLS_RETRIEVAL_FAILED = "EWL0009";
	public static final String REARRANGE_SCRIPS_FAILED = "EWL0010";
	public static final String SYMBOL_ALREADY_PRESENT="EWL0011";
	public static final String NO_MARKET_WATCH="EWL0012";
	public static final String GROUP_DELETION_DMW_FAILED = "EWL0013";
	public static final String ADD_SYMBOL_NP_EXCHANGE_FAILED = "EWL0014";

	public static final String QUOTES_RETRIEVAL_FAILED = "EQT0001";
	public static final String MARKETDEPTH_RETRIEVAL_FAILED = "EQT0002";
	public static final String MARKETDATA_RETRIEVAL_FAILED = "EQT0003";
	public static final String QUOTE_TRY_ANOTHER_SYMBOL = "EQT0004";

	public static final String HOLDINGS_RETRIEVAL_FAILED = "EHL0001";
	public static final String HOLDINGS_NOT_FOUND = "EHL0002";
	public static final String POSITIONS_RETRIEVAL_FAILED = "EPS0001";
	public static final String SQUAREOFF_POSITION_FAILED = "EPS0002";
	public static final String POSITION_CONVERSION_FAILED = "EPS0003";
	public static final String POSITIONS_NOT_FOUND = "EPS0004";

	public static final String INDEXEXCHANGES_RETRIEVAL_FAILED = "EIN0001";
	public static final String INDEXDETAILS_RETRIEVAL_FAILED = "EIN0002";

	public static final String CHARTDATA_RETRIEVAL_FAILED = "ECH0001";
	
	public static final String TIME_ELAPSED = "ETE0001";

	public static final String EQUITY_SYMBOL_NOT_FOUND = "ESE0001";
	public static final String DERIVATIVE_SYMBOL_NOT_FOUND = "ESD0001";

	public static final String LIMITS_RETRIEVAL_FAILED = "ELS0001";

	public static final String CIRCUIT_LIMITS_NOT_AVAILABLE = "ETV0001";
	public static final String CIRCUIT_LIMITS_WRONG_RANGE = "ETV0002";
	public static final String GTD_INVALID_DATE = "ETV0003";
	public static final String GTD_RETRIEVAL_FAILED = "ETV0004";
	public static final String GTD_INVALID_MONTH = "ETV0005";
	public static final String GTD_INVALID_DAY = "ETV0006";
	public static final String GTD_DATE_GT_EXPIRY = "ETV0007";
	public static final String GTD_DATE_LT_TODAY = "ETV0008";
	public static final String GTD_DATE_GT_7_DAYS = "ETV0009";
	public static final String GTD_DAYS_BT_1TO7 = "ETV0010";
	public static final String GTD_INVALID_NO_OF_DAYS = "ETV0011";
	public static final String GTD_DAYS_LT_EXPIRY = "ETV0012";
	public static final String GTD_INVALID_TIME = "ETV0013";

	public static final String O_CO_MKT_ONLY = "ETV0014";
	public static final String LTP_COVER_PERCENTAGE_NOT_AVAILABLE = "ETV0015";

	/* Cancel Order */
	public static final String CANCEL_UNABLE_TO_FETCH_SERVICE = "ECO0001";
	public static final String CANCEL_ERROR = "ECO0002";

	/* Modify Order */
	public static final String MODIFY_UNABLE_TO_FETCH_SERVICE = "EMO0001";
	public static final String MODIFY_ERROR = "EMO0002";
	public static final String MODIFY_GTD_DATE_REQUIRED = "EMO0003";
	public static final String MODIFY_GTD_ERROR = "EMO0004";

	/* Related derivatives, Futute chain */
	public static final String REL_DERIVATIVES_NO_DATA = "ERS0001";
	public static final String FUTURE_CHAIN_NO_DATA = "ERS0002";

	/* Errors in place order */
	public static final String PLACEORDER_UNABLE_TO_FETCH_SERVICE = "EPO0001";
	public static final String PLACEORDER_ERROR = "EPO0002";

	public static final String OB_NOT_ABLE_TO_RETRIEVE = "EOB0001";
	public static final String OB_ORDERS_NOT_FOUND = "EOB0002";
	public static final String OB_NO_ORDERS_AVAILABLE_FOR_FILTER = "EOB0003";

	/* Favourites error */
	public static final String MAX_SYMBOLS_REACHED = "ESN0001";
	public static final String INVALID_GROUP = "ESN0002";
	public static final String INVALID_SYMBOL = "ESN0003";
	public static final String STOCK_NOTES_WL_FAILED = "ESN0004";
	public static final String STOCK_NOTES_FAV_FAILED = "ESN0005";
	public static final String STOCK_NOTES_GROUP_FAILED = "ESN0006";
	public static final String NO_FAV_SYMBOLS = "ESN0007";
	public static final String NO_MUTED_SYMBOLS = "ESN0008";

	/* Trade Book Error */
	public static final String TB_NOT_ABLE_TO_RETRIEVE = "ETB0001";
	public static final String TB_ORDERS_NOT_FOUND = "ETB0002";
	public static final String TB_NO_ORDERS_AVAILABLE_FOR_FILTER = "ETB0003";

	/* Bracket Order error */
	public static final String BO_TRAILING_TICK_ERROR = "EBO0001";
	public static final String BO_SL_TICK_ERROR = "EBO0002";
	public static final String BO_INVALID_TICK = "EBO0003";
	public static final String BO_SQROFF_TICK_ERROR = "EBO0004";
	public static final String BO_INVALID_SL_ABSOLUTE = "EBO0005";
	public static final String BO_INVALID_SQROFF_ABSOLUTE = "EBO0006";
	public static final String BO_ERROR = "EBO0007";
	public static final String BO_EXIT_ERROR = "EBO0008";

	/* Add alert error */
	public static final String AL_DUPLICATE_ERROR = "EAT0001";
	public static final String AL_NO_NOTIFICATION = "EAT0002";
	public static final String AL_NO_NOTIFICATION_FOR_FILTER = "EAT0003";

	/* Pay in / Pay out */
	public static final String PAYIN_DETAIL_UNWAVAILABLE = "EPAY0001";
	public static final String PAYOUT_DETAIL_UNWAVAILABLE = "EPAY0002";

	/* Pagination error */
	public static final String PAGINATION_ERROR = "EPG0001";

	public static final String SAVE_2FA = "ELG0101";
	public static final String SAVE_2FA_ALREADY_SET = "ELG0102";

	public static final String BASKET_ALREADY_PRESENT = "ESB0001";
	public static final String BASKET_WATCHLIST_ALREADY_ADDED = "ESB0002";
	public static final String BASKET_WATCHLIST_NOT_FOUND ="ESB0003";
}
