CREATE TRIGGER add_twitter_current_trigger
AFTER INSERT
ON data_feed
FOR EACH ROW
EXECUTE PROCEDURE add_twitter_trigg_function();
