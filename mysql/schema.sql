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
  PRIMARY KEY (`id`)
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
  `products` varchar(255) NOT NULL,
  `address_line_1` varchar(200) NOT NULL,
  `address_line_2` varchar(200) NOT NULL,
  `city` varchar(100) NOT NULL,
  `postcode` varchar(11) NOT NULL,
  `country` char(3) NOT NULL,
  `linked_user`varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_centers_products` (`products`),
  CONSTRAINT `fk_centers_products` FOREIGN KEY (`products`) REFERENCES `products` (`name`) ON UPDATE CASCADE,
  KEY `fk_country` (`country`),
  CONSTRAINT `fk_country` FOREIGN KEY (`country`) REFERENCES `countries` (`iso3`) ON UPDATE CASCADE,
  KEY `fk_linked_user` (`linked_user`),
  CONSTRAINT `fk_linked_user` FOREIGN KEY (`linked_user`) REFERENCES `users` (`username`) ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `collections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collections` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product` varchar(200) NOT NULL,
  `asigned_to`varchar(50) NOT NULL,
  `due_date` date NOT NULL DEFAULT (CURDATE() + INTERVAL 7 DAY),
  `completed` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` date not null default curdate(),
  `link` varchar(255),
  `service` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_collections_product` (`product`),
  CONSTRAINT `fk_collections_product` FOREIGN KEY (`product`) REFERENCES `products` (`name`) ON UPDATE CASCADE,
  KEY `fk_collections_assigned_to` (`asigned_to`),
  CONSTRAINT `fk_collections_assigned_to` FOREIGN KEY (`asigned_to`) REFERENCES `users` (`username`) ON UPDATE CASCADE
);
SET foreign_key_checks = 0;