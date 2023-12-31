CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) UNIQUE NOT NULL,

  PRIMARY KEY (`id`)
);

INSERT INTO `roles` (id,name) VALUES (1,'ADMINISTRATOR'), (2,'PROVIDER_ADMINISTRATOR'), (3,'EMPLOYEE'), (4,'CLIENT');

ALTER TABLE `account`
ADD COLUMN role_id bigint NOT NULL DEFAULT 1;

ALTER TABLE `account`
ADD FOREIGN KEY (role_id) REFERENCES roles(id);
