# Football manager

POC permettant de gérer les transactions liées à un club de football.

---

## Table des matières

1. [Prérequis](#prerequis)
2. [Installation](#installation)
3. [Documentation](#documentation)
4. [Scénarios](#scénarios)
5. [Tests](#tests)
6. [Choix techniques](#choix-techniques)
7. [Temps passé](#temps-passe)

## 1. Prérequis

Avant d'exécuter ce projet, assurez-vous d'avoir les éléments suivants installés et configurés :

- **JDK 11+** : Java Development Kit version 11 ou supérieure.
- **Maven** : Outil de gestion de projet et de dépendances.
- **Git** : Système de contrôle de version pour cloner le repository.
- **Docker** : Permet d'exécuter d'une instance MySQL et PhpMyAdmin dans un conteneur.

## 2. Installation

1. Clonez le repository :
   ```bash
   git clone https://github.com/bennour/football-manager.git

2. Accédez à la racine du projet et exécutez la commande suivante pour compiler et installer les dépendances :
   ```bash
   mvn clean install

3. Toujours à la racine du projet, exécutez la commande suivante pour démarrer les services via Docker :
   ```bash
   docker-compose up -d 

Cette commande crée une instance MySQL et un client PhpMyAdmin pour visualiser et gérer la base de
données. Le client PhpMyAdmin est disponible sur le port [8081](http://localhost:8081). Les credentials d'accès
sont ***admin/admin***.

3. Enfin, pour démarrer l'application Spring Boot, exécutez la commande suivante :
   ```bash
   mvn spring-boot:run

Le projet est accessible sur le port [8080](http://localhost:8080)

## 3. Documentation

La documentation de l'API via Swagger est disponible à l'adresse
suivante : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

## 4. Scénarios

> ℹ️ **La base de données est pré-remplie de données équipes et joueurs.**

<details>
  <summary><strong>1. Lister les équipes et les trier par nom</strong></summary>

### 🟢 Requête

  ```sh
  curl --location 'localhost:8080/teams?sort=name&direction=asc'
```

### 📩 Réponse (code 200)

  ```json
  {
  "content": [
    {
      "id": 2,
      "name": "Nice",
      "acronym": "OGC",
      "budget": 10.0
    },
    {
      "id": 3,
      "name": "Olympique de Marseille",
      "acronym": "OM",
      "budget": 40.0
    },
    {
      "id": 1,
      "name": "Paris Saint-Germain",
      "acronym": "PSG",
      "budget": 100.0
    }
  ],
  "totalPages": 1,
  "totalElements": 3,
  "currentPage": 0
}
```

</details>


<details>
  <summary><strong>2. Créer une équipe avec des joueurs</strong></summary>

### 🟢 Requête

  ```sh
curl --location 'localhost:8080/teams' \
--header 'Content-Type: application/json' \
--data '{
  "name": "Monaco",
  "acronym": "ASM",
  "players": [
    { "name": "Aleksandr Golovin", "position": "MIDFIELDER" },
    { "name": "Thilo Kehrer", "position": "DEFENDER" }
  ],
  "budget": 75
}
'
```

### 📩 Réponse (code 201)

  ```json
  {
  "id": 4,
  "name": "Monaco",
  "acronym": "ASM",
  "players": [
    {
      "id": 6,
      "name": "Aleksandr Golovin",
      "position": "MIDFIELDER"
    },
    {
      "id": 7,
      "name": "Thilo Kehrer",
      "position": "DEFENDER"
    }
  ],
  "budget": 75.0
}
```

</details>


<details>
  <summary><strong>3. Info d'une équipe</strong></summary>

### 🟢 Requête

  ```sh
curl --location 'localhost:8080/teams/4'
```

### 📩 Réponse (code 200)

  ```json
 {
  "id": 4,
  "name": "Monaco",
  "acronym": "ASM",
  "budget": 75.0
}
```

</details>


<details>
  <summary><strong>4. Info d'une équipe complète (avec joueurs)</strong></summary>

### 🟢 Requête

  ```sh
curl --location 'localhost:8080/teams/4/players'
```

### 📩 Réponse (code 200)

  ```json
{
  "id": 4,
  "name": "Monaco",
  "acronym": "ASM",
  "players": [
    {
      "id": 6,
      "name": "Aleksandr Golovin",
      "position": "MIDFIELDER"
    },
    {
      "id": 7,
      "name": "Thilo Kehrer",
      "position": "DEFENDER"
    }
  ],
  "budget": 75.0
}
```

</details>


<details>
  <summary><strong>5. Ajout d'un joueur dans une équipe</strong></summary>

### 🟢 Requête

  ```sh
curl --location 'localhost:8080/teams/3/players' \
--header 'Content-Type: application/json' \
--data '{
    "name" : "Mason Greenwood",
    "position": "FORWARD"
}'
```

### 📩 Réponse (code 201)

  ```json
{
  "id": 8,
  "name": "Mason Greenwood",
  "position": "FORWARD"
}
```

</details>


<details>
  <summary><strong>6. Transfert d'un joueur d'une équipe à une autre</strong></summary>

### 🟢 Requête

  ```sh
curl --location 'localhost:8080/transfer' \
--header 'Content-Type: application/json' \
--data '{
    "playerId" : 1,
    "sellerTeamId": 1,
    "buyerTeamId": 2,
    "transferAmount": 8
}'
```

### 📩 Réponse (code 200)

  ```json
```

On peut vérifier que le joueur appartient bien à la nouvelle équipe et que le budget des 2 équipes a évolué en fonction
du prix du transfert.
</details>
---

#### Quelques scénarios d'erreurs (non éxhaustifs)

<details>
  <summary><strong>1. Echec de transfert : l'équipe acheteuse ne dispose pas des fonds nécéssaires</strong></summary>

### 🟢 Requête

  ```sh
curl --location 'localhost:8080/transfer' \
--header 'Content-Type: application/json' \
--data '{
    "playerId" : 2,
    "sellerTeamId": 1,
    "buyerTeamId": 2,
    "transferAmount": 30
}'
```

### 📩 Réponse (code 422)

  ```json
Transfer failed due to insufficient funds. Nice current funds : 2.0
  ```

</details>


<details>
  <summary><strong>2. Echec de transfert : le joueur n'appartient pas à l'équipe vendeuse</strong></summary>

### 🟢 Requête

  ```sh
curl --location 'localhost:8080/transfer' \
--header 'Content-Type: application/json' \
--data '{
    "playerId" : 4,
    "sellerTeamId": 1,
    "buyerTeamId": 2,
    "transferAmount": 12
}'
```

### 📩 Réponse (code 400)

  ```json
Player Jonathan Clauss does not belong to selling team Paris Saint-Germain
  ```

</details>


<details>
  <summary><strong>3. Echec de création d'équipe : des champs invalides sont fournis</strong></summary>

### 🟢 Requête

  ```sh
curl --location 'localhost:8080/teams' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Rennes",
    "acronym": "Stade de Rennes"
}'
```

### 📩 Réponse (code 400)

  ```json
{
  "errors": [
    {
      "attributes": "acronym",
      "message": "Acronym must be between 2 and 5 characters long."
    },
    {
      "attributes": "budget",
      "message": "Team budget cannot be null."
    }
  ]
}
  ```

</details>

## 5. Tests

Pour des contraintes de temps, j'ai uniquement réalisé des tests d'intégration sur les services
_**TeamService**_ et _**TransferService**_, en utilisant une base de données embarquée H2. Cette approche me semble
suffisante pour le POC.

## 6. Choix techniques

Le projet ne présente pas de spécificités architecturales nécessitant des explications détaillées. J'ai mis en place un
**Controller
Advice** pour centraliser la gestion des exceptions.
J'ai aussi utilisé un gestionnaire de logs pour tracer les événements dans la console. Les logs de niveaux
**INFO** et **DEBUG** sont gérés au niveau des services, et les logs **WARN** et **FATAL** sont traités par le
**Controller Advice**.
Concernant la base de données j'ai fais le choix d'indexer les champs **name**, **acronym** et **budget** de la table *
*teams**.
Ce choix est adapté pour un volume de données restreint, étant donnée que l'application cible uniquement les clubs de
ligue 1 alors il ne
devrait pas y avoir plus de 18 entrées dans cette table.

## 7. Temps passé

J'ai consacré un total de 3 jours à ce projet, répartis sur plusieurs sessions en fonction de mon emploi du temps.
