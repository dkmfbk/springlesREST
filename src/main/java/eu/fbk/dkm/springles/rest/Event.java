package eu.fbk.dkm.springles.rest;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Event {
 private String id;
 private String label;
 private String time;
 
 
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


public String getTime() {
	return time;
}


public void setTime(String time) {
	this.time = time;
}


public Event() {
	super();
	// TODO Auto-generated constructor stub
}
 
 
}