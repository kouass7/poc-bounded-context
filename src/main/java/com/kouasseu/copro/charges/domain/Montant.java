package com.kouasseu.copro.charges.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object : montant monétaire dans le contexte "Charges".
 *
 * <p>Encapsule les calculs financiers avec une précision correcte ({@link BigDecimal}).
 * L'arrondi à 2 décimales est la règle métier standard pour les appels de fonds.
 */
public record Montant(BigDecimal valeur) {

    public Montant {
        if (valeur == null || valeur.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Un montant ne peut pas être négatif");
        }
    }

    public static Montant de(BigDecimal valeur) {
        return new Montant(valeur);
    }

    public static Montant de(double valeur) {
        return new Montant(BigDecimal.valueOf(valeur));
    }

    /**
     * Calcule la quote-part au prorata des tantièmes.
     *
     * <p>Exemple : {@code Montant.de(10_000).auProrata(new Tantiemes(150, 10000))}
     * = 150,00 €
     */
    public Montant auProrata(Tantiemes tantiemes) {
        return new Montant(
            valeur.multiply(BigDecimal.valueOf(tantiemes.ratio()))
                  .setScale(2, RoundingMode.HALF_UP)
        );
    }

    @Override
    public String toString() {
        return valeur.setScale(2, RoundingMode.HALF_UP) + " €";
    }
}
