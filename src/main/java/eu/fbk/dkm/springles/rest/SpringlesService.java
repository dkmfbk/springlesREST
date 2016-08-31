package eu.fbk.dkm.springles.rest;


import java.io.BufferedReader;
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





/**
 * SpringlesService is the REST service that allowed you to manage semantic data, by adding, deleting, 
 * applying the closure and other features.
 * @author Gaetano Calabrese, Christian Joppi 
 *
 */
@Path("/rest")
public class SpringlesService {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

    public  SpringlesService (){
    	super();
    }

    /**
     * Method to test Springles Service -
     * url : http://localhost:8080/SpringlesREST/rest/rest/hallo
     * @return message to a web page
     */
    @GET
	@Path("/hallo")
	@Produces(MediaType.TEXT_PLAIN)
	  public String sayPlainTextHello() {
	    return "Hello!It's SpringlesService";
	  }

	 
	  
	  
	  
	 /**
	  * Method to create a new springles repository with an ID and a Title -
	  * url : http://localhost:8080/SpringlesREST/rest/rest/create
	  * @param springlesrepositoryID ID of the new springles repository
	  * @param springlesserverURL URL of the springles server  
	  * @param springlesrepositorytitle title of new springles repository
	  * @return a response of the creation request.
	  */
	  @GET
	  @Path("/create")
	  @Produces(MediaType.TEXT_HTML)
	  public Response createRepository(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL,
			  @QueryParam("springlesrepositorytitle") String springlesrepositorytitle ) {
		  RemoteRepositoryManager manager = new RemoteRepositoryManager(springlesserverURL);
		  try {
			manager.initialize();
			boolean persist = true;
			SpringlesRepositoryFactory srf=new SpringlesRepositoryFactory();
			RepositoryImplConfig repositoryTypeSpec = srf.getConfig();
			
	
	
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
				valueMap.put("Repository title", springlesrepositorytitle);
				valueMap.put("Inferencer type", "VoidInferencer");
				String configString = ct.render(valueMap);
				System.out.println(configString);
	           g = Rio.parse(IOUtils.toInputStream(configString, "UTF-8"),"",RDFFormat.TURTLE);		
				
				
			}
			
			
			RepositoryConfig repConfig = new RepositoryConfig(springlesrepositoryID, repositoryTypeSpec);
		
			repConfig.parse(g, g.filter(null, RDF.TYPE, new URIImpl("http://www.openrdf.org/config/repository#Repository")).subjectResource());
			manager.addRepositoryConfig(repConfig);
			 
		} catch (RepositoryException |RDFParseException| IOException|RepositoryConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return Response.ok().build();	 

  }  
	  
	
	   
	
	  
	  
	  
	  /**
	   * Method to export statements of a springles repository -
	   * url : http://localhost:8080/SpringlesREST/rest/rest/fullexport
	   * @param springlesrepositoryID ID of the springles repository to export
	   * @param springlesserverURL URL of the springles server 
	   * @param exportformat format of export file ( trig, xml, ttl )
	   * @param includeinferred set to 1 if were included inferred statement in export file, 0 otherwise
	   * @return response of the export request
	   */
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
	      
	  /**
	   * Method to add statements to a springles repository from a file -
	   * url : http://localhost:8080/SpringlesREST/rest/rest/upload
	   * @param springlesrepositoryID ID of the springles repository
	   * @param springlesserverURL URL of the springles server 
	   * @param filetoupload inputstream of file to upload
	   * @param fileDetail details of file to upload
	   * @return OK if uploaded was successful, FAIL otherwise
	   */
	  @POST
	  @Path("/upload")
	  @Consumes(MediaType.MULTIPART_FORM_DATA)

	  @Produces(MediaType.TEXT_HTML)
	  public String addData(
			  @FormDataParam("springlesrepositoryID") String springlesrepositoryID,
			  @FormDataParam("springlesserverURL") String springlesserverURL,
			 @FormDataParam("filetoupload") InputStream filetoupload,
		     @FormDataParam("filetoupload") FormDataContentDisposition fileDetail)
			   {
		
	System.out.println(springlesrepositoryID);
	System.out.println(springlesserverURL);	  
	System.out.println(filetoupload);
		  

	String result ="FAIL";
	
	
	//String baseURI = "http://example.org/example/local";	

	
	
	RemoteRepositoryManager manager = new RemoteRepositoryManager(springlesserverURL);
		  
	String repositoryId = springlesrepositoryID;
	
	try {
		manager.initialize();
	
		Repository repository = manager.getRepository(repositoryId);
		RepositoryConnection con = repository.getConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(filetoupload));
	    con.add(filetoupload, "", RDFFormat.TRIG);
		//con.add(file, baseURI, RDFFormat.TRIG);
		result="200 OK";
	
	
	
	
	} catch (RepositoryException | RepositoryConfigException | RDFParseException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  
	  return result;
			   }
	  
	  
	  
	  
	  /**
	   * Method to update closure on statements of a springles repository -
	   * url : http://localhost:8080/SpringlesREST/rest/rest/computeclosure
	   * @param springlesrepositoryID ID of the springles repository
	   * @param springlesserverURL URL of the springles server 
	   * @param inferencer inferencer using to apply closure on statements
	   * @param ruleset ruleset using by inferencer to update closure
	   * @param bind variables binding has to apply to ruleset
	   * @param inferredcontext prefix of context of inferred statements
	   * @return OK if closure was successful, FAIL otherwise
	   */
	  @GET
	  @Path("/computeclosure")
	  @Produces(MediaType.TEXT_HTML)
	  public String computeClosure(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL,
			  @QueryParam("inferencer") String inferencer,
			  @QueryParam("ruleset") String ruleset,
			  @QueryParam("bindings") String bind,
			  @QueryParam("inferredcontext") String inferredcontext)
			
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
	  			
	  			String queryString ="";
	  			if(inferencer.compareTo("RDFProInferencer")==0){
	  				queryString = "clear graph <rdfpro:update-closure>";
	  				ruleset = "file:"+ ruleset;
	  			}else if(inferencer.compareTo("NaiveInferencer")==0){
	  				queryString = "clear graph <springles:update-closure>";
	  			}
	  			changeInferenceParameters(springlesserverURL, springlesrepositoryID, ruleset.replace(" ", "%20"), inferencer,bind,inferredcontext);

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
	  private void changeInferenceParameters(String serverURL,String repoID,String rulesetURI,String inferencer,String bind, String inferenceprefix){
		  try {
			  	RemoteRepositoryManager manager = new RemoteRepositoryManager(serverURL);
				manager.initialize();
				boolean persist = true;
				
				RepositoryImplConfig repositoryTypeSpec = manager.getRepositoryConfig(repoID).getRepositoryImplConfig();
				Model	g =null;
		
		
				try(
						InputStream url=getClass().getClassLoader().getResourceAsStream("springles.ttl");
					){
					
					String template = IOUtil.readString(new InputStreamReader(url,
							"UTF-8"));
					System.out.println(template);
					ConfigTemplate ct= new ConfigTemplate(template);
					Map<String, String> valueMap =new HashMap<String, String>();
					valueMap.put("Repository ID", repoID);
					valueMap.put("Ruleset", rulesetURI);
					valueMap.put("Repository title", manager.getRepositoryInfo(repoID).getDescription());
					valueMap.put("Inferencer type", inferencer);
					valueMap.put("Inferred context prefix", inferenceprefix);
					valueMap.put("Bindings", bind);
					String configString = ct.render(valueMap);
					System.out.println(configString);
		           g = Rio.parse(IOUtils.toInputStream(configString, "UTF-8"),"",RDFFormat.TURTLE);		
					
					
				}
				RepositoryConfig repConfig = new RepositoryConfig(repoID, repositoryTypeSpec);
			
				repConfig.parse(g, g.filter(null, RDF.TYPE, new URIImpl("http://www.openrdf.org/config/repository#Repository")).subjectResource());
				manager.addRepositoryConfig(repConfig);
		  } catch (RepositoryException |RDFParseException| IOException|RepositoryConfigException e) {
				e.printStackTrace();
			}
			 
	  }
	  
	  /**
	   * Method to clear statements of a springles repository -
	   * url : http://localhost:8080/SpringlesREST/rest/rest/clear 
	   * @param springlesrepositoryID ID of the springles repository
	   * @param springlesserverURL URL of springles server
	   * @param includenotinferred set to 1 to delete all repository statements, 0 to delete also inferred statements
	   * @return response of the clear request. 
	   */
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
	  
	  
	  /**
	   * Method to delete a springles repository -
	   * url : http://localhost:8080/SpringlesREST/rest/rest/delete
	   * @param springlesrepositoryID ID of the springles repository
	   * @param springlesserverURL URL of the springles server
	   * @return response of the delete request.
	   */
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
	  
	  /**
	   * Method to get the graphs contains in a springles repository -
	   * url : http://localhost:8080/SpringlesREST/rest/rest/contexts
	   * @param springlesrepositoryID ID of the springles repository
	   * @param springlesserverURL URL of the springles server 
	   * @param includeinferred set to 1 to include also inferred graphs
	   * @return a html table of the list of graphs 
	   */
	  @GET
	  @Path("/contexts")
	  @Produces(MediaType.APPLICATION_JSON)
	  public String contexts(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL,
			  @QueryParam("includeinferred") int includeinferred) 
	  {

		  
		  StringBuilder result = new StringBuilder();
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
			result.append("{\"graphs\":[");
			for(Resource r:ct){
				result.append("{\"graph_name\":\""+r.toString() + "\"},");
			}
			result.append("]}");
			result.deleteCharAt(result.lastIndexOf(","));
		}
		  
		   
	  	  return result.toString();
	  }	 
	  
	  
	  /**
	   * Method to get all information about a springles repository -
	   * url : http://localhost:8080/SpringlesREST/rest/rest/summary
	   * @param springlesrepositoryID ID of the springles repository
	   * @param springlesserverURL URL of the springles server
	   * @return a html table of all information about the repository  
	   */
	  @GET
	  @Path("/summary")
	  @Produces(MediaType.TEXT_HTML)
	  public String summary(
			  @QueryParam("springlesrepositoryID") String springlesrepositoryID,
			  @QueryParam("springlesserverURL") String springlesserverURL)
	  {

		  
		  StringBuilder result = new StringBuilder();
		  if(springlesrepositoryID.compareTo("null")==0)
		  {
			  result.append("{\"ID\":\"-\","+
						"\"Title\":\"-\","+
						"\"Location\":\"-\","+
						"\"Server\":\"-\","+
						"\"Total statements\":\"-\","+
						"\"Explicit statements\":\"-\","+
						"\"Inferred statements\":\"-\","+
						"\"Closure status\":\"-\","+
						"\"Last Inferencer\":\"-\","+
						"\"Last Ruleset\":\"-\","+
						"\"Inferred context prefix\":\"-\"}");		 
			  return result.toString();
			 }
		  else{
		  Repository	myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		  try {
			myRepository.initialize();
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
			
			String status = getClosureStatus(myRepository);
			
			
			while(conf.hasNext())
				sts.add(conf.next());
			String ruleset = sts.get(22).getObject().stringValue();
			if(sts.get(22).getObject().stringValue().lastIndexOf(File.separatorChar) != -1)
				ruleset = sts.get(22).getObject().stringValue().replaceAll(sts.get(22).getObject().stringValue().substring(0,sts.get(22).getObject().stringValue().lastIndexOf("/")+1),"");
			
				
			result.append("{\"ID\":\"" + ri.getId()+ "\","+
					"\"Title\":\"" + ri.getDescription()+"\","+
					"\"Location\":\""+ri.getLocation()+"\","+
					"\"Server\":\""+manager.getServerURL()+"\","+
					"\"Total statements\":\""+ mod.size()+"\","+
					"\"Explicit statements\":\""+ myRepository.getConnection().size()+"\","+
					"\"Inferred statements\":\""+ (mod.size()-myRepository.getConnection().size())+"\","+
					"\"Closure status\":\""+status+"\","+
					"\"Last Inferencer\":\""+sts.get(23).getObject().stringValue().split("#")[1]+"\","+
					"\"Last Ruleset\":\""+ ruleset.replace("%20", " ") +"\","+
					"\"Inferred context prefix\":\""+ sts.get(6).getObject().stringValue()+"\"}");
					
		} catch (RepositoryException | RepositoryConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
		   
	  	  return result.toString();
		  }
	  }	 
	  
	  
	  private String getClosureStatus(Repository r) throws RepositoryException{
		  RepositoryConnection con = r.getConnection();
			TupleQuery query;
			String status="";
			try {
				query = con.prepareTupleQuery(QueryLanguage.SPARQL,
				                     "SELECT ?closurestatus {}");
				TupleQueryResult qresult = query.evaluate();
				status = qresult.next().getValue("closurestatus").stringValue();
				return status;
				
			} catch (MalformedQueryException | QueryEvaluationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Fail";
			}
	  }
	  
	  
	  
	  
	  
	  /**
	   * Method to get number of statements in a springles repository -
	   * url : http://localhost:8080/SpringlesREST/rest/rest/nstatements
	   * @param springlesrepositoryID ID of the springles repository
	   * @param springlesserverURL URL of the springles server
	   * @param includeinferred set to 1 to include inferred statements, 0 otherwise
	   * @return number of statements
	   */
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
	  
	  
	  /**
	   * Method to execute a SPARQL query over the semantic data contains in a springles repository -
	   * url : http://localhost:8080/SpringlesREST/rest/rest/querysparql
	   * @param springlesrepositoryID ID of the springles repository
	   * @param springlesserverURL URL of the springles server
	   * @param includeinferred set to 1 to execute the query also over the inferred statements
	   * @param query SPARQL query to execute 
	   * @return a html table of the query result 
	   */
	  @POST
	  @Path("/querysparql")
	  @Produces(MediaType.APPLICATION_JSON)

	  @Consumes(MediaType.MULTIPART_FORM_DATA)
		  public String getTuples(
				  @FormDataParam("repositoryID") String springlesrepositoryID,
				  @FormDataParam("serverURL") String springlesserverURL,
				  @FormDataParam("includeinferred") int includeinferred, 
				  @FormDataParam("querySPARQL") String query) {
				List<BindingSet> tuples = new ArrayList<BindingSet>();
				StringBuilder result= new StringBuilder();
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
				char arr[] = {'s','p','o'};
				int i=0;
				result.append("{\"res\":[");
				for (BindingSet s : tuples) {
					i=0;
					result.append("{");
					for(String n:s.getBindingNames()){
						result.append("\""+arr[i]+"\":\""+n+"\",\""+arr[i]+"_val\":\""+(s.getValue(n) != null ? s.getValue(n).stringValue().toString().replace('"', ' ') : "")+"\",");
						i++;
					}
					int lastIndex = result.lastIndexOf(",");
					if (lastIndex != -1)
						result.deleteCharAt(lastIndex);
					result.append("},");
				}
				result.append("]}");
				int lastIndex = result.lastIndexOf(",");
				if (lastIndex != -1)
					result.deleteCharAt(lastIndex);
				System.out.println(result.toString());
				return result.toString();
			}  
	  
	  	  
	  

	  	/**
	  	 * Method to get all springles repositories contained in the springles server -
	  	 * url : http://localhost:8080/SpringlesREST/rest/rest/getRepositories 
	  	 * @param springlesserverURL URL of the springles server
	  	 * @return a list of repositories
	  	 */
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
			
			/**
			 * Method to get the closure status of a springles repository -
			 * url : http://localhost:8080/SpringlesREST/rest/rest/getClosureStatus
			 * @param springlesrepositoryID ID of the springles repository
			 * @param springlesserverURL URL of the springles server
			 * @return the required closure status 
			 */
			 @GET
			  @Path("/getClosureStatus")
			  @Produces(MediaType.TEXT_HTML)
			 		  		
				  public String getClosureStatus(
						  @QueryParam("repositoryID") String springlesrepositoryID,
						  @QueryParam("serverURL") String springlesserverURL) {
						List<BindingSet> tuples = new ArrayList<BindingSet>();
						 if(springlesrepositoryID.compareTo("null")==0)
						  {
							 return "error";
						  }
				
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
			   
			
			/**
			 * Method to create new ruleset  -
			 * url : http://localhost:8080/SpringlesREST/rest/rest/create_ruleset
			 * @param filename name of the new ruleset
			 * @param ruleset file contained all rules to uploaded
			 * @param inferencer inferencer which connect the ruleset
			 * @return response of the create request 
			 */
			 @POST
			  @Path("/create_ruleset")
			  @Consumes(MediaType.MULTIPART_FORM_DATA)
			  @Produces(MediaType.TEXT_HTML)
			  public String create_ruleset(
					  @FormDataParam("newfilename") String filename,
					  @FormDataParam("filetoupload") InputStream ruleset,
					  @FormDataParam("inferencer") String inferencer)
			  {
				 
				 System.out.println(filename);
				 String separator = "/";
				 BufferedReader reader = new BufferedReader(new InputStreamReader(ruleset));
				 String contenuto ="";
				 String tmp = "";
				 try {
					while((tmp=reader.readLine())!=null)
						 contenuto+= tmp+"\n";
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(inferencer.compareTo("RDFProInferencer")==0){
					 String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
					 
					 springles_url = springles_url.substring(5,springles_url.indexOf("webapps"+separator)+8)+"openrdf-sesame"+separator+"WEB-INF"
							 +separator+"classes"+separator;
					 
					 File file = new File(springles_url.replace("%20", " ")+filename);
					 if (file.exists()){
						 return "Ruleset with the same name is just in memory";
					 }
					try {
						if (file.createNewFile())	
							if(appendToFile(springles_url.replace("%20", " ")+filename, contenuto))
								if(appendToFile(springles_url.replace("%20", " ")+"META-INF"+separator+"rdfpro-rulesets",filename+"\n")){
									
									return "Ruleset "+filename+" was uploaded!";
								}
								else
									return "Uploading Error!";
						else
							return "Uploading Error!";
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 return "Error!";
				 }else if(inferencer.compareTo("NaiveInferencer")==0){
					 String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
					 springles_url = springles_url.substring(5,springles_url.indexOf("webapps"+separator)+8)+"openrdf-sesame"+separator+"WEB-INF"
							 +separator+"classes"+separator;
					 
					 File file = new File(springles_url.replace("%20", " ")+filename);
					 if (file.exists()){
						 return "Ruleset with the same name is just in memory";
					 }
					try {
						if (file.createNewFile())	
							if(appendToFile(springles_url.replace("%20", " ")+filename, contenuto))
								if(appendToFile(springles_url.replace("%20", " ")+"META-INF"+separator+"springles-rulesets",filename+"\n"))
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
				 
				 return "Error! [ Wrong inferencer ]";
			  }
	
			 
			 
			 
			/**
			 * Method to get all ruleset in memory connected to a inferecer  -
			 * url : http://localhost:8080/SpringlesREST/rest/rest/list_of_ruleset
			 * @param inferencer name of inferencer 
			 * @param springlesserverURL URL of the springles server
			 * @param springlesrepositoryID ID of a springles repository
			 * @return list of the required rulesets
			 */
			 @GET
			  @Path("/list_of_ruleset")
			  @Produces(MediaType.TEXT_HTML)
			  public String list_of_ruleset(
					  @QueryParam("inferencer") String inferencer,
					  @QueryParam("serverURL") String springlesserverURL,
					  @QueryParam("repositoryID") String springlesrepositoryID)
			  {
				 System.out.println(inferencer);
				 System.out.println(springlesserverURL);
				 System.out.println(springlesrepositoryID);
				 String separator = "/";
				 if(inferencer.compareTo("RDFProInferencer")==0){
					 String result = "";
					 String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
					 springles_url = springles_url.substring(5,springles_url.indexOf("webapps"+separator)+8)+"openrdf-sesame"+separator+"WEB-INF"
					 +separator+"classes"+separator;
					 
					 System.out.println(springles_url);
					 String contenuto;
					if((contenuto = getFileContent(springles_url.replace("%20", " ")+"META-INF"+separator+"rdfpro-rulesets")).compareTo("-1") == 0)
						 return "Reading Error!";
					 else{
						 if(contenuto.length() ==0)
							 return "";
						 for(String s : contenuto.split("\n")){
							 result += springles_url+s+"&"+s+"\n";
						 }
						 return result;
					 }
				 }else if (inferencer.compareTo("NaiveInferencer")==0){
					 String result="";
					
						List<BindingSet> tuples = new ArrayList<BindingSet>();
						try{
							 	Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
								myRepository.initialize();
								RepositoryConnection connection = myRepository.getConnection();
						try {
							TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,  "SELECT ?listOfRuleset {}");
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
					
					String list = "";
					System.out.println(tuples.size());
					for (BindingSet s : tuples) {						 
						list+= s.getValue("listofruleset").stringValue();
					}
					System.out.println(list);
					if(list.replaceAll("\\s+", "").compareTo("no-ruleset")==0)
						return "";
					for(String s : list.split("\n"))
					{
						 result += s.split("--")[1]+"&"+s.split("--")[0]+"\n";
					}
					return result;
					 
				 }
					 
				
				return "";
				
					
			  }
			 
			 /**
			  * Method to delete from memory a ruleset -
			  * url : http://localhost:8080/SpringlesREST/rest/rest/delete_ruleset
			  * @param filename name of the ruleset
			  * @param inferencer inferencer to which ruleset is connected
			  * @return response of the delete request
			  */
			 @GET
			  @Path("/delete_ruleset")
			  @Produces(MediaType.TEXT_HTML)
			  public String delete_ruleset(
					  @QueryParam("filename") String filename,
					  @QueryParam("inferencer") String inferencer)
			  {
				 System.out.println("DELETE "+ filename + " "+inferencer);
				 String separator = "/";
				 if(inferencer.compareTo("RDFProInferencer")==0){
					 String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
					 springles_url = springles_url.substring(5,springles_url.indexOf("webapps"+separator)+8)+"openrdf-sesame"+separator+"WEB-INF"
							 +separator+"classes"+separator;
					 File ruleset = new File(filename);
					
					 if(ruleset.exists())
					 {
						 if(!ruleset.delete())
							 return "Deleting Error!";
					 }else
						 return "File is not exists!";
						
					 
					 String contenuto;
					 if((contenuto = getFileContent(springles_url.replace("%20", " ")+"META-INF"+separator+"rdfpro-rulesets")).compareTo("-1") == 0)
						 return "Deleting Error!";
					 contenuto = contenuto.replaceAll(filename.substring(filename.lastIndexOf(separator)+1, filename.length())+"\n", "");
					 System.out.println(contenuto);
					 if(!replaceToFile(springles_url.replace("%20", " ")+"META-INF"+separator+"rdfpro-rulesets", contenuto))
						 return "Deleting Error!";
					 return "Ruleset was deleted!";
				 }else if(inferencer.compareTo("NaiveInferencer")==0){
					 String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
					 System.out.println(separator);
					 springles_url = springles_url.substring(5,springles_url.indexOf("webapps"+separator)+8)+"openrdf-sesame"+separator+"WEB-INF"
							 +separator+"classes"+separator;
					 File ruleset = new File(filename);
					
					 if(ruleset.exists())
					 {
						 if(!ruleset.delete())
							 return "Deleting Error!";
					 }else
						 return "File is not exists!";
						
					 
					 String contenuto;
					 
					 System.out.println(separator);
					 if((contenuto = getFileContent(springles_url.replace("%20", " ")+"META-INF"+separator+"springles-rulesets")).compareTo("-1") == 0)
						 return "Deleting Error!";
					 contenuto = contenuto.replaceAll(filename.substring(filename.lastIndexOf(separator)+1, filename.length())+"\n", "");
					 System.out.println(contenuto);
					 if(!replaceToFile(springles_url.replace("%20", " ")+"META-INF"+separator+"springles-rulesets", contenuto))
						 return "Deleting Error!";
					 return "Ruleset was deleted!";
				 }
				 return "Deleting Error [ Wrong Inferencer ]";
				 
				 
			  }
			 
			 
			 /**
			  * Method to get the content of a ruleset -
			  * url : http://localhost:8080/SpringlesREST/rest/rest/content_of_ruleset
			  * @param filename name of the ruleset
			  * @return the required content
			  */
			 @GET
			  @Path("/content_of_ruleset")
			  @Produces(MediaType.TEXT_HTML)
			  public String content_of_ruleset(
					  @QueryParam("filename") String filename)
			  {
				 
				 //String springles_url = getClass().getClassLoader().getResource("springles.ttl").toString();
				 //springles_url = '/'+springles_url.split("/")[1]+'/'+springles_url.split("/")[2]+'/'+springles_url.split("/")[3]+'/'+springles_url.split("/")[4]+"/openrdf-sesame/WEB-INF/classes/";
				 String contenuto;
				 filename = filename.replace("%20", " ");
				 if((contenuto = getFileContent(filename)).compareTo("-1") == 0)
					 return "Reading Error!";
				 else{
					 contenuto = contenuto.replaceAll("<", "&#60;");
					 contenuto = contenuto.replaceAll(">", "&#62;");
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
			
			@GET
			  @Path("/prova")
			  @Produces(MediaType.APPLICATION_JSON)
			  public String prova(
					  )
			  {
				 
					StringBuilder crunchifyBuilder = new StringBuilder();
					crunchifyBuilder.append("{ \"id\":\"ciao#:%45_123=?\"},{\"id\":\"ciao#:%45_123=?\"}");
					return crunchifyBuilder.toString();
					
			  }
}


