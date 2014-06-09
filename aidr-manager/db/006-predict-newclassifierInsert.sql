
/** userID = 1 indicates system userID WITH Admin Role. check you user table first **/
/** Informative **/
INSERT INTO `aidr_predict`.`nominal_attribute`
(
`userID`,
`name`,
`description`,
`code`)
VALUES
(
1,
'Informative',
'Indicate if the item contains information that is useful for capturing and understanding the situation on the ground',
'informative');

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Informative', 'Contains useful information that helps you understand the situation', 'informative'
from `aidr_predict`.`nominal_attribute` where code = 'informative' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Not informative', 'Refers to the crisis, but does not contain useful information that helps you understand the situation', 'not_informative'
from `aidr_predict`.`nominal_attribute` where code = 'informative' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Not related to crisis', 'Not related to this crisis', 'not_related'
from `aidr_predict`.`nominal_attribute` where code = 'informative' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'N/A', 'Not applicable, cannot judge, not readable, not sure', 'null'
from `aidr_predict`.`nominal_attribute` where code = 'informative' and userID = 1;

/** Information provided **/
INSERT INTO `aidr_predict`.`nominal_attribute`
(
`userID`,
`name`,
`description`,
`code`)
VALUES
(
1,
'Information provided',
'Indicate what type of information is provided in the message. This is a ranked list. If more than one information type is present, choose the one that appears first on the list',
'information_provided');

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Injured or dead people', 'Casualties due to the crisis', 'casualties'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Missing, trapped, or found people', 'Questions and/or reports about missing or found people', 'missing'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Displaced people', 'People who have relocated due to the crisis, even for a short time (includes evacuations)', 'displaced'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Infrastructure and utilities', 'Buildings or roads damaged or operational; utilities/services interrupted or restored', 'infrastructure'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Shelter and supplies', 'Needs or donations of shelter and/or supplies such as food, water, clothing, medical supplies or blood', 'supplies'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Money', 'Money requested, donated or spent', 'money'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Volunteer or professional services', 'Services needed or offered by volunteers or professionals', 'services'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Animal management', 'Pets and animals, living, missing, displaced, or injured/dead', 'animals'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;


INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Caution and advice', 'Warnings issued or lifted, guidance and tips', 'caution_advice'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;


INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Personal updates', 'Status updates about individuals or loved ones', 'personal'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Sympathy and emotional support', 'Thoughts and prayers', 'sympathy'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Other relevant information', 'Other useful information that helps understand the situation', 'other'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Not related or irrelevant', 'Unrelated to the situation or irrelevant', 'not_related'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'N/A', 'Not applicable, cannot judge, not readable, not sure', 'null'
from `aidr_predict`.`nominal_attribute` where code = 'information_provided' and userID = 1;

/** Humanitarian Clusters **/
INSERT INTO `aidr_predict`.`nominal_attribute`
(
`userID`,
`name`,
`description`,
`code`)
VALUES
(
1,
'Humanitarian Clusters',
'These categories are in line with those put forth by the United Nations "clusters" approach. If more than one category is equally represented in the message, choose "multiple"',
'un_cluster');

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Education/child welfare', 'Children\'s well being and education', 'children'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Telecommunications', 'Mobile and landline networks, internet', 'telecommunications'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Food/nutrition', 'Nutritional well being', 'food'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Health', 'Mental, physical, emotional well being', 'health'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Logistics/transportation', 'Delivery and storage of goods and supplies', 'logistics'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Camp/shelter', 'Condition and location of shelters and camps', 'shelter'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Water, sanitation, hygiene', 'Availability of clean water, waste and sewage disposal, access to hygienic facilities', 'hygiene'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Safety/security', 'Protection of people/property against harm such as violence or theft', 'security'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Multiple', 'More than one of the above are equally represented on this message', 'multiple'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'N/A', 'Not applicable, cannot judge, not readable, not sure', 'null'
from `aidr_predict`.`nominal_attribute` where code = 'un_cluster' and userID = 1;


/**Urgent needs **/
INSERT INTO `aidr_predict`.`nominal_attribute`
(
`userID`,
`name`,
`description`,
`code`)
VALUES
(
1,
'Urgent needs',
'Indicate if the item mentions a need of the affected population that is immediate or short-term. If more than one applies, indicate "multiple"',
'needs');

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Money needed', 'Donations or transfers of money are needed/requested', 'money'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Food and/or water needed', 'Food and/or water are needed', 'food_water'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Clothing needed', 'Clothing, shoes and/or blankets are needed', 'clothing'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Shelter needed', 'Shelter space/beds are needed', 'shelter'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Blood or other medical supplies needed', 'Blood donors or other medical supplies are needed', 'medical'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Services are needed', 'Volunteer or professional services are needed', 'service'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Other type of need', 'Other type of help, supplies, equipment or machines are needed', 'other'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Multiple', 'More than one of the above is mentioned', 'multiple'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Not need-related', 'Not related to a need', 'not_need'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'N/A', 'Not applicable, cannot judge, not readable, not sure', 'null'
from `aidr_predict`.`nominal_attribute` where code = 'needs' and userID = 1;

/** Information source**/
INSERT INTO `aidr_predict`.`nominal_attribute`
(
`userID`,
`name`,
`description`,
`code`)
VALUES
(
1,
'Information source',
'Indicate what the is the apparent source of this information. Click on links when necessary to identify the source',
'information_source');


INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Eye witness', 'Describes an eyewitness account', 'eyewitness'
from `aidr_predict`.`nominal_attribute` where code = 'information_source' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Government', 'National, regional or local government agencies, police, and/or military', 'government'
from `aidr_predict`.`nominal_attribute` where code = 'information_source' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Non-government', 'Non-governmental organizations', 'ngo'
from `aidr_predict`.`nominal_attribute` where code = 'information_source' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Business', 'For-profit business or corporations', 'business'
from `aidr_predict`.`nominal_attribute` where code = 'information_source' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Traditional media', 'Mentions traditional media: television, radio, or newspaper', 'traditional_media'
from `aidr_predict`.`nominal_attribute` where code = 'information_source' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'Internet-only media', 'Mentions websites, blogs or other sites not associated with television, radio or newspapers', 'internet_only_media'
from `aidr_predict`.`nominal_attribute` where code = 'information_source' and userID = 1;

INSERT INTO `aidr_predict`.`nominal_label`
(
`nominalAttributeID`,
`name`,
`description`,
`nominalLabelCode`)
Select nominalAttributeID, 'N/A', 'Not applicable, cannot judge, not readable, not sure', 'null'
from `aidr_predict`.`nominal_attribute` where code = 'information_source' and userID = 1;