DROP DATABASE IF EXISTS exam;

CREATE DATABASE exam;

USE exam;

CREATE TABLE `reply` (
  `id`     INT(11) NOT NULL AUTO_INCREMENT,
  `etid`   INT(11)          DEFAULT NULL,
  `uid`    INT(11)          DEFAULT NULL,
  `answer` TEXT,
  `cruser` VARCHAR(200)     DEFAULT NULL,
  `crtime` VARCHAR(200)     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `examine` (
  `id`        INT(11) NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(200)     DEFAULT NULL,
  `type`      INT(11)          DEFAULT NULL,
  `cruser`    VARCHAR(200)     DEFAULT NULL,
  `crtime`    VARCHAR(100)     DEFAULT NULL,
  `starttime` VARCHAR(100)     DEFAULT NULL,
  `endtime`   VARCHAR(100)     DEFAULT NULL,
  `score`     INT(11)          DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `topic` (
  `id`       INT(11) NOT NULL AUTO_INCREMENT,
  `type`     INT(11)          DEFAULT NULL,
  `content`  TEXT,
  `cruser`   VARCHAR(200)     DEFAULT NULL,
  `crtime`   VARCHAR(200)     DEFAULT NULL,
  `period`   INT(11)          DEFAULT NULL,
  `name`     VARCHAR(200)     DEFAULT NULL,
  `score`    INT(11)          DEFAULT NULL,
  `playtype` INT(11)          DEFAULT NULL,
  `answer`   TEXT,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `user` (
  `id`         INT(11) NOT NULL AUTO_INCREMENT,
  `username`   VARCHAR(200)     DEFAULT NULL,
  `password`   VARCHAR(200)     DEFAULT NULL,
  `photo`      VARCHAR(200)     DEFAULT NULL,
  `name`       VARCHAR(200)     DEFAULT NULL,
  `permission` INT(11)          DEFAULT NULL,
  `type`       INT(11)          DEFAULT NULL,
  `sex`        INT(11)          DEFAULT NULL,
  `pos`        VARCHAR(200)     DEFAULT NULL,
  `cruser`     VARCHAR(200)     DEFAULT NULL,
  `crtime`     VARCHAR(200)     DEFAULT NULL,
  `idcard`     VARCHAR(200)     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `score` (
  `id`       INT(11) NOT NULL AUTO_INCREMENT,
  `accuracy` FLOAT            DEFAULT NULL,
  `score`    FLOAT            DEFAULT NULL,
  `cruser`   VARCHAR(200)     DEFAULT NULL,
  `crtime`   VARCHAR(200)     DEFAULT NULL,
  `rid`      INT(11)          DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `examine_user` (
  `id`     INT(11) NOT NULL AUTO_INCREMENT,
  `eid`    INT(11)          DEFAULT NULL,
  `uid`    INT(11)          DEFAULT NULL,
  `done`   INT(1)           DEFAULT NULL,
  `cruser` VARCHAR(200)     DEFAULT NULL,
  `crtime` VARCHAR(200)     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `esession` (
  `id`        INT(11) NOT NULL AUTO_INCREMENT,
  `eid`       INT(11)          DEFAULT NULL,
  `name`      VARCHAR(200)     DEFAULT NULL,
  `startTime` VARCHAR(200)     DEFAULT NULL,
  `endTime`   VARCHAR(200)     DEFAULT NULL,
  `cruser`    VARCHAR(200)     DEFAULT NULL,
  `crtime`    VARCHAR(200)     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `examine_topic` (
  `id`     INT(11) NOT NULL AUTO_INCREMENT,
  `eid`    INT(11)          DEFAULT NULL,
  `tid`    INT(11)          DEFAULT NULL,
  `cruser` VARCHAR(200)     DEFAULT NULL,
  `crtime` VARCHAR(200)     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `examine_info` (
  `id`     INT(11) NOT NULL AUTO_INCREMENT,
  `ksxz`   VARCHAR(2000)    DEFAULT NULL,
  `kssm`   VARCHAR(2000)    DEFAULT NULL,

  `cruser` VARCHAR(200)     DEFAULT NULL,
  `crtime` VARCHAR(200)     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO user (username, password, photo, name, permission, type, sex, cruser, crtime, idcard)
VALUES ('admin', '3d186804534370c3c817db0563f0e461', NULL, 'admin', 100, 2, 1, 'system', '2017325205', NULL);

INSERT INTO user (username, password, photo, name, permission, type, sex, cruser, crtime, idcard)
VALUES ('musicPlayer1', '3d186804534370c3c817db0563f0e461', NULL, 'admin', 100, 2, 1, 'system', '2017325205', NULL);

INSERT INTO user (username, password, photo, name, permission, type, sex, cruser, crtime, idcard)
VALUES ('musicPlayer2', '3d186804534370c3c817db0563f0e461', NULL, 'admin', 100, 2, 1, 'system', '2017325205', NULL);
