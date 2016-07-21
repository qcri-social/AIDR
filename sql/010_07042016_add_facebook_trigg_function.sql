CREATE OR REPLACE FUNCTION add_facebook_trigg_function()
  RETURNS trigger AS
$BODY$
BEGIN
	  IF EXISTS (SELECT 1 FROM facebook_data_model WHERE "id" = NEW.fb_id) THEN
			RETURN null;
		END IF;


		INSERT INTO facebook_data_model(
			id,
			collection_code,
			text,
			status_type,
			comment_count,
			like_count,
			share_count,
			link,
			media_story,
			media_title,
			media_url,
			created_at,
			updated
		)
		VALUES(
			NEW.fb_id ,
			NEW.code,
			NEW.feed->>'message',
			NEW.feed->>'statusType',
			CAST(NEW.feed->>'commentsCount' AS INT),
			CAST(NEW.feed->>'likesCount' AS INT),
			CAST(NEW.feed->>'sharesCount' AS INT),
			NEW.feed->>'link',
			NEW.feed->>'description',
			NEW.feed->>'name',
			NEW.feed->>'fullPicture',
			NEW.feed->>'createdTime',
			now()
		);

		RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;