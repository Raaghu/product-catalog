package in.krraghavendra.microservice.productcatalog.service;

import java.net.URI;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import in.krraghavendra.microservice.productcatalog.model.Product;
import in.krraghavendra.microservice.productcatalog.service.jaxb.CatalogJaxb;
import in.krraghavendra.microservice.productcatalog.service.jaxb.ProductJaxb;

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
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCatalog(@PathParam("id") int id){
		Session session = (Session)request.getServletContext().getAttribute("hibernateSession");
		Transaction tx = session.beginTransaction();
		
		Catalog catalog = session.load(Catalog.class, id);
		
		CatalogJaxb catalogJaxb = new CatalogJaxb();
		catalogJaxb.setId(catalog.getId());
		catalogJaxb.setName(catalog.getName());
		catalogJaxb.setDescription(catalog.getDescription());
		
	    tx.commit();
	    
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
	
	@POST
	@Path("{id}/products/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProduct(@PathParam("id") int id,ProductJaxb productJaxb){
		Session session = (Session)request.getServletContext().getAttribute("hibernateSession");
		Transaction tx = session.beginTransaction();
		
		Catalog catalog = session.load(Catalog.class, id);

		Product product = new Product();
		product.setName(productJaxb.getName());
		product.setDescription(productJaxb.getDescription());
		
		catalog.getProducts().add(product);
		session.save(catalog);
		
		tx.commit();
		return Response.created(URI.create("/product/"+product.getId())).build();
	}
	
	@GET
	@Path("{id}/products")
	@Produces(MediaType.APPLICATION_JSON)
	public Product[] listProducts(@PathParam("id") int id){
		Session session = (Session)request.getServletContext().getAttribute("hibernateSession");
		Transaction tx = session.beginTransaction();
		
		Catalog catalog = session.load(Catalog.class, id);
		
		Set<Product> products = catalog.getProducts();
		
		Product[] aProducts = new Product[0];
		tx.commit();
		return products.toArray(aProducts);
	}
	
	@POST
	@Path("{id}/product/{productId}/remove")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProduct(@PathParam("id") int id,@PathParam("productId") int productId){
		Session session = (Session)request.getServletContext().getAttribute("hibernateSession");
		Transaction tx = session.beginTransaction();
		
		Catalog catalog = session.load(Catalog.class, id);
		Set<Product> products = catalog.getProducts();
		
		Product product = session.load(Product.class, productId);
		products.remove(product);
		
		session.save(catalog);
		
		tx.commit();
		
		ProductJaxb productJaxb = new ProductJaxb();
		productJaxb.setId(product.getId());
		productJaxb.setName(product.getName());
		productJaxb.setDescription(product.getDescription());
		
		return Response.ok(productJaxb).build();
	}
	
	
	@GET
	@Path("/product/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProduct(@PathParam("id") int id){
		Session session = (Session)request.getServletContext().getAttribute("hibernateSession");
		Transaction tx = session.beginTransaction();
		
		Product product = session.load(Product.class, id);
		
		ProductJaxb productJaxb = new ProductJaxb();
		productJaxb.setId(product.getId());
		productJaxb.setName(product.getName());
		productJaxb.setDescription(product.getDescription());
		
	    tx.commit();
	    
	    return Response.ok(productJaxb).build();
	}
	

}
