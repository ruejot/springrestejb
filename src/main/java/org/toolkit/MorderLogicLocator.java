package org.toolkit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.NamingException;

public class MorderLogicLocator {

	private String providerUrl	;
	private EJbNameToWildflyJndi parser ; 
	public boolean isWildfly() {
		if(this.providerUrl!=null && this.providerUrl.contains("t3")) {
			//weblogic
			return false;
		}else if(this.providerUrl!=null && this.providerUrl.contains("remote+http")) {
			//wildfly
			return true;
		}		
		return true;
	}

	public MorderLogicLocator() {
		this.providerUrl = "t3://localhost:7001";
//		this.providerUrl = "remote+http://localhost:8080";
		this.parser = new EJbNameToWildflyJndi();
	}
	public MorderLogicLocator(final String providerUrl) {
		this.providerUrl = providerUrl ; 
		this.parser = new EJbNameToWildflyJndi();
	}
	public <O extends EJBObject> O getEJBByName(final String jndiName)
			throws ClassNotFoundException, NamingException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, CreateException {
		O result = null;
		
//		final String implName =jndiName.replace("com.cht.morder.ejb.jndi.", "com.cht.morder.ejb.");
		final String implName = WeblogicJNDIMapping.getWeblogicJndiMapping() .get(jndiName);
		
		final Class<?> beanClass = Class.forName(implName);

		final Class<? extends EJBHome> ejbHomeClass = (Class<? extends EJBHome>) Class.forName(implName.replace("Bean", "Home") );

		final String ejbObjectName = beanClass.getPackage().getName() + "."
				+ beanClass.getSimpleName().replace("Bean", "");

		final Class<? extends EJBObject> ejbObjectClass = (Class<? extends EJBObject>) Class.forName(ejbObjectName);

		GenericsServiceLocator locator = new GenericsServiceLocator();
		if (isWildfly()) {
			final Context context = locator.createContextForWildfly(this.providerUrl);
			final String wildflyJndiName = this.parser.retrieveName(this.providerUrl ,beanClass ,ejbObjectClass);
			result = locator.lookupByEJB3Style(context,wildflyJndiName);
		} else {
			final Context context = locator.createContextForWeblogic(this.providerUrl);
			result = locator.lookupByEJB2Style(context, jndiName, ejbHomeClass);
		}
		return result;
	}

	

}
