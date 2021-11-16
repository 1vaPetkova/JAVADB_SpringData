
USE `test_orm`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `age` int(8) DEFAULT NULL,
  `registration` date DEFAULT NULL,
  PRIMARY KEY (`id`)
);