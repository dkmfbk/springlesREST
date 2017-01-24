

#springlesREST

**springlesREST** is a REST web service that exposes useful methods to mange a SPRINGLES repository

* create/delete a repository
* upload data
* upload rulesets
* perform SPARQL querys
* choose a Rule based inferencer
* perform a closure
* see the status of the repository (number of triples, inferred)

A web based GUI is also provided to easily use the service



##REQUIREMENTS

Java 8.x

Apache Tomcat 8.x

openRDFSesame 2.8.9

springles-0.1-SNAPSHOT.jar

rdf-pro-core-0.5.1.jar

##INSTALLATION

compile SpringleREST and export as a SpringleREST.war

copy `openrdf-sesame.war` and `SpringleREST.war` in the Tomcat webbapp dir

Once the services are deployed two new directory are created under webapp

copy `springles-0.1-SNAPSHOT.jar` in webapps/SpringleREST/WEB-INF/lib

copy `springles-0.1-SNAPSHOT.jar` in webapps/openrdf-sesame/WEB-INF/lib

copy `rdf-pro-core-0.5.1.jar` in webapps/openrdf-sesame/WEB-INF/lib

create empty file webapps/openrdf-sesame/META-INF/classes/`springles-rulesets` 

create empty file webapps/openrdf-sesame/META-INF/classes/`rdfpro-rulesets` 

Restart Tomcat

to use the GUI go to:
`http://localhost:8080/SpringlesREST/demosite/springlesGUI.html`

