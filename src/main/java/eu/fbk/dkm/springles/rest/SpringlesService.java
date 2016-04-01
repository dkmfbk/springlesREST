package eu.fbk.dkm.springles.rest;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.ConfigTemplate;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.config.RepositoryImplConfig;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.rio.trig.TriGParser;
import org.openrdf.rio.trig.TriGWriter;
import org.openrdf.rio.turtle.TurtleWriter;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import eu.fbk.dkm.internal.springles.config.SpringlesRepositoryFactory;
import info.aduna.io.IOUtil;
import info.aduna.iteration.Iterations;






@Path("/rest")
public class SpringlesService {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	//String sesameServer = "http://stettler.fbk.eu:50000";
	// String sesameServer;
	// String sesameServer = "http://localhost:50000";
    // String repositoryID="test-springles-100";;
	 //String repositoryID = "trentour-4-12-2013";	
    // Repository myRepository ;
     public  SpringlesService (){
 		
 		
 		super();
 		//final Properties properties = new Properties();
 		//try {
 		//	properties.load(this.getClass().getResourceAsStream("/config.properties"));
 			
 			//sesameServer = properties.getProperty("sesameServer");
 		//	repositoryID = properties.getProperty("repositoryID");
 		//	 sesameServer="http://localhost:8080/openrdf-sesame";
 			// repositoryID="test-springles-100";
 		 //myRepository = new HTTPRepository(sesameServer, repositoryID);

 			
 	//	} catch (IOException e) {
 			// TODO Auto-generated catch block
 		//	e.printStackTrace();
 		//}
 			
 		
 		
 		
 		
 		
 	}
	  // This method is called if TEXT_PLAIN is request
	  @GET
	  @Path("/hallo")
	  @Produces(MediaType.TEXT_PLAIN)
	  public String sayPlainTextHello() {
	    return "Hello Jersey1";
	  }

	 
	  
	  
	  
	  // This method is called if HTML is request
	 
	  @GET
	  @Path("/create")
	  @Produces(MediaType.TEXT_HTML)
	  public Response createRepository(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL,
			  @QueryParam("rulesetURI") String rulesetURI,
			  @QueryParam("springlesrepositorytitle") String springlesrepositorytitle,
			  @QueryParam("inferencer") String inferencer
		
				
				
			  ) {
		//  String serverUrl = "http://localhost:8080/openrdf-sesame";
			System.out.println(springlesrepositoryID);
		  RemoteRepositoryManager manager = new RemoteRepositoryManager(springlesserverURL);
		  try {
			manager.initialize();
			//String repositoryId = "test-springles-"+ruleset;
			boolean persist = true;
			SpringlesRepositoryFactory srf=new SpringlesRepositoryFactory();
			//SailImplConfig backendConfig = new MemoryStoreConfig(persist);
			RepositoryImplConfig repositoryTypeSpec = srf.getConfig();
//	Graph g= new GraphImpl();
			
	
	
			Model	g =null;
	
	
			try(
	InputStream url=getClass().getClassLoader().getResourceAsStream("springles.ttl");
	){
				
				String template = IOUtil.readString(new InputStreamReader(url,
						"UTF-8"));
				System.out.println(template);
				ConfigTemplate ct= new ConfigTemplate(template);
				Map<String, String> valueMap =new HashMap<String, String>();
				valueMap.put("Repository ID", springlesrepositoryID);
				valueMap.put("Ruleset", rulesetURI);
				valueMap.put("Repository title", springlesrepositorytitle);
				valueMap.put("Inferencer type", inferencer);
				
				String configString = ct.render(valueMap);
				System.out.println(configString);
	           g = Rio.parse(IOUtils.toInputStream(configString, "UTF-8"),"",RDFFormat.TURTLE);		
				
				
			}
			
			//URI u =new URIImpl("http://dkm.fbk.eu/springles/config#test");
			
			RepositoryConfig repConfig = new RepositoryConfig(springlesrepositoryID, repositoryTypeSpec);
		
			repConfig.parse(g, g.filter(null, RDF.TYPE, new URIImpl("http://www.openrdf.org/config/repository#Repository")).subjectResource());
			manager.addRepositoryConfig(repConfig);
			// Repository repository = manager.getRepository(repositoryId);
			 
		} catch (RepositoryException |RDFParseException| IOException|RepositoryConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return Response.ok().build();
	 //   return "<html> " + "<title>" + "Hello Jersey" + "</title>"
	   //     + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	 

  }  
	  
	  

	   
	  @GET
	  @Path("/getfile")
	  @Produces(MediaType.APPLICATION_OCTET_STREAM)
	  public Response getFile() {
	String FILE_PATH = "C:/tmp/text.txt";
		  File file = new File(FILE_PATH);
	      ResponseBuilder response = Response.ok((Object) file);
	      response.header("Content-Disposition", "attachment; filename=text.txt");
	      return response.build();

	  }
	   
	
	  
	  
	  
	  
	  @GET
	  @Path("/fullexport") 
	  @Produces(MediaType.APPLICATION_OCTET_STREAM)
	  public Response fullexport(
					
				@QueryParam("springlesrepositoryID") String springlesrepositoryID,
				@QueryParam("springlesserverURL") String springlesserverURL,
				@QueryParam("contextURI") String contextURI,
				@QueryParam("exportformat") String exportformat,
				@QueryParam("includeinferred") int includeinferred
				 
			 
			  ) {
	     
		  File tempFile;
		try {
			tempFile = File.createTempFile("backup", ".txt");
		
			System.out.println(springlesrepositoryID);
			System.out.println(springlesserverURL);	
			System.out.println(exportformat);
			
			  
			  RemoteRepositoryManager manager = new RemoteRepositoryManager(springlesserverURL);
			  
			 
				manager.initialize();
				boolean persist = includeinferred == 1 ? true:false;
				manager.initialize();
				Repository repository = manager.getRepository(springlesrepositoryID);
				ValueFactory f = repository.getValueFactory();
				RepositoryConnection con = repository.getConnection();
				String context = contextURI;

				URI contextURI_ = f.createURI(context);
				
			
				
					
				
				
				Writer writer = new BufferedWriter(new FileWriter(tempFile));
				RDFHandler rdfxmlWriter = new RDFXMLPrettyWriter(writer);
				TriGWriter trigWriter= new TriGWriter(writer);
				TurtleWriter ttlWriter = new TurtleWriter(writer);
				
				
				if(exportformat.equals("trig")){
				 con.exportStatements(null, null, null, persist, trigWriter);
				}else if(exportformat.equals("ttl")){
					 con.exportStatements(null, null, null, persist, ttlWriter);
					}{
					 con.exportStatements(null, null, null, persist, rdfxmlWriter);
					
				}
				 ResponseBuilder response = Response.ok((Object) tempFile);
				 
				 response.header("Cache-Control", "public");
				 response.header("Content-Description", "File Transfer");
				 response.header("Content-Transfer-Encoding","binary");
				 response.header("Content-Type","binary/octet-stream");
				 response.header("Access-Control-Allow-Origin", "*");
			      response.header("Content-Disposition", "attachment; filename="+tempFile.getName()+"\"");
			  	tempFile.deleteOnExit();
			  
			      return response.build();
			  
			  } catch (RepositoryException | RepositoryConfigException  | RDFHandlerException | IOException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	    	  
	    	  
	    	  
	 }
	      
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  @POST
	  @Path("/upload_old")
	  @Consumes(MediaType.MULTIPART_FORM_DATA)

	  @Produces(MediaType.TEXT_HTML)
	  public String addData_old(
			  @FormDataParam("springlesrepositoryID") String springlesrepositoryID,
			  @FormDataParam("springlesserverURL") String springlesserverURL,
			  @FormDataParam("baseURI") String baseURI,
			 @FormDataParam("filetoupload") InputStream filetoupload,
		     @FormDataParam("filetoupload") FormDataContentDisposition fileDetail)
			   {
		
	System.out.println(springlesrepositoryID);
	System.out.println(springlesserverURL);	  
	System.out.println(baseURI);	  
	System.out.println(filetoupload);	 
		  
		  
RemoteRepositoryManager manager = new RemoteRepositoryManager(springlesserverURL);
		  
String repositoryId = springlesrepositoryID;
try{

manager.initialize();
Repository repository = manager.getRepository(repositoryId);
RepositoryConnection con = repository.getConnection();

TriGParser parser = (TriGParser) Rio.createParser(RDFFormat.TRIG);
 
// add our own custom RDFHandler to the parser. This handler takes care of adding
// triples to our repository and doing intermittent commits
//RDFHandler cc=new ChunkCommitter1(con) ;
 //parser.setRDFHandler(cc );
// System.out.println("chunked");
 

try {
    parser.parse(filetoupload,"file://"+ baseURI);
    System.out.println("parsed");
    con.commit();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
finally {
    con.close();

}







try {
	

ValueFactory f = repository.getValueFactory();

// create some resources and literals to make statements out of
//URI alice = f.createURI("http://example.org/people/alice");
//URI bob = f.createURI("http://example.org/people/bob");
//URI name = f.createURI("http://example.org/ontology/name");
//URI person = f.createURI("http://example.org/ontology/Person");
//Literal bobsName = f.createLiteral("Bob");
//Literal alicesName = f.createLiteral("Alice");

try {
  // RepositoryConnection con = myRepository.getConnection();

   try {
      // alice is a person
 //     con.add(alice, RDF.TYPE, person);
      // alice's name is "Alice"
  //    con.add(alice, name, alicesName);

      // bob is a person
  //    con.add(bob, RDF.TYPE, person);
      // bob's name is "Bob"
   //   con.add(bob, name, bobsName);
   }
   finally {
      con.close();
   }
}
catch (OpenRDFException e) {
   // handle exception
}
	
   
    //  con.add(file, baseURI, RDFFormat.TRIG);
//      URL url = new URL("http://example.org/example/remote.rdf");
 //     con.add(url, url.toString(), RDFFormat.RDFXML);
    //  URL url = new URL("http://example.org/example/remote.rdf");
      //con.add(url, url.toString(), RDFFormat.RDFXML);

}finally{
	
con.close();	
}   

}catch (OpenRDFException e) {
   // handle exception

}
	  
	    return "<html> " + "<title>" + "Hello Jersey" + "</title>"
	        + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	 

  
	  

		     
		   
	  }
	  
	  
	  
	  
	  
	  @POST
	  @Path("/upload")
	  @Consumes(MediaType.MULTIPART_FORM_DATA)

	  @Produces(MediaType.TEXT_HTML)
	  public String addData(
			  @FormDataParam("springlesrepositoryID") String springlesrepositoryID,
			  @FormDataParam("springlesserverURL") String springlesserverURL,
			  @FormDataParam("baseURI") String baseURI,
			 @FormDataParam("filetoupload") InputStream filetoupload,
		     @FormDataParam("filetoupload") FormDataContentDisposition fileDetail)
			   {
		
	System.out.println(springlesrepositoryID);
	System.out.println(springlesserverURL);	  
	System.out.println(baseURI);	  
	System.out.println(filetoupload);	 
		  

	String result ="FAIL";
	
	
	//String baseURI = "http://example.org/example/local";	

	
	
	RemoteRepositoryManager manager = new RemoteRepositoryManager(springlesserverURL);
		  
	String repositoryId = springlesrepositoryID;
	
	try {
		manager.initialize();
	
		Repository repository = manager.getRepository(repositoryId);
		RepositoryConnection con = repository.getConnection();
	
	    con.add(filetoupload, baseURI, RDFFormat.TRIG);
		//con.add(file, baseURI, RDFFormat.TRIG);
		result="200 OK";
	
	
	
	
	} catch (RepositoryException | RepositoryConfigException | RDFParseException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  
	  return result;
			   }
	  
	  
	  
	  
	  @GET
	  @Path("/computeclosure2")
	  @Produces(MediaType.APPLICATION_JSON)
	  public String computeClosure2(
			  
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL
			  
			  
			  ) {

	  	String result = "FAIL";
	  	
	  	  
	  	try {
	  		
	  	Repository	myRepository2 = new HTTPRepository(springlesserverURL, springlesrepositoryID);
	  		myRepository2.initialize();
	  		RepositoryConnection con = myRepository2.getConnection();

	  		// result= result+""+con.size();
	  		try {
	  			System.out.println(springlesrepositoryID);
		  		System.out.println(springlesserverURL);	
		  			
		  			long numberOfStatements= con.size();
					System.out.println(" Computo inferenze........");   
	  			String queryString = "clear graph <springles:update-closure>";
	  			
	  			//	con.begin();
	  				//	conn.prepareUpdate(QueryLanguage.SPARQL, updateQuery);
	  					Update insert = con.prepareUpdate(QueryLanguage.SPARQL,
	  							queryString);
	  					insert.execute();
	  					insert.clearBindings();
	  					 con.commit();
	  					// con.close();
	  					 result="200 OK";
	  					// tupleQuery.setIncludeInferred(true);
	  					//TupleQueryResult qresult = tupleQuery.evaluate();
	  					try {
	  						

	  					} finally {
	  						//qresult.close();

	  					}
	  					
	  		
	  		} finally {
	  			con.close();

	  		}

	  	} catch (OpenRDFException e) {
	  		// TODO Auto-generated catch block
	  		e.printStackTrace();
	  	}

	  	
	  	return result;
	  }

	  @GET
	  @Path("/computeclosure")
	  @Produces(MediaType.TEXT_HTML)
	  public String computeClosure(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL)
			
			   {

		  
		  String result="Fail";
		  Repository	myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
			try {
				myRepository.initialize();
			
	  		RepositoryConnection con = myRepository.getConnection();
	  		// result= result+""+con.size();
	  	
	  		System.out.println(springlesrepositoryID);
	  		System.out.println(springlesserverURL);	
	  			
	  			long numberOfStatements= con.size();
				System.out.println(" Computo inferenze........");     
	  			
	  			String queryString = "clear graph <springles:update-closure>";
	  				//if (!con.isActive()){
	  					con.begin();
	  				//}
	  				//	conn.prepareUpdate(QueryLanguage.SPARQL, updateQuery);
	  					Update insert = con.prepareUpdate(QueryLanguage.SPARQL,
	  							queryString);
	  					long startTime = System.currentTimeMillis();
	  					insert.execute();
	  					 con.commit();
	  					 
	  				//	 con.close();
	  					 result="200 OK";
	  					long estimatedTime = System.currentTimeMillis() - startTime;
	  					// tupleQuery.setIncludeInferred(true);
	  					//TupleQueryResult qresult = tupleQuery.evaluate();
	  					System.out.println("elapsed time in millisecondi= "+estimatedTime);
						//	logger.info("elapsed time in millisecondi= "+estimatedTime);
						//	logger.info("Number of statement= "+con.size());	
							System.out.println("Number of statement= "+numberOfStatements);	
							  try {
						             final TupleQuery query =
						 con.prepareTupleQuery(QueryLanguage.SPARQL,
						                     "SELECT (COUNT(*) AS ?n) WHERE { ?s ?p ?o }");
						             query.setIncludeInferred(true);
					long numberOfTotalStatements= ((Literal) Iterations.asList(query.evaluate()).get(0).getValue("n"))
						                     .longValue();
				//	logger.info("Number of total statement= "+number);
					System.out.println("Number of total statement= "+numberOfTotalStatements);
								con.close();
								} finally {
			  						con.close();

			  					}
	  				
	  		
			
		
			} catch (RepositoryException | MalformedQueryException |QueryEvaluationException | UpdateExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	  		
			}
	  

	  	
	  	return result;
	  }	  

	  	  
	  
	  
	  
	  @GET
	  @Path("/nstatements")
	  @Produces(MediaType.TEXT_HTML)
	  public String numberOfStatements(
			  
			  
			  
			  @QueryParam("repositoryID") String springlesrepositoryID,
			  @QueryParam("serverURL") String springlesserverURL,
			  @QueryParam("includeinferred") int includeinferred
			  
			  
			  ) {

	  	String result = "FAIL";
	  	
	  	  
	  	try {
	  	Repository	myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
	  		myRepository.initialize();
	  		RepositoryConnection con = myRepository.getConnection();

	  		 result= "";

	  			
	  		
							  try {
						             final TupleQuery query =
						 con.prepareTupleQuery(QueryLanguage.SPARQL,
						                     "SELECT (COUNT(*) AS ?n) WHERE { ?s ?p ?o }");
						             query.setIncludeInferred(includeinferred == 1 ? true:false);
					long numberOfTotalStatements= ((Literal) Iterations.asList(query.evaluate()).get(0).getValue("n"))
						                     .longValue();
				//	logger.info("Number of total statement= "+number);
					System.out.println("Number of total statement= "+numberOfTotalStatements);
					result = result + "  Number of total statement= "+numberOfTotalStatements;
								} finally {
			  						con.close();

			  					}
	  					try {
	  						

	  					} finally {
	  						//qresult.close();

	  					}
	  					
	  		
	  	
	  	} catch (OpenRDFException e) {
	  		// TODO Auto-generated catch block
	  		e.printStackTrace();
	  	}

	  	
	  	return result;
	  }	  
	  
	  
	  
	  @GET
	  @Path("/querysparql")
	  @Produces(MediaType.TEXT_HTML)
	 		  		
		  public String getTuples(
				  @QueryParam("repositoryID") String springlesrepositoryID,
				  @QueryParam("serverURL") String springlesserverURL,
				  @QueryParam("includeinferred") int includeinferred, 
					@QueryParam("querySPARQL") String query) {
				List<BindingSet> tuples = new ArrayList<BindingSet>();
				String result="";
				try {
			Repository		myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
					myRepository.initialize();
			  		RepositoryConnection connection = myRepository.getConnection();
				//	RepositoryConnection connection = repo.getConnection();
			  	//	RDFWriter writer = Rio.createWriter(RDFFormat.TURTLE, System.out);
					try {
					//	query="SELECT ?s ?p ?o WHERE { ?s ?p ?o }";
						TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,  query);
						tupleQuery.setIncludeInferred(includeinferred == 1 ? true:false);
						TupleQueryResult qresult = tupleQuery.evaluate();
						try {
							while (qresult.hasNext()) {
								tuples.add(qresult.next());
							}
						} finally {
							qresult.close();
						}
					} finally {
						connection.close();
					}
				} catch (OpenRDFException ex) {
					ex.printStackTrace();
				}
				
				for (BindingSet bindingSet : tuples) {
					result=result+bindingSet.toString()+"\n";
				}
				
				
				return result;
			}  
	  
	  	  
	  
}
