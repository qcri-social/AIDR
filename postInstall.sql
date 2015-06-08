use aidr_predict;

# Final view structure for view `nominal_label_evaluation_data`

DROP TABLE IF EXISTS `nominal_label_evaluation_data`;
DROP VIEW IF EXISTS `nominal_label_evaluation_data`;
CREATE VIEW nominal_label_evaluation_data AS 
select `d`.`documentID` AS `documentID`,`d`.`crisisID` AS `crisisID`,`dnl`.`nominalLabelID` AS `nominalLabelID`,`nl`.`nominalAttributeID` AS `nominalAttributeID`,`d`.`wordFeatures` AS `wordFeatures` from ((`document` `d` join `document_nominal_label` `dnl` on((`d`.`documentID` = `dnl`.`documentID`))) join `nominal_label` `nl` on((`nl`.`nominalLabelID` = `dnl`.`nominalLabelID`))) where (`d`.`isEvaluationSet` and (`d`.`wordFeatures` is not null) and (`nl`.`nominalLabelCode` <> 'null'));


# Final view structure for view `nominal_label_training_data`

DROP TABLE IF EXISTS `nominal_label_training_data`;
DROP VIEW IF EXISTS `nominal_label_training_data`;
CREATE VIEW nominal_label_training_data AS 
select `d`.`documentID` AS `documentID`,`d`.`crisisID` AS `crisisID`,`dnl`.`nominalLabelID` AS `nominalLabelID`,`nl`.`nominalAttributeID` AS `nominalAttributeID`,`d`.`wordFeatures` AS `wordFeatures` from ((`document` `d` join `document_nominal_label` `dnl` on((`d`.`documentID` = `dnl`.`documentID`))) join `nominal_label` `nl` on((`nl`.`nominalLabelID` = `dnl`.`nominalLabelID`))) where ((not(`d`.`isEvaluationSet`)) and (`d`.`wordFeatures` is not null) and (`nl`.`nominalLabelCode` <> 'null'));


# Trigger on document to enable inserting data to nominal_label_evaluation_data

DROP TRIGGER IF EXISTS `document_BINS`;
CREATE TRIGGER `document_BINS`
BEFORE INSERT ON `document`
FOR EACH ROW set new.isEvaluationSet = mod((SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='document'), 5)=0;


# Populate table crisis_type

DELETE FROM `crisis_type`;
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (10, '(Not defined)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1100, 'Natural Hazard: Geophysical: Earthquake and/or Tsunami');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1110, 'Natural Hazard: Geophysical: Volcano');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1120, 'Natural Hazard: Geophysical: Dry mass movement (rockfall, avalanche, landslide, subsidence)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1130, 'Natural Hazard: Meteorological: Storm (tropical, cyclone, tornado, blizzard, dust storm)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1140, 'Natural Hazard: Hydrological: Flood (river-, flash-, coastal-)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1150, 'Natural Hazard: Hydrological: Wet mass movement (rockfall, avalanche, landslide, subsidence)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1160, 'Natural Hazard: Climatological: Extreme temperature (heat/cold wave)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1170, 'Natural Hazard: Climatological: Drought');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1180, 'Natural Hazard: Climatological: Fire (forest, bush, grass, wild)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1190, 'Natural Hazard: Biological: Epidemic (diseases, insects, animals)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1200, 'Natural Hazard: Other');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1210, 'Human Induced: War or armed conflict, incl. acts of war');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1220, 'Human Induced: Terrorist attack against civilians');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1230, 'Human Induced: Demonstrations (peaceful or violent, riots)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1240, 'Human Induced: Pollution (hazardous material, oil spill, etc.)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1250, 'Human Induced: Radiation, including nuclear explosion');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1260, 'Human Induced: Transportation accident (train, boat, plane, etc.)');
INSERT INTO `crisis_type` (`crisisTypeID`, `name`) values (1270, 'Human Induced: Other');


# create system user

INSERT INTO `users` (`userID`, `name`, `role`) VALUES ('1', 'System', 'admin');


# populate table nominal_attribute

INSERT INTO `nominal_attribute` (`nominalAttributeID`, `userID`, `name`, `description`, `code`)
VALUES
	(1,1,'Informative','Indicate if the item contains information that is useful for capturing and understanding the situation on the ground','informative'),
	(2,1,'Information provided','Indicate what type of information is provided in the message. This is a ranked list. If more than one information type is present, choose the one that appears first on the list','information_provided'),
	(3,1,'Humanitarian Clusters','These categories are in line with those put forth by the United Nations \"clusters\" approach. If more than one category is equally represented in the message, choose \"multiple\"','un_cluster'),
	(4,1,'Urgent needs','Indicate if the item mentions a need of the affected population that is immediate or short-term. If more than one applies, indicate \"multiple\"','needs'),
	(5,1,'Information source','Indicate what the is the apparent source of this information. Click on links when necessary to identify the source','information_source');
	

# populate table nominal_label

INSERT INTO `nominal_label` (`nominalLabelCode`, `nominalAttributeID`, `name`, `description`, `sequence`)
VALUES
	('informative',1,'Informative','Contains useful information that helps you understand the situation',100),
	('not_informative',1,'Not informative','Refers to the crisis, but does not contain useful information that helps you understand the situation',100),
	('not_related',1,'Not related to crisis','Not related to this crisis',100),
	('null',1,'N/A','Not applicable, cannot judge, not readable, not sure',100),
	('casualties',2,'Injured or dead people','Casualties due to the crisis',100),
	('missing',2,'Missing, trapped, or found people','Questions and/or reports about missing or found people',100),
	('displaced',2,'Displaced people','People who have relocated due to the crisis, even for a short time (includes evacuations)',100),
	('infrastructure',2,'Infrastructure and utilities','Buildings or roads damaged or operational; utilities/services interrupted or restored',100),
	('supplies',2,'Shelter and supplies','Needs or donations of shelter and/or supplies such as food, water, clothing, medical supplies or blood',100),
	('money',2,'Money','Money requested, donated or spent',100),
	('services',2,'Volunteer or professional services','Services needed or offered by volunteers or professionals',100),
	('animals',2,'Animal management','Pets and animals, living, missing, displaced, or injured/dead',100),
	('caution_advice',2,'Caution and advice','Warnings issued or lifted, guidance and tips',100),
	('personal',2,'Personal updates','Status updates about individuals or loved ones',100),
	('sympathy',2,'Sympathy and emotional support','Thoughts and prayers',100),
	('other',2,'Other relevant information','Other useful information that helps understand the situation',100),
	('not_related',2,'Not related or irrelevant','Unrelated to the situation or irrelevant',100),
	('null',2,'N/A','Not applicable, cannot judge, not readable, not sure',100),
	('children',3,'Education/child welfare','Children\'s well being and education',100),
	('telecommunications',3,'Telecommunications','Mobile and landline networks, internet',100),
	('food',3,'Food/nutrition','Nutritional well being',100),
	('health',3,'Health','Mental, physical, emotional well being',100),
	('logistics',3,'Logistics/transportation','Delivery and storage of goods and supplies',100),
	('shelter',3,'Camp/shelter','Condition and location of shelters and camps',100),
	('hygiene',3,'Water, sanitation, hygiene','Availability of clean water, waste and sewage disposal, access to hygienic facilities',100),
	('security',3,'Safety/security','Protection of people/property against harm such as violence or theft',100),
	('multiple',3,'Multiple','More than one of the above are equally represented on this message',100),
	('null',3,'N/A','Not applicable, cannot judge, not readable, not sure',100),
	('money',4,'Money needed','Donations or transfers of money are needed/requested',100),
	('food_water',4,'Food and/or water needed','Food and/or water are needed',100),
	('clothing',4,'Clothing needed','Clothing, shoes and/or blankets are needed',100),
	('shelter',4,'Shelter needed','Shelter space/beds are needed',100),
	('medical',4,'Blood or other medical supplies needed','Blood donors or other medical supplies are needed',100),
	('service',4,'Services are needed','Volunteer or professional services are needed',100),
	('other',4,'Other type of need','Other type of help, supplies, equipment or machines are needed',100),
	('multiple',4,'Multiple','More than one of the above is mentioned',100),
	('not_need',4,'Not need-related','Not related to a need',100),
	('null',4,'N/A','Not applicable, cannot judge, not readable, not sure',100),
	('eyewitness',5,'Eye witness','Describes an eyewitness account',100),
	('government',5,'Government','National, regional or local government agencies, police, and/or military',100),
	('ngo',5,'Non-government','Non-governmental organizations',100),
	('business',5,'Business','For-profit business or corporations',100),
	('traditional_media',5,'Traditional media','Mentions traditional media: television, radio, or newspaper',100),
	('internet_only_media',5,'Internet-only media','Mentions websites, blogs or other sites not associated with television, radio or newspapers',100),
	('null',5,'N/A','Not applicable, cannot judge, not readable, not sure',100);


# Update DB character set

ALTER DATABASE aidr_fetch_manager CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
ALTER TABLE aidr_fetch_manager.AIDR_COLLECTION CHANGE last_document last_document LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER DATABASE aidr_predict CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
ALTER TABLE aidr_predict.document CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE aidr_predict.document CHANGE data data TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


