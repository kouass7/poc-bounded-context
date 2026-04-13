package com.kouasseu.copro.charges.domain;

/**
 * Clé de répartition des charges — concept central du contexte "Charges".
 *
 * <p>En droit de la copropriété, les charges sont réparties selon différentes
 * clés correspondant à des parties communes spécifiques. Un lot peut contribuer
 * à différents taux selon la clé (ex: un appartement du RDC ne paie pas l'ascenseur).
 *
 * <p>Ce concept n'existe ni dans le contexte "Lots" (qui gère la propriété)
 * ni dans le contexte "AG" (qui gère les votes). Illustration parfaite du
 * Bounded Context : chaque contexte a ses propres concepts métier.
 */
public enum CleDeRepartition {

    /** Charges de l'ensemble de l'immeuble (entretien, gardien, assurance...). */
    CHARGES_GENERALES,

    /** Charges spécifiques à l'ascenseur (entretien, contrat...). */
    ASCENSEUR,

    /** Charges liées au chauffage collectif. */
    CHAUFFAGE_COLLECTIF,

    /** Charges liées à l'eau froide. */
    EAU_FROIDE
}
