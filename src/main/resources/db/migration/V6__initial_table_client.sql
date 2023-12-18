CREATE TABLE `client` (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `phone` VARCHAR(255) NOT NULL,
   `address` VARCHAR(255) NOT NULL,
   `account_id` BIGINT NOT NULL,

  PRIMARY KEY (`id`)
);

