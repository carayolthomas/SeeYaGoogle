package utils;


import main.ProcessingFiles;

import org.hibernate.Session;
import org.hibernate.Transaction;

import databasetable.ConnectionTableConteneur;
import databasetable.ConnectionTableDocument;
import databasetable.ConnectionTableTerme;

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

	/**
	 * Insert a word in the database
	 * @param pWord
	 */
	public static void insertWord(String pWord) {
		ConnectionTableTerme terme = new ConnectionTableTerme();
		terme.setNomTerme(pWord);
		DatabaseConnection.s.save(terme);
	}

	/**
	 * Insert a new document in the database
	 * @param name
	 */
	public static void insertDocument(String name) {
		ConnectionTableDocument doc = new ConnectionTableDocument();
		doc.setNomDocument(name);
		DatabaseConnection.s.save(doc);
	}
	
	/**
	 * Insert a new presentation in the database
	 */
	public static void insertPresentation() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.PRESENTATION);
		con.setXpathConteneur("");
		con.setIdDocument(ProcessingFiles.currentIdDocument);
		DatabaseConnection.s.save(con);
	}

}
