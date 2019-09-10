package com.msf.samco.services.loginhelp;

import java.util.HashMap;
import java.util.Map;

import org.json.me.JSONArray;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.connection.DBConnection;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.loginhelpers.Save2FaInDBRequest;
import com.msf.libsb.loginhelpers.Save2FaInDBResponse;
import com.msf.libsb.loginhelpers.TwoFaHelper;
import com.msf.libsb.userlogin.SaveAnsRequest;
import com.msf.libsb.userlogin.SaveAnsResponse;
import com.msf.libsb.utils.exception.InvalidRequestParameter;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.keygeneration.KeyGenerator;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class Save2FaAnswers extends BaseServlet 
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		String sUid = ssRequest.getFromData(APP_CONSTANT.USER_ID);
		JSONArray jAns = ssRequest.getArrayFromData(APP_CONSTANT.ANSWERS);

		if (jAns == null || jAns.length() < 5)
			throw new InvalidRequestParameter("");

		/* Save answers to DB */
		Save2FaInDBRequest saveReq = new Save2FaInDBRequest(DBConnection.getInstance());
		saveReq.setUser(sUid);
		saveReq.setAnswers(jAns);
		Save2FaInDBResponse saveRes = (Save2FaInDBResponse) saveReq.postRequest();

		/* GETTING JKEY AND JSESSION */
		KeyGenerator keyGen = new KeyGenerator(sUid);
		String jsession = keyGen.getJSession();
		String jKey = keyGen.getJKey();

		TwoFaHelper twoFaHelper = new TwoFaHelper(DBConnection.getInstance(), sUid);
		Map<String, String> mExistingAns = new HashMap<String, String>();

		if (twoFaHelper.checkAnswerInDb(mExistingAns)) 
		{
			StringBuilder sb = new StringBuilder();

			for (Map.Entry<String, String> entry : mExistingAns.entrySet())
			{
				sb.append(entry.getKey());
				sb.append("|");
				sb.append(entry.getValue());
				sb.append("|");
			}

			String sSaveAnsURL = AppConfig.getConfigValue("save_ans_url");
			SaveAnsRequest saveAnsReq = new SaveAnsRequest(sSaveAnsURL);
			saveAnsReq.setSession(jsession);
			saveAnsReq.setKey(jKey);

			/* Set request parameters */
			saveAnsReq.setUserID(sUid);
			saveAnsReq.setIndexAns(sb.toString());

			SaveAnsResponse savAnsRes = (SaveAnsResponse) saveAnsReq.postRequest();

			if (savAnsRes.isSuccessResponse()) 
			{
				ssResponse.addToData(APP_CONSTANT.MESSAGE, saveRes.getString(APP_CONSTANT.MESSAGE));
			} 
			else 
			{
				throw new SamcoException(savAnsRes.getErrorID());
			}
		}
	}

	@Override
	protected String getSvcName()
	{
		return "Save2FaAnswers";
	}

	@Override
	protected String getSvcGroup()
	{
		return "LoginHelp";
	}

	@Override
	protected String getSvcVersion() 
	{
		return "1.0.0";
	}
}
