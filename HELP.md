# Getting Started

### How to start
Partir le projet dans IntelliJ trouver la classe ServerApplication
Clique sur la flèche verte pour le partir

Envoie une requête en post sur https://localhost:8787/api/addbaby

Console de données au 
https://localhost:8787/h2-console
Ensuite coller la valeur de url trouvée dans DataSourceConfig

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Deploiement sur Heroku
https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku
* créer un compte heorku
* télécharger le cli
* en ligne de commandes : heroku login
* aller dans le dossier du projet : heroku create
* https://devcenter.heroku.com/articles/local-maven-dependencies local library fucks
* comment gérer le SSL va dépendre du déploiement