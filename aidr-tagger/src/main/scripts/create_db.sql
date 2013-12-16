
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table active_attributes
# ------------------------------------------------------------

DROP VIEW IF EXISTS `active_attributes`;

CREATE TABLE `active_attributes` (
   `crisisID` INT(10) UNSIGNED NOT NULL,
   `attributeInfo` VARCHAR(358) NULL DEFAULT NULL
) ENGINE=MyISAM;



# Dump of table active_attributes_inner
# ------------------------------------------------------------

DROP VIEW IF EXISTS `active_attributes_inner`;

CREATE TABLE `active_attributes_inner` (
   `crisisID` INT(10) UNSIGNED NOT NULL,
   `nominalAttributeID` INT(10) UNSIGNED NOT NULL,
   `name` VARCHAR(140) NOT NULL,
   `description` VARCHAR(600) NOT NULL DEFAULT '',
   `labels` VARCHAR(352) NULL DEFAULT NULL
) ENGINE=MyISAM;



# Dump of table crisis
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crisis`;

CREATE TABLE `crisis` (
  `crisisID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(140) NOT NULL DEFAULT 'unnamed crisis',
  `crisisTypeID` int(10) unsigned NOT NULL,
  `code` varchar(64) NOT NULL DEFAULT '',
  `userID` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`crisisID`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `crisisTypeID_idx` (`crisisTypeID`),
  KEY `fk_crisis_users_userID_idx` (`userID`),
  CONSTRAINT `fk_Crisis_CrisisType_crisisTypeID` FOREIGN KEY (`crisisTypeID`) REFERENCES `crisis_type` (`crisisTypeID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_crisis_users_userID` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table crisis_nominal_attribute
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crisis_nominal_attribute`;

CREATE TABLE `crisis_nominal_attribute` (
  `crisisID` int(11) unsigned NOT NULL,
  `nominalAttributeID` int(11) unsigned NOT NULL,
  PRIMARY KEY (`crisisID`,`nominalAttributeID`),
  KEY `crisisID_idx` (`crisisID`),
  KEY `nominalAttributeID_idx` (`nominalAttributeID`),
  CONSTRAINT `fk_Crisis_NominalAttribute_crisisID` FOREIGN KEY (`crisisID`) REFERENCES `crisis` (`crisisID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Crisis_NominalAttribute_nominalAttributeID` FOREIGN KEY (`nominalAttributeID`) REFERENCES `nominal_attribute` (`nominalAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table crisis_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crisis_type`;

CREATE TABLE `crisis_type` (
  `crisisTypeID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(140) NOT NULL DEFAULT 'default type',
  PRIMARY KEY (`crisisTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table document
# ------------------------------------------------------------

DROP TABLE IF EXISTS `document`;

CREATE TABLE `document` (
  `documentID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `crisisID` int(10) unsigned NOT NULL,
  `isEvaluationSet` bit(1) NOT NULL DEFAULT b'0',
  `hasHumanLabels` bit(1) NOT NULL DEFAULT b'0',
  `sourceIP` int(10) unsigned NOT NULL,
  `valueAsTrainingSample` double unsigned NOT NULL DEFAULT '0',
  `receivedAt` datetime NOT NULL,
  `language` varchar(5) NOT NULL DEFAULT 'en',
  `doctype` varchar(20) NOT NULL,
  `data` text NOT NULL,
  `wordFeatures` text,
  `geoFeatures` text,
  PRIMARY KEY (`documentID`),
  KEY `fk_Document_Crisis_crisisID_idx` (`crisisID`),
  KEY `isEvaluationSet` (`isEvaluationSet`),
  CONSTRAINT `fk_Document_Crisis_crisisID` FOREIGN KEY (`crisisID`) REFERENCES `crisis` (`crisisID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `document_BINS` BEFORE INSERT ON `document` FOR EACH ROW set new.isEvaluationSet = mod((SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='document'), 5)=0 */;;
DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;


# Dump of table document_nominal_label
# ------------------------------------------------------------

DROP TABLE IF EXISTS `document_nominal_label`;

CREATE TABLE `document_nominal_label` (
  `documentID` bigint(20) unsigned NOT NULL,
  `nominalLabelID` int(10) unsigned NOT NULL,
  `timestamp` datetime NOT NULL,
  `taskID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`documentID`,`nominalLabelID`),
  UNIQUE KEY `taskID_UNIQUE` (`taskID`),
  KEY `fk_Document_NominalLabel_documentID_idx` (`documentID`),
  KEY `fk_document_nominal_label_nominal_label_idx` (`nominalLabelID`),
  CONSTRAINT `fk_Document_NominalLabel_documentID` FOREIGN KEY (`documentID`) REFERENCES `document` (`documentID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_document_nominal_label_nominal_label` FOREIGN KEY (`nominalLabelID`) REFERENCES `nominal_label` (`nominalLabelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table model
# ------------------------------------------------------------

DROP TABLE IF EXISTS `model`;

CREATE TABLE `model` (
  `modelID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `modelFamilyID` int(10) unsigned NOT NULL,
  `avgPrecision` double unsigned NOT NULL,
  `avgRecall` double unsigned NOT NULL,
  `avgAuc` double unsigned NOT NULL,
  `trainingCount` int(10) unsigned NOT NULL,
  `trainingTime` datetime NOT NULL,
  `isCurrentModel` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`modelID`),
  KEY `fk_Model_ModelFamily_modelFamilyID_idx` (`modelFamilyID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table model_family
# ------------------------------------------------------------

DROP TABLE IF EXISTS `model_family`;

CREATE TABLE `model_family` (
  `modelFamilyID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `crisisID` int(10) unsigned NOT NULL,
  `nominalAttributeID` int(10) unsigned NOT NULL,
  `isActive` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`modelFamilyID`),
  UNIQUE KEY `unique` (`crisisID`,`nominalAttributeID`),
  KEY `fk_ModelFamily_Crisis_idx` (`crisisID`),
  KEY `fk_ModelFamily_NominalAttribute_idx` (`nominalAttributeID`),
  CONSTRAINT `fk_ModelFamily_Crisis` FOREIGN KEY (`crisisID`) REFERENCES `crisis` (`crisisID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ModelFamily_NominalAttribute` FOREIGN KEY (`nominalAttributeID`) REFERENCES `nominal_attribute` (`nominalAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table model_nominal_label
# ------------------------------------------------------------

DROP TABLE IF EXISTS `model_nominal_label`;

CREATE TABLE `model_nominal_label` (
  `modelID` int(10) unsigned NOT NULL,
  `nominalLabelID` int(10) unsigned NOT NULL,
  `labelPrecision` double DEFAULT NULL,
  `labelRecall` double DEFAULT NULL,
  `labelAuc` double DEFAULT NULL,
  `classifiedDocumentCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`modelID`,`nominalLabelID`),
  KEY `fk_model_label_modelID_idx` (`modelID`),
  KEY `fk_model_nominal_label_nominal_label_idx` (`nominalLabelID`),
  CONSTRAINT `fk_model_nominal_label_nominal_label` FOREIGN KEY (`nominalLabelID`) REFERENCES `nominal_label` (`nominalLabelID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_model_modellabel_modelID` FOREIGN KEY (`modelID`) REFERENCES `model` (`modelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table nominal_attribute
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nominal_attribute`;

CREATE TABLE `nominal_attribute` (
  `nominalAttributeID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userID` int(10) unsigned NOT NULL DEFAULT '1',
  `name` varchar(140) NOT NULL,
  `description` varchar(600) NOT NULL DEFAULT '',
  `code` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`nominalAttributeID`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_nominalAttribute_users_userID_idx` (`userID`),
  CONSTRAINT `fk_nominalAttribute_users_userID` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table nominal_label
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nominal_label`;

CREATE TABLE `nominal_label` (
  `nominalLabelID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nominalLabelCode` varchar(64) NOT NULL DEFAULT '',
  `nominalAttributeID` int(10) unsigned NOT NULL,
  `name` varchar(140) NOT NULL DEFAULT '',
  `description` varchar(600) NOT NULL DEFAULT '',
  PRIMARY KEY (`nominalLabelID`),
  KEY `asd_idx` (`nominalAttributeID`),
  CONSTRAINT `fk_NominalLabel_NominalAttribute_nominalAttributeID` FOREIGN KEY (`nominalAttributeID`) REFERENCES `nominal_attribute` (`nominalAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table nominal_label_evaluation_data
# ------------------------------------------------------------

DROP VIEW IF EXISTS `nominal_label_evaluation_data`;

CREATE TABLE `nominal_label_evaluation_data` (
   `documentID` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
   `crisisID` INT(10) UNSIGNED NOT NULL,
   `nominalLabelID` INT(10) UNSIGNED NOT NULL,
   `nominalAttributeID` INT(10) UNSIGNED NOT NULL,
   `wordFeatures` TEXT NULL DEFAULT NULL
) ENGINE=MyISAM;



# Dump of table nominal_label_training_data
# ------------------------------------------------------------

DROP VIEW IF EXISTS `nominal_label_training_data`;

CREATE TABLE `nominal_label_training_data` (
   `documentID` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
   `crisisID` INT(10) UNSIGNED NOT NULL,
   `nominalLabelID` INT(10) UNSIGNED NOT NULL,
   `nominalAttributeID` INT(10) UNSIGNED NOT NULL,
   `wordFeatures` TEXT NULL DEFAULT NULL
) ENGINE=MyISAM;



# Dump of table task_answer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `task_answer`;

CREATE TABLE `task_answer` (
  `documentID` bigint(20) unsigned NOT NULL,
  `userID` int(10) unsigned NOT NULL,
  `answer` text NOT NULL,
  `timestamp` datetime NOT NULL,
  `fromTrustedUser` bit(1) NOT NULL DEFAULT b'0',
  `taskID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`taskID`,`documentID`,`userID`),
  UNIQUE KEY `taskID_UNIQUE` (`taskID`),
  KEY `fk_TaskAnswer_Document_idx` (`documentID`),
  CONSTRAINT `fk_TaskAnswer_Document` FOREIGN KEY (`documentID`) REFERENCES `document` (`documentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table task_assignment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `task_assignment`;

CREATE TABLE `task_assignment` (
  `documentID` bigint(20) unsigned NOT NULL,
  `userID` int(10) unsigned NOT NULL,
  `assignedAt` datetime NOT NULL,
  PRIMARY KEY (`documentID`,`userID`),
  KEY `fk_TaskAssignment_Document_idx` (`documentID`),
  CONSTRAINT `fk_TaskAssignment_Document` FOREIGN KEY (`documentID`) REFERENCES `document` (`documentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table task_buffer
# ------------------------------------------------------------

DROP VIEW IF EXISTS `task_buffer`;

CREATE TABLE `task_buffer` (
   `documentID` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
   `crisisID` INT(10) UNSIGNED NOT NULL,
   `attributeInfo` VARCHAR(358) NULL DEFAULT NULL,
   `language` VARCHAR(5) NOT NULL DEFAULT 'en',
   `doctype` VARCHAR(20) NOT NULL,
   `data` TEXT NOT NULL,
   `valueAsTrainingSample` DOUBLE UNSIGNED NOT NULL DEFAULT '0',
   `assignedCount` DECIMAL(23) NULL DEFAULT NULL
) ENGINE=MyISAM;



# Dump of table users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `userID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL DEFAULT 'normal',
  PRIMARY KEY (`userID`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;





# Replace placeholder table for task_buffer with correct view syntax
# ------------------------------------------------------------

DROP TABLE `task_buffer`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `task_buffer`
AS SELECT
   `d`.`documentID` AS `documentID`,
   `d`.`crisisID` AS `crisisID`,
   `active_attributes`.`attributeInfo` AS `attributeInfo`,
   `d`.`language` AS `language`,
   `d`.`doctype` AS `doctype`,
   `d`.`data` AS `data`,
   `d`.`valueAsTrainingSample` AS `valueAsTrainingSample`,sum((`asg`.`documentID` is not null)) AS `assignedCount`
FROM ((`document` `d` join `active_attributes` on((`d`.`crisisID` = `active_attributes`.`crisisID`))) left join `task_assignment` `asg` on((`asg`.`documentID` = `d`.`documentID`))) where (not(`d`.`hasHumanLabels`)) group by `d`.`documentID` order by `d`.`crisisID`,`d`.`valueAsTrainingSample` desc,`d`.`documentID` desc;


# Replace placeholder table for nominal_label_evaluation_data with correct view syntax
# ------------------------------------------------------------

DROP TABLE `nominal_label_evaluation_data`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `nominal_label_evaluation_data`
AS SELECT
   `d`.`documentID` AS `documentID`,
   `d`.`crisisID` AS `crisisID`,
   `dnl`.`nominalLabelID` AS `nominalLabelID`,
   `nl`.`nominalAttributeID` AS `nominalAttributeID`,
   `d`.`wordFeatures` AS `wordFeatures`
FROM ((`document` `d` join `document_nominal_label` `dnl` on((`d`.`documentID` = `dnl`.`documentID`))) join `nominal_label` `nl` on((`nl`.`nominalLabelID` = `dnl`.`nominalLabelID`))) where (`d`.`isEvaluationSet` and (`d`.`wordFeatures` is not null));


# Replace placeholder table for active_attributes with correct view syntax
# ------------------------------------------------------------

DROP TABLE `active_attributes`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `active_attributes`
AS SELECT
   `active_attributes_inner`.`crisisID` AS `crisisID`,concat('{"attributes":[',group_concat(concat('{"id":',`active_attributes_inner`.`nominalAttributeID`,',"name":"',`active_attributes_inner`.`name`,'","description":"',`active_attributes_inner`.`description`,'",',`active_attributes_inner`.`labels`,'}') separator ','),']}') AS `attributeInfo`
FROM `active_attributes_inner` group by `active_attributes_inner`.`crisisID`;


# Replace placeholder table for active_attributes_inner with correct view syntax
# ------------------------------------------------------------

DROP TABLE `active_attributes_inner`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `active_attributes_inner`
AS SELECT
   `m`.`crisisID` AS `crisisID`,
   `m`.`nominalAttributeID` AS `nominalAttributeID`,
   `a`.`name` AS `name`,
   `a`.`description` AS `description`,concat('"labels":[',group_concat(concat('{"name":"',`l`.`name`,'","description":"',`l`.`description`,'","code":"',`l`.`nominalLabelCode`,'","id":',`l`.`nominalLabelID`,'}') order by (`l`.`nominalLabelCode` = 'null') DESC,`l`.`name` ASC separator ','),']') AS `labels`
FROM ((`model_family` `m` join `nominal_attribute` `a` on((`a`.`nominalAttributeID` = `m`.`nominalAttributeID`))) join `nominal_label` `l` on((`l`.`nominalAttributeID` = `m`.`nominalAttributeID`))) where `m`.`isActive` group by `m`.`crisisID`,`m`.`nominalAttributeID`;


# Replace placeholder table for nominal_label_training_data with correct view syntax
# ------------------------------------------------------------

DROP TABLE `nominal_label_training_data`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `nominal_label_training_data`
AS SELECT
   `d`.`documentID` AS `documentID`,
   `d`.`crisisID` AS `crisisID`,
   `dnl`.`nominalLabelID` AS `nominalLabelID`,
   `nl`.`nominalAttributeID` AS `nominalAttributeID`,
   `d`.`wordFeatures` AS `wordFeatures`
FROM ((`document` `d` join `document_nominal_label` `dnl` on((`d`.`documentID` = `dnl`.`documentID`))) join `nominal_label` `nl` on((`nl`.`nominalLabelID` = `dnl`.`nominalLabelID`))) where ((not(`d`.`isEvaluationSet`)) and (`d`.`wordFeatures` is not null));

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
