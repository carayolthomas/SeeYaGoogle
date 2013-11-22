package utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.jdom2.Element;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class DatabaseConnection {
	
	private static Connection connection;
	
	/* Création de l'objet gérant les requêtes */
	private static Statement statement;
	private static java.sql.ResultSet resultat;
	
	public static void doConnect() {
		/** Connexion à la base de données */
		String url = "jdbc:mysql://localhost:3306/RI";
		String utilisateur = "root";
		String motDePasse = "july31";
		try { 
			System.out.println("Loading driver..."); 
			Class.forName("com.mysql.jdbc.Driver"); 
			System.out.println("Driver loaded!"); 
			} catch (ClassNotFoundException e) { 
				throw new RuntimeException("Cannot find the driver in the classpath!", e); 
			}
		try {
			connection = (Connection) DriverManager.getConnection( url, utilisateur, motDePasse );
			statement = (Statement) connection.createStatement();
		} catch (SQLException e) {
			connection = null;
			e.printStackTrace();
		}
	}
	
	;

	//INSERT INTO Conteneur (idDocument, typeConteneur) VALUES ((SELECT idDocument FROM Document WHERE nomDocument="NOMDOCUMENT"), "TYPECONTENEUR");

	public static int insertText(Element element, String typeConteneur, String nomDoc){
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
		    	// System.out.println(resultat.getRow());
		    	 //probleme, ne sait pas comment recuperer le resultat de la requete pour savoir si deja dans base
		    	 /*
		    	 if (statement.executeQuery("SELECT \"Voyage\" FROM Terme;")==null) {
		    		 System.out.println("bbbbbbbbbbbbb");
		    	 }else {
		    		 System.out.println("eeeeeeeeeeeeeeeeeeeeeeeee");
		    	 }*/
		    	 
		    	// if (resultat.next())
		    	// System.out.println(resultat.first());
		         System.out.println(st.nextToken());
		     }
			return statement.executeUpdate( "INSERT INTO Conteneur (idDocument, typeConteneur) VALUES ((SELECT idDocument FROM Document WHERE nomDocument=\""+nomDoc+"\"), \""+typeConteneur+"\");" );
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	//Parse le texte mot par mot, verifie si le mot n'est pas deja dans la table terme
	// Actualiser table occurence 
	//Actualiser table position
	public static void parseText (Element element, String typeConteneur, String nomDoc) {
		
	}
	
	public static int insertDocument(String name) {
		/** Exécution d'une requête d'écriture */
		try {
			return statement.executeUpdate( "INSERT INTO Document (nomDocument) VALUES (\""+name+"\");" );
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
