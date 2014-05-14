CREATE TABLE `AIDR_AUTHENTICATETOKEN` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(36) NOT NULL,
  `status` int(10) unsigned NOT NULL DEFAULT '0',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `token_UNIQUE` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
