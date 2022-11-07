package org.toolkit;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.SECURITY_CREDENTIALS;
import static javax.naming.Context.SECURITY_PRINCIPAL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class GenericsServiceLocator {
	
	
	@SuppressWarnings("unchecked")
	public <T> T lookupByEJB3Style(final Context context, final String lookUpName) throws NamingException {
		
		System.out.println("lookUpName: " + lookUpName);
		return (T) context.lookup(lookUpName);
	}

	@SuppressWarnings("unchecked")
	public <T extends EJBObject, h extends EJBHome> T lookupByEJB2Style(final Context context, final String lookUpName,
			final Class<h> homeClass ) throws NamingException, CreateException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	
		final Object obj = context.lookup(lookUpName);
		final h home = (h) javax.rmi.PortableRemoteObject.narrow(obj, homeClass); // EJB2 caller style
		final Method method = homeClass.getDeclaredMethod("create");
		final Object ejbOject = method.invoke(home);
		return (T) ejbOject;
	}

	public  void closeContext(final Context context) throws NamingException {
		if (context != null) {
			context.close();
		}
	}
	public Context createContextForWildfly(final  String  providerUrl) throws NamingException {
    	final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        //use HTTP upgrade, an initial upgrade requests is sent to upgrade to the remoting protocol
        
        
		if (providerUrl == null || providerUrl.length() == 0) {
			jndiProperties.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
		} else {
			jndiProperties.put(Context.PROVIDER_URL, providerUrl);
		}	 
        
//      jndiProperties.put(SECURITY_PRINCIPAL, "admin");
//		jndiProperties.put(SECURITY_CREDENTIALS, "secret123!");     
        
        return new InitialContext(jndiProperties);
    } 
	
	public Context createContextForWeblogic(final  String  providerUrl ) throws NamingException {
    	final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory"); 
        
		if (providerUrl == null || providerUrl.length() == 0) {
			jndiProperties.put(Context.PROVIDER_URL, "t3://localhost:7001");
		} else {
			jndiProperties.put(Context.PROVIDER_URL, providerUrl);
		}		
        jndiProperties.put(SECURITY_PRINCIPAL, "admin");
		jndiProperties.put(SECURITY_CREDENTIALS, "!admin123");
        return new InitialContext(jndiProperties);
    }
}
