ALTER TABLE `aidr_scheduler`.`clientApp` 
ADD COLUMN (`isCustom` bit(1) NOT NULL DEFAULT b'0',
  `tcProjectId` int(11) DEFAULT NULL,
  `groupID` bigint(20) DEFAULT NULL) 
AFTER `appType`;
