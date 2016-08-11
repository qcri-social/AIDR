CREATE DATABASE  IF NOT EXISTS `aidr_predict`;
USE `aidr_predict`;

--
-- Table structure for table `UserConnection`
--
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `UserConnection`;
CREATE TABLE `UserConnection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accessToken` varchar(255) DEFAULT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(255) DEFAULT NULL,
  `providerId` varchar(255) DEFAULT NULL,
  `providerUserId` varchar(255) DEFAULT NULL,
  `rank` int(11) NOT NULL,
  `refreshToken` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `userId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_key` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `locale` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `provider` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `user_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `download_permitted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `account_activity`
--

DROP TABLE IF EXISTS `account_activity`;
CREATE TABLE `account_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `activity_date` datetime DEFAULT NULL,
  `activity_type` varchar(255) NOT NULL,
  `download_count` int(11) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fgo4axri9gm1he9vk7wgaglg6` (`account_id`),
  CONSTRAINT `FK_fgo4axri9gm1he9vk7wgaglg6` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `account_role`
--

DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_role_unique_key` (`account_id`,`role_id`),
  KEY `FK_p2jpuvn8yll7x96rae4hvw3sj` (`role_id`),
  CONSTRAINT `FK_ibmw1g5w37bmuh5fc0db7wn10` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK_p2jpuvn8yll7x96rae4hvw3sj` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `authenticate_token`
--

DROP TABLE IF EXISTS `authenticate_token`;
CREATE TABLE `authenticate_token` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(36) NOT NULL,
  `status` int(10) unsigned NOT NULL DEFAULT '0',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `token_UNIQUE` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `classifier_enabled` bit(1) DEFAULT NULL,
  `code` varchar(64) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `duration_hours` int(11) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `follow` longtext,
  `geo` longtext,
  `geo_r` varchar(255) DEFAULT NULL,
  `lang_filters` varchar(255) DEFAULT NULL,
  `last_document` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `micromappers_enabled` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `publicly_listed` bit(1) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `track` longtext,
  `trashed` bit(1) DEFAULT NULL,
  `classifier_enabled_by` bigint(20) DEFAULT NULL,
  `crisis_type` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  `purpose` longtext,
  `save_media_enabled` bit(1) DEFAULT b'0',
  `usage_type` int(11) DEFAULT NULL,
  `fetch_interval` int(11) DEFAULT '0',
  `last_execution_time` datetime DEFAULT NULL,
  `fetch_from` int(11) DEFAULT '168',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pyhw8xfr9ke76q1eau3b3gw43` (`code`),
  UNIQUE KEY `UK_y9tqsfhcsdnb3oqmc6dmb2i1` (`name`),
  KEY `FK_g5km0lqea175itpx4n9dhpfc7` (`classifier_enabled_by`),
  KEY `FK_496k935i20qm8463yenj3b8gg` (`owner_id`),
  KEY `fk_collection_crisis_type` (`crisis_type`),
  CONSTRAINT `FK_496k935i20qm8463yenj3b8gg` FOREIGN KEY (`owner_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK_g5km0lqea175itpx4n9dhpfc7` FOREIGN KEY (`classifier_enabled_by`) REFERENCES `account` (`id`),
  CONSTRAINT `fk_collection_crisis_type` FOREIGN KEY (`crisis_type`) REFERENCES `crisis_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `collection_collaborator`
--

DROP TABLE IF EXISTS `collection_collaborator`;
CREATE TABLE `collection_collaborator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `collection_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `collection_collaborator_unique_key` (`collection_id`,`account_id`),
  KEY `FK_ltq4n1y6j19ljslbc6u9d6leq` (`account_id`),
  CONSTRAINT `FK_b8u8nrdte1d11qb05620ry6f` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`),
  CONSTRAINT `FK_ltq4n1y6j19ljslbc6u9d6leq` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `collection_log`
--

DROP TABLE IF EXISTS `collection_log`;
CREATE TABLE `collection_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `collection_id` bigint(20) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `follow` longtext,
  `geo` longtext,
  `lang_filters` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `track` longtext,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `collection_summary`
--

DROP TABLE IF EXISTS `collection_summary`;
CREATE TABLE `collection_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `collection_creation_date` datetime DEFAULT NULL,
  `curator` varchar(255) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `geo` varchar(1000) DEFAULT NULL,
  `keywords` varchar(5000) DEFAULT NULL,
  `label_count` int(11) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `publicly_listed` bit(1) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_count` bigint(20) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `crisis_type` varchar(255) DEFAULT NULL,
  `human_tag_count` bigint(20) DEFAULT NULL,
  `machine_tag_count` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `collection_summery_history`
--

DROP TABLE IF EXISTS `collection_summery_history`;
CREATE TABLE `collection_summery_history` (
  `collection_id` int(11) NOT NULL,
  `name` varchar(140) COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(65) COLLATE utf8mb4_unicode_ci NOT NULL,
  `total_count` bigint(20) NOT NULL,
  PRIMARY KEY (`collection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `crisis_type`
--

DROP TABLE IF EXISTS `crisis_type`;
CREATE TABLE `crisis_type` (
  `name` varchar(140) NOT NULL DEFAULT 'default type',
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `custom_ui_template`
--

DROP TABLE IF EXISTS `custom_ui_template`;
CREATE TABLE `custom_ui_template` (
  `customUITemplateID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `crisisID` bigint(20) NOT NULL,
  `nominalAttributeID` bigint(20) DEFAULT NULL,
  `templateType` int(10) unsigned NOT NULL,
  `templateValue` text NOT NULL,
  `status` int(11) DEFAULT '0',
  `isActive` bit(1) NOT NULL DEFAULT b'1',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`customUITemplateID`),
  UNIQUE KEY `customUITemplateID_UNIQUE` (`customUITemplateID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
CREATE TABLE `document` (
  `documentID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `crisisID` bigint(20) NOT NULL,
  `isEvaluationSet` bit(1) NOT NULL DEFAULT b'0',
  `hasHumanLabels` bit(1) NOT NULL DEFAULT b'0',
  `valueAsTrainingSample` double unsigned NOT NULL DEFAULT '0',
  `receivedAt` datetime NOT NULL,
  `language` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'en',
  `doctype` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `data` text COLLATE utf8mb4_unicode_ci,
  `wordFeatures` mediumtext COLLATE utf8mb4_unicode_ci,
  `geoFeatures` mediumtext COLLATE utf8mb4_unicode_ci,
  `source_colllection_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`documentID`),
  KEY `fk_Document_Crisis_crisisID_idx` (`crisisID`),
  KEY `isEvaluationSet` (`isEvaluationSet`),
  KEY `valueAsTrainingSample` (`valueAsTrainingSample`),
  KEY `receivedAt` (`receivedAt`),
  KEY `index_hasHumanLabels` (`crisisID`,`hasHumanLabels`),
  KEY `index_humanLabels` (`hasHumanLabels`),
  KEY `FK_ens02un7wfvld2n1db2h4dd38` (`source_colllection_id`),
  CONSTRAINT `FK_ens02un7wfvld2n1db2h4dd38` FOREIGN KEY (`source_colllection_id`) REFERENCES `collection` (`id`),
  CONSTRAINT `fk_document_collection` FOREIGN KEY (`crisisID`) REFERENCES `collection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `document_nominal_label`
--

DROP TABLE IF EXISTS `document_nominal_label`;
CREATE TABLE `document_nominal_label` (
  `documentID` bigint(20) unsigned NOT NULL,
  `nominalLabelID` bigint(20) unsigned NOT NULL,
  `timestamp` datetime NOT NULL,
  `userID` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`documentID`,`nominalLabelID`,`userID`),
  KEY `fk_Document_NominalLabel_documentID_idx` (`documentID`),
  KEY `fk_document_nominal_label_nominal_label_idx` (`nominalLabelID`),
  CONSTRAINT `fk_Document_NominalLabel_documentID` FOREIGN KEY (`documentID`) REFERENCES `document` (`documentID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_document_nominal_label_nominal_label` FOREIGN KEY (`nominalLabelID`) REFERENCES `nominal_label` (`nominalLabelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `emsc_earthquake`
--

DROP TABLE IF EXISTS `emsc_earthquake`;
CREATE TABLE `emsc_earthquake` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  `event_code` varchar(100) NOT NULL,
  `crisis_code` varchar(100) NOT NULL,
  `token` varchar(36) NOT NULL,
  `magnitude` decimal(10,0) NOT NULL,
  `durationInHours` int(11) NOT NULL,
  `geo` varchar(200) NOT NULL,
  `epicenter` varchar(100) NOT NULL,
  `started_at` datetime NOT NULL,
  `scheduled_stop` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `model`
--

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
  KEY `fk_Model_ModelFamily_modelFamilyID_idx` (`modelFamilyID`),
  CONSTRAINT `fk_Model_ModelFamily_modelFamilyID_idx` FOREIGN KEY (`modelFamilyID`) REFERENCES `model_family` (`modelFamilyID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `model_family`
--

DROP TABLE IF EXISTS `model_family`;
CREATE TABLE `model_family` (
  `modelFamilyID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `crisisID` bigint(20) NOT NULL,
  `nominalAttributeID` bigint(20) unsigned NOT NULL,
  `isActive` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`modelFamilyID`),
  UNIQUE KEY `unique` (`crisisID`,`nominalAttributeID`),
  UNIQUE KEY `UK_2pwnj9eh4m95fa8w589oockx8` (`crisisID`,`nominalAttributeID`),
  KEY `fk_ModelFamily_Crisis_idx` (`crisisID`),
  KEY `fk_ModelFamily_NominalAttribute_idx` (`nominalAttributeID`),
  CONSTRAINT `fk_ModelFamily_NominalAttribute` FOREIGN KEY (`nominalAttributeID`) REFERENCES `nominal_attribute` (`nominalAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_modelfamily_collection` FOREIGN KEY (`crisisID`) REFERENCES `collection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `model_nominal_label`
--

DROP TABLE IF EXISTS `model_nominal_label`;
CREATE TABLE `model_nominal_label` (
  `modelID` int(10) unsigned NOT NULL,
  `nominalLabelID` bigint(20) unsigned NOT NULL,
  `labelPrecision` double DEFAULT NULL,
  `labelRecall` double DEFAULT NULL,
  `labelAuc` double DEFAULT NULL,
  `classifiedDocumentCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`modelID`,`nominalLabelID`),
  KEY `fk_model_label_modelID_idx` (`modelID`),
  KEY `fk_model_nominal_label_nominal_label_idx` (`nominalLabelID`),
  CONSTRAINT `fk_model_modellabel_modelID` FOREIGN KEY (`modelID`) REFERENCES `model` (`modelID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_model_nominal_label_nominal_label` FOREIGN KEY (`nominalLabelID`) REFERENCES `nominal_label` (`nominalLabelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `nominal_attribute`
--

DROP TABLE IF EXISTS `nominal_attribute`;
CREATE TABLE `nominal_attribute` (
  `nominalAttributeID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userID` bigint(20) NOT NULL,
  `name` varchar(140) NOT NULL,
  `description` varchar(600) NOT NULL DEFAULT '',
  `code` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`nominalAttributeID`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  UNIQUE KEY `UK_gf3jrpsx7xvm8ep4rt6igsgdj` (`code`),
  KEY `fk_nominalAttribute_users_userID_idx` (`userID`),
  CONSTRAINT `fk_nominalAttribute_account_id` FOREIGN KEY (`userID`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `nominal_label`
--

DROP TABLE IF EXISTS `nominal_label`;
CREATE TABLE `nominal_label` (
  `nominalLabelID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nominalLabelCode` varchar(64) NOT NULL DEFAULT '',
  `nominalAttributeID` bigint(20) unsigned NOT NULL,
  `name` varchar(140) NOT NULL DEFAULT '',
  `description` varchar(600) NOT NULL DEFAULT '',
  `sequence` int(10) unsigned NOT NULL DEFAULT '100',
  PRIMARY KEY (`nominalLabelID`),
  KEY `asd_idx` (`nominalAttributeID`),
  CONSTRAINT `fk_NominalLabel_NominalAttribute_nominalAttributeID` FOREIGN KEY (`nominalAttributeID`) REFERENCES `nominal_attribute` (`nominalAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `level` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `system_event`
--

DROP TABLE IF EXISTS `system_event`;
CREATE TABLE `system_event` (
  `eventID` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `emailSent` bit(1) DEFAULT NULL,
  `module` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `receivedAt` datetime NOT NULL,
  `severity` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`eventID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `task_answer`
--

DROP TABLE IF EXISTS `task_answer`;
CREATE TABLE `task_answer` (
  `documentID` bigint(20) unsigned NOT NULL,
  `userID` bigint(20) unsigned NOT NULL,
  `answer` text NOT NULL,
  `timestamp` datetime NOT NULL,
  `fromTrustedUser` bit(1) NOT NULL DEFAULT b'0',
  `taskID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`taskID`,`documentID`,`userID`),
  UNIQUE KEY `taskID_UNIQUE` (`taskID`),
  UNIQUE KEY `UK_k8gd6ua0rot3ylow8yqeykjik` (`taskID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `task_assignment`
--

DROP TABLE IF EXISTS `task_assignment`;
CREATE TABLE `task_assignment` (
  `documentID` bigint(20) unsigned NOT NULL,
  `userID` bigint(20) unsigned NOT NULL,
  `assignedAt` datetime NOT NULL,
  PRIMARY KEY (`documentID`,`userID`),
  KEY `fk_TaskAssignment_Document_idx` (`documentID`),
  KEY `assignedAt` (`assignedAt`),
  CONSTRAINT `fk_TaskAssignment_Document` FOREIGN KEY (`documentID`) REFERENCES `document` (`documentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `word_dictionary`
--

DROP TABLE IF EXISTS `word_dictionary`;
CREATE TABLE `word_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `is_stop_word` bit(1) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `word` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE VIEW `nominal_label_evaluation_data` AS
	SELECT 
        `d`.`documentID` AS `documentID`,
        `d`.`crisisID` AS `crisisID`,
        `dnl`.`nominalLabelID` AS `nominalLabelID`,
        `nl`.`nominalAttributeID` AS `nominalAttributeID`,
        `d`.`wordFeatures` AS `wordFeatures`
    FROM
        ((`document` `d`
        JOIN `document_nominal_label` `dnl` ON ((`d`.`documentID` = `dnl`.`documentID`)))
        JOIN `nominal_label` `nl` ON ((`nl`.`nominalLabelID` = `dnl`.`nominalLabelID`)))
    WHERE
        (`d`.`isEvaluationSet`
            AND (`d`.`wordFeatures` IS NOT NULL)
            AND (`nl`.`nominalLabelCode` <> 'null'));

			
CREATE VIEW `nominal_label_training_data` AS
    SELECT 
        `d`.`documentID` AS `documentID`,
        `d`.`crisisID` AS `crisisID`,
        `dnl`.`nominalLabelID` AS `nominalLabelID`,
        `nl`.`nominalAttributeID` AS `nominalAttributeID`,
        `d`.`wordFeatures` AS `wordFeatures`
    FROM
        ((`document` `d`
        JOIN `document_nominal_label` `dnl` ON ((`d`.`documentID` = `dnl`.`documentID`)))
        JOIN `nominal_label` `nl` ON ((`nl`.`nominalLabelID` = `dnl`.`nominalLabelID`)))
    WHERE
        ((NOT (`d`.`isEvaluationSet`))
            AND (`d`.`wordFeatures` IS NOT NULL)
            AND (`nl`.`nominalLabelCode` <> 'null'));

SET FOREIGN_KEY_CHECKS=0;