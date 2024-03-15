/*
Nom du script : CreationTables.sql
Auteur : Djony
Date de création : 2024-01-02
Objectif : Ce script contient les requêtes de création des tables nécessaires pour le projet VDNGestion.
*/

CREATE DATABASE IF NOT EXISTS VDNGestion;
USE VDNGestion;

/* Cette variable est seulement accessible en lecture dans mySQL donc impossible de le set de cette manière.
*SET SESSION DATE_FORMAT = "%d-%m-%Y";
*/

CREATE TABLE Ligue (
    id_ligue INT PRIMARY KEY AUTO_INCREMENT,
    id_admin INT REFERENCES Employe (id_employe)
    nom VARCHAR(256) NOT NULL
);

CREATE TABLE Employe (
    id_employe INT,
    id_ligue_administree INT,
    droit BOOL NOT NULL,
    nom VARCHAR(256) NOT NULL,
    prenom VARCHAR(256) NOT NULL,
    password VARCHAR(256) NOT NULL,
    mail VARCHAR(256) NOT NULL,
    date_arrive DATE NOT NULL,
    date_depart DATE
    PRIMARY KEY (id_employe, id_ligue_administree),
    FOREIGN KEY id_ligue_administree REFERENCES Ligue(id_ligue)
);

