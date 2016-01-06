ALTER TABLE `collection`
	CHANGE COLUMN `last_document` `last_document` VARCHAR(1024) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci';