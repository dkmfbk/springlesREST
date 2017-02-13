

#SpringlesREST

**SpringlesREST** is a REST web service that exposes useful methods to mange a SPRINGLES repository

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

## Developers Guide

**SPRINGLES** is developed using **MAVEN** (http://maven.apache.org). You need a basic understanding of MAVEN in order to compile the code and generate the documentation.
The code has been developed with **Eclipse Neon**

##INSTALLATION
Download the code from Git as a zip file.
unzip the files
Open Eclipe and import it as a Existing Maven project

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



# MAVEN instructions


In the following we gave a brief overview of the main commands to work with the source package. You need to install MAVEN version 3 or greater on your machine (Windows, Linux, Mac OSX) in order to follow these instructions.
Installing the package and prepare the built environment

Unzip the package where you prefer on your file system. Open a command prompt and enter the following command:
```sh
    mvn -v
```
This should display the version of MAVEN currently installed. Please check you have version 3 of Maven installed, otherwise please update your installation.

It would be also helpful to allocate more memory to MAVEN for its execution: to this end, define the following environment variable (in Windows: select control panel | system):
```sh
  MAVEN_OPTS = -Xms128m -Xmx256m
```
Now you should be ready to compile the code.
Compile the code

From the command line, move to the root directory of the source package and execute the following command:
```sh
mvn package -Dmaven.test.skip
```
This tells MAVEN to compile the code and generate the resulting JARs; switch "-Dmaven.test.skip" causes MAVEN to skip JUnit tests (you may omit it if you want to perform testing before packaging). If everything works you should see a "BUILD SUCCESSFUL" message at the end.
Generate the documentation

Launch the following two commands from the prompt:

```sh
mvn site -Dmaven.test.skip
mvn site:deploy
```

The first command generates the HTML documentation from the APT sources (a MAVEN format, used to write these pages) and generates also all the configured MAVEN reports (including Javadocs). The second command deploys the generated documentation under the /docs folder under the package (you can change the pom.xml to deploy to a different location / Web site).

In order to generate the PDF manual, the Doxia MAVEN plugin has been configured to translate part of the documentation into Latex. You need to create the site, then manually enter folder /target/doxia/latex/manual, open manual.tex, change the document type from book to article (remove also the /chapter instruction) and then compile the document using your preferred Latex distribution (e.g., MikTeX on Windows).
Generate the source and binary packages

From the command prompt, execute the following:
```sh
mvn assembly:assembly -Dmaven.test.skip
```

MAVEN will generate two zip files for the source and the binary packages, under the /target folder. These files are exactly the packages published on the web site. Please update the assembly.xml descriptors under /src/main/assemblies subfolders if you wish to customize the generated packages.
Note on local MAVEN repository

This package includes a local MAVEN repository (/mvnrepo)containing JAR files for external libraries not available on public MAVEN repositories at the time SPRINGLES was built.
