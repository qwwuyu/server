--修改编码
show variables like 'character_set_%';
show variables like 'collation_%';
SET NAMES 'utf8'; 
set character_set_client=utf8;
set character_set_connection=utf8;
set character_set_database=utf8;
set character_set_results=utf8;
set character_set_server=utf8;
set character_set_system=utf8;
set collation_connection=utf8_general_ci;
set collation_database=utf8_general_ci;
set collation_server=utf8_general_ci;

--展示数据库
show databases;
--删除数据库
DROP database if exists server;
--创建数据库
CREATE database server;
--使用数据库
use server;

--删除表
DROP TABLE if exists user;
--创建表
CREATE TABLE user(
  id int unsigned NOT NULL AUTO_INCREMENT,
  name varchar(20) NOT NULL,
  pwd varchar(100) NOT NULL,
  nick varchar(40) NOT NULL,
  token varchar(1000),
  auth int,
  PRIMARY KEY (id),
  UNIQUE (name)
);
--删除数据
DELETE FROM user;
--插入数据
INSERT INTO user (name,pwd,nick,token,auth) VALUES ('qwwuyu','123456','qwwuyu',null,1);
--查询数据
SELECT * FROM user;

--显示所有表
show tables;
