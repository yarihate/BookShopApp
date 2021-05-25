-- CREATE TABLE  authors(
-- id INT AUTO_INCREMENT PRIMARY KEY,
-- full_name VARCHAR(250) NOT NULL,
-- PRIMARY KEY (id)
-- );

DROP TABLE IF EXISTS books;

CREATE TABLE  books(
id INT AUTO_INCREMENT PRIMARY KEY,
author_id INT NOT NULL,
title VARCHAR(250) NOT NULL,
priceOld  VARCHAR(250) DEFAULT NULL,
price VARCHAR(250) DEFAULT NULL
-- PRIMARY KEY (id),
-- FOREIGN KEY (author_id) REFERENCES authors(id)
);

create table authors (
	id INT,
	first_name VARCHAR(50),
	last_name VARCHAR(50)
);