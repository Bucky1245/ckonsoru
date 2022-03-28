# Projet ckonsoru
## Installation

1) initialisation de la base de données Postgres
   1) créer une base de données le_nom_de_ma_bdd_ckonsoru
   2) y restaurer le fichier `ckonsoru.sql`
   3) modifier le fichier ckonsoru\src\main\ressources\config.properties avec votre login postgres et votre mot de passe postgres comme indiqué ci-dessous. 
```
# configuration pour le mode bdd
# url, exemple : jdbc:postgresql://localhost/test
bdd.url=jdbc:postgresql://localhost/le_nom_de_ma_bdd_ckonsoru
bdd.login=mon_login_postgres
bdd.mdp=mon_mot_de_passe_postgres
```

Pour installer la première fois (récupération des drivers jdbc et bibliothèques jdom), lancez maven install

```
mvn install
```

## Utilisation

Pour lancer :

```
mvn exec:java
```

Ou exécuter la classe App.java