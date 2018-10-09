/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 90603
 Source Host           : localhost:5432
 Source Catalog        : parina
 Source Schema         : base

 Target Server Type    : PostgreSQL
 Target Server Version : 90603
 File Encoding         : 65001

 Date: 31/01/2014 00:00:29
*/


-- ----------------------------
-- Type structure for server_type
-- ----------------------------
DROP TYPE IF EXISTS "base"."server_type";
CREATE TYPE "base"."server_type" AS ENUM (
  'proxy',
  'hub',
  'lobby',
  'game'
);

-- ----------------------------
-- Sequence structure for users_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "base"."users_id_seq";
CREATE SEQUENCE "base"."users_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS "base"."users";
CREATE TABLE "base"."users" (
  "id" int4 NOT NULL DEFAULT nextval('"base".users_id_seq'::regclass),
  "uuid" uuid NOT NULL,
  "username" varchar(16) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "base"."users_id_seq"
OWNED BY "base"."users"."id";
SELECT setval('"base"."users_id_seq"', 4, true);

-- ----------------------------
-- Indexes structure for table users
-- ----------------------------
CREATE INDEX "username" ON "base"."users" USING btree (
  "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uuid" ON "base"."users" USING btree (
  "uuid" "pg_catalog"."uuid_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table users
-- ----------------------------
ALTER TABLE "base"."users" ADD CONSTRAINT "users_pkey" PRIMARY KEY ("id");
