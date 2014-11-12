ALTER TABLE `aidr_predict`.`nominal_label` 
ADD COLUMN `sequence` INT(10) UNSIGNED NOT NULL DEFAULT 100 AFTER `description`;