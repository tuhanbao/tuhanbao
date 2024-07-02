
drop table if exists `tb_dynamic_enum`;
create table `tb_dynamic_enum`
(
    `id`           bigint auto_increment,
    `name_cn`      varchar(511),
    `name_en`      varchar(511),
    `enum_type`    int,
    `gmt_created`  bigint,
    `gmt_modified` bigint,
    primary key (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

drop table if exists `tb_dynamic_table`;
create table `tb_dynamic_table`
(
    `table_name`   varchar(127),
    `name_cn`      varchar(511),
    `name_en`      varchar(511),
    `cache_type`   int default 0,
    `tag`          varchar(127) comment '标签，业务自定义，可以自己灵活指定其用途',
    `indexes`      varchar(255) comment '索引',
    `remark`       varchar(511) comment '备注',
    `gmt_created`  bigint,
    `gmt_modified` bigint,
    primary key (`table_name`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

drop table if exists `tb_dynamic_column`;
create table `tb_dynamic_column`
(
    `id`            bigint auto_increment,
    `col_name`      varchar(255),
    `name_cn`       varchar(511),
    `name_en`       varchar(511),
    `table_name`    varchar(255),
    `data_type`     int comment '对应枚举datatype的名称',
    `args`          varchar(255) comment '自定义参数',
    `need_show`     tinyint,
    `need_filter`   tinyint,
    `need_order`    tinyint,
    `required`      tinyint comment '必填',
    `default_value` varchar(255) comment '默认值',
    `priority`      int comment '优先级，越小优先级越高',
    `remark`        varchar(511) comment '备注',
    `gmt_created`   bigint,
    `gmt_modified`  bigint,
    primary key (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

drop table if exists `tb_dynamic_enum_item`;
create table `tb_dynamic_enum_item`
(
    `id`           bigint auto_increment,
    `enum_id`      bigint,
    `name_cn`      varchar(511),
    `name_en`      varchar(511),
    `value`        int,
    `parent_id`    bigint comment '父id',
    `priority`     int comment '优先级，越小优先级越高',
    `gmt_created`  bigint,
    `gmt_modified` bigint,
    primary key (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;