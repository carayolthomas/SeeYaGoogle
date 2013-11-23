package utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdom2.Element;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class DatabaseConnection {
	
	public static Session s;
	public static Transaction t;
	
	
	
	public static void doConnect() {
		/** Connexion à la base de données */		
		
		s = HibernateUtils.getSession();		 
        // Start transaction
        t = s.beginTransaction();  
        /** TODO : begin transaction ici aussi ?! */

	}
	
	public static void endConnect () {
		s.close();
	}


	

	

}
