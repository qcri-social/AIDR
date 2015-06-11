use aidr_predict;

SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE crisis_nominal_attribute drop foreign key fk_Crisis_NominalAttribute_nominalAttributeID;
ALTER TABLE model_family drop foreign key fk_ModelFamily_NominalAttribute;
ALTER TABLE nominal_label drop foreign key fk_NominalLabel_NominalAttribute_nominalAttributeID;
ALTER TABLE nominal_attribute_dependent_label drop foreign key nomiAtt_depAtt;

ALTER TABLE nominal_attribute MODIFY COLUMN nominalAttributeID bigint(20) unsigned NOT NULL AUTO_INCREMENT;

ALTER TABLE nominal_attribute_dependent_label MODIFY COLUMN nominalAttributeID bigint(20) unsigned NOT NULL;
ALTER TABLE nominal_attribute_dependent_label add constraint nomiAtt_depAtt foreign key (`nominalAttributeID`) REFERENCES `nominal_attribute` (`nominalAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE nominal_label MODIFY COLUMN nominalAttributeID bigint(20) unsigned NOT NULL;
ALTER TABLE nominal_label add constraint fk_NominalLabel_NominalAttribute_nominalAttributeID foreign key (`nominalAttributeID`) REFERENCES `nominal_attribute` (`nominalAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE crisis_nominal_attribute MODIFY COLUMN nominalAttributeID bigint(20) unsigned NOT NULL;
ALTER TABLE crisis_nominal_attribute add constraint fk_Crisis_NominalAttribute_nominalAttributeID foreign key (`nominalAttributeID`) REFERENCES `nominal_attribute` (`nominalAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE model_family MODIFY COLUMN nominalAttributeID bigint(20) unsigned NOT NULL;
ALTER TABLE model_family add constraint fk_ModelFamily_NominalAttribute foreign key (`nominalAttributeID`) REFERENCES `nominal_attribute` (`nominalAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE;



ALTER TABLE document_nominal_label drop foreign key fk_document_nominal_label_nominal_label;
ALTER TABLE model_nominal_label drop foreign key fk_model_nominal_label_nominal_label;
ALTER TABLE nominal_attribute_dependent_label drop foreign key nomiLabel_depLabel;

ALTER TABLE nominal_label MODIFY COLUMN nominalLabelID bigint(20) unsigned NOT NULL AUTO_INCREMENT;

ALTER TABLE nominal_attribute_dependent_label MODIFY COLUMN nominalLabelID bigint(20) unsigned NOT NULL;
ALTER TABLE nominal_attribute_dependent_label add constraint nomiLabel_depLabel foreign key (`nominalLabelID`) REFERENCES `nominal_label` (`nominalLabelID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE document_nominal_label MODIFY COLUMN nominalLabelID bigint(20) unsigned NOT NULL;
ALTER TABLE document_nominal_label add constraint fk_document_nominal_label_nominal_label foreign key (`nominalLabelID`) REFERENCES `nominal_label` (`nominalLabelID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE model_nominal_label MODIFY COLUMN nominalLabelID bigint(20) unsigned NOT NULL;
ALTER TABLE model_nominal_label add constraint fk_model_nominal_label_nominal_label foreign key (`nominalLabelID`) REFERENCES `nominal_label` (`nominalLabelID`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE crisis drop foreign key fk_crisis_users_userID;
ALTER TABLE nominal_attribute drop foreign key fk_nominalAttribute_users_userID;

ALTER TABLE users MODIFY COLUMN userID bigint(20) unsigned NOT NULL AUTO_INCREMENT;

ALTER TABLE crisis MODIFY COLUMN userID bigint(20) unsigned NOT NULL;
ALTER TABLE crisis add constraint fk_crisis_users_userID foreign key (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE nominal_attribute MODIFY COLUMN userID bigint(20) unsigned NOT NULL;
ALTER TABLE nominal_attribute add constraint fk_nominalAttribute_users_userID foreign key (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE document_nominal_label MODIFY COLUMN userID bigint(20) unsigned NOT NULL;
ALTER TABLE task_answer MODIFY COLUMN userID bigint(20) unsigned NOT NULL;
ALTER TABLE task_assignment MODIFY COLUMN userID bigint(20) unsigned NOT NULL;

SET FOREIGN_KEY_CHECKS = 1;