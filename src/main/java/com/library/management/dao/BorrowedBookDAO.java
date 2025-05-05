package com.library.management.dao;

import com.library.management.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

public class BorrowedBookDAO {
   private EntityManager em2;
   
   public BorrowedBookDAO(EntityManager em2) {
	super();
	this.em2 = em2;
   }
   //Borrow book function - insert/update transaction
   public String BorrowBook(int bookId, int userId){
	   EntityTransaction tx = em2.getTransaction();
	   try{
		   tx.begin();
		   
		   Book newBook = em2.find(Book.class, bookId);
		   System.out.println(newBook);
		   User newUser = em2.find(User.class, userId);
		   System.out.println(newUser);
		   
		   if (newBook == null || newUser == null){
			   tx.rollback();
			   return "Book or User not found. "; 
		   }
		   if (newBook.getAvailableCopies() <= 0){
			   tx.rollback();
			   return "No copies available to borrow!";
		   }
		   
		   newBook.setAvailableCopies(newBook.getAvailableCopies() - 1);
		   em2.merge(newBook);
		   
		   BorrowedBook borrowedBook = new BorrowedBook();
	       borrowedBook.setBook(newBook);
	       borrowedBook.setUser(newUser);
	       borrowedBook.setBorrowDate(LocalDateTime.now());
	       borrowedBook.setReturnDate(LocalDate.now().plusDays(21));
	       borrowedBook.setReturned(false);
	       borrowedBook.setDaysOverdue(0);
	       
	       em2.persist(borrowedBook);
	       tx.commit();
	       return "Book borrowed succesfully!";
		   
	   }catch(Exception e){
		   if (tx.isActive()) tx.rollback();
		   e.printStackTrace();
		   return "Error while borrowing book";
	   }
   }
   // Fetch by ID
   public BorrowedBook fetchBorrowedBookById(int borrowId) {
       return em2.find(BorrowedBook.class, borrowId);
   }
   // Fetch all borrowed books
   public List<BorrowedBook> fetchAllBorrowedBooks() {
       TypedQuery<BorrowedBook> query = em2.createQuery("SELECT b FROM BorrowedBook b", BorrowedBook.class);
       return query.getResultList();
   }
   // Fetch borrowed books by user ID
   public List<BorrowedBook> fetchBorrowedBooksByUserId(int userId) {
       TypedQuery<BorrowedBook> query = em2.createQuery(
           "SELECT b FROM BorrowedBook b WHERE b.user.userId = :userId AND b.returned = false", 
           BorrowedBook.class
       );
       query.setParameter("userId", userId);
       return query.getResultList();
   }
   // Update returned status and actual return date
   public void updateReturnStatus(int borrowId, boolean returned, int daysOverdue) {
       EntityTransaction transaction = em2.getTransaction();
       try {
           transaction.begin();
           BorrowedBook borrowedBook = em2.find(BorrowedBook.class, borrowId);
           if (borrowedBook != null) {
               borrowedBook.setReturned(!returned);
               borrowedBook.setActualReturnDate(java.time.LocalDate.now());
               borrowedBook.setDaysOverdue(daysOverdue);
               
               Book newBook = borrowedBook.getBook();
               if(newBook != null){
            	   int availableCopies = newBook.getAvailableCopies();
            	   if(returned) {
            		   newBook.setAvailableCopies(availableCopies + 1);
            	   }
               }
              
               em2.merge(borrowedBook);
           }
           transaction.commit();
       } catch (Exception e) {
           if (transaction.isActive()) transaction.rollback();
           e.printStackTrace();
       }
   }
   // Close EntityManager and Factory
   public void close() {
       em2.close();
   }
}
