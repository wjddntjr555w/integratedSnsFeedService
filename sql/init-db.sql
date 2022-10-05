
create table user(id INT NOT NULL AUTO_INCREMENT,create_date date,modified_date date,email varchar(30),name varchar(10),picture varchar(100),role varchar(10),attribute varchar(30),primary key(id));

create table feed(id INT NOT NULL AUTO_INCREMENT,created_time date, message varchar(1000), feed_id varchar(100), feed_owner INT, primary key(id));

create table main_user(id INT NOT NULL AUTO_INCREMENT, email varchar(100), password varchar(100), role varchar(10),primary key(id));
