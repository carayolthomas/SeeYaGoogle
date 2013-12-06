package utils;


import java.util.ArrayList;
import java.util.List;

import main.ProcessingFiles;

import databasetable.*;

public class MemoryConnection {
	
	/** TEST NO BD */
	public static List<ConnectionTableDocument> tableDocument = new ArrayList<ConnectionTableDocument>();
	public static List<ConnectionTableOccurrence> tableOccurrence = new ArrayList<ConnectionTableOccurrence>();
	public static List<ConnectionTablePosition> tablePosition = new ArrayList<ConnectionTablePosition>();
	public static List<ConnectionTableTerme> tableTerme = new ArrayList<ConnectionTableTerme>();
	public static List<ConnectionTableConteneur> tableConteneur = new ArrayList<ConnectionTableConteneur>();
	
	public static int idTableDocument = 0;
	public static int idTableOccurrence = 1;
	public static int idTablePosition = 1;
	public static int idTableTerme = 1;
	public static int idTableConteneur = 0;
	/** TEST NO BD */
	
	/**
	 * Insert a word in a map
	 * @param pWord
	 */
	public static void insertWord(String pWord) {
		/** TODO : Change this */
		ConnectionTableTerme terme = new ConnectionTableTerme();
		terme.setNomTerme(pWord);
		/** Current conteneur */
		terme.setIdTerme(idTableTerme++);
		terme.setIdConteneur(ProcessingFiles.currentIdConteneur);
		tableTerme.add(terme);
		ProcessingFiles.currentIdTerme = idTableTerme;
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
		doc.setIdDocument(idTableDocument++);
		doc.setNomDocument(name);
		tableDocument.add(doc);
		ProcessingFiles.currentIdDocument = idTableDocument;
	}
	
	/**
	 * Insert a new recit in the database
	 */
	public static void insertRecit() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.RECIT);
		con.setXpathConteneur("");
		con.setIdDocument(idTableDocument);
		con.setIdConteneur(idTableConteneur++);
		tableConteneur.add(con);
		ProcessingFiles.currentIdConteneur = idTableConteneur;
	}
	
	/**
	 * Insert a new presentation in the database
	 */
	public static void insertPresentation() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.PRESENTATION);
		con.setXpathConteneur("");
		con.setIdDocument(idTableDocument);
		con.setIdConteneur(idTableConteneur++);
		tableConteneur.add(con);
		ProcessingFiles.currentIdConteneur = idTableConteneur;
	}
	
	/**
	 * Insert a new section in the database
	 */
	public static void insertSec() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.SEC);
		con.setXpathConteneur("");
		con.setIdDocument(idTableDocument);
		con.setIdConteneur(idTableConteneur++);
		tableConteneur.add(con);
		ProcessingFiles.currentIdConteneur = idTableConteneur;
	}
	
	/**
	 * Insert a new description in the database
	 */
	public static void insertDescription() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.DESCRIPTION);
		con.setXpathConteneur("");
		con.setIdDocument(idTableDocument);
		con.setIdConteneur(idTableConteneur++);
		tableConteneur.add(con);
		ProcessingFiles.currentIdConteneur = idTableConteneur;
	}
	
	/**
	 * Insert a new paragraphe in the database
	 */
	public static void insertP(String xpath) {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.P);
		con.setXpathConteneur(xpath);
		con.setIdDocument(idTableDocument);
		con.setIdConteneur(idTableConteneur++);
		tableConteneur.add(con);
		ProcessingFiles.currentIdConteneur = idTableConteneur;
	}
	
	/**
	 * Insert a new complements in the database
	 */
	public static void insertComplements() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.COMPLEMENTS);
		con.setXpathConteneur("");
		con.setIdDocument(idTableDocument);
		con.setIdConteneur(idTableConteneur++);
		tableConteneur.add(con);
		ProcessingFiles.currentIdConteneur = idTableConteneur;
	}
	
	/**
	 * Insert a new liste in the database
	 */
	public static void insertListe() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.LISTE);
		con.setXpathConteneur("");
		con.setIdDocument(idTableDocument);
		con.setIdConteneur(idTableConteneur++);
		tableConteneur.add(con);
		ProcessingFiles.currentIdConteneur = idTableConteneur;
	}
	
	/**
	 * Insert a new item in the database
	 */
	public static void insertItem() {
		ConnectionTableConteneur con = new ConnectionTableConteneur();
		con.setTypeConteneur(ProcessingFiles.ITEM);
		con.setXpathConteneur("");
		con.setIdDocument(idTableDocument);
		con.setIdConteneur(idTableConteneur++);
		tableConteneur.add(con);
		ProcessingFiles.currentIdConteneur = idTableConteneur;
	}
	
	/** Insert in occurrence */
	public static void updateOccurrence(String pWord) {
		/**
		 * 1- Select dans occurrence si le mot + currentConteneur existe déjà
		 * 2- Si oui, je prend la ligne correspondante et j'incrémente nbOccurrences
		 * 3- Sinon, j'ajoute une nouvelle ligne pour ce mot + currentConteneur et 
		 * je mets nbOccurrence à 1
		 */
		int lIndexOccurrenceFound = -1;
		int lCptOccurrence = 0;
		for(ConnectionTableOccurrence lOccurrence : tableOccurrence) {
			if(lOccurrence.getPkOccurrence().getIdConteneur() == idTableConteneur
					&& lOccurrence.getPkOccurrence().getNomTerme().equals(pWord)) {
				lIndexOccurrenceFound = lCptOccurrence;
				break;
			}
			lCptOccurrence++;
		}
		if(lIndexOccurrenceFound != -1) {
			tableOccurrence.get(lIndexOccurrenceFound).setNbOccurrence(tableOccurrence.get(lIndexOccurrenceFound).getNbOccurrence() + 1);
		} else {
			ConnectionTableOccurrence occ = new ConnectionTableOccurrence();
			PKOccurrence pkOcc = new PKOccurrence();
			pkOcc.setIdConteneur(idTableConteneur);
			pkOcc.setNomTerme(pWord);
			pkOcc.setIdDocument(idTableDocument);
			occ.setPkOccurrence(pkOcc);
			occ.setNbOccurrence(1);
			tableOccurrence.add(occ);
		}
		insertPosition();
	}
	
	private static void insertPosition() {
		ConnectionTablePosition pos = new ConnectionTablePosition();
		PKPosition pkPos = new PKPosition();
		pkPos.setIdConteneur(idTableConteneur);
		pkPos.setIdTerme(idTableTerme-1);
		pos.setPKPosition(pkPos);		
		pos.setPosition(++ProcessingFiles.currentWordPos);
		tablePosition.add(pos);
	}
}
