package in.krraghavendra.microservice.productcatalog.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="CATALOG")
public class Catalog implements Serializable{
	
	@Id @GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="parent_id")
	private Catalog parentCatalog;
	
	@OneToMany(mappedBy="parentCatalog")
	private Set<Catalog> subCatalogs = new HashSet<Catalog>();
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="CATALOG_PRODUCT",
	          joinColumns={@JoinColumn(name="CATALOG_ID")},
	          inverseJoinColumns={@JoinColumn(name="PRODUCT_ID")})
	private Set<Product> products = new HashSet<Product>();
	
	public Catalog(){
		
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Catalog getParentCatalog() {
		return parentCatalog;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public void setParentCatalog(Catalog parentCatalog) {
		this.parentCatalog = parentCatalog;
	}

	public Set<Catalog> getSubCatalogs() {
		return subCatalogs;
	}

	public void setSubCatalogs(Set<Catalog> subCatalogs) {
		this.subCatalogs = subCatalogs;
	}
	

}
