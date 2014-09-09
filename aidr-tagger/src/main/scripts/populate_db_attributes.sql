
DELETE FROM `nominal_label`;
DELETE FROM `nominal_attribute`;

INSERT INTO `nominal_attribute` (`nominalAttributeID`, `code`, `name`, `description`)
 VALUES (100, "unclusters_v1", "Clusters v1.0", "Humanitarian Clusters, v1.0");

INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "null", "Does not apply", "The label does not apply, or I am not sure about the label for this message");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "010_camps", "Camp management", "Camp coordination and camp management (IOM, UNHCR, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "020_recovery", "Early recovery", "Early recovery (UNDP, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "030_education", "Education", "Education (UNICEF, Save the Children, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "040_telecom", "Emergency telecommunications", "Emergency telecommunications (WFP, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "050_foodsec", "Food security", "Food security (WFP, FAO, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "060_health", "Health", "Health (WHO, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "070_logistics", "Logistics", "Logistics (WFP, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "080_nutrition", "Nutrition", "Nutrition (UNICEF, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "090_protection", "Protection", "Protection (UNHCR, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "100_shelter", "Shelter", "Shelter (IFRC, UNHCR, ...)");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (100, "110_sanitation", "Sanitation", "Water, sanitation and hygiene (UNICEF, ...)");


INSERT INTO `nominal_attribute` (`nominalAttributeID`, `code`, `name`, `description`)
 VALUES (200, "eyewitness_v1", "Eye witness v1.0", "Eye witness accounts, v1.0");

INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (200, "null", "Does not apply", "The label does not apply, or I am not sure about the label for this message");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (200, "010_true", "Yes", "Describes an eyewitness account");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (200, "020_false", "No", "Does not describe an eyewitness account");


INSERT INTO `nominal_attribute` (`nominalAttributeID`, `code`, `name`, `description`)
 VALUES (300, "informative_v1", "Informative v1.0", "Informative messages enhancing situational awareness, v1.0");

INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (300, "null", "Does not apply", "The label does not apply, or I am not sure about the label for this message");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (300, "010_informative", "Informative", "Contributes useful information enhancing situational awareness");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (300, "020_personal", "Personal only", "Personal and only useful to a small circle of family/friends of the author");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (300, "030_not_info", "Not related to crisis", "Not related to the crisis");


INSERT INTO `nominal_attribute` (`nominalAttributeID`, `code`, `name`, `description`)
 VALUES (400, "type_v1", "Type v1.0", "Main type of a message, broad types, v1.0");

INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (400, "null", "Does not apply", "The label does not apply");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (400, "010_caution", "Caution and advice", "Warns about a potential danger or provides advice");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (400, "020_damage", "Casualties and damage", "Reports about people injured/dead, or infrastructure damage");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (400, "030_donation", "Donations", "Requests or offers donations of goods or services");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (400, "040_people", "People", "Informs about people missing or found, or celebrities/authorities reacting to the crisis");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (400, "050_sources", "Information sources", "Informs about information sources");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (400, "800_other_news", "Other news", "Generic news report that does not fit the above classes");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (400, "900_other", "None of the above", "Other, none of the above");


INSERT INTO `nominal_attribute` (`nominalAttributeID`, `code`, `name`, `description`)
 VALUES (500, "media_v1", "Multimedia v1.0", "Multimedia resources, v1.0");

INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (500, "null", "Does not apply", "The label does not apply");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (500, "010_photo", "Photo", "Contains a still photo or album");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (500, "020_video", "Video", "Contains video footage");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (500, "030_audio", "Audio", "Contains audio");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (500, "900_other", "None of the above", "Does not contain any of the above");

INSERT INTO `nominal_attribute` (`nominalAttributeID`, `code`, `name`, `description`)
 VALUES (600, "needs_v1", "Individual needs v1.0", "Messages of type needs like water, food, blood");

INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (600, "null", "N/A: does not apply, or cannot judge", "If these categories do not apply to this message, or you cannot be sure about which is the correct category");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
 VALUES (600, "010_food", "Food", "If the message talks about food requests");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
    VALUES (600,"020_water","Water","If the message contains water request");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
    VALUES (600,"030_clothes_shoes","Clothes or shoes","If the message contains clothes or shoes request");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
    VALUES (600,"040_volunteer_work","Colunterr work","If the message contains any type of voluteers requests");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
    VALUES (600,"050_medical","Medical supplies","If the message contains request for medical supplies");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
    VALUES (600,"060_blood","Blood","If blood request is reported in the message");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
    VALUES (600,"070_equipment","Equipment or machinery","If any kind of equipment or machinery is requested");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
    VALUES (600,"080_transport","Means of transport","If any means of transport are requested");
INSERT INTO `nominal_label` (`nominalAttributeID`, `nominalLabelCode`, `name`, `description`)
    VALUES (600,"090_money","Other need","Any other kind of need/request");