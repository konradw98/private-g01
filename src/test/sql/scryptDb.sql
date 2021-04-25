CREATE TABLE sport (
	sid INT NOT NULL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	description VARCHAR(255) NOT NULL
);

CREATE TABLE users(
	uid INT NOT NULL PRIMARY KEY,
	email VARCHAR(50) NOT NULL,
	name VARCHAR(50) NOT NULL );

CREATE TABLE route(
	rid INT NOT NULL PRIMARY KEY,
	start_location VARCHAR(50) NOT NULL,
	end_location VARCHAR(50) NOT NULL,
	distance INT NOT NULL);

CREATE TABLE activity (
	aid INT PRIMARY KEY,
	date date NOT NULL,
	duration_time INT NOT NULL,
	sid INT REFERENCES sport(sid)NOT NULL,
	uid INT REFERENCES users(uid) NOT NULL,
	rid INT REFERENCES route(rid));
