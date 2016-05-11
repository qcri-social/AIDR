CREATE OR REPLACE FUNCTION add_twitter_trigg_function()
  RETURNS trigger AS
$BODY$
BEGIN
	    IF EXISTS (SELECT 1 FROM twitter_data_feed WHERE "id" = NEW.feed->>'id') THEN
			RETURN null;
		END IF;

		INSERT INTO twitter_data_feed(
			id,
			text,
			media_type,
			media_url,
			location,
			longnitude,
			latitude,
			nominal_label_code,
			nominal_label_name,
			confidence,
			created_at,
			collection_code,
			updated, lang
		)
		VALUES(
			NEW.feed->'id' ,
			NEW.feed->>'text',
			NEW.feed->'entities'->'media'->0->>'type',
			NEW.feed->'entities'->'media'->0->>'media_url',
			NEW.place->>'full_name',
			NEW.geo->'coordinates'->0,
			NEW.geo->'coordinates'->1,
			NEW.aidr->'nominal_labels'->0->>'label_code',
			NEW.aidr->'nominal_labels'->0->>'label_name',
			cast(NEW.aidr->'nominal_labels'->0->>'confidence' AS float) ,
			NEW.feed->>'created_at',
			NEW.code,
			now(),
			NEW.feed->>'lang'
		);

		RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;