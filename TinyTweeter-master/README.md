# TinyTweeter
# Par Matthias Goulley, Thomas Fonteneau, Maxime Loizon et Apolon Viera

## Mesures

### Ecriture
#### En local
Pour écrire un tweet avec 100 followers en moyenne le temps est de 6,48 ms avec une variance de 2,8.

Pour écrire un tweet avec 1000 followers en moyenne le temps est de 6,9 ms avec une variance de 3,7.

Pour écrire un tweet avec 5000 followers en moyenne le temps est de 17,7 ms avec une variance de 21,2.

#### Sur Google App Engine

Pour poster un tweet avec 100 followers il faut en moyenne 347 ms avec une variance de 3171.

Pour poster un tweet avec 300 followers le emps moyen est de 335 ms avec une variance de 1074.

### Lecture

Pour lire les 10 derniers message avec 100 followers le temps moyen est de 3,32 ms avec une variance de 0,87 , pour les 50 derniers messages avec 100 followers de 14,03 ms et une variance de 4,54. Et enfin pour les 100 derniers messages le temps est de 28,57 ms et une variance de 51,7.

Pour lire les 10 derniers message avec 1000 followers le temps moyen est de 3,35 ms avec une variance de 0,47 , pour les 50 derniers messages avec 100 followers de 13,22 ms et une variance de 4,56. Et enfin pour les 100 derniers messages le temps est de 27,51 ms et une variance de 34,4.

Pour lire les 10 derniers message avec 5000 followers le temps moyen est de 5,7 ms avec une variance de 2,32 , pour les 50 derniers messages avec 5000 followers de 16,54 ms et une variance de 7,27. Et enfin pour les 100 derniers messages le temps est de 26,25 ms et une variance de 16,32.

### Récuêration tweet d'un hashtag
#### En local 

Pour récupérer les 50 derniers messages le temps moyen est de 9,8ms avec une variance de 37,6.
Pour récupérer les 1000 derniers messages, le temps moyens est de 106ms et une variance de 88,7.

#### Sur Google App Engine

Pour récupérer les 50 derniers messages d'un tweet le temps moyen est de 59ms avec une variance de 1687.

# Conclusion

On peut donc conclure que notre appli scale car les temps pour poster un tweet avec 100 et 300 followers et quasiment le même.
Selon les tests en local, nous voyons aussi que pour accéder aux 10 derniers messages d'un utilisateur, que le temps moyen est relativement le même quelque soit le nombre de followers.
