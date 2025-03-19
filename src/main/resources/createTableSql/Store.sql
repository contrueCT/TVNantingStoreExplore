create table store(
                      id int primary key not null unique auto_increment,
                      store_name varchar(50),
                      store_address varchar(70) not null,
                      store_phone char(11) not null check (regexp_like(`store_phone`,_utf8mb4'^1[3-9]\\d{9}$')),
                      description varchar(100) not null
)comment "商铺表";