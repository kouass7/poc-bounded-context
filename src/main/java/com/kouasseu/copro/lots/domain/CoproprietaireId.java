package com.kouasseu.copro.lots.domain;

/**
 * Value Object : identifiant opaque d'un copropriétaire dans le contexte "Lots".
 *
 * <p>Ce type est <em>interne</em> au module {@code lots}. Les autres contextes
 * n'ont pas accès à ce type et travaillent avec des {@code String} bruts.
 *
 * <p>En production, cet identifiant référencerait un agrégat "Copropriétaire"
 * dans un éventuel contexte "Personnes", mais pour ce POC il est simplement
 * un identifiant textuel opaque.
 */
public record CoproprietaireId(String value) {

    public CoproprietaireId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("L'identifiant copropriétaire ne peut pas être vide");
        }
    }
}
