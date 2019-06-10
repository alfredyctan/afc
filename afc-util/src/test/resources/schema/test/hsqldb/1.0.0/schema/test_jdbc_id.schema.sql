CREATE SEQUENCE IF NOT EXISTS test_id_seq START WITH 1 INCREMENT BY 3;

CREATE SEQUENCE IF NOT EXISTS test_id_seq_concurrency START WITH 1 INCREMENT BY 3;

CREATE TABLE test_identity (
	name varchar(20) NOT NULL,
    id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 3)
);

CREATE TABLE test_identity_concurrency (
	name varchar(20) NOT NULL,
    id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 3)
);

