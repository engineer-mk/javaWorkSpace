create table `account`
(
    `id`          bigint not null AUTO_INCREMENT,
    `username`    varchar(255) default null comment '用户名',
    `phone`       varchar(255) default null comment '手机号',
    `create_time` datetime     default null,
    `update_time` datetime     default null,
    `department_id` bigint  default null,
    primary key (`id`) using btree,
    unique key `uk_username` (`username`) using btree
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='账户'