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

show databases;
DROP database if exists server;
CREATE database server;
use server;

DROP TABLE if exists user;
CREATE TABLE user(
  id int unsigned NOT NULL AUTO_INCREMENT,
  name varchar(20) NOT NULL,
  pwd varchar(100) NOT NULL,
  nick varchar(40) NOT NULL,
  auth int,
  ip varchar(100),
  token varchar(1000),
  apptoken varchar(1000),
  time bigint,
  apptime bigint,
  PRIMARY KEY (id),
  UNIQUE (name)
);
DELETE FROM user;
INSERT INTO user (name,pwd,nick,token,auth) VALUES ('qwwuyu','$2a$10$qwwuyu000000000000000uxgJHtcXh1eNTNQKEv.iSpQVnashnBSK','qwwuyu',null,1);
SELECT * FROM user;

DROP TABLE if exists note;
CREATE TABLE note(
  id int unsigned NOT NULL AUTO_INCREMENT,
  user_id int unsigned NOT NULL,
  type int NOT NULL,
  auth int NOT NULL,
  nick varchar(40) NOT NULL,
  title varchar(100) NOT NULL,
  content varchar(1000) NOT NULL,
  herf varchar(100) NOT NULL,
  time bigint NOT NULL,
  PRIMARY KEY (id),
  constraint fk_user foreign key(user_id) references user(id)
);
DELETE FROM note;
INSERT INTO note (user_id,type,auth,nick,title,content,herf,time) VALUES (1,1,2,'nick','title','content','herf',500);
SELECT * FROM note;

--显示所有表
show tables;
--基本结构
desc user;
--详细结构
show create table user;
--修改表名
alter table user rename user2;
--修改表结构
alter table user change name name2 varchar(20);
alter table user add tField varchar(20) after name;
alter table user drop tField;
