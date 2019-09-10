package com.msf.samco.objects;

import com.msf.audit.AuditObject;
import com.msf.objects.MSFRequest;
import com.msf.objects.MSFResponse;

public class SSAuditObject extends AuditObject 
{

	public SSAuditObject(MSFRequest msfRequest, MSFResponse msfResponse) 
	{
		super(msfRequest, msfResponse);
	}

	private boolean needAudit = false;

	public boolean isNeedAudit() 
	{
		return needAudit;
	}

	public void setNeedAudit(boolean needAudit) 
	{
		this.needAudit = needAudit;
	}

}
