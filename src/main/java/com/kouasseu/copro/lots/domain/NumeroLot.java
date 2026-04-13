package com.kouasseu.copro.lots.domain;

/**
 * Value Object : identité d'un lot par son numéro.
 *
 * <p><strong>Règles des Value Objects :</strong>
 * <ul>
 *   <li>Immuable (record Java garantit l'immutabilité)</li>
 *   <li>Égalité structurelle (par valeur, non par référence)</li>
 *   <li>Validation à la construction (invariant garanti en tout temps)</li>
 * </ul>
 *
 * <p><strong>Bounded Context :</strong> Ce type est <em>interne</em> au module {@code lots}.
 * Les contextes {@code charges} et {@code assemblee} NE CONNAISSENT PAS ce type.
 * Ils manipulent des {@code String} bruts reçus via l'événement {@link com.kouasseu.copro.lots.LotMute}.
 *
 * <p>Note : si chaque contexte définit son propre {@code NumeroLot}, c'est voulu —
 * chaque équipe garde le contrôle total de son modèle sans coordination inter-équipes.
 */
public record NumeroLot(String value) {

    public NumeroLot {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Le numéro de lot ne peut pas être vide");
        }
    }
}
