-- 1. Create database (optional)
/*
-- StwÛrz dedykowanego uøytkownika dla aplikacji
CREATE LOGIN stockapp WITH PASSWORD = 'StockApp123!';
CREATE USER stockapp FOR LOGIN stockapp;

-- Daj mu uprawnienia
ALTER SERVER ROLE dbcreator ADD MEMBER stockapp;
ALTER SERVER ROLE securityadmin ADD MEMBER stockapp;

*/
SELECT * FROM sys.event_log WHERE event_type = 'login_failed' ORDER BY log_date DESC;

CREATE DATABASE StockExchangeDB;
GO

USE StockExchangeDB;
GO

-- 2. Create tables
-- CATEGORIES table
CREATE TABLE CATEGORIES (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL,
    description NVARCHAR(255) NULL
);

-- USERS table
CREATE TABLE USERS (
    id INT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(50) NOT NULL,
    last_name NVARCHAR(50) NOT NULL,
    email NVARCHAR(100) UNIQUE NOT NULL,
    registration_date DATETIME DEFAULT GETDATE()
);

-- COMPANIES table
CREATE TABLE COMPANIES (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    symbol VARCHAR(10) UNIQUE NOT NULL,
    category_id INT NOT NULL,
);

-- ORDERS table
CREATE TABLE ORDERS (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    company_id INT NOT NULL,
    order_type VARCHAR(10) NOT NULL CHECK (order_type IN ('BUY', 'SELL')),
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(15,2) NOT NULL CHECK (price > 0),
    order_date DATETIME DEFAULT GETDATE(),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'EXECUTED', 'CANCELLED'))
);

-- PORTFOLIOS table
CREATE TABLE PORTFOLIOS (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    company_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    last_updated DATETIME DEFAULT GETDATE()
);

-- 3. Create foreign key relationships
-- COMPANIES -> CATEGORIES
ALTER TABLE COMPANIES 
ADD CONSTRAINT FK_Companies_Categories 
FOREIGN KEY (category_id) REFERENCES CATEGORIES(id);

-- ORDERS -> USERS
ALTER TABLE ORDERS 
ADD CONSTRAINT FK_Orders_Users 
FOREIGN KEY (user_id) REFERENCES USERS(id);

-- ORDERS -> COMPANIES
ALTER TABLE ORDERS 
ADD CONSTRAINT FK_Orders_Companies 
FOREIGN KEY (company_id) REFERENCES COMPANIES(id);

-- PORTFOLIOS -> USERS
ALTER TABLE PORTFOLIOS 
ADD CONSTRAINT FK_Portfolios_Users 
FOREIGN KEY (user_id) REFERENCES USERS(id);

-- PORTFOLIOS -> COMPANIES
ALTER TABLE PORTFOLIOS 
ADD CONSTRAINT FK_Portfolios_Companies 
FOREIGN KEY (company_id) REFERENCES COMPANIES(id);

-- 4. Insert sample data
-- Categories
INSERT INTO CATEGORIES (name, description) VALUES
('Stocks', 'Publicly traded companies'),
('Cryptocurrencies', 'Digital currencies and tokens'),
('ETF', 'Exchange-traded funds');

-- Users
INSERT INTO USERS (first_name, last_name, email, registration_date) VALUES
('John', 'Smith', 'john.smith@email.com', '2024-01-15'),
('Anna', 'Johnson', 'anna.johnson@email.com', '2024-02-20'),
('Peter', 'Wilson', 'peter.wilson@email.com', '2024-03-10');

-- Companies
INSERT INTO COMPANIES (name, symbol, category_id) VALUES
('CD Projekt Red', 'CDR', 1),
('PKN Orlen', 'PKN', 1),
('Bitcoin', 'BTC', 2),
('Ethereum', 'ETH', 2),
('iShares S&P 500', 'ISP500', 3),
('Vanguard Total Stock', 'VTS', 3);

-- Orders
INSERT INTO ORDERS (user_id, company_id, order_type, quantity, price, order_date, status) VALUES
(1, 1, 'BUY', 10, 118.00, '2024-11-01 09:30:00', 'EXECUTED'),
(1, 3, 'BUY', 2, 80000.00, '2024-11-02 10:15:00', 'EXECUTED'),
(2, 2, 'BUY', 100, 64.50, '2024-11-03 11:20:00', 'EXECUTED'),
(3, 5, 'BUY', 5, 448.00, '2024-11-04 14:45:00', 'PENDING'),
(1, 1, 'SELL', 5, 122.00, '2024-11-05 15:30:00', 'PENDING');

-- Portfolios (positions after executed orders)
INSERT INTO PORTFOLIOS (user_id, company_id, quantity, last_updated) VALUES
(1, 1, 5, GETDATE()),  -- John has 5 CDR shares (bought 10, selling 5 - order still pending)
(1, 3, 2, GETDATE()),  -- John has 2 BTC
(2, 2, 100, GETDATE()), -- Anna has 100 PKN shares
(3, 5, 5, GETDATE());  -- Peter has 5 ETF units

-- 5. Display sample data (optional - for verification)
SELECT * FROM CATEGORIES;

SELECT * FROM USERS;

SELECT * FROM COMPANIES;

SELECT * FROM ORDERS;

SELECT * FROM PORTFOLIOS;


/*
-- Sprawdü nazwÍ serwera
SELECT @@SERVERNAME as 'Server Name';

-- Sprawdü nazwÍ instance
SELECT @@SERVICENAME as 'Service Name'; 

-- Sprawdü port
SELECT DISTINCT local_net_address, local_tcp_port 
FROM sys.dm_exec_connections 
WHERE local_net_address IS NOT NULL;*/


