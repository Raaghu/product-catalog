package in.krraghavendra.microservice.productcatalog.model.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import in.krraghavendra.microservice.productcatalog.model.Product;
import in.krraghavendra.microservice.productcatalog.model.Catalog;

public class TestProduct {

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
	public void testProduct(){
		
		// create data
		Session session = sessionFactory.openSession();
		Product product = new Product();
		product.setName("product1");
		product.setDescription("desc Prod");
		
		Transaction tx = session.beginTransaction();
		session.save(product);
		tx.commit();
		session.close();
		
		// read data
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<Product> products = session.createCriteria(Product.class).list();
		Assert.assertEquals(1,products.size());
		Product actualProduct = products.get(0);
		Assert.assertEquals(product.getName(), actualProduct.getName());
		Assert.assertEquals(product.getDescription(), actualProduct.getDescription());
		tx.commit();
		session.close();
		
		// add product to catalog
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		Catalog catalog = new Catalog();
		catalog.setName("Catalog 1");
		catalog.setDescription("Cat1 Desc");
		Set<Product> setProducts = new HashSet<Product>();
		setProducts.add(actualProduct);
		catalog.setProducts(setProducts);
		session.save(catalog);
		tx.commit();
		session.close();
		
		
		// assert adding product to catalog
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<Catalog> catalogs = session.createCriteria(Catalog.class).list();
		Assert.assertEquals(1,catalogs.size());
		Catalog actualCatalog = catalogs.get(0);
		Assert.assertEquals(catalog.getName(), actualCatalog.getName());
		Assert.assertEquals(catalog.getDescription(), actualCatalog.getDescription());
		
		Set<Product> actualProductsInCatalog = actualCatalog.getProducts();
		Assert.assertEquals(1, actualProductsInCatalog.size());
		Product actualProductInCatalog = actualProductsInCatalog.toArray(new Product[0])[0];

		Assert.assertEquals(product.getName(), actualProductInCatalog.getName());
		Assert.assertEquals(product.getDescription(), actualProductInCatalog.getDescription());
		
		
		tx.commit();
		session.close();
		
	}
	
	
	

}
