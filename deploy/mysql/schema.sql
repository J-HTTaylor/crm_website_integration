-- MySQL dump 10.13  Distrib 8.0.46, for Linux (x86_64)
--
-- Host: localhost    Database: velorim_crm
-- ------------------------------------------------------
-- Server version	8.0.46-0ubuntu0.24.04.4

--
-- Table structure for table `users`
--
SET foreign_key_checks = 0;
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(254) NOT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `avatar` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`username`)
);


--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `SKU` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`name`)
);


--
-- Table structure for table `centers`
--

DROP TABLE IF EXISTS `centers`;
CREATE TABLE `centers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `join_date` date NOT NULL,
  `address_line_1` varchar(200) NOT NULL,
  `address_line_2` varchar(200) NOT NULL,
  `city` varchar(100) NOT NULL,
  `postcode` varchar(11) NOT NULL,
  `country` char(3) NOT NULL,
  `linked_user`varchar(50) NOT NULL,
  `image` varchar(200) default NULL,
  PRIMARY KEY (`id`),
  KEY `fk_country` (`country`),
  CONSTRAINT `fk_country` FOREIGN KEY (`country`) REFERENCES `countries` (`iso3`) ON UPDATE CASCADE,
  KEY `fk_linked_user` (`linked_user`),
  CONSTRAINT `fk_linked_user` FOREIGN KEY (`linked_user`) REFERENCES `users` (`username`) ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `collections`;
CREATE TABLE `collections` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `assigned_to`varchar(50) NOT NULL,
  `due_date` date NOT NULL DEFAULT ((CURDATE()) + INTERVAL 7 DAY),
  `completed` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` date not null default (curdate()),
  `link` varchar(255),
  `service` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_collections_product` (`product_id`),
  CONSTRAINT `fk_collections_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON UPDATE CASCADE,
  KEY `fk_collections_assigned_to` (`assigned_to`),
  CONSTRAINT `fk_collections_assigned_to` FOREIGN KEY (`assigned_to`) REFERENCES `users` (`username`) ON UPDATE CASCADE
);


-- join table
drop table if exists `center_products`;
CREATE TABLE `center_products` (
  `center_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`center_id`, `product_id`),
  CONSTRAINT `fk_cp_center` FOREIGN KEY (`center_id`) REFERENCES `centers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cp_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE);

SET foreign_key_checks = 1;

-- test data
insert into `users` (`username`, `password`, `email`, `admin`, `avatar`) values
('admin', 'admin', 'james.ht.taylor@gmail.com', 1, NULL),
('testUser1', 'test1', 'example@gmail.com', 0, NULL),
('testUser2', 'test2', 'example@gmail.com', 0, NULL),
('testUser3', 'test3', 'example@gmail.com', 0, NULL);

insert into `products` (`name`, `price`, `SKU`) values
('Tyre', 1.50, 'T150'),
('Inner Tube', 1.00, 'IT100'),
('Li-ion Battery', 30.00, 'LB3000');

INSERT INTO `centers` (`name`, `status`, `join_date`,  `address_line_1`, `address_line_2`, `city`, `postcode`, `country`, `linked_user`, `image`) VALUES 
('Downtown Distribution Center', 1, '2026-03-31', '123 Main Street', 'Newtown', 'London', 'EC1A 1BB', 'GBR', 'testUser1', NULL),
('Uptown Distribution Center', 1, '2025-08-25', '3 side Street', 'new Town', 'London', 'SW1D 2AS', 'GBR', 'testUser2', NULL),
('Main Distribution Center', 1, '2026-02-12', 'Dead end', 'new Town', 'London', 'WS12 4FD', 'GBR', 'testUser3', NULL);

insert into `center_products` (`center_id`, `product_id`) values
(1,1),
(2,3),
(2,1),
(3,1),
(3,2),
(3,3);