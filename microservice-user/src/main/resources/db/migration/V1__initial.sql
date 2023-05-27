-- Create table users --

CREATE TABLE users (
	id bigserial NOT NULL,
	external_id bpchar(36) NOT NULL,
	last_name varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	status bool NULL,
	CONSTRAINT uk_users_email UNIQUE (email),
	CONSTRAINT uk_users_external_id UNIQUE (external_id),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);