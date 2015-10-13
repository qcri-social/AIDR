DELIMITER $$
CREATE PROCEDURE aidr_user_migration()

BEGIN

	DECLARE i INT DEFAULT 0;
	DECLARE size INT DEFAULT 0;
	DECLARE accountID BIGINT DEFAULT 0;
	DECLARE oldUserID BIGINT DEFAULT 0;
	DECLARE foreignKeyName varchar(255);

	# disable foreign key checks	
	set foreign_key_checks=0;
	
	# copy data from aidr_user to account table
	insert into account(id, provider, user_name, created_at, updated_at, api_key, locale) 
	select id, provider, user_name, now(), now(), uuid(), 'en' from aidr_user;

	# insert admin role
	insert into role(id, created_at, updated_at, level, description) values (1, now(), now(), 'ADMIN', 'Admin role allows users to manage collections, users, and roles.');
	
	# copy data from user_role to account_role table
	insert into account_role (account_id, role_id, created_at, updated_at) 
	select user_id, 1, now(), now() from user_role;
	
	# insert system user
	insert into account(created_at, updated_at, api_key, user_name) values (now(), now(), uuid(), 'System');
	
	# create temp table for account id and users id 
	DROP TEMPORARY TABLE IF EXISTS user_temp;
	CREATE TEMPORARY TABLE user_temp AS select a.id, u.userID from account a, users u where a.user_name = u.name;

	select count(1) from user_temp into size;

	# update the tables with account id 
	WHILE (i < size) DO
	 SELECT id FROM user_temp LIMIT i,1 INTO accountID;
	 SELECT userID FROM user_temp LIMIT i,1 INTO oldUserID;
	 
	 update crisis set userID = accountID where userID = oldUserID;
	 update nominal_attribute set userID = accountID where userID = oldUserID;
	 update document_nominal_label set userID = accountID where userID = oldUserID;
	 update task_answer set userID = accountID where userID = oldUserID;
	 update task_assignment set userID = accountID where userID = oldUserID;
	 
	 
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
	
	# enable foreign key checks
	set foreign_key_checks=1;
END
