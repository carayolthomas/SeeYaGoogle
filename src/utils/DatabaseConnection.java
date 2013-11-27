package utils;


import java.util.List;

import main.ProcessingFiles;

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

	/**
	 * Insert a word in a map
	 * @param pWord
	 */
	public static void insertWord(String pWord) {
		/** TODO : Change this */
		ConnectionTableTerme terme = new ConnectionTableTerme();
		terme.setNomTerme(pWord);
		/** Current conteneur */
		terme.setIdConteneur(getLastIdConteneur());
		DatabaseConnection.s.save(terme);
		updateOccurrence(pWord);
	}
	
	/**
	 * Insert all words in the database
	 * @param pWord
	 */
	public static void insertAllWords() {
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
	 * Insert a new recit in the database
	 */
	public static void insertRecit() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.RECIT);
		con.setXpathConteneur("");
		con.setIdDocument(ProcessingFiles.currentIdDocument);
		DatabaseConnection.s.save(con);
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
	
	/**
	 * Insert a new section in the database
	 */
	public static void insertSec() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.SEC);
		con.setXpathConteneur("");
		con.setIdDocument(ProcessingFiles.currentIdDocument);
		DatabaseConnection.s.save(con);
	}
	
	/**
	 * Insert a new description in the database
	 */
	public static void insertDescription() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.DESCRIPTION);
		con.setXpathConteneur("");
		con.setIdDocument(ProcessingFiles.currentIdDocument);
		DatabaseConnection.s.save(con);
	}
	
	/**
	 * Insert a new paragraphe in the database
	 */
	public static void insertP(String xpath) {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.P);
		con.setXpathConteneur(xpath);
		con.setIdDocument(ProcessingFiles.currentIdDocument);
		DatabaseConnection.s.save(con);
	}
	
	/**
	 * Insert a new complements in the database
	 */
	public static void insertComplements() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.COMPLEMENTS);
		con.setXpathConteneur("");
		con.setIdDocument(ProcessingFiles.currentIdDocument);
		DatabaseConnection.s.save(con);
	}
	
	/**
	 * Insert a new liste in the database
	 */
	public static void insertListe() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.LISTE);
		con.setXpathConteneur("");
		con.setIdDocument(ProcessingFiles.currentIdDocument);
		DatabaseConnection.s.save(con);
	}
	
	/**
	 * Insert a new item in the database
	 */
	public static void insertItem() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.ITEM);
		con.setXpathConteneur("");
		con.setIdDocument(ProcessingFiles.currentIdDocument);
		DatabaseConnection.s.save(con);
	}
	
	/** Insert in occurrence */
	public static void updateOccurrence(String pWord) {
		/**
		 * 1- Select dans occurrence si le mot + currentConteneur existe déjà
		 * 2- Si oui, je prend la ligne correspondante et j'incrémente nbOccurrences
		 * 3- Sinon, j'ajoute une nouvelle ligne pour ce mot + currentConteneur et 
		 * je mets nbOccurrence à 1
		 */
		@SuppressWarnings("unchecked")
		List<ConnectionTableOccurrence> lOccurrence = s.createQuery("FROM ConnectionTableOccurrence WHERE " +
											  "idConteneur=" + getLastIdConteneur() + " AND " + 
											  "nomTerme='" + pWord + "'").list();
		/** To finish with Terme */
		if(!lOccurrence.isEmpty()) {
			lOccurrence.get(0).setNbOccurrence(lOccurrence.get(0).getNbOccurrence() + 1);
			DatabaseConnection.s.update(lOccurrence.get(0));
		} else {
			ConnectionTableOccurrence occ = new ConnectionTableOccurrence();
			PKOccurrence pkOcc = new PKOccurrence();
			pkOcc.setIdConteneur(getLastIdConteneur());
			pkOcc.setNomTerme(pWord);
			occ.setPkOccurrence(pkOcc);
			occ.setNbOccurrence(1);
			DatabaseConnection.s.save(occ);
		}
		insertPosition();
	}
	
	private static void insertPosition() {
		ConnectionTablePosition pos = new ConnectionTablePosition();
		PKPosition pkPos = new PKPosition();
		pkPos.setIdConteneur(getLastIdConteneur());
		pkPos.setIdTerme(getLastIdTerme());
		pos.setPKPosition(pkPos);		
		pos.setPosition(++ProcessingFiles.currentWordPos);
		DatabaseConnection.s.save(pos);
	}

	/** Get last idConteneur in the table Conteneur */
	public static int getLastIdConteneur() {
		@SuppressWarnings("unchecked")
		List<Integer> lIds = s.createSQLQuery("SELECT idConteneur from Conteneur "
				+ "ORDER BY idConteneur DESC "
				+ "LIMIT 1;").list();
		if(lIds.get(0) == null) {
			try {
				throw new Exception("Error during retrieving last conteneur Id");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lIds.get(0);
	}
	
	/** Get last idTerme in the table Terme */
	public static int getLastIdTerme() {
		@SuppressWarnings("unchecked")
		List<Integer> lIds = s.createSQLQuery("SELECT idTerme from Terme "
				+ "ORDER BY idTerme DESC "
				+ "LIMIT 1;").list();
		if(lIds.get(0) == null) {
			try {
				throw new Exception("Error during retrieving last conteneur Id");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lIds.get(0);
	}

}
