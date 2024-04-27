CREATE DATABASE IF NOT EXISTS VDNGestion;
USE VDNGestion;

CREATE TABLE employe (
    id_employe INT PRIMARY KEY AUTO_INCREMENT,
    id_ligue INT DEFAULT NULL,
    droit BOOLEAN NOT NULL,
    nom VARCHAR(256) NOT NULL,
    prenom VARCHAR(256) DEFAULT NULL,
    mail VARCHAR(256) DEFAULT NULL,
    password VARCHAR(256) NOT NULL,
    date_arrive DATE DEFAULT NULL,
    date_depart DATE DEFAULT NULL
);

CREATE TABLE ligue (
    id_ligue INT PRIMARY KEY AUTO_INCREMENT,
    id_admin INT,
    nom VARCHAR(256) NOT NULL,
    FOREIGN KEY (id_admin) REFERENCES employe (id_employe)
);

ALTER TABLE employe
ADD FOREIGN KEY (id_ligue) REFERENCES ligue(id_ligue);
