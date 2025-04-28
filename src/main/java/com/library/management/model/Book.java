package com.library.management.model;

import javax.persistence.*;



@Entity
@Table(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private int bookId;
	
	@Column(name = "author", nullable = false )
	private String author;
	
	@Column(name = "genre", nullable = false )
	private String genre;
	
	@Column(name = "title", nullable = false )
	private String title;
	
	@Column(name = "available_copies", nullable = false )
	private int availableCopies;
	
	@Column(name = "total_copies", nullable = false )
	private int totalCopies;
	
	@Override
	public String toString() {
		return "Book{" +
		           "id=" + bookId +
		           ", author='" + author + '\'' +
		           ", genre='" + genre + '\'' +  
		           ", title='" + title + '\'' +
		           ", totalCopies='" + totalCopies + '\'' +
		           ", availableCopies='" + availableCopies + '\'' + 
		           '}';
	}
	
	//constructors
	public Book() {
		super();
	}

	public Book(int bookId, String author, String genre, String title, int availableCopies, int totalCopies) {
		super();
		this.bookId = bookId;
		this.author = author;
		this.genre = genre;
		this.title = title;
		this.availableCopies = availableCopies;
		this.totalCopies = totalCopies;
	}

	

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAvailableCopies() {
		return availableCopies;
	}

	public void setAvailableCopies(int availableCopies) {
		this.availableCopies = availableCopies;
	}

	public int getTotalCopies() {
		return totalCopies;
	}

	public void setTotalCopies(int totalCopies) {
		this.totalCopies = totalCopies;
	}
	
	
}
