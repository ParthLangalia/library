// API service for handling all API calls
const API = {
    // Base URL for API calls
    BASE_URL: '/api',
    
    // Headers with authentication token
    getHeaders: function() {
        return {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${AUTH.getToken()}`
        };
    },
    
    // Search books based on criteria
    searchBooks: async function(searchTerm, searchType) {
        try {
            const response = await fetch(`${this.BASE_URL}/books/search?term=${encodeURIComponent(searchTerm)}&type=${searchType}`, {
                method: 'GET',
                headers: this.getHeaders()
            });
            
            if (!response.ok) throw new Error('Failed to search books');
            return await response.json();
        } catch (error) {
            console.error('Error searching books:', error);
            throw error;
        }
    },
    
    // Get user's borrowed books
    getBorrowedBooks: async function() {
        try {
            const response = await fetch(`${this.BASE_URL}/books/borrowed`, {
                method: 'GET',
                headers: this.getHeaders()
            });
            
            if (!response.ok) throw new Error('Failed to get borrowed books');
            return await response.json();
        } catch (error) {
            console.error('Error getting borrowed books:', error);
            throw error;
        }
    },
    
    // Borrow a book
    borrowBook: async function(bookId) {
        try {
            const response = await fetch(`${this.BASE_URL}/books/${bookId}/borrow`, {
                method: 'POST',
                headers: this.getHeaders()
            });
            
            if (!response.ok) throw new Error('Failed to borrow book');
            return await response.json();
        } catch (error) {
            console.error('Error borrowing book:', error);
            throw error;
        }
    },
    
    // Return a book
    returnBook: async function(bookId) {
        try {
            const response = await fetch(`${this.BASE_URL}/books/${bookId}/return`, {
                method: 'POST',
                headers: this.getHeaders()
            });
            
            if (!response.ok) throw new Error('Failed to return book');
            return await response.json();
        } catch (error) {
            console.error('Error returning book:', error);
            throw error;
        }
    },
    
    // Get book details
    getBookDetails: async function(bookId) {
        try {
            const response = await fetch(`${this.BASE_URL}/books/${bookId}`, {
                method: 'GET',
                headers: this.getHeaders()
            });
            
            if (!response.ok) throw new Error('Failed to get book details');
            return await response.json();
        } catch (error) {
            console.error('Error getting book details:', error);
            throw error;
        }
    }
}; 