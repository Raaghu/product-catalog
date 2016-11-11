package in.krraghavendra.microservice.productcatalog.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import in.krraghavendra.microservice.productcatalog.model.Catalog;
import in.krraghavendra.microservice.productcatalog.service.jaxb.CatalogJaxb;

@Path("/")
public class CatalogService {
	
	@Context
	HttpServletRequest request;
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCatalog(CatalogJaxb catalogJaxb){
		Session session = (Session)request.getServletContext().getAttribute("hibernateSession");
		Transaction tx = session.beginTransaction();
		
		Catalog catalog = new Catalog();
		catalog.setName(catalogJaxb.getName());
		catalog.setDescription(catalogJaxb.getDescription());
		
	    session.save(catalog);
	    tx.commit();
	    
	    catalogJaxb.setId(catalog.getId());
		return Response.ok(catalogJaxb).build();
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteCatalog(@PathParam("id") int id){
		Session session = (Session)request.getServletContext().getAttribute("hibernateSession");
		Transaction tx = session.beginTransaction();
		
		Catalog catalog = session.load(Catalog.class, id);
		session.delete(catalog);
		
		tx.commit();
		return Response.ok().build();
	}
	

}
