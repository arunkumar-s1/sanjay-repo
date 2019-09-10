package com.msf.libsb.userlogin;

import com.msf.libsb.base.BaseResponse;
import com.msf.libsb.constants.APIConstants;
import com.msf.libsb.constants.INFO_IDS;

public class ValidPwdResponse extends BaseResponse
{

	public ValidPwdResponse(String sAPIResponse) throws Exception 
	{
		super(sAPIResponse);
	}

	public String getIndex() throws Exception 
	{
		return this.getString(APIConstants.VAL_PWD_INDICES);
	}

	public int getQuestCount() throws Exception 
	{
		String sCount = this.getString(APIConstants.VAL_PWD_QUEST_COUNT);
		int iCount = Integer.parseInt(sCount);
		return iCount;
	}

	public String getTData() throws Exception
	{
		String sTData = this.getString(APIConstants.TDATA);
		return sTData;
	}

	public String get2FAResetFlag() throws Exception 
	{
		String s2FAResetFlag = this.getString(APIConstants.VAL_PWD_2FA_RESET_FLAG);
		return s2FAResetFlag;
	}

	public String getQuestions() throws Exception
	{
		return this.getString(APIConstants.QUESTIONS);
	}

	@Override
	public String getErrorID()
	{
		if (!this.isSuccess) 
		{
			if (this.errorMessage.equalsIgnoreCase("User Blocked Contact System Administrator"))
			{
				return INFO_IDS.USER_BLOCKED;
			} 

			else if(this.errorMessage.equalsIgnoreCase("Session Expired"))
			{
				return INFO_IDS.INVALID_SESSION;
			}

			else if(this.errorMessage.equalsIgnoreCase("Please Enter Valid Password"))
			{
				return INFO_IDS.INVALID_PASSWORD;
			}

			else
			{
				return INFO_IDS.LOGIN_FAILED;
			}
		}

		return INFO_IDS.SUCCESS;
	}
}
