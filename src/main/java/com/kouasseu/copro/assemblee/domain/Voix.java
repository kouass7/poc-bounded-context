package com.kouasseu.copro.assemblee.domain;

/**
 * Value Object : nombre de voix attribuées à un lot lors d'une AG.
 *
 * <p>En droit de la copropriété française (loi du 10 juillet 1965),
 * le nombre de voix est proportionnel aux tantièmes généraux du lot.
 * Un lot de 300/10000 dispose de 300 voix sur 10 000.
 *
 * <p>Ce concept est propre au contexte "AG" — ni "Lots" ni "Charges"
 * ne manipulent des voix. C'est une illustration directe du fait que
 * le même actif (le lot) donne lieu à des concepts métier différents
 * selon le contexte.
 */
public record Voix(int nombre) {

    public Voix {
        if (nombre <= 0) {
            throw new IllegalArgumentException("Le nombre de voix doit être positif");
        }
    }
}
