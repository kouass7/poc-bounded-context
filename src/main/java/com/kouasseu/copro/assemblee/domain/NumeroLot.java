package com.kouasseu.copro.assemblee.domain;

/**
 * Value Object : identifiant d'un lot dans le contexte "Assemblée Générale".
 *
 * <p>Troisième version de "NumeroLot" — chaque contexte possède le sien.
 * Dans ce contexte, le numéro de lot sert uniquement à associer un droit de vote
 * à une unité de copropriété. Aucune donnée physique (nature, surface) n'est portée.
 */
public record NumeroLot(String value) {

    public NumeroLot {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Le numéro de lot ne peut pas être vide");
        }
    }
}
