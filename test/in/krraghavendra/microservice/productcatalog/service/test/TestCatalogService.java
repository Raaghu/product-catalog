package in.krraghavendra.microservice.productcatalog.service.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.jetty.servlet.JettyWebContainerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import in.krraghavendra.microservice.productcatalog.model.Util;
import in.krraghavendra.microservice.productcatalog.service.ApplicationConfig;
import in.krraghavendra.microservice.productcatalog.service.jaxb.CatalogJaxb;
import in.krraghavendra.microservice.productcatalog.service.jaxb.ProductJaxb;

public class TestCatalogService {
	
	static Server server = null;
	static URI baseUri = null;
	static Feature loggingFeature = null;
	static Client client = null;
	
	@BeforeClass
	public static void setupBeforeClass() throws Exception{
		
		try {
			// find free PORT number
			ServerSocket socket = new ServerSocket(0);
			Integer port = socket.getLocalPort();
			socket.close();
			
			baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();
			
			Map<String,String> initParams = new HashMap<String,String>();
			initParams.put("javax.ws.rs.Application", ApplicationConfig.class.getName());

			
			ServletContainer container = new ServletContainer();
			
			server = JettyWebContainerFactory.create(baseUri, container , initParams,new HashMap());
			WebAppContext handler =  (WebAppContext)server.getHandler();
			handler.getServletContext().setAttribute("hibernateSession", Util.getHibernateSession());
			
			server.start();
	        
	        Logger logger = Logger.getLogger(TestCatalogService.class.getName());

			loggingFeature = new LoggingFeature(logger, Level.INFO, null, null);
	        
			System.out.println("Started Jetty Server.....");
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception{
		server.stop();
		System.out.println("Stopped Jetty Server.....");
	}
	
	
	@Before
	public void setup(){
		client = JerseyClientBuilder.newClient();
	    client.register(loggingFeature);
	}
	
	@After
	public void teardown(){
		client.close();
	}
	
	@Test
	public void testCatalogCreate(){
		
		WebTarget target = client.target(baseUri).path("/create");
		
		CatalogJaxb catalog = new CatalogJaxb();
		catalog.setName("Cat 1");
		catalog.setDescription("Desc 1");
		
		CatalogJaxb createdCatalog = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(catalog,MediaType.APPLICATION_JSON),CatalogJaxb.class);
		Assert.assertEquals(catalog.getName(), createdCatalog.getName());
		Assert.assertEquals(catalog.getDescription(), createdCatalog.getDescription());
	}
	
	@Test
	public void testCatalogGet(){
		
		CatalogJaxb catalog = new CatalogJaxb();
		catalog.setName("Cat 2");
		catalog.setDescription("Desc 2");
		
		WebTarget target = client.target(baseUri).path("/create");
		CatalogJaxb createdCatalog = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(catalog,MediaType.APPLICATION_JSON),CatalogJaxb.class);
		int createdCatalogId = createdCatalog.getId();
		
		target = client.target(baseUri).path("/"+createdCatalogId);
		CatalogJaxb retrievedCatalog = target.request(MediaType.APPLICATION_JSON).get(CatalogJaxb.class);
		
		Assert.assertEquals(createdCatalogId, retrievedCatalog.getId());
		Assert.assertEquals(catalog.getName(), retrievedCatalog.getName());
		Assert.assertEquals(catalog.getDescription(), retrievedCatalog.getDescription());
		
	}
	
	
	@Test
	public void testCatalogDelete(){
	    
		
		CatalogJaxb catalog = new CatalogJaxb();
		catalog.setName("Cat 1");
		catalog.setDescription("Desc 1");
		
		WebTarget target = client.target(baseUri).path("/create");
		CatalogJaxb createdCatalog = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(catalog,MediaType.APPLICATION_JSON),CatalogJaxb.class);
		
		int createdCatalogId = createdCatalog.getId();
		target = client.target(baseUri).path("/"+createdCatalogId);
		Response response = target.request().delete();
		Assert.assertEquals(200, response.getStatus());
		
	}
	
	@Test
	public void testCatalogAddProduct(){
	    
		
		CatalogJaxb catalog = new CatalogJaxb();
		catalog.setName("Cat 1");
		catalog.setDescription("Desc 1");
		
		WebTarget target = client.target(baseUri).path("/create");
		CatalogJaxb createdCatalog = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(catalog,MediaType.APPLICATION_JSON),CatalogJaxb.class);
		
		int createdCatalogId = createdCatalog.getId();
		
		ProductJaxb product = new ProductJaxb();
		product.setName("product Name 1");
		product.setDescription("product Name 1");
		
		
		target = client.target(baseUri).path("/"+createdCatalogId+"/products/add");
		Response response = target.request().post(Entity.entity(product, MediaType.APPLICATION_JSON));
		Assert.assertEquals(201, response.getStatus());
		
	}
	
	
	@Test
	public void testCatalogListProducts(){
	   
		CatalogJaxb catalog = new CatalogJaxb();
		catalog.setName("Cat 1");
		catalog.setDescription("Desc 1");
		
		WebTarget target = client.target(baseUri).path("/create");
		CatalogJaxb createdCatalog = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(catalog,MediaType.APPLICATION_JSON),CatalogJaxb.class);
		
		int createdCatalogId = createdCatalog.getId();
		
		ProductJaxb product = new ProductJaxb();
		product.setName("product Name 1");
		product.setDescription("product Desc 1");
		
		
		target = client.target(baseUri).path("/"+createdCatalogId+"/products/add");
		Response response = target.request().post(Entity.entity(product, MediaType.APPLICATION_JSON));
		Assert.assertEquals(201, response.getStatus());
		
		target = client.target(baseUri).path("/"+createdCatalogId+"/products");
		List<ProductJaxb> actualProducts = target.request().get(new GenericType<List<ProductJaxb>>(){});
		Assert.assertEquals(1, actualProducts.size());
		
		// add another product 
		ProductJaxb product2 = new ProductJaxb();
		product2.setName("product Name 2");
		product2.setDescription("product Desc 2");
		
		
		target = client.target(baseUri).path("/"+createdCatalogId+"/products/add");
		response = target.request().post(Entity.entity(product2, MediaType.APPLICATION_JSON));
		Assert.assertEquals(201, response.getStatus());
		
		target = client.target(baseUri).path("/"+createdCatalogId+"/products");
		actualProducts = target.request().get(new GenericType<List<ProductJaxb>>(){});
		Assert.assertEquals(2, actualProducts.size());
		
	}
	
	@Test
	public void testCatalogRemoveProduct(){
	   
		CatalogJaxb catalog = new CatalogJaxb();
		catalog.setName("Cat 1");
		catalog.setDescription("Desc 1");
		
		WebTarget target = client.target(baseUri).path("/create");
		CatalogJaxb createdCatalog = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(catalog,MediaType.APPLICATION_JSON),CatalogJaxb.class);
		
		int createdCatalogId = createdCatalog.getId();
		
		ProductJaxb product = new ProductJaxb();
		product.setName("product Name 1");
		product.setDescription("product Desc 1");
		
		
		target = client.target(baseUri).path("/"+createdCatalogId+"/products/add");
		Response response = target.request().post(Entity.entity(product, MediaType.APPLICATION_JSON));
		Assert.assertEquals(201, response.getStatus());
		
		target = client.target(baseUri).path("/"+createdCatalogId+"/products");
		List<ProductJaxb> actualProducts = target.request().get(new GenericType<List<ProductJaxb>>(){});
		Assert.assertEquals(1, actualProducts.size());
		int actualProductId = actualProducts.get(0).getId();
		
		// remove product
		target = client.target(baseUri).path("/"+createdCatalogId+"/product/"+actualProductId+"/remove");
		response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(null, MediaType.APPLICATION_JSON));
		Assert.assertEquals(200, response.getStatus());
		
		target = client.target(baseUri).path("/"+createdCatalogId+"/products");
		actualProducts = target.request().get(new GenericType<List<ProductJaxb>>(){});
		Assert.assertEquals(0, actualProducts.size());
		
	}
	
	@Test
	public void testProductGet(){
		
		CatalogJaxb catalog = new CatalogJaxb();
		catalog.setName("Cat 1");
		catalog.setDescription("Desc 1");
		
		WebTarget target = client.target(baseUri).path("/create");
		CatalogJaxb createdCatalog = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(catalog,MediaType.APPLICATION_JSON),CatalogJaxb.class);
		
		int createdCatalogId = createdCatalog.getId();
		
		ProductJaxb product = new ProductJaxb();
		product.setName("product Name 1");
		product.setDescription("product Name 1");
		
		target = client.target(baseUri).path("/"+createdCatalogId+"/products/add");
		Response response = target.request().post(Entity.entity(product, MediaType.APPLICATION_JSON));
		Assert.assertEquals(201, response.getStatus());
		
		String productEndPoint = response.getHeaderString("Location");
		
		target = client.target(productEndPoint);
		ProductJaxb retrievedProduct = target.request(MediaType.APPLICATION_JSON).get(ProductJaxb.class);
		
		Assert.assertEquals(product.getName(), retrievedProduct.getName());
		Assert.assertEquals(product.getDescription(), retrievedProduct.getDescription());
		
	}
	

}
