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
DROP database hello;
--创建数据库
CREATE database hello;
--使用数据库
use hello;

--删除表
DROP TABLE if exists test;
--创建表
CREATE TABLE test(
  id int unsigned NOT NULL AUTO_INCREMENT,
  content varchar(100) NOT NULL,
  PRIMARY KEY (id)
);
--显示所有表
show tables;
--删除数据
DELETE FROM test;
--插入数据
INSERT INTO test (content) VALUES ('哈哈');
--查询数据
SELECT * FROM test;
