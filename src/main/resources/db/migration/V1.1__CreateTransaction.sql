USE equity;

CREATE TABLE user (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY
    , first_name VARCHAR(50) NULL
    , last_name VARCHAR(50) NULL
);

INSERT INTO user(first_name, last_name)
VALUES('James', 'Carson');

SELECT @USER := id from user
WHERE first_name = 'James'
AND last_name = 'Carson';

--
CREATE TABLE transaction_type (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY
    , description CHAR(12) NOT NULL
);

INSERT INTO transaction_type(description)
VALUES('deposit');

INSERT INTO transaction_type(description)
VALUES('withdraw');

INSERT INTO transaction_type(description)
VALUES('buy');

INSERT INTO transaction_type(description)
VALUES('sell');

SELECT @DEPOSIT := id from transaction_type
WHERE description = 'deposit';

SELECT @WITHDRAW := id from transaction_type
WHERE description = 'withdraw';

SELECT @BUY := id from transaction_type
WHERE description = 'buy';

SELECT @SELL := id from transaction_type
WHERE description = 'sell';


CREATE TABLE transaction (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY
    , user_id BIGINT UNSIGNED NOT NULL
    , transaction_type_id BIGINT UNSIGNED NOT NULL
    , symbol CHAR(12) NULL
    , quantity BIGINT NULL -- let's plan for fractional shares
    , value DECIMAL(15,2) NULL
    , timestamp TIMESTAMP NOT NULL
    , CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES user(id)
    , CONSTRAINT fk_transaction_transaction_type FOREIGN KEY (transaction_type_id) REFERENCES transaction_type(id)
);


INSERT INTO transaction(user_id, transaction_type_id, symbol, quantity, value, timestamp)
VALUES(@USER, @DEPOSIT, NULL, NULL, 1000.0, '2020-01-01 12:05:22');

INSERT INTO transaction(user_id, transaction_type_id, symbol, quantity, value, timestamp)
VALUES(@USER, @BUY, 'AAPL', 10, 760.60, '2020-01-02 16:01:23');

INSERT INTO transaction(user_id, transaction_type_id, symbol, quantity, value, timestamp)
VALUES(@USER, @BUY, 'AAPL', 5, 435.11, '2020-06-05 15:21:55');

INSERT INTO transaction(user_id, transaction_type_id, symbol, quantity, value, timestamp)
VALUES(@USER, @BUY, 'GME', 5, 200.99, '2020-12-21 15:45:24');

INSERT INTO transaction(user_id, transaction_type_id, symbol, quantity, value, timestamp)
VALUES(@USER, @SELL, 'GME', 5, 905.04, '2021-01-26 18:34:12');

INSERT INTO transaction(user_id, transaction_type_id, symbol, quantity, value, timestamp)
VALUES(@USER, @WITHDRAW, NULL, NULL, 500.0, '2021-02-04 11:01:56');
