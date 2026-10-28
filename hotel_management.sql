-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 25, 2026 lúc 08:19 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `hotel_management`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bookings`
--

CREATE TABLE `bookings` (
  `booking_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `status` enum('BOOKED','CHECKED_IN','CHECKED_OUT','CANCELLED') NOT NULL DEFAULT 'BOOKED',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `note` varchar(255) DEFAULT NULL
) ;

--
-- Đang đổ dữ liệu cho bảng `bookings`
--

INSERT INTO `bookings` (`booking_id`, `customer_id`, `check_in_date`, `check_out_date`, `status`, `created_at`, `note`) VALUES
(1, 1, '2026-10-26', '2026-10-28', 'BOOKED', '2026-10-25 07:08:43', 'Dat truoc'),
(2, 2, '2026-10-20', '2026-10-22', 'CHECKED_OUT', '2026-10-25 07:08:43', 'Da tra phong'),
(3, 3, '2026-10-25', '2026-10-27', 'CHECKED_IN', '2026-10-25 07:08:43', 'Dang o'),
(4, 4, '2026-10-29', '2026-10-31', 'BOOKED', '2026-10-25 07:08:43', 'Khach dat truoc');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `booking_rooms`
--

CREATE TABLE `booking_rooms` (
  `booking_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `price_per_night` decimal(12,2) NOT NULL
) ;

--
-- Đang đổ dữ liệu cho bảng `booking_rooms`
--

INSERT INTO `booking_rooms` (`booking_id`, `room_id`, `price_per_night`) VALUES
(1, 1, 300000.00),
(2, 4, 500000.00),
(3, 2, 300000.00),
(4, 6, 550000.00);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `identity_no` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `customers`
--

INSERT INTO `customers` (`customer_id`, `full_name`, `identity_no`, `phone`, `email`, `address`) VALUES
(1, 'Nguyen Van An', '001234567890', '0901234567', 'an@gmail.com', 'Ha Noi'),
(2, 'Tran Thi Binh', '001234567891', '0912345678', 'binh@gmail.com', 'Hai Phong'),
(3, 'Le Van Cuong', '001234567892', '0923456789', 'cuong@gmail.com', 'Da Nang'),
(4, 'Pham Thi Dung', '001234567893', '0934567890', 'dung@gmail.com', 'Ho Chi Minh'),
(5, 'Hoang Van Em', '001234567894', '0945678901', 'em@gmail.com', 'Nam Dinh');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `invoices`
--

CREATE TABLE `invoices` (
  `invoice_id` int(11) NOT NULL,
  `booking_id` int(11) NOT NULL,
  `issued_at` datetime DEFAULT current_timestamp(),
  `room_amount` decimal(12,2) NOT NULL DEFAULT 0.00,
  `surcharge` decimal(12,2) NOT NULL DEFAULT 0.00,
  `discount` decimal(12,2) NOT NULL DEFAULT 0.00,
  `total_amount` decimal(12,2) NOT NULL DEFAULT 0.00,
  `payment_method` varchar(50) DEFAULT NULL,
  `payment_status` enum('UNPAID','PAID','REFUNDED') NOT NULL DEFAULT 'UNPAID'
) ;

--
-- Đang đổ dữ liệu cho bảng `invoices`
--

INSERT INTO `invoices` (`invoice_id`, `booking_id`, `issued_at`, `room_amount`, `surcharge`, `discount`, `total_amount`, `payment_method`, `payment_status`) VALUES
(1, 1, '2026-10-25 14:08:43', 600000.00, 0.00, 0.00, 600000.00, 'Tien mat', 'UNPAID'),
(2, 2, '2026-10-25 14:08:43', 1000000.00, 50000.00, 50000.00, 1000000.00, 'Chuyen khoan', 'PAID'),
(3, 3, '2026-10-25 14:08:43', 600000.00, 0.00, 0.00, 600000.00, 'Tien mat', 'UNPAID'),
(4, 4, '2026-10-25 14:08:43', 1100000.00, 0.00, 0.00, 1100000.00, 'The', 'UNPAID');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `rooms`
--

CREATE TABLE `rooms` (
  `room_id` int(11) NOT NULL,
  `room_number` varchar(10) NOT NULL,
  `room_type_id` int(11) NOT NULL,
  `price_per_night` decimal(12,2) NOT NULL,
  `status` enum('AVAILABLE','OCCUPIED','MAINTENANCE','CLEANING') NOT NULL DEFAULT 'AVAILABLE',
  `description` varchar(255) DEFAULT NULL
) ;

--
-- Đang đổ dữ liệu cho bảng `rooms`
--

INSERT INTO `rooms` (`room_id`, `room_number`, `room_type_id`, `price_per_night`, `status`, `description`) VALUES
(1, '101', 1, 300000.00, 'AVAILABLE', 'Tang 1'),
(2, '102', 1, 300000.00, 'AVAILABLE', 'Tang 1'),
(3, '103', 1, 350000.00, 'AVAILABLE', 'Tang 1'),
(4, '201', 2, 500000.00, 'OCCUPIED', 'Tang 2'),
(5, '202', 2, 500000.00, 'CLEANING', 'Tang 2'),
(6, '203', 2, 550000.00, 'AVAILABLE', 'Tang 2'),
(7, '301', 3, 1000000.00, 'MAINTENANCE', 'Tang 3'),
(8, '302', 3, 1000000.00, 'AVAILABLE', 'Tang 3'),
(9, '401', 2, 123000.00, 'AVAILABLE', 'tang 4');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `room_types`
--

CREATE TABLE `room_types` (
  `room_type_id` int(11) NOT NULL,
  `type_name` varchar(50) NOT NULL,
  `capacity` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `room_types`
--

INSERT INTO `room_types` (`room_type_id`, `type_name`, `capacity`, `description`) VALUES
(1, 'Phong don', 1, 'Phong cho mot nguoi'),
(2, 'Phong doi', 2, 'Phong cho hai nguoi'),
(3, 'Phong VIP', 4, 'Phong cao cap');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `role` enum('ADMIN','STAFF') NOT NULL DEFAULT 'STAFF',
  `is_active` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`user_id`, `username`, `password_hash`, `full_name`, `role`, `is_active`) VALUES
(1, 'admin', '123456', 'Quan tri vien', 'ADMIN', 1),
(2, 'nhanvien', '123456', 'Nguyen Van An', 'STAFF', 1),
(3, '1', '1', '1', 'ADMIN', 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Chỉ mục cho bảng `booking_rooms`
--
ALTER TABLE `booking_rooms`
  ADD PRIMARY KEY (`booking_id`,`room_id`),
  ADD KEY `room_id` (`room_id`);

--
-- Chỉ mục cho bảng `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`),
  ADD UNIQUE KEY `identity_no` (`identity_no`);

--
-- Chỉ mục cho bảng `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`invoice_id`),
  ADD UNIQUE KEY `booking_id` (`booking_id`);

--
-- Chỉ mục cho bảng `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`room_id`),
  ADD UNIQUE KEY `room_number` (`room_number`),
  ADD KEY `room_type_id` (`room_type_id`);

--
-- Chỉ mục cho bảng `room_types`
--
ALTER TABLE `room_types`
  ADD PRIMARY KEY (`room_type_id`),
  ADD UNIQUE KEY `type_name` (`type_name`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `bookings`
--
ALTER TABLE `bookings`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `invoices`
--
ALTER TABLE `invoices`
  MODIFY `invoice_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `rooms`
--
ALTER TABLE `rooms`
  MODIFY `room_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `room_types`
--
ALTER TABLE `room_types`
  MODIFY `room_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Các ràng buộc cho bảng `booking_rooms`
--
ALTER TABLE `booking_rooms`
  ADD CONSTRAINT `booking_rooms_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
  ADD CONSTRAINT `booking_rooms_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`);

--
-- Các ràng buộc cho bảng `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`);

--
-- Các ràng buộc cho bảng `rooms`
--
ALTER TABLE `rooms`
  ADD CONSTRAINT `rooms_ibfk_1` FOREIGN KEY (`room_type_id`) REFERENCES `room_types` (`room_type_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
