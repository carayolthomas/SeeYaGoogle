package main;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import utils.*;

public class ProcessingFiles {

	public static File MAIN_DIRECTORY = new File("/home/thomas/git/SeeYaGoogle/src/");
	//public static File MAIN_DIRECTORY = new File("/home/julie/Bureau/COURS/5IL/RI/SeeYaGoogle/src/collection");
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
	
	public static ConnectionTableDocument doc;
	public static Query q;
	
	public static void parseAllDocuments() {
		/** Liste de tous les fichiers */
		String[] allFiles = FileXML.checkAllXMLFiles();
		
		/** Traitement des fichiers un par un */
		SAXBuilder sxb = new SAXBuilder();
		try {
			for(int fileId = 0 ; fileId < allFiles.length; fileId++) {
				Document document = sxb.build(new File(MAIN_DIRECTORY.getAbsolutePath() + "/" + allFiles[fileId]));
				/** Sauvegarde du document dans la BD */
				insertDocument(allFiles[fileId]);
				/** Element racine (BALADE) */
				Element racine = document.getRootElement();
				//parsingDocument(racine, allFiles[fileId]); 
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void parsingDocument(Element racine, String nomDoc)
	{
	   List<Element> childFromRacine = racine.getChildren();
	   Iterator<Element> iteChildFromRacine = childFromRacine.iterator();
	   while(iteChildFromRacine.hasNext()) {
		   Element currentElement = iteChildFromRacine.next();
		   switch (currentElement.getName()) {
			case PRESENTATION:
				//traitementPresentation(currentElement, nomDoc);
				System.out.println(nomDoc);
				break;
			case RECIT:
				traitementRecit(currentElement, nomDoc);
				break;
			case COMPLEMENTS:
				//traitementComplements(currentElement, nomDoc);
				break;
			default:
				break;
			}
	   }
	}
	
	private static void traitementComplements(Element currentElement) {
		
	}

	private static void traitementRecit(Element currentElement, String nomDoc) {
		switch (currentElement.getName()) {
		case SEC:
			//traitementSec(currentElement, nomDoc);
			
			break;
		case P:
			//traitementP(currentElement, nomDoc);
			break;
		case PHOTO:
			//traitementPhoto(currentElement, nomDoc);
			break;
		default:
			break;
		}
	}

	
	private static void traitementP(Element element, String nomDoc){
		
		
		
	}
	
	private static void traitementPresentation(Element element, String nomDoc) {
		List<Element> childFromPresentation = element.getChildren();
		Iterator<Element> iteChildFromPresentation = childFromPresentation
				.iterator();
		while (iteChildFromPresentation.hasNext()) {
			Element currentElement = iteChildFromPresentation.next(); 
			//traitementTexte(currentElement, currentElement.getName(), nomDoc);
			
		}
	}

	/*private static void traitementTexte(Element element, String typeConteneur, String nomDoc) {
		DatabaseConnection.insertText(element, typeConteneur, nomDoc);
		
	}*/
	
	
	
	public static void insertDocument(String name) {
	//Exécution d'une requête d'écriture 
		ConnectionTableDocument doc = new ConnectionTableDocument();
		doc.setNomDocument(name);
		DatabaseConnection.s.save(doc);
	//	DatabaseConnection.t.commit();
		/** TODO : commit a la fin, pd transaction globale ?? */
		
	}

	//INSERT INTO Conteneur (idDocument, typeConteneur) VALUES ((SELECT idDocument FROM Document WHERE nomDocument="NOMDOCUMENT"), "TYPECONTENEUR");

	/*public static int insertText(Element element, String typeConteneur, String nomDoc){
		String mot;
		try {
			System.out.println(element.getText());
			
			StringTokenizer st = new StringTokenizer(element.getText());
		     while (st.hasMoreTokens()) {
		    	 mot = st.nextToken();
		    	 resultat=statement.executeQuery("SELECT * FROM Terme where nomTerme =\""+mot+"\";");
		    	// resultat.next();
		    	 System.out.println(mot);
		    	 if (!resultat.next()){
		    		 System.out.println("coucou  hibou");
		    	 }
		    	
		         System.out.println(st.nextToken());
		     }
			return statement.executeUpdate( "INSERT INTO Conteneur (idDocument, typeConteneur) VALUES ((SELECT idDocument FROM Document WHERE nomDocument=\""+nomDoc+"\"), \""+typeConteneur+"\");" );
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}*/
	public static void main(String[] args) {
		DatabaseConnection.doConnect();
		
		
		doc = new ConnectionTableDocument();
		
		parseAllDocuments();
		/*String requete1 = "FROM ConnectionTableDocument doc where idDocument = 5";
		Query q = DatabaseConnection.s.createQuery(requete1);
		ConnectionTableDocument ctd = (ConnectionTableDocument) q.uniqueResult();
		//List results = q.list();
		System.out.println("Résultat :"+ctd.getIdDocument());*/
		
		
		
		//DatabaseConnection.t = DatabaseConnection.s.beginTransaction();
		DatabaseConnection.endConnect();
		
	}
}
