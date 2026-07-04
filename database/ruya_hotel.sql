-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 04, 2026 at 03:53 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ruya_hotel`
--

-- --------------------------------------------------------

--
-- Table structure for table `contact_messages`
--

CREATE TABLE `contact_messages` (
  `message_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `message` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` enum('New','Read','Replied') DEFAULT 'New'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `feedback_id` int(11) NOT NULL,
  `reservation_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL CHECK (`rating` between 1 and 5),
  `comment` text NOT NULL,
  `status` enum('Active','Deleted') DEFAULT 'Active',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`feedback_id`, `reservation_id`, `user_id`, `room_id`, `rating`, `comment`, `status`, `created_at`) VALUES
(1, 16, 2, 2, 5, 'very nice', 'Active', '2026-05-25 06:14:00'),
(2, 17, 2, 4, 5, 'excelent service', 'Active', '2026-05-25 06:18:12'),
(3, 19, 2, 6, 1, 'fff', 'Active', '2026-05-25 06:57:16');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `payment_id` int(11) NOT NULL,
  `reservation_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `amount` decimal(12,2) NOT NULL CHECK (`amount` > 0),
  `payment_method` enum('Chapa','Telebirr') NOT NULL,
  `status` enum('Pending','Paid','Failed','Refunded') DEFAULT 'Pending',
  `transaction_ref` varchar(100) DEFAULT NULL,
  `paid_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`payment_id`, `reservation_id`, `user_id`, `amount`, `payment_method`, `status`, `transaction_ref`, `paid_at`, `created_at`) VALUES
(1, 2, 3, 4000.00, 'Telebirr', 'Paid', 'TXN-F65CA62C', '2026-05-24 10:47:38', '2026-05-24 10:10:00'),
(2, 4, 3, 6000.00, 'Telebirr', 'Paid', 'DEO8BBF5ZE', '2026-05-24 14:26:25', '2026-05-24 11:33:59'),
(3, 4, 3, 6000.00, 'Telebirr', 'Paid', 'DEO1AWUW6L', '2026-05-24 17:04:59', '2026-05-24 11:48:08'),
(4, 4, 3, 6000.00, 'Telebirr', 'Paid', 'DEO1AWUW6L', '2026-05-24 17:05:05', '2026-05-24 11:55:16'),
(5, 4, 3, 6000.00, 'Telebirr', 'Pending', 'DE01AWUW6L', NULL, '2026-05-24 12:26:53'),
(6, 4, 3, 6000.00, 'Telebirr', 'Pending', 'DEOLAWUW6L', NULL, '2026-05-24 12:43:11'),
(7, 4, 3, 6000.00, 'Chapa', 'Paid', 'TXN-FA6494C0', '2026-05-24 15:03:38', '2026-05-24 12:52:40'),
(8, 5, 3, 4000.00, 'Telebirr', 'Paid', 'DEO1AWUW6L', NULL, '2026-05-24 13:04:40'),
(9, 6, 3, 8000.00, 'Telebirr', 'Paid', 'DEN8AL1YUC', NULL, '2026-05-24 13:27:20'),
(10, 3, 3, 2500.00, 'Telebirr', 'Paid', 'DEN8AL1YUC', '2026-05-24 13:50:21', '2026-05-24 13:50:21'),
(11, 10, 4, 200.00, 'Telebirr', 'Paid', 'DEO8B8F5ZE', '2026-05-24 17:48:23', '2026-05-24 17:48:23'),
(12, 11, 4, 10.00, 'Telebirr', 'Paid', 'DEO1AWUW6L', '2026-05-24 17:53:41', '2026-05-24 17:53:41'),
(13, 12, 5, 2500.00, 'Telebirr', 'Paid', 'dfgtyui', '2026-05-24 19:13:09', '2026-05-24 19:13:09'),
(14, 14, 4, 300.00, 'Telebirr', 'Paid', 'sdgdhehje', '2026-05-24 20:11:59', '2026-05-24 20:11:59'),
(15, 16, 2, 2500.00, 'Telebirr', 'Paid', 'ce-isjud', '2026-05-24 21:48:10', '2026-05-24 21:48:10'),
(16, 17, 2, 4000.00, 'Telebirr', 'Paid', 'pay', '2026-05-25 03:16:55', '2026-05-25 03:16:55'),
(17, 18, 4, 150.00, 'Telebirr', 'Paid', 'cehsy676', '2026-05-25 05:16:14', '2026-05-25 05:16:13'),
(18, 19, 2, 32000.00, 'Telebirr', 'Paid', 'hhhh', '2026-05-25 06:13:41', '2026-05-25 06:13:41'),
(19, 20, 2, 2500.00, 'Telebirr', 'Paid', 'hhhhh', '2026-05-25 06:57:04', '2026-05-25 06:57:04'),
(20, 21, 9, 2500.00, 'Telebirr', 'Paid', ']sd]fghjpl', '2026-05-25 07:47:57', '2026-05-25 07:47:57'),
(21, 22, 9, 6000.00, 'Telebirr', 'Paid', 'dgjk', '2026-05-25 08:14:49', '2026-05-25 08:14:49');

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

CREATE TABLE `reservations` (
  `reservation_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `full_name` varchar(200) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `total_days` int(11) NOT NULL,
  `total_price` decimal(12,2) NOT NULL,
  `special_request` text DEFAULT NULL,
  `status` enum('Pending','Confirmed','Checked-In','Checked-Out','Cancelled','Expired') DEFAULT 'Pending',
  `payment_status` enum('Pending','Paid','Failed','Refunded') DEFAULT 'Pending',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reservations`
--

INSERT INTO `reservations` (`reservation_id`, `user_id`, `room_id`, `full_name`, `email`, `phone`, `check_in_date`, `check_out_date`, `total_days`, `total_price`, `special_request`, `status`, `payment_status`, `created_at`, `updated_at`) VALUES
(1, 2, 1, 'esrael enyew', 'esraelenyew@gmail.com', '+251986750960', '2026-05-24', '2026-05-25', 1, 2500.00, NULL, 'Cancelled', 'Pending', '2026-05-24 08:13:13', '2026-05-25 03:37:15'),
(2, 3, 3, 'Dereje Tesfaye', 'domain@gmail.com', '+251989806310', '2026-05-24', '2026-05-25', 1, 4000.00, NULL, 'Cancelled', 'Paid', '2026-05-24 10:09:19', '2026-05-25 06:59:34'),
(3, 3, 2, 'Dere', 'dere@gmail.com', '+251989806210', '2026-05-24', '2026-05-25', 1, 2500.00, NULL, 'Cancelled', 'Paid', '2026-05-24 11:31:01', '2026-05-25 06:59:37'),
(4, 3, 5, 'Dereje Tesfaye', 'domain@gmail.com', '+251989806310', '2026-05-24', '2026-05-25', 1, 6000.00, NULL, 'Cancelled', 'Paid', '2026-05-24 11:32:49', '2026-05-25 06:59:43'),
(5, 3, 4, 'Dereje Tesfaye', 'domain@gmail.com', '+251989806310', '2026-05-24', '2026-05-25', 1, 4000.00, NULL, 'Cancelled', 'Paid', '2026-05-24 12:02:21', '2026-05-25 06:59:45'),
(6, 3, 6, 'DEREJE TESFAYE', 'dereje@gmail.com', '+251989806310', '2026-05-24', '2026-05-25', 1, 8000.00, NULL, 'Confirmed', 'Paid', '2026-05-24 13:06:25', '2026-05-24 13:27:20'),
(7, 3, 9, 'dere', 'dere@gmail.com', '+251989806310', '2026-05-24', '2027-05-25', 366, 36600.00, NULL, 'Cancelled', 'Pending', '2026-05-24 15:05:50', '2026-05-25 03:29:13'),
(8, 3, 10, 'dereee', 'deeee@gmail.com', '+251989806310', '2026-05-24', '2026-05-27', 3, 450.00, NULL, 'Cancelled', 'Pending', '2026-05-24 15:25:05', '2026-05-25 03:29:10'),
(9, 3, 9, 'dereje', 'dere@gmail.com', '+251989806310', '2026-05-24', '2026-05-27', 3, 300.00, NULL, 'Cancelled', 'Pending', '2026-05-24 16:58:03', '2026-05-25 03:29:01'),
(10, 4, 11, 'eremi', 'domain1@gmail.com', '+251989806432', '2026-05-24', '2026-05-25', 1, 200.00, NULL, 'Checked-In', 'Paid', '2026-05-24 17:31:11', '2026-05-25 03:35:35'),
(11, 4, 12, 'ermi1', 'gdgg@gmail.com', '+2519876543', '2026-05-24', '2026-05-26', 2, 10.00, NULL, 'Confirmed', 'Paid', '2026-05-24 17:52:56', '2026-05-24 17:53:41'),
(12, 5, 1, 'yetnayet shewangizaw', 'shewangizawyetnayet@gmail.com', '+251918917643', '2026-05-24', '2026-05-25', 1, 2500.00, NULL, 'Confirmed', 'Paid', '2026-05-24 19:10:19', '2026-05-24 19:13:09'),
(13, 5, 5, 'yetnayet shewangizaw', 'shewangizawyetnayet@gmail.com', '+251918917643', '2026-05-24', '2026-05-25', 1, 6000.00, NULL, 'Expired', 'Pending', '2026-05-24 19:15:27', '2026-05-24 21:45:22'),
(14, 4, 10, 'ermiyas degu', 'ermiyasdegu@gmail.com', '+251944007712', '2026-05-24', '2026-05-26', 2, 300.00, NULL, 'Checked-Out', 'Paid', '2026-05-24 20:11:42', '2026-05-25 07:53:25'),
(15, 4, 9, 'ermiyasdegu', 'ermiyasdegu@gmial.com', '+251944007712', '2026-05-24', '2026-05-26', 2, 200.00, NULL, 'Expired', 'Pending', '2026-05-24 20:57:54', '2026-05-24 21:45:22'),
(16, 2, 2, 'esrael enyew', 'esraelenyew@gmail.com', '+251977299905', '2026-05-25', '2026-05-26', 1, 2500.00, NULL, 'Confirmed', 'Paid', '2026-05-24 21:47:53', '2026-05-24 21:48:10'),
(17, 2, 4, 'esrael enyew', 'esraelenyew@gmail.com', '+251977299905', '2026-05-25', '2026-05-26', 1, 4000.00, NULL, 'Confirmed', 'Paid', '2026-05-25 03:16:09', '2026-05-25 03:16:55'),
(18, 4, 10, 'ermiyas degu', 'ermiyasdegu@gmail.com', '+251944007712', '2026-05-25', '2026-05-26', 1, 150.00, NULL, 'Confirmed', 'Paid', '2026-05-25 05:16:02', '2026-05-25 05:16:14'),
(19, 2, 6, 'esrael enyew', 'esraelenyew@gmail.com', '+251977299905', '2026-05-25', '2026-05-29', 4, 32000.00, NULL, 'Confirmed', 'Paid', '2026-05-25 06:13:02', '2026-05-25 06:13:41'),
(20, 2, 1, 'esrael enyew', 'esraelenyew@gmail.com', '+251977299905', '2026-05-25', '2026-05-26', 1, 2500.00, NULL, 'Confirmed', 'Paid', '2026-05-25 06:56:57', '2026-05-25 06:57:04'),
(21, 9, 2, 'sisay alemayew', 'sis@gmail.com', '+251977299905', '2026-05-25', '2026-05-26', 1, 2500.00, NULL, 'Confirmed', 'Paid', '2026-05-25 07:47:36', '2026-05-25 07:47:57'),
(22, 9, 5, 'sisay', 'sis@gmail.com', '+251912345678', '2026-05-25', '2026-05-26', 1, 6000.00, NULL, 'Confirmed', 'Paid', '2026-05-25 08:14:27', '2026-05-25 08:14:49');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(50) NOT NULL,
  `description` text DEFAULT NULL,
  `permissions` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`permissions`)),
  `status` enum('Active','Inactive','Deleted') DEFAULT 'Active',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`role_id`, `role_name`, `description`, `permissions`, `status`, `created_at`, `updated_at`) VALUES
(1, 'Admin', 'Full system access', '[\"users\",\"rooms\",\"reservations\",\"payments\",\"feedback\",\"roles\",\"reports\",\"settings\",\"trash\"]', 'Active', '2026-05-24 05:17:00', '2026-05-24 05:17:00'),
(2, 'Manager', 'Manage rooms and reservations', '[\"rooms\",\"reservations\",\"payments\",\"feedback\",\"reports\"]', 'Active', '2026-05-24 05:17:00', '2026-05-24 05:17:00'),
(3, 'Receptionist', 'Handle check-in/check-out', '[\"reservations\",\"rooms\"]', 'Active', '2026-05-24 05:17:00', '2026-05-24 05:17:00'),
(4, 'Customer', 'Book rooms and manage profile', '[\"profile\",\"bookings\",\"payments\",\"feedback\"]', 'Active', '2026-05-24 05:17:00', '2026-05-24 05:17:00'),
(5, 'staffm', 'staffmanager ', '[\"users\",\"rooms\"]', 'Active', '2026-05-24 22:12:16', '2026-05-24 22:12:16'),
(6, 'roomservice', 'great service ', '[\"users\",\"rooms\",\"reservations\",\"feedback\"]', 'Active', '2026-05-25 03:31:59', '2026-05-25 03:31:59');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `room_id` int(11) NOT NULL,
  `room_number` varchar(20) NOT NULL,
  `room_type` enum('Standard','Double','Deluxe','Family','Presidential Suite') NOT NULL,
  `price_per_night` decimal(10,2) NOT NULL CHECK (`price_per_night` > 0),
  `capacity` int(11) NOT NULL CHECK (`capacity` > 0),
  `description` text DEFAULT NULL,
  `features` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`features`)),
  `images` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`images`)),
  `status` enum('Available','Reserved','Occupied','Maintenance','Deleted') DEFAULT 'Available',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`room_id`, `room_number`, `room_type`, `price_per_night`, `capacity`, `description`, `features`, `images`, `status`, `created_at`, `updated_at`) VALUES
(1, '101', 'Standard', 2500.00, 2, 'Cozy standard room with essential amenities.', '[\"WiFi\",\"TV\",\"AC\",\"Hot Shower\"]', '[\"standard.jpg\"]', 'Reserved', '2026-05-24 05:17:00', '2026-05-25 06:56:57'),
(2, '102', 'Standard', 2500.00, 2, 'Cozy standard room with essential amenities.', '[\"WiFi\",\"TV\",\"AC\",\"Hot Shower\"]', '[\"standard.jpg\"]', 'Reserved', '2026-05-24 05:17:00', '2026-05-25 07:47:36'),
(3, '201', 'Double', 4000.00, 2, 'Spacious double room with city view.', '[\"WiFi\",\"TV\",\"AC\",\"Mini Bar\",\"Balcony\"]', '[\"double.jpg\"]', 'Available', '2026-05-24 05:17:00', '2026-05-25 06:59:34'),
(4, '202', 'Double', 4000.00, 2, 'Spacious double room with city view.', '[\"WiFi\",\"TV\",\"AC\",\"Mini Bar\",\"Balcony\"]', '[\"double.jpg\"]', 'Available', '2026-05-24 05:17:00', '2026-05-25 06:59:45'),
(5, '301', 'Deluxe', 6000.00, 2, 'Luxury deluxe room with premium services.', '[\"WiFi\",\"TV\",\"AC\",\"Mini Bar\",\"Jacuzzi\",\"Room Service\"]', '[\"deluxe.jpg\"]', 'Reserved', '2026-05-24 05:17:00', '2026-05-25 08:14:27'),
(6, '401', 'Family', 8000.00, 4, 'Perfect for families with connecting rooms.', '[\"WiFi\",\"TV\",\"AC\",\"Mini Bar\",\"Kitchenette\",\"Kids Area\"]', '[\"family.jpg\"]', 'Reserved', '2026-05-24 05:17:00', '2026-05-25 06:13:02'),
(7, '501', 'Presidential Suite', 15000.00, 4, 'Ultimate luxury with panoramic views.', '[\"WiFi\",\"TV\",\"AC\",\"Mini Bar\",\"Jacuzzi\",\"Private Pool\",\"Butler Service\",\"Lounge\"]', '[\"presidential.jpg\"]', 'Deleted', '2026-05-24 05:17:00', '2026-05-24 19:50:53'),
(9, '901', 'Deluxe', 100.00, 1, 'bhjdhdhfg', '[\"WiFi\",\"TV\",\"AC\"]', '[\"deluxe.jpg\"]', 'Available', '2026-05-24 15:04:32', '2026-05-25 03:29:01'),
(10, '104', 'Double', 150.00, 1, 'dhshf', '[\"WiFi\",\"TV\",\"AC\"]', '[\"double.jpg\"]', 'Available', '2026-05-24 15:19:39', '2026-05-25 07:53:25'),
(11, '390', 'Double', 200.00, 1, 'bjhfsbbdfbbfb', '[\"WiFi\",\"TV\",\"AC\"]', '[\"double.jpg\"]', 'Occupied', '2026-05-24 16:55:06', '2026-05-25 03:35:35'),
(12, '604', 'Standard', 5.00, 1, 'vvfgfgfg', '[\"WiFi\",\"TV\",\"AC\"]', '[\"standard.jpg\"]', 'Deleted', '2026-05-24 17:51:36', '2026-05-25 05:21:54'),
(13, '605', 'Presidential Suite', 10000.00, 5, 'wifi,tv,airfresh,Mini Bar', '[\"WiFi\",\"TV\",\"AC\"]', '[\"room.jpg\"]', 'Available', '2026-05-24 20:04:31', '2026-05-25 07:51:58'),
(14, '222', 'Family', 2345.00, 5, 'best', '[\"WiFi\",\"TV\",\"AC\"]', '[\"family.jpg\"]', 'Available', '2026-05-25 07:58:08', '2026-05-25 07:58:08');

-- --------------------------------------------------------

--
-- Table structure for table `system_settings`
--

CREATE TABLE `system_settings` (
  `setting_id` int(11) NOT NULL,
  `setting_key` varchar(100) NOT NULL,
  `setting_value` text DEFAULT NULL,
  `category` enum('Hotel','Business','Payment','Room','User','Notification') DEFAULT 'Hotel',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `system_settings`
--

INSERT INTO `system_settings` (`setting_id`, `setting_key`, `setting_value`, `category`, `updated_at`) VALUES
(1, 'hotel_name', 'RuyaHotel', 'Hotel', '2026-05-25 03:39:10'),
(2, 'hotel_description', '', 'Hotel', '2026-05-25 03:39:10'),
(3, 'hotel_address', '', 'Hotel', '2026-05-25 03:39:10'),
(4, 'hotel_phone', '+251977299905', 'Hotel', '2026-05-25 03:39:10'),
(5, 'hotel_email', 'hotelruya@gmail.com', 'Hotel', '2026-05-25 03:39:10'),
(6, 'check_in_time', '14:00', 'Business', '2026-05-24 05:17:00'),
(7, 'check_out_time', '12:00', 'Business', '2026-05-24 05:17:00'),
(8, 'cancellation_policy', 'No cancellation after check-in date', 'Business', '2026-05-24 05:17:00'),
(9, 'refund_policy', 'No refunds after payment', 'Business', '2026-05-24 05:17:00'),
(10, 'currency', 'ETB', 'Payment', '2026-05-24 05:17:00'),
(11, 'chapa_enabled', 'true', 'Payment', '2026-05-24 05:17:00'),
(12, 'telebirr_enabled', 'true', 'Payment', '2026-05-24 05:17:00'),
(13, 'auto_expire_reservations', 'ewa', 'Room', '2026-05-24 22:47:31'),
(14, 'session_timeout', '30', 'User', '2026-05-24 05:17:00');

-- --------------------------------------------------------

--
-- Table structure for table `trash_log`
--

CREATE TABLE `trash_log` (
  `trash_id` int(11) NOT NULL,
  `item_type` enum('User','Room','Reservation','Feedback','Role') NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_name` varchar(200) DEFAULT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  `deleted_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `original_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`original_data`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `trash_log`
--

INSERT INTO `trash_log` (`trash_id`, `item_type`, `item_id`, `item_name`, `deleted_by`, `deleted_at`, `original_data`) VALUES
(1, 'User', 2, 'Esrael Enyew', 1, '2026-05-24 11:04:46', '{\"username\":\"esru\",\"email\":\"esraelenyew@gmail.com\",\"role_id\":4}'),
(4, 'Room', 11, '390 - Double', 1, '2026-05-25 06:58:47', '{\"room_number\":\"390\",\"room_type\":\"Double\",\"price_per_night\":200.00,\"capacity\":1,\"description\":\"bjhfsbbdfbbfb\",\"features\":[\"WiFi\",\"TV\",\"AC\"],\"images\":[\"double.jpg\"],\"status\":\"Occupied\"}');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `gender` enum('Male','Female','Other') DEFAULT 'Other',
  `nationality` varchar(50) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `profile_picture` varchar(255) DEFAULT NULL,
  `role_id` int(11) DEFAULT 3,
  `account_status` enum('Active','Inactive','Deleted') DEFAULT 'Active',
  `registration_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_login` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `username`, `email`, `phone`, `password_hash`, `gender`, `nationality`, `address`, `profile_picture`, `role_id`, `account_status`, `registration_date`, `last_login`) VALUES
(1, 'System', 'Admin', 'admin', 'admin@ruyahotel.com', '+251911000000', 'O2Esdae1BIpDX7bsgeUv+S1teVqLWpwXBw9qY8l6U7I=', 'Other', 'Ethiopia', NULL, NULL, 1, 'Active', '2026-05-24 05:17:00', '2026-06-17 11:00:00'),
(2, 'Esrael', 'Enyew', 'esru', 'esraelenyew@gmail.com', '+251986750960', 'R22a3HVNbkNcI3kcQeWsZSFp9Bo5lnisHUd8284cKpU=', 'Female', 'ethiopian', NULL, NULL, 4, 'Active', '2026-05-24 06:35:34', '2026-06-17 11:01:06'),
(3, 'dereje', 'Tesfaye', 'dere', 'domain@gmail.com', '+251989806310', '2eI6Rdg3gyIAfiEN/6otQAAeiKG9hv6i7dDrRn7efHU=', 'Male', 'Ethiopia', NULL, NULL, 4, 'Deleted', '2026-05-24 10:07:37', '2026-05-24 16:57:10'),
(4, 'ermi', 'abc', 'ermi', 'ermiyasdegu@gmail.com', '+25198778654', 'xAqO4B0+N09nG+Ey6ht7T5i8WvNKFFFRI6riOgLjedA=', 'Male', 'Ethiopia', NULL, NULL, 4, 'Active', '2026-05-24 17:29:59', '2026-05-25 05:59:16'),
(5, 'yetinayet', 'shewangzaw', 'yeti', 'shewangizawyetnayet@gmail.com', '+251918917643', 'sRw3pUTJCmVM7SHLJKk5vkskoNLYRm2jS4DLVv2j3T8=', 'Female', 'ethiopian', NULL, NULL, 4, 'Active', '2026-05-24 19:09:26', '2026-05-24 20:01:49'),
(7, 'hermela', 'habtewold', 'herry', 'herry123@gmail.com', '+251977223366', 'd4doOQ/7uI+UQU+rymUF2OyfNnrmHn+HEVlXizPxRI4=', NULL, NULL, NULL, NULL, 3, 'Active', '2026-05-24 22:45:22', NULL),
(8, 'olgiyas', 'niguse', 'olgi', 'olgi@gmail.com', '+251967676565', 'c7zmGZbd3I11nRDqW11paweLJ5JySxgp0euDPH0Z2YI=', NULL, NULL, NULL, NULL, 6, 'Active', '2026-05-25 05:18:28', '2026-05-25 05:20:15'),
(9, 'sisay', 'alemayew', 'sisy', 'sis@gmail.com', '+251912345678', 'rkawt6Xexn7dC67kBf/jpv7hFIx7wl2IAuJardAU5Bo=', 'Male', 'ethiopia', NULL, NULL, 4, 'Active', '2026-05-25 07:45:30', '2026-05-25 08:23:29');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `contact_messages`
--
ALTER TABLE `contact_messages`
  ADD PRIMARY KEY (`message_id`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`feedback_id`),
  ADD KEY `reservation_id` (`reservation_id`),
  ADD KEY `idx_feedback_user` (`user_id`),
  ADD KEY `idx_feedback_room` (`room_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`payment_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `idx_payments_reservation` (`reservation_id`);

--
-- Indexes for table `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`reservation_id`),
  ADD KEY `idx_reservations_user` (`user_id`),
  ADD KEY `idx_reservations_room` (`room_id`),
  ADD KEY `idx_reservations_status` (`status`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`),
  ADD UNIQUE KEY `role_name` (`role_name`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`room_id`),
  ADD UNIQUE KEY `room_number` (`room_number`);

--
-- Indexes for table `system_settings`
--
ALTER TABLE `system_settings`
  ADD PRIMARY KEY (`setting_id`),
  ADD UNIQUE KEY `setting_key` (`setting_key`);

--
-- Indexes for table `trash_log`
--
ALTER TABLE `trash_log`
  ADD PRIMARY KEY (`trash_id`),
  ADD KEY `deleted_by` (`deleted_by`),
  ADD KEY `idx_trash_type` (`item_type`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_users_role` (`role_id`),
  ADD KEY `idx_users_status` (`account_status`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `contact_messages`
--
ALTER TABLE `contact_messages`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `feedback_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `payment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `reservation_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `room_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `system_settings`
--
ALTER TABLE `system_settings`
  MODIFY `setting_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `trash_log`
--
ALTER TABLE `trash_log`
  MODIFY `trash_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`reservation_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `feedback_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `feedback_ibfk_3` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE CASCADE;

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`reservation_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`);

--
-- Constraints for table `trash_log`
--
ALTER TABLE `trash_log`
  ADD CONSTRAINT `trash_log_ibfk_1` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
