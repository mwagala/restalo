# Exercices - TP1

# Clean code
### Analyse de formatage

mvn spotless:apply

# Captures d'écran
<details>
  <summary>Captures d'écran</summary>

### Projet

![Image](https://github.com/GLO2003-H24-EQ33/restalo/assets/122502464/273f0ed9-155c-421d-855f-75c7f0a4ea4c)

### Milestone

![Image](https://github.com/GLO2003-H24-EQ33/restalo/assets/122502464/19b4a63a-af8f-46f1-a8d5-5ef876545a66)

### Issues

![Image](https://github.com/GLO2003-H24-EQ33/restalo/assets/122502464/06b1515e-da87-4d71-b28d-d15b5498977c)

![Image](https://github.com/GLO2003-H24-EQ33/restalo/assets/122502464/285c888b-e445-4e7a-8a6a-fa3b64ed9409)

![Image](https://github.com/GLO2003-H24-EQ33/restalo/assets/122502464/36d3640e-5f87-4f0f-9f35-cf4a5b482630)

### Pull requests
***1:***

![Image](https://github.com/GLO2003-H24-EQ33/restalo/assets/122502464/ad7d59ee-0169-4ade-a54a-ec1b3d5f47cf)

![Image](https://github.com/GLO2003-H24-EQ33/restalo/assets/122502464/b2286cc4-486a-44d5-b2ed-1fa958407f6e)

***2:***

![Image](https://github.com/GLO2003-H24-EQ33/restalo/assets/122502464/09da37f0-eb6a-44d1-9ec4-0da7253a054f)

### Arbre de commits

![Image](https://github.com/GLO2003-H24-EQ33/restalo/assets/122502464/fc575232-5556-4177-aa7e-ad7f74182fce)

</details>

# Conventions Git
## Nommage des commits


### Comment nommer les commits selon leur type/section/grosseur/acteur/etc.

1. Le nom commence par : #

2. Mettre le numéro de l'issue dans le nom du commit

3. Ajouter un : “-” entouré d’espaces

4. Ajouter une description générale de ce qu’on a fait dans le nom du commit tout en mettant des espaces entre les mots


### Exemple

Un commit en rapport à l’issue #1 :

- git commit -m “#1 – Correction ajout”



### Quoi et quand commiter?

- Le contenu du commit doit toujours être fonctionnel.

- Il faut commiter dès qu’une fonctionnalité ajoutée est fonctionnelle.

- Dès que l’on cesse de travailler sur le projet et que le tout est encore fonctionnel, on commit. Si ce n’est pas fonctionnel, il faut minimalement le mentionner dans la conversation d’équipe.



## Stratégie de branchage



### Quelles sont les branches de base (qui sont communes et qui existeront toujours) et quels sont leurs rôles (chacune) ?

#### Main

- Branche principale du repo

- Branche qui contiendra le code final d’un milestone

#### Branche de test

- Elle sert à tester le produit cumulatif des issues

- Elle sera seulement supprimée lorsque toutes les issues vont être complétées

#### Branche d’issue

- Elle sert à compléter une issue

- Elle sera seulement supprimée une fois l’issue complètement fonctionnelle et testée

### Quelle branche est LA branche principale (contenant le code officiellement intégré et pouvant être remis) ?

- C’est la branche main

### Quand est-ce qu’il faut créer une nouvelle branche ?

- Au début de chaque TP

- Au début de chaque issue



### Quand faire une demande de changement / d'intégration (pull request / merge request) et sur quelle branche la faire ?

- Lorsque la branche d'une issue est prête à être intégrée dans la branche de test
- À la fin de chaque TP, lorsque le projet est prêt à être remis