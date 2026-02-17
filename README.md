# Projet - Restalo

Le meilleur logiciel de réservation en restauration!

## Requis

- Java 21
- Maven 3.x

## Commandes

### Compilation

```
mvn compile
```

### Exécution

```
mvn exec:java
```

### Installer les dépendances
```
mvn install
```

### Démarrer l'image mongo localement
```
docker compose up -d
```

### Démarrer l'application en utilisant inmemory
```
mvn exec:java -D persistence=inmemory
```

### Démarrer l'application en utilisant mongo
```
set MONGO_CLUSTER_URL=mongodb://root:example@localhost:27017

set MONGO_DATABASE=restalo

mvn exec:java -D persistence=mongo
```

### Se connecter au registre docker
```
docker login ghcr.io -u <<VOTRE_NOM_UTILISATEUR_GITHUB>> -p <<VOTRE_TOKEN_PERSONNEL_GITHUB>>
```

### Prendre l'image déployée à partir du registre git
```
docker pull ghcr.io/glo2003-h24-eq33/restalo:remise3
```

### Démarrer l'image de l'application
```
docker run --rm -it -p 8080:8080 -e MONGO_CLUSTER_URL=mongodb://root:example@db:27017 --env MONGO_DATABASE=restalo --network <<nom du network utilisé par l'image mongo>> ghcr.io/glo2003-h24-eq33/restalo:remise3 mvn exec:java -D persistence=mongo
```


### Variables d'environnement Sentry
2 options:
1. Effectuer ces commandes dans le terminal
    ```
    set SENTRY_AUTH_TOKEN=sntrys_eyJpYXQiOjE3MTMzNzM4NzAuMjE4MzM3LCJ1cmwiOiJodHRwczovL3NlbnRyeS5pbyIsInJlZ2lvbl91cmwiOiJodHRwczovL3VzLnNlbnRyeS5pbyIsIm9yZyI6ImdsbzIwMDMtaDI0LWVxMzMifQ\=\=_LaJTAwoFXyl+zYBKY/WtRtu9ygpsBnI+vkIP8HZhVM8
    
    set SENTRY_DSN=https://e214bced68a4c1660e90b4c640481fb8@o4507102843699200.ingest.us.sentry.io/4507102964350976
    ```
2. Ajouter à votre IDE les variables d'environement suivante:
    #### SENTRY_AUTH_TOKEN
    ```
    sntrys_eyJpYXQiOjE3MTMzNzM4NzAuMjE4MzM3LCJ1cmwiOiJodHRwczovL3NlbnRyeS5pbyIsInJlZ2lvbl91cmwiOiJodHRwczovL3VzLnNlbnRyeS5pbyIsIm9yZyI6ImdsbzIwMDMtaDI0LWVxMzMifQ\=\=_LaJTAwoFXyl+zYBKY/WtRtu9ygpsBnI+vkIP8HZhVM8
    ```
    #### SENTRY_DSN
    ```
    https://e214bced68a4c1660e90b4c640481fb8@o4507102843699200.ingest.us.sentry.io/4507102964350976
    ```

## URLs des stories
### Créer un item dans le menu d'un restaurant
``` 
http://0.0.0.0:8080/restaurants/<Id du restaurant>/itemMenu

*Doit avoir comme header "owner" qui contient le ownerId du restaurant duquel vient l'item
*Doit contenir le body suivant
``` 
```json
{
    "name": String,
    "description": String,
    "price": Float
}
```
#### Exemple :
```json
{
  "name": "Spaghetti",
  "description": "Spaghetti aux tomates avec parmesan de fromage de chèvre.",
  "price": 34.44
}
```
### Obtenir le menu d'un restaurant
```
http://0.0.0.0:8080/restaurants/<Id du restaurant>/menu
``` 


### Supprimer un item du menu d'un restaurant
```
http://0.0.0.0:8080/restaurants/<Id du restaurant>/itemMenu/<Id de l'item>

*Doit avoir comme header "owner" qui contient le ownerId du restaurant duquel vient l'item
```  
