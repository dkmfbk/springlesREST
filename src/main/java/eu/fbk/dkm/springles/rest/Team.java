package eu.fbk.dkm.springles.rest;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Team {
 private String id;
 private String label;
 private String type;
 
 

public Team() {
	super();
	// TODO Auto-generated constructor stub
}



public String getId() {
	return id;
}



public void setId(String id) {
	this.id = id;
}



public String getLabel() {
	return label;
}



public void setLabel(String label) {
	this.label = label;
}



public String getType() {
	return type;
}



public void setType(String type) {
	this.type = type;
}
 
 
}
