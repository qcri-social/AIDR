use aidr_predict;

set global event_scheduler = 1;

DELIMITER $$

CREATE PROCEDURE delete_unassigned_documents()

BEGIN

DECLARE size INT DEFAULT 0;
DECLARE i INT DEFAULT 0;
DECLARE countToDelete BIGINT DEFAULT 0;
DECLARE crisisSelect INT DEFAULT 0;

DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
   DO RELEASE_LOCK('delete_unassigned_documents');
END;

IF GET_LOCK('delete_unassigned_documents', 0) THEN	
 
DROP TEMPORARY TABLE IF EXISTS crisis_count_temp;
CREATE TEMPORARY TABLE crisis_count_temp AS
	SELECT crisisID, count(1) as crisisCount FROM aidr_predict.document d LEFT JOIN aidr_predict.task_assignment t ON d.documentID = t.documentID WHERE !d.hasHumanLabels GROUP BY crisisID;

DELETE FROM crisis_count_temp WHERE crisisCount <= 10000;
UPDATE crisis_count_temp SET crisisCount = crisisCount - 10000;
SELECT count(*) FROM crisis_count_temp INTO size;
CREATE TEMPORARY TABLE docs(docID bigint(20));

WHILE (i < size) DO

SELECT crisisID FROM crisis_count_temp LIMIT i,1 INTO crisisSelect;
SELECT crisisCount FROM crisis_count_temp LIMIT i,1 INTO countToDelete;

INSERT INTO docs
	SELECT d.documentID as docID FROM aidr_predict.document d LEFT JOIN aidr_predict.task_assignment t ON d.documentID = t.documentID WHERE !d.hasHumanLabels and d.crisisID = crisisSelect order by d.valueAsTrainingSample,d.documentID limit countToDelete;

SET i = i + 1;

END WHILE;

DELETE d from aidr_predict.document d join docs where d.documentID = docs.docID;

DROP TEMPORARY TABLE IF EXISTS crisis_count_temp;
DROP TEMPORARY TABLE IF EXISTS docs;

END IF;

DO RELEASE_LOCK('delete_unassigned_documents');

END $$



CREATE EVENT delete_stale_documents 
	ON SCHEDULE EVERY 10 MINUTE 
	DO 
	BEGIN
 
	DELETE d FROM aidr_predict.document d LEFT JOIN aidr_predict.task_assignment t ON d.documentID = t.documentID WHERE !d.hasHumanLabels AND t.documentID IS NULL AND TIMESTAMPDIFF(HOUR, d.receivedAt, now()) > 6;

	DELETE d FROM aidr_predict.document d JOIN aidr_predict.task_assignment t ON d.documentID = t.documentID WHERE !d.hasHumanLabels AND TIMESTAMPDIFF(HOUR, t.assignedAt, now()) > 6;

	CALL delete_unassigned_documents;

END $$

DELIMITER ;
