package main;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import databasetable.*;
import utils.*;

public class ProcessingFiles {

	public static File MAIN_DIRECTORY = new File("/home/thomas/git/SeeYaGoogle/src/");
	//public static File MAIN_DIRECTORY = new File("/home/julie/Bureau/COURS/5IL/RI/SeeYaGoogle/src/");
	public final static String PRESENTATION = "PRESENTATION";
	public final static String RECIT = "RECIT";
	public final static String COMPLEMENTS = "COMPLEMENTS";
	public final static String TITRE = "TITRE";
	public final static String AUTEUR = "AUTEUR";
	public final static String DATE = "DATE";
	public final static String DESCRIPTION = "DESCRIPTION";
	public final static String P = "P";
	public final static String LISTE = "LISTE";
	public final static String SEC = "SEC";
	public final static String PHOTO = "PHOTO";
	public final static String SOUS_TITRE = "SOUS-TITRE";
	public final static String INFO = "INFO";
	public final static String ITEM = "ITEM";
	
	public static Query q;
	
	public static int currentIdDocument;
	public static List<String> stopList;

	
	/**
	 * Parse all documents
	 */
	public static void parseAllDocuments() {
		/** Chargement de la stopList en mémoire */
		stopList = WordUtils.loadStopList();
		
		/** Liste de tous les fichiers */
		String[] allFiles = FileXML.checkAllXMLFiles();

		/** Traitement des fichiers un par un */
		SAXBuilder sxb = new SAXBuilder();
		try {
			//for (int fileId = 0; fileId < allFiles.length; fileId++) {
				Document document = sxb.build(new File(MAIN_DIRECTORY
						.getAbsolutePath() + "/collection/" + allFiles[0]));
				/** Sauvegarde du document dans la BD */
				DatabaseConnection.insertDocument(allFiles[0]);
				/** MAJ currentIdDocument */
				setCurrentIdDocument(allFiles[0]);
				/** Element racine (BALADE) */
				Element racine = document.getRootElement();
				parsingDocument(racine, allFiles[0]);

			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse a specific document
	 * @param racine
	 * @param nomDoc
	 */
	public static void parsingDocument(Element racine, String nomDoc) {
		List<Element> childFromRacine = racine.getChildren();
		Iterator<Element> iteChildFromRacine = childFromRacine.iterator();
		while (iteChildFromRacine.hasNext()) {
			Element currentElement = iteChildFromRacine.next();
			switch (currentElement.getName()) {
			case PRESENTATION:
				/** Traitement de la presentation */
				traitementPresentation(currentElement);
				break;
			case RECIT:
				/** Traitement du recit */
				traitementRecit(currentElement);
				break;
			case COMPLEMENTS:
				/** Traitement du Complement */
				traitementComplements(currentElement);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Traitement d'un récit
	 * @param currentElement
	 */
	private static void traitementRecit(Element currentElement) {
		switch (currentElement.getName()) {
		case SEC:
			// traitementSec(currentElement, nomDoc);
			break;
		case P:
			// traitementP(currentElement, nomDoc);
			break;
		case PHOTO:
			// traitementPhoto(currentElement, nomDoc);
			break;
		default:
			break;
		}
	}

	/**
	 * Traitement d'une presentation
	 * @param element
	 */
	private static void traitementPresentation(Element element) {
		/** Ajout de la presentation dans la BD */
		DatabaseConnection.insertPresentation();
		/** Traitement des fils de la presentation */
		List<Element> childFromPresentation = element.getChildren();
		Iterator<Element> iteChildFromPresentation = childFromPresentation
				.iterator();
		while (iteChildFromPresentation.hasNext()) {
			Element currentElement = iteChildFromPresentation.next();
			/**
			 * Si c'est une description il faut aller plus loin pour chercher les différents paragraphes
			 * Sinon c'est juste du texte
			 */
			switch (currentElement.getName()) {
			case DESCRIPTION:
				/** Traitement de la description */
				traitementDescription(currentElement);
				break;
			default:
				/** Traitement de PCDATA */
				traitementTexte(currentElement);
				break;
			}
		}
	}
	
	/**
	 * Traitement d'une description
	 * @param element
	 */
	public static void traitementDescription(Element element) {
		
	}
	
	/**
	 * Traitement d'un compléments
	 * @param currentElement
	 */
	private static void traitementComplements(Element currentElement) {

	}
	
	/**
	 * Traitement d'un paragraphe
	 * @param element
	 * @param nomDoc
	 */
	private static void traitementP(Element element, String nomDoc) {

	}

	/**
	 * Traitement de CDATA
	 * @param element
	 */
	public static void traitementTexte(Element element) {
		String[] lWords = element.getText().split(" ");
		int lCptWords = 0 ;
		while(lCptWords < lWords.length) {
			traitementWord(lWords[lCptWords].toLowerCase());
			lCptWords++;
		}
	}
	 
	/**
	 * Traitement sur un mot
	 * @param pWord
	 */
	public static void traitementWord(String pWord) {
		if(!WordUtils.isInStopList(pWord, stopList)) {
			DatabaseConnection.insertWord(WordUtils.transformWord(pWord));
		}
	}

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseConnection.doConnect();
		parseAllDocuments();
		DatabaseConnection.t.commit();
		DatabaseConnection.endConnect();
	}
	
	/**
	 * Set the current id document
	 * @param pNomDocument
	 */
	public static void setCurrentIdDocument(String pNomDocument) {
		@SuppressWarnings("unchecked")
		List<Integer> lIds = DatabaseConnection.s.createQuery("SELECT idDocument FROM ConnectionTableDocument WHERE nomDocument='" + pNomDocument + "'").list();
		if(!lIds.isEmpty() && lIds.size() == 1) {
			currentIdDocument = lIds.get(0);
		} else {
			try {
				throw new Exception("Problem with setCurrentIdDocument");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}




/*
 * String requete1 =
 * "FROM ConnectionTableDocument doc where idDocument = 5"; Query q =
 * DatabaseConnection.s.createQuery(requete1); ConnectionTableDocument
 * ctd = (ConnectionTableDocument) q.uniqueResult(); //List results =
 * q.list(); System.out.println("Résultat :"+ctd.getIdDocument());
 */

// DatabaseConnection.t = DatabaseConnection.s.beginTransaction();


//INSERT INTO Conteneur (idDocument, typeConteneur) VALUES ((SELECT
	// idDocument FROM Document WHERE nomDocument="NOMDOCUMENT"),
	// "TYPECONTENEUR");

	/*
	 * public static int insertText(Element element, String typeConteneur,
	 * String nomDoc){ String mot; try { System.out.println(element.getText());
	 * 
	 * StringTokenizer st = new StringTokenizer(element.getText()); while
	 * (st.hasMoreTokens()) { mot = st.nextToken();
	 * resultat=statement.executeQuery
	 * ("SELECT * FROM Terme where nomTerme =\""+mot+"\";"); // resultat.next();
	 * System.out.println(mot); if (!resultat.next()){
	 * System.out.println("coucou  hibou"); }
	 * 
	 * System.out.println(st.nextToken()); } return statement.executeUpdate(
	 * "INSERT INTO Conteneur (idDocument, typeConteneur) VALUES ((SELECT idDocument FROM Document WHERE nomDocument=\""
	 * +nomDoc+"\"), \""+typeConteneur+"\");" );
	 * 
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); return -1; } }
	 */