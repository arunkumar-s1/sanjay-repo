package com.msf.libsb.jmx;

import java.util.Vector;

import com.msf.log.Logger;
import com.msf.monitoring.jmx.beans.PerfItem;
import com.msf.monitoring.jmx.beans.ServiceBean;

public class SamcoServiceBean extends ServiceBean
{

	private static Logger log = Logger.getLogger(SamcoServiceBean.class);

	private int critical_value = 8;
	private int warning_value = 5;

	public SamcoServiceBean(String serviceName) 
	{

		super(serviceName);
	}

	public void setCriticalValue(int value)
	{

		this.critical_value = value;
	}

	public void setWarningValue(int value)
	{

		this.warning_value = value;
	}

	public synchronized int getStatus()
	{

		return nFailure;
	}

	public synchronized String getInfo()
	{

		lastSyncTime = getCurrentDate();

		String info = " - ";

		info += nFailure + " failed ";

		if (failureMsg.length() > 0)
			info += "(" + failureMsg + ")";

		info += " - " + lastSyncTime;

		Vector<PerfItem> perfItems = new Vector<PerfItem>();

		// Failure
		PerfItem failure = new PerfItem();
		failure.setLabel(nFailureLabel);
		failure.setValue(nFailure);
		failure.setWarn(warning_value);
		failure.setCritical(critical_value);

		perfItems.add(failure);

		String perfData = getNagioPerf(perfItems);

		if (perfData.length() > 0)
		{
			// Performance data
			info += " |";
			info += perfData;
		}

		log.debug("JMX Nagios " + serviceName + " - " + info);

		//6 failed (Communications link failure Last packet sent to the server was 0 ms ago.) - 2016/10/31 21:26:08 PM |'failure'=6.0;4.0;10.0;;;

		nFailure = 0;
		failureMsg = "";

		return info;
	}
}
