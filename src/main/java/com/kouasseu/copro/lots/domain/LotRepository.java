package com.kouasseu.copro.lots.domain;

/**
 * Port (interface) : contrat que le domaine impose à l'infrastructure.
 *
 * <p>Le domaine définit <em>ce dont il a besoin</em>, sans savoir comment
 * c'est implémenté (principe d'inversion de dépendance — DIP).
 *
 * <p>L'implémentation de ce POC ({@code LotRepositoryEnMemoire}) est dans
 * {@code com.kouasseu.copro.lots.application} — une implémentation JPA/MongoDB serait
 * dans une couche infrastructure séparée en production.
 */
public interface LotRepository {

    /**
     * Charge un lot par son numéro.
     *
     * @throws IllegalArgumentException si le lot n'existe pas
     */
    Lot parNumero(NumeroLot numero);

    /** Persiste un lot (création ou mise à jour). */
    void sauvegarder(Lot lot);
}
