package com.kouasseu.copro.charges.domain;

/**
 * Value Object : numéro de lot dans le contexte "Charges".
 *
 * <p><strong>Modèles autonomes :</strong> Ce type est intentionnellement distinct
 * de {@code com.kouasseu.copro.lots.domain.NumeroLot}. Chaque contexte possède et contrôle
 * ses propres types. Si le contexte "Lots" décide de changer la structure de
 * {@code NumeroLot} (ajouter une validation, changer le format), le contexte
 * "Charges" n'est pas impacté — les équipes peuvent évoluer indépendamment.
 */
public record NumeroLot(String value) {

    public NumeroLot {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Le numéro de lot ne peut pas être vide");
        }
    }
}
