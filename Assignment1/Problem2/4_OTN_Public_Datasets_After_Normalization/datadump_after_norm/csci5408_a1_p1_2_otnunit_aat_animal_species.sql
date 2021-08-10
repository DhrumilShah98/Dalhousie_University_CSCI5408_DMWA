-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: csci5408_a1_p1_2
-- ------------------------------------------------------
-- Server version	5.7.23

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
-- Table structure for table `otnunit_aat_animal_species`
--

DROP TABLE IF EXISTS `otnunit_aat_animal_species`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otnunit_aat_animal_species` (
  `vernacularname` varchar(255) NOT NULL,
  `scientificname` text,
  `aphiaid` int(11) DEFAULT NULL,
  `tsn` int(11) DEFAULT NULL,
  PRIMARY KEY (`vernacularname`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otnunit_aat_animal_species`
--

LOCK TABLES `otnunit_aat_animal_species` WRITE;
/*!40000 ALTER TABLE `otnunit_aat_animal_species` DISABLE KEYS */;
INSERT INTO `otnunit_aat_animal_species` VALUES ('Arctic char','Salvelinus alpinus',127188,162001),('Atlantic salmon','Salmo salar',127186,161996),('blue shark','Prionace glauca',105801,160424),('leervis','Lichia amia',126810,168769),('spotted grunter','Pomadasys commersonnii',218563,630243),('white shark','Carcharodon carcharias',105838,159903),('smooth hound shark','Mustelus mustelus',105822,160242),('sevengill shark','Notorynchus cepedianus',217628,159829),('Zambezi shark','Carcharhinus leucas',105792,160275),('tiger shark','Galeocerdo cuvier',105799,160189),('Chinook salmon','Oncorhynchus tshawytscha',158075,161980),('Dungeness crab','Cancer magister',452271,98675),('English sole','Parophrys vetulus',254393,172921),('Steelhead salmon','Oncorhynchus mykiss',127185,161989);
/*!40000 ALTER TABLE `otnunit_aat_animal_species` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-02  7:44:24
