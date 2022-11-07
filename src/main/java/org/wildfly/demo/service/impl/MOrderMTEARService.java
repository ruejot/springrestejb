package org.wildfly.demo.service.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.wildfly.demo.service.MOrderMTEARServiceInterface;

import com.cht.morder.ejb.bm.batch.BatchSessionHome;
import com.cht.morder.ejb.bm.querymgr.QuerySessionHome;
import com.cht.morder.ejb.sys.systableutil.SystableUtil;
import com.cht.morder.ejb.sys.systableutil.SystableUtilHome;

import bms.ejbUtil;

@Service
public class MOrderMTEARService implements MOrderMTEARServiceInterface{
	private static String testPath = "localhost:8080";
	
	@Override
	public String getEJBReloadMemoryCenter(String destination) throws Exception {
		String IP = "remote+http://" + testPath;
		SystableUtil memS = (SystableUtil) getEjb(IP, "MemS");
		memS.reloadTable("mordersysparameter", "code");
		System.out.println("---------------------------------------------------");
		String content = memS.getEJBQueryResult("mordersysparameter", "code", "testMT5");
		System.out.println(content);
		return content;
	}

	@Override
	public String getEJBQueryMemoryCenter(String destination) throws Exception {
		String IP = "remote+http://" + testPath;
//      String IP = "t3://" + testPath;//weblogic
		SystableUtil memS = (SystableUtil) getEjb(IP, "MemS");
//      memS.preloadMemoryForTimer();//若查詢沒資料，請先執行此行preload
		ArrayList ret = memS.reflashTablenameItems_EJB();
		System.out.println(ret.toString());		
		return ret.toString();
	}

	private static Object getEjb(String IP, String ejbName) throws Exception {
		Object ejbObject = null;
		ejbUtil ejb = new ejbUtil();
		ejb.setEjbUrl(IP);

		// --批次EJB
		if (ejbName.equals("BS")) {
			ejb.setEjbJndi("com.cht.morder.ejb.jndi.bm.batch.BatchSessionBean");
			ejb.setEjbHomeClass(BatchSessionHome.class);
			ejbObject = ((BatchSessionHome) ejb.getEjbHome()).create();
		} else if (ejbName.equals("QS")) {
			ejb.setEjbJndi("com.cht.morder.ejb.jndi.bm.querymgr.QuerySessionBean");
			ejb.setEjbHomeClass(QuerySessionHome.class);
			ejbObject = ((QuerySessionHome) ejb.getEjbHome()).create();
		} else if (ejbName.equals("MemS")) {
			ejb.setEjbJndi("com.cht.morder.ejb.jndi.MOrderEAR.SystableUtilBean");
//        	ejb.setEjbJndi("ejb:MOrderMTEAR-1.0.0/MOrderMT-1.0.0/SystableUtilBean!com.cht.morder.ejb.sys.systableutil.SystableUtil");
			ejb.setEjbHomeClass(SystableUtilHome.class);
//            ejbObject = ((SystableUtilHome) ejb.getEjbHome()).create();
			ejbObject = ejb.getEjbObject();
		}
		return ejbObject;
	}

}
