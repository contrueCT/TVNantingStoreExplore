create table user_role(
                          user_id int not null,
                          role_id int not null,
                          primary key (user_id,role_id),
                          foreign key (role_id) references role(id) on delete cascade,
                          foreign key (user_id) references user(id) on delete cascade
)comment "用户-角色关联表";