create table `account_resource_relation`
(
    `account_id`  bigint not null,
    `resource_id` bigint not null,
    primary key (`account_id`, `resource_id`) using btree,
    key `idx_resource` (`resource_id`) using btree,
    constraint `fk_resource_account_id` foreign key (`account_id`) references `account` (`id`),
    constraint `fk_resource_id` foreign key (`resource_id`) references `resource` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='账户资源关系'