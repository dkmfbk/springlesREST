package eu.fbk.dkm.springles.rest;
import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.util.RDFInserter;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;


class ChunkCommitter1 implements RDFHandler {
    
    private RDFInserter inserter;
    private RepositoryConnection conn;
     
    private long count = 0L;

    // do intermittent commit every 500,000 triples
    private long chunksize = 50000L;
     
    public ChunkCommitter1(RepositoryConnection conn) {
        inserter = new RDFInserter(conn);
        this.conn = conn;
    }

    @Override
    public void startRDF() throws RDFHandlerException {
        inserter.startRDF();
    }

    @Override
    public void endRDF() throws RDFHandlerException {
        inserter.endRDF();
    }

    @Override
    public void handleNamespace(String prefix, String uri)
            throws RDFHandlerException {
        inserter.handleNamespace(prefix, uri);
    }

    @Override
    public void handleStatement(Statement st) throws RDFHandlerException {
        inserter.handleStatement(st);
        count++;
        // do an intermittent commit whenever the number of triples
        // has reached a multiple of the chunk size
        if (count % chunksize == 0) { 
            try {                conn.commit();
           // conn.close();
            } catch (RepositoryException e) {
                throw new RDFHandlerException(e);
            }
        }
    }

    @Override
    public void handleComment(String comment) throws RDFHandlerException {
        inserter.handleComment(comment);
    }
}