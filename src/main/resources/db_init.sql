-- Drop tables if they exist
DROP TABLE IF EXISTS borrowed_books;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

-- Create tables
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1
);

CREATE TABLE borrowed_books (
    borrow_id SERIAL PRIMARY KEY,
    book_id INT NOT NULL REFERENCES books(book_id),
    user_id INT NOT NULL REFERENCES users(user_id),
    borrow_date TIMESTAMP NOT NULL,
    return_date DATE NOT NULL,
    actual_return_date DATE,
    days_overdue INT NOT NULL DEFAULT 0,
    returned BOOLEAN NOT NULL DEFAULT FALSE
);

-- Insert sample data for testing
INSERT INTO users (username, password, email, role) VALUES 
('user1', 'password123', 'user1@example.com', 'USER'),
('librarian1', 'password123', 'librarian1@example.com', 'STAFF');

INSERT INTO books (title, author, genre, total_copies, available_copies) VALUES 
('The Great Gatsby', 'F. Scott Fitzgerald', 'Fiction', 5, 5),
('To Kill a Mockingbird', 'Harper Lee', 'Fiction', 3, 3),
('1984', 'George Orwell', 'Science Fiction', 2, 2),
('Pride and Prejudice', 'Jane Austen', 'Romance', 4, 4),
('The Catcher in the Rye', 'J.D. Salinger', 'Fiction', 3, 3),
('Moby Dick', 'Herman Melville', 'Adventure', 2, 2);

-- Uncomment to insert borrowed books for testing
-- INSERT INTO borrowed_books (book_id, user_id, borrow_date, return_date, returned) VALUES
-- (1, 1, CURRENT_TIMESTAMP, CURRENT_DATE + INTERVAL '14 days', FALSE),
-- (3, 1, CURRENT_TIMESTAMP, CURRENT_DATE + INTERVAL '14 days', FALSE);

-- UPDATE books SET available_copies = available_copies - 1 WHERE book_id IN (1, 3); 
