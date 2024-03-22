# Déploiement sur Render

Render est un service d'hébergement qui supporte plusieurs technos mais en particulier
les applications compatible avec [Docker](https://www.docker.com).

Le fichier qui rend notre application compatible avec Docker est le fichier `Dockerfile`.

## Etapes de préparation

1. Render va se connecter sur un repo Github que vous contrôlez pour faire le déploiement
2. Pour ça, vous allez devoir faire un fork du projet KickMyB-Server
3. Sur la page [du repo](https://github.com/departement-info-cem/KickMyB-Server)
4. Trouve le bouton Fork et appuie dessus
5. Le fork va être un repo qui t'appartient avec l'histoire du projet KickMyB-Server
6. On peut maintenant procéder au déploiement sur Render

## Etapes pour le déploiement en tant que tel

1. Aller sur le [dashboard de Render](https://dashboard.render.com)
2. Se connecter avec ton compte GitHub (le même que celui où tu as forké le projet)
3. Cliquer sur le bouton `New` en haut
4. Sélectionner `New Web Service` ou `Web Service`
5. Sélectionner le repo forké depuis le projet KickMyB-Server
6. Assure toi de lui donner
  - un nom pratique pour toi, nous on l'a appelé kmb-server
  - la branche est le "main"
  - rien à mettre dans le root directory
  - sélectionne le Runtime `Docker` si ce n'est pas déjà fait
  - sélectionne une instance gratuite ou autre si tu as du budget
7. Il ne devrait pas y avoir besoin des options "advanced"
8. Cliquer sur le bouton `Create Web Service`
9. Tu devrais arriver sur la page de ton service
10. Si le deploy a échoué, tu peux essayer le bouton "Manual Deploy" et sélectionner le dernier commit du repo

## Utilisation avec ton client

La page de ton service devrait te donner l'URL de ton service.

Il faut prendre en compte que le service s'arrête si rien ne se passe pendant un certain temps.

Donc, quand ton serveur va repartir, la première requête va prendre un certain temps.

Aussi, comme la BD est uniquement en mémoire vive, dès que le serveur repartira, la BD sera vide.

