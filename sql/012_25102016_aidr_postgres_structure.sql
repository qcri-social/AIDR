/*
 Navicat PostgreSQL Data Transfer

 Source Server         : aidr_meeza_prod
 Source Server Version : 90409
 Source Host           : localhost
 Source Database       : aidr
 Source Schema         : public

 Target Server Version : 90409
 File Encoding         : utf-8

 Date: 10/25/2016 09:31:32 AM
*/

-- ----------------------------
--  Sequence structure for client_app_deployment_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."client_app_deployment_id_seq";
CREATE SEQUENCE "public"."client_app_deployment_id_seq" INCREMENT 1 START 3 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."client_app_deployment_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for client_app_event_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."client_app_event_id_seq";
CREATE SEQUENCE "public"."client_app_event_id_seq" INCREMENT 1 START 21 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."client_app_event_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for client_app_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."client_app_id_seq";
CREATE SEQUENCE "public"."client_app_id_seq" INCREMENT 1 START 2553 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."client_app_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for client_app_source_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."client_app_source_id_seq";
CREATE SEQUENCE "public"."client_app_source_id_seq" INCREMENT 1 START 115834 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."client_app_source_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for client_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."client_id_seq";
CREATE SEQUENCE "public"."client_id_seq" INCREMENT 1 START 5 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."client_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for crisis_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."crisis_id_seq";
CREATE SEQUENCE "public"."crisis_id_seq" INCREMENT 1 START 2656 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."crisis_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for data_feed_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."data_feed_id_seq";
CREATE SEQUENCE "public"."data_feed_id_seq" INCREMENT 1 START 69037423 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."data_feed_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for facebook_data_feed_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."facebook_data_feed_id_seq";
CREATE SEQUENCE "public"."facebook_data_feed_id_seq" INCREMENT 1 START 15115 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."facebook_data_feed_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for filtered_task_run_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."filtered_task_run_id_seq";
CREATE SEQUENCE "public"."filtered_task_run_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."filtered_task_run_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for hibernate_sequence
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."hibernate_sequence";
CREATE SEQUENCE "public"."hibernate_sequence" INCREMENT 1 START 2704 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."hibernate_sequence" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for image_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."image_config_id_seq";
CREATE SEQUENCE "public"."image_config_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."image_config_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for image_feed_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."image_feed_id_seq";
CREATE SEQUENCE "public"."image_feed_id_seq" INCREMENT 1 START 170624 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."image_feed_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for image_meta_data_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."image_meta_data_id_seq";
CREATE SEQUENCE "public"."image_meta_data_id_seq" INCREMENT 1 START 3443 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."image_meta_data_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for image_task_queue_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."image_task_queue_id_seq";
CREATE SEQUENCE "public"."image_task_queue_id_seq" INCREMENT 1 START 2738 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."image_task_queue_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for marker_style_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."marker_style_id_seq";
CREATE SEQUENCE "public"."marker_style_id_seq" INCREMENT 1 START 1930 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."marker_style_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for namibia_image_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."namibia_image_id_seq";
CREATE SEQUENCE "public"."namibia_image_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."namibia_image_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for namibia_report_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."namibia_report_id_seq";
CREATE SEQUENCE "public"."namibia_report_id_seq" INCREMENT 1 START 5445 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."namibia_report_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for news_image_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."news_image_id_seq";
CREATE SEQUENCE "public"."news_image_id_seq" INCREMENT 1 START 15302 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."news_image_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for pam_report_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."pam_report_id_seq";
CREATE SEQUENCE "public"."pam_report_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."pam_report_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for report_template_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."report_template_id_seq";
CREATE SEQUENCE "public"."report_template_id_seq" INCREMENT 1 START 38179 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."report_template_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for sliced_image_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sliced_image_id_seq";
CREATE SEQUENCE "public"."sliced_image_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."sliced_image_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for source_image_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."source_image_id_seq";
CREATE SEQUENCE "public"."source_image_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."source_image_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for task_queue_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."task_queue_id_seq";
CREATE SEQUENCE "public"."task_queue_id_seq" INCREMENT 1 START 399977 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."task_queue_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for task_run_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."task_run_id_seq";
CREATE SEQUENCE "public"."task_run_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."task_run_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for task_translation_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."task_translation_id_seq";
CREATE SEQUENCE "public"."task_translation_id_seq" INCREMENT 1 START 62446 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."task_translation_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Sequence structure for userconnection_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."userconnection_id_seq";
CREATE SEQUENCE "public"."userconnection_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."userconnection_id_seq" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for client_app_answer
-- ----------------------------
DROP TABLE IF EXISTS "public"."client_app_answer";
CREATE TABLE "public"."client_app_answer" (
	"client_app_id" int8 NOT NULL,
	"answer" text NOT NULL COLLATE "default",
	"vote_cut_off" int4 NOT NULL DEFAULT 3,
	"created" timestamp(6) NULL DEFAULT now(),
	"active_answer_key" text COLLATE "default",
	"answerMarkerInfo" text COLLATE "default",
	"answer_marker_info" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."client_app_answer" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for client_app_deployment
-- ----------------------------
DROP TABLE IF EXISTS "public"."client_app_deployment";
CREATE TABLE "public"."client_app_deployment" (
	"id" int8 NOT NULL DEFAULT nextval('client_app_deployment_id_seq'::regclass),
	"name" varchar(100) COLLATE "default",
	"status" int4 NOT NULL,
	"updated" timestamp(6) NULL DEFAULT now(),
	"client_app_id" int8
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."client_app_deployment" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for client_app_event
-- ----------------------------
DROP TABLE IF EXISTS "public"."client_app_event";
CREATE TABLE "public"."client_app_event" (
	"id" int8 NOT NULL DEFAULT nextval('client_app_event_id_seq'::regclass),
	"name" varchar(45) NOT NULL COLLATE "default",
	"client_app_id" int8 NOT NULL,
	"sequence" int4 NOT NULL,
	"event_id" int8 NOT NULL,
	"created" timestamp(6) NULL DEFAULT now()
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."client_app_event" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for client_app_source
-- ----------------------------
DROP TABLE IF EXISTS "public"."client_app_source";
CREATE TABLE "public"."client_app_source" (
	"id" int8 NOT NULL DEFAULT nextval('client_app_source_id_seq'::regclass),
	"client_app_id" int8 NOT NULL,
	"source_url" varchar(200) NOT NULL COLLATE "default",
	"status" int4 NOT NULL,
	"created" timestamp(6) NULL DEFAULT now()
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."client_app_source" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS "public"."client";
CREATE TABLE "public"."client" (
	"id" int8 NOT NULL DEFAULT nextval('client_id_seq'::regclass),
	"aidr_user_id" numeric(20,0),
	"name" varchar(100) NOT NULL COLLATE "default",
	"host_url" varchar(200) NOT NULL COLLATE "default",
	"host_api_key" varchar(200) COLLATE "default",
	"description" varchar(500) COLLATE "default",
	"queue_size" int8 NOT NULL,
	"aidr_host_url" varchar(100) COLLATE "default",
	"default_task_run_per_task" int8 NOT NULL,
	"status" int8 NOT NULL,
	"created" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."client" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for filtered_task_run
-- ----------------------------
DROP TABLE IF EXISTS "public"."filtered_task_run";
CREATE TABLE "public"."filtered_task_run" (
	"id" int4 NOT NULL DEFAULT nextval('filtered_task_run_id_seq'::regclass),
	"created" timestamp(6) NULL,
	"app_id" int4,
	"task_id" int4,
	"user_id" int4,
	"user_ip" varchar(100) COLLATE "default",
	"finish_time" varchar(250) COLLATE "default",
	"timeout" int4,
	"calibration" int4,
	"info" text COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."filtered_task_run" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for image_feed
-- ----------------------------
DROP TABLE IF EXISTS "public"."image_feed";
CREATE TABLE "public"."image_feed" (
	"id" int8 NOT NULL DEFAULT nextval('image_feed_id_seq'::regclass),
	"created_at" timestamp(6) NOT NULL,
	"updated_at" timestamp(6) NOT NULL,
	"collection_code" varchar(255) COLLATE "default",
	"collection_feed_id" int8,
	"image_url" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."image_feed" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for image_meta_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."image_meta_data";
CREATE TABLE "public"."image_meta_data" (
	"id" int4 NOT NULL DEFAULT nextval('image_meta_data_id_seq'::regclass),
	"path" varchar(200) COLLATE "default",
	"file_name" varchar(150) NOT NULL COLLATE "default",
	"thumbnail" varchar(150) COLLATE "default",
	"lat" varchar(100) COLLATE "default",
	"lng" varchar(100) COLLATE "default",
	"bounds" text COLLATE "default",
	"source_type" int4 NOT NULL,
	"status" int8 NOT NULL,
	"created" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."image_meta_data" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for lookup
-- ----------------------------
DROP TABLE IF EXISTS "public"."lookup";
CREATE TABLE "public"."lookup" (
	"lookupID" numeric(20,0) NOT NULL,
	"tableName" varchar(45) NOT NULL COLLATE "default",
	"columnName" varchar(45) NOT NULL COLLATE "default",
	"columnValue" int4 NOT NULL,
	"description" varchar(200) NOT NULL COLLATE "default",
	"created" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."lookup" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for marker_style
-- ----------------------------
DROP TABLE IF EXISTS "public"."marker_style";
CREATE TABLE "public"."marker_style" (
	"id" int8 NOT NULL DEFAULT nextval('marker_style_id_seq'::regclass),
	"client_app_id" int8,
	"app_type" varchar(25) NOT NULL COLLATE "default",
	"style" text NOT NULL COLLATE "default",
	"is_default" bool NOT NULL DEFAULT false
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."marker_style" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for namibia_image
-- ----------------------------
DROP TABLE IF EXISTS "public"."namibia_image";
CREATE TABLE "public"."namibia_image" (
	"id" int8 NOT NULL DEFAULT nextval('namibia_image_id_seq'::regclass),
	"animal_found" int4 NOT NULL,
	"grid_image" varchar(255) NOT NULL COLLATE "default",
	"path" varchar(255) NOT NULL COLLATE "default",
	"source" varchar(255) NOT NULL COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."namibia_image" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for namibia_report
-- ----------------------------
DROP TABLE IF EXISTS "public"."namibia_report";
CREATE TABLE "public"."namibia_report" (
	"id" int4 NOT NULL DEFAULT nextval('namibia_report_id_seq'::regclass),
	"task_id" int8 NOT NULL,
	"geo" text COLLATE "default",
	"source_image" varchar(200) COLLATE "default",
	"sliced_image" varchar(200) COLLATE "default",
	"found_count" int4,
	"no_found_count" int4
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."namibia_report" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for namibia_report_reprocess
-- ----------------------------
DROP TABLE IF EXISTS "public"."namibia_report_reprocess";
CREATE TABLE "public"."namibia_report_reprocess" (
	"id" int8 NOT NULL,
	"found_count" int4 NOT NULL,
	"geo" text COLLATE "default",
	"no_found_count" int4 NOT NULL,
	"sliced_image" varchar(255) NOT NULL COLLATE "default",
	"source_image" varchar(255) NOT NULL COLLATE "default",
	"task_id" int8 NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."namibia_report_reprocess" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for news_image
-- ----------------------------
DROP TABLE IF EXISTS "public"."news_image";
CREATE TABLE "public"."news_image" (
	"id" int8 NOT NULL DEFAULT nextval('news_image_id_seq'::regclass),
	"article_link" varchar(255) COLLATE "default",
	"client_app_id" int8,
	"created" varchar(255) COLLATE "default",
	"crisis_code" varchar(255) COLLATE "default",
	"image_url" varchar(255) COLLATE "default",
	"language" varchar(255) COLLATE "default",
	"latitude" varchar(255) COLLATE "default",
	"location" varchar(255) COLLATE "default",
	"longitude" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."news_image" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for nimibia_image
-- ----------------------------
DROP TABLE IF EXISTS "public"."nimibia_image";
CREATE TABLE "public"."nimibia_image" (
	"path" varchar(150) COLLATE "default",
	"source" varchar(100) COLLATE "default",
	"grid_image" varchar(100) NOT NULL COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."nimibia_image" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for pam_report
-- ----------------------------
DROP TABLE IF EXISTS "public"."pam_report";
CREATE TABLE "public"."pam_report" (
	"id" int8 NOT NULL DEFAULT nextval('pam_report_id_seq'::regclass),
	"damage_type" varchar(255) NOT NULL COLLATE "default",
	"geo" varchar(255) NOT NULL COLLATE "default",
	"img_url" varchar(255) NOT NULL COLLATE "default",
	"lat" varchar(255) NOT NULL COLLATE "default",
	"lng" varchar(255) NOT NULL COLLATE "default",
	"task_id" varchar(255) NOT NULL COLLATE "default",
	"user_id" varchar(255) NOT NULL COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."pam_report" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for facebook_data_feed
-- ----------------------------
DROP TABLE IF EXISTS "public"."facebook_data_feed";
CREATE TABLE "public"."facebook_data_feed" (
	"id" int8 NOT NULL DEFAULT nextval('facebook_data_feed_id_seq'::regclass),
	"created_at" timestamp(6) NOT NULL,
	"updated_at" timestamp(6) NOT NULL,
	"aidr" jsonb,
	"code" varchar(64) COLLATE "default",
	"fb_id" varchar(64) COLLATE "default",
	"feed" jsonb,
	"feed_created_at" timestamp(6) NULL,
	"parent_type" varchar(64) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."facebook_data_feed" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for facebook_data_model
-- ----------------------------
DROP TABLE IF EXISTS "public"."facebook_data_model";
CREATE TABLE "public"."facebook_data_model" (
	"id" varchar(200) NOT NULL COLLATE "default",
	"collection_code" varchar(100) NOT NULL COLLATE "default",
	"status_type" varchar(200) COLLATE "default",
	"link" varchar(500) COLLATE "default",
	"text" text COLLATE "default",
	"media_story" text COLLATE "default",
	"media_title" varchar(500) COLLATE "default",
	"media_url" varchar(1000) COLLATE "default",
	"location" varchar(250) COLLATE "default",
	"longnitude" varchar(100) COLLATE "default",
	"latitude" varchar(100) COLLATE "default",
	"created_at" varchar(100) NOT NULL COLLATE "default",
	"updated" timestamp(6) NULL,
	"like_count" int4 DEFAULT 0,
	"comment_count" int4 DEFAULT 0,
	"share_count" int4 DEFAULT 0
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."facebook_data_model" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for report_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."report_template";
CREATE TABLE "public"."report_template" (
	"id" int8 NOT NULL DEFAULT nextval('report_template_id_seq'::regclass),
	"client_app_id" int8 NOT NULL,
	"task_queue_id" int4 NOT NULL,
	"task_id" int8 NOT NULL,
	"tweet_id" varchar(500) NOT NULL COLLATE "default",
	"tweet" text NOT NULL COLLATE "default",
	"author" varchar(100) COLLATE "default",
	"lat" varchar(100) COLLATE "default",
	"lon" varchar(100) COLLATE "default",
	"url" varchar(500) COLLATE "default",
	"created" varchar(150) COLLATE "default",
	"answer" text NOT NULL COLLATE "default",
	"status" int4 NOT NULL,
	"updated" timestamp(6) NULL DEFAULT now()
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."report_template" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for report_template_typhoon_ruby
-- ----------------------------
DROP TABLE IF EXISTS "public"."report_template_typhoon_ruby";
CREATE TABLE "public"."report_template_typhoon_ruby" (
	"id" numeric(20,0) NOT NULL,
	"client_app_id" numeric(20,0) NOT NULL,
	"task_queue_id" int8 NOT NULL,
	"task_id" int8 NOT NULL,
	"tweet_id" varchar(500) NOT NULL COLLATE "default",
	"tweet" text NOT NULL COLLATE "default",
	"author" varchar(100) COLLATE "default",
	"lat" varchar(100) COLLATE "default",
	"lon" varchar(100) COLLATE "default",
	"url" varchar(500) COLLATE "default",
	"created" varchar(150) COLLATE "default",
	"answer" text NOT NULL COLLATE "default",
	"status" int4 NOT NULL,
	"updated" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."report_template_typhoon_ruby" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for twitter_data_feed
-- ----------------------------
DROP TABLE IF EXISTS "public"."twitter_data_feed";
CREATE TABLE "public"."twitter_data_feed" (
	"id" int8 NOT NULL DEFAULT nextval('data_feed_id_seq'::regclass),
	"aidr" jsonb,
	"code" varchar(64) COLLATE "default",
	"created_at" timestamp(6) NOT NULL,
	"feed" jsonb,
	"geo" jsonb,
	"place" jsonb,
	"source" varchar(45) COLLATE "default",
	"updated_at" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."twitter_data_feed" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for twitter_data_model
-- ----------------------------
DROP TABLE IF EXISTS "public"."twitter_data_model";
CREATE TABLE "public"."twitter_data_model" (
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
	"lang" varchar(20) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."twitter_data_model" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for task_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."task_log";
CREATE TABLE "public"."task_log" (
	"task_queue_id" int8 NOT NULL,
	"status" int4 NOT NULL,
	"created" timestamp(6) NULL DEFAULT now()
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."task_log" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for task_queue_response
-- ----------------------------
DROP TABLE IF EXISTS "public"."task_queue_response";
CREATE TABLE "public"."task_queue_response" (
	"task_queue_id" int8 NOT NULL,
	"response" text NOT NULL COLLATE "default",
	"task_info" text COLLATE "default",
	"created" timestamp(6) NULL DEFAULT now(),
	"exported" bool DEFAULT false
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."task_queue_response" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for task_run
-- ----------------------------
DROP TABLE IF EXISTS "public"."task_run";
CREATE TABLE "public"."task_run" (
	"id" int8 NOT NULL DEFAULT nextval('task_run_id_seq'::regclass),
	"created" timestamp(6) NOT NULL,
	"duplicate_info" text COLLATE "default",
	"info" text COLLATE "default",
	"task_id" int8 NOT NULL,
	"update_info" text COLLATE "default",
	"user_id" int8 NOT NULL,
	"user_ip" varchar(255) NOT NULL COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."task_run" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for task_translation
-- ----------------------------
DROP TABLE IF EXISTS "public"."task_translation";
CREATE TABLE "public"."task_translation" (
	"id" int8 NOT NULL DEFAULT nextval('task_translation_id_seq'::regclass),
	"task_id" int8,
	"task_queue_id" int8,
	"client_app_id" int8,
	"twb_order_id" int8,
	"tweet_id" varchar(100) NOT NULL COLLATE "default",
	"original_text" text COLLATE "default",
	"translated_text" text COLLATE "default",
	"author" varchar(100) COLLATE "default",
	"lat" varchar(100) COLLATE "default",
	"lon" varchar(100) COLLATE "default",
	"url" varchar(300) COLLATE "default",
	"answer_code" varchar(100) COLLATE "default",
	"status" varchar(30) NOT NULL DEFAULT 0 COLLATE "default",
	"created" timestamp(6) NULL DEFAULT now(),
	"updated" timestamp(6) NULL DEFAULT now(),
	"document_id" int8
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."task_translation" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for task_queue
-- ----------------------------
DROP TABLE IF EXISTS "public"."task_queue";
CREATE TABLE "public"."task_queue" (
	"id" int8 NOT NULL DEFAULT nextval('task_queue_id_seq'::regclass),
	"task_id" int8,
	"client_app_id" int8 NOT NULL,
	"document_id" int8,
	"client_app_source_id" int8,
	"status" int4 NOT NULL,
	"created" timestamp(6) NULL DEFAULT now(),
	"updated" timestamp(6) NULL DEFAULT now()
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."task_queue" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for typhoon_ruby_text_geo_clicker
-- ----------------------------
DROP TABLE IF EXISTS "public"."typhoon_ruby_text_geo_clicker";
CREATE TABLE "public"."typhoon_ruby_text_geo_clicker" (
	"task_id" int8 NOT NULL,
	"author" varchar(255) COLLATE "default",
	"final_tweet_id" varchar(255) COLLATE "default",
	"tweet" varchar(255) COLLATE "default",
	"tweet_id" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."typhoon_ruby_text_geo_clicker" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS "public"."role";
CREATE TABLE "public"."role" (
	"id" int8 NOT NULL,
	"created_at" timestamp(6) NOT NULL,
	"updated_at" timestamp(6) NOT NULL,
	"description" varchar(255) COLLATE "default",
	"level" varchar(255) NOT NULL COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."role" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for userconnection
-- ----------------------------
DROP TABLE IF EXISTS "public"."userconnection";
CREATE TABLE "public"."userconnection" (
	"id" int4 NOT NULL DEFAULT nextval('userconnection_id_seq'::regclass),
	"accesstoken" varchar(255) COLLATE "default",
	"displayname" varchar(255) COLLATE "default",
	"expiretime" int8,
	"imageurl" varchar(255) COLLATE "default",
	"profileurl" varchar(255) COLLATE "default",
	"providerid" varchar(255) COLLATE "default",
	"provideruserid" varchar(255) COLLATE "default",
	"rank" int4,
	"refreshtoken" varchar(255) COLLATE "default",
	"secret" varchar(255) COLLATE "default",
	"userid" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."userconnection" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for crisis
-- ----------------------------
DROP TABLE IF EXISTS "public"."crisis";
CREATE TABLE "public"."crisis" (
	"id" int8 NOT NULL DEFAULT nextval('crisis_id_seq'::regclass),
	"collection_id" int8,
	"client_app_id" int8,
	"display_name" varchar(100) NOT NULL COLLATE "default",
	"description" varchar(500) COLLATE "default",
	"activation_start" timestamp(6) NULL,
	"activation_end" timestamp(6) NULL,
	"clicker_type" varchar(100) COLLATE "default",
	"created" timestamp(6) NULL DEFAULT now(),
	"bounds" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."crisis" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for sliced_image
-- ----------------------------
DROP TABLE IF EXISTS "public"."sliced_image";
CREATE TABLE "public"."sliced_image" (
	"id" int8 NOT NULL DEFAULT nextval('sliced_image_id_seq'::regclass),
	"bounds" varchar(255) COLLATE "default",
	"created_at" timestamp(6) NULL,
	"lat" varchar(255) COLLATE "default",
	"lon" varchar(255) COLLATE "default",
	"sliced_file_url" varchar(255) COLLATE "default",
	"thumbnail" varchar(255) COLLATE "default",
	"source_image_id" int8
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."sliced_image" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for client_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."client_app";
CREATE TABLE "public"."client_app" (
	"id" int8 NOT NULL DEFAULT nextval('client_app_id_seq'::regclass),
	"client_id" int8 NOT NULL,
	"crisis_id" int8,
	"nominal_attribute_id" int8,
	"platform_app_id" int8 NOT NULL,
	"name" varchar(100) NOT NULL COLLATE "default",
	"description" varchar(200) NOT NULL COLLATE "default",
	"short_name" varchar(100) NOT NULL COLLATE "default",
	"task_run_per_task" int4 NOT NULL,
	"quorum" int4 NOT NULL,
	"icon_url" varchar(200) COLLATE "default",
	"status" int2 NOT NULL,
	"created" timestamp(6) NULL DEFAULT now(),
	"app_type" int4,
	"is_custom" bool NOT NULL DEFAULT false,
	"tc_project_id" int4
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."client_app" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS "public"."account";
CREATE TABLE "public"."account" (
	"id" int8 NOT NULL,
	"created_at" timestamp(6) NOT NULL,
	"updated_at" timestamp(6) NOT NULL,
	"api_key" varchar(255) NOT NULL COLLATE "default",
	"email" varchar(255) COLLATE "default",
	"locale" varchar(255) COLLATE "default",
	"provider" varchar(255) COLLATE "default",
	"user_name" varchar(255) NOT NULL COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."account" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for image_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."image_config";
CREATE TABLE "public"."image_config" (
	"id" int8 NOT NULL DEFAULT nextval('image_config_id_seq'::regclass),
	"columns" int4,
	"created_at" timestamp(6) NULL,
	"rows" int4,
	"source" varchar(255) COLLATE "default",
	"client_app_id" int8
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."image_config" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for account_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."account_role";
CREATE TABLE "public"."account_role" (
	"id" int8 NOT NULL,
	"created_at" timestamp(6) NOT NULL,
	"updated_at" timestamp(6) NOT NULL,
	"account_id" int8 NOT NULL,
	"role_id" int8 NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."account_role" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for source_image
-- ----------------------------
DROP TABLE IF EXISTS "public"."source_image";
CREATE TABLE "public"."source_image" (
	"id" int8 NOT NULL DEFAULT nextval('source_image_id_seq'::regclass),
	"created_at" timestamp(6) NULL,
	"file_name" varchar(255) COLLATE "default",
	"image_config_id" int8
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."source_image" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for data_model_image_export
-- ----------------------------
DROP TABLE IF EXISTS "public"."data_model_image_export";
CREATE TABLE "public"."data_model_image_export" (
	"collection_code" varchar(150) COLLATE "default",
	"text" text COLLATE "default",
	"created_at" varchar(200) COLLATE "default",
	"location" varchar(150) COLLATE "default",
	"latitude" varchar(100) COLLATE "default",
	"longnitude" varchar(100) COLLATE "default",
	"media_url" text COLLATE "default",
	"id" varchar(200) COLLATE "default",
	"status" int2
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."data_model_image_export" OWNER TO "aidr_admin";

-- ----------------------------
--  Table structure for image_task_queue
-- ----------------------------
DROP TABLE IF EXISTS "public"."image_task_queue";
CREATE TABLE "public"."image_task_queue" (
	"task_queue_id" int4,
	"pybossa_task_id" int4,
	"image_url" text COLLATE "default",
	"image_text" text COLLATE "default",
	"category" varchar(100) COLLATE "default",
	"lat" varchar(150) COLLATE "default",
	"lon" varchar(150) COLLATE "default",
	"location" varchar(250) COLLATE "default",
	"id" int4 NOT NULL DEFAULT nextval('image_task_queue_id_seq'::regclass)
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."image_task_queue" OWNER TO "aidr_admin";


-- ----------------------------
--  Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."client_app_deployment_id_seq" RESTART 4 OWNED BY "client_app_deployment"."id";
ALTER SEQUENCE "public"."client_app_event_id_seq" RESTART 22 OWNED BY "client_app_event"."id";
ALTER SEQUENCE "public"."client_app_id_seq" RESTART 2554 OWNED BY "client_app"."id";
ALTER SEQUENCE "public"."client_app_source_id_seq" RESTART 115835 OWNED BY "client_app_source"."id";
ALTER SEQUENCE "public"."client_id_seq" RESTART 6;
ALTER SEQUENCE "public"."crisis_id_seq" RESTART 2657 OWNED BY "crisis"."id";
ALTER SEQUENCE "public"."data_feed_id_seq" RESTART 69037424 OWNED BY "twitter_data_feed"."id";
ALTER SEQUENCE "public"."facebook_data_feed_id_seq" RESTART 15116 OWNED BY "facebook_data_feed"."id";
ALTER SEQUENCE "public"."filtered_task_run_id_seq" RESTART 2;
ALTER SEQUENCE "public"."hibernate_sequence" RESTART 2705;
ALTER SEQUENCE "public"."image_config_id_seq" RESTART 2;
ALTER SEQUENCE "public"."image_feed_id_seq" RESTART 170625 OWNED BY "image_feed"."id";
ALTER SEQUENCE "public"."image_meta_data_id_seq" RESTART 3444;
ALTER SEQUENCE "public"."image_task_queue_id_seq" RESTART 2739 OWNED BY "image_task_queue"."id";
ALTER SEQUENCE "public"."marker_style_id_seq" RESTART 1931;
ALTER SEQUENCE "public"."namibia_image_id_seq" RESTART 2;
ALTER SEQUENCE "public"."namibia_report_id_seq" RESTART 5446;
ALTER SEQUENCE "public"."news_image_id_seq" RESTART 15303;
ALTER SEQUENCE "public"."pam_report_id_seq" RESTART 2;
ALTER SEQUENCE "public"."report_template_id_seq" RESTART 38180 OWNED BY "report_template"."id";
ALTER SEQUENCE "public"."sliced_image_id_seq" RESTART 2;
ALTER SEQUENCE "public"."source_image_id_seq" RESTART 2;
ALTER SEQUENCE "public"."task_queue_id_seq" RESTART 399978 OWNED BY "task_queue"."id";
ALTER SEQUENCE "public"."task_run_id_seq" RESTART 2;
ALTER SEQUENCE "public"."task_translation_id_seq" RESTART 62447 OWNED BY "task_translation"."id";
ALTER SEQUENCE "public"."userconnection_id_seq" RESTART 2 OWNED BY "userconnection"."id";
-- ----------------------------
--  Primary key structure for table client_app_deployment
-- ----------------------------
ALTER TABLE "public"."client_app_deployment" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table client_app_event
-- ----------------------------
ALTER TABLE "public"."client_app_event" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table client_app_source
-- ----------------------------
ALTER TABLE "public"."client_app_source" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Indexes structure for table client_app_source
-- ----------------------------
CREATE INDEX  "client_app_id_index" ON "public"."client_app_source" USING btree(client_app_id "pg_catalog"."int8_ops" ASC NULLS LAST);

-- ----------------------------
--  Primary key structure for table client
-- ----------------------------
ALTER TABLE "public"."client" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table image_feed
-- ----------------------------
ALTER TABLE "public"."image_feed" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table namibia_image
-- ----------------------------
ALTER TABLE "public"."namibia_image" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table namibia_report_reprocess
-- ----------------------------
ALTER TABLE "public"."namibia_report_reprocess" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table news_image
-- ----------------------------
ALTER TABLE "public"."news_image" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table pam_report
-- ----------------------------
ALTER TABLE "public"."pam_report" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table facebook_data_feed
-- ----------------------------
ALTER TABLE "public"."facebook_data_feed" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Indexes structure for table facebook_data_feed
-- ----------------------------
CREATE INDEX  "code_index" ON "public"."facebook_data_feed" USING btree(code COLLATE "default" "pg_catalog"."text_ops" ASC NULLS LAST);

-- ----------------------------
--  Triggers structure for table facebook_data_feed
-- ----------------------------
CREATE TRIGGER "add_facebook_current_trigger" AFTER INSERT ON "public"."facebook_data_feed" FOR EACH ROW EXECUTE PROCEDURE "add_facebook_trigg_function"();
COMMENT ON TRIGGER "add_facebook_current_trigger" ON "public"."facebook_data_feed" IS NULL;

-- ----------------------------
--  Primary key structure for table facebook_data_model
-- ----------------------------
ALTER TABLE "public"."facebook_data_model" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Triggers structure for table facebook_data_model
-- ----------------------------
CREATE TRIGGER "add_facebook_data_model_image_trigger" AFTER INSERT ON "public"."facebook_data_model" FOR EACH ROW EXECUTE PROCEDURE "add_facebook_data_model_image"();
COMMENT ON TRIGGER "add_facebook_data_model_image_trigger" ON "public"."facebook_data_model" IS NULL;

-- ----------------------------
--  Primary key structure for table report_template
-- ----------------------------
ALTER TABLE "public"."report_template" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table twitter_data_feed
-- ----------------------------
ALTER TABLE "public"."twitter_data_feed" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Indexes structure for table twitter_data_feed
-- ----------------------------
CREATE INDEX  "confidence_index" ON "public"."twitter_data_feed" USING btree(code COLLATE "default" "pg_catalog"."text_ops" ASC NULLS LAST, geo "pg_catalog"."jsonb_ops" ASC NULLS LAST, place "pg_catalog"."jsonb_ops" ASC NULLS LAST, ((((aidr -> 'nominal_labels'::text) -> 0) ->> 'confidence'::text)::double precision) "pg_catalog"."float8_ops" ASC NULLS LAST) WHERE geo IS NOT NULL OR place IS NOT NULL;
CREATE INDEX  "created_at_index" ON "public"."twitter_data_feed" USING btree(created_at "pg_catalog"."timestamp_ops" DESC NULLS FIRST);
CREATE INDEX  "image_index" ON "public"."twitter_data_feed" USING btree(code COLLATE "default" "pg_catalog"."text_ops" ASC NULLS LAST, ((((feed -> 'entities'::text) -> 'media'::text) -> 0) ->> 'type'::text) COLLATE "default" "pg_catalog"."text_ops" ASC NULLS LAST);

-- ----------------------------
--  Triggers structure for table twitter_data_feed
-- ----------------------------
CREATE TRIGGER "add_twitter_current_trigger" AFTER INSERT ON "public"."twitter_data_feed" FOR EACH ROW EXECUTE PROCEDURE "add_twitter_trigg_function"();
COMMENT ON TRIGGER "add_twitter_current_trigger" ON "public"."twitter_data_feed" IS NULL;

-- ----------------------------
--  Primary key structure for table twitter_data_model
-- ----------------------------
ALTER TABLE "public"."twitter_data_model" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Triggers structure for table twitter_data_model
-- ----------------------------
CREATE TRIGGER "add_twitter_data_model_image_trigger" AFTER INSERT ON "public"."twitter_data_model" FOR EACH ROW EXECUTE PROCEDURE "add_twitter_data_model_image"();
COMMENT ON TRIGGER "add_twitter_data_model_image_trigger" ON "public"."twitter_data_model" IS NULL;

-- ----------------------------
--  Primary key structure for table task_run
-- ----------------------------
ALTER TABLE "public"."task_run" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table task_translation
-- ----------------------------
ALTER TABLE "public"."task_translation" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table task_queue
-- ----------------------------
ALTER TABLE "public"."task_queue" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table typhoon_ruby_text_geo_clicker
-- ----------------------------
ALTER TABLE "public"."typhoon_ruby_text_geo_clicker" ADD PRIMARY KEY ("task_id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table role
-- ----------------------------
ALTER TABLE "public"."role" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Uniques structure for table role
-- ----------------------------
ALTER TABLE "public"."role" ADD CONSTRAINT "uk_k7rb0l8j00xce4eg56n4jgkkb" UNIQUE ("level") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table userconnection
-- ----------------------------
ALTER TABLE "public"."userconnection" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table crisis
-- ----------------------------
ALTER TABLE "public"."crisis" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table sliced_image
-- ----------------------------
ALTER TABLE "public"."sliced_image" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table client_app
-- ----------------------------
ALTER TABLE "public"."client_app" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table account
-- ----------------------------
ALTER TABLE "public"."account" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Uniques structure for table account
-- ----------------------------
ALTER TABLE "public"."account" ADD CONSTRAINT "uk_9bmtcmctutbdr107aa60ss7qx" UNIQUE ("api_key") NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."account" ADD CONSTRAINT "uk_f6xpj7h12wr185bqhfi1hqlbr" UNIQUE ("user_name") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table image_config
-- ----------------------------
ALTER TABLE "public"."image_config" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table account_role
-- ----------------------------
ALTER TABLE "public"."account_role" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Uniques structure for table account_role
-- ----------------------------
ALTER TABLE "public"."account_role" ADD CONSTRAINT "account_role_unique_key" UNIQUE ("account_id","role_id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table source_image
-- ----------------------------
ALTER TABLE "public"."source_image" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Indexes structure for table data_model_image_export
-- ----------------------------
CREATE INDEX  "data_model_image_export_collection_code_idx" ON "public"."data_model_image_export" USING btree(collection_code COLLATE "default" "pg_catalog"."text_ops" ASC NULLS LAST);

-- ----------------------------
--  Primary key structure for table image_task_queue
-- ----------------------------
ALTER TABLE "public"."image_task_queue" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table crisis
-- ----------------------------
ALTER TABLE "public"."crisis" ADD CONSTRAINT "fk_3q5ntifmfao3720drgmqqrcs3" FOREIGN KEY ("client_app_id") REFERENCES "public"."client_app" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table sliced_image
-- ----------------------------
ALTER TABLE "public"."sliced_image" ADD CONSTRAINT "fk_83s2406ldfpdjnryxs1ckpigk" FOREIGN KEY ("source_image_id") REFERENCES "public"."source_image" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table client_app
-- ----------------------------
ALTER TABLE "public"."client_app" ADD CONSTRAINT "fk_cq3wwpbyendymxfe9uw8vli2p" FOREIGN KEY ("client_id") REFERENCES "public"."client" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table image_config
-- ----------------------------
ALTER TABLE "public"."image_config" ADD CONSTRAINT "fk_h73pqgf8ymjpmwd0a04qxcg93" FOREIGN KEY ("client_app_id") REFERENCES "public"."client_app" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table account_role
-- ----------------------------
ALTER TABLE "public"."account_role" ADD CONSTRAINT "fk_ibmw1g5w37bmuh5fc0db7wn10" FOREIGN KEY ("account_id") REFERENCES "public"."account" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."account_role" ADD CONSTRAINT "fk_p2jpuvn8yll7x96rae4hvw3sj" FOREIGN KEY ("role_id") REFERENCES "public"."role" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table source_image
-- ----------------------------
ALTER TABLE "public"."source_image" ADD CONSTRAINT "fk_s2y762x4j40c1m04m6v3iayw" FOREIGN KEY ("image_config_id") REFERENCES "public"."image_config" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table image_task_queue
-- ----------------------------
ALTER TABLE "public"."image_task_queue" ADD CONSTRAINT "fk_2jcanc9hpum0ueym26vtim965" FOREIGN KEY ("task_queue_id") REFERENCES "public"."task_queue" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

