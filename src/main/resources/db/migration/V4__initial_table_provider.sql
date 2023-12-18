CREATE TABLE `provider` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) UNIQUE NOT NULL,
  `website` VARCHAR(255) UNIQUE NOT NULL,
  `business_domain` VARCHAR(255) UNIQUE NOT NULL,
  `phone` VARCHAR(255) NOT NULL,
  `start_time` TIME  NOT NULL,
  `end_time` TIME NOT NULL,
  `working_days` VARCHAR(255) NOT NULL,

  PRIMARY KEY (`id`)
)
