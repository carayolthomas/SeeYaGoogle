package utils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
 
public class HibernateUtils {
    private static final SessionFactory sessionFactory;
 
    // This code create a unique instance of the SessionFactory with the configuration file hibernate.cfg.xml
    static {
        try {
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (HibernateException ex) {
            throw new RuntimeException("configuration problem: " + ex.getMessage(), ex);
        }
    }
    // Return a new Hibernate session by calling the openSession method of the SessionFactory class
    public static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }
}