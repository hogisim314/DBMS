-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: videosystem
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `construct`
--

DROP TABLE IF EXISTS `construct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `construct` (
  `c_listID` int NOT NULL,
  `c_videoID` int NOT NULL,
  PRIMARY KEY (`c_listID`,`c_videoID`),
  KEY `construct_ibfk_2` (`c_videoID`),
  CONSTRAINT `construct_ibfk_1` FOREIGN KEY (`c_listID`) REFERENCES `playlist` (`listID`),
  CONSTRAINT `construct_ibfk_2` FOREIGN KEY (`c_videoID`) REFERENCES `video` (`videoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `construct`
--

LOCK TABLES `construct` WRITE;
/*!40000 ALTER TABLE `construct` DISABLE KEYS */;
INSERT INTO `construct` VALUES (1,101),(1001,101),(2,102),(3,103),(1000,103),(1,104),(1000,104),(4,105),(3,106),(5,107),(3,108);
/*!40000 ALTER TABLE `construct` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manager` (
  `managerID` int NOT NULL,
  `numOfUser` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `numOfVideo` int NOT NULL,
  PRIMARY KEY (`managerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` VALUES (0,7,'giho',8);
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `playlist` (
  `listID` int NOT NULL,
  `listName` varchar(50) NOT NULL,
  `numOfVideo` int DEFAULT '0',
  `CreaterID` int NOT NULL,
  `mgrID` int NOT NULL,
  PRIMARY KEY (`listID`),
  KEY `playlist_ibfk_1` (`CreaterID`),
  KEY `playlist_ibfk_2` (`mgrID`),
  CONSTRAINT `playlist_ibfk_1` FOREIGN KEY (`CreaterID`) REFERENCES `user` (`userID`),
  CONSTRAINT `playlist_ibfk_2` FOREIGN KEY (`mgrID`) REFERENCES `manager` (`managerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
INSERT INTO `playlist` VALUES (1,'Alice013playlist',2,1,0),(2,'Elizabeth1playlist',1,2,0),(3,'Michael23playlist',3,3,0),(4,'Alice35playlist',1,4,0),(5,'Joy62playlist',1,5,0),(1000,'Alice_pl1',2,1,0),(1001,'Joy_pl1',1,5,0);
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userID` int NOT NULL,
  `ID` varchar(15) NOT NULL,
  `password` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `alias` varchar(50) DEFAULT NULL,
  `phoneNum` int DEFAULT NULL,
  `mngerID` int NOT NULL,
  PRIMARY KEY (`userID`),
  KEY `user_ibfk_1` (`mngerID`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`mngerID`) REFERENCES `manager` (`managerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Alice013',1536,'Alice','key',1039502938,0),(2,'Elizabeth1',2347,'Elizabeth','samsung',1029583929,0),(3,'Michael23',2364,'Michael','lg',1057384948,0),(4,'Alice35',8492,'Alice','sk',1087492838,0),(5,'Joy62',9393,'Joy','posco',1028349392,0),(6,'Theodore32',9293,'Theodore','hyundai',1038572939,0),(7,'Abner135',1827,'Abner','eco',1028475849,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video` (
  `videoTitle` varchar(50) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `videoID` int NOT NULL,
  `playtime` int NOT NULL,
  `views` int NOT NULL,
  `upload_date` date NOT NULL,
  `ownerID` int NOT NULL,
  `videomgrID` int NOT NULL,
  PRIMARY KEY (`videoID`),
  KEY `video_ibfk_1` (`ownerID`),
  KEY `video_ibfk_2` (`videomgrID`),
  CONSTRAINT `video_ibfk_1` FOREIGN KEY (`ownerID`) REFERENCES `user` (`userID`),
  CONSTRAINT `video_ibfk_2` FOREIGN KEY (`videomgrID`) REFERENCES `manager` (`managerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES ('LOL','GAME',101,60,23,'2018-12-01',1,0),('AVARTAR','ACTION',102,120,3,'2022-12-03',2,0),('CSE','STUDY',103,103,613,'2019-03-02',3,0),('CSE','ENGINEERING',104,100,23,'2015-02-21',1,0),('TOPGUN_ACTION_SCENES','ACTION',105,130,5102,'2022-08-01',4,0),('TOPGUN_ACTION_SCENES','ACTION',106,110,55602,'2021-03-01',3,0),('CHRISTMAS','SONG',107,13,107,'2021-12-25',5,0),('CHRISTMAS','PARTY',108,15,1307,'2019-12-25',3,0);
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watch`
--

DROP TABLE IF EXISTS `watch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `watch` (
  `w_userID` int NOT NULL,
  `w_videoID` int NOT NULL,
  `watch_num` int NOT NULL,
  PRIMARY KEY (`w_userID`,`w_videoID`),
  KEY `watch_ibfk_2` (`w_videoID`),
  CONSTRAINT `watch_ibfk_1` FOREIGN KEY (`w_userID`) REFERENCES `user` (`userID`),
  CONSTRAINT `watch_ibfk_2` FOREIGN KEY (`w_videoID`) REFERENCES `video` (`videoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watch`
--

LOCK TABLES `watch` WRITE;
/*!40000 ALTER TABLE `watch` DISABLE KEYS */;
INSERT INTO `watch` VALUES (1,105,2),(4,103,3);
/*!40000 ALTER TABLE `watch` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-06  6:48:16
