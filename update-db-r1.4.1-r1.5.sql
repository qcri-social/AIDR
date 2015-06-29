DROP TRIGGER IF EXISTS `document_BINS`;

# -- update sort orders for all standard classifiers
update nominal_label set sequence = nominalLabelID where nominalAttributeID in (select nominalAttributeID from nominal_attribute where userid=1) and sequence=100;

# -- update sort order for all 'null' classifiers to be 999
update nominal_label set sequence = 999 WHERE nominalLabelCode = 'null' and nominalAttributeID in (select nominalAttributeID from nominal_attribute where userid !=1);
update nominal_label set name='Cannot judge',description='Not readable or not sure how to classify' where nominalLabelCode='null' and nominalAttributeID in (select nominalAttributeID from nominal_attribute where userid=1);


# -- adding new classifiers
INSERT INTO nominal_attribute (`nominalAttributeID`,`userID`, `name`, `description`, `code`)
VALUES
	(630.1,'Information provided (simple)','What does this tweet refers to?','information_provided_short'),
	(631,1,'Actionable Information','Classify messages containing actionable information on these categories.','actionable_information'),
	(632,1,'Humanitarian Information','Indicate what information relevant to humanitarian relief is contained on this tweet.','humanitarian_information');

INSERT INTO nominal_label (`nominalLabelCode`, `nominalAttributeID`, `name`, `description`, `sequence`)
VALUES
	('casualties',630,'Injured/Dead','Casualties',101),
	('damage',630,'Damage','Buildings or roads damaged or operational; utilities/services interrupted or restored',102),
	('shelter',630,'Shelter','Needs of shelter, or shelter is available',103),
	('food_water',630,'Food/Water','Needs food/water, or able to provide food/water',104),
	('other_useful',630,'Useful Information','Other useful information that helps understand the situation',105),
	('other',630,'None of the above','Not related, irrelevant, or not sure how to classify',106),
	('null',630,'Cannot judge','Not readable or not sure how to classify',999),

	('casualties',631,'Injured or dead people','Casualties due to the crisis',101),
	('missing',631,'Missing, trapped, or found people','Questions and/or reports about missing or found people',102),
	('infrastructure',631,'Infrastructure and utilities','Buildings damaged or operational; utilities/services interrupted or restored',103),
	('roads',631,'Roads','Including streets, bridges and highways, damaged, closed, or re-opened',104),
	('needs',631,'Shelter or supplies needed','Needs of shelter or supplies such as food, water, clothing, medical supplies or blood',105),
	('response',631,'Relief and response efforts','Shelter, supplies, or other services being offered, provided, or received, reports of emergency relief actions',106),
	('personal',631,'Personal updates','Status updates about individuals or loved ones',107),
	('non_english',631,'Not in English','For translation purposes, use this label even if you are able to read/understand the message',108),
	('other',631,'None of the above','Messages not corresponding to the categories above',109),
	('null',631,'Cannot judge','Not readable or not sure how to classify',999),

	('needs',632,'Needs and requests for help','People needing food, water, medical attention, rescue, or other necessities, except shelter',101),
	('shelter',632,'Population displacement','People forced to leave their home, or needing shelter',102),
	('infrastructure',632,'Infrastructure and utilities','Buildings damaged or operational; utilities/services interrupted or restored',103),
	('response',632,'Relief and response efforts','Humanitarian aid being given, offered, or received, reports of emergency relief actions',104),	
	('other_useful_hum',632,'Other useful for response','Information that could be useful for emergency response and humanitarian organizations',105),
	('not_hum_relevant',632,'Not relevant','Not relevant for emergency response or humanitarian organizations',106),	
	('null',632,'Cannot judge','Not readable or not sure how to classify',999);



