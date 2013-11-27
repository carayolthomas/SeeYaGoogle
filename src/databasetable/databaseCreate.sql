DROP DATABASE RI;
CREATE DATABASE RI;
use RI;

CREATE TABLE Document
( 
	idDocument INT UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (idDocument),
	nomDocument VARCHAR(50) NOT NULL
);


CREATE TABLE Conteneur
( 
	idConteneur INT UNSIGNED NOT NULL AUTO_INCREMENT,
 	idDocument INT UNSIGNED NOT NULL,
	FOREIGN KEY (idDocument) REFERENCES Document (idDocument),
	PRIMARY KEY (idConteneur, idDocument),
	xpathConteneur VARCHAR(50),
	typeConteneur VARCHAR(20) NOT NULL
);

CREATE TABLE Terme 
(
	idTerme INT UNSIGNED NOT NULL AUTO_INCREMENT,
	nomTerme VARCHAR(30) NOT NULL,
	idConteneur INT UNSIGNED NOT NULL,
	PRIMARY KEY (idTerme, idConteneur)
);	

CREATE TABLE Occurrence 
(
	idConteneur INT UNSIGNED NOT NULL,
	nomTerme VARCHAR(30) NOT NULL,
	FOREIGN KEY (idConteneur) REFERENCES Conteneur (idConteneur),
	PRIMARY KEY (idConteneur, nomTerme),
	nbOccurrence INT UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE Position
(
	idConteneur INT UNSIGNED NOT NULL,
	idTerme INT UNSIGNED NOT NULL,
	position INT UNSIGNED NOT NULL,
	FOREIGN KEY (idConteneur) REFERENCES Conteneur (idConteneur),
	FOREIGN KEY (idTerme) REFERENCES Terme (idTerme),
	PRIMARY KEY (idConteneur, idTerme, position)
);
