CREATE TABLE `droneTracker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `geojson` text NOT NULL,
  `videoURL` varchar(200) NOT NULL,
  `displayName` varchar(250) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
