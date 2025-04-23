package com.library.management.model;

import javax.persistence.*;
import java.time.*;

@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "borrow_id")
	private int borrowId;
	
	@ManyToOne
	@JoinColumn(name="book_id", nullable = false)
	private Book book;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	@Column(name="days_overdue", nullable = false)
	private int daysOverdue = 0;

	@Column(name="returned", nullable = false)
	private boolean returned = false;
	
	@Column(name="borrow_date", nullable = false)
	private LocalDateTime borrowDate;
	
	@Column(name="return_date", nullable = false)
	private LocalDate returnDate;
	
	@Column(name="actual_return_date")
	private LocalDate actualReturnDate;
	
	public String toString() {
		return "BorrowedBook{" +
		           "id=" + borrowId +
		           ", book_id='" + book.getBookId() + '\'' +
		           ", user_id='" + user.getUserId() + '\'' +  
		           ", days_overdue='" + daysOverdue + '\'' +
		           ", borrow_date='" + borrowDate + '\'' +
		           ", return_date='" + returnDate + '\'' +
		           ", actual_return_date='" + actualReturnDate + '\'' +
		           ", returned='" + returned + '\'' +
		           '}';
	}
	
	public BorrowedBook() {
		super();
	}

	public BorrowedBook(int borrowId, Book book, User user, int daysOverdue, boolean returned, LocalDateTime borrowDate,
			LocalDate returnDate, LocalDate actualReturnDate) {
		super();
		this.borrowId = borrowId;
		this.book = book;
		this.user = user;
		this.daysOverdue = daysOverdue;
		this.returned = returned;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.actualReturnDate = actualReturnDate;
	}
	
	public int getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(int borrowId) {
		this.borrowId = borrowId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getDaysOverdue() {
		return daysOverdue;
	}

	public void setDaysOverdue(int daysOverdue) {
		this.daysOverdue = daysOverdue;
	}

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(boolean returned) {
		this.returned = returned;
	}

	public LocalDateTime getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(LocalDateTime borrowDate) {
		this.borrowDate = borrowDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public LocalDate getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(LocalDate actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}
	
}