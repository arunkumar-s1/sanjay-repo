package com.msf.samco.services.loginhelp;

import org.json.me.JSONArray;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.constants.APP_CONSTANT;
import com.msf.libsb.constants.INFO_IDS;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.samco.objects.SSRequest;
import com.msf.samco.objects.SSResponse;
import com.msf.samco.servlet.BaseServlet;

public class GetSEBIRegNumber extends BaseServlet 
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(SSRequest ssRequest, SSResponse ssResponse) throws Exception 
	{
		String sConfigRegs = AppConfig.getConfigValue("sebi_registration_number");
		if (sConfigRegs == null || sConfigRegs.length() == 0)
			throw new SamcoException(INFO_IDS.NO_DATA_FOUND);

		JSONArray jArr = new JSONArray(sConfigRegs);
		ssResponse.addToData(APP_CONSTANT.SEBI_REG, jArr);
	}

	@Override
	protected String getSvcName() 
	{
		return "GetSEBIRegNumber";
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
