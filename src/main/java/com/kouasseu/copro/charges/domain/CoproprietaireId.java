package com.kouasseu.copro.charges.domain;

/**
 * Value Object : identifiant du copropriétaire débiteur dans le contexte "Charges".
 *
 * <p>Dans ce contexte, un copropriétaire est avant tout un <em>débiteur</em> —
 * celui à qui on envoie les appels de fonds. La sémantique est différente
 * de {@code com.kouasseu.copro.lots.domain.CoproprietaireId} où c'est un <em>propriétaire</em>.
 * Même identifiant technique, sémantiques métier différentes selon le contexte.
 */
public record CoproprietaireId(String value) {

    public CoproprietaireId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("L'identifiant copropriétaire ne peut pas être vide");
        }
    }
}
