/*
Nom du script : CreationTables.sql
Auteur : Djony
Date de création : 2024-01-02
Objectif : Ce script contient les requêtes de création des tables nécessaires pour le projet AtelierProBts.
*/

CREATE DATABASE IF NOT EXISTS Atelier_BTS;
USE Atelier_BTS;
SET SESSION DATE_FORMAT = "%d-%m-%Y";

CREATE TABLE Ligue (
    id_ligue INT PRIMARY KEY,
    nom VARCHAR(256) NOT NULL
);

CREATE TABLE Employe (
    id_employe INT PRIMARY KEY,
    droit VARCHAR(256) NOT NULL,
    nom VARCHAR(256) NOT NULL,
    prenom VARCHAR(256) NOT NULL,
    password VARCHAR(256) NOT NULL,
    mail VARCHAR(256) NOT NULL,
    date_arrive DATE NOT NULL,
    date_depart DATE
);

CREATE TABLE Appartenir (
    id_employe INT,
    id_ligue INT,
    FOREIGN KEY (id_employe) REFERENCES Employe(id_employe),
    FOREIGN KEY (id_ligue) REFERENCES Ligue(id_ligue),
    PRIMARY KEY (id_employe, id_ligue)
);

CREATE TABLE Administrer (
    id_employe INT,
    id_ligue INT,
    FOREIGN KEY (id_employe) REFERENCES Employe(id_employe),
    FOREIGN KEY (id_ligue) REFERENCES Ligue(id_ligue),
    PRIMARY KEY (id_employe, id_ligue)
);