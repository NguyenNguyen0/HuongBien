-- Tạo database HuongBien
USE master;
CREATE DATABASE HuongBien;
GO
-- Server=localhost;Database=HuongBien;User Id=sa;Password=sapassword;
-- Sử dụng database HuongBien
USE HuongBien;
GO

-- Tạo bảng Customer
CREATE TABLE Customer (
    id CHAR(9) PRIMARY KEY,
    name NVARCHAR(30),
    [address] NVARCHAR(100),
    gender BIT DEFAULT NULL,
    phoneNumber CHAR(10),
    email NVARCHAR(25),
    birthday DATE,
    registrationDate DATE,
    accumulatedPoints INT DEFAULT 0,
    membership NVARCHAR(10),
);
GO

-- Tạo bảng Employee
CREATE TABLE Employee (
    id CHAR(9) PRIMARY KEY,
    name NVARCHAR(30) NOT NULL,
    address NVARCHAR(100) NOT NULL,
    gender BIT NOT NULL,
    birthday DATE NOT NULL,
    citizenIDNumber CHAR(13) NOT NULL,
    status NVARCHAR(15) NOT NULL,
    phoneNumber CHAR(10) NOT NULL,
    email NVARCHAR(20),
    hireDate DATE NOT NULL,
    position NVARCHAR(20) NOT NULL,
    hourlyPay REAL NOT NULL,
    salary REAL NOT NULL,

    managerId CHAR(9) NULL,
    FOREIGN KEY (managerId) REFERENCES Employee(id),
);
GO

-- Tạo bảng Promotion
CREATE TABLE Promotion (
    id CHAR(11) PRIMARY KEY,
    name NVARCHAR(30) NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    isUsed BIT DEFAULT 0,
    discount REAL CHECK (discount >= 0),
    description NVARCHAR(100),
    minimumOrderAmount REAL CHECK (minimumOrderAmount >= 0),
);
GO

-- Tạo bảng Payment
CREATE TABLE Payment (
    id CHAR(14) PRIMARY KEY,
    amount REAL NOT NULL,
    paymentDate DATE NOT NULL,
    paymentMethod NVARCHAR(50),
    paymentTime TIME NOT NULL,
);
GO

-- Tạo bảng Account
CREATE TABLE Account (
    username CHAR(9) PRIMARY KEY, -- username
    hashcode NVARCHAR(100) NOT NULL,
    role NVARCHAR(15) NOT NULL,
    email NVARCHAR(25) NOT NULL,
    isActive BIT DEFAULT 1,
    avatar VARBINARY(MAX),

    FOREIGN KEY (username) REFERENCES Employee(id),
);
GO

-- Tạo bảng TableType
CREATE TABLE TableType (
    id CHAR(4) PRIMARY KEY,
    name NVARCHAR(40) NOT NULL,
    description NVARCHAR(100) NOT NULL
);
GO

-- Tạo bảng Table
CREATE TABLE [Table] (
    id CHAR(6) PRIMARY KEY,
    name NVARCHAR(20) NOT NULL,
	seats INT NOT NULL,
    [floor] INT NOT NULL,
    isAvailable BIT NOT NULL DEFAULT 1,

    tableTypeId CHAR(4),
    FOREIGN KEY (tableTypeId) REFERENCES TableType(id),
);
GO

-- Tạo bảng Category
CREATE TABLE Category (
    id CHAR(5) PRIMARY KEY,
    name NVARCHAR(40) NOT NULL,
    description NVARCHAR(100) NOT NULL
);
GO

-- Tạo bảng Cuisine
CREATE TABLE Cuisine (
    id CHAR(4) PRIMARY KEY,
    name NVARCHAR(40) NOT NULL,
    price REAL NOT NULL,
    description NVARCHAR(50) NOT NULL,
    image VARBINARY(MAX) NOT NULL,

    categoryId CHAR(5),
    FOREIGN KEY (categoryId) REFERENCES Category(id),
);
GO

-- Tạo bảng Reservation
CREATE TABLE Reservation (
    id CHAR(9) PRIMARY KEY,
    partyType NVARCHAR(20) NOT NULL,
    partySize INT NOT NULL CHECK (partySize >= 1),
    reservationDate DATE NOT NULL,
    reservationTime TIME NOT NULL,
    receiveDate DATE NOT NULL,
    status NVARCHAR(50) NOT NULL,
    deposit REAL NOT NULL CHECK (deposit >= 0),
    refundDeposit REAL NOT NULL,

    employeeId CHAR(9),
    customerId CHAR(9),
    tableId CHAR(6),
    paymentId CHAR(14),
    FOREIGN KEY (employeeId) REFERENCES Employee(id),
    FOREIGN KEY (customerId) REFERENCES Customer(id),
    FOREIGN KEY (tableId) REFERENCES [Table](id),
    FOREIGN KEY (paymentId) REFERENCES Payment(id),
);
GO

-- Tạo bảng FoodOrder
CREATE TABLE FoodOrder (
    id CHAR(14) PRIMARY KEY,
    quantity INT CHECK (quantity >= 1),
    note NVARCHAR(100),
    salePrice REAL NOT NULL,

    cuisineId CHAR(4),
    reservationId CHAR(9),
    FOREIGN KEY (cuisineId) REFERENCES Cuisine(id),
    FOREIGN KEY (reservationId) REFERENCES Reservation(id),
);
GO

-- Tạo bảng Order
CREATE TABLE [Order] (
    id CHAR(13) PRIMARY KEY,
    orderDate DATE NOT NULL,
    notes NVARCHAR(100) NOT NULL,
    vatTax REAL DEFAULT 0.1,
    paymentAmount REAL NOT NULL,
    dispensedAmount REAL NOT NULL,
    totalAmount REAL NOT NULL,
    discount REAL DEFAULT 0,

    customerId CHAR(9),
    employeeId CHAR(9),
    promotionId CHAR(11),
    paymentId CHAR(14),
    tableId CHAR(6),
    FOREIGN KEY (customerId) REFERENCES Customer(id),
    FOREIGN KEY (employeeId) REFERENCES Employee(id),
    FOREIGN KEY (promotionId) REFERENCES Promotion(id),
    FOREIGN KEY	(paymentId ) REFERENCES Payment(id),
	FOREIGN KEY (tableId) REFERENCES [Table](id)
);
GO

-- Tạo bảng OrderDetail
CREATE TABLE OrderDetail (
    id CHAR(19) PRIMARY KEY,
    quantity INT CHECK (quantity >= 1),
    note NVARCHAR(100),
    salePrice REAL NOT NULL,

    cuisineId CHAR(4),
    orderId CHAR(13),
    FOREIGN KEY (cuisineId) REFERENCES Cuisine(id),
    FOREIGN KEY (orderId) REFERENCES [Order](id),
);
GO