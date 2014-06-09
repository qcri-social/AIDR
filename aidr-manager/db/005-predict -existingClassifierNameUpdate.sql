/**  userID = 83 is non system userID. You can use any of non-system userID instead **/
UPDATE nominal_attribute
SET name = 'Clusters (v1)', userID = 83
where userID =1 AND code = 'unclusters_v1';

UPDATE nominal_attribute 
SET name = 'Eye witness (v1)', userID = 83
where userID =1 AND code = 'eyewitness_v1';

UPDATE nominal_attribute 
SET name = 'Informative (v1)', userID = 83
where userID =1 AND code = 'informative_v1';

UPDATE nominal_attribute 
SET name = 'Type (v1)', userID = 83
where userID =1 AND code = 'type_v1';

UPDATE nominal_attribute 
SET name = 'Multimedia (v1)', userID = 83
where userID =1 AND code = 'media_v1';

UPDATE nominal_attribute 
SET name = 'Individual needs (v1)', userID = 83
where userID =1 AND code = 'needs_v1';
