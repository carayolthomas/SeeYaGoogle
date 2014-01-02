package sparqlclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SparqlUtil {

    /**
     * @param args the command line arguments
     */
	
	/**
	 * 1- On regarde si le mot clé passé en param correspond à un rdfs label dans la base de connaissance
	 * 2- Si oui, on check les properties de ce label
	 * 3- On détermine ainsi les termes synonymes de ce label
	 */
    public static ArrayList<String> requestToSPARQL(List<String> research) {
        SparqlClient sparqlClient = new SparqlClient("localhost:3030");

        String query = "ASK WHERE { ?s ?p ?o }";
        boolean serverIsUp = sparqlClient.ask(query);
        if (serverIsUp) {
            System.out.println("server is UP");
            ArrayList<String> newWords = new ArrayList<String>();
            for(String word : research) {
	            query = "PREFIX : <http://ontologies.alwaysdata.net/space#>\n"
	            		+ "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
	            		+"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
	            		+ "PREFIX owl:  <http://www.w3.org/2002/07/owl#>"
	            		+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
	                    + "SELECT ?mot WHERE\n"
	                    + "{\n"
	                    + "    ?uri rdfs:label \"" + word + "\"@fr.\n"
	                    + "    ?classuri rdfs:subClassOf ?uri.\n"
	                    + "	   ?classuri rdfs:label ?mot."
	                    + "}\n";
	            Iterable<Map<String, String>> results = sparqlClient.select(query);
	            for (Map<String, String> result : results) {
	            	newWords.add(result.get("mot"));
	            }
            }
            return newWords;
        } else {
            System.out.println("service is DOWN");
            return null;
        }
    }
}
