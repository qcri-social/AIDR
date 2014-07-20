
ALTER TABLE aidr_predict.crisis ADD `isTrashed` bit(1) NOT NULL DEFAULT b'0';

ALTER TABLE aidr_fetch_manager.AIDR_COLLECTION ADD `publiclyListed` bit(1) NOT NULL DEFAULT b'1';
