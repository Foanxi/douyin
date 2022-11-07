/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : douyin

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 07/11/2022 19:19:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
    `comment_id`   bigint(0)                                                     NOT NULL COMMENT '评论id',
    `user_id`      bigint(0)                                                     NOT NULL COMMENT '评论者id',
    `video_id`     bigint(0)                                                     NOT NULL COMMENT '被评论视频id',
    `commentText`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '评论内容',
    `create_time`  timestamp(0)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '评论时间',
    `delete_time`  timestamp(0)                                                  NULL     DEFAULT NULL COMMENT '删除评论时间',
    `logic_delete` int(0)                                                        NOT NULL DEFAULT 1 COMMENT '逻辑删除，1-为未删除，0为删除',
    PRIMARY KEY (`comment_id`, `create_time`) USING BTREE,
    INDEX `user_id` (`user_id`) USING BTREE,
    INDEX `video_id` (`video_id`) USING BTREE,
    CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for favourite
-- ----------------------------
DROP TABLE IF EXISTS `favourite`;
CREATE TABLE `favourite`
(
    `favourite_id` bigint(0) NOT NULL COMMENT '点赞关系id',
    `user_id`      bigint(0) NOT NULL COMMENT '用户id',
    `video_id`     bigint(0) NOT NULL COMMENT '视频id',
    PRIMARY KEY (`favourite_id`, `user_id`, `video_id`) USING BTREE,
    INDEX `user_id` (`user_id`) USING BTREE,
    INDEX `video_id` (`video_id`) USING BTREE,
    CONSTRAINT `favourite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `favourite_ibfk_2` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation`
(
    `relation_id`  bigint(0) NOT NULL COMMENT '关注关系id唯一标识符',
    `author_id`    bigint(0) NOT NULL COMMENT '被关注用户id/作者id',
    `favourite_id` bigint(0) NOT NULL COMMENT '粉丝id',
    PRIMARY KEY (`relation_id`, `author_id`, `favourite_id`) USING BTREE,
    INDEX `author_id` (`author_id`) USING BTREE,
    INDEX `favourite_id` (`favourite_id`) USING BTREE,
    CONSTRAINT `relation_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `relation_ibfk_2` FOREIGN KEY (`favourite_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `user_id`        bigint(0)                                                     NOT NULL COMMENT '用户id',
    `name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
    `password`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
    `follow_count`   int(0)                                                        NOT NULL COMMENT '关注数',
    `follower_count` int(0)                                                        NOT NULL COMMENT '粉丝数',
    `create_time`    timestamp(0)                                                  NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '用户插入时间',
    `update_time`    timestamp(0)                                                  NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '用户更新时间',
    `delete_time`    timestamp(0)                                                  NULL     DEFAULT NULL COMMENT '用户删除时间',
    `logic_delete`   int(0)                                                        NOT NULL DEFAULT 1 COMMENT '逻辑删除，1-未删除，0-已删除',
    PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`
(
    `video_id`        bigint(0)                                                     NOT NULL COMMENT '视频id',
    `author_id`       bigint(0)                                                     NOT NULL COMMENT '视频作者id',
    `play_url`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频播放地址',
    `cover_url`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频封面地址',
    `favourite_count` int(0)                                                        NULL DEFAULT NULL COMMENT '视频被点赞数量',
    `comment_count`   int(0)                                                        NULL DEFAULT NULL COMMENT '视频评论数量',
    `create_time`     timestamp(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '视频的发布时间',
    `delete_time`     timestamp(0)                                                  NULL DEFAULT NULL COMMENT '视频删除时间',
    `title`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频标题',
    PRIMARY KEY (`video_id`) USING BTREE,
    INDEX `author_id` (`author_id`) USING BTREE,
    CONSTRAINT `video_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
