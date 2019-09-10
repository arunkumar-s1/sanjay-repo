package com.msf.libsb.jmx;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.monitoring.jmx.MonitorRegistrySingleton;

public class Monitor {

	public static String MSF_DB = "";
	public static String TR_API = "";
	public static String ALERTS_API = "";

	public static void setServiceBeans() throws InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, MalformedObjectNameException, SamcoException 
	{

		// DataBase JMXBean
		MSF_DB = AppConfig.getConfigValue("jmx.prefix").concat(":type=MSF_DB");
		SamcoServiceBean dbBean = new SamcoServiceBean(MSF_DB);
		ObjectName objectName = new ObjectName(MSF_DB);
		MonitorRegistrySingleton.getInstance().register(objectName, dbBean, MSF_DB);

		// TR-API JMXBean
		TR_API = AppConfig.getConfigValue("jmx.prefix").concat(":type=TR_API");
		SamcoServiceBean trAPIBean = new SamcoServiceBean(TR_API);
		ObjectName ebAPIObjName = new ObjectName(TR_API);
		MonitorRegistrySingleton.getInstance().register(ebAPIObjName, trAPIBean, TR_API);

		// Alerts API JMXBean
		ALERTS_API = AppConfig.getConfigValue("jmx.prefix").concat(":type=ALERTS_API");
		SamcoServiceBean alertsAPIBean = new SamcoServiceBean(ALERTS_API);
		ObjectName alertsAPIObjName = new ObjectName(ALERTS_API);
		MonitorRegistrySingleton.getInstance().register(alertsAPIObjName, alertsAPIBean, ALERTS_API);
		
	}

	public static SamcoServiceBean getServiceBean(String ID) 
	{

		return (SamcoServiceBean) MonitorRegistrySingleton.getInstance().getMXBean(ID);

	}

	public static void release() throws MBeanRegistrationException, InstanceNotFoundException 
	{

		MonitorRegistrySingleton.getInstance().unRegisterAll();
	}

}
