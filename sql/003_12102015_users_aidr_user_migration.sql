DELIMITER $$
CREATE PROCEDURE aidr_user_migration()

BEGIN

	DECLARE i INT DEFAULT 0;
	DECLARE size INT DEFAULT 0;
	DECLARE accountID BIGINT DEFAULT 0;
	DECLARE oldUserID BIGINT DEFAULT 0;
	DECLARE foreignKeyName varchar(255);
	DECLARE collectionID BIGINT DEFAULT 0;
	
	# disable foreign key checks	
	set foreign_key_checks=0;
	
	# copy data from aidr_user to account table
	insert into account(id, provider, user_name, created_at, updated_at, api_key, locale) 
	select userID, 'twitter', name, now(), now(), uuid(), 'en' from users;

	# insert admin role
	insert into role(id, created_at, updated_at, level, description) values (1, now(), now(), 'ADMIN', 'Admin role allows users to manage collections, users, and roles.');
	
	# copy data from user_role to account_role table
	insert into account_role (account_id, role_id, created_at, updated_at) 
	select a.id, 1, now(), now() from aidr_user au, account a where au.user_name = a.user_name and au.id in(select user_id from user_role); 
	
	# create temp table for admin ids
	DROP TEMPORARY TABLE IF EXISTS aidr_user_temp;
	CREATE TEMPORARY TABLE aidr_user_temp AS select * from aidr_user where user_name not in (select user_name from account);
	
	# insert into account_role
	insert into account(created_at, updated_at, api_key, user_name, locale, provider) 
	select now(), now(), uuid(), user_name, 'en', provider from aidr_user_temp;
	
	# create temp table for account id and users id 
	DROP TEMPORARY TABLE IF EXISTS user_temp;
	CREATE TEMPORARY TABLE user_temp AS select a.id, u.id as userID, u.user_name from account a, aidr_user u where a.user_name = u.user_name;

	# temp table for collection
	DROP TEMPORARY TABLE IF EXISTS collection_temp;
	CREATE TEMPORARY TABLE collection_temp AS select id, user_id from aidr_collection;
	
	# update the tables with account id 
	
	select count(1) from collection_temp into size;
	WHILE (i < size) DO
		SELECT id FROM collection_temp order by id LIMIT i,1 INTO collectionID; 
		SELECT user_id FROM collection_temp order by id LIMIT i,1 INTO oldUserID;
	 
		update aidr_collection set user_id = (select id from user_temp where userID = oldUserID) where id =
		collectionID;
	 
		SET i = i + 1;
	END WHILE;
	 
	SET size = 0;
	SET i = 0;
	
	# temp table for collection manager
	DROP TEMPORARY TABLE IF EXISTS collection_manager_temp;
	CREATE TEMPORARY TABLE collection_manager_temp AS select id_collection, id_manager from aidr_collection_manager;
	
	# temp table for distinct manager ids
	DROP TEMPORARY TABLE IF EXISTS manager_id_temp;
	CREATE TEMPORARY TABLE manager_id_temp AS select distinct(id_manager) as manager_id from aidr_collection_manager;
	
	select count(1) from manager_id_temp into size;
	delete from aidr_collection_manager;
	
	WHILE (i < size) DO
		SELECT manager_id FROM manager_id_temp order by manager_id LIMIT i,1 INTO oldUserID; 
	 	
		insert into aidr_collection_manager (id_collection, id_manager) 
		select id_collection, (select id from user_temp where userID = oldUserID) from collection_manager_temp
		where id_manager = oldUserID;
	 
		SET i = i + 1;
	 END WHILE; 
	 
	# for aidr_collection
	
	select constraint_name 
	from information_schema.key_column_usage 
	where referenced_table_name = 'aidr_user' and table_name = 'aidr_collection' limit 1 into foreignKeyName;

	SET @query = CONCAT('alter table aidr_collection drop FOREIGN KEY ', foreignKeyName, ';');
	PREPARE stmt FROM @query; 
	EXECUTE stmt; 

	ALTER TABLE aidr_collection CHANGE COLUMN user_id user_id BIGINT(20) NOT NULL;
	ALTER TABLE aidr_collection ADD CONSTRAINT fk_collection_account FOREIGN KEY(user_id) REFERENCES account(id);

	# for aidr_collection_manager
	
	select constraint_name 
	from information_schema.key_column_usage 
	where referenced_table_name = 'aidr_user' and table_name = 'aidr_collection_manager' limit 1 into foreignKeyName;

	SET @query = CONCAT('alter table aidr_collection_manager drop FOREIGN KEY ', foreignKeyName, ';');
	PREPARE stmt FROM @query; 
	EXECUTE stmt; 

	ALTER TABLE aidr_collection_manager CHANGE COLUMN id_manager id_manager BIGINT(20) NOT NULL;
	ALTER TABLE aidr_collection_manager ADD CONSTRAINT fk_collection_manager FOREIGN KEY(id_manager) REFERENCES account(id);

	# for crisis

	alter table crisis drop foreign key fk_crisis_users_userID;
	ALTER TABLE crisis CHANGE COLUMN userID userID BIGINT(20) NOT NULL;
	alter table crisis add constraint fk_crisis_users_userID foreign key (userID) 
	references account(id);
	
	# for nominal attribute
	
	alter table nominal_attribute drop foreign key fk_nominalAttribute_users_userID;
	ALTER TABLE nominal_attribute ALTER userID DROP DEFAULT;
	ALTER TABLE nominal_attribute CHANGE COLUMN userID userID BIGINT(20) NOT NULL;
	alter table nominal_attribute add constraint fk_nominalAttribute_account_id foreign key (userID) references account(id);
	
	DROP TEMPORARY TABLE IF EXISTS user_temp;
	DROP TEMPORARY TABLE IF EXISTS collection_temp;
	DROP TEMPORARY TABLE IF EXISTS collection_manager_temp;
	DROP TEMPORARY TABLE IF EXISTS manager_id_temp;
	# enable foreign key checks
	set foreign_key_checks=1;
END
