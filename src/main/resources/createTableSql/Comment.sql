create table comment(
                        id int unique primary key not null auto_increment,
                        user_id int not null ,
                        target_id int not null ,
                        user_name varchar(50),
                        target_name varchar(50),
                        content varchar(200) not null ,
                        create_time timestamp default current_timestamp,
                        foreign key (user_id) references user(id) on delete cascade
)comment "评论表";