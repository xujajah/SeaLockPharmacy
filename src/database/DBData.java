package database;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class DBData {

	private static SessionFactory factory ;
	public static SessionFactory createSessionFactory() {
	    Configuration configuration = new Configuration();
	    configuration.configure();
	    ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(
	            configuration.getProperties()). buildServiceRegistry();
	    factory = configuration.buildSessionFactory(serviceRegistry);
	    return factory;
	}

	public static Session getSession() {
		return factory.openSession();
	}

}
