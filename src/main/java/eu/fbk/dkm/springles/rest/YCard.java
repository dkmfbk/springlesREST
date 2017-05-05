package eu.fbk.dkm.springles.rest;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class YCard {
 private String id;
 private String time;
 
 
public String getId() {
	return id;
}


public void setId(String id) {
	this.id = id;
}



public String getTime() {
	return time;
}


public void setTime(String time) {
	this.time = time;
}


public YCard() {
	super();
	// TODO Auto-generated constructor stub
}
 
 
}
