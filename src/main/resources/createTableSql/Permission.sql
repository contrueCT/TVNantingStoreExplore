create table permission(
                           id int not null unique primary key auto_increment,
                           permission_name varchar(50) not null unique,
                           permission_description varchar(70)
)comment "权限表";