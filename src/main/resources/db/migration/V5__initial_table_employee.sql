CREATE TABLE `employee` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(255) NOT NULL,
  `rate` DOUBLE NOT NULL,
  `date` DATE NOT NULL,
  `account_id` bigint NOT NULL,
  `provider_id` bigint NOT NULL,

  PRIMARY KEY (`id`)
);

ALTER TABLE `employee`
ADD CONSTRAINT provider_fk FOREIGN KEY (provider_id) REFERENCES `provider`(id);

ALTER TABLE `employee`
ADD CONSTRAINT account_fk FOREIGN KEY (account_id) REFERENCES `account`(id);



