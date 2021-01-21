# Traffic Routier

Application Java simulant des véhicules circulants en ligne droite avec gestion des collisions.

Les vehicules étant arrivés à destination ainsi que les véhicules accidentés sont supprimés de la carte tout les 3 cycles.

Deux nouveaux véhicules apparaissent aléatoirement sur la carte tout les 3 cycles également. 

# Installation

Executez 
```
mvn clean install
mvn eclipse:eclipse
```
Pour importer les dépendances JavaFX dans le projet.

# Legende

| Couleur | Etat |
| ------ | ------ |
| Rouge | En circulation |
| Noir | Accidenté |
| Vert | Arrivé à destination |

# Exemple

![](view.gif)
