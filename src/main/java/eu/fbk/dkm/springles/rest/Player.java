package eu.fbk.dkm.springles.rest;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Player {
 private String name;
 private String number;
 private String team;
 private String ttype;
 private String position;
 private String playing;
 private String scoredGoal;
 private String substitutionOut;
 private String substitutionIn;
 private String hasYCard;
 
public Player() {
	super();
	// TODO Auto-generated constructor stub
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getNumber() {
	return number;
}
public void setNumber(String number) {
	this.number = number;
}
public String getTeam() {
	return team;
}
public void setTeam(String team) {
	this.team = team;
}
public String getPosition() {
	return position;
}
public void setPosition(String position) {
	this.position = position;
}
public String getPlaying() {
	return playing;
}
public void setPlaying(String playing) {
	this.playing = playing;
}
public String getScoredGoal() {
	return scoredGoal;
}
public void setScoredGoal(String scoredGoal) {
	this.scoredGoal = scoredGoal;
}
public String getSubstitutionOut() {
	return substitutionOut;
}
public void setSubstitutionOut(String substitutionOut) {
	this.substitutionOut = substitutionOut;
}
public String getSubstitutionIn() {
	return substitutionIn;
}
public void setSubstitutionIn(String substitutionIn) {
	this.substitutionIn = substitutionIn;
}
public String getHasYCard() {
	return hasYCard;
}
public void setHasYCard(String hasYCard) {
	this.hasYCard = hasYCard;
}
public String getTtype() {
	return ttype;
}
public void setTtype(String ttype) {
	this.ttype = ttype;
}
 
 
}
