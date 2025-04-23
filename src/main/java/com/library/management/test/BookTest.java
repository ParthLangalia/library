package com.library.management.test;

import com.library.management.dao.*;
import com.library.management.model.*;
import java.util.*;

import javax.persistence.*;

public class BookTest {
	public static void main(String[] a){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LibraryPU");
		EntityManager em1 = emf.createEntityManager();
		
		BookDAO bDAO = new BookDAO(em1);
		
//		inserting a book
//		Book newBook = new Book();
//		newBook.setAuthor("Parth Langalia");
//		newBook.setGenre("History");
//		newBook.setTitle("Adventure of a lifetime");
//		newBook.setTotalCopies(8);
//		bDAO.insertBook(newBook);
		
//		Book b2 = bDAO.fetchBookbyId(1);
//		System.out.println("Book fetched : " + b2);
		
//		List<Book> booksByAuthor = bDAO.fetchBookbyAuthor("Author AX");
//		System.out.println("Book fetched : " + booksByAuthor);
		
		
//		List<Book> booksOfGenre = bDAO.fetchBookbyGenre("Mystery");
//		System.out.println("Book fetched : " + booksOfGenre);
		
//		bDAO.deleteBookByTitle("Adventure of a lifetime");
		
//		bDAO.updateBookByTitle("AP", "History", "Book Title 1", 5);
		
//		List<Book> books = bDAO.fetchAllBooks();
//		for(Book book: books ) {
//		 	System.out.println(book);
//		}
		
		//updating book
//		int updateBookId = 1;
//		String newAuthor = "Updated Author";
//		String newGenre = "Updated Genre";
//		String newTitle = "Updated Title";
//		int newTotalCopies = 10;
//		
//		bDAO.updateBook(updateBookId, newAuthor, newGenre, newTitle, newTotalCopies);
//		
//		em1.close();
//		emf.close();
		
		//deleting a book
//		bDAO.deleteBook(10);
//		Book afterDeletion = bDAO.fetchBookbyId(10);
//		if(afterDeletion == null) {
//			System.out.println("book deleted succesfully");
//		}
		
		em1.close();
		emf.close();
	}
}