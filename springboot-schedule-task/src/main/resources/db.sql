CREATE TABLE `sys_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) DEFAULT NULL,
  `bean_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `method_params` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `job_status` char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `update_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO sys_job (`id`, `job_id`, `bean_name`, `method_params`, `cron_expression`, `job_status`, `remark`, `create_time`, `update_time`, `method_name`) VALUES ('1', '1', 'demoTask', NULL, '*/5 * * * * ?', '1', '测试无参定时任务 ', '2020-04-10', '2020-04-10', 'taskNoParams');
INSERT INTO sys_job (`id`, `job_id`, `bean_name`, `method_params`, `cron_expression`, `job_status`, `remark`, `create_time`, `update_time`, `method_name`) VALUES ('2', '2', 'demoTask', '123456', '*/10 * * * * ?', '1', '测试有参定时任务 ', '2020-04-10', '2020-04-10', 'taskWithParams');
