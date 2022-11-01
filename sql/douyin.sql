/*
SQLyog Ultimate v10.00 Beta1
MySQL - 8.0.26 : Database - douyin
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

create database if not exists douyin;
use douyin;

DROP TABLE IF EXISTS `relation`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `favourite`;
DROP TABLE IF EXISTS `video`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `follow_count` bigint DEFAULT NULL,
                        `follower_count` bigint DEFAULT NULL,
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`password`,`follow_count`,`follower_count`) values (1,'3070364701','80b777ceae80d2ebcbaef0799bc0becf',0,0),(2,'3070364702','80b777ceae80d2ebcbaef0799bc0becf',0,0);




CREATE TABLE `video` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `author_id` int NOT NULL,
                         `play_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                         `cover_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                         `favourite_count` bigint DEFAULT NULL,
                         `comment_count` bigint DEFAULT NULL,
                         `create_time` varchar(20) DEFAULT NULL,
                         `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         PRIMARY KEY (`id`) USING BTREE,
                         UNIQUE KEY `Video_id_uindex` (`id`) USING BTREE,
                         KEY `Video_user_id_fk` (`author_id`) USING BTREE,
                         CONSTRAINT `Video_user_id_fk` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='视频信息表';

/*Data for the table `video` */

/*Table structure for table `comment` */



CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `video_id` int NOT NULL,
  `comment_text` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `comment_data` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`comment_id`) USING BTREE,
  KEY `comment_user_id_fk` (`user_id`) USING BTREE,
  KEY `comment_video_id_fk` (`video_id`) USING BTREE,
  CONSTRAINT `comment_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_video_id_fk` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户评论表';

/*Data for the table `comment` */

/*Table structure for table `favourite` */



CREATE TABLE `favourite` (
  `user_id` int NOT NULL,
  `video_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`video_id`) USING BTREE,
  KEY `favourite_video_id_fk` (`video_id`) USING BTREE,
  CONSTRAINT `favourite_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `favourite_video_id_fk` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户点赞表';

/*Data for the table `favourite` */

/*Table structure for table `relation` */



CREATE TABLE `relation` (
  `author_id` int NOT NULL,
  `favourite_id` int NOT NULL,
  UNIQUE KEY `relation_pk` (`author_id`,`favourite_id`) USING BTREE,
  KEY `relation_favourite_id_index` (`favourite_id`) USING BTREE,
  CONSTRAINT `relation_user_id_fk` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relation_user_id_fk_2` FOREIGN KEY (`favourite_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='粉丝表';

/*Data for the table `relation` */

/*Table structure for table `user` */

/*Table structure for table `video` */


insert  into `video`(`id`,`author_id`,`play_url`,`cover_url`,`favourite_count`,`comment_count`,`create_time`,`title`) values (5,2,'E:/IDEAproject/douyin/src/main/resources/video/2/e205c604-70e0-4187-9270-ddfaf76a534e.mp4','E:/IDEAproject/douyin/src/main/resources/picture/2/e205c604-70e0-4187-9270-ddfaf76a534e.jpg',0,0,'1667218556828','1'),(6,2,'E:/IDEAproject/douyin/src/main/resources/video/2/93aa41a6-49e1-477f-8f52-7101f4fadf8d.mp4','E:/IDEAproject/douyin/src/main/resources/picture/2/93aa41a6-49e1-477f-8f52-7101f4fadf8d.jpg',0,0,'1667218569086','1'),(7,2,'E:/IDEAproject/douyin/src/main/resources\\video\\2\\d284cb47-b89e-4549-8d91-db5c4bef336e.mp4','E:/IDEAproject/douyin/src/main/resources\\picture\\2\\d284cb47-b89e-4549-8d91-db5c4bef336e.jpg',0,0,'1667223856212','1'),(8,2,'E:/IDEAproject/douyin/src/main/resources\\video\\2\\2d8008a9-cdf8-4948-bd7b-1aa03d791de0.mp4','E:/IDEAproject/douyin/src/main/resources\\picture\\2\\2d8008a9-cdf8-4948-bd7b-1aa03d791de0',0,0,'1667223922493','q'),(9,2,'E:/IDEAproject/douyin/src/main/resources\\video\\2\\21871b66-fbe6-4594-9ce1-e19efb809442.mp4','E:/IDEAproject/douyin/src/main/resources\\picture\\2\\21871b66-fbe6-4594-9ce1-e19efb809442.jpg',0,0,'1667224281359','q'),(10,2,'E:/IDEAproject/douyin/src/main/resources\\video\\2\\523c7142-deb8-4cf5-bf24-3be1d10b8a64.mp4','E:/IDEAproject/douyin/src/main/resources\\picture\\2\\523c7142-deb8-4cf5-bf24-3be1d10b8a64.jpg',0,0,'1667224399047','q');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
