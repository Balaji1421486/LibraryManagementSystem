CREATE DATABASE IF NOT EXISTS library_management;

USE library_management;

CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(100),
    quantity INT NOT NULL DEFAULT 1,
    available_quantity INT NOT NULL DEFAULT 1
);

CREATE TABLE members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    member_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15)
);

CREATE TABLE book_issues (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    issue_date DATE NOT NULL,
    return_date DATE,
    fine DECIMAL(10,2) DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'Issued',

    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

INSERT INTO books
(title, author, category, quantity, available_quantity)
VALUES
('Java Programming', 'James Gosling', 'Programming', 5, 5),
('Python Basics', 'Guido van Rossum', 'Programming', 4, 4),
('Database Management', 'Abraham Silberschatz', 'Database', 3, 3),
('The Alchemist', 'Paulo Coelho', 'Fiction', 2, 2);

INSERT INTO members
(member_name, email, phone)
VALUES
('Balaji', 'balaji@gmail.com', '9876543210'),
('Rahul', 'rahul@gmail.com', '9876543211'),
('Suresh', 'suresh@gmail.com', '9876543212');