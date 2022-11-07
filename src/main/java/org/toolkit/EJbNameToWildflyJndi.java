package org.toolkit;

import javax.ejb.EJBObject;

public class EJbNameToWildflyJndi {
	/**
	 * 針對於地端或是cloud上不同的服務,而wildfly則有不同形式的EJB JNDI Name
	 * ***/
	public String retrieveName(final String providerUrl,final Class<?> beanClass ,final Class<? extends EJBObject> ejbObjectClass) {
		final String ejbImplName = 	beanClass.getSimpleName();
		if(providerUrl.contains("localhost")) {
			switch(ejbImplName) {
				case "WebSessionBean":
					return "ejb:/morder-wildfly-server-basic/WebSessionBean!com.cht.morder.ejb.bm.webinterface.WebSession";
				case "SystableUtilBean":
//					return "ejb:MOrderMTEAR-1.0.0/MOrderMT-1.0.0/SystableUtilBean!com.cht.morder.ejb.sys.systableutil.SystableUtil";
					return "ejb:MOrderMTEAR-1.0.0/com.cht.morder-MOrderMT-1.0.0/SystableUtilBean!com.cht.morder.ejb.sys.systableutil.SystableUtil";
			}
		}
		return null;
	}
}
