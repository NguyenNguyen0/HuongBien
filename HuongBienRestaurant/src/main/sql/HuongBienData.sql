USE HuongBien;

--GO
--BACKUP DATABASE HuongBien
--TO DISK = 'D:\HuongBien\HuongBien.bak'
--   WITH FORMAT,
--      MEDIANAME = 'SQLServerBackups',
--      NAME = 'Full Backup of HuongBien';
--GO

select * from Category;
select * from Cuisine;
select * from TableType;
select * from [Table];
select * from employee;
select * from account;


INSERT INTO Category (id, name, description)
VALUES
('CG001', N'Khai vị', N'Món ăn nhẹ nhàng, kích thích vị giác.'),
('CG002', N'Súp', N'Món súp nóng hổi, dễ ăn, giàu dinh dưỡng.'),
('CG003', N'Salad', N'Rau xanh tươi mát, ăn kèm với sốt.'),
('CG004', N'Món nướng', N'Các loại thịt nướng thơm ngon.'),
('CG005', N'Món hấp', N'Món ăn lành mạnh, giữ nguyên hương vị.'),
('CG006', N'Món chiên', N'Giòn rụm, đậm đà gia vị.'),
('CG007', N'Hải sản', N'Các món chế biến từ tôm, cua, cá.'),
('CG008', N'Cơm', N'Món cơm trắng kết hợp với nhiều nguyên liệu.'),
('CG009', N'Lẩu', N'Món lẩu đa dạng, phù hợp ăn chung nhóm.'),
('CG010', N'Nước uống', N'Nước uống giải khát.');

INSERT INTO Cuisine (id, name, price, description, image, categoryId)
VALUES
-- Khai vị
('M001', N'Gỏi tôm', 120000, N'Tôm tươi trộn cùng rau và sốt chua ngọt.', NULL, 'CG001'),
('M002', N'Chả mực', 150000, N'Mực giã nhuyễn, chiên giòn, thơm ngon.', NULL, 'CG001'),
('M003', N'Gỏi cá hồi', 170000, N'Cá hồi tươi trộn rau củ và nước sốt.', NULL, 'CG001'),
('M004', N'Nghêu hấp', 110000, N'Nghêu hấp với sả và ớt, thơm lừng.', NULL, 'CG001'),
('M005', N'Tôm chiên', 140000, N'Tôm chiên giòn rụm, ăn kèm nước chấm.', NULL, 'CG001'),

-- Súp
('M006', N'Súp cua', 90000, N'Súp cua bổ dưỡng với thịt cua tươi ngon.', NULL, 'CG002'),
('M007', N'Súp tôm', 95000, N'Súp tôm ngọt, đậm đà với rau củ.', NULL, 'CG002'),
('M008', N'Súp hải sản', 120000, N'Súp hải sản tổng hợp, thơm ngọt.', NULL, 'CG002'),
('M009', N'Súp cá hồi', 130000, N'Súp từ cá hồi, giàu dinh dưỡng.', NULL, 'CG002'),
('M010', N'Súp nghêu', 85000, N'Nghêu nấu súp cùng rau củ, đậm vị.', NULL, 'CG002'),

-- Salad
('M011', N'Salad tôm', 100000, N'Tôm tươi kết hợp rau xanh và sốt.', NULL, 'CG003'),
('M012', N'Salad cá ngừ', 120000, N'Cá ngừ cùng rau xanh tươi mát.', NULL, 'CG003'),
('M013', N'Salad mực', 110000, N'Mực tươi và rau sống, sốt chua ngọt.', NULL, 'CG003'),
('M014', N'Salad hải sản', 130000, N'Hải sản kết hợp cùng salad rau.', NULL, 'CG003'),
('M015', N'Sasimi bạch tuộc', 140000, N'Sasimi bạch tuộc tươi ngon.', NULL, 'CG003'),

-- Món nướng
('M016', N'Tôm nướng', 180000, N'Tôm nướng muối ớt thơm lừng.', NULL, 'CG004'),
('M017', N'Cá nướng giấy bạc', 200000, N'Cá nướng giấy bạc giữ nguyên hương vị.', NULL, 'CG004'),
('M018', N'Mực nướng sa tế', 170000, N'Mực tươi nướng sa tế cay nồng.', NULL, 'CG004'),
('M019', N'Hàu nướng phô mai', 190000, N'Hàu nướng phô mai béo ngậy.', NULL, 'CG004'),
('M020', N'Sò điệp nướng', 150000, N'Sò điệp nướng thơm ngon.', NULL, 'CG004'),

-- Món hấp
('M021', N'Cua hấp bia', 220000, N'Cua biển hấp bia, giữ nguyên vị ngọt.', NULL, 'CG005'),
('M022', N'Tôm hấp nước dừa', 180000, N'Tôm hấp với nước dừa tươi.', NULL, 'CG005'),
('M023', N'Nghêu hấp sả', 120000, N'Nghêu hấp sả, cay thơm.', NULL, 'CG005'),
('M024', N'Mực hấp gừng', 160000, N'Mực hấp với gừng tươi, thơm ngon.', NULL, 'CG005'),
('M025', N'Cá hồi hấp xì dầu', 200000, N'Cá hồi hấp xì dầu.', NULL, 'CG005'),

-- Món chiên
('M026', N'Tôm chiên xù', 150000, N'Tôm chiên giòn, ăn kèm nước sốt.', NULL, 'CG006'),
('M027', N'Mực chiên giòn', 140000, N'Mực chiên giòn, vàng ươm.', NULL, 'CG006'),
('M028', N'Cá chiên sốt me', 160000, N'Cá chiên giòn sốt me chua ngọt.', NULL, 'CG006'),
('M029', N'Tôm chiên tempura', 170000, N'Tôm chiên tNV0ura kiểu Nhật.', NULL, 'CG006'),
('M030', N'Cua chiên bơ tỏi', 210000, N'Cua chiên với bơ tỏi.', NULL, 'CG006'),

-- Hải sản
('M031', N'Gỏi cá trích', 130000, N'Cá trích tươi trộn rau và nước mắm.', NULL, 'CG007'),
('M032', N'Tôm hùm hấp', 350000, N'Tôm hùm hấp giữ nguyên hương vị.', NULL, 'CG007'),
('M033', N'Cua rang me', 250000, N'Cua rang me chua ngọt.', NULL, 'CG007'),
('M034', N'Hàu nướng phô mai', 180000, N'Hàu nướng phô mai béo ngậy.', NULL, 'CG007'),
('M035', N'Sò huyết sốt me', 140000, N'Sò huyết sốt me đậm vị.', NULL, 'CG007'),

-- Cơm
('M036', N'Cơm chiên hải sản', 120000, N'Cơm chiên với tôm, mực, sò.', NULL, 'CG008'),
('M037', N'Cơm chiên Dương Châu', 100000, N'Cơm chiên kiểu Dương Châu.', NULL, 'CG008'),
('M038', N'Cơm hải sản xốt XO', 140000, N'Cơm xào hải sản với xốt XO.', NULL, 'CG008'),
('M039', N'Cơm chiên cua', 130000, N'Cơm chiên cùng cua tươi ngon.', NULL, 'CG008'),
('M040', N'Cơm chiên hải sản trứng muối', 150000, N'Cơm chiên hải sản với trứng muối.', NULL, 'CG008'),

-- Lẩu
('M041', N'Lẩu hải sản', 300000, N'Lẩu tổng hợp các loại hải sản.', NULL, 'CG009'),
('M042', N'Lẩu cua đồng', 280000, N'Lẩu cua đồng tươi ngon.', NULL, 'CG009'),
('M043', N'Lẩu tôm chua cay', 270000, N'Tôm nấu lẩu chua cay.', NULL, 'CG009'),
('M044', N'Lẩu cá hồi', 320000, N'Lẩu cá hồi giàu dinh dưỡng.', NULL, 'CG009'),
('M045', N'Lẩu nghêu', 250000, N'Nghêu nấu lẩu thanh ngọt.', NULL, 'CG009'),

-- Nước uống
('M046', N'Coca cola', 15000, N'Nước uống có ga Coca cola.', NULL, 'CG010'),
('M047', N'Pepsi', 15000, N'Nước uống có ga Pepsi.', NULL, 'CG010'),
('M048', N'Bia Tiger', 37000, N'Đồ uống có cồn bia Tiger.', NULL, 'CG010'),
('M049', N'Bia Sài Gòn', 32000, N'Đồ uống có cồn bia Sài Gòn.', NULL, 'CG010'),
('M050', N'Bia Heineken', 35000, N'Đồ uống có cồn bia Heineken.', NULL, 'CG010');

--GO
--BEGIN TRANSACTION
--	DECLARE @i INT = 1;
--	DECLARE @filePath NVARCHAR(255);
--	DECLARE @id NVARCHAR(5);
--	DECLARE @sql NVARCHAR(MAX);

--	WHILE @i <= 50
--	BEGIN
--		-- Tạo mã món ăn (M001, M002, ..., M050)
--		SET @id = 'M' + RIGHT('000' + CAST(@i AS NVARCHAR(3)), 3);

--		-- Tạo đường dẫn ảnh dựa trên mã món ăn
--		SET @filePath = 'D:\HuongBien\Cuisine\' + @id + '.jpg';

--		-- Tạo câu lệnh động để chèn ảnh từ đường dẫn
--		SET @sql = N'
--		BEGIN TRY
--			BEGIN TRANSACTION
--				UPDATE Cuisine
--				SET image = (SELECT BulkColumn FROM OPENROWSET(BULK ''' + @filePath + ''', SINGLE_BLOB) AS ImageData)
--				WHERE id = ''' + @id + ''';
--			COMMIT;
--		END TRY
--		BEGIN CATCH
--			-- Nếu có lỗi, in ra đường dẫn bị lỗi
--			PRINT ''Error loading file: ' + @filePath + ''';
--			ROLLBACK;
--		END CATCH;';

--		-- Thực thi câu lệnh SQL động
--		EXEC sp_executesql @sql;

--		-- Tăng biến đếm
--		SET @i = @i + 1;
--	END;
--COMMIT;
--GO

INSERT INTO TableType (id, name, description)
VALUES
('LB001', N'Bàn thường', N'Dành cho thực khách phổ thông.'),
('LB002', N'Bàn VIP', N'Dành cho thực khách muốn không gian riêng tư.');

-- Insert tables for the ground floor (Tầng Trệt)
INSERT INTO [Table] (id, name, seats, [floor], tableTypeId)
VALUES 
('T0B001', N'Bàn 01', 4, 0, 'LB001'),
('T0B002', N'Bàn 02', 4, 0, 'LB001'),
('T0B003', N'Bàn 03', 4, 0, 'LB001'),
('T0B004', N'Bàn 04', 4, 0, 'LB001'),
('T0B005', N'Bàn 05', 4, 0, 'LB001'),
('T0B006', N'Bàn 06', 4, 0, 'LB001'),
('T0B007', N'Bàn 07', 4, 0, 'LB001'),
('T0B008', N'Bàn 08', 4, 0, 'LB001'),
('T0B009', N'Bàn 09', 4, 0, 'LB001'),
('T0B010', N'Bàn 10', 4, 0, 'LB001'),
('T0B011', N'Bàn 11', 6, 0, 'LB002'),
('T0B012', N'Bàn 12', 6, 0, 'LB002'),
('T0B013', N'Bàn 13', 6, 0, 'LB002'),
('T0B014', N'Bàn 14', 6, 0, 'LB002'),
('T0B015', N'Bàn 15', 6, 0, 'LB002');

-- Insert tables for floor "Tầng 1"
INSERT INTO [Table] (id, name, seats, [floor], tableTypeId)
VALUES 
('T1B001', N'Bàn 01', 4, 1, 'LB001'),
('T1B002', N'Bàn 02', 4, 1, 'LB001'),
('T1B003', N'Bàn 03', 4, 1, 'LB001'),
('T1B004', N'Bàn 04', 4, 1, 'LB001'),
('T1B005', N'Bàn 05', 4, 1, 'LB001'),
('T1B006', N'Bàn 06', 4, 1, 'LB001'),
('T1B007', N'Bàn 07', 4, 1, 'LB001'),
('T1B008', N'Bàn 08', 4, 1, 'LB001'),
('T1B009', N'Bàn 09', 4, 1, 'LB001'),
('T1B010', N'Bàn 10', 4, 1, 'LB001'),
('T1B011', N'Bàn 11', 6, 1, 'LB002'),
('T1B012', N'Bàn 12', 6, 1, 'LB002'),
('T1B013', N'Bàn 13', 6, 1, 'LB002');

-- Insert tables for floor "Tầng 2"
INSERT INTO [Table] (id, name, seats, [floor], tableTypeId)
VALUES 
('T2B001', N'Bàn 01', 4, 2, 'LB001'),
('T2B002', N'Bàn 02', 4, 2, 'LB001'),
('T2B003', N'Bàn 03', 4, 2, 'LB001'),
('T2B004', N'Bàn 04', 4, 2, 'LB001'),
('T2B005', N'Bàn 05', 4, 2, 'LB001'),
('T2B006', N'Bàn 06', 4, 2, 'LB001'),
('T2B007', N'Bàn 07', 4, 2, 'LB001'),
('T2B008', N'Bàn 08', 4, 2, 'LB001'),
('T2B009', N'Bàn 09', 4, 2, 'LB001'),
('T2B010', N'Bàn 10', 4, 2, 'LB001'),
('T2B011', N'Bàn 11', 6, 2, 'LB002');

-- Insert 5 rows into the Customer table
INSERT INTO Customer (id, name, [address], gender, phoneNumber, email, birthday, registrationDate, accumulatedPoints, membershiplevel)
VALUES
('KH241020101', N'Trần Văn An', N'123 Lê Lợi, Quận 1, TP. Hồ Chí Minh, Việt Nam', 1, '0901123456', N'vanan@example.com', '1990-05-20', '2024-10-24', 100, 1),
('KH241020102', N'Nguyễn Thị Bích', N'45 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh, Việt Nam', 0, '0902234567', N'bichnguyen@example.com', '1985-08-15', '2024-10-24', 200, 2),
('KH241020103', N'Lê Minh Tâm', N'789 Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh, Việt Nam', 1, '0903345678', N'minhtam@example.com', '1992-11-10', '2024-10-24', 50, 1),
('KH241020104', N'Phạm Quỳnh Hoa', N'25 Võ Văn Kiệt, Quận 5, TP. Hồ Chí Minh, Việt Nam', 0, '0904456789', N'quynhhoa@example.com', '1988-02-28', '2024-10-24', 150, 2),
('KH241020105', N'Đỗ Thành Nhân', N'678 Trường Sa, Quận Phú Nhuận, TP. Hồ Chí Minh, Việt Nam', 1, '0905567890', N'thanhnhan@example.com', '1995-07-07', '2024-10-24', 300, 3);

-- Insert the manager
INSERT INTO Employee (id, name, address, gender, birthday, citizenIDNumber, status, phoneNumber, email, hireDate, position, workHours, hourlyPay, salary, managerId)
VALUES 
('NV001122001', N'Nguyễn Trung Nguyên', N'12 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh, Viêt Nam', 0, '1980-01-01', '0123456789012', N'Đang làm', '0901234567', N'trungnguyen@example.com', '2020-01-01', N'Quản lý', 40.0, 20000.0, 8000.0, NULL);

-- Insert the Employee
INSERT INTO Employee (id, name, address, gender, birthday, citizenIDNumber, status, phoneNumber, email, hireDate, position, workHours, hourlyPay, salary, managerId)
VALUES 
('NV001122002', N'Đào Quốc Tuấn', N'120 Lý Tự Trọng, Quận 1, TP. Hồ Chí Minh, Viêt Nam', 0, '1990-02-01', '0123456789013', N'Đang làm', '0901234568', N'quoctuan@example.com', '2021-02-01', N'Tiếp tân', 40.0, 20000.0, 2400.0, 'NV001122001'),
('NV001122003', N'Lê Văn Đạt', N'45 Võ Văn Tần, Quận 3, TP. Hồ Chí Minh, Viêt Nam', 0, '1992-03-01', '0123456789014', N'Đang làm', '0901234569', N'ledat@example.com', '2021-03-01', N'Tiếp tân', 40.0, 20000.0, 2400.0, 'NV001122001'),
('NV001122004', N'Nguyễn Trần Gia Sĩ', N'50 Đinh Tiên Hoàng, Quận Bình Thạnh, TP. Hồ Chí Minh, Viêt Nam', 0, '1991-04-01', '0123456789015', N'Đang làm', '0901234570', N'nguyensi@example.com', '2021-04-01', N'Tiếp tân', 40.0, 20000.0, 2400.0, 'NV001122001'),
('NV001122005', N'Nguyễn Văn Minh', N'25 Phan Đăng Lưu, Quận Phú Nhuận, TP. Hồ Chí Minh, Viêt Nam', 1, '1985-05-01', '0123456789016', N'Đang làm', '0901234571', N'nguyenminh@example.com', '2021-05-01', N'Tiếp tân', 40.0, 20000.0, 2400.0, 'NV001122001'),
('NV000000006', N'Ngô Thị Bình', N'38 Nguyễn Thị Minh Khai, Quận 3, TP. Hồ Chí Minh, Viêt Nam', 0, '1993-06-01', '0123456789017', N'Đã nghỉ', '0901234572', NULL, '2021-06-01', N'Bồi bàn', 40.0, 20000.0, 2400.0, 'NV001122001'),
('NV000000007', N'Vũ Văn Cường', N'22 Lê Văn Sỹ, Quận Tân Bình, TP. Hồ Chí Minh, Viêt Nam', 1, '1994-07-01', '0123456789018', N'Đang làm', '0901234573', NULL, '2021-07-01', N'Bồi bàn', 40.0, 20000.0, 2400.0, 'NV001122001'),
('NV000000008', N'Bùi Thị Hải', N'150 Trần Hưng Đạo, Quận 5, TP. Hồ Chí Minh, Viêt Nam', 0, '1995-08-01', '0123456789019', N'Đang làm', '0901234574', NULL, '2021-08-01', N'Bồi bàn', 40.0, 20000.0, 2400.0, 'NV001122001'),
('NV000000009', N'Doãn Văn Đường', N'120 Âu Cơ, Quận Tân Phú, TP. Hồ Chí Minh, Viêt Nam', 1, '1996-09-01', '0123456789020', N'Đang làm', '0901234575', NULL, '2021-09-01', N'Bồi bàn', 40.0, 20000.0, 2400.0, 'NV001122001'),
('NV000000010', N'Tạ Thị Kim Ngân', N'200 Xô Viết Nghệ Tĩnh, Quận Bình Thạnh, TP. Hồ Chí Minh, Viêt Nam', 0, '1997-10-01', '0123456789021', N'Đang làm', '0901234576', NULL, '2021-10-01', N'Đầu bếp', 40.0, 20000.0, 2400.0, 'NV001122001');

-- Insert accounts for the first 5 Employee
INSERT INTO Account (username, hashcode, role, email, isActive, avatar)
VALUES
('NV001122001', '02d03b4538505309655c15780962858fa410fd85d8bbeffae8514fb345d01656', N'Quản lý', N'trungnguyen@example.com', 1, NULL),
('NV001122002', '52872b962111f3b6f9f8d882750471b32b0e4da893011d79425d6e0e9c5cf77e', N'Tiếp tân', N'quoctuan@example.com', 1, NULL),
('NV001122003', '6a11bb748f97e023ce442e4d9b21002690ee75485e0f1d33a5cf5797da843c7a', N'Tiếp tân', N'ledat@example.com', 1, NULL),
('NV001122004', '533400a5b1efdba4056c13b605451ac4c3f834589f6ea84ae14aea77b5996998', N'Tiếp tân', N'nguyensi@example.com', 1, NULL),
('NV001122005', 'd27cbb487826f9528d44011bcdef1566e48a842b99df60858424260de7c6f6d5', N'Tiếp tân', N'nguyenminh@example.com', 1, NULL);

-- Insert 10 rows into the Payment table
INSERT INTO Payment (id, amount, paymentDate, paymentMethod, paymentTime)
VALUES
('TT000000000000001', 500000.0, '2024-10-01', N'Tiền mặt', '10:30:00'),
('TT000000000000002', 1200000.0, '2024-10-02', N'Chuyển khoản', '14:45:00'),
('TT000000000000003', 750000.0, '2024-10-03', N'Tiền mặt', '09:15:00'),
('TT000000000000004', 980000.0, '2024-10-04', N'Chuyển khoản', '11:00:00'),
('TT000000000000005', 450000.0, '2024-10-05', N'Tiền mặt', '08:30:00'),
('TT000000000000006', 1600000.0, '2024-10-06', N'Chuyển khoản', '16:20:00'),
('TT000000000000007', 300000.0, '2024-10-07', N'Tiền mặt', '17:15:00'),
('TT000000000000008', 1050000.0, '2024-10-08', N'Chuyển khoản', '13:50:00'),
('TT000000000000009', 920000.0, '2024-10-09', N'Tiền mặt', '10:00:00'),
('TT000000000000010', 820000.0, '2024-10-10', N'Chuyển khoản', '15:35:00');

-- Insert 5 rows into the Reservation table
INSERT INTO Reservation (id, partyType, partySize, reservationDate, reservationTime, receiveDate, status, deposit, refundDeposit, employeeId, customerId, paymentId)
VALUES
('DB241018103000001', N'Tiệc sinh nhật', 10, '2024-11-01', '18:30:00', '2024-10-18', N'Confirmed', 500000.0, 0.0, 'NV001122004', 'KH241020101', 'TT000000000000010'),
('DB241018110000002', N'Tiệc gia đình', 6, '2024-11-02', '19:00:00', '2024-10-18', N'Confirmed', 300000.0, 0.0, 'NV001122004', 'KH241020101', 'TT000000000000010'),
('DB241018113000003', N'Gặp mặt bạn bè', 8, '2024-11-03', '20:00:00', '2024-10-18', N'Pending', 400000.0, 0.0, 'NV001122004', 'KH241020101', 'TT000000000000009'),
('DB241018120000004', N'Tiệc công ty', 15, '2024-11-04', '17:30:00', '2024-10-18', N'Confirmed', 800000.0, 0.0, 'NV001122004', 'KH241020101', 'TT000000000000008'),
('DB241018123000005', N'Tiệc tất niên', 12, '2024-11-05', '21:00:00', '2024-10-18', N'Pending', 600000.0, 0.0, 'NV001122004', 'KH241020101', 'TT000000000000007');

-- Insert 5 rows into the Order table
INSERT INTO [Order] (id, orderDate, notes, vatTax, paymentAmount, dispensedAmount, totalAmount, discount, customerId, employeeId, promotionId, paymentId)
VALUES
('HD000000000000001', '2024-10-18', N'Order includes seafood dishes', 0.1, 1000000.0, 1100000.0, 1100000.0, 50000.0, 'KH241020101', 'NV001122004', NULL, 'TT000000000000010'),
('HD000000000000002', '2024-10-19', N'Order includes steak and beverages', 0.1, 1500000.0, 1650000.0, 1650000.0, 75000.0, 'KH241020101', 'NV001122004', NULL, 'TT000000000000010'),
('HD000000000000003', '2024-10-20', N'Order includes vegetarian dishes', 0.1, 800000.0, 880000.0, 880000.0, 40000.0, 'KH241020101', 'NV001122004', NULL, 'TT000000000000009'),
('HD000000000000004', '2024-10-21', N'Order includes desserts and coffee', 0.1, 500000.0, 550000.0, 550000.0, 25000.0, 'KH241020101', 'NV001122004', NULL, 'TT000000000000008'),
('HD000000000000005', '2024-10-22', N'Order includes family dinner package', 0.1, 2000000.0, 2200000.0, 2200000.0, 100000.0, 'KH241020101', 'NV001122004', NULL, 'TT000000000000007');

-- Insert 5 rows into the Promotion table
INSERT INTO Promotion (id, name, startDate, endDate, discount, description, minimumOrderAmount, membershipLevel)
VALUES
('KM000000001', N'Giảm giá mùa hè', '2024-06-01', '2024-08-31', 0.10, N'Giảm 10% cho tất cả các đơn hàng trên 500.000đ', 500000.0, 1),
('KM000000002', N'Khuyến mãi cuối năm', '2024-12-01', '2024-12-31', 0.15, N'Giảm 15% cho tất cả các đơn hàng trên 1.000.000đ', 1000000.0, 1),
('KM000000003', N'Ưu đãi khách hàng mới', '2024-01-01', '2024-03-31', 0.20, N'Giảm 20% cho khách hàng mới lần đầu đặt hàng', 300000.0, 1),
('KM000000004', N'Combo gia đình', '2024-05-01', '2024-05-31', 0.05, N'Giảm 5% cho đơn hàng gia đình trên 1.500.000đ', 1500000.0, 1),
('KM000000005', N'Ưu đãi sinh nhật', '2024-10-01', '2024-10-31', 0.25, N'Giảm 25% cho khách hàng đặt tiệc sinh nhật', 2000000.0, 1);

INSERT INTO Order_Table (orderId, tableId)
VALUES
('HD000000000000001', 'T0B001'),
('HD000000000000001', 'T0B002'),
('HD000000000000001', 'T0B003'),
('HD000000000000003', 'T0B001'),
('HD000000000000003', 'T0B002');

INSERT INTO Reservation_Table(reservationId, tableId)
VALUES
('DB241018103000001', 'T0B001'),
('DB241018103000001', 'T0B002'),
('DB241018103000001', 'T0B003'),
('DB241018113000003', 'T0B001'),
('DB241018113000003', 'T0B002');

