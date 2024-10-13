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
    address NVARCHAR(100),
    gender BIT,
    phoneNumber CHAR(10),
    email NVARCHAR(25),
    birthday DATE,
    registrationDate DATE,
    accumulatedPoints INT,
    membership NVARCHAR(10)
);
GO

-- Tạo bảng Employee
CREATE TABLE Employee (
    id CHAR(9) PRIMARY KEY,
    name NVARCHAR(30),
    address NVARCHAR(100),
    gender BIT,
    birthday DATE,
    citizenIDNumber CHAR(13),
    status NVARCHAR(15),
    phoneNumber CHAR(10),
    email NVARCHAR(20),
    hireDate DATE,
    position NVARCHAR(20),
    hourlyPay REAL,
    salary REAL,

    managerId CHAR(9),
    FOREIGN KEY (managerId) REFERENCES Employee(id),
);
GO

-- Tạo bảng Promotion
CREATE TABLE Promotion (
    id CHAR(11) PRIMARY KEY,
    name NVARCHAR(30),
    startDate DATE,
    endDate DATE,
    isUsed BIT,
    discount REAL,
    description NVARCHAR(100),
    minimumOrderAmount REAL,
);
GO

-- Tạo bảng Payment
CREATE TABLE Payment (
    id CHAR(14) PRIMARY KEY,
    amount REAL,
    paymentDate DATE,
    paymentMethod NVARCHAR(50),
    paymentTime TIME,
);
GO

-- Tạo bảng Account
CREATE TABLE Account (
    username NVARCHAR(50) PRIMARY KEY,
    hashcode NVARCHAR(100),
    role NVARCHAR(15),
    email NVARCHAR(25),
    isActive BIT,
    avatar VARBINARY(MAX),

    employeeId CHAR(9),
    FOREIGN KEY (employeeId) REFERENCES Employee(id),
);
GO

-- Tạo bảng TableType
CREATE TABLE TableType (
    id CHAR(4) PRIMARY KEY,
    name NVARCHAR(10),
    description NVARCHAR(100)
);
GO

-- Tạo bảng Table
CREATE TABLE [Table] (
    id CHAR(6) PRIMARY KEY,
    seats INT,
    location NVARCHAR(100),
    isAvailable BIT,

    tableTypeId CHAR(4),
    FOREIGN KEY (tableTypeId) REFERENCES TableType(id),
);
GO

-- Tạo bảng Reservation
CREATE TABLE Reservation (
    id CHAR(9) PRIMARY KEY,
    partyType NVARCHAR(20),
    partySize INT,
    reservationDate DATE,
    reservationTime TIME,
    receiveDate DATE,
    status NVARCHAR(50),
    deposit REAL,
    refundDeposit REAL,

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

-- Tạo bảng Category
CREATE TABLE Category (
    id CHAR(5) PRIMARY KEY,
    name NVARCHAR(40),
    description NVARCHAR(100)
);
GO

-- Tạo bảng Cuisine
CREATE TABLE Cuisine (
    id CHAR(4) PRIMARY KEY,
    name NVARCHAR(20),
    price REAL,
    description NVARCHAR(30),
    image VARBINARY(MAX),

    categoryId CHAR(5),
    FOREIGN KEY (categoryId) REFERENCES Category(id),
);
GO

-- Tạo bảng Order
CREATE TABLE [Order] (
    id CHAR(13) PRIMARY KEY,
    orderDate DATE,
    notes NVARCHAR(100),
    vatTax REAL,
    paymentAmount REAL,
    dispensedAmount REAL,
    totalAmount REAL,
    discount REAL,

    customerId CHAR(9),
    employeeId CHAR(9),
    promotionId CHAR(11),
    paymentId CHAR(14),
    tableId CHAR(6),
    FOREIGN KEY (customerId) REFERENCES Customer(id),
    FOREIGN KEY (employeeId) REFERENCES Employee(id),
    FOREIGN KEY (promotionId) REFERENCES Promotion(id),
    FOREIGN KEY (tableId) REFERENCES [Table](id),
);
GO

-- Tạo bảng OrderDetail
CREATE TABLE OrderDetail (
    id CHAR(19) PRIMARY KEY,
    quantity INT,
    note NVARCHAR(100),
    salePrice REAL,

    cuisineId CHAR(4),
    orderId CHAR(13),
    FOREIGN KEY (cuisineId) REFERENCES Cuisine(id),
    FOREIGN KEY (orderId) REFERENCES [Order](id),
);
GO
