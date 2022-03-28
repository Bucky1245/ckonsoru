CREATE TABLE veterinaire (
	vet_id SERIAL PRIMARY KEY,
	vet_nom CHARACTER VARYING (150)
);

INSERT INTO veterinaire (vet_nom)
	VALUES ('L. Belacqua');
INSERT INTO veterinaire (vet_nom)
	VALUES ('L. Asriel');
INSERT INTO veterinaire (vet_nom)
	VALUES ('M. Coulter');

SELECT * FROM veterinaire;

CREATE TABLE jour (
	jou_id integer primary key,
	jou_libelle CHARACTER VARYING (100)
);

INSERT INTO jour (jou_id, jou_libelle)
	VALUES (1, 'lundi');
INSERT INTO jour (jou_id, jou_libelle)
	VALUES (2, 'mardi');
INSERT INTO jour (jou_id, jou_libelle)
	VALUES (3, 'mercredi');
INSERT INTO jour (jou_id, jou_libelle)
	VALUES (4, 'jeudi');
INSERT INTO jour (jou_id, jou_libelle)
	VALUES (5, 'vendredi');
INSERT INTO jour (jou_id, jou_libelle)
	VALUES (6, 'samedi');
INSERT INTO jour (jou_id, jou_libelle)
	VALUES (7, 'dimanche');
	
CREATE TABLE disponibilite (
	dis_id SERIAL PRIMARY KEY,
	vet_id integer references veterinaire(vet_id), 
	dis_debut time,
	dis_fin time,
	dis_jour integer references jour(jou_id) 
);

-- L. Belacqua
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Belacqua'),
			1,'15:00:00','19:00:00');
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Belacqua'),
			2,'08:00:00','12:00:00');
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Belacqua'),
			4,'08:00:00','12:00:00');
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Belacqua'),
			6,'08:00:00','12:00:00');

-- L. Asriel
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
			2,'14:00:00','19:00:00');
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
			3,'08:00:00','12:00:00');
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
			3,'14:00:00','19:00:00');
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
			4,'14:00:00','19:00:00');
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
			6,'08:00:00','12:00:00');
			
-- M. Coulter
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'M. Coulter'),
			1,'08:00:00','12:00:00');
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'M. Coulter'),
			3,'15:00:00','19:00:00');
INSERT INTO disponibilite (vet_id, dis_jour, dis_debut, dis_fin) 
	VALUES ((SELECT vet_id FROM veterinaire WHERE vet_nom = 'M. Coulter'),
			6,'08:00:00','12:00:00');
			
CREATE TABLE rendezvous (
	rv_id SERIAL PRIMARY KEY,
	vet_id integer REFERENCES veterinaire(vet_id),
	rv_debut timestamp,
	rv_client CHARACTER VARYING(150)
);

INSERT INTO rendezvous (vet_id, rv_debut, rv_client)
	VALUES((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
		  '2021-03-18 14:00:00',
		  'M. Lee');
INSERT INTO rendezvous (vet_id, rv_debut, rv_client)
	VALUES((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
		  '2021-03-18 14:20:00',
		  'M. Scoresby');	
INSERT INTO rendezvous (vet_id, rv_debut, rv_client)
	VALUES((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
		  '2021-03-18 17:00:00',
		  'M. Byrnison');	
INSERT INTO rendezvous (vet_id, rv_debut, rv_client)
	VALUES((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
		  '2021-03-04 16:20:00',
		  'M. Byrnison');	
INSERT INTO rendezvous (vet_id, rv_debut, rv_client)
	VALUES((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
		  '2021-05-27 18:20:00',
		  'M. Byrnison');	
INSERT INTO rendezvous (vet_id, rv_debut, rv_client)
	VALUES((SELECT vet_id FROM veterinaire WHERE vet_nom = 'L. Asriel'),
		  '2021-03-18 18:40:00',
		  'M. Parslow');
		  
-- affichage des disponibilités par véto
SELECT vet_nom, jou_libelle, dis_debut, dis_fin
FROM disponibilite
	INNER JOIN veterinaire
		ON veterinaire.vet_id = disponibilite.vet_id
	INNER JOIN jour
		ON jour.jou_id = dis_jour
ORDER BY vet_nom, dis_id

-- affiche les rendez-vous du client 'M. Byrnison'
SELECT rv_id, rv_debut, rv_client
FROM rendezvous
WHERE rv_client = 'M. Byrnison'
ORDER BY rv_debut DESC;

-- affichage des disponibilités par véto pour une date donnée
WITH creneauxDisponibles AS 
	(SELECT vet_nom, generate_series('18-03-2021'::date+dis_debut,
						   '18-03-2021'::date+dis_fin-'00:20:00'::time,
						   '20 minutes'::interval) debut
	FROM disponibilite
		INNER JOIN veterinaire
			ON veterinaire.vet_id = disponibilite.vet_id
	WHERE dis_jour = EXTRACT('DOW' FROM '18-03-2021'::date)
	ORDER BY vet_nom, dis_id),
	creneauxReserves AS 
	(SELECT vet_nom, rv_debut debut
	 FROM rendezvous
		INNER JOIN veterinaire
		ON veterinaire.vet_id = rendezvous.vet_id
		WHERE rv_debut 
		BETWEEN '18-03-2021'::date 
		AND '18-03-2021'::date +'23:59:59'::time),
	creneauxRestants AS
	(SELECT * FROM creneauxDisponibles
	EXCEPT
	SELECT * FROM creneauxReserves)
SELECT * FROM creneauxRestants
ORDER BY vet_nom, debut)
