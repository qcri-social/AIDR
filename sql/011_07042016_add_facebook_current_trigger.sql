CREATE TRIGGER add_facebook_current_trigger
AFTER INSERT
ON facebook_data_feed
FOR EACH ROW
EXECUTE PROCEDURE add_facebook_trigg_function();
