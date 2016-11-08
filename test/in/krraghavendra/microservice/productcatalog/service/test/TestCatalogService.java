package in.krraghavendra.microservice.productcatalog.service.test;

import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.BeforeClass;

public class TestCatalogService {
	
	@BeforeClass
	public static void setupBeforeClass(){
		ServletHolder sh = new ServletHolder(ServletContainer.class);
	}
	
	

}
