CREATE DATABASE metrio;

USE metrio;

CREATE TABLE agency (
	id bigint PRIMARY KEY AUTO_INCREMENT,
    creator_id bigint NOT NULL,
    agency_name varchar(255) NOT NULL
);

CREATE TABLE users (
	id bigint PRIMARY KEY AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    email varchar(255) UNIQUE,
    hash_password varchar(255) NOT NULL,
    user_type varchar(255) NOT NULL,
    user_status varchar(255) NOT NULL,
    agency_id bigint,
    
    FOREIGN KEY (agency_id) REFERENCES agency(id)
);

CREATE TABLE clients (
	id bigint PRIMARY KEY AUTO_INCREMENT,
    agency_id bigint NOT NULL,
    client_name varchar(255) NOT NULL,
    client_login varchar(255) NOT NULL,
    client_pass varchar(255) NOT NULL,
    client_status varchar(255) NOT NULL,
    
    FOREIGN KEY (agency_id) REFERENCES agency(id)
);

CREATE TABLE campaigns (
	id bigint PRIMARY KEY AUTO_INCREMENT,
    client_id bigint NOT NULL,
    user_id bigint NOT NULL,
    camp_name varchar(255),
    camp_status varchar(255) NOT NULL,
    
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
    
);

CREATE TABLE results (
	id bigint PRIMARY KEY AUTO_INCREMENT,
    campaign_id bigint,
    clicks int,
    ad_spent DECIMAL(10,2),
    leads int,
    reach int,
    views int,
    
    FOREIGN KEY (campaign_id) REFERENCES campaigns(id)
);

ALTER TABLE agency
ADD FOREIGN KEY (creator_id) REFERENCES users(id);

ALTER TABLE agency AUTO_INCREMENT = 1000001;
ALTER TABLE users AUTO_INCREMENT = 1000001;
ALTER TABLE clients AUTO_INCREMENT = 1000001;
ALTER TABLE campaigns AUTO_INCREMENT = 1000001;
ALTER TABLE results AUTO_INCREMENT = 1000001;
 
SELECT * FROM users;
SELECT * FROM agency;

DROP DATABASE metrio;