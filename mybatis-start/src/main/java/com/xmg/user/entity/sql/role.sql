create table `role`
(
    `id`   bigint not null AUTO_INCREMENT,
    `name` varchar(255) default null comment '名称',
    primary key (`id`) using btree,
    unique key `uk_name` (`name`) using btree
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色';

insert into  `role` (`id`, `name`) values (1,'角色1'),(2,'角色2'),(3,'角色3'),(4,'角色4'),(5,'角色5'),(6,'角色6'),(7,'角色7')