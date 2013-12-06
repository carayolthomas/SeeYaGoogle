package main;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Query;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;


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
	
	public static List<String> stopList;
	
	public static int currentIdParent = 0;
	public static int currentIdElement = 0;
	public static int currentWordPos = 0;
	
	public static int currentIdDocument;
	public static int currentIdTerme;
	public static int currentIdConteneur;
	
	/**
	 * Parse all documents
	 */
	public static void parseAllDocuments() {
		/** Chargement de la stopList en mémoire */
		stopList = WordUtils.loadStopList();
		
		/** Liste de tous les fichiers */
		String[] allFiles = FileXML.checkAllXMLFiles("collection");

		/** Traitement des fichiers un par un */
		SAXBuilder sxb = new SAXBuilder();
		try {
			for (int fileId = 0; fileId < allFiles.length; fileId++) {
				/** Num fichier pour debug */
				System.out.println(allFiles[fileId]);
				Document document = sxb.build(new File(MAIN_DIRECTORY
						.getAbsolutePath() + "/collection/" + allFiles[fileId]));
				/** Sauvegarde du document dans la BD */
				MemoryConnection.insertDocument(allFiles[fileId]);
				/** Element racine (BALADE) */
				Element racine = document.getRootElement();
				parsingDocument(racine, allFiles[fileId]);
			}
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
				currentIdParent = 0;
				currentIdElement = 0;
				/** Traitement de la presentation : DONE*/
				traitementPresentation(currentElement);
				break;
			case RECIT:
				currentIdParent = 0;
				currentIdElement = 0;
				/** Traitement du recit : DOING*/
				traitementRecit(currentElement);
				break;
			case COMPLEMENTS:
				currentIdParent = 0;
				currentIdElement = 0;
				/** Traitement du Complement : DONE*/
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
	private static void traitementRecit(Element element) {
		/** Ajout du dans la table Conteneur */
		MemoryConnection.insertRecit();
		/** Traitement des fils de la presentation */
		List<Element> childFromRecit = element.getChildren();
		Iterator<Element> iteChildFromRecit = childFromRecit.iterator();
		while (iteChildFromRecit.hasNext()) {
			Element currentElement = iteChildFromRecit.next();
			switch (currentElement.getName()) {
			case SEC:
				/** Traitement d'une section : TOCHECK */
				traitementSec(currentElement);
				break;
			case P:
				/** Traitement d'un paragraphe : DONE */
				traitementP(currentElement);
				break;
			case PHOTO:
				/** Traitement d'une photo : TOCHECK */
				traitementTexte(currentElement);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Traitement d'une presentation
	 * @param element
	 */
	private static void traitementPresentation(Element element) {
		/** Ajout de la presentation dans la table Conteneur */
		MemoryConnection.insertPresentation();
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
				currentIdParent++;
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
	 * Traitement d'une section
	 * @param element
	 */
	private static void traitementSec(Element element) {
		currentIdParent++;
		currentIdElement = 0;
		/** Ajout de la section dans la table Conteneur */
		MemoryConnection.insertSec();
		/** Traitement des fils de la section */
		List<Element> childFromSection = element.getChildren();
		Iterator<Element> iteChildFromSection = childFromSection
				.iterator();
		while (iteChildFromSection.hasNext()) {
			Element currentElement = iteChildFromSection.next();
			switch (currentElement.getName()) {
			case PHOTO:
				/** Traitement de la photo */
				traitementTexte(currentElement);
				break;
			case P:
				/** Traitement du paragraphe */
				traitementP(currentElement);
				break;
			default:
				/** Traitement de PCDATA (Sous-titre) */
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
		/** Ajout de la description dans la table Conteneur */
		MemoryConnection.insertDescription();
		/** Traitement des fils de la description */
		List<Element> childFromDescription = element.getChildren();
		Iterator<Element> iteChildFromDescription = childFromDescription
				.iterator();
		while (iteChildFromDescription.hasNext()) {
			Element currentElement = iteChildFromDescription.next();
			/** Traitement du paragraphe */
			traitementP(currentElement);
		}
	}
	
	/**
	 * Traitement d'un compléments
	 * @param currentElement
	 */
	private static void traitementComplements(Element element) {
		/** Ajout du complément dans la table Conteneur */
		MemoryConnection.insertComplements();
		/** Traitement des fils de complément */
		List<Element> childFromComplements = element.getChildren();
		Iterator<Element> iteChildFromComplements = childFromComplements
				.iterator();
		while (iteChildFromComplements.hasNext()) {
			Element currentElement = iteChildFromComplements.next();
			/** Traitement du paragraphe */
			traitementP(currentElement);
		}
	}
	
	/**
	 * Traitement d'un paragraphe
	 * @param element
	 * @param nomDoc
	 */
	private static void traitementP(Element element) {
		currentIdElement++;
		/** Ajout de la description dans la table Conteneur */
		String xpath = ("(/BALADE[1]/" + element.getParentElement().getParentElement().getName() +
						   "[1]/" + element.getParentElement().getName() + "[" + currentIdParent + 
						   "]/P[" + currentIdElement + "])");
		MemoryConnection.insertP(xpath);
		/** Traitement des fils de la description */
		List<Element> childFromDescription = element.getChildren();
		Iterator<Element> iteChildFromDescription = childFromDescription
				.iterator();
		if(!iteChildFromDescription.hasNext()) {
			traitementTexte(element);
		} else {
			while (iteChildFromDescription.hasNext()) {
				Element currentElement = iteChildFromDescription.next();
				/**
				 * Si c'est une liste il faut aller plus loin pour chercher les différents items
				 * Sinon c'est juste du texte
				 */
				switch (currentElement.getName()) {
				case LISTE:
					/** Traitement de la description */
					traitementListe(currentElement);
					break;
				default:
					/** Traitement de PCDATA */
					traitementTexte(currentElement);
					break;
				}
			}
		}
	}
	
	/**
	 * Traitement d'une liste
	 * @param element
	 * @param nomDoc
	 */
	public static void traitementListe(Element element) {
		/** Ajout de la liste dans la table Conteneur */
		MemoryConnection.insertListe();
		/** Traitement des fils de la liste */
		List<Element> childFromListe = element.getChildren();
		Iterator<Element> iteChildFromListe = childFromListe
				.iterator();
		while (iteChildFromListe.hasNext()) {
			Element currentElement = iteChildFromListe.next();
			traitementItem(currentElement);
		}
	}
	
	/**
	 * Traitement d'un item
	 * @param element
	 * @param nomDoc
	 */
	public static void traitementItem(Element element) {
		/** Ajout de la liste dans la table Conteneur */
		MemoryConnection.insertItem();
		/** Traitement des fils de la liste */
		List<Element> childFromListe = element.getChildren();
		Iterator<Element> iteChildFromListe = childFromListe
				.iterator();
		while (iteChildFromListe.hasNext()) {
			Element currentElement = iteChildFromListe.next();
			traitementTexte(currentElement);
		}
	}

	/**
	 * Traitement de CDATA
	 * @param element
	 */
	public static void traitementTexte(Element element) {
		StringTokenizer tokenizer = new StringTokenizer(element.getText());
		currentWordPos = 0;
		while(tokenizer.hasMoreTokens()) {
			traitementWord(tokenizer.nextToken().toLowerCase());
		}
	}
	 
	/**
	 * Traitement sur un mot
	 * @param pWord
	 */
	public static void traitementWord(String pWord) {
		pWord = pWord.replaceAll("^-$", "");
		pWord = pWord.replace("(", "");
		pWord = pWord.replace(")", "");
		pWord = pWord.replace(",", "");
		pWord = pWord.replace("l'", "");
		pWord = pWord.replace("\n", "");
		pWord = pWord.replace(".", "");
		pWord = pWord.replace(":", "");
		pWord = pWord.replace("?", "");
		pWord = pWord.replace(" ", "");
		pWord = pWord.replace("\t", "");
		pWord = pWord.replace(">", "");
		pWord = pWord.replace("?", "");
		pWord = pWord.replace("\"", "");
		pWord = pWord.replace("", "");
		pWord = pWord.replace("d'", "");
		pWord = pWord.replace("c'", "");
		pWord = pWord.replace("s'", "");
		pWord = pWord.replace("n'", "");
		pWord = pWord.replace("'", "");
		pWord = pWord.replace("\"", "");
		pWord = pWord.replace("l'", "");
		pWord = pWord.replace("-", "");
		pWord = pWord.replace("", "");
		pWord = pWord.replace("", "");
		pWord = pWord.replace("«", "");
		pWord = pWord.replace("»", "");
		pWord = pWord.replaceAll("[ÀÁÂÃÄÅÇÑñÇçÈÉÊËÌÍÎÏÒÓÔÕÖØÙÚÛÜÝàáâãäåçèéêëìíîïðòóôõöøùúûüýÿ]", "");
		
		pWord = pWord.replaceAll("^[0-9]$", "");
		if(!WordUtils.isInStopList(pWord, stopList) && !pWord.equals("")) {
			MemoryConnection.insertWord(WordUtils.transformWord(pWord));
		}
	}

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseConnection.doConnect();
		parseAllDocuments();
		ProcessingCompute.doCompute();
		DatabaseConnection.insertListsMem();
		DatabaseConnection.t.commit();
		DatabaseConnection.endConnect();
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