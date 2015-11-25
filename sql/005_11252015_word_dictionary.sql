CREATE TABLE `aidr_predict`.`word_dictionary` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `word` VARCHAR(100) NOT NULL,
  `language` VARCHAR(100) NOT NULL,
  `is_stop_word` BIT NOT NULL DEFAULT 0,
  `created_at` DATETIME NULL,
  PRIMARY KEY (`id`));
