package bms;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.toolkit.MorderLogicLocator;

import com.cht.morder.ejb.bm.webinterface.WebSession;
import com.cht.morder.ejb.bm.webinterface.WebSessionHome;
import com.cht.morder.web.to.ResultCodeTO; 

public class ejbUtil {
    private String region; // ejb's region , ex: N
    
//    private filev13 bmslog;
    private String ejbJndi; // ejb jndi name
    
    /**
     * It had been removed From Jarkata EE 8
     * **/
    @Deprecated
    private Class ejbHomeClass; // ejb home class
    
    private String ejbUrl; // ejb t3 url
    private WebSession webSession;

    
    public ejbUtil() {
        initial();
    }

    private void initial() {
//      this.bmslog = new filev13();
        this.ejbUrl = null;
      this.ejbJndi = "com.cht.morder.ejb.jndi.bm.webinterface.WebSessionBean";
//        this.ejbJndi = "ejb:/morder-wildfly-server-basic/WebSessionBean!com.cht.morder.ejb.bm.webinterface.WebSession";
        this.ejbHomeClass = WebSessionHome.class;
    }

    private Context getContext() {
        try {
            String url = System.getProperty("MOrder2.Ejb.Url." + region);
            if (this.ejbUrl != null) {
                url = this.ejbUrl;
            }
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
            		"org.wildfly.naming.client.WildFlyInitialContextFactory");
            if (url != null)
                properties.put(Context.PROVIDER_URL, url); // url liks "remote+http://localhost:8080"
            return new InitialContext(properties);
        } catch (NamingException ex) {
//            bmslog.appendLog("err", "ejbUtil getContext exception:" + ex);
            return null;
        }
    }

    /**
     * It had been removed From Jarkata EE 8
     * @param ejbReferenceComponent String
     * @param homeClass Class
     * @return EJBHome
     * @throws NamingException
     */
    @Deprecated
    public EJBHome getEjbHome() throws
            Exception {
        try {
            Context context = getContext();
            Object ref = context.lookup(ejbJndi);
            return (EJBHome) javax.rmi.PortableRemoteObject.narrow(ref,
                    ejbHomeClass);
        } catch (Exception e) {
            throw e;
        }
    }
    public EJBObject getEjbObject() throws
    Exception {
		try {
			 MorderLogicLocator locator =new MorderLogicLocator(this.ejbUrl); 
			return (EJBObject)locator.getEJBByName(this.ejbJndi );
		} catch (Exception e) {
			throw e;
		}
	}

    private boolean initialWebSession() {
        boolean ret = true; 
        try {
        	  MorderLogicLocator locator =new MorderLogicLocator(this.ejbUrl);
        	  this.webSession =  locator.getEJBByName(this.ejbJndi );
//            Context context = getContext(); 
//            this.webSession = (WebSession) context.lookup(this.ejbJndi);
        } catch (Exception ex) {
            ret = false;
//            bmslog.appendLog("err", "ejbUtil initial exception:" + ex);
        }
        return ret;
    } 

    private String getBig5(String s) {
        try {
            if (System.getProperty("file.encoding") != null &&
                System.getProperty("file.encoding").indexOf("8859") != -1) {
                return new String(s.getBytes("MS950"), "ISO8859-1");
            }
        } catch (Exception e) {}
        return s;
    }

    public void setEjbJndi(String ejbJndi) {
        this.ejbJndi = ejbJndi;
    }

    public void setEjbUrl(String ejbUrl) {
        this.ejbUrl = ejbUrl;
    }
    @Deprecated
    public void setEjbHomeClass(Class ejbHomeClass) {
        this.ejbHomeClass = ejbHomeClass;
    }

    public String getEjbJndi() {
        return ejbJndi;
    }

    public String getEjbUrl() {
        return ejbUrl;
    }

    @Deprecated
    public Class getEjbHomeClass() {
        return ejbHomeClass;
    }


    public ResultCodeTO doWork(String region, String func, HashMap funcMap) {
        ResultCodeTO resultCodeTO = new ResultCodeTO();
        if (region == null || "N,C,S".indexOf(region) == -1 || func == null ||
            funcMap.isEmpty()) {
            resultCodeTO.setRetCode(0);
            resultCodeTO.setRetMsg(getBig5("系統忙線中，請稍候再使用！(parameter is null)"));
            return resultCodeTO;
        }
        this.region = region;

        if (webSession == null) {
            if (!initialWebSession()) {
                resultCodeTO.setRetCode(0);
                resultCodeTO.setRetMsg(getBig5("系統忙線中，請稍候再使用！(WebSession Create不成功!)"));
                return resultCodeTO;
            }
        }

        try {
            return webSession.doWork(func, funcMap);
        } catch (RemoteException ex) {
            resultCodeTO.setRetCode(0);
            resultCodeTO.setRetMsg(getBig5("系統忙線中，請稍候再使用！(" + ex + ")"));
            return resultCodeTO;
        }
    }
 
 
    public HashMap getPhasePackageInfoByAssignDate(String region,Date assignDate, int contractId, int packageType) {
        
        HashMap ret = new HashMap();
        if (region == null || "N,C,S".indexOf(region) == -1) {
            return null;            
        }
        this.region = region;

        if (webSession == null) {
            if (!initialWebSession()) {
//                bmslog.appendLog("err", "ejbUtil getPhasePackageInfoByAssignDate INIT不成功");
               return null;
               
            }
        }
        try {            
            return webSession.getPhasePackageInfoByAssignDate(assignDate, contractId, packageType);
        } catch (RemoteException ex) {
            
//            bmslog.appendLog("err", "ejbUtil getPhasePackageInfoByAssignDate 發生exception"+ex);
            return null;
        }
    }

}