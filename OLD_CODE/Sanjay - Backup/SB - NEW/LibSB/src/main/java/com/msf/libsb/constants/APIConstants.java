package com.msf.libsb.constants;

public class APIConstants 
{

	public static final int SUCCESS_CODE = 200;

	public static final String STATUS = "stat";
	public static final String SUCCESS_STATUS = "Ok";
	public static final String FAILURE_STATUS = "Not_Ok";
	public static final String MESSAGE = "msg";
	public static final String RESPONSE_MESSAGE = "Resp";
	public static final String ERROR_MESSAGE = "Emsg";
	public static final String API_SESSION_EXPIRED = "Session Expired";
	public static final String ERROR_MESSAGE2 = "error";
	public static final String TDATA = "tdata";

	public static final String USER_ID = "uid";
	public static final String PASSWORD = "pwd";
	public static final String IMEI_NO= "imei";
	public static final String FTL= "ftl";
	public static final String APP_VERSION= "apk";
	public static final String SOURCE = "Source";
	public static final String INDEX = "sIndex";
	public static final String COUNT = "scount";

	public static final String USER_TOKEN = "sUserToken";
	public static final String USER_SESSION_ID = "UserSessionID";
	public static final String PASSWORD_RESET = "sPasswordReset";

	public static final String ACCOUNT_ID = "sAccountId";
	public static final String ACCOUNT_NAME = "accountName";
	public static final String ACTIVE_TIME = "i_ActiveTime";

	public static final String EXCHANGE = "Exchange";
	public static final String S_EXCHANGE = "sExchange";
	public static final String SCRIP_TOKEN = "Token";
	public static final String S_SCRIP_TOKEN = "sToken";
	public static final String PRICE = "Price";
	public static final String STOP_LOSS_PRICE = "sSLPrice";
	public static final String ORDER_TYPE= "sOrderType";
	public static final String S_MARKET_PROTECTION = "sMarketProtection";

	public static final String RETENTION_LIST = "Ret";
	public static final String DATE = "Date";

	//Order Related Constants
	public static final String PRODUCT_ALIAS = "s_prdt_ali";
	public static final String O_ACCOUNT_ID = "actid";
	public static final String TRADING_SYMBOL = "Tsym";
	public static final String O_EXCHANGE = "exch";
	public static final String TRANSACTION_TYPE = "Ttranstype";
	public static final String O_RETENTION = "Ret";
	public static final String PRICE_TYPE = "prctyp";
	public static final String QUANTITY = "qty";
	public static final String DISCLOSED_QUANTITY = "discqty";
	public static final String MIN_QUANTITY = "MinQty";
	public static final String O_SCRIP_CODE = "TokenNo";
	public static final String O_MARKET_PROTECTION = "MktPro";
	public static final String O_PRICE = "Price";
	public static final String TRIGGER_PRICE = "TrigPrice";
	public static final String PRODUCT_CODE = "Pcode";
	public static final String DATE_DAYS = "DateDays";
	public static final String AMO_ORDER = "AMO";
	public static final String POSITION_SQUARE_FLAG = "PosSquareFlg";
	public static final String ORDER_NO = "NOrdNo";
	public static final String BUY = "B";
	public static final String SELL = "S";
	public static final String EXPIRY_DATE = "Expdate";
	public static final String RETENTION = "Retention";
	public static final String GTD_VALUE = "Gtdval";
	public static final String MARKET_PROTECTION = "MarketProtection";
	public static final String MARKET_ORDER= "MKT";
	public static final String LIMIT_ORDER = "L";
	public static final String STOP_MARKET_ORDER = "SL-M";
	public static final String STOP_LIMIT_ORDER = "SL";
	public static final String VENDOR_CODE = "naicCode";
	public static final String O_ORDER_SOURCE = "orderSource";

	//Order Book Constants
	public static final String OPEN_ORDER = "open";
	public static final String TRIGGER_PENDING_ORDER = "trigger pending";
	public static final String OPEN_PENDING_ORDER = "open pending";
	public static final String COMPLETE_ORDER = "complete";
	public static final String REJECTED_ORDER = "rejected";
	public static final String CANCELLED_ORDER = "cancelled";
	public static final String AFTER_MARKET_ORDER = "after market order req received";
	public static final String MODIFY_AFTER_MARKET_ORDER = "modify after market order req received";
	public static final String OB_USER = "user";
	public static final String OB_ACCOUNT_ID = "accountId";
	public static final String OB_NEST_ORDER_NUMBER = "Nstordno";
	public static final String OB_PRICE = "Prc";
	public static final String AVERAGE_PRICE = "Avgprc";
	public static final String OB_QUANTITY = "Qty";
	public static final String OB_DISCLOSED_QUANTITY = "Dscqty";
	public static final String OB_TRIGGER_PRICE = "Trgprc";
	public static final String OB_SCRIP_NAME = "Scripname";
	public static final String OB_SCRIP_CODE = "Sym";
	public static final String OB_STATUS = "Status";
	public static final String FILLED_QUANTITY = "Fillshares";
	public static final String OB_EXPIRY_DATE = "ExpDate";
	public static final String EXCHANGE_ORDER_NO = "ExchOrdID";
	public static final String OB_TRANSACTION_TYPE = "Trantype";
	public static final String OB_PRICE_TYPE = "Prctype";
	public static final String TICK_SIZE = "ticksize";
	public static final String BOARD_LOD_QUANTITY = "bqty";
	public static final String REMARKS = "remarks";
	public static final String OB_MARKET_PROTECTION = "Mktpro";
	public static final String RESULT = "Result";
	public static final String OB_SYOM_ORDER_ID = "SyomOrderId";
	public static final String OB_EXCHANGE_SEGMENT_NAME = "Exseg";
	public static final String COVER_ORDER = "CO";
	public static final String OB_COVER_ORDER_PERCENT = "COPercentage";
	public static final String OB_PRECISION = "decprec";
	public static final String OB_INSTRUMENT="InstName";
	public static final String ORDER_SOURCE="ordersource";
	public static final String OB_TRADING_SYMBOL = "Trsym";

	public static final String MO_ACCOUNT_ID = "Actid";
	public static final String MO_EXCHANGE_SEGMENT_NAME = "Exchangeseg";
	public static final String MO_TRANSACTION_TYPE = "Transtype";
	public static final String VALIDITY = "Validity";
	public static final String MO_SCRIP_CODE = "Symbol";
	public static final String MO_FILLED_QUANTITY = "Filledqty";
	public static final String REQUEST_ID = "RequestID";
	public static final String MO_MARKET_PROTECTION = "Mktpro";
	public static final String MO_FILL_QUANTITY = "Fillqty";

	public static final String CO_STATUS = "sStatus";
	public static final String CO_TRADING_SYMBOL = "sTradeSymbol";
	public static final String CO_EXCHANGE = "sExch";
	public static final String CO_NEST_ORDER_NO = "NestOrd";

	public static final String SYOM_ORDER_NUMBER = "s_sImOrderNumb";

	public static final String OH_TRADING_SYMBOL = "Trsym";
	public static final String OH_TRANSACTION_TYPE = "Action";
	public static final String OH_NEST_ORDER_NO = "nestordernumber";
	public static final String OH_TRIGGER_PRICE = "triggerprice";
	public static final String OH_DISCLOSED_QUANTITY = "disclosedqty";
	public static final String SYMBOL_NAME = "symbolname";
	public static final String OH_EXCHANGE_ORER_ID = "exchangeorderid";
	public static final String OH_PRODUCT_CODE = "productcode";
	public static final String OH_EXCHANGE = "exchange";

	public static final String LAST_TRADED_PRICE = "Ltp";
	public static final String COVER_PERCENTAGE = "Coverprc";
	public static final String DECIMAL_PRECISION = "Decprc";

	public static final String TB_PRICE = "Price";
	public static final String TB_EXCHANGE_SEGMENT_NAME = "Exchseg";
	public static final String TB_ORDER_TIME = "Time";
	public static final String TB_EXCHANGE_ORDER_NO = "ExchordID";
	public static final String TB_FILL_ID = "FillId";

	public static final String FILL_QUANTITY = "Fillqty";
	public static final String FILL_ID = "FillID";
	public static final String FILL_LEG = "Fillleg";
	public static final String FILL_DATE = "Filldate";
	public static final String FILL_TIME = "Filltime";
	public static final String CUSTOMER_FIRM = "Custofrm";
	public static final String ORDER_DURATION = "Ordduration";
	public static final String NEST_ORDER_REQUEST_ID = "NOReqID";
	public static final String PRODUCT_TO_CHANGE = "Pchange";
	public static final String FROM_DATE = "fromDate";
	public static final String TO_DATE = "toDate";
	public static final String SORT_TYP = "SortType";
	public static final String SORT_ON = "SortOn";

	public static final String TH_CUSTOMER_FIRM = "customerFirm";
	public static final String TH_FILL_DATE = "fillDate";
	public static final String TH_FILL_ID= "fillId";
	public static final String TH_FILLED_SHARES = "filledShares";
	public static final String TH_FILL_PRICE = "iFillPrice";
	public static final String TH_NEST_ORDER_REQUEST_ID = "nestOrderReqid";
	public static final String TH_DISCLOSED_QUANTITY = "orderDisclosedQty";
	public static final String TH_PRICE = "priceToFill";
	public static final String TH_PRICE_TYPE = "priceType";	
	public static final String TH_PRODUCT_CODE = "product";
	public static final String TH_TRADED_QUANTITY = "sFillQty";
	public static final String TH_STATUS = "status";
	public static final String TH_TRADING_SYMBOL = "tradingSymbol";
	public static final String TH_TRANSACTION_TYPE = "transactionType";
	public static final String TH_PENDING_QUANTITY = "unfilledSize";
	public static final String TH_NEST_ORDER_NUMBER = "nestOrderNumber";

	public static final String ENABLED = "Enabled";
	public static final String INDEX_DETAIL = "IndexDetail";
	public static final String INDEX_NAME = "name";
	public static final String EXCHANGE_NAME = "ExchName";
	public static final String TOKEN = "token";
	public static final String A_DECIMAL_PRECISION = "decimalPrecision";
	public static final String EXCHANGE_ARRAY = "exarry";
	public static final String REJECTION_REASON = "RejReason";
	public static final String ORDER_ENTRY_TIME = "orderentrytime";
	public static final String EXCHANGE_CONFIRMATION_TIME = "ExchConfrmtime";

	/* For AMO status request */
	public static final String AMO_STATUS = "amostatusflag";

	/* For margin limits request */
	public static final String LIMITS_SEGMENT = "segment";
	public static final String LIMITS_OPEN_BAL = "OpeningBalance";
	public static final String LIMITS_USED_AMT = "Utilizedamount";
	public static final String LIMITS_STOCK_VAL = "StockValuation";
	public static final String LIMITS_DIR_COLL_VAL = "directcollateralvalue";
	public static final String LIMITS_ADHOC_MARGIN = "adhocmargin";
	public static final String LIMITS_BRANCH_ADHOC = "branchadhoc";
	public static final String LIMITS_CREDITS = "credits";
	public static final String LIMITS_NOTIONAL_CASH = "notionalcash";
	public static final String LIMITS_PAY_IN_AMT = "PayinAmt";
	public static final String LIMITS_CNC_VAR_MARGIN = "cncMarginVarPrsnt";
	public static final String LIMITS_CNC_ELM_MARGIN = "cncMarginElmPrsnt";
	public static final String LIMITS_BUY_EXPO = "buyExposurePrsnt";
	public static final String LIMITS_SELL_EXPO = "sellExposurePrsnt";
	public static final String LIMITS_IPO_AMT = "IPOAmount";
	public static final String LIMITS_PAYOUT_AMT = "PayoutAmt";
	public static final String LIMITS_CATEGORY = "category";
	public static final String LIMITS_TURNOVER = "turnover";
	public static final String LIMITS_MULTIPLIER = "multiplier";
	public static final String LIMITS_GROSS_EXPO_VAL = "grossexposurevalue";
	public static final String LIMITS_ELM = "elm";
	public static final String LIMITS_VAL_IN_DELIVERY = "valueindelivery";
	public static final String LIMITS_VAR_MARGIN = "varmargin";
	public static final String LIMITS_SPAN_MARGIN = "spanmargin";
	public static final String LIMITS_ADHOC_SCRIP_MARGIN = "adhocscripmargin";
	public static final String LIMITS_SCRIP_MKT_MARGIN = "scripbasketmargin";
	public static final String LIMITS_EXPO_MARGIN = "exposuremargin";
	public static final String LIMITS_PREMIUM = "premiumpresent";
	public static final String LIMITS_RE_MTOM = "realisedmtom";
	public static final String LIMITS_UN_MTOM = "unrealisedmtom";
	public static final String LIMITS_MF_AMOUNT = "mfamount";
	public static final String LIMITS_DEBITS = "debits";
	public static final String LIMITS_CNC_SELL_CREDIT = "cncsellcreditpresent";
	public static final String LIMITS_CNC_MARGIN = "cncmarginused";
	public static final String LIMITS_CNC_BROKERAGE = "cncbrokerageprsnt";
	public static final String LIMITS_CNC_UN_MTOM = "cncunrealizedmtomprsnt";
	public static final String LIMITS_CNC_RE_MTOM = "cncrealizedmtomprsnt";
	public static final String LIMITS_MFSS_AMT = "mfssamountused";
	public static final String LIMITS_NFO_SPREAD_BENEFIT = "nfospreadbenefit";
	public static final String LIMITS_CDS_SPREAD_BENEFIT = "cdsspreadbenefit";
	public static final String LIMITS_BROKERAGE = "brokerageprsnt";
	public static final String LIMITS_CO_MARGIN_REQ = "COMarginRequired";
	public static final String LIMITS_BO_MARGIN_REQ = "BOmarginRequired";
	public static final String LIMITS_BPNL = "BookedPNL";
	public static final String LIMITS_UBPNL = "UnbookedPNL";
	public static final String LIMITS_BUYPOWER = "BuyPower";
	public static final String LIMITS_ADHOC = "Adhoc";
	public static final String LIMITS_NET_CASH_AVAIL = "Netcashavailable";


	/* For show quote request */
	public static final String Q_SYMBOL = "Symbol";
	public static final String Q_TRADING_SYMBOL = "TSymbl";
	public static final String Q_LTP = "LTP";
	public static final String Q_CHANGE_PER = "PerChange";
	public static final String Q_SERIES = "Series";
	public static final String Q_EXPIRY_DATE = "Exp";
	public static final String Q_BUY_QTY = "BQty";
	public static final String Q_SELL_QTY = "SQty";
	public static final String Q_BUY_RATE = "BRate";
	public static final String Q_SELL_RATE = "SRate";
	public static final String Q_LTT = "LTT";
	public static final String Q_CHANGE = "Change";
	public static final String Q_MULTIPLIER = "Multiplier";
	public static final String Q_TICKSIZE = "TickSize";
	public static final String Q_LOTSIZE = "BodLotQty";
	public static final String Q_PREV_CLOSE = "PrvClose";
	public static final String Q_HIGH = "High";
	public static final String Q_LOW = "Low";
	public static final String Q_OPEN = "openPrice";
	public static final String Q_52W_HIGH = "yearlyHighPrice";
	public static final String Q_52W_LOW = "yearlyLowPrice";
	public static final String Q_TRADE_VOLUME = "TradeVolume";
	public static final String Q_PRECISION = "DecimalPrecision";

	/* For Security info aka market data */
	public static final String M_SCRIPT_CODE = "SrchFor";
	public static final String M_LOW_CIR_LIMIT = "LCircuitLimit";
	public static final String M_UPP_CIR_LIMIT = "HCircuitLimit";
	public static final String M_OPENINTEREST = "openinterest";
	public static final String M_OPENINTEREST_CHANGE = "Perchgopeninterest";
	public static final String M_SPOT_PRICE = "Spotprc";
	public static final String M_BOARD_LOT_QTY = "Blq";
	public static final String M_DELIVERY_END_DATE = "DeliveryEndDate";
	public static final String M_DELIVERY_START_DATE = "DeliveryStartDate";
	public static final String M_DELIVERY_UNITS = "DeliveryUnits";
	public static final String M_EXPIRY_DATE = "ExpiryDte";
	public static final String M_ISSUE_MATURITY_DATE = "IsuMaturityDate";
	public static final String M_ISSUE_START_DATE = "IsuStartDate";
	public static final String M_LAST_TRADING_DATE = "LastTradingDate";
	public static final String M_MARKET_TYPE = "MarketType";
	public static final String M_MAX_ORDER_SIZE = "MaxOrderSize";
	public static final String M_PRICE_QUOTATION = "PriceQuatation";
	public static final String M_PRICE_UNITS = "PriceUnits";
	public static final String M_QUANTITY_UNITS = "QuantityUnit";
	public static final String M_OTHER_BUY_MARGIN = "OtherBuyMargin";
	public static final String M_OTHER_SELL_MARGIN = "OtherSellMargin";
	public static final String M_TENDOR_PERIOD_START_DATE = "TenderPeriodStart";
	public static final String M_TENDOR_PERIOD_END_DATE = "TenderPeriodEnd";
	public static final String M_BUY_CARRYING_COST = "BuyCryCost";
	public static final String M_SELL_CARRYING_COST = "SellCryCost";
	public static final String M_CREDIT_RATING = "CreditRating";


	/* For Show Market Pic aka market depth */
	public static final String MD_SYMBOL = "Symbol";
	public static final String MD_BEST_BUY_PRICE_1 = "BPrice1";
	public static final String MD_BEST_BUY_PRICE_2 = "BPrice2";
	public static final String MD_BEST_BUY_PRICE_3 = "BPrice3";
	public static final String MD_BEST_BUY_PRICE_4 = "BPrice4";
	public static final String MD_BEST_BUY_PRICE_5 = "BPrice5";

	public static final String MD_BEST_SELL_PRICE_1 = "SPrice1";
	public static final String MD_BEST_SELL_PRICE_2 = "SPrice2";
	public static final String MD_BEST_SELL_PRICE_3 = "SPrice3";
	public static final String MD_BEST_SELL_PRICE_4 = "SPrice4";
	public static final String MD_BEST_SELL_PRICE_5 = "SPrice5";

	public static final String MD_BEST_BUY_QTY_1 = "BQty1";
	public static final String MD_BEST_BUY_QTY_2 = "BQty2";
	public static final String MD_BEST_BUY_QTY_3 = "BQty3";
	public static final String MD_BEST_BUY_QTY_4 = "BQty4";
	public static final String MD_BEST_BUY_QTY_5 = "BQty5";

	public static final String MD_BEST_SELL_QTY_1 = "SQty1";
	public static final String MD_BEST_SELL_QTY_2 = "SQty2";
	public static final String MD_BEST_SELL_QTY_3 = "SQty3";
	public static final String MD_BEST_SELL_QTY_4 = "SQty4";
	public static final String MD_BEST_SELL_QTY_5 = "SQty5";

	public static final String MD_TRADING_SYMBOL = "TradingSymbol";
	public static final String MD_LTP = "Ltp";
	public static final String MD_LTT = "lasttradedtime";
	public static final String MD_DESCRIPTION = "companyname";
	public static final String MD_CHANGE = "abschange";
	public static final String MD_CHANGEPER_PER = "PerChange";
	public static final String MD_SPOT_PRICE = "spotprice";

	/* For Login - Get Initial Key */
	public static final String GIK_PUBLIC_KEY = "publicKey";
	public static final String GIK_TOMCAT_COUNT = "tomcatCount";

	/* Watchlist constants */
	public static final String W_SYMBOL = "Symbol";
	public static final String MARKETWATCH_NAME = "MWname";
	public static final String MARKETWATCH_NAME1 = "MWName";
	public static final String VALUES = "values";
	public static final String INDEX_VALUES = "indexvalues";
	public static final String SCRIP_COUNT = "ScripCount";
	public static final String MW_SCRIP_LIST = "sMwScripList";
	public static final String GROUP_LEG3 = "sGroupLeg3";
	public static final String MW_STRING = "mWString";

	/* For Pre Auth Key */
	public static final String PAK_PUBLICKEY_3 = "publicKey3";

	/* For Valid Pwd aka PASSWORD AUTHENTICATION */
	public static final String VAL_PWD_HASHED_PWD = "pwd";
	public static final String VAL_PWD_FTL = "ftl";
	public static final String VAL_PWD_APK = "apk";
	public static final String VAL_PWD_IMEI = "Imei";
	public static final String VAL_PWD_DEVICE = "loginDevice";
	public static final String VAL_PWD_SOURCE = "Source";
	public static final String VAL_PWD_QUEST_COUNT = "scount";
	public static final String VAL_PWD_INDICES = "sIndex";
	public static final String VAL_PWD_2FA_RESET_FLAG = "2faResetFlag";

	public static final String SAVE_ANS_QA = "qa";
	public static final String VAL_ANS_COUNT = "Count";
	public static final String VAL_ANS_IS = "is";
	public static final String VAL_ANS_AS = "as";
	public static final String VAL_ANS_TIME = "i_ActiveTime";

	public static final String VAL_ANS_TOKEN = "sUserToken";
	public static final String VAL_ANS_SESSION = "UserSessionID";
	public static final String VAL_ANS_IS_RESET = "sPasswordReset";

	public static final String DEF_LOGIN_IMEI = "imei";
	public static final String DEF_ACC_ID = "sAccountId";
	public static final String DEF_ACC_NAME = "accountName";
	public static final String DEF_BROKER = "brkname";
	public static final String DEF_BRANCH = "brnchid";
	public static final String DEF_EXCHANGES = "exarr";
	public static final String DEF_PRODUCTS = "prarr";
	public static final String DEF_ORDER_TYPES = "orarr";
	public static final String DEF_MKT_WATCH = "dmw";
	public static final String DEF_PROD_ALIAS = "s_prdt_ali";
	public static final String DEF_WEBLINK = "Weblink";

	/* For change password */
	public static final String CP_OLD_PWD = "sOldPwd";
	public static final String CP_NEW_PWD = "sNewPwd";
	public static final String CP_OLD_TX_PWD = "sOldTPwd";
	public static final String CP_NEW_TX_PWD = "sNewTPwd";
	public static final String CP_IS_TX = "sTxFlag";

	public static final String LIST = "list";


	/* Positions & Holdings */
	public static final String PH_ACCOUNT_ID = "acctid";
	public static final String PH_BROKER_NAME = "brkname";
	public static final String PH_PRODUCT_ALIAS = "s_prdt_ali";
	public static final String PH_SYMBOL = "symbol";
	public static final String PH_EXCHANGE = "exch";
	public static final String HOLDING_VALUE = "HoldingVal";
	public static final String TOTAL_VALUE = "Totalval";
	public static final String CLIENT_ID = "clientid";
	public static final String P_ACCOUNT_ID = "actid";
	public static final String TYPE = "type";
	public static final String PCODE = "Pcode";
	public static final String P_ORDER_SOURCE = "orderSource";

	/* Span Calculator */
	public static final String SP_SYMBOL = "symbol";
	public static final String SP_EXCHANGE = "exch";
	public static final String SP_NET_QUANTITY = "netQty";
	public static final String TOTAL_REQUIREMENT = "totalRequirement";
	public static final String SPREAD_BENEFIT = "spreadBenefit";
	public static final String EXPOSURE_MARGIN = "exposureMarginPrst";
	public static final String SPAN_REQUIREMENT = "spanRequirement";
	public static final String ERROR_MESSAGE3 = "emsg";
	public static final String SP_BUY_QUANTITY = "buyQty";
	public static final String SP_SELL_QUANTITY = "sellQty";

	/*Position Conversion & SquareOff */
	public static final String PC_ACCOUNT_ID = "actid";
	public static final String BRANCH_ID = "branchid";
	public static final String BROKER_NAME = "brokname";
	public static final String PRODUCT_TO_CODE = "Ptocode";
	public static final String PC_QUANTITY = "Qty";
	public static final String NET_AMOUNT = "Netamt";
	public static final String PC_TRANSACTION_TYPE = "Transtype";
	public static final String PC_TOKEN = "Token";
	public static final String NET_QUANTITY = "Netqty";
	public static final String BUY_AVERAGE_PRICE = "Buyavgprice";
	public static final String SELL_AVERAGE_PRICE = "Sellavgprice";
	public static final String PC_TYPE = "Type";
	public static final String BOARD_LOT_FLAG = "BdLtFlag";
	public static final String BOARD_LOT_QUANTITY = "BodLot";
	public static final String EXCHANGE_SEGMENT = "Exchangeseg";

	/* Forgot password keys */
	public static final String FP_EMAIL = "email";
	public static final String FP_PAN_NUMBER = "pan";
	public static final String FP_DOB = "dob";

	/* Positions Response API keys */
	public static final String P_OPTION_TYPE = "Opttype";
	public static final String P_MARK_TO_MARKET = "MtoM";
	public static final String P_CARRY_FORWARD_BUY_QUANTITY = "CFbuyqty";
	public static final String P_CARRY_FORWARD_SELL_QUANTITY = "CFsellqty";
	public static final String P_BUY_QUANTITY = "Bqty";
	public static final String P_SELL_QUANTITY = "Sqty";
	public static final String P_BUY_AVERAGE_PRICE = "Buyavgprc";
	public static final String P_CARRY_FORWARD_BUY_AVERAGE_PRICE = "CFBuyavgprc";
	public static final String P_SELL_AVERAGE_PRICE = "Sellavgprc";
	public static final String P_CARRY_FORWARD_SELL_AVERAGE_PRICE = "CFSellavgprc";
	public static final String P_UNREALISED_PROFIT_LOSS = "unrealisedprofitloss";
	public static final String P_SQUARE_OFF_FLAG = "sSqrflg";
	public static final String P_POSITION_FLAG = "posflag";
	public static final String P_NET_QUANTITY_POSITION_CONVERSION_FLAG = "s_NetQtyPosConv";
	public static final String P_INSTRUMENT_TYPE = "Instname";
	public static final String P_TOKEN = "Token";
	public static final String P_LAST_TRADED_PRICE = "LTP";
	public static final String P_STRIKE_PRICE = "Stikeprc";
	public static final String P_BOD_LOT_QUANTITY = "BLQty";
	public static final String P_NET_BUY_AVERAGE_PRICE = "NetBuyavgprc";
	public static final String P_NET_SELL_AVERAGE_PRICE = "NetSellavgprc";
	public static final String P_NET_QTY = "Netqty";
	public static final String P_BREAK_EVEN_PRICE = "BEP";

	/* Holdings Response API keys */
	public static final String H_TOKEN_ONE = "Token1";
	public static final String H_TOKEN_TWO = "Token2";
	public static final String H_NSE_TRADING_SYMBOL = "Nsetsym";
	public static final String H_BSE_TRADING_SYMBOL = "Bsetsym";
	public static final String H_HOLDING_QUANTITY = "Holdqty";
	public static final String H_HOLDING_UPDATE_QUANTITY = "HUqty";
	public static final String H_WITHHELD_HOLDING_QUANTITY = "WHqty";
	public static final String H_BTST_QUANTITY = "Btst";
	public static final String H_SELLABLE_QUANTITY = "SellableQty";
	public static final String H_ISIN_ONE = "isin1";
	public static final String H_ISIN_TWO = "isin2";
	public static final String H_HAIRCUT = "Haircut";
	public static final String H_LAST_TRADED_PRICE = "Ltp";

	/* Samco API Request API Keys */
	public static final String SAMCO_API = "api";
	public static final String SAMCO_CLIENTID = "client_id";
	public static final String SAMCO_MERCHANT_ID = "merchant_id";
	public static final String SAMCO_MERCHANT_PASSWORD = "merchant_pass";
	public static final String SAMCO_TOKEN = "token";
	public static final String SAMCO_TO_DATE = "to_date";
	public static final String SAMCO_EXCHANGE_SEGMENT = "ex_segment";

	public static final String GOOGLE_ERROR = "error_description";
	public static final String GOOGLE_NAME = "name";
	public static final String GOOGLE_ID = "sub";

	public static final String FB_ACCCESS_TOKEN = "access_token";
	public static final String FB_IS_VALID = "is_valid";
	public static final String FB_USER_ID = "user_id";
	public static final String FB_DATA = "data";

	public static final String TB_AVERAGE_PRICE = "AvgPrice";
	public static final String TB_SCRIP_CODE = "Symbol";

	/* Bracket order Request keys */
	public static final String BO_LTP_OR_ATP = "ltpOratp";
	public static final String BO_SQROFF_TYPE = "SqrOffAbsOrticks";
	public static final String BO_SQROFF_VAL = "SqrOffvalue";
	public static final String BO_STOPLOSS_TYPE = "SLAbsOrticks";
	public static final String BO_STOPLOSS_VAL = "SLvalue";
	public static final String BO_TRAILING_SL = "trailingSL";
	public static final String BO_TRAILING_SL_VAL = "tSLticks";
	public static final String BO_VENDOR = "naicCode";
	public static final String BO_ORDER_SOURCE = "orderSource";
	public static final String BO_WEB_OR_MOB = "userTag";


	public static final String MCX_EXCHANGE = "MCX";

	/* For payin and payout */
	public static final String URL = "url";
	public static final String SECURE = "Secure";
	public static final String NAME = "name";
	public static final String OM_TOKEN = "SessionToken";

	/* History Trade */
	public static final String ROWS = "rows";
	public static final String TOTAL_ROWS = "total_rows";
	public static final String SCRIP_NAME = "scrip_name";
	public static final String ORDER_NO_HISTORY = "order_no";
	public static final String TRADE_DATE = "trade_date";
	public static final String TRADE_TIME = "trade_time";
	public static final String SERIES = "series";
	public static final String OPTION_TYPE = "option_type";
	public static final String STRIKE_PRICE = "strike_price";
	public static final String EXPIRY_DATE_HISTORY = "expiry_date";
	public static final String BUY_QTY = "buy_qty";
	public static final String BUY_PRICE = "buy_price";
	public static final String SELL_QTY = "sell_qty";
	public static final String SELL_PRICE = "sell_price";
	public static final String SEGMENT = "segment";
	public static final String TRADE_NO = "trade_no";

	/* User Details */
	public static final String UD_REQ_ACC = "acctId";
	public static final String UD_EMAIL = "emailAddr";
	public static final String UD_MOBILE = "cellAddr";
	public static final String UD_ACC_TYPE = "accountType";
	public static final String UD_ACC_ID = "accountId";
	public static final String UD_DP_NAME = "dpName";
	public static final String UD_PAN_NO = "panNo";
	public static final String UD_DOB = "dobAccount";
	public static final String UD_NAME = "accountName";

	public static final String EM_EXCHANGE = "Exch";

	public static final String SCRIPT_LIST = "scripList";
	public static final String QUESTIONS = "sQuestions";
	public static final String FILL_QTY = "Fillshares";
}
