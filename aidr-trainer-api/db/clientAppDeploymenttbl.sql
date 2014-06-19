CREATE TABLE `clientAppDeployment` (
  `deploymentID` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clientAppID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`deploymentID`),
  UNIQUE KEY `crisisID_UNIQUE` (`deploymentID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
