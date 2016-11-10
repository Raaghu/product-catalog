package in.krraghavendra.microservice.productcatalog.service.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.jetty.JettyHttpContainer;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.jetty.servlet.JettyWebContainerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import in.krraghavendra.microservice.productcatalog.service.ApplicationConfig;
import in.krraghavendra.microservice.productcatalog.service.jaxb.CatalogJaxb;
import in.krraghavendra.microservice.session.SessionListener;

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
			
			//Application config = new ApplicationConfig();
			
			Map<String,String> initParams = new HashMap<String,String>();
			initParams.put("javax.ws.rs.Application", ApplicationConfig.class.getName());

			
			ServletContainer container = new ServletContainer();
			container.getServletContext().createListener(SessionListener.class);
			
			server = JettyWebContainerFactory.create(baseUri, container , initParams,new HashMap());
			
			server.start();
			
			
			//final JettyHttpContainer container = ContainerFactory.createContainer(JettyHttpContainer.class, config);
			//container.
	        //server = JettyHttpContainerFactory.createServer(baseUri, null, container, true);
	        
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
	

}
