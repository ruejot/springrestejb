package org.wildfly.demo.service;

public interface MOrderMTEARServiceInterface {
	
	public String getEJBReloadMemoryCenter(final String destination) throws Exception;
	
	public String getEJBQueryMemoryCenter(final String destination) throws Exception;
}
