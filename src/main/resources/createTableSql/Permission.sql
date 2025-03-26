create table permission(
                           id int not null unique primary key auto_increment,
                           method VARCHAR(10) not null,
                           url VARCHAR(200) not null
)comment "权限表";