package com.library.management.service;

import com.library.management.dao.BorrowedBookDAO;
import com.library.management.model.BorrowedBook;
import javax.persistence.EntityManager;
import java.util.List;

public class BorrowedBookService {
   private BorrowedBookDAO borrowedBookDAO;
   public BorrowedBookService(EntityManager em) {
       this.borrowedBookDAO = new BorrowedBookDAO(em);
   }
   public String borrowBook(int bookId, int userId) {
       return borrowedBookDAO.BorrowBook(bookId, userId);
   }
   public BorrowedBook getBorrowedBookById(int id) {
       return borrowedBookDAO.fetchBorrowedBookById(id);
   }
   public List<BorrowedBook> getAllBorrowedBooks() {
       return borrowedBookDAO.fetchAllBorrowedBooks();
   }
   public void updateReturnStatus(int borrowId, boolean returned, int daysOverdue) {
       borrowedBookDAO.updateReturnStatus(borrowId, returned, daysOverdue);
   }
   public void close() {
       borrowedBookDAO.close();
   }
}