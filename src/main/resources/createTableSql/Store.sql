create table store(
                      id int primary key not null unique auto_increment,
                      name varchar(50),
                      address varchar(70) not null,
                      phone char(11) not null check (regexp_like(`phone`,_utf8mb4'^1[3-9]\\d{9}$')),
                      description varchar(100) not null
)comment "商铺表";