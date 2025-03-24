create table store_role(
                           store_id int not null,
                           role_id int not null,
                           primary key (store_id,role_id),
                           foreign key (role_id) references role(id) on delete cascade,
                           foreign key (store_id) references store(id) on delete cascade
)comment "商铺-角色关联表";