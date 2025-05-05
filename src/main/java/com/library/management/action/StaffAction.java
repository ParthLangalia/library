package com.library.management.action;

import com.library.management.dao.*;
import com.library.management.model.*;
import com.library.management.util.*;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import javax.persistence.EntityManager;
import java.util.*;

public class StaffAction extends ActionSupport {
    private static final Logger logger = LogManager.getLogger(StaffAction.class);
    private static final long serialVersionUID = 1L;

    private int bookId;
    private int userId;
    private Book book;
    private User user;
    private List<Book> books;
    private List<User> users;
    private Map<String, Object> dashboardStats;
    private String message;
    private boolean success;
    private String email;
    private String password;

    // Getters and setters
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
    public Map<String, Object> getDashboardStats() { return dashboardStats; }
    public void setDashboardStats(Map<String, Object> dashboardStats) { this.dashboardStats = dashboardStats; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Dashboard statistics
    public String fetchDashboardStats() {
        Session session = null;
        EntityManager em = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            em = session.getEntityManagerFactory().createEntityManager();
            
            BookDAO bookDAO = new BookDAO();
            UserDAO userDAO = new UserDAO();
            BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO(em);

            dashboardStats = new HashMap<>();
            dashboardStats.put("totalBooks", bookDAO.fetchAllBooks().size());
            dashboardStats.put("totalUsers", userDAO.fetchAllUsers().size());
            dashboardStats.put("borrowedBooks", borrowedBookDAO.fetchAllBorrowedBooks().size());
            dashboardStats.put("overdueBooks", borrowedBookDAO.fetchAllBorrowedBooks().stream()
                .filter(book -> book.getDaysOverdue() > 0)
                .count());

            success = true;
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error getting dashboard stats: " + e.getMessage());
            success = false;
            message = "Error getting dashboard statistics";
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

    // Book management
    public String getAllBooks() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            BookDAO bookDAO = new BookDAO();
            books = bookDAO.fetchAllBooks();
            session.close();
            success = true;
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error getting all books: " + e.getMessage());
            success = false;
            message = "Error getting books list";
            return ERROR;
        }
    }

    public String fetchBook() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            BookDAO bookDAO = new BookDAO();
            book = bookDAO.fetchBookById(bookId);
            session.close();
            success = true;
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error getting book: " + e.getMessage());
            success = false;
            message = "Error getting book details";
            return ERROR;
        }
    }

    public String addBook() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            BookDAO bookDAO = new BookDAO();
            bookDAO.addBook(book);
            session.close();
            success = true;
            message = "Book added successfully";
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error adding book: " + e.getMessage());
            success = false;
            message = "Error adding book";
            return ERROR;
        }
    }

    public String updateBook() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            BookDAO bookDAO = new BookDAO();
            bookDAO.updateBook(book);
            session.close();
            success = true;
            message = "Book updated successfully";
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error updating book: " + e.getMessage());
            success = false;
            message = "Error updating book";
            return ERROR;
        }
    }

    public String deleteBook() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            BookDAO bookDAO = new BookDAO();
            bookDAO.deleteBook(bookId);
            session.close();
            success = true;
            message = "Book deleted successfully";
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error deleting book: " + e.getMessage());
            success = false;
            message = "Error deleting book";
            return ERROR;
        }
    }

    // User management
    public String getAllUsers() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            UserDAO userDAO = new UserDAO();
            users = userDAO.fetchAllUsers();
            session.close();
            success = true;
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error getting all users: " + e.getMessage());
            success = false;
            message = "Error getting users list";
            return ERROR;
        }
    }

    public String fetchUser() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            UserDAO userDAO = new UserDAO();
            user = userDAO.fetchUserById(userId);
            session.close();
            success = true;
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error getting user: " + e.getMessage());
            success = false;
            message = "Error getting user details";
            return ERROR;
        }
    }

    public String addUser() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            UserDAO userDAO = new UserDAO();
            userDAO.addUser(user);
            session.close();
            success = true;
            message = "User added successfully";
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error adding user: " + e.getMessage());
            success = false;
            message = "Error adding user";
            return ERROR;
        }
    }

    public String updateUser() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            UserDAO userDAO = new UserDAO();
            userDAO.updateUser(user);
            session.close();
            success = true;
            message = "User updated successfully";
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error updating user: " + e.getMessage());
            success = false;
            message = "Error updating user";
            return ERROR;
        }
    }

    public String deleteUser() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            UserDAO userDAO = new UserDAO();
            userDAO.deleteUser(userId);
            session.close();
            success = true;
            message = "User deleted successfully";
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error deleting user: " + e.getMessage());
            success = false;
            message = "Error deleting user";
            return ERROR;
        }
    }

    // Staff login
    public String staffLogin() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            UserDAO userDAO = new UserDAO();
            
            // Find user by email
            User staffUser = userDAO.fetchUserByEmail(email);
            
            if (staffUser != null && staffUser.getPassword().equals(password)) {
                if (staffUser.getRole().equals("STAFF")) {
                    // Set user info for JSON response
                    user = staffUser;
                    success = true;
                    message = "Login successful";
                    return SUCCESS;
                } else {
                    success = false;
                    message = "Access denied. Staff access only.";
                    return ERROR;
                }
            } else {
                success = false;
                message = "Invalid email or password";
                return ERROR;
            }
        } catch (Exception e) {
            logger.error("Staff login error: " + e.getMessage());
            success = false;
            message = "An error occurred during login";
            return ERROR;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
} 
