-- 修改编码
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

-- 展示数据库
show databases;
-- 删除数据库
DROP database hello;
-- 创建数据库
CREATE database hello;
-- 使用数据库
use hello;

-- 删除表
DROP TABLE if exists test;
-- 创建表
CREATE TABLE test(
  id int unsigned NOT NULL AUTO_INCREMENT,
  content varchar(100) NOT NULL,
  PRIMARY KEY (id)
);
-- 显示所有表
show tables;
-- 修改表名
RENAME TABLE test TO test;
-- 显示表结构
SHOW COLUMNS FROM test;
-- 删除数据
DELETE FROM test where id = 1;
-- 插入数据
INSERT INTO test (content) VALUES ('哈哈');
-- 查询数据
SELECT * FROM test;

-- 基本结构
desc user;
-- 详细结构
show create table user;
-- 修改表名
alter table user rename user2;
-- 修改表结构
alter table user change name name2 varchar(20);
alter table user add tField varchar(20) after name;
alter table user drop tField;
