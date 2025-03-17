create table role_permission(
                                role_id int not null,
                                permission_id int not null,
                                primary key (role_id,permission_id),
                                foreign key (role_id) references role(id) on delete cascade,
                                foreign key (permission_id) references permission(id) on delete cascade
)comment "角色-权限关联表";