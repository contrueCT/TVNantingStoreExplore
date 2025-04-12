create table subscribes
(
    subscribes_user_id     int                                not null comment '关注者的id',
    subscribes_target_id   int                                not null comment '被关注者的id',
    subscribes_target_type varchar(50)                        null comment '目标类型',
    subscribes_create_time datetime default CURRENT_TIMESTAMP not null comment '关注时间',
    subscribes_target_name varchar(50)                        null comment '关注对象的名字',
    primary key (subscribes_user_id, subscribes_target_id)
)
    comment '记录用户对用户和商铺的关注记录';