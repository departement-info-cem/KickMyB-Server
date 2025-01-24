# KickMyB-Server

Ce serveur sert dans le cadre de cours au collège Edouard Montpetit. Il sert dans le cadre des cours de programmation mobile. Les étudiants doivent 
programmer un client qui se connecte à ce serveur.

# Déploiement

[Déploiement sur Render](deploiement-render.md)

ANCIEN [Déploiement sur Azure](deploiement-azure.md)

# Fonctionnalités Points d'entrées

## Créer un compte :: POST /api/id/signup
```json title="POST /api/id/signup"
{
  "username" : "MrPipo",
  "password" : "pipo"
}
```
## S'inscrire avec un compte existant :: POST /api/id/signin
```json title="POST /api/id/signin"
{
  "username" : "MrPipo",
  "password" : "pipo"
}
```

## Ajouter une tâche au compte actuel (nécessite d'être connecté) :: POST /api/add
```json title="POST /api/add"
{
  "name" : "test",
  "deadline" : "2069-05-24T12:12:12"
}
```

## Voir les tâches du compte actuel (nécessite d'être connecté) :: GET /api/home
```json title="GET /api/home"

```


# changements pour passer à Spring Boot 3 
- Sauvegarde explicite du contexte de sécurité au signin et signout
- Changement de la syntaxe pour la config Security