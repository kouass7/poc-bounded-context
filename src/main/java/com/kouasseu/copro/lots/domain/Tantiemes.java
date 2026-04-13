package com.kouasseu.copro.lots.domain;

/**
 * Value Object : quote-part d'un lot exprimée en tantièmes.
 *
 * <p>Exemple : {@code new Tantiemes(150, 10000)} = 150/10 000 = 1,5%.
 *
 * <p><strong>Modèles autonomes :</strong> Le contexte {@code charges} possède son
 * propre {@code Tantiemes} (avec une méthode {@code ratio()}) et le contexte
 * {@code assemblee} n'utilise pas de tantièmes du tout — il compte des voix.
 * Même concept métier, trois représentations adaptées à chaque contexte.
 */
public record Tantiemes(int valeur, int base) {

    public Tantiemes {
        if (valeur <= 0 || base <= 0) {
            throw new IllegalArgumentException("Les tantièmes doivent être des valeurs positives");
        }
        if (valeur > base) {
            throw new IllegalArgumentException(
                "Les tantièmes (%d) ne peuvent pas dépasser la base (%d)".formatted(valeur, base));
        }
    }
}
