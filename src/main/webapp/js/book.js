const BASE_URL = "http://localhost:8080/library"; // update if context path is different
// Add Book
document.getElementById("addBookForm").addEventListener("submit", function (e) {
   e.preventDefault();
   const book = {
       title: document.getElementById("title").value,
       author: document.getElementById("author").value,
       genre: document.getElementById("genre").value,
       availableCopies: parseInt(document.getElementById("availableCopies").value)
   };
   fetch(`${BASE_URL}/book/add`, {
       method: "POST",
       headers: { "Content-Type": "application/json" },
       body: JSON.stringify(book)
   })
       .then(response => response.text())
       .then(data => {
           alert(data);
           fetchBooks(); // Refresh book list
           document.getElementById("addBookForm").reset();
       })
       .catch(error => console.error("Error:", error));
});
// Fetch All Books
function fetchBooks() {
   fetch(`${BASE_URL}/book/fetchall`)
       .then(response => response.json())
       .then(books => {
           const list = document.getElementById("bookList");
           list.innerHTML = "";
           books.forEach(book => {
               const item = document.createElement("li");
               item.className = "list-group-item";
               item.innerText = `ID: ${book.id} | Title: ${book.title} | Author: ${book.author} | Genre: ${book.genre} | Available: ${book.availableCopies}`;
               list.appendChild(item);
           });
       })
       .catch(error => console.error("Error fetching books:", error));
}
// Initial load
fetchBooks();

// Book operations
const bookOperations = {
    // Search books
    searchBooks: function(title, author, genre) {
        const params = new URLSearchParams();
        if (title) params.append('title', title);
        if (author) params.append('author', author);
        if (genre) params.append('genre', genre);
        
        return fetch(`/library-management/api/books/search?${params.toString()}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Search failed');
                }
                return response.json();
            });
    },
    
    // Borrow a book
    borrowBook: function(bookId, userId) {
        const params = new URLSearchParams();
        params.append('bookId', bookId);
        params.append('userId', userId);
        
        return fetch('/library-management/api/books/borrow', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Borrow operation failed');
            }
            return response.json();
        });
    },
    
    // Get borrowed books for a user
    getBorrowedBooks: function(userId) {
        return fetch(`/library-management/api/books/borrowed?userId=${userId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch borrowed books');
                }
                return response.json();
            });
    },
    
    // Return a book
    returnBook: function(borrowId) {
        const params = new URLSearchParams();
        params.append('borrowId', borrowId);
        
        return fetch('/library-management/api/books/return', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Return operation failed');
            }
            return response.json();
        });
    }
};

// UI component to display popup messages
const popupMessage = {
    show: function(message, isSuccess = true) {
        const popup = document.createElement('div');
        popup.className = `popup ${isSuccess ? 'success' : 'error'}`;
        
        const content = document.createElement('div');
        content.className = 'popup-content';
        
        const text = document.createElement('p');
        text.textContent = message;
        
        const closeBtn = document.createElement('span');
        closeBtn.className = 'close-popup';
        closeBtn.innerHTML = '&times;';
        closeBtn.onclick = function() {
            document.body.removeChild(popup);
        };
        
        content.appendChild(text);
        content.appendChild(closeBtn);
        popup.appendChild(content);
        
        document.body.appendChild(popup);
        
        // Auto-hide after 3 seconds
        setTimeout(() => {
            if (document.body.contains(popup)) {
                document.body.removeChild(popup);
            }
        }, 3000);
    }
};

// Function to create a book card in the UI
function createBookCard(book) {
    const bookCard = document.createElement('div');
    bookCard.className = 'book-card';
    bookCard.dataset.bookId = book.bookId;
    
    const bookHeader = document.createElement('div');
    bookHeader.className = 'book-header';
    
    const title = document.createElement('h3');
    title.textContent = book.title;
    
    const expandBtn = document.createElement('button');
    expandBtn.className = 'expand-btn';
    expandBtn.innerHTML = '&#9660;'; // Down arrow
    
    bookHeader.appendChild(title);
    bookHeader.appendChild(expandBtn);
    
    const bookDetails = document.createElement('div');
    bookDetails.className = 'book-details';
    bookDetails.style.display = 'none';
    
    const author = document.createElement('p');
    author.innerHTML = `<strong>Author:</strong> ${book.author}`;
    
    const genre = document.createElement('p');
    genre.innerHTML = `<strong>Genre:</strong> ${book.genre}`;
    
    const copies = document.createElement('p');
    copies.innerHTML = `<strong>Available Copies:</strong> ${book.availableCopies}/${book.totalCopies}`;
    
    const borrowBtn = document.createElement('button');
    borrowBtn.className = 'borrow-btn';
    borrowBtn.textContent = 'Borrow';
    borrowBtn.disabled = book.availableCopies <= 0;
    
    bookDetails.appendChild(author);
    bookDetails.appendChild(genre);
    bookDetails.appendChild(copies);
    bookDetails.appendChild(borrowBtn);
    
    bookCard.appendChild(bookHeader);
    bookCard.appendChild(bookDetails);
    
    // Expand/collapse functionality
    expandBtn.addEventListener('click', () => {
        if (bookDetails.style.display === 'none') {
            bookDetails.style.display = 'block';
            expandBtn.innerHTML = '&#9650;'; // Up arrow
        } else {
            bookDetails.style.display = 'none';
            expandBtn.innerHTML = '&#9660;'; // Down arrow
        }
    });
    
    // Borrow functionality
    borrowBtn.addEventListener('click', () => {
        // Get the user ID from localStorage (set during login)
        const userId = localStorage.getItem('userId');
        if (!userId) {
            popupMessage.show('Please log in to borrow books', false);
            return;
        }
        
        bookOperations.borrowBook(book.bookId, userId)
            .then(result => {
                if (result.success) {
                    popupMessage.show(result.message);
                    
                    // Update UI
                    book.availableCopies--;
                    copies.innerHTML = `<strong>Available Copies:</strong> ${book.availableCopies}/${book.totalCopies}`;
                    borrowBtn.disabled = book.availableCopies <= 0;
                    
                    // Refresh borrowed books section
                    loadBorrowedBooks(userId);
                } else {
                    popupMessage.show(result.message, false);
                }
            })
            .catch(error => {
                popupMessage.show(`Error: ${error.message}`, false);
            });
    });
    
    return bookCard;
}

// Function to create a borrowed book card in the UI
function createBorrowedBookCard(borrowedBook) {
    const card = document.createElement('div');
    card.className = 'borrowed-book-card';
    card.dataset.borrowId = borrowedBook.borrowId;
    
    const title = document.createElement('h3');
    title.textContent = borrowedBook.book.title;
    
    const returnDate = document.createElement('p');
    returnDate.innerHTML = `<strong>Return by:</strong> ${new Date(borrowedBook.returnDate).toLocaleDateString()}`;
    
    const returnBtn = document.createElement('button');
    returnBtn.className = 'return-btn';
    returnBtn.textContent = 'Return';
    
    card.appendChild(title);
    card.appendChild(returnDate);
    card.appendChild(returnBtn);
    
    // Return functionality
    returnBtn.addEventListener('click', () => {
        bookOperations.returnBook(borrowedBook.borrowId)
            .then(result => {
                if (result.success) {
                    popupMessage.show(result.message);
                    
                    // Remove the card from UI
                    card.parentNode.removeChild(card);
                    
                    // Refresh book search results if displayed
                    const searchForm = document.getElementById('searchForm');
                    if (searchForm) {
                        const event = new Event('submit');
                        searchForm.dispatchEvent(event);
                    }
                } else {
                    popupMessage.show(result.message, false);
                }
            })
            .catch(error => {
                popupMessage.show(`Error: ${error.message}`, false);
            });
    });
    
    return card;
}

// Function to handle search form submission
function handleSearch(event) {
    event.preventDefault();
    
    const title = document.getElementById('title').value;
    const author = document.getElementById('author').value;
    const genre = document.getElementById('genre').value;
    
    const resultsContainer = document.getElementById('searchResults');
    resultsContainer.innerHTML = '<p>Searching...</p>';
    
    bookOperations.searchBooks(title, author, genre)
        .then(books => {
            resultsContainer.innerHTML = '';
            
            if (books.length === 0) {
                resultsContainer.innerHTML = '<p>No books found.</p>';
                return;
            }
            
            books.forEach(book => {
                const bookCard = createBookCard(book);
                resultsContainer.appendChild(bookCard);
            });
        })
        .catch(error => {
            resultsContainer.innerHTML = `<p>Error: ${error.message}</p>`;
        });
}

// Function to load borrowed books for a user
function loadBorrowedBooks(userId) {
    const borrowedBooksContainer = document.getElementById('borrowedBooks');
    if (!borrowedBooksContainer) return;
    
    borrowedBooksContainer.innerHTML = '<p>Loading borrowed books...</p>';
    
    bookOperations.getBorrowedBooks(userId)
        .then(borrowedBooks => {
            borrowedBooksContainer.innerHTML = '';
            
            if (borrowedBooks.length === 0) {
                borrowedBooksContainer.innerHTML = '<p class="text-center text-muted">No books currently borrowed</p>';
                return;
            }
            
            borrowedBooks.forEach(borrowedBook => {
                const borrowedBookCard = createBorrowedBookCard(borrowedBook);
                borrowedBooksContainer.appendChild(borrowedBookCard);
            });
        })
        .catch(error => {
            borrowedBooksContainer.innerHTML = `<p>Error loading borrowed books: ${error.message}</p>`;
        });
}

// Initialize the book functionality when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    // Set up search form
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', handleSearch);
    }
    
    // Load borrowed books if user is logged in
    const userId = localStorage.getItem('userId');
    if (userId) {
        loadBorrowedBooks(userId);
    }
});