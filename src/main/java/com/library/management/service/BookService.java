package com.library.management.service;

import com.library.management.dao.BookDAO;
import com.library.management.model.Book;
import java.util.List;

public class BookService {
   private BookDAO bookDAO;
   
   public BookService() {
	super();
	this.bookDAO = new BookDAO();
   }
   // Create Book
   public void insertBook(Book book) {
       bookDAO.insertBook(book);
   }
   // Fetch Book by ID
   public Book getBookById(int bookId) {
       return bookDAO.fetchBookById(bookId);
   }
   // Fetch Book by Title
   public Book getBookByTitle(String title) {
       return bookDAO.fetchBookByTitle(title);
   }
   // Fetch Books by Author
   public List<Book> getBooksByAuthor(String author) {
       return bookDAO.fetchBookByAuthor(author);
   }
   // Fetch Books by Genre
   public List<Book> getBooksByGenre(String genre) {
       return bookDAO.fetchBookByGenre(genre);
   }
   // Fetch All Books
   public List<Book> getAllBooks() {
       return bookDAO.fetchAllBooks();
   }
   // Update Book
   public void updateBook(int bookId, String newTitle, String newAuthor, String newGenre, int newTotalCopies) {
       bookDAO.updateBook(bookId, newTitle, newAuthor, newGenre, newTotalCopies);
   }
   // Update Book by Title
   public void updateBookByTitle(String newAuthor, String newGenre, String newTitle, int newTotalCopies) {
       bookDAO.updateBookByTitle(newAuthor, newGenre, newTitle, newTotalCopies);
   }
   // Delete Book by ID
   public void deleteBook(int bookId) {
       bookDAO.deleteBook(bookId);
   }
   // Delete Book by Title
   public void deleteBookByTitle(String title) {
       bookDAO.deleteBookByTitle(title);
   }
   
}