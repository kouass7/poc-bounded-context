# POC — Bounded Context avec Spring Modulith

> Projet co-construit avec [Claude Code](https://claude.ai/code) (Anthropic).

---

## Objectif

Ce projet est un **proof of concept pédagogique** dont l'unique objectif est d'illustrer le concept de **Bounded Context** issu du Domain-Driven Design (DDD), en s'appuyant sur **Spring Modulith** pour rendre les frontières vérifiables par le framework.

Il ne prétend pas être une application de production. Pas d'API REST, pas de persistance réelle, pas de sécurité — uniquement les patterns architecturaux à l'état pur.

---

## Cas pratique : gestion de copropriété

Le même actif métier — un **lot de copropriété** — est représenté de trois façons différentes selon le contexte qui l'observe :

| Bounded Context | Le lot est... | Langage ubiquitaire |
|---|---|---|
| **Lots** | une propriété (nature, tantièmes) | `muter()` |
| **Charges** | une unité de débit (quotes-parts par clé) | `transfererCompte()` |
| **Assemblée Générale** | un droit de vote (voix, présence) | `changerDeTitulaire()` |

Même concept, trois modèles, trois vocabulaires, trois équipes potentiellement indépendantes. C'est exactement ce que le Bounded Context rend possible.

---

## Structure du projet

```
com.kouasseu.copro/
├── lots/                          Bounded Context "Lots & Copropriétaires"
│   ├── LotMute.java               ← API publique : Domain Event (@Externalized)
│   ├── LotService.java            ← API publique : facade applicative
│   ├── package-info.java          ← @ApplicationModule(allowedDependencies = {})
│   ├── domain/                    ← INTERNE : Lot, NumeroLot, Tantiemes...
│   └── application/               ← INTERNE : GestionDesLots, repo en mémoire
│
├── charges/                       Bounded Context "Charges & Appels de fonds"
│   ├── package-info.java          ← @ApplicationModule(allowedDependencies = {"lots"})
│   ├── domain/                    ← INTERNE : LotChargeable, Montant, CleDeRepartition...
│   └── application/               ← INTERNE : SurMutationDeLot (@ApplicationModuleListener)
│
└── assemblee/                     Bounded Context "Assemblée Générale"
    ├── package-info.java          ← @ApplicationModule(allowedDependencies = {"lots"})
    ├── domain/                    ← INTERNE : DroitDeVote, Voix, Votant, Pouvoir...
    └── application/               ← INTERNE : SurMutationDeLot (@ApplicationModuleListener)
```

**Règle d'accès :**
- Package racine (`com.kouasseu.copro.lots`) = **API publique**, accessible depuis les autres modules
- Sous-packages (`lots.domain`, `lots.application`) = **internes**, inaccessibles depuis l'extérieur

---

## Concepts DDD illustrés

### Bounded Context
Trois packages indépendants. Chaque contexte possède son propre modèle du "lot" sans partage de classes entre contextes.

### Langage ubiquitaire
Le vocabulaire reflète celui des experts métier de chaque contexte :
- Notaire/syndic → `muter()`
- Comptable → `transfererCompte()`
- Président d'AG → `changerDeTitulaire()`

### Value Objects
`NumeroLot`, `Tantiemes`, `Voix`, `Montant` — immuables (records Java), égalité par valeur, validation à la construction. Chaque contexte définit les siens.

### Aggregate Root
`Lot` protège ses invariants et produit les Domain Events sans dépendance vers le mécanisme de publication.

### Domain Event
`LotMute` — record immuable placé dans le **package racine** du module `lots`, avec des types primitifs (`String`, `LocalDate`) et non des Value Objects internes. Cela garantit que les consommateurs restent indépendants du modèle interne.

### Communication inter-contextes par événements
```
lots/ publie LotMute
    → charges/application/SurMutationDeLot  réagit : transfererCompte()
    → assemblee/application/SurMutationDeLot réagit : changerDeTitulaire()
```
Zéro couplage direct entre `charges` et `assemblee`. Ils partagent uniquement l'événement.

---

## Ce que Spring Modulith apporte

### Sans Spring Modulith
Les frontières de Bounded Context sont une **convention d'équipe** — respectées si tout le monde y pense, violées dès qu'on est pressé.

### Avec Spring Modulith

**`@ApplicationModule`** sur chaque `package-info.java` déclare les dépendances autorisées :
```java
// charges/package-info.java
@ApplicationModule(allowedDependencies = {"lots"})
package com.kouasseu.copro.charges;
```

**`ApplicationModules.verify()`** verrouille les frontières à chaque build :
```java
// Un seul test suffit pour protéger toute l'architecture
@Test
void verifierFrontieresBoundedContexts() {
    ApplicationModules.of(CoproApplication.class).verify();
    // Échoue si charges/ importe com.kouasseu.copro.lots.domain.Lot
    // Échoue si un cycle de dépendances existe
    // Échoue si une dépendance non déclarée est utilisée
}
```

**`@ApplicationModuleListener`** remplace `@EventListener` avec des garanties transactionnelles :

| `@EventListener` | `@ApplicationModuleListener` |
|---|---|
| Synchrone, dans la transaction émettrice | Asynchrone, après commit |
| Si le handler plante → rollback de la transaction émettrice | Si le handler plante → rejeu automatique au redémarrage |
| Pas de persistance des événements | Event Publication Registry (table `EVENT_PUBLICATION`) |

**`@Externalized`** sur `LotMute` signale l'intention de publier vers un broker externe (Kafka, RabbitMQ) sans en dépendre dans ce POC.

---

## Lancer le projet

```bash
# Compiler et lancer les tests (inclut la vérification architecturale)
./mvnw test

# Démarrer l'application (H2 en mémoire, logs des événements inter-contextes)
./mvnw spring-boot:run
```

La console H2 est accessible sur `http://localhost:8080/h2-console` (JDBC URL : `jdbc:h2:mem:copro`).

---

## Limites volontaires de ce POC

Ce projet illustre **uniquement** le pattern Bounded Context. Les éléments suivants ont été intentionnellement omis pour ne pas diluer le propos :

- Pas d'API REST ni de contrôleurs
- Persistance en mémoire (pas de JPA, pas de base réelle)
- Pas d'authentification ni de sécurité
- Pas de gestion des erreurs métier avancée
- Pas d'architecture hexagonale complète (ports & adapters)
- Pas de tests de chaque contexte en isolation (`@ApplicationModuleTest`)

Ces aspects font l'objet de POC séparés.

---

## Stack technique

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Modulith 1.2.4** — vérification des modules, Event Publication Registry
- **H2** — base de données en mémoire (Event Store JDBC)
