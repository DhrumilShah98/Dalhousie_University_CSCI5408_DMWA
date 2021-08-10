-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 35.239.92.109    Database: data5408_vm2
-- ------------------------------------------------------
-- Server version	5.7.33-0ubuntu0.16.04.1

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
-- Table structure for table `product_category_name_translation`
--

DROP TABLE IF EXISTS `product_category_name_translation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_category_name_translation` (
  `product_category_name` varchar(255) NOT NULL,
  `product_category_name_english` text NOT NULL,
  PRIMARY KEY (`product_category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category_name_translation`
--

LOCK TABLES `product_category_name_translation` WRITE;
/*!40000 ALTER TABLE `product_category_name_translation` DISABLE KEYS */;
INSERT INTO `product_category_name_translation` VALUES ('agro_industria_e_comercio','agro_industry_and_commerce'),('alimentos','food'),('alimentos_bebidas','food_drink'),('artes','art'),('artes_e_artesanato','arts_and_craftmanship'),('artigos_de_festas','party_supplies'),('artigos_de_natal','christmas_supplies'),('audio','audio'),('automotivo','auto'),('bebes','baby'),('bebidas','drinks'),('beleza_saude','health_beauty'),('brinquedos','toys'),('cama_mesa_banho','bed_bath_table'),('casa_conforto','home_confort'),('casa_conforto_2','home_comfort_2'),('casa_construcao','home_construction'),('cds_dvds_musicais','cds_dvds_musicals'),('cine_foto','cine_photo'),('climatizacao','air_conditioning'),('consoles_games','consoles_games'),('construcao_ferramentas_construcao','construction_tools_construction'),('construcao_ferramentas_ferramentas','costruction_tools_tools'),('construcao_ferramentas_iluminacao','construction_tools_lights'),('construcao_ferramentas_jardim','costruction_tools_garden'),('construcao_ferramentas_seguranca','construction_tools_safety'),('cool_stuff','cool_stuff'),('dvds_blu_ray','dvds_blu_ray'),('eletrodomesticos','home_appliances'),('eletrodomesticos_2','home_appliances_2'),('eletronicos','electronics'),('eletroportateis','small_appliances'),('esporte_lazer','sports_leisure'),('fashion_bolsas_e_acessorios','fashion_bags_accessories'),('fashion_calcados','fashion_shoes'),('fashion_esporte','fashion_sport'),('fashion_roupa_feminina','fashio_female_clothing'),('fashion_roupa_infanto_juvenil','fashion_childrens_clothes'),('fashion_roupa_masculina','fashion_male_clothing'),('fashion_underwear_e_moda_praia','fashion_underwear_beach'),('ferramentas_jardim','garden_tools'),('flores','flowers'),('fraldas_higiene','diapers_and_hygiene'),('industria_comercio_e_negocios','industry_commerce_and_business'),('informatica_acessorios','computers_accessories'),('instrumentos_musicais','musical_instruments'),('la_cuisine','la_cuisine'),('livros_importados','books_imported'),('livros_interesse_geral','books_general_interest'),('livros_tecnicos','books_technical'),('malas_acessorios','luggage_accessories'),('market_place','market_place'),('moveis_colchao_e_estofado','furniture_mattress_and_upholstery'),('moveis_cozinha_area_de_servico_jantar_e_jardim','kitchen_dining_laundry_garden_furniture'),('moveis_decoracao','furniture_decor'),('moveis_escritorio','office_furniture'),('moveis_quarto','furniture_bedroom'),('moveis_sala','furniture_living_room'),('musica','music'),('papelaria','stationery'),('pcs','computers'),('perfumaria','perfumery'),('pet_shop','pet_shop'),('portateis_casa_forno_e_cafe','small_appliances_home_oven_and_coffee'),('relogios_presentes','watches_gifts'),('seguros_e_servicos','security_and_services'),('sinalizacao_e_seguranca','signaling_and_security'),('tablets_impressao_imagem','tablets_printing_image'),('telefonia','telephony'),('telefonia_fixa','fixed_telephony'),('utilidades_domesticas','housewares');
/*!40000 ALTER TABLE `product_category_name_translation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-20  5:52:41
