CREATE TABLE `aidr_fetch_manager`.`collection_summery_history` (
  `collection_id` INT(11) NOT NULL,
  `name` VARCHAR(140) NOT NULL,
  `code` VARCHAR(65) NOT NULL,
  `total_count` BIGINT NOT NULL,
  PRIMARY KEY (`collection_id`));

USE `aidr_fetch_manager`;
Insert into `aidr_fetch_manager`.`collection_summery_history` (collection_id, name, code, total_count)
select   collectionID, a1.name, a1.code, SUM(a2.count+a1.count) as total_count from AIDR_COLLECTION_LOG a2
Inner Join AIDR_COLLECTION a1 on a1.id = a2.collectionID
group by a2.collectionID ;


