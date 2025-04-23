package com.library.management.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

import com.library.management.model.Book;
import com.library.management.model.User;
import com.library.management.util.HibernateUtil;

public class HibernateTest {
    
    public static void main(String[] args) {
        System.out.println("Starting Hibernate test...");
        
        try (SessionFactory factory = HibernateUtil.getSessionFactory();
             Session session = factory.openSession()) {
            
            System.out.println("Connected to database successfully!");
            
            // Test queries
            System.out.println("\nBooks in database:");
            Query<Book> bookQuery = session.createQuery("from Book", Book.class);
            List<Book> books = bookQuery.getResultList();
            books.forEach(System.out::println);
            
            System.out.println("\nUsers in database:");
            Query<User> userQuery = session.createQuery("from User", User.class);
            List<User> users = userQuery.getResultList();
            users.forEach(System.out::println);
            
            System.out.println("\nTest completed successfully!");
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
