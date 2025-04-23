// Dashboard functionality
document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements
    const searchForm = document.getElementById('searchForm');
    const searchTitle = document.getElementById('searchTitle');
    const searchAuthor = document.getElementById('searchAuthor');
    const searchCategory = document.getElementById('searchCategory');
    const booksGrid = document.getElementById('booksGrid');
    const noResults = document.getElementById('noResults');
    const borrowedBooksGrid = document.getElementById('borrowedBooksGrid');
    const noBorrowedBooks = document.getElementById('noBorrowedBooks');
    const bookDetailModal = document.getElementById('bookDetailModal');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const bookDetailContent = document.getElementById('bookDetailContent');
    const loadingIndicator = document.getElementById('loadingIndicator');
    const messageContainer = document.getElementById('messageContainer');
    const logoutBtn = document.getElementById('logoutBtn');
    const userName = document.getElementById('userName');
    const userRole = document.getElementById('userRole');
    const userInitials = document.getElementById('userInitials');

    // API endpoints (adjust these as needed for your backend)
    const API = {
        SEARCH: '/books/search',
        BORROWED: '/books/borrowed',
        BORROW: '/books/borrow',
        RETURN: '/books/return',
        USER_INFO: '/users/profile',
        LOGOUT: '/logout'
    };

    // Current user information
    let currentUser = null;

    // Event Listeners
    document.addEventListener('DOMContentLoaded', initializeDashboard);
    searchForm.addEventListener('submit', handleSearchSubmit);
    closeModalBtn.addEventListener('click', closeModal);
    logoutBtn.addEventListener('click', handleLogout);

    // Initialize the dashboard
    async function initializeDashboard() {
        try {
            showLoading();
            await loadUserProfile();
            await loadBorrowedBooks();
            hideLoading();
        } catch (error) {
            showMessage('Error initializing dashboard. Please try again.', 'error');
            console.error('Dashboard initialization error:', error);
            hideLoading();
        }
    }

    // Load user profile information
    async function loadUserProfile() {
        try {
            const response = await fetch('/api/users/profile', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Failed to load user profile');
            }
            
            currentUser = await response.json();
            displayUserInfo(currentUser);
        } catch (error) {
            console.error('Error loading user profile:', error);
            window.location.href = 'login.html'; // Redirect to login if not authenticated
        }
    }

    // Display user information
    function displayUserInfo(user) {
        if (user) {
            userName.textContent = user.name || 'User';
            userRole.textContent = user.role || 'Member';
            
            // Set user initials for avatar
            if (user.name) {
                const nameParts = user.name.split(' ');
                if (nameParts.length > 1) {
                    userInitials.textContent = (nameParts[0][0] + nameParts[1][0]).toUpperCase();
                } else {
                    userInitials.textContent = nameParts[0][0].toUpperCase();
                }
            } else {
                userInitials.textContent = 'U';
            }
        }
    }

    // Handle search form submission
    async function handleSearchSubmit(event) {
        event.preventDefault();
        showLoading();
        
        const title = searchTitle.value.trim();
        const author = searchAuthor.value.trim();
        const category = searchCategory.value;

        try {
            const books = await searchBooks(title, author, category);
            displaySearchResults(books);
        } catch (error) {
            showMessage('Error searching books. Please try again.', 'error');
            console.error('Search error:', error);
        } finally {
            hideLoading();
        }
    }

    // Search for books using API
    async function searchBooks(title, author, category) {
        let queryParams = [];
        
        if (title) queryParams.push(`title=${encodeURIComponent(title)}`);
        if (author) queryParams.push(`author=${encodeURIComponent(author)}`);
        if (category) queryParams.push(`category=${encodeURIComponent(category)}`);
        
        const queryString = queryParams.length > 0 ? `?${queryParams.join('&')}` : '';
        
        const response = await fetch(`${API.SEARCH}${queryString}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to search books');
        }
        
        return await response.json();
    }

    // Display search results
    function displaySearchResults(books) {
        booksGrid.innerHTML = '';
        
        if (!books || books.length === 0) {
            noResults.style.display = 'flex';
            return;
        }
        
        noResults.style.display = 'none';
        
        books.forEach(book => {
            const bookCard = createBookCard(book, false);
            booksGrid.appendChild(bookCard);
        });
    }

    // Load user's borrowed books
    async function loadBorrowedBooks() {
        try {
            const response = await fetch(API.BORROWED, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Failed to load borrowed books');
            }
            
            const books = await response.json();
            displayBorrowedBooks(books);
        } catch (error) {
            console.error('Error loading borrowed books:', error);
            showMessage('Error loading borrowed books.', 'error');
        }
    }

    // Display borrowed books
    function displayBorrowedBooks(books) {
        borrowedBooksGrid.innerHTML = '';
        
        if (!books || books.length === 0) {
            noBorrowedBooks.style.display = 'flex';
            return;
        }
        
        noBorrowedBooks.style.display = 'none';
        
        books.forEach(book => {
            const bookCard = createBookCard(book, true);
            borrowedBooksGrid.appendChild(bookCard);
        });
    }

    // Create a book card element
    function createBookCard(book, isBorrowed) {
        const bookCard = document.createElement('div');
        bookCard.className = 'book-card';
        bookCard.dataset.id = book.id;
        
        // Add specific class if the book is borrowed
        if (isBorrowed) {
            bookCard.classList.add('borrowed');
        }
        
        // Create book card content
        bookCard.innerHTML = `
            <div class="book-cover">
                <div class="book-cover-img" style="background-color: ${getRandomColor()}">
                    <i class="fas fa-book"></i>
                </div>
                ${book.available && !isBorrowed ? '<span class="book-available">Available</span>' : ''}
                ${!book.available && !isBorrowed ? '<span class="book-unavailable">Borrowed</span>' : ''}
            </div>
            <div class="book-info">
                <h3 class="book-title">${escapeHtml(book.title)}</h3>
                <p class="book-author">by ${escapeHtml(book.author)}</p>
                <div class="book-details">
                    <span class="book-category">${formatCategory(book.category)}</span>
                    ${isBorrowed ? `<span class="book-due">Due: ${formatDate(book.dueDate)}</span>` : ''}
                </div>
                <div class="book-actions">
                    <button class="btn btn-secondary view-btn" data-id="${book.id}">
                        <i class="fas fa-info-circle"></i> Details
                    </button>
                    ${book.available && !isBorrowed ? 
                        `<button class="btn btn-primary borrow-btn" data-id="${book.id}">
                            <i class="fas fa-hand-holding"></i> Borrow
                         </button>` : ''}
                    ${isBorrowed ? 
                        `<button class="btn btn-primary return-btn" data-id="${book.id}">
                            <i class="fas fa-undo"></i> Return
                         </button>` : ''}
                </div>
            </div>
        `;
        
        // Add event listeners to buttons
        bookCard.querySelector('.view-btn').addEventListener('click', () => showBookDetails(book.id));
        
        const borrowBtn = bookCard.querySelector('.borrow-btn');
        if (borrowBtn) {
            borrowBtn.addEventListener('click', () => borrowBook(book.id));
        }
        
        const returnBtn = bookCard.querySelector('.return-btn');
        if (returnBtn) {
            returnBtn.addEventListener('click', () => returnBook(book.id));
        }
        
        return bookCard;
    }

    // Show book details in modal
    async function showBookDetails(bookId) {
        try {
            showLoading();
            const book = await fetchBookDetails(bookId);
            
            if (!book) {
                throw new Error('Book not found');
            }
            
            const isBorrowed = book.borrowerId === (currentUser?.id || 0);
            
            bookDetailContent.innerHTML = `
                <div class="book-detail-header">
                    <div class="book-detail-cover" style="background-color: ${getRandomColor()}">
                        <i class="fas fa-book"></i>
                    </div>
                    <div class="book-detail-info">
                        <h2>${escapeHtml(book.title)}</h2>
                        <p class="book-detail-author">by ${escapeHtml(book.author)}</p>
                        <div class="book-detail-meta">
                            <span class="book-detail-category">
                                <i class="fas fa-bookmark"></i> ${formatCategory(book.category)}
                            </span>
                            <span class="book-detail-isbn">
                                <i class="fas fa-barcode"></i> ISBN: ${book.isbn || 'N/A'}
                            </span>
                        </div>
                        ${book.available ? 
                            '<span class="book-available-tag">Available</span>' : 
                            '<span class="book-unavailable-tag">Currently Borrowed</span>'}
                    </div>
                </div>
                <div class="book-detail-body">
                    <div class="book-description">
                        <h3>Description</h3>
                        <p>${book.description || 'No description available for this book.'}</p>
                    </div>
                    <div class="book-metrics">
                        <div class="metric">
                            <div class="metric-value">${book.publicationYear || 'N/A'}</div>
                            <div class="metric-label">Published</div>
                        </div>
                        <div class="metric">
                            <div class="metric-value">${book.pageCount || 'N/A'}</div>
                            <div class="metric-label">Pages</div>
                        </div>
                        <div class="metric">
                            <div class="metric-value">${book.language || 'N/A'}</div>
                            <div class="metric-label">Language</div>
                        </div>
                    </div>
                    <div class="book-detail-actions">
                        ${book.available && !isBorrowed ? 
                            `<button class="btn btn-primary borrow-detail-btn" data-id="${book.id}">
                                <i class="fas fa-hand-holding"></i> Borrow this Book
                             </button>` : ''}
                        ${isBorrowed ? 
                            `<button class="btn btn-primary return-detail-btn" data-id="${book.id}">
                                <i class="fas fa-undo"></i> Return this Book
                             </button>
                             <div class="book-due-detail">Due: ${formatDate(book.dueDate)}</div>` : ''}
                    </div>
                </div>
            `;
            
            // Add event listeners to buttons
            const borrowDetailBtn = bookDetailContent.querySelector('.borrow-detail-btn');
            if (borrowDetailBtn) {
                borrowDetailBtn.addEventListener('click', () => {
                    borrowBook(book.id);
                    closeModal();
                });
            }
            
            const returnDetailBtn = bookDetailContent.querySelector('.return-detail-btn');
            if (returnDetailBtn) {
                returnDetailBtn.addEventListener('click', () => {
                    returnBook(book.id);
                    closeModal();
                });
            }
            
            openModal();
            hideLoading();
        } catch (error) {
            hideLoading();
            showMessage('Error loading book details.', 'error');
            console.error('Error in showBookDetails:', error);
        }
    }

    // Fetch book details from API
    async function fetchBookDetails(bookId) {
        const response = await fetch(`/books/${bookId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to fetch book details');
        }
        
        return await response.json();
    }

    // Borrow a book
    async function borrowBook(bookId) {
        try {
            showLoading();
            
            const response = await fetch(API.BORROW, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    bookId: bookId,
                    userId: localStorage.getItem('userId')
                })
            });

            if (!response.ok) {
                throw new Error('Failed to borrow book');
            }
            
            const result = await response.json();
            showMessage('Book borrowed successfully! Due date: ' + formatDate(result.dueDate), 'success');
            
            // Refresh book lists
            await loadBorrowedBooks();
            
            // If we're in search results, refresh those too
            if (booksGrid.children.length > 0) {
                await handleSearchSubmit(new Event('submit'));
            }
            
            hideLoading();
        } catch (error) {
            hideLoading();
            showMessage('Error borrowing book. Please try again.', 'error');
            console.error('Error in borrowBook:', error);
        }
    }

    // Return a book
    async function returnBook(bookId) {
        try {
            showLoading();
            
            const response = await fetch(API.RETURN, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    bookId: bookId,
                    userId: localStorage.getItem('userId')
                })
            });

            if (!response.ok) {
                throw new Error('Failed to return book');
            }
            
            showMessage('Book returned successfully!', 'success');
            
            // Refresh book lists
            await loadBorrowedBooks();
            
            // If we're in search results, refresh those too
            if (booksGrid.children.length > 0) {
                await handleSearchSubmit(new Event('submit'));
            }
            
            hideLoading();
        } catch (error) {
            hideLoading();
            showMessage('Error returning book. Please try again.', 'error');
            console.error('Error in returnBook:', error);
        }
    }

    // Handle logout
    function handleLogout() {
        fetch('/api/auth/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        }).finally(() => {
            // Redirect to login page regardless of success/failure
            window.location.href = 'login.html';
        });
    }

    // Utility Functions
    function openModal() {
        bookDetailModal.style.display = 'flex';
        document.body.classList.add('modal-open');
    }

    function closeModal() {
        bookDetailModal.style.display = 'none';
        document.body.classList.remove('modal-open');
    }

    function showLoading() {
        loadingIndicator.style.display = 'flex';
    }

    function hideLoading() {
        loadingIndicator.style.display = 'none';
    }

    function showMessage(text, type = 'info') {
        const message = document.createElement('div');
        message.className = `message message-${type}`;
        message.innerHTML = `
            <div class="message-icon">
                <i class="fas ${type === 'success' ? 'fa-check-circle' : type === 'error' ? 'fa-exclamation-circle' : 'fa-info-circle'}"></i>
            </div>
            <div class="message-text">${text}</div>
        `;
        
        messageContainer.appendChild(message);
        
        // Auto-remove after 5 seconds
        setTimeout(() => {
            message.classList.add('message-hiding');
            setTimeout(() => {
                if (messageContainer.contains(message)) {
                    messageContainer.removeChild(message);
                }
            }, 300); // match the CSS transition time
        }, 5000);
    }

    function formatCategory(category) {
        if (!category) return 'Uncategorized';
        
        // Convert from UPPER_CASE to Title Case
        return category
            .split('_')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
            .join(' ');
    }

    function formatDate(dateString) {
        if (!dateString) return 'N/A';
        
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', { 
            year: 'numeric', 
            month: 'short', 
            day: 'numeric' 
        });
    }

    function getRandomColor() {
        // Generate a pastel color
        const hue = Math.floor(Math.random() * 360);
        return `hsl(${hue}, 70%, 80%)`;
    }

    function escapeHtml(str) {
        if (!str) return '';
        
        return str
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    // Event Listeners
    searchForm.addEventListener('submit', handleSearchSubmit);
    
    closeModalBtn.addEventListener('click', () => {
        bookDetailModal.style.display = 'none';
    });
    
    window.addEventListener('click', (event) => {
        if (event.target === bookDetailModal) {
            bookDetailModal.style.display = 'none';
        }
    });
    
    logoutBtn.addEventListener('click', handleLogout);

    // Initialize the dashboard
    initializeDashboard();
}); 