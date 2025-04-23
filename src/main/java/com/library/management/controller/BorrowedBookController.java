package com.library.management.controller;

import com.library.management.model.BorrowedBook;
import com.library.management.service.BorrowedBookService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

@Path("/borrowedbooks")
public class BorrowedBookController {
   private static final EntityManager em = Persistence.createEntityManagerFactory("libraryPU").createEntityManager();
   private BorrowedBookService borrowedBookService = new BorrowedBookService(em);
   @POST
   @Path("/borrow")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response borrowBook(@QueryParam("bookId") int bookId, @QueryParam("userId") int userId) {
       String result = borrowedBookService.borrowBook(bookId, userId);
       return Response.ok(result).build();
   }
   @GET
   @Path("/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getBorrowedBookById(@PathParam("id") int id) {
       BorrowedBook book = borrowedBookService.getBorrowedBookById(id);
       if (book != null) {
           return Response.ok(book).build();
       } else {
           return Response.status(Response.Status.NOT_FOUND).entity("Borrowed book not found").build();
       }
   }
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAllBorrowedBooks() {
       List<BorrowedBook> books = borrowedBookService.getAllBorrowedBooks();
       return Response.ok(books).build();
   }
   @PUT
   @Path("/return")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response updateReturnStatus(@QueryParam("borrowId") int borrowId,
                                      @QueryParam("returned") boolean returned,
                                      @QueryParam("daysOverdue") int daysOverdue) {
       borrowedBookService.updateReturnStatus(borrowId, returned, daysOverdue);
       return Response.ok("Return status updated").build();
   }
}