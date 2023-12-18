CREATE TABLE `activity_employee` (
  `activity_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  KEY `FKkreccaavlj622muwxabnp66ke` (`employee_id`),
  KEY `FKbpp987g3l9xuyepq3leb84kb6` (`activity_id`),
  CONSTRAINT `FKbpp987g3l9xuyepq3leb84kb6` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `FKkreccaavlj622muwxabnp66ke` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci