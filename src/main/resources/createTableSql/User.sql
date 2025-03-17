create table user(
                     id int primary key not null unique auto_increment,
                     username varchar(50) not null unique,
                     password varchar(6) not null check(regexp_like(`password`,_utf8mb4'^\\d{6}$')),
                     email varchar(70),
                     phone varchar(11) not null check (regexp_like(`phone`,_utf8mb4'^1[3-9]\\d{9}$')),
                     address varchar(50),
                     age int check(age>0 and age<=250),
                     gender varchar(10)
)comment "用户表";