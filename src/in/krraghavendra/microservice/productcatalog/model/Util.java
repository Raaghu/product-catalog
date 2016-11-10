package in.krraghavendra.microservice.productcatalog.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Util {
	
	public static Session getHibernateSession(){
		Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = cfg.buildSessionFactory();
        return sessionFactory.openSession();
	}
	
	public static void closeHibernateSession(Session session){
		SessionFactory sessionFactory = session.getSessionFactory();
		session.close();
		sessionFactory.close();
	}
	

}
