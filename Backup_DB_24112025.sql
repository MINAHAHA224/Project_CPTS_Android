CREATE DATABASE  IF NOT EXISTS `laptopshop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `laptopshop`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: laptopshop
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `SPRING_SESSION`
--

DROP TABLE IF EXISTS `SPRING_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPRING_SESSION` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SPRING_SESSION`
--

LOCK TABLES `SPRING_SESSION` WRITE;
/*!40000 ALTER TABLE `SPRING_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `SPRING_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SPRING_SESSION_ATTRIBUTES`
--

DROP TABLE IF EXISTS `SPRING_SESSION_ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPRING_SESSION_ATTRIBUTES` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SPRING_SESSION_ATTRIBUTES`
--

LOCK TABLES `SPRING_SESSION_ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `SPRING_SESSION_ATTRIBUTES` DISABLE KEYS */;
/*!40000 ALTER TABLE `SPRING_SESSION_ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_method`
--

DROP TABLE IF EXISTS `auth_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_method` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `login_type` varchar(20) NOT NULL,
  `external_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`login_type`),
  CONSTRAINT `auth_method_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `auth_method_chk_1` CHECK ((`login_type` in (_utf8mb4'EMAIL',_utf8mb4'GOOGLE',_utf8mb4'FACEBOOK',_utf8mb4'GITHUB')))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_method`
--

LOCK TABLES `auth_method` WRITE;
/*!40000 ALTER TABLE `auth_method` DISABLE KEYS */;
INSERT INTO `auth_method` VALUES (1,62,'GOOGLE','114989178555441004075'),(2,64,'GOOGLE','105195904420947974280');
/*!40000 ALTER TABLE `auth_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_detail`
--

DROP TABLE IF EXISTS `cart_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `quantity` bigint NOT NULL,
  `cart_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbql1m2v2po7hcawonqsgqex88` (`cart_id`),
  KEY `FKclb1c0wg3mofxnpgidib1t987` (`product_id`),
  CONSTRAINT `FKbql1m2v2po7hcawonqsgqex88` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`),
  CONSTRAINT `FKclb1c0wg3mofxnpgidib1t987` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_detail`
--

LOCK TABLES `cart_detail` WRITE;
/*!40000 ALTER TABLE `cart_detail` DISABLE KEYS */;
INSERT INTO `cart_detail` VALUES (54,15490000,4,34,2),(56,15490000,11,35,2),(57,17490000,3,35,1),(58,11900000,2,35,4),(59,26999000,1,35,9),(60,24990000,1,35,7),(61,31490000,1,35,6),(62,17490000,1,34,1),(63,11900000,1,34,4),(64,21399001,1,34,10),(65,26999000,1,34,9),(66,19500000,2,34,3),(71,23490000,4,36,8),(73,15490000,4,37,2),(74,11900000,3,37,4),(75,24990000,2,37,7),(76,19500000,1,37,3),(77,26999000,1,37,9),(78,15490000,1,39,2),(79,19500000,2,39,3),(80,11900000,3,39,4),(81,15490000,3,41,2),(83,11900000,3,41,4),(85,15490000,3,43,2),(86,15490000,2,44,2),(87,19500000,1,44,3),(88,11900000,2,44,4),(91,21399001,1,45,10),(92,21399001,1,46,10),(98,11900000,1,47,4),(102,24990000,3,48,7),(103,21399001,2,48,10);
/*!40000 ALTER TABLE `cart_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sum` int NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `carts_users_id_fk` (`user_id`),
  CONSTRAINT `carts_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `carts_chk_1` CHECK ((`sum` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (34,6,55,'ACTIVE'),(35,6,56,'ORDERED'),(36,3,63,'ORDERED'),(37,5,64,'ORDERED'),(39,3,64,'ORDERED'),(41,3,64,'ORDERED'),(43,1,64,'ORDERED'),(44,3,64,'ORDERED'),(45,1,63,'ORDERED'),(46,1,63,'ORDERED'),(47,1,63,'ORDERED'),(48,2,63,'ORDERED');
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `quantity` bigint NOT NULL,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrws2q0si6oyd6il8gqe2aennc` (`order_id`),
  KEY `FKc7q42e9tu0hslx6w4wxgomhvn` (`product_id`),
  CONSTRAINT `FKc7q42e9tu0hslx6w4wxgomhvn` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKrws2q0si6oyd6il8gqe2aennc` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (6,39000000,2,4,3),(7,15490000,1,4,2),(8,30980000,2,5,2),(9,23800000,2,5,4),(10,49980000,2,5,7),(11,15490000,1,6,2),(12,39000000,2,6,3),(13,35700000,3,6,4),(14,46470000,3,7,2),(15,35700000,3,7,4),(16,46470000,3,8,2),(35,30980000,2,17,2),(36,19500000,1,17,3),(37,23800000,2,17,4),(38,17490000,1,18,1),(39,70470000,3,18,8),(40,31490000,1,18,6),(41,17490000,1,19,1),(42,70470000,3,19,8),(43,31490000,1,19,6),(47,93960000,4,23,8),(48,21399001,1,24,10),(49,21399001,1,25,10),(50,26999000,1,26,9),(51,26999000,1,27,9),(52,11900000,1,28,4),(53,11900000,1,29,4),(54,11900000,1,30,4),(55,11900000,1,31,4),(56,11900000,1,32,4),(57,11900000,1,33,4),(58,11900000,1,34,4),(59,11900000,1,35,4),(60,11900000,1,36,4),(61,11900000,1,37,4),(62,11900000,1,38,4),(63,11900000,1,39,4),(64,11900000,1,40,4),(65,11900000,1,41,4),(66,74970000,3,42,7),(67,42798002,2,42,10);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `total_price` double NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `receiver_address` varchar(255) DEFAULT NULL,
  `receiver_name` varchar(255) DEFAULT NULL,
  `receiver_phone` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `type_payment` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `status_payment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  KEY `IX_TIME_ID_ORDERS` (`time`,`id` DESC),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (4,54490000,56,'22323dddd','caoduythai','123123123','PENDING','2025-05-24 14:31:51','COD','UNPAID'),(5,104760000,64,'19 nguyen khac tinh','ThÃ¡i Cao','0964515577','DELIVERED','2025-05-25 14:31:55','COD','PAID'),(6,90190000,64,'19 nguyen khac tinh','ThÃ¡i Cao','0964515577','DELIVERED','2025-05-26 14:31:59','ONLINE','PAID'),(7,82170000,64,'19 nguyen khac tinh','ThÃ¡i Cao','0964515577','SHIPPED','2025-05-27 14:32:03','OCD','PENDING'),(8,46470000,64,'19 nguyen khac tinh','ThÃ¡i Cao','0964515577','CONFIRMED','2025-05-29 08:43:57','COD','UNPAID'),(17,74280000,64,'19 nguyen khac tinh','ThÃ¡i Cao','0964515577','PENDING','2025-05-29 11:17:47','COD','UNPAID'),(18,1000,63,'hkhk','hhhhhkhkh','0964515577','PENDING','2025-11-10 18:31:59','MOMO','UNPAID'),(19,1000,63,'hkhk','hhhhhkhkh','0964515577','PENDING','2025-11-10 18:43:34','MOMO','UNPAID'),(23,93960000,63,'hhhh','Cao Duy Thais','0964515511','PENDING','2025-11-16 14:54:44','COD','UNPAID'),(24,21399001,63,'dihocta','Cao Duy Thai','0964515533','PENDING','2025-11-16 15:43:22','COD','UNPAID'),(25,21399001,63,'chuan','Cao Duy Thai','0964515533','PENDING','2025-11-16 15:46:38','COD','UNPAID'),(26,26999000,63,'kakak','Cao Duy Thai','0964515588','PENDING','2025-11-16 15:49:05','MOMO','UNPAID'),(27,26999000,63,'khkha','Cao Duy Thai','0964515533','PENDING','2025-11-17 22:15:27','MOMO','UNPAID'),(28,11900000,63,'lplp','Cao Duy Thai','0964515514','PENDING','2025-11-17 22:21:22','MOMO','UNPAID'),(29,11900000,63,'kl','Cao Duy Thai','0964515544','PENDING','2025-11-19 21:20:00','MOMO','UNPAID'),(30,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 21:30:06','MOMO','UNPAID'),(31,11900000,63,'kl','Cao Duy Thai','0964515544','PENDING','2025-11-19 21:46:57','MOMO','UNPAID'),(32,11900000,63,'kl','Cao Duy Thai','0964515544','PENDING','2025-11-19 21:53:19','MOMO','UNPAID'),(33,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 21:58:49','MOMO','UNPAID'),(34,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 22:05:43','MOMO','UNPAID'),(35,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 22:12:45','MOMO','UNPAID'),(36,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 22:16:27','MOMO','UNPAID'),(37,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 22:20:28','MOMO','UNPAID'),(38,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 22:36:44','MOMO','UNPAID'),(39,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 22:39:47','MOMO','UNPAID'),(40,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 22:45:50','MOMO','UNPAID'),(41,11900000,63,'kl','Cao Duy ThÃ¡i','0964515544','PENDING','2025-11-19 22:49:30','MOMO','PAID'),(42,117768002,63,'478 nkh','Cao Duy Thai','0964515544','PENDING','2025-11-23 14:46:50','COD','UNPAID');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `detail_desc` mediumtext NOT NULL,
  `factory` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `quantity` bigint NOT NULL,
  `short_desc` varchar(255) NOT NULL,
  `sold` bigint NOT NULL,
  `target` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_NAME_PRODUCTS` (`name`),
  KEY `IX_FACTORY_PRODUCTS` (`factory`),
  CONSTRAINT `products_chk_1` CHECK ((`quantity` >= 1))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'ASUS TUF Gaming F15 FX506HF HN017W lÃ  chiáº¿c laptop gaming giÃ¡ ráº» nhÆ°ng vÃ´ cÃ¹ng máº¡nh máº½. KhÃ´ng chá»‰ bá»™ vi xá»­ lÃ½ Intel tháº¿ há»‡ thá»© 11, card Ä‘á»“ há»a RTX 20 series mÃ  Ä‘iá»ƒm máº¡nh cÃ²n Ä‘áº¿n tá»« viá»‡c trang bá»‹ sáºµn 16GB RAM, cho báº¡n hiá»‡u nÄƒng xuáº¥t sáº¯c mÃ  khÃ´ng cáº§n nÃ¢ng cáº¥p mÃ¡y.','ASUS','1711078092373-asus-01.png','Laptop Asus TUF Gaming',17489995,150,'Intel, Core i5, 11400H',72,'ASUS'),(2,'KhÃ¡m phÃ¡ sá»©c máº¡nh tá»‘i Æ°u tá»« Dell Inspiron 15 N3520, chiáº¿c laptop cÃ³ cáº¥u hÃ¬nh cá»±c máº¡nh vá»›i bá»™ vi xá»­ lÃ½ Intel Core i5 1235U tháº¿ há»‡ má»›i vÃ  dung lÆ°á»£ng RAM lÃªn tá»›i 16GB. Báº¡n cÃ³ thá»ƒ thoáº£i mÃ¡i xá»­ lÃ½ nhiá»u tÃ¡c vá»¥, nÃ¢ng cao nÄƒng suáº¥t trong cÃ´ng viá»‡c mÃ  khÃ´ng gáº·p báº¥t ká»³ trá»Ÿ ngáº¡i nÃ o.','DELL','1711078452562-dell-01.png','Laptop Dell Inspiron 15 ',15490000,194,'i5 1235U/16GB/512GB/15.6\"FHD',5,'SINHVIEN-VANPHONG'),(3,' Má»›i Ä‘Ã¢y, Lenovo Ä‘Ã£ tung ra thá»‹ trÆ°á»ng má»™t sáº£n pháº©m gaming tháº¿ há»‡ má»›i vá»›i hiá»‡u nÄƒng máº¡nh máº½, thiáº¿t káº¿ tá»‘i giáº£n, lá»‹ch lÃ£m phÃ¹ há»£p cho nhá»¯ng game thá»§ thÃ­ch sá»± Ä‘Æ¡n giáº£n. Táº£n nhiá»‡t mÃ¡t máº» vá»›i há»‡ thá»‘ng quáº¡t kÃ©p kiá»ƒm soÃ¡t Ä‘Æ°á»£c nhiá»‡t Ä‘á»™ mÃ¡y luÃ´n mÃ¡t máº» khi chÆ¡i game.','LENOVO','1711079073759-lenovo-01.png','Lenovo IdeaPad Gaming 3',19500000,148,' i5-10300H, RAM 8G',1,'GAMING'),(4,'Táº­n hÆ°á»Ÿng cáº£m giÃ¡c mÃ¡t láº¡nh sÃ nh Ä‘iá»‡u vá»›i thiáº¿t káº¿ kim loáº¡i\r\nÄÆ°á»£c thiáº¿t káº¿ Ä‘á»ƒ Ä‘Ã¡p á»©ng nhá»¯ng nhu cáº§u Ä‘iá»‡n toÃ¡n hÃ ng ngÃ y cá»§a báº¡n, dÃ²ng mÃ¡y tÃ­nh xÃ¡ch tay ASUS K Series sá»Ÿ há»¯u thiáº¿t káº¿ tá»‘i giáº£n, gá»n nháº¹ vÃ  cá»±c má»ng vá»›i má»™t lá»›p vá» há»a tiáº¿t vÃ¢n kim loáº¡i phong cÃ¡ch. Hiá»‡u nÄƒng cá»§a mÃ¡y ráº¥t máº¡nh máº½ nhá» trang bá»‹ bá»™ vi xá»­ lÃ½ IntelÂ® Coreâ„¢ i7 processor vÃ  Ä‘á»“ há»a má»›i nháº¥t. BÃªn cáº¡nh Ä‘Ã³, cÃ¡c cÃ´ng nghá»‡ sÃ¡ng táº¡o Ä‘á»™c quyá»n cá»§a ASUS Ä‘Æ°a thiáº¿t bá»‹ lÃªn Ä‘áº³ng cáº¥p má»›i, cho báº¡n má»™t tráº£i nghiá»‡m ngÆ°á»i dÃ¹ng trá»±c quan vÃ  tÃ­nh nÄƒng cÃ´ng thÃ¡i há»c vÆ°á»£t trá»™i.','ASUS','1711079496409-asus-02.png','Asus K501UX',11900000,91,'VGA NVIDIA GTX 950M- 4G',16,'THIET-KE-DO-HOA'),(5,'Chiáº¿c MacBook Air cÃ³ hiá»‡u nÄƒng Ä‘á»™t phÃ¡ nháº¥t tá»« trÆ°á»›c Ä‘áº¿n nay Ä‘Ã£ xuáº¥t hiá»‡n. Bá»™ vi xá»­ lÃ½ Apple M1 hoÃ n toÃ n má»›i Ä‘Æ°a sá»©c máº¡nh cá»§a MacBook Air M1 13 inch 2020 vÆ°á»£t xa khá»i mong Ä‘á»£i ngÆ°á»i dÃ¹ng, cÃ³ thá»ƒ cháº¡y Ä‘Æ°á»£c nhá»¯ng tÃ¡c vá»¥ náº·ng vÃ  thá»i lÆ°á»£ng pin Ä‘Ã¡ng kinh ngáº¡c.','APPLE','1711079954090-apple-01.png','MacBook Air 13',17690000,99,'Apple M1 GPU 7 nhÃ¢n',0,'GAMING'),(6,'14.0 ChÃ­nh: inch, 2880 x 1800 Pixels, OLED, 90 Hz, OLED','LG','1711080386941-lg-01.png','Laptop LG Gram Style',31490000,99,'Intel Iris Plus Graphics',2,'DOANH-NHAN'),(7,'KhÃ´ng chá»‰ khÆ¡i gá»£i cáº£m há»©ng qua viá»‡c cÃ¡ch tÃ¢n thiáº¿t káº¿, MacBook Air M2 2022 cÃ²n chá»©a Ä‘á»±ng nguá»“n sá»©c máº¡nh lá»›n lao vá»›i chip M2 siÃªu máº¡nh, thá»i lÆ°á»£ng pin cháº¡m  ngÆ°á»¡ng 18 giá», mÃ n hÃ¬nh Liquid Retina tuyá»‡t Ä‘áº¹p vÃ  há»‡ thá»‘ng camera káº¿t há»£p cÃ¹ng Ã¢m thanh tÃ¢n tiáº¿n.','APPLE','1711080787179-apple-02.png','MacBook Air 13 ',24990000,97,' Apple M2 GPU 8 nhÃ¢n',3,'MONG-NHE'),(8,'LÃ  chiáº¿c laptop gaming tháº¿ há»‡ má»›i nháº¥t thuá»™c dÃ²ng Nitro 5 luÃ´n chiáº¿m Ä‘Æ°á»£c ráº¥t nhiá»u cáº£m tÃ¬nh cá»§a game thá»§ trÆ°á»›c Ä‘Ã¢y, Acer Nitro Gaming AN515-58-769J nay cÃ²n áº¥n tÆ°á»£ng hÆ¡n ná»¯a vá»›i bá»™ vi xá»­ lÃ½ Intel Core i7 12700H cá»±c khá»§ng vÃ  card Ä‘á»“ há»a RTX 3050, sáºµn sÃ ng cÃ¹ng báº¡n chinh phá»¥c nhá»¯ng Ä‘á»‰nh cao.\r\n','ACER','1711080948771-acer-01.png','Laptop Acer Nitro ',23490000,99,'AN515-58-769J i7 12700H',10,'SINHVIEN-VANPHONG'),(9,'15.6 inch, FHD (1920 x 1080), IPS, 144 Hz, 250 nits, Acer ComfyView LED-backlit','ASUS','1711081080930-asus-03.png','Laptop Acer Nitro V',26999000,99,' NVIDIA GeForce RTX 4050',2,'MONG-NHE'),(10,'Dell Inspiron N3520 lÃ  chiáº¿c laptop lÃ½ tÆ°á»Ÿng cho cÃ´ng viá»‡c hÃ ng ngÃ y. Bá»™ vi xá»­ lÃ½ Intel Core i5 tháº¿ há»‡ thá»© 12 hiá»‡u suáº¥t cao, mÃ n hÃ¬nh lá»›n 15,6 inch Full HD 120Hz mÆ°á»£t mÃ , thiáº¿t káº¿ bá»n bá»‰ sáº½ giÃºp báº¡n giáº£i quyáº¿t cÃ´ng viá»‡c nhanh chÃ³ng má»i lÃºc má»i nÆ¡i.','DELL','1720254996683-1711081278418-dell-02.png','Laptop Dell Latitude 3420',21399001,99,' Intel Iris Xe Graphics',4,'MONG-NHE');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Admin thÃ¬ full quyá»n','ADMIN'),(2,'User thÃ´ng thÆ°á»ng','USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_otp`
--

DROP TABLE IF EXISTS `user_otp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_otp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `otp_code` varchar(6) NOT NULL,
  `expiry_time` timestamp NOT NULL,
  `is_used` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_otp_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_otp`
--

LOCK TABLES `user_otp` WRITE;
/*!40000 ALTER TABLE `user_otp` DISABLE KEYS */;
INSERT INTO `user_otp` VALUES (1,63,'MJE4CU','2025-05-19 09:37:36',0,'2025-05-19 09:36:36'),(2,63,'BTQZOJ','2025-05-19 09:41:27',0,'2025-05-19 09:40:27'),(3,63,'LCNWWL','2025-05-19 10:48:08',0,'2025-05-19 10:47:08'),(4,63,'AP2TFO','2025-05-19 14:49:41',0,'2025-05-19 14:48:41'),(5,63,'UTSVLL','2025-05-19 14:53:29',0,'2025-05-19 14:52:29'),(6,63,'9OFUFH','2025-05-19 14:55:58',1,'2025-05-19 14:54:58'),(7,63,'2CTDXT','2025-05-19 15:00:18',1,'2025-05-19 14:59:18'),(8,63,'OO4MKY','2025-05-19 20:24:58',1,'2025-05-19 20:23:58'),(9,63,'4H78RX','2025-05-20 14:14:55',1,'2025-05-20 14:13:55'),(10,63,'9QXPBJ','2025-05-24 15:54:48',0,'2025-05-24 15:53:48'),(11,63,'XL8QYF','2025-05-24 16:06:01',1,'2025-05-24 16:05:01'),(12,63,'CGANAM','2025-05-24 16:08:22',1,'2025-05-24 16:07:22'),(13,55,'WXB8IY','2025-07-23 21:46:42',0,'2025-07-23 21:45:42'),(14,63,'82GAGC','2025-11-10 17:03:02',0,'2025-11-10 17:02:02'),(15,63,'QX9PUB','2025-11-19 20:50:35',0,'2025-11-19 20:49:35'),(16,63,'OPHBFK','2025-11-19 20:53:56',0,'2025-11-19 20:52:56'),(17,63,'YBH91G','2025-11-19 21:05:32',0,'2025-11-19 21:04:32'),(18,63,'UT21RM','2025-11-19 21:08:53',1,'2025-11-19 21:07:53');
/*!40000 ALTER TABLE `user_otp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `face_data` longtext,
  PRIMARY KEY (`id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  KEY `IX_FULLNAME_ID_USERNAME` (`full_name`,`id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (55,'11111','1753283073130-30ff80149f0c3b52621d.jpg','admin1@gmail.com','admin1','$2a$10$fWiRoRqmitOi31ES.xIY0u86Wedqui/wL6iC593htXee8wkl8b8Oi','0964515577',1,NULL),(56,'22222','1721385098799-roaring-lion-4k-3840Ã—2160.jpg','user1@gmail.com','user1','$2a$10$fWiRoRqmitOi31ES.xIY0u86Wedqui/wL6iC593htXee8wkl8b8Oi','',2,NULL),(57,NULL,'default-avatar.jpg','abc@gmail.com','cao thai','$2a$10$4cb4cMbhzAFF5n3zmD02ueKpJe15QM2wnGPliSK.ozoqHAW1cjejW',NULL,2,NULL),(62,NULL,'default-avatar.jpg','3tedutech@gmail.com','3TEduTech',NULL,NULL,2,NULL),(63,'kl','1763544345872-avatar.jpg','caothaiiiop@gmail.com','Cao Duy ThÃ¡i','$2a$10$FV0mjntJlzV25eDirt1.V.K0k37CmGwxJcPqToQhXE4lbEaxUBOyO','0964515544',2,NULL),(64,NULL,'default-avatar.jpg','caothaiiop1234@gmail.com','ThÃ¡i Cao',NULL,NULL,2,NULL),(65,'11111 nguyen khac tinh','1748250357425-Snaptik.app_728305061920258790522.jpg','admin2@gmail.com','Cao Duy Thais','$2a$10$akuFub7gASupRMnpGleeruQlVwnVzyX4YV0WO2WJtikdtvNqfmAUO','0964515578',1,NULL),(66,NULL,'default-avatar.jpg','androidTest@Gmail.com','android test','$2a$10$qAvhn2WvyKkaFZQGCwcQi.76Lac8Tn.ZRcJNPR/7wu3IjMbnHe9w.',NULL,2,NULL),(67,NULL,NULL,'manhquan@gmail.com','manh quan','$2a$10$uIqcEAgjz6nZ1IA4M4GUv.ifl/Qtbqc/MFxm4n0NN5w8oOEs.Nzum',NULL,2,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-24 22:39:29
