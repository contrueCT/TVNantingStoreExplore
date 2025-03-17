create table permission(
                           id int not null unique primary key auto_increment,
                           name varchar(50) not null unique,
                           description varchar(70)
)comment "权限表";