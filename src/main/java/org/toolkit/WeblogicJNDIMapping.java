package org.toolkit;

import java.util.HashMap;
import java.util.Map;

public class WeblogicJNDIMapping {
	private static final Map<String,String> result = new HashMap<String,String>();
	
	/***
	 * 原本以為規則是JNDI是com.cht.morder.ejb.jndi.XXXX.YYYBean ,實作class為com.cht.morder.ejb.XXXX.YYYBean<br/>
	 * 發現還是不少例外
	 * **/
	public static  Map<String,String>  getWeblogicJndiMapping() {		
		result.put("com.cht.morder.ejb.jndi.bm.webinterface.WebSessionBean", "com.cht.morder.ejb.bm.webinterface.WebSessionBean");
		result.put("com.cht.morder.ejb.jndi.MOrderEAR.SystableUtilBean", "com.cht.morder.ejb.sys.systableutil.SystableUtilBean");		
		return result ;
	}
}
