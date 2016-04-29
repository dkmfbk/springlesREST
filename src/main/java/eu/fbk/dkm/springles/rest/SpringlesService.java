package eu.fbk.dkm.springles.rest;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.LinkedHashModel;
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
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.config.ConfigTemplate;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.config.RepositoryImplConfig;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.manager.RepositoryInfo;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
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
			  @QueryParam("inferencer") String inferencer,
			  @QueryParam("inferenceprefix") String inferenceprefix
				
				
			  ) {
		//  String serverUrl = "http://localhost:8080/openrdf-sesame";
			System.out.println(inferenceprefix);
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
				valueMap.put("Inferred context prefix", inferenceprefix);
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
				
				
			
				
					
				
				
				Writer writer = new BufferedWriter(new FileWriter(tempFile));
				RDFHandler rdfxmlWriter = new RDFXMLPrettyWriter(writer);
				TriGWriter trigWriter= new TriGWriter(writer);
				TurtleWriter ttlWriter = new TurtleWriter(writer);
				
				
				if(exportformat.equals("trig")){
				 con.exportStatements(null, null, null, persist, trigWriter);
				}else if(exportformat.equals("ttl")){
					 con.exportStatements(null, null, null, persist, ttlWriter);
					}else{
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
					 result="Inferred statements: " + (numberOfTotalStatements - numberOfStatements);
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
	  @Path("/clear")
	  @Produces(MediaType.TEXT_HTML)
	  public String clear(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL,
			  @QueryParam("includenotinferred") int includenotinferred)
	  {

		  
		  String result="Fail";
		  Repository	myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		  try {
				myRepository.initialize();
				RepositoryConnection con = myRepository.getConnection();	  	
		  		System.out.println(springlesrepositoryID);
		  		System.out.println(springlesserverURL);	
		  		System.out.println(includenotinferred);
	  			long numberOfStatements= con.size(); 
	  			
	  			String queryString = "clear graph <springles:clear-closure>";
	  			con.begin();
	  			Update insert = con.prepareUpdate(QueryLanguage.SPARQL,
	  							queryString);
	  			long startTime = System.currentTimeMillis();
	  			insert.execute();
	  			con.commit();
	  			result="Inferred statements were cleared";
	  				
				
	  			
	  			if(includenotinferred == 1){
	  				 con.begin();
	  				startTime = System.currentTimeMillis();
	  				RepositoryResult<Statement> st =  con.getStatements(null, null, null, true);
	  				Model mod  = Iterations.addAll(st, new LinkedHashModel());
	  				con.remove(mod);
  					con.commit();
  					result="200 OK";
	  			}
	  			
	  			long estimatedTime = System.currentTimeMillis() - startTime;
	  			System.out.println("elapsed time in millisecondi= "+estimatedTime);
				System.out.println("Number of statement= "+numberOfStatements);
	  			try {
					 final TupleQuery query =
					 con.prepareTupleQuery(QueryLanguage.SPARQL,
					                     "SELECT (COUNT(*) AS ?n) WHERE { ?s ?p ?o }");
					 query.setIncludeInferred(true);
					 long numberOfTotalStatements= ((Literal) Iterations.asList(query.evaluate()).get(0).getValue("n"))
						                     .longValue();
				
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
	  @Path("/delete")
	  @Produces(MediaType.TEXT_HTML)
	  public String delete(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL) 
	  {

		  
		  String result="Fail";
		  Repository	myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		  File dataRepo = myRepository.getDataDir();
		  RemoteRepositoryManager manager = new RemoteRepositoryManager(springlesserverURL);
		  try {
			manager.initialize();
			manager.removeRepository(springlesrepositoryID);
			
		}catch (RepositoryException | RepositoryConfigException e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			System.out.println("Repository deleted!");
			result = "Repository deleted!";
		}
		  
		   
	  	  return result;
	  }	 
	  
	  @GET
	  @Path("/contexts")
	  @Produces(MediaType.TEXT_HTML)
	  public String contexts(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL,
			  @QueryParam("includeinferred") int includeinferred) 
	  {

		  
		  String result="Fail";
		  Repository	myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		  Set<Resource> ct = null;
		  try {
			myRepository.initialize();
			RepositoryConnection con = myRepository.getConnection();
			RepositoryResult<Statement> st = con.getStatements(null, null, null, includeinferred == 1 ? true:false);
			Model mod = Iterations.addAll(st, new LinkedHashModel());
			ct = mod.contexts();
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			result = "<table  border='1'><th style='text-align:center;'>Graphs</th>";
			for(Resource r:ct){
				result += "<tr><td class='graph'>"+r.toString() + "<tr><td>";
			}
			result +="</table>";
		}
		  
		   
	  	  return result;
	  }	 
	  
	  
	  
	  @GET
	  @Path("/summary")
	  @Produces(MediaType.TEXT_HTML)
	  public String types(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL)
	  {

		  
		  String result="Fail";
		  Repository	myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		  try {
			myRepository.initialize();
			result = "";
			RemoteRepositoryManager manager = new RemoteRepositoryManager(springlesserverURL);
			manager.initialize();
			RepositoryInfo ri = manager.getRepositoryInfo(springlesrepositoryID);
			Repository r = manager.getRepository(springlesrepositoryID);
			RepositoryConfig rc = manager.getRepositoryConfig(springlesrepositoryID);
			
			RepositoryResult<Statement> st =  myRepository.getConnection().getStatements(null, null, null, true);
				Model mod  = Iterations.addAll(st, new LinkedHashModel());
			
			Graph g = new GraphImpl();
			rc.export(g);
			Iterator<Statement> conf = g.iterator();
			ArrayList<Statement> sts = new ArrayList<Statement>();
			RepositoryConnection con = myRepository.getConnection();
			TupleQuery query;
			String status="";
			try {
				query = con.prepareTupleQuery(QueryLanguage.SPARQL,
				                     "SELECT ?closurestatus {}");
				TupleQueryResult qresult = query.evaluate();
				status = qresult.next().getValue("closurestatus").stringValue();
				
			} catch (MalformedQueryException | QueryEvaluationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					            
			
			
			while(conf.hasNext())
				sts.add(conf.next());
			
			result ="<table style='font-size:0.9em;' >";
			result += "<tr><th>ID:</th><td>" + ri.getId()+
					"</td></tr><tr><th>Title:</th><td>" + ri.getDescription()+
					"</td></tr><tr><th>Location:</th><td>"+ri.getLocation()+
					"</td></tr><tr><th>Server:</th><td>"+manager.getServerURL()+
					"</td></tr><tr><th>Total statements:</th><td>"+ mod.size()+
					"</td></tr><tr><th>Explicit statements:</th><td>"+ myRepository.getConnection().size()+
					"</td></tr><tr><th>Inferred statements:</th><td>"+ (mod.size()-myRepository.getConnection().size())+
					"</td></tr><tr><th>Closure status:</th><td>"+status+
					"</td></tr><tr><th>Inferencer:</th><td>"+ sts.get(23).getObject().stringValue().split("#")[1] +
					"</td></tr><tr><th>Ruleset:</th><td>"+ sts.get(22).getObject().stringValue() +
					"</td></tr><tr><th>Inferred context prefix:</th><td>"+ sts.get(6).getObject().stringValue()+"</td></tr></table>";
					
		} catch (RepositoryException | RepositoryConfigException e) {
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
	  
	  
	  
	  @POST
	  @Path("/querysparql")
	  @Produces(MediaType.TEXT_HTML)

	  @Consumes(MediaType.MULTIPART_FORM_DATA)
		  public String getTuples(
				  @FormDataParam("repositoryID") String springlesrepositoryID,
				  @FormDataParam("serverURL") String springlesserverURL,
				  @FormDataParam("includeinferred") int includeinferred, 
				  @FormDataParam("querySPARQL") String query) {
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
				
				System.out.println(query);
				System.out.println("RES:"+tuples.size());
				
				for (BindingSet s : tuples) {
					result+="<tr><td class='subj'>"+(s.getValue("s") != null ? s.getValue("s").toString() : "")+"</td><td class='pred'>"+(s.getValue("p") != null ? s.getValue("p").toString() : " ")+"</td><td class='obj'>"+(s.getValue("o") != null ? s.getValue("o").toString() : " ")+"</td></tr>";
				}
				
				
				return result;
			}  
	  
	  	  
	  


			@GET
			@Path("/getRepositories")
			@Produces(MediaType.TEXT_HTML)
			public String getRepositories(
					  @QueryParam("springlesserverURL") String springlesserverURL)
			{
				 String result = "Fail";
				  RemoteRepositoryManager man = new RemoteRepositoryManager(springlesserverURL);
				  Iterator<RepositoryInfo> rs;
				try {
					man.initialize();
					rs = man.getAllRepositoryInfos().iterator();
					result="";
					int i=0;
					while(rs.hasNext()){
						RepositoryInfo ri = rs.next();
						if(i!=0)
							result += "<option value="+ri.getId()+">"+ri.getDescription()+"</option>";
						i++;
					}
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("RESULT"+result);
				return result;
			}	 
			
			
			 @GET
			  @Path("/getClosureStatus")
			  @Produces(MediaType.TEXT_HTML)
			 		  		
				  public String getClosureStatus(
						  @QueryParam("repositoryID") String springlesrepositoryID,
						  @QueryParam("serverURL") String springlesserverURL) {
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
								TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,  "SELECT ?closurestatus {}");
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
						
				
						
						for (BindingSet s : tuples) {
							result+= s.getValue("closurestatus").stringValue();
						}
						
						
						return result;
					}  
			   
			
			
			 @GET
			  @Path("/create_ruleset")
			  @Produces(MediaType.TEXT_HTML)
			  public String create_ruleset(
					  @QueryParam("newfilename") String filename,
					  @QueryParam("ruleset_path") String ruleset_path)
			  {
				 
				 System.out.println(filename);
				 
				 System.out.println(ruleset_path);
				 String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
				 springles_url = '/'+springles_url.split("/")[1]+'/'+springles_url.split("/")[2]+'/'+springles_url.split("/")[3]+'/'+springles_url.split("/")[4]+"/openrdf-sesame/WEB-INF/classes/";
				 
				 String contenuto="";

				 File file = new File(springles_url+filename);
				 if (file.exists()){
					 return "Ruleset with the same name is just in memory";
				 }
				try {
					if((contenuto = getFileContent(ruleset_path)).compareTo("-1") == 0)
						return "Reading Error!";
					if (file.createNewFile())	
						if(appendToFile(springles_url+filename, contenuto))
							if(appendToFile(springles_url+"META-INF/springles-rulesets","\n"+filename))
								return "Ruleset "+filename+" was uploaded!";
							else
								return "Uploading Error!";
					else
						return "Uploading Error!";
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 return "Error!";
				 
			  }
			 
			 @GET
			  @Path("/list_of_ruleset")
			  @Produces(MediaType.TEXT_HTML)
			  public String list_of_ruleset()
			  {
				 
				 String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
				 springles_url = '/'+springles_url.split("/")[1]+'/'+springles_url.split("/")[2]+'/'+springles_url.split("/")[3]+'/'+springles_url.split("/")[4]+"/openrdf-sesame/WEB-INF/classes/";
				 String contenuto;
				 if((contenuto = getFileContent(springles_url+"META-INF/springles-rulesets")).compareTo("-1") == 0)
					 return "Reading Error!";
				 else{
					String result="<table border='1'><tr><th>Name</th><th></th></tr>";
					 for(String s : contenuto.split("\n")){
						 result += "<tr><td><a id="+s+" class='ruleset'>"+s+"</a></td><td><a style='color:red;' class='delete' id='r_"+s+"'>Delete</a></td></tr>";
					 }
					 result+= "</table>";
					 return result;
				 }
					
			  }
			 
			 @GET
			  @Path("/delete_ruleset")
			  @Produces(MediaType.TEXT_HTML)
			  public String delete_ruleset(
					  @QueryParam("filename") String filename)
			  {
				 
				 String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
				 springles_url = '/'+springles_url.split("/")[1]+'/'+springles_url.split("/")[2]+'/'+springles_url.split("/")[3]+'/'+springles_url.split("/")[4]+"/openrdf-sesame/WEB-INF/classes/";
				 File ruleset = new File(springles_url+filename);
				 if(ruleset.exists())
				 {
					 if(!ruleset.delete())
						 return "Deleting Error!";
				 }else
					 return "File is not exists!";
					
				 System.out.println(springles_url+filename);
				 String contenuto;
				 if((contenuto = getFileContent(springles_url+"META-INF/springles-rulesets")).compareTo("-1") == 0)
					 return "Deleting Error!";
				 contenuto = contenuto.replaceAll(filename, "");
				 if(!replaceToFile(springles_url+"META-INF/springles-rulesets", contenuto))
					 return "Deleting Error!";
				 return "Ruleset was deleted!";
				 
				 
			  }
			 @GET
			  @Path("/content_of_ruleset")
			  @Produces(MediaType.TEXT_HTML)
			  public String content_of_ruleset(
					  @QueryParam("filename") String filename)
			  {
				 
				 String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
				 springles_url = '/'+springles_url.split("/")[1]+'/'+springles_url.split("/")[2]+'/'+springles_url.split("/")[3]+'/'+springles_url.split("/")[4]+"/openrdf-sesame/WEB-INF/classes/";
				 String contenuto;
				 if((contenuto = getFileContent(springles_url+filename)).compareTo("-1") == 0)
					 return "Reading Error!";
				 else{		
					 return contenuto;
				 }
					
			  }
			 
			 
			private String getFileContent(String path){
				String contenuto="";
				try {
		            // apre il file in lettura
		            FileReader filein = new FileReader(path);
		            
		            int next;
		            do {
		                next = filein.read(); // legge il prossimo carattere
		                
		                if (next != -1) { // se non e' finito il file
		                    char nextc = (char) next;
		                    contenuto += nextc;
		                }

		            } while (next != -1);
		            
		            filein.close(); // chiude il file
		            
		        } catch (IOException e) {
		            System.out.println(e);
		            return "-1";
		        }
			 
				 return contenuto;
			}
			
			private boolean appendToFile(String path,String contenuto){
				 FileWriter list;
					try {
						list = new FileWriter(path, true);
						list.write(contenuto);
						list.close();
						return true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
			}
			private boolean replaceToFile(String path,String contenuto){
				 FileWriter list;
					try {
						list = new FileWriter(path);
						list.write(contenuto);
						list.close();
						return true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
			}
}
