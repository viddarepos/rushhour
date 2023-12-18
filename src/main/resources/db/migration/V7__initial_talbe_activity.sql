CREATE TABLE activity (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255)  NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    duration BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (provider_id) REFERENCES provider(id)
);