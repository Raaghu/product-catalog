package in.krraghavendra.microservice.productcatalog.model.test;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.Assert;
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
		
		// create data
		Session session = sessionFactory.openSession();
		Catalog catalog = new Catalog();
		catalog.setName("cat1");
		catalog.setDescription("desc");
		
		Transaction tx = session.beginTransaction();
		session.save(catalog);
		tx.commit();
		session.close();
		
		// read data
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<Catalog> catalogs = session.createCriteria(Catalog.class).list();
		Assert.assertEquals(1,catalogs.size());
		Catalog actualCatalog = catalogs.get(0);
		Assert.assertEquals(catalog.getName(), actualCatalog.getName());
		Assert.assertEquals(catalog.getDescription(), actualCatalog.getDescription());
		tx.commit();
		session.close();
	}
	
	
	

}
