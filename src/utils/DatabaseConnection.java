package utils;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import databasetable.*;

public class DatabaseConnection {
	
	public static Session s;
	public static Transaction t;
	
	/**
	 * Init the database connection
	 */
	public static void doConnect() {
		/** Connexion à la base de données */		
		s = HibernateUtils.getSession();		 
        // Start transaction
        t = s.beginTransaction();  
	}
	
	/**
	 * End the database connection
	 */
	public static void endConnect () {
		s.close();
	}
	
	public static void insertListsMem() {
		for(ConnectionTableDocument lDocument : MemoryConnection.tableDocument) {
			//System.out.println("INSERT DOCUMENT : " + lDocument.toString());
			s.save(lDocument);
		}
		for(ConnectionTableTerme lTerme : MemoryConnection.tableTerme) {
			//System.out.println("INSERT TERME : " + lTerme.toString());
			s.save(lTerme);
		}
		for(ConnectionTableConteneur lConteneur : MemoryConnection.tableConteneur) {
			//System.out.println("INSERT CONTENEUR : " + lConteneur.toString());
			s.save(lConteneur);
		}
		for(ConnectionTableOccurrence lOccurrence : MemoryConnection.tableOccurrence) {
			//System.out.println("INSERT OCCURRENCE : " + lOccurrence.toString());
			s.save(lOccurrence);
		}
		for(ConnectionTablePosition lPosition : MemoryConnection.tablePosition) {
			//System.out.println("INSERT POSITION : " + lPosition.toString());
			s.save(lPosition);
		}
	}
	
	@SuppressWarnings("unchecked")
    public static List<ConnectionTableOccurrence> getOccurrenceRowByDocument() {
            return s.createQuery("FROM ConnectionTableOccurrence").list();
    }
}
