CREATE TABLE `taskTranslation` (
  `translationID` int(20) NOT NULL AUTO_INCREMENT,
  `taskID` bigint(20),
  `taskQueueID` bigint(20),
  `clientAppID` bigint(20),
  `twbOrderID` bigint(20),  
  `tweetID` varchar(100) NOT NULL,
  `originalText` text,
  `translatedText` text,
  `author` varchar(100) DEFAULT NULL,
  `lat` varchar(100) DEFAULT NULL,
  `lon` varchar(100) DEFAULT NULL,
  `url` varchar(300) DEFAULT NULL,
  `answerCode` varchar(100), 
  `status` varchar(30) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`translationID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;


