package utils;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.jdom2.Element;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DatabaseConnection {
	
	private static Connection connection;
	
	/* Création de l'objet gérant les requêtes */
	private static Statement statement;
	
	public static void doConnect() {
		/** Connexion à la base de données */
		String url = "jdbc:mysql://localhost:3306/ri";
		String utilisateur = "root";
		String motDePasse = "root";
		try {
			connection = (Connection) DriverManager.getConnection( url, utilisateur, motDePasse );
			statement = (Statement) connection.createStatement();
		} catch (SQLException e) {
			connection = null;
			e.printStackTrace();
		}
	}
	
	public int insertDocument(Element document) {
		/** Exécution d'une requête d'écriture */
		try {
			return statement.executeUpdate( "INSERT INTO Document (nomDocument) VALUES ("+ document.getDocument() +");" );
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
