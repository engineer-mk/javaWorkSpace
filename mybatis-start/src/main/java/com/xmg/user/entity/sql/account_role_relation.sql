create table `account_role_relation`
(
    `account_id`  bigint not null,
    `role_id` bigint not null,
    primary key (`account_id`, `role_id`) using btree,
    key `idx_role` (`role_id`) using btree,
    constraint `fk_account_id` foreign key (`account_id`) references `account` (`id`),
    constraint `fk_role_id` foreign key (`role_id`) references `role` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='账户角色关系'