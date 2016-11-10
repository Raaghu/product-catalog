package in.krraghavendra.microservice.productcatalog.service.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="catalog")
public class CatalogJaxb {
	@XmlAttribute(name="id")
	private int id;
	
	@XmlAttribute(name = "name")
	private String name;
	
	@XmlAttribute(name = "description")
	private String description;

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
		
}
