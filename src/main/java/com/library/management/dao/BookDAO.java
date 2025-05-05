package com.library.management.dao;

import javax.persistence.*;
import javax.persistence.Query;

import java.util.*;
import org.hibernate.*;
import com.library.management.model.*;
import com.library.management.util.*;

public class BookDAO {
	private EntityManager em1;

	public BookDAO(EntityManager em1) {
		super();
		this.em1 = em1;
	}
	
	//CRUD FOR BOOKS

	public BookDAO() {
		super();
	}

	//CREATE OPERATIONS
	//CREATE BOOK - 'C'
	public void insertBook(Book newBook) {
		EntityTransaction transaction = em1.getTransaction();
		try {
			transaction.begin();
			em1.persist(newBook);
			transaction.commit();
		} catch (Exception e) {
			if(transaction.isActive()){
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
	
	
	//UPDATE OPERATIONS
	//UPDATE USER - 'U'
	public void updateBook(int bookId, String newTitle, String newAuthor, String newGenre, int newTotalCopies) {
		EntityTransaction transaction = em1.getTransaction();
		try {
			transaction.begin();
			Book newBook = em1.find(Book.class, bookId);
			if(newBook != null){
				if(newAuthor != null && !newAuthor.isEmpty()){
					newBook.setAuthor(newAuthor);
				}
				if(newGenre != null && !newGenre.isEmpty()){
					newBook.setGenre(newGenre);
				}
				if(newTitle != null && !newTitle.isEmpty()){
					newBook.setTitle(newTitle);
				}
				if(newTotalCopies >= 0){
					newBook.setTotalCopies(newTotalCopies);
				}	
				em1.merge(newBook);
			}else {
				System.out.println("Book" + bookId  +"is not present in the database!");
			}
			
		}catch(Exception e) {
			if(transaction.isActive()){
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}	
	
	//update book by title
	public void updateBookByTitle(String newAuthor, String newTitle, String newGenre,  int newTotalCopies){
		em1.getTransaction().begin();
		Query query = em1.createQuery("UPDATE Book b SET b.author = :newAuthor, b.genre = :newGenre, b.totalCopies = :newTotalCopies WHERE b.title = :newTitle");
		query.setParameter("newAuthor", newAuthor);
		query.setParameter("newGenre", newGenre);
		query.setParameter("newTotalCopies", newTotalCopies);
		query.setParameter("newTitle", newTitle);
		int updatedCount = query.executeUpdate();
		em1.getTransaction().commit();
		System.out.println(updatedCount + " book(s) updated by title");
	}
	
	
	//READ OPERATIONS
	//fetch books by id - 'R'
	public Book fetchBookById(int bookId) {
		Book newBook = null;
		try{
			newBook = em1.find(Book.class, bookId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return newBook;
	}
	
	//fetch books by title - 'R'
	public Book fetchBookByTitle(String newTitle) {
		TypedQuery<Book> query = em1.createQuery("SELECT b FROM Book b WHERE b.title = : title", Book.class);
		query.setParameter("title", newTitle);
		return query.getSingleResult();
		
		
	}
	
	//fetch books by authorName - 'R'
	public List<Book> fetchBookByAuthor(String newAuthor) {
		TypedQuery<Book> query = em1.createQuery("SELECT b FROM Book b WHERE b.author = : author", Book.class);
		query.setParameter("author", newAuthor);
		List<Book> books = query.getResultList();
		
		if(books.isEmpty()) {
			System.out.println("No books found!");
		}else {
			System.out.println("Books by Author : " + newAuthor);
			for (Book book: books) {
				System.out.println(book);
			}
		}	
		return books;
	}
	
	//fetch books by Genre - 'R'
	public List<Book> fetchBookByGenre(String newGenre) {
		TypedQuery<Book> query = em1.createQuery("SELECT b FROM Book b WHERE b.genre = : genre", Book.class);
		query.setParameter("genre", newGenre);
		List<Book> books = query.getResultList();
			
		if(books.isEmpty()) {
			System.out.println("No books found!");
		}else {
			System.out.println("Books of Genre : " + newGenre);
			for (Book book: books) {
				System.out.println(book);
			}
		}	
		return books;
	}

	//fetch all books
	public List<Book> fetchAllBooks() {
		List<Book> bookList= null;
		try{
			TypedQuery<Book> query = em1.createQuery("FROM Book", Book.class);
			bookList = query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return bookList;
	}
	
	//DELETE OPERATIONS
	//DELETE BOOK - 'D'
	public void deleteBook(int bookId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Book book = session.get(Book.class, bookId);
			if (book != null) {
				session.delete(book);
			} else {
				System.out.println("Book not present in db");
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void deleteBookByTitle(String title) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			String hql = "DELETE FROM Book WHERE title = :title";
			int deletedCount = session.createQuery(hql)
					.setParameter("title", title)
					.executeUpdate();
			transaction.commit();
			System.out.println(deletedCount + " book(s) deleted by title");	
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	public void addBook(Book book) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(book);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	public void updateBook(Book book) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(book);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}
}
