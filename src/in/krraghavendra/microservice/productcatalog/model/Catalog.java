package in.krraghavendra.microservice.productcatalog.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="CATALOG")
public class Catalog implements Serializable{
	
	@Id @GeneratedValue
	@Column(name="id")
	private String id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "parentId")
	private String parentId;
	
	public Catalog(){
		
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
	

}
