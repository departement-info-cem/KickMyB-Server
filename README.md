# KickMyB-Server
Spring Boot Server for KickMyB


# Déploiement

## Abonnement étudiant préalable
- **Assurez-vous d'avoir votre abonnement étudiant actif**. Pour ce faire, tentez d'accéder à la liste des logiciels disponibles sur Azure avec votre compte étudiant. Si tu en vois qu'une dizaine, c'est que ton abonnement étudiant n'est pas activité. Normalement, tu devrais en voir une centaine. Dans la page avec la liste des logiciels, il y aura un message en mauve apparaissant sur la page d'invitant à compléter ton inscription si ton abonnement n'était pas actif.

## Installations d'outils préalables
- Télécharger et installer le client Azure [ici](https://aka.ms/installazurecliwindows)
  - Tester l'installation dans un terminal en tapant l'instruction : az --version

- Télécharger le zip contenant Apache Maven 3.9.0 [ici](https://dlcdn.apache.org/maven/maven-3/3.9.0/binaries/apache-maven-3.9.0-bin.zip)
  - Dézipper le contenu dans un répertoire proche de la racine (ex : C:\apache-maven-3.9.0).
  - Ajouter à votre PATH, un chemin vers le répertoire "bin" contenu dans le répertoire (ex : C:\apache-maven-3.9.0\bin)
  - Tester l'installation dans un terminal en tapant l'instruction : mvn --version

## Configuration pour le déploiement
- Éditer le fichier pom.xml, vers la fin du fichier modifier les deux éléments XML suivant afin d'y rajouter les valeurs suivantes 

  - `<resourceGroup>4204n6kickmyb-NUMEROETUDIANT</resourceGroup>`
  - `<appName>4204n6kickmyb-NUMEROETUDIANT</appName>`

## Déploiement du serveur
- Dans un terminal, entrer la commande pour se connecter à son compte Azure :
  ```sh
  az login 
  ```
- Dans le même terminal, à la racine du projet Java représentant le serveur, entrer la commande pour préparer un package (artéfact à envoyer au serveur) : 
  ```sh
  mvn clean package
  ```
- Tester l'artefact sur le port 8080 après avoir faire la commande suivante : 
  ```sh
  mvn spring-boot:run
  ```
- Déployer le serveur sur Azure (ignorer les messages d'erreurs en lien avec Application Insights) :
  ```sh
  mvn azure-webapp:deploy
  ```
  
Que la force soit avec vous!