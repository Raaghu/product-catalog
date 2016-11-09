package in.krraghavendra.microservice.productcatalog.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import in.krraghavendra.microservice.productcatalog.model.Catalog;

@Path("/")
public class CatalogService {
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCatalog(@Context HttpServletRequest request,in.krraghavendra.microservice.productcatalog.service.jaxb.Catalog catalog){
		Session session = Util.getHibernateSession();
		Transaction tx = session.beginTransaction();
		
		Catalog catalogModel = new Catalog();
		catalogModel.setName(catalog.getName());
		catalogModel.setDescription(catalog.getDescription());
		
	    session.save(catalogModel);
	    tx.commit();
	    
	    catalog.setId(catalogModel.getId());
	    
		return Response.ok(catalog).build();
	}

}
