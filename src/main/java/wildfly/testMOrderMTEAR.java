/**
 * 
 */
package wildfly;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MultiHashMap;

import com.cht.morder.ejb.bm.batch.BatchSessionHome;
import com.cht.morder.ejb.bm.querymgr.QuerySessionHome;
import com.cht.morder.ejb.sys.systableutil.SystableUtil;
import com.cht.morder.ejb.sys.systableutil.SystableUtilHome;

import bms.ejbUtil;

/**
 * @author dan
 *
 */
public class testMOrderMTEAR {
//    private static String testPath = "10.197.11.71:8050";
	private static String testPath = "localhost:8080";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			/** 測試http網頁重載記憶體內容 */
//          testReloadQueryMemoryCenter();

			/** 測試http網頁查詢記憶體內容 */
			/** 若不重載memory table為空 */
//          testQueryMemoryCenter("mempreloaddef","tablename");
//			testQueryMemoryCenter("mordersysparameter", "code");
//          testQueryMemoryCenter("applychannel","productid");

			/** 測試ejb重載 */
          testEJBReloadMemoryCenter();

			/** 測試ejb撈取記憶體資訊 */
			/** 若不進行ejb重載，則memory輸出為空 */
//          testEJBQueryMemoryCenter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testQueryMemoryCenter(String tablename, String keyfield) throws Exception {
		URL url = new URL("http://" + testPath + "/ORMemoryCenter/jsp/queryMemoryCenter.jsp?tablename=" + tablename
				+ "&keyfield=" + keyfield);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedInputStream bis = new BufferedInputStream(con.getInputStream(), 8192);
		ObjectInputStream ois = new ObjectInputStream(bis);
		final Object obj = ois.readObject();
		MultiHashMap mp = null;
		if (obj != null) {
			mp = (MultiHashMap) obj;
		}

		Set set = mp.entrySet();
		Iterator i = set.iterator();
		while (i.hasNext()) {
			Map.Entry<String, List<String>> me = (Map.Entry) i.next();
			for (int j = 0; j < me.getValue().size(); j++) {
				String key = me.getKey();
				Object value = me.getValue().get(j);
				System.out.println("key=" + key + ",value=" + String.valueOf(value));
			}
		}
	}

	private static void testReloadQueryMemoryCenter() throws Exception {
		URL url = new URL("http://" + testPath + "/ORMemoryCenter/jsp/reloadMemoryCenter.jsp");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.getInputStream();
	}

	private static void testEJBQueryMemoryCenter() throws Exception {
		String IP = "remote+http://" + testPath;
//        String IP = "t3://" + testPath;//weblogic
		SystableUtil memS = (SystableUtil) getEjb(IP, "MemS");
//        memS.preloadMemoryForTimer();//若查詢沒資料，請先執行此行preload
		ArrayList ret = memS.reflashTablenameItems_EJB();
		System.out.println(ret.toString());
	}

	public static void testEJBReloadMemoryCenter() throws Exception {
		// String IP = "t3://" + testPath; //weblogic
		String IP = "remote+http://" + testPath;
		SystableUtil memS = (SystableUtil) getEjb(IP, "MemS");
		memS.reloadTable("mordersysparameter", "code");
		System.out.println("---------------------------------------------------");
		String content = memS.getEJBQueryResult("mordersysparameter", "code", "testMT5");
		System.out.println(content);
		;
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
