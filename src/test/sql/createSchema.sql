drop table if exists sports;
drop table if exists users;
drop table if exists activities;
drop table if exists routes;

CREATE TABLE sports (
    sid SERIAL UNIQUE,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE users(
    uid SERIAL UNIQUE,
    email VARCHAR(50)  UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL );

CREATE TABLE routes(
    rid SERIAL UNIQUE,
    start_location VARCHAR(50) NOT NULL,
    end_location VARCHAR(50) NOT NULL,
    distance DECIMAL NOT NULL);

CREATE TABLE activities (
    aid SERIAL UNIQUE,
    date date NOT NULL,
    duration_time TIME NOT NULL,
    sid INT REFERENCES sports(sid)NOT NULL,
    uid INT REFERENCES users(uid) NOT NULL,
    rid INT REFERENCES routes(rid),
    timestamp TIMESTAMP);