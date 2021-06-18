-- CREATE TABLE  authors(
-- id INT AUTO_INCREMENT PRIMARY KEY,
-- full_name VARCHAR(250) NOT NULL,
-- PRIMARY KEY (id)
-- );

DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

CREATE TABLE  books(
id BIGSERIAL PRIMARY KEY,
author_id INT NOT NULL,
title VARCHAR(250) NOT NULL,
priceOld  VARCHAR(250) DEFAULT NULL,
price VARCHAR(250) DEFAULT NULL
-- PRIMARY KEY (id),
-- FOREIGN KEY (author_id) REFERENCES authors(id)
);

create table authors (
	id BIGSERIAL PRIMARY KEY,
	first_name VARCHAR(50),
	last_name VARCHAR(50)
);