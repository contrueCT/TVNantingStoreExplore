create table role(
                     id int unique primary key not null auto_increment,
                     role_name varchar(50) unique not null ,
                     role_description varchar(70)
)comment "角色表";