package com.kouasseu.copro.assemblee.domain;

import java.util.Objects;

/**
 * Entity du contexte "AG" : le lot vu comme un <em>droit de vote</em>.
 *
 * <p><strong>Modèle autonome — troisième représentation du même lot :</strong>
 * <pre>
 * com.kouasseu.copro.lots.domain.Lot          → lot = propriété (nature, tantièmes)
 * com.kouasseu.copro.charges.domain.LotChargeable → lot = unité de débit (quotes-parts)
 * com.kouasseu.copro.assemblee.domain.DroitDeVote → lot = droit de vote ← ici
 * </pre>
 *
 * <p>Ce modèle ne connaît PAS :
 * <ul>
 *   <li>La nature du lot (APPARTEMENT, PARKING...)</li>
 *   <li>Les tantièmes de charges</li>
 *   <li>Le débiteur des charges</li>
 * </ul>
 *
 * <p>Tout ce qui importe ici : <em>qui vote</em> et <em>combien de voix</em>.
 */
public class DroitDeVote {

    private final NumeroLot lot;

    /**
     * Nombre de voix attribuées à ce lot.
     * En droit français, proportionnel aux tantièmes généraux (immuable par lot).
     */
    private final Voix nombreDeVoix;

    /**
     * Titulaire actuel du droit de vote (le propriétaire en exercice).
     * Mis à jour lors d'une mutation de lot via l'événement {@code LotMute}.
     */
    private Votant titulaire;

    /**
     * Procuration éventuelle.
     * {@code null} si le titulaire vote directement ou est absent sans représentant.
     */
    private Pouvoir pouvoir;

    public DroitDeVote(NumeroLot lot, Voix nombreDeVoix, Votant titulaire) {
        this.lot = Objects.requireNonNull(lot);
        this.nombreDeVoix = Objects.requireNonNull(nombreDeVoix);
        this.titulaire = Objects.requireNonNull(titulaire);
        this.pouvoir = null;
    }

    /**
     * Indique si ce droit de vote sera exercé lors de l'AG.
     *
     * <p>Un droit de vote est représenté si :
     * <ul>
     *   <li>Le titulaire est présent physiquement, OU</li>
     *   <li>Il a donné pouvoir à un mandataire (procuration)</li>
     * </ul>
     *
     * <p>C'est le concept clé du contexte "AG" — absent des deux autres contextes.
     */
    public boolean estRepresente() {
        return titulaire.estPresent() || pouvoir != null;
    }

    /**
     * Change le titulaire du droit de vote suite à une mutation de lot.
     *
     * <p><strong>Langage ubiquitaire :</strong> dans ce contexte, on dit
     * "changer de titulaire" — pas "muter" (Lots) ni "transférer le compte" (Charges).
     *
     * <p>Le pouvoir est annulé lors du changement de titulaire :
     * la procuration de l'ancien propriétaire n'est plus valable.
     */
    public void changerDeTitulaire(Votant nouveauTitulaire) {
        this.titulaire = Objects.requireNonNull(nouveauTitulaire);
        this.pouvoir = null;  // La procuration de l'ancien propriétaire devient caduque
    }

    /**
     * Enregistre une procuration pour représenter ce lot à l'AG.
     */
    public void donnerPouvoir(Pouvoir pouvoir) {
        this.pouvoir = Objects.requireNonNull(pouvoir);
    }

    /**
     * Révoque la procuration (le propriétaire viendra personnellement).
     */
    public void revoquerPouvoir() {
        this.pouvoir = null;
    }

    public NumeroLot getLot() { return lot; }
    public Voix getNombreDeVoix() { return nombreDeVoix; }
    public Votant getTitulaire() { return titulaire; }
    public Pouvoir getPouvoir() { return pouvoir; }
}
