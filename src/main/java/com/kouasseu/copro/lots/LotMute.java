package com.kouasseu.copro.lots;

import org.springframework.modulith.events.Externalized;

import java.time.LocalDate;

/**
 * Domain Event : un lot a changé de propriétaire.
 *
 * <hr>
 *
 * <p><strong>Pourquoi ce fichier est dans {@code com.kouasseu.copro.lots} et non
 * {@code com.kouasseu.copro.lots.domain} :</strong><br>
 * Spring Modulith ne rend visibles aux autres modules que les classes du
 * <em>package racine</em> du module. Les sous-packages ({@code domain/},
 * {@code application/}) sont <strong>internes</strong> et inaccessibles depuis
 * l'extérieur, même avec une dépendance déclarée.
 * En plaçant {@code LotMute} ici, on crée une API publique minimale et maîtrisée.
 *
 * <p><strong>Pourquoi des {@code String} et non des Value Objects internes :</strong><br>
 * Si cet événement utilisait {@code NumeroLot} ou {@code CoproprietaireId} (types de
 * {@code com.kouasseu.copro.lots.domain}), les contextes récepteurs ({@code charges},
 * {@code assemblee}) auraient une dépendance compile-time sur le modèle interne
 * de {@code lots} — exactement ce que le Bounded Context cherche à éviter.
 * Les types primitifs ({@code String}, {@code LocalDate}) garantissent le découplage.
 *
 * <p><strong>{@code @Externalized} :</strong><br>
 * Marque cet événement comme éligible à la publication vers un broker externe
 * (Kafka, RabbitMQ via Spring Modulith Events). Sans broker configuré dans ce POC,
 * il reste en diffusion in-process. L'annotation documente l'<em>intention</em>
 * architecturale : dans un déploiement multi-services, cet événement traverserait
 * les frontières réseau.
 */
@Externalized("copro.lots.LotMute")
public record LotMute(

    /** Identifiant du lot muté (ex: "A12", "P03"). */
    String numeroLot,

    /** Identifiant du précédent propriétaire. */
    String ancienProprietaire,

    /** Identifiant du nouvel acquéreur. */
    String nouveauProprietaire,

    /** Date effective de la mutation (acte notarié). */
    LocalDate dateMutation

) {}
