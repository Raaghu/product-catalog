package in.krraghavendra.microservice.productcatalog.model.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import in.krraghavendra.microservice.productcatalog.model.Catalog;

public class TestCatalog {

	static SessionFactory sessionFactory = null;
	
	@BeforeClass
	public static void setUpClass(){
		Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        sessionFactory = cfg.buildSessionFactory();
	}
	
	@AfterClass
	public static void teardownAfterClass(){
		sessionFactory.close();
	}
	
	
	@Test
	public void testCatalog(){
		
		Session session = sessionFactory.openSession();
		
		Catalog catalog = new Catalog();
		catalog.setName("cat1");
		catalog.setDescription("desc");
		catalog.setParentId("");
		
		Transaction tx = session.beginTransaction();
		session.save(catalog);
		tx.commit();
		session.close();
		
	}
	
	
	

}
