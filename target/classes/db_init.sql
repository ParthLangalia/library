-- Drop tables if they exist
DROP TABLE IF EXISTS borrowed_books;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

-- Create tables
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'STAFF', 'ADMIN'))
);

CREATE TABLE IF NOT EXISTS books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    isbn VARCHAR(20) UNIQUE,
    total_copies INTEGER NOT NULL DEFAULT 1,
    available_copies INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS borrowed_books (
    borrow_id SERIAL PRIMARY KEY,
    book_id INTEGER REFERENCES books(book_id),
    user_id INTEGER REFERENCES users(user_id),
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'BORROWED' CHECK (status IN ('BORROWED', 'RETURNED', 'OVERDUE')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default staff user
INSERT INTO users (username, password, email, role) 
VALUES ('Librarian', 'password123', 'librarian1@example.com', 'STAFF')
ON CONFLICT (email) DO UPDATE 
SET username = EXCLUDED.username,
    password = EXCLUDED.password,
    role = EXCLUDED.role;

-- Insert sample books
INSERT INTO books (title, author, genre, isbn, total_copies, available_copies) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 'Fiction', '9780743273565', 5, 5),
('To Kill a Mockingbird', 'Harper Lee', 'Fiction', '9780446310789', 3, 3),
('1984', 'George Orwell', 'Science Fiction', '9780451524935', 4, 4),
('Pride and Prejudice', 'Jane Austen', 'Romance', '9780141439518', 2, 2),
('The Hobbit', 'J.R.R. Tolkien', 'Fantasy', '9780547928227', 3, 3)
ON CONFLICT (isbn) DO NOTHING;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_books_title ON books(title);
CREATE INDEX IF NOT EXISTS idx_books_author ON books(author);
CREATE INDEX IF NOT EXISTS idx_books_genre ON books(genre);
CREATE INDEX IF NOT EXISTS idx_borrowed_books_user_id ON borrowed_books(user_id);
CREATE INDEX IF NOT EXISTS idx_borrowed_books_book_id ON borrowed_books(book_id);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- Create function to update timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updating timestamps
CREATE TRIGGER update_books_updated_at
    BEFORE UPDATE ON books
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_borrowed_books_updated_at
    BEFORE UPDATE ON borrowed_books
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

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
