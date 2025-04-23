package com.library.management.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sf;
	
	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			
			configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
			configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/test_library_db");
			configuration.setProperty("hibernate.connection.username", "postgres");
			configuration.setProperty("hibernate.connection.password", "0000");
			
			sf = configuration.buildSessionFactory();
		}catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sf;
	}

}