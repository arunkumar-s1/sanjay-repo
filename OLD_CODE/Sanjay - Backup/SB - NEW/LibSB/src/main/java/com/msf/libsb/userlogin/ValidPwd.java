package com.msf.libsb.userlogin;

import org.json.me.JSONObject;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.connection.DBPool;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.loginhelpers.Reset2FARequest;
import com.msf.libsb.loginhelpers.Reset2FAResponse;
import com.msf.libsb.loginhelpers.TwoFaHelper;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;

public class ValidPwd
{
	private JSONObject jDefaultLoginDetails = null;
	private String sPublicKey4 = "";

	public ValidPwd(DBPool pool, String sUID, String sPassword, String jsession, String jKey, String sPan,
			String sEmail, JSONObject dataObject) throws Exception 
	{
		String sValPwdURL = AppConfig.getConfigValue("valid_pwd_url");
		ValidPwdRequest validPwdReq = new ValidPwdRequest(sValPwdURL);
		validPwdReq.setSession(jsession);
		validPwdReq.setKey(jKey);
		validPwdReq.setUserID(sUID);
		validPwdReq.setPwd(SamcoHelper.hashPassword(sPassword));
		validPwdReq.setFirstLogin("N");
		validPwdReq.setAPK(dataObject.getString("apkVersion"));
		validPwdReq.setIMEI("NA");
		validPwdReq.setSource("MOB");

		ValidPwdResponse validPwdRes = (ValidPwdResponse) validPwdReq.postRequest();

		String sIndex = null;
		int nCount = 0;
		String sTData = null;
		if (validPwdRes.isSuccessResponse()) 
		{
			sIndex = validPwdRes.getIndex();
			nCount = validPwdRes.getQuestCount();
			sTData = validPwdRes.getTData();

			if (sTData.equalsIgnoreCase("Please Register the Questions")) 
			{
				//log.debug("First time log in process to follow");
				//--String[] sIndexList = sIndex.split("\\|");
				//log.debug("Index list length :: " + sIndexList.length);
				TwoFaHelper twoFaHelper = new TwoFaHelper(pool, sUID);
				twoFaHelper.setActualIndex(sIndex);
				twoFaHelper.setActualQuestions(validPwdRes.getQuestions());

				twoFaHelper.updateQuestionsInDB();

				// throw special info msg so device will send get2fa questions
				// -> save 2fa ans -> then login
				throw new SamcoException(INFO_IDS.SAVE_2FA);
			}

			if (sTData.equalsIgnoreCase("Answer the questions"))
			{
				String sCount = Integer.toString(nCount);
				String sAnsURL = AppConfig.getConfigValue("ans_validation_url");
				ValidAnsRequest valAnsRequest = new ValidAnsRequest(sAnsURL);
				valAnsRequest.setSession(jsession);
				valAnsRequest.setKey(jKey);

				TwoFaHelper twoFaHelper = new TwoFaHelper(pool, sUID);
				twoFaHelper.setActualIndex(sIndex);
				String sIndQ = null;
				String sAnsQ = null;

				try 
				{
					sIndQ = twoFaHelper.getQuestionIndex();
					sAnsQ = twoFaHelper.getAnswersFromDB();
				} 
				catch (SamcoException e)
				{
					if (e.getErrorCode().equals(INFO_IDS.SAVE_2FA_ALREADY_SET))
					{
						//log.debug("WARNING : Automatically resetting 2FA's for :" + sUID);
						/* if the user has already set tfa answer from somewhere else, resetting 2fa automatically 
						 * and resending validPwd request
						 */

						String sURL = AppConfig.getConfigValue("reset_2fa_url");
						Reset2FARequest resetReq = new Reset2FARequest(sURL);
						resetReq.setSession(jsession);
						resetReq.setKey(jKey);
						resetReq.setUserID(sUID);
						resetReq.setEmail(sEmail);
						resetReq.setPan(sPan);

						Reset2FAResponse resetRes = (Reset2FAResponse) resetReq.postRequest();
						if (resetRes.isSuccessResponse()) 
						{
							validPwdRes = (ValidPwdResponse) validPwdReq.postRequest();
							if (validPwdRes.isSuccessResponse()) 
							{
								sIndex = validPwdRes.getIndex();
								nCount = validPwdRes.getQuestCount();
								sTData = validPwdRes.getTData();
								//--s2FAResetFlag = validPwdRes.get2FAResetFlag();
								if (sTData.equalsIgnoreCase("Please Register the Questions"))
								{
									//log.debug("First time log in process to follow");
									//--String[] sIndexList = sIndex.split("\\|");
									//log.debug("Index list length :: " + sIndexList.length);

									twoFaHelper.setActualIndex(sIndex);
									twoFaHelper.setActualQuestions(validPwdRes.getQuestions());

									twoFaHelper.updateQuestionsInDB();

									// throw special info msg so device will
									// send get2fa questions -> save 2fa ans ->
									// then login
									throw new SamcoException(INFO_IDS.SAVE_2FA);
								}

							} 
							else
							{
								throw new SamcoException(resetRes.getErrorID());
							}
						} 
						else
						{
							throw new SamcoException(resetRes.getErrorID());
						}
					} 
					else
						throw e;
				}

				/* If  2fa answers are available in middleware db, proceeding normally */

				/* Request parameters */
				valAnsRequest.setUserID(sUID);
				valAnsRequest.setCount(sCount);
				valAnsRequest.setIS(sIndQ);
				valAnsRequest.setAS(sAnsQ);
				valAnsRequest.setTime("86400");

				ValidAnsResponse valAnsResp = (ValidAnsResponse) valAnsRequest.postRequest();
				if (valAnsResp.isSuccessResponse()) 
				{
					if (valAnsResp.getTData().equalsIgnoreCase("Invalid Answers")) 
					{
						throw new SamcoException(INFO_IDS.INVALID_DOB);
					}
					String sDecodedPublicKeyFour = valAnsResp.getToken();
					//--String sToken = valAnsResp.getSession();
					String sPasswordResetFlag = valAnsResp.getPwdRest();

					/* BEGIN OF VALIDANS */

					/* DEFAULT LOGIN BEGIN */
					String sDefLogURL = AppConfig.getConfigValue("default_login_url");
					DefaultLoginRequest defLoginReq = new DefaultLoginRequest(sDefLogURL);
					defLoginReq.setSession(jsession);
					sPublicKey4 = SamcoHelper.hashData(sDecodedPublicKeyFour);
					defLoginReq.setKey(sPublicKey4);

					/* Request Parameters */
					defLoginReq.setUserID(sUID);
					defLoginReq.setIMEI("0000");

					DefaultLoginResponse defLoginRes = (DefaultLoginResponse) defLoginReq.postRequest();

					if (defLoginRes.isSuccessResponse()) {
						jDefaultLoginDetails = new JSONObject();
						jDefaultLoginDetails.put(APIConstants.DEF_ACC_ID, defLoginRes.getAccountID());
						jDefaultLoginDetails.put(APIConstants.DEF_ACC_NAME, defLoginRes.getAccountName());
						jDefaultLoginDetails.put(APIConstants.DEF_BRANCH, defLoginRes.getBranch());
						jDefaultLoginDetails.put(APIConstants.DEF_BROKER, defLoginRes.getBroker());
						jDefaultLoginDetails.put(APIConstants.DEF_EXCHANGES, defLoginRes.getExcArray());
						jDefaultLoginDetails.put(APIConstants.DEF_PRODUCTS, defLoginRes.getProductArray());
						jDefaultLoginDetails.put(APIConstants.DEF_ORDER_TYPES, defLoginRes.getOrderTypeArray());
						//--jDefaultLoginDetails.put(APIConstants.DEF_MKT_WATCH, defLoginRes.getDefMktWatch());
						jDefaultLoginDetails.put(APIConstants.DEF_PROD_ALIAS, defLoginRes.getProductAlias());
						jDefaultLoginDetails.put(APIConstants.VAL_ANS_IS_RESET, sPasswordResetFlag);
						jDefaultLoginDetails.put(APIConstants.DEF_WEBLINK, defLoginRes.getWebLink());

					}
					else
					{
						throw new SamcoException(defLoginRes.getErrorID());
					}
				}
				else
				{
					throw new SamcoException(valAnsResp.getErrorID());
				}
			} 
			else if (nCount == 0) 
			{
				throw new SamcoException(INFO_IDS.LOGIN_FAILED);
			} 
			else 
			{
				throw new SamcoException(validPwdRes.getErrorID());
			}
		} 
		else 
		{
			throw new SamcoException(validPwdRes.getErrorID());
		}
	}

	public JSONObject getUserDetails() 
	{
		return jDefaultLoginDetails;
	}

	public String getPublicKey4()
	{
		return this.sPublicKey4;
	}
}
