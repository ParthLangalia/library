package com.library.management.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.library.management.controller.BookController;

@WebServlet(name = "BookServlet", urlPatterns = { "/books/*" })
public class BookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/search")) {
                // Search books
                BookController.searchBooks(request, response);
            } else if (pathInfo.equals("/borrowed")) {
                // Get borrowed books
                BookController.getBorrowedBooks(request, response);
            } else if (pathInfo.matches("/\\d+")) {
                // Get book details by ID
                int bookId = Integer.parseInt(pathInfo.substring(1));
                request.setAttribute("bookId", bookId);
                // Here you would call a method like BookController.getBookById(request, response)
                // Since we don't have this method, we'll return a 404 for now
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/borrow")) {
                // Borrow a book
                BookController.borrowBook(request, response);
            } else if (pathInfo.equals("/return")) {
                // Return a book
                BookController.returnBook(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
} 