package com.library.management.test;
import com.library.management.dao.BorrowedBookDAO;
import com.library.management.model.BorrowedBook;
import com.library.management.model.Book;
import com.library.management.model.User;
import java.time.*;
import java.util.List;
import javax.persistence.*;


public class BorrowedBookTest {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LibraryPU");
		EntityManager em2 = emf.createEntityManager();
		
       BorrowedBookDAO bBDAO = new BorrowedBookDAO(em2);
       // Dummy Book and User (Assuming already present or persisted)
       
       //borrow a book - create/update
//       String borrowResult = bBDAO.BorrowBook(100, 911);
//       System.out.println(borrowResult);
       
       // Fetch by ID
       BorrowedBook fetched = bBDAO.fetchBorrowedBookById(120);
       if (fetched != null) {
           System.out.println("Fetched Borrowed Book: " + fetched);
       }
       // Fetch all
//       List<BorrowedBook> allBooks = bDAO.fetchAllBorrowedBooks();
//       System.out.println("All Borrowed Books:");
//       for (BorrowedBook bb : allBooks) {
//           System.out.println(bb);
//       }
       // Update return status
//       bBDAO.updateReturnStatus(6, false, 0);
//       System.out.println("Return Status Updated.");

//       bDAO.close();
   }
}