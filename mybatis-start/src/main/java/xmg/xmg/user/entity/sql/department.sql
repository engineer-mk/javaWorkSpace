create table `department`
(
    `id`   bigint not null AUTO_INCREMENT,
    `name` varchar(255) default null comment '名称',
    primary key (`id`) using btree,
    unique key `uk_name` (`name`) using btree
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='部门';

insert into  `department` (`id`, `name`) values (1,'部门1'),(2,'部门2'),(3,'部门3'),(4,'部门4'),(5,'部门5'),(6,'部门6'),(7,'部门7')