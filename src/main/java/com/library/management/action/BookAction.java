package com.library.management.action;

import com.library.management.dao.BookDAO;
import com.library.management.dao.BorrowedBookDAO;
import com.library.management.model.Book;
import com.library.management.model.BorrowedBook;
import com.library.management.util.HibernateUtil;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class BookAction extends ActionSupport {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(BookAction.class);
    
    private String title;
    private String author;
    private String genre;
    private int bookId;
    private int userId;
    private List<Book> books;
    private List<BorrowedBook> borrowedBooks;
    private String message;
    
    // Search action method
    public String search() {
        logger.info("Searching for books with title: {}, author: {}, genre: {}", title, author, genre);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        BookDAO bookDAO = new BookDAO(em);
        
        try {
            books = new ArrayList<>();
            
            // Handle different search criteria
            if (title != null && !title.isEmpty()) {
                Book book = bookDAO.fetchBookByTitle(title);
                if (book != null) {
                    books.add(book);
                }
            } else if (author != null && !author.isEmpty()) {
                books = bookDAO.fetchBookByAuthor(author);
            } else if (genre != null && !genre.isEmpty()) {
                books = bookDAO.fetchBookByGenre(genre);
            } else {
                // If no criteria specified, fetch all books
                books = bookDAO.fetchAllBooks();
            }
            
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error searching for books", e);
            message = "Error occurred while searching for books";
            return ERROR;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    // Borrow book action
    public String borrow() {
        logger.info("Borrowing book with ID: {} for user ID: {}", bookId, userId);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO(em);
        
        try {
            message = borrowedBookDAO.BorrowBook(bookId, userId);
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error borrowing book", e);
            message = "Error occurred while borrowing the book";
            return ERROR;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    // Return book action
    public String returnBook() {
        logger.info("Returning borrowed book with ID: {}", bookId);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO(em);
        
        try {
            // Assuming the bookId here is actually the borrowId
            borrowedBookDAO.updateReturnStatus(bookId, true, 0);
            message = "Book returned successfully";
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error returning book", e);
            message = "Error occurred while returning the book";
            return ERROR;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    // Get user's borrowed books
    public String getUserBooks() {
        logger.info("Fetching borrowed books for user ID: {}", userId);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        
        try {
            // This should be implemented in BorrowedBookDAO
            // For now, we're just getting all borrowed books
            BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO(em);
            borrowedBooks = borrowedBookDAO.fetchAllBorrowedBooks();
            // In a real implementation, you would filter by user ID
            
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error fetching user's borrowed books", e);
            message = "Error occurred while fetching borrowed books";
            return ERROR;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    // Getters and setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public List<Book> getBooks() {
        return books;
    }
    
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }
    
    public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
} 