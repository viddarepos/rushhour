CREATE TABLE `account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` varchar(255) UNIQUE NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) Not Null ,
  PRIMARY KEY (`id`)

) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
