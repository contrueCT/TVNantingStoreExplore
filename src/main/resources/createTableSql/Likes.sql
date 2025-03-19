create table likes(
                      id int primary key not null unique auto_increment,
                      user_id int not null,
                      target_id int not null,
                      target_type varchar(10) not null,
                      target_name varchar(50) not null,
                      user_name varchar(50) not null,
                      create_time datetime default current_timestamp
)comment "点赞表";