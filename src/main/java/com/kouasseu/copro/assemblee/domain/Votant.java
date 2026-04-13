package com.kouasseu.copro.assemblee.domain;

import java.util.Objects;

/**
 * Objet du domaine : le copropriétaire en tant que participant à l'AG.
 *
 * <p>Dans ce contexte, un copropriétaire est un <em>votant potentiel</em>
 * (présent ou représenté). Cette vision est très différente des autres contextes :
 * <ul>
 *   <li>Dans "Lots" → il est un <em>propriétaire</em></li>
 *   <li>Dans "Charges" → il est un <em>débiteur</em></li>
 *   <li>Dans "AG" → il est un <em>votant</em></li>
 * </ul>
 */
public class Votant {

    private final String coproprietaireId;

    /** Présence physique à l'AG — initialement absent, marqué présent à l'émargement. */
    private boolean present;

    private Votant(String coproprietaireId, boolean present) {
        this.coproprietaireId = Objects.requireNonNull(coproprietaireId);
        this.present = present;
    }

    /** Crée un votant absent (état par défaut avant le début de l'AG). */
    public static Votant de(String coproprietaireId) {
        return new Votant(coproprietaireId, false);
    }

    /** Marque ce votant comme présent physiquement (émargement à l'AG). */
    public void marquerPresent() {
        this.present = true;
    }

    /**
     * Indique si ce votant est présent physiquement.
     *
     * <p>Un droit de vote est "représenté" si le votant est présent
     * OU s'il a donné pouvoir à un mandataire.
     *
     * @see DroitDeVote#estRepresente()
     */
    public boolean estPresent() {
        return present;
    }

    public String getCoproprietaireId() {
        return coproprietaireId;
    }
}
