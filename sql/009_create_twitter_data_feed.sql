CREATE TABLE "public"."twitter_data_feed" (
	"id" varchar(250) NOT NULL COLLATE "default",
	"text" text NOT NULL COLLATE "default",
	"media_type" varchar(100) COLLATE "default",
	"media_url" varchar(1000) COLLATE "default",
	"location" varchar(250) COLLATE "default",
	"longnitude" varchar(100) COLLATE "default",
	"latitude" varchar(100) COLLATE "default",
	"nominal_label_code" varchar(100) COLLATE "default",
	"nominal_label_name" varchar(100) COLLATE "default",
	"confidence" numeric(20,5),
	"created_at" varchar(100) COLLATE "default",
	"collection_code" varchar(100) NOT NULL COLLATE "default",
	"updated" timestamp(6) NULL,
	"lang" varchar(20) COLLATE "default",
	CONSTRAINT "twitter_data_feed_pkey" PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."twitter_data_feed" OWNER TO "aidr_admin";