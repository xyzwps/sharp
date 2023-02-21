CREATE TABLE `user` (
  `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username`     varchar(40) NOT NULL,
  `display_name` varchar(40) NOT NULL,
  `created_at`   datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at`   datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user_details` (
  `user_id`      bigint(20) unsigned NOT NULL,
  `password`     varchar(1024)   DEFAULT NULL,
  `password_exp` datetime        DEFAULT NULL,
  `locked`       tinyint(1)          NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `todo` (
  `id`         varchar(24)   NOT NULL,
  `user_id`     bigint(20)   NOT NULL,
  `details`     varchar(100) NOT NULL,
  `status`      enum('TODO','IN_PROGRESS','DONE','DELETED') NOT NULL,
  `create_time` datetime(3)  NOT NULL,
  `update_time` datetime(3)  NOT NULL,
  PRIMARY KEY (`user_id`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `anthology` (
  `anthology_id` varchar(60)  NOT NULL,
  `author_id`    bigint(20)   NOT NULL,
  `title`        varchar(100) NOT NULL,
  `create_time`  datetime(3)  NOT NULL,
  PRIMARY KEY (`anthology_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `post` (
  `id`      bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20)   NOT NULL,
  `title`   varchar(200) NOT NULL,
  `idem`    varchar(50)  NOT NULL COMMENT 'idempotency for inserting',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_create_idem` (`idem`),
  KEY `idx_user_post` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `post_content` (
  `id`          bigint(20) unsigned NOT NULL,
  `type`        enum('EDITOR','MD','HTML','TXT') NOT NULL,
  `content`     mediumtext NOT NULL,
  `create_time` datetime   NOT NULL,
  `update_time` datetime   NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tag` (
  `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name`        varchar(40) NOT NULL,
  `create_time` datetime    NOT NULL,
  `update_time` datetime    NOT NULL,
  `deleted` tinyint(1)      NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tag_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tagged` (
  `id`      varchar(40)   NOT NULL,
  `type`    varchar(10)   NOT NULL,
  `tag_ids` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;