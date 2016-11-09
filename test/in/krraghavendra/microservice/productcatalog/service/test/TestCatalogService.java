package in.krraghavendra.microservice.productcatalog.service.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.Container;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.component.LifeCycle.Listener;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import in.krraghavendra.microservice.productcatalog.service.ApplicationConfig;
import in.krraghavendra.microservice.productcatalog.service.CatalogService;
import in.krraghavendra.microservice.productcatalog.service.jaxb.Catalog;

public class TestCatalogService {
	
	static Server server = null;
	static URI baseUri = null;
	
	@BeforeClass
	public static void setupBeforeClass() throws IOException, InterruptedException{
		
		try {
			// find free PORT number
			ServerSocket socket = new ServerSocket(0);
			Integer port = socket.getLocalPort();
			socket.close();
			
			baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();
			//ResourceConfig config = new ResourceConfig(ApplicationConfig.class);
			Set<Class<?>> resources = new HashSet();
			resources.add(CatalogService.class);
			ResourceConfig config = new ResourceConfig(resources);
			
			server = JettyHttpContainerFactory.createServer(baseUri, config);
			
			//server.join();
			
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
	
	@Test
	public void testCatalogCreate(){
		
		Logger logger = Logger.getLogger(getClass().getName());

		Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
		
	    Client client = JerseyClientBuilder.newClient();
	    client.register(feature);
	    
		WebTarget target = client.target(baseUri).path("/add");
		
		Catalog catalog = new Catalog();
		catalog.setName("Cat 1");
		catalog.setDescription("Desc 1");
		
		Catalog createdCatalog = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(catalog,MediaType.APPLICATION_JSON),Catalog.class);
		
		System.out.println("Success");
	}

}
