CREATE TABLE `appointment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `start_date` DATETIME  NOT NULL ,
  `end_date` DATETIME NOT NULL ,
  `employee_id` BIGINT NOT NULL,
  `client_id` BIGINT NOT NULL,
  `activity_id` BIGINT NOT NULL,

   PRIMARY KEY (`id`),
   FOREIGN KEY (`employee_id`) REFERENCES `employee`(`id`),
   FOREIGN KEY (`client_id`) REFERENCES `client`(`id`),
   FOREIGN KEY (`activity_id`) REFERENCES `activity`(`id`)

   )
