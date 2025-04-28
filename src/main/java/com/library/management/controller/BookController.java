package com.library.management.controller;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.library.management.model.Book;
import com.library.management.model.BorrowedBook;
import com.library.management.model.User;
import com.library.management.util.HibernateUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BookController {
    
    // Search books by title, author, and/or genre
    public static void searchBooks(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String genre = request.getParameter("genre");
        
        StringBuilder hql = new StringBuilder("FROM Book WHERE 1=1");
        
        if (title != null && !title.trim().isEmpty()) {
            hql.append(" AND lower(title) LIKE lower(:title)");
        }
        
        if (author != null && !author.trim().isEmpty()) {
            hql.append(" AND lower(author) LIKE lower(:author)");
        }
        
        if (genre != null && !genre.trim().isEmpty()) {
            hql.append(" AND lower(genre) LIKE lower(:genre)");
        }
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery(hql.toString(), Book.class);
            
            if (title != null && !title.trim().isEmpty()) {
                query.setParameter("title", "%" + title.trim() + "%");
            }
            
            if (author != null && !author.trim().isEmpty()) {
                query.setParameter("author", "%" + author.trim() + "%");
            }
            
            if (genre != null && !genre.trim().isEmpty()) {
                query.setParameter("genre", "%" + genre.trim() + "%");
            }
            
            List<Book> books = query.getResultList();
            
            // Convert the result to JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonResult = mapper.writeValueAsString(books);
            
            // Set response content type and write JSON to response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResult);
        }
    }
    
    // Get borrowed books for a user
    public static void getBorrowedBooks(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int userId = Integer.parseInt(request.getParameter("userId"));
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM BorrowedBook WHERE user.userId = :userId AND returned = false";
            Query<BorrowedBook> query = session.createQuery(hql, BorrowedBook.class);
            query.setParameter("userId", userId);
            
            List<BorrowedBook> borrowedBooks = query.getResultList();
            
            // Convert the result to JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonResult = mapper.writeValueAsString(borrowedBooks);
            
            // Set response content type and write JSON to response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResult);
        }
    }
    
    // Borrow a book
    // public static void borrowBook(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //     int bookId = Integer.parseInt(request.getParameter("bookId"));
    //     int userId = Integer.parseInt(request.getParameter("userId"));
        
    //     Transaction transaction = null;
    //     Map<String, Object> result = new HashMap<>();
        
    //     try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    //         // Start transaction
    //         transaction = session.beginTransaction();
            
    //         // Get book and user
    //         Book book = session.get(Book.class, bookId);
    //         User user = session.get(User.class, userId);
            
    //         if (book == null || user == null) {
    //             result.put("success", false);
    //             result.put("message", "Book or user not found");
    //         } else if (book.getAvailableCopies() <= 0) {
    //             result.put("success", false);
    //             result.put("message", "No copies available for borrowing");
    //         } else {
    //             // Create new borrowed book record
    //             BorrowedBook borrowedBook = new BorrowedBook();
    //             borrowedBook.setBook(book);
    //             borrowedBook.setUser(user);
    //             borrowedBook.setBorrowDate(LocalDateTime.now());
    //             borrowedBook.setReturnDate(LocalDate.now().plusDays(14)); // 2 weeks loan period
    //             borrowedBook.setReturned(false);
                
    //             // Update available copies
    //             book.setAvailableCopies(book.getAvailableCopies() - 1);
                
    //             // Save changes
    //             session.save(borrowedBook);
    //             session.update(book);
                
    //             // Commit transaction
    //             transaction.commit();
                
    //             result.put("success", true);
    //             result.put("message", "Book borrowed successfully");
    //         }
            
    //         // Convert the result to JSON
    //         ObjectMapper mapper = new ObjectMapper();
    //         String jsonResult = mapper.writeValueAsString(result);
            
    //         // Set response content type and write JSON to response
    //         response.setContentType("application/json");
    //         response.setCharacterEncoding("UTF-8");
    //         response.getWriter().write(jsonResult);
    //     } catch (Exception e) {
    //         if (transaction != null) {
    //             transaction.rollback();
    //         }
    //         throw e;
    //     }
    // }
    public static void borrowBook(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Transaction transaction = null;
    Map<String, Object> result = new HashMap<>();

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        // Read JSON body from request
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> body = mapper.readValue(sb.toString(), new TypeReference<Map<String, Integer>>() {});
        int bookId = body.get("bookId");
        int userId = body.get("userId");

        // Start transaction
        transaction = session.beginTransaction();

        // Get book and user
        Book book = session.get(Book.class, bookId);
        User user = session.get(User.class, userId);

        if (book == null || user == null) {
            result.put("success", false);
            result.put("message", "Book or User not found.");
        } else if (book.getAvailableCopies() <= 0) {
            result.put("success", false);
            result.put("message", "No copies available for borrowing.");
        } else {
            // Create new borrowed book record
            BorrowedBook borrowedBook = new BorrowedBook();
            borrowedBook.setBook(book);
            borrowedBook.setUser(user);
            borrowedBook.setBorrowDate(LocalDateTime.now());
            borrowedBook.setReturnDate(LocalDate.now().plusDays(14));
            borrowedBook.setReturned(false);

            // Update available copies
            book.setAvailableCopies(book.getAvailableCopies() - 1);

            // Save changes
            session.save(borrowedBook);
            session.update(book);

            // Commit transaction
            transaction.commit();

            result.put("success", true);
            result.put("message", "Book borrowed successfully.");
        }

        // Send JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResult = mapper.writeValueAsString(result);
        response.getWriter().write(jsonResult);

    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
        throw e;
    }
}

    
    // Return a book
    public static void returnBook(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int borrowId = Integer.parseInt(request.getParameter("borrowId"));
        
        Transaction transaction = null;
        Map<String, Object> result = new HashMap<>();
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start transaction
            transaction = session.beginTransaction();
            
            // Get borrowed book record
            BorrowedBook borrowedBook = session.get(BorrowedBook.class, borrowId);
            
            if (borrowedBook == null) {
                result.put("success", false);
                result.put("message", "Borrowed book record not found");
            } else if (borrowedBook.isReturned()) {
                result.put("success", false);
                result.put("message", "Book already returned");
            } else {
                // Update borrowed book record
                borrowedBook.setReturned(true);
                borrowedBook.setActualReturnDate(LocalDate.now());
                
                // Calculate days overdue if any
                if (LocalDate.now().isAfter(borrowedBook.getReturnDate())) {
                    long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(
                            borrowedBook.getReturnDate(), LocalDate.now());
                    borrowedBook.setDaysOverdue((int) daysOverdue);
                }
                
                // Update available copies
                Book book = borrowedBook.getBook();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                
                // Save changes
                session.update(borrowedBook);
                session.update(book);
                
                // Commit transaction
                transaction.commit();
                
                result.put("success", true);
                result.put("message", "Book returned successfully");
            }
            
            // Convert the result to JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonResult = mapper.writeValueAsString(result);
            
            // Set response content type and write JSON to response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResult);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
