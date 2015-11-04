DELIMITER $$
CREATE PROCEDURE aidr_collection_migration()

BEGIN

	DECLARE i INT DEFAULT 0;
	DECLARE size INT DEFAULT 0;
	DECLARE logID BIGINT DEFAULT 0;
	DECLARE oldCollectionID BIGINT DEFAULT 0;
	DECLARE collectionID1 BIGINT DEFAULT 0;

	
	# disable foreign key checks	
	set foreign_key_checks=0;
	
	# 1. insert from crisis, aidr_collection
	
	insert into collection (id, created_at, updated_at, name, code, crisis_type, provider, duration_hours, count,  start_date, end_date, follow, geo, geo_r, lang_filters, last_document, classifier_enabled, publicly_listed, status, track, classifier_enabled_by, owner_id, micromappers_enabled)

	select c.crisisID, ac.createdDate, now(), ac.name, ac.code, ac.crisisType, ac.collection_type, ac.durationHours, ac.count, ac.startDate, ac.endDate, ac.follow, ac.geo, ac.geoR, ac.langFilters, ac.last_document, 1, ac.publiclyListed, ac.status, ac.track, c.userID, ac.user_id, c.isMicromapperEnabled from crisis c join aidr_collection ac on c.code = ac.code;


	# 2. insert from aidr_collection not in collection
	
	insert into collection (created_at, updated_at, name, code, crisis_type, provider, duration_hours, count,  start_date, end_date, follow, geo, geo_r, lang_filters, last_document, classifier_enabled, publicly_listed, status, track, classifier_enabled_by, owner_id, micromappers_enabled)

	select ac.createdDate, now(), ac.name, ac.code, ac.crisisType, ac.collection_type, ac.durationHours, ac.count, ac.startDate, ac.endDate, ac.follow, ac.geo, ac.geoR, ac.langFilters, ac.last_document, 0, ac.publiclyListed, ac.status, ac.track, null, ac.user_id, 0 from aidr_collection ac where ac.code not in (select code from collection)


	# 3. temp table for old and new collection id
	
	DROP TEMPORARY TABLE IF EXISTS collection_id_temp;
	CREATE TEMPORARY TABLE collection_id_temp AS select ac.id as oldID, c.id as newID from collection c join aidr_collection ac on ac.code = c.code;

	# insert into collection collaborator
	select count(1) from collection_id_temp into size;
	WHILE (i < size) DO
		
		SELECT oldID FROM collection_id_temp order by oldID LIMIT i,1 INTO oldCollectionID; 
	
		insert into collection_collaborator(created_at, updated_at, collection_id, account_id)
		select now(), now(), (select newID from collection_id_temp where oldID = oldCollectionID), id_manager from aidr_collection_manager where id_collection = oldCollectionID;
		 
		SET i = i + 1;
	END WHILE; 
	
	SET i = 0;

	# 4. temp table for collectionID from aidr_collection_log
	DROP TEMPORARY TABLE IF EXISTS collection_log_id_temp;
	CREATE TEMPORARY TABLE collection_log_id_temp AS select distinct (collectionID) from aidr_collection_log;
	
	select count(1) from collection_log_id_temp into size;
	
	# 5. insert into collection_log from aidr_collection_log
	WHILE (i < size) DO
		
		SELECT collectionID FROM collection_log_id_temp order by collectionID LIMIT i,1 INTO oldCollectionID; 
		select newID from collection_id_temp where oldID = oldCollectionID INTO collectionID1
	
		insert into collection_log(created_at, updated_at, collection_id, track, follow, count, geo, lang_filters, updated_by, end_date, start_date)
		select now(), now(), collectionID1, track, follow, count, geo, langFilters, 1, endDate, startDate from aidr_collection_log where collectionID = oldCollectionID;
		 
		SET i = i + 1;
	END WHILE; 
	
	# model_family and document foreign key to collection

	ALTER TABLE document ALTER crisisID DROP DEFAULT;
	ALTER TABLE document CHANGE COLUMN crisisID crisisID BIGINT(20) NOT NULL;
	alter table document add constraint fk_document_collection foreign key (crisisID) references collection(id);
		
	alter table model_family drop foreign key fk_ModelFamily_Crisis;
	ALTER TABLE model_family ALTER crisisID DROP DEFAULT;
	ALTER TABLE model_family CHANGE COLUMN crisisID crisisID BIGINT(20) NOT NULL;
	alter table model_family add constraint fk_modelfamily_collection foreign key (crisisID) references collection(id);

	update crisis_type set id = crisisTypeID, created_at = now(), updated_at = now() ;
	ALTER TABLE crisis_type CHANGE id id BIGINT(20) AUTO_INCREMENT PRIMARY KEY;
	alter table crisis_type drop column crisisTypeID;
	
	
	# enable foreign key checks	
	set foreign_key_checks=1;
END