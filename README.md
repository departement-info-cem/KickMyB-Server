# KickMyB-Server

Ce serveur sert dans le cadre de cours au collège Edouard Montpetit. Il sert dans le cadre des cours de programmation mobile. Les étudiants doivent 
programmer un client qui se connecte à ce serveur.

# Déploiement

[Déploiement sur Render](deploiement-render.md)

[Déploiement sur Azure](deploiement-azure.md)

# Fonctionnalités Points d'entrées

## Créer un compte
```json title="POST /api/id/signup"
{
  "username" : "MrPipo",
  "password" : "pipo"
}
```
## S'inscrire avec un compte existant
```json title="POST /api/id/signin"
{
  "username" : "MrPipo",
  "password" : "pipo"
}
```

## Ajouter une tâche au compte actuel (nécessite d'être connecté)
```json title="POST /api/add"
{
  "name" : "test",
  "deadline" : "2069-05-24T12:12:12"
}
```

## Voir les tâches du compte actuel (nécessite d'être connecté)
```json title="GET /api/home"

```
