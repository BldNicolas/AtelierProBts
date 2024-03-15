CREATE DATABASE IF NOT EXISTS VDNGestion;
USE VDNGestion;

CREATE TABLE Employe (
    id_employe INT PRIMARY KEY,
    id_ligue INT,
    droit BOOL NOT NULL,
    nom VARCHAR(256) NOT NULL,
    prenom VARCHAR(256) NOT NULL,
    password VARCHAR(256) NOT NULL,
    mail VARCHAR(256) NOT NULL,
    date_arrive DATE NOT NULL,
    date_depart DATE
);

CREATE TABLE Ligue (
    id_ligue INT PRIMARY KEY AUTO_INCREMENT,
    id_admin INT,
    nom VARCHAR(256) NOT NULL,
    FOREIGN KEY (id_admin) REFERENCES Employe (id_employe)
);

ALTER TABLE Employe
ADD FOREIGN KEY (id_ligue) REFERENCES Ligue(id_ligue);
