DELIMITER $$
CREATE PROCEDURE aidr_database_clean_up()

BEGIN

--	collection temp table : all those collection older than 2 years, never started and created a month ago or earlier
	DROP TEMPORARY TABLE IF EXISTS collection_temp;
	CREATE TEMPORARY TABLE collection_temp AS
	
		SELECT ac.id, ac.code, ac.createdDate, ac.startDate FROM aidr_predict.aidr_collection ac 
						WHERE ac.createdDate < DATE_SUB(CURDATE(), INTERVAL 2 YEAR) 
						OR (ac.startDate is NULL and ac.createdDate < DATE_SUB(CURDATE(), INTERVAL 1 MONTH));
	
--	all those collections : never stopped and have no history and were created a month ago or earlier
	INSERT INTO collection_temp
		SELECT ac.id, ac.code, ac.createdDate, ac.startDate FROM aidr_predict.aidr_collection ac 
				LEFT JOIN aidr_predict.aidr_collection_log acl ON ac.id = acl.collectionID 
				WHERE ac.id IS NULL AND ac.createdDate < DATE_SUB(CURDATE(), INTERVAL 1 MONTH);
				 
--  delete collection history
	DELETE acl FROM aidr_predict.aidr_collection_log acl JOIN collection_temp ct ON ct.id = acl.collectionID;
	
--	delete collection to manager mapping
	DELETE acm FROM aidr_predict.aidr_collection_manager acm JOIN collection_temp ct ON ct.id = acm.id_collection;
	
--	crisis temp table
	DROP TEMPORARY TABLE IF EXISTS crisis_temp;
	CREATE TEMPORARY TABLE crisis_temp AS
		SELECT c.crisisID FROM aidr_predict.crisis c JOIN collection_temp ct ON c.code = ct.code;
	
--	delete custom_ui_template
	DELETE cui FROM aidr_predict.custom_ui_template cui JOIN crisis_temp ct ON cui.crisisID = ct.crisisID;
	
--	document temp table
	DROP TEMPORARY TABLE IF EXISTS document_temp;
	CREATE TEMPORARY TABLE document_temp AS
		SELECT d.documentID FROM aidr_predict.document d JOIN crisis_temp ct ON d.crisisID = ct.crisisID;
	
--	delete document_nominal_label
	DELETE dnl FROM aidr_predict.document_nominal_label dnl JOIN document_temp dt ON dnl.documentID = dt.documentID;
	
--	model_family temp table
	DROP TEMPORARY TABLE IF EXISTS model_family_temp;
	CREATE TEMPORARY TABLE model_family_temp AS
		SELECT mf.modelFamilyID FROM aidr_predict.model_family mf JOIN crisis_temp ct ON mf.crisisID = ct.crisisID;
	
--	model temp table
	DROP TEMPORARY TABLE IF EXISTS model_temp;
	CREATE TEMPORARY TABLE model_temp AS
		SELECT m.modelID FROM aidr_predict.model m JOIN model_family_temp mft ON mft.modelFamilyID = m.modelFamilyID;
	
--	all those model : having dangling references to model family	
	INSERT INTO model_temp 
		SELECT m.modelID FROM model m LEFT JOIN model_family mf on m.modelFamilyID = mf.modelFamilyID WHERE mf.modelFamilyID IS NULL;
		
--	delete model_nominal_label
	DELETE mnl FROM aidr_predict.model_nominal_label mnl JOIN model_temp mt ON mt.modelID = mnl.modelID;
		
--	delete model
	DELETE m FROM aidr_predict.model m JOIN model_temp mt ON mt.modelID = m.modelID;
	 
--	delete model_family
	DELETE mf FROM aidr_predict.model_family mf JOIN model_family_temp mft ON mft.modelFamilyID = mf.modelFamilyID;
	
--	delete task_answer
	DELETE tans FROM aidr_predict.task_answer tans JOIN document_temp dt ON tans.documentID = dt.documentID;
	
--	delete task_assignment
	DELETE tass FROM aidr_predict.task_assignment tass JOIN document_temp dt ON tass.documentID = dt.documentID;
	
--	delete document 
	DELETE d FROM aidr_predict.document d JOIN document_temp dt ON d.documentID = dt.documentID;
	
--  delete crisis data
	DELETE c FROM aidr_predict.crisis c JOIN crisis_temp ct ON ct.crisisID = c.crisisID;
	
--	delete analytics data
	DELETE td FROM aidr_analysis.tag_data td JOIN collection_temp ct ON ct.code = td.crisis_code; 
	DELETE cd FROM aidr_analysis.conf_data cd JOIN collection_temp ct ON ct.code = cd.crisis_code; 
	
	DELETE td FROM aidr_analysis.tag_data td LEFT JOIN aidr_predict.crisis c ON c.code = td.crisis_code 
		WHERE c.code IS NULL;
	
	DELETE cd FROM aidr_analysis.conf_data cd LEFT JOIN aidr_predict.crisis c ON c.code = cd.crisis_code 
		WHERE c.code IS NULL;
	
--	delete collections
	DELETE c FROM aidr_predict.aidr_collection c JOIN collection_temp ct ON ct.id = c.id;
	
--	drop all temp tables
	DROP TEMPORARY TABLE IF EXISTS model_temp;
	DROP TEMPORARY TABLE IF EXISTS model_family_temp;
	DROP TEMPORARY TABLE IF EXISTS document_temp;
	DROP TEMPORARY TABLE IF EXISTS crisis_temp;
	DROP TEMPORARY TABLE IF EXISTS collection_temp;

END