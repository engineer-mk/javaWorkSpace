create table `resource`
(
    `id`   bigint not null AUTO_INCREMENT,
    `name` varchar(255) default null comment '名称',
    primary key (`id`) using btree,
    unique key `uk_name` (`name`) using btree
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='资源'

insert into  `resource` (`id`, `name`) values (3,'资源3'),(4,'资源4'),(5,'资源5'),(6,'资源6'),(7,'资源7'),(8,'资源8')