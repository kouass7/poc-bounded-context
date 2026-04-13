package com.kouasseu.copro.charges.domain;

/**
 * Value Object : tantièmes dans le contexte "Charges".
 *
 * <p>Enrichi par rapport à {@code com.kouasseu.copro.lots.domain.Tantiemes} :
 * dans ce contexte, on a besoin du ratio calculé pour la répartition
 * des charges. Chaque contexte adapte le Value Object à ses besoins.
 */
public record Tantiemes(int valeur, int base) {

    public Tantiemes {
        if (valeur <= 0 || base <= 0) {
            throw new IllegalArgumentException("Les tantièmes doivent être des valeurs positives");
        }
    }

    /**
     * Ratio de répartition (entre 0.0 et 1.0).
     *
     * <p>Cette méthode n'existe que dans le contexte "Charges" — le contexte
     * "Lots" n'en a pas besoin. Chaque contexte enrichit le Value Object
     * selon son propre langage ubiquitaire.
     */
    public double ratio() {
        return (double) valeur / base;
    }
}
