# Exercices - TP4

## Rétrospective finale
### Processus

1. Décrivez 2 problématiques que possèdent votre processus et développez 2 plans distincts afin de les résoudre. Soyez constructifs dans vos critiques et évitez de mettre la faute sur une ou un groupe de personnes en particulier.
    - Nous avons remarqué que les rencontres prenaient plus de temps que prévu, puisque le code des PR n'était pas encore mergé avant le début de la rencontre. En plus de finir les issues pour le temps convenu, il faudrait faire les PR aussi.
    - Il faudrait aussi faire plus d'une rencontre par semaine pour qu'elles soient plus productives et moins longues.


2. Décrivez la démarche que vous aviez entreprise afin d'intégrer de nouveaux outils technologiques. Quelles étaient les étapes du processus? Comment avez-vous réagi aux différents bogues? Exploriez-vous à l'aide de tests unitaires ou manuels? Qu'avez-vous appris suite à cette démarche?
   - Afin d’intégrer les différentes technologies, nous nous sommes référés à la documentation en ligne. Il fallait installer Docker et MongoCompass et ensuite comprendre à quoi ils servaient et comment ils fonctionnaient. 
   - Face aux bogues, il y a eu beaucoup de frustrations, mais cela nous a permis d’améliorer notre capacité à faire des recherches afin de comprendre la raison des bogues et trouver des solutions. 
   - Pour l’exploration, nous utilisions les tests manuels. Nous avons appris qu'il est difficile d’avoir un code qui répond exactement à nos attentes dès la première itération. C’est donc primordial de faire des tests qui couvrent toutes les possibilités de résultat afin de mettre en évidence toutes les pistes d’améliorations et de gérer tous les cas d’erreurs.


3. Quels sont les bons coups de votre équipe? De quelles parties êtes-vous fiers? Nommez-en 3.
   - La correction de l’architecture qui a dû être faite au cours du TP3.
   - La validation extensive des paramètres faits à travers toute l'application.
   - L'abondance de tests unitaires qui ont été créés.


4. Quel conseil donneriez-vous aux prochains étudiants qui doivent faire ce projet? 
   - Commencer par structurer l’application en équipe pour ne pas avoir à la retravailler plus tard.


5. Quels apprentissages, trucs ou techniques appris dans ce projet croyez-vous pouvoir utiliser plus tard? Décrivez-en au moins 2. Cela peut être des apprentissages techniques, pratiques, sur le travail d'équipe ou encore par rapport au processus.
   - Le fonctionnement du CI/CD et son utilisation
   - Comment utiliser Docker
   - L'utilisation d’une méthodologie agile pour la réalisation d’un projet (ex. PRs)

### Outils d'intelligence artificielle

Avec les avancées récentes en matière d'intelligence artificielle, de nombreux services permettent désormais de faciliter le développement logiciel. Que ce soit de la pose de questions à la suggestion d'idées, l'IA est un nouvel outils pour les programmeurs et fait partie de la vie quotidienne.

1. Avez-vous utilisé, dans le cadre du cours, du projet ou de façon personnelle, un outil d'intelligence artificielle (style ChatGPT) pour vous aider à programmer?
   - Pour les technologies que l’on connaissait moins, nous avons utilisé ChatGPT pour obtenir des réponses précises et rapides

- Si oui, lequel et quelle utilisation en faisiez-vous? (ex: qualité de code, exploration, résolution de bogue, etc.).
   - Questions sur la mise en place du CI.
   - Questions sur la mise en place de mongo dans l’application.


2. Quel est le principal avantage qu'offre une telle technologie? Expliquez en quelques phrases.
   - Elle nous permet d’obtenir des explications précises sur un problème en allant elle-même chercher l’information. Ça facilite la recherche de solutions aux situations de configuration frustrante. Cela nous sauve aussi beaucoup de temps dans la recherche de solutions. Aussi, les sections moins claires de la réponse générée peuvent être réexpliquées en d'autres termes par la suite.


3. Quels sont les désavantages potentiels d'utiliser une telle technologie?
   - Elle peut nous guider vers une méthode ou une réponse qui ne fonctionnent pas ou qui ne fait pas exactement ce que l’on veut. Il faut s’assurer que le tout fonctionne avec les versions des technologies utilisées dans le projet. Si l’on ne prend pas le temps de comprendre le fonctionnement de la réponse donnée, nous perdons un niveau de compréhension de l’application, ce qui rend le tout plus difficile à déboguer.


4. Selon vous, aurons-nous toujours besoin de spécialistes en développement logiciel malgré cette technologie?
   - Pour le temps présent, l’intelligence artificielle n’est pas assez puissante pour pouvoir remplacer les développeurs logiciels.
     - Tout d’abord, le manque de jugement de cette dernière rend la résolution de problème non idéale.
     - L’intelligence artificielle n’est pas fiable à 100%. Elle ne comprend pas toujours ce qu’on veut exactement.
     - On pense que l’intelligence artificielle va aider les développeurs, mais ne pourra pas les remplacer totalement.
     - L’intelligence artificielle ne peut pas comprendre les besoins utilisateurs d’une application. 

## Outils d'analyse de code (SCA)

### La qualité du code - Qodana

<details>
  <summary>Analyse sommaire</summary>

![Image](../images/qualite_code_sommaire.png)
</details>

<details>
  <summary>Analyse détaillée</summary>

![Image](../images/qualite_code_detaillee.png)
</details>


### La couverture des tests - JaCoCo

<details>
  <summary>Analyse sommaire</summary>

![Image](../images/couverture_tests_sommaire.png)
</details>

<details>
  <summary>Analyse détaillée</summary>

![Image](../images/couverture_tests_detaillee_1.png)
![Image](../images/couverture_tests_detaillee_2.png)
</details>

### La sécurité du code

### Github Project
<details>
  <summary>Capture d'écran</summary>

![Image](../images/github_project_TP4.png)
</details>

### Milestone
<details>
  <summary>Capture d'écran</summary>

![Image](../images/milestone_TP4.png)
</details>

### Issues
<details>
  <summary>Captures d'écran</summary>

![Image](../images/issue_1_TP4.png)
![Image](../images/issue_2_TP4.png)
![Image](../images/issue_3_TP4.png)
</details>

### Pull requests
<details>
  <summary>Captures d'écran - Pull request 1</summary>

![Image](../images/pull_request_1.1_TP4.png)
![Image](../images/pull_request_1.2_TP4.png)

</details>

<details>
  <summary>Captures d'écran - Pull request 2</summary>

![Image](../images/pull_request_2_TP4.png)
</details>

<details>
  <summary>Captures d'écran - Pull request 3</summary>

![Image](../images/pull_request_3_TP4.png)
</details>

### Arbre de commits
<details>
  <summary>Capture d'écran</summary>

![Image](../images/arbre_commits_TP4.png)
</details>

## Sentry
<details>
  <summary>Capture d'écran - Rapport sommaire</summary>

![Image](../images/sentry_issues_summary.png)
</details>

<details>

  <summary>Captures d'écran - Rapport détaillés</summary>

![Image](../images/sentry_issue_detailled_1.png)
![Image](../images/sentry_issue_detailled_2.png)
![Image](../images/sentry_issue_detailled_3.png)
</details>
