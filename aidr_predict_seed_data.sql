
/*	aidr_predict.role	*/
INSERT INTO `role` (`id`,`created_at`,`updated_at`,`description`,`level`) VALUES (1,'2015-10-20 08:34:46','2015-10-20 08:34:46','Admin role allows users to manage collections, users, and roles.','ADMIN');


/*	aidr_predict.account	*/
INSERT INTO `account` (`id`,`api_key`,`created_at`,`locale`,`provider`,`updated_at`,`user_name`,`email`,`download_permitted`) VALUES (1,'6b8abbd6-7705-11e5-84fd-000d3ab04947','2015-10-20 08:34:46','en','twitter','2015-10-20 08:34:46','System',NULL,0);


/*  aidr_predict.crisis_type */
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('(Not defined)',10,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Geophysical: Earthquake and/or Tsunami',1100,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Geophysical: Volcano',1110,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Geophysical: Dry mass movement (rockfall, avalanche, landslide, subsidence)',1120,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Meteorological: Storm (tropical, cyclone, tornado, blizzard, dust storm)',1130,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Hydrological: Flood (river-, flash-, coastal-)',1140,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Hydrological: Wet mass movement (rockfall, avalanche, landslide, subsidence)',1150,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Climatological: Extreme temperature (heat/cold wave)',1160,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Climatological: Drought',1170,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Climatological: Fire (forest, bush, grass, wild)',1180,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Biological: Epidemic (diseases, insects, animals)',1190,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Natural Hazard: Other',1200,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Human Induced: War or armed conflict, incl. acts of war',1210,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Human Induced: Terrorist attack against civilians',1220,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Human Induced: Demonstrations (peaceful or violent, riots)',1230,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Human Induced: Pollution (hazardous material, oil spill, etc.)',1240,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Human Induced: Radiation, including nuclear explosion',1250,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Human Induced: Transportation accident (train, boat, plane, etc.)',1260,'2015-11-19 14:27:36','2015-11-19 14:27:36');
INSERT INTO `crisis_type` (`name`,`id`,`created_at`,`updated_at`) VALUES ('Human Induced: Other',1270,'2015-11-19 14:27:36','2015-11-19 14:27:36');

/*	aidr_predict.nominal_attribute 	*/

INSERT INTO `nominal_attribute` (`nominalAttributeID`,`userID`,`name`,`description`,`code`) VALUES (100,1,'Clusters (v1)','Humanitarian Clusters, v1.0','unclusters_v1');
INSERT INTO `nominal_attribute` (`nominalAttributeID`,`userID`,`name`,`description`,`code`) VALUES (200,1,'Eye witness (v1)','Eye witness accounts, v1.0','eyewitness_v1');
INSERT INTO `nominal_attribute` (`nominalAttributeID`,`userID`,`name`,`description`,`code`) VALUES (300,1,'Informative (v1)','Informative messages enhancing situational awareness, v1.0','informative_v1');
INSERT INTO `nominal_attribute` (`nominalAttributeID`,`userID`,`name`,`description`,`code`) VALUES (400,1,'Type (v1)','Main type of a message, broad types, v1.0','type_v1');
INSERT INTO `nominal_attribute` (`nominalAttributeID`,`userID`,`name`,`description`,`code`) VALUES (500,1,'Multimedia (v1)','Multimedia resources, v1.0','media_v1');
INSERT INTO `nominal_attribute` (`nominalAttributeID`,`userID`,`name`,`description`,`code`) VALUES (504,1,'Related to PRISM/NSA spying row','Is the tweet related to the PRISM/NSA spying row?','nsa_prism_yesno');
INSERT INTO `nominal_attribute` (`nominalAttributeID`,`userID`,`name`,`description`,`code`) VALUES (505,1,'Informative-NonInfomative','Informative If the message reports significant information about the disaster, otherwise not information.','info-nonInfo');


/*aidr_predict.nominal_label */

INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (156,'010_na',100,'Does not apply','The label does not apply',100);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (157,'010_camps',100,'Camp management','Camp coordination and camp management (IOM, UNHCR, ...)',101);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (158,'020_recovery',100,'Early recovery','Early recovery (UNDP, ...)',102);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (159,'030_education',100,'Education','Education (UNICEF, Save the Children, ...)',103);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (160,'040_telecom',100,'Emergency telecommunications','Emergency telecommunications (WFP, ...)',104);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (161,'050_foodsec',100,'Food security','Food security (WFP, FAO, ...)',105);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (162,'060_health',100,'Health','Health (WHO, ...)',106);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (163,'070_logistics',100,'Logistics','Logistics (WFP, ...)',107);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (164,'080_nutrition',100,'Nutrition','Nutrition (UNICEF, ...)',108);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (165,'090_protection',100,'Protection','Protection (UNHCR, ...)',109);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (166,'100_shelter',100,'Shelter','Shelter (IFRC, UNHCR, ...)',110);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (167,'110_sanitation',100,'Sanitation','Water, sanitation and hygiene (UNICEF, ...)',111);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (168,'null',200,'Does not apply','The label does not apply',999);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (169,'010_true',200,'Yes','Describes an eyewitness account',101);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (170,'020_false',200,'No','Does not describe an eyewitness account',102);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (171,'null',300,'Does not apply','The label does not apply, or I am not sure about the label for this message',999);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (172,'010_informative',300,'Informative','Contributes useful information enhancing situational awareness',101);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (173,'020_personal',300,'Personal only','Personal and only useful to a small circle of family/friends of the author',102);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (174,'030_not_info',300,'Not related to crisis','Not related to the crisis',103);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (175,'null',400,'Does not apply','The label does not apply',999);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (176,'010_caution',400,'Caution and advice','Warns about a potential danger or provides advice',101);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (177,'020_damage',400,'Casualties and damage','Reports about people injured/dead, or infrastructure damage',102);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (178,'030_donation',400,'Donations','Requests or offers donations of goods or services',103);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (179,'040_people',400,'People','Informs about people missing or found, or celebrities/authorities reacting to the crisis',104);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (180,'050_sources',400,'Information sources','Informs about information sources',105);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (181,'800_other_news',400,'Other news','Generic news report that does not fit the above classes',106);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (182,'900_other',400,'None of the above','Other, none of the above',107);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (183,'null',500,'Does not apply','The label does not apply',999);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (184,'010_photo',500,'Photo','Contains a still photo or album',101);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (185,'020_video',500,'Video','Contains video footage',102);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (186,'030_audio',500,'Audio','Contains audio',103);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (187,'900_other',500,'None of the above','Does not contain any of the above',104);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (194,'null',504,'N/A','Can\'t judge or does not apply',999);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (195,'spying_no',504,'No: unrelated to spying','The tweet is not related to the spying row',101);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (196,'spying_yes',504,'Yes: related to spying','The tweet is related to the spying row',102);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (197,'info',505,'Informative','If the message conveys useful information about disaster situational awareness.',100);
INSERT INTO `nominal_label` (`nominalLabelID`,`nominalLabelCode`,`nominalAttributeID`,`name`,`description`,`sequence`) VALUES (198,'null',505,'Not Informative','If the message does not convey information about the disaster.',999);
