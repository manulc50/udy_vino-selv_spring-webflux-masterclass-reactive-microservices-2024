CREATE TABLE users (
	id BIGINT AUTO_INCREMENT,
	name VARCHAR(50),
	balance INT,
	PRIMARY KEY (id)
);

CREATE TABLE user_transactions (
	id BIGINT AUTO_INCREMENT,
	user_id BIGINT,
	amount INT,
	transaction_date TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	PRIMARY KEY (id)
);

INSERT INTO users(name, balance) VALUES('sam', 1000);
INSERT INTO users(name, balance) VALUES('mike', 1200);
INSERT INTO users(name, balance) VALUES('jake', 800);
INSERT INTO users(name, balance) VALUES('marshal', 2000);