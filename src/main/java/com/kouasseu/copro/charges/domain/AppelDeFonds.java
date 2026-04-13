package com.kouasseu.copro.charges.domain;

/**
 * Value Object : un appel de fonds trimestriel ou exceptionnel.
 *
 * <p>Un appel de fonds est défini par :
 * <ul>
 *   <li>Une clé de répartition (qui paie quoi)</li>
 *   <li>Un montant total à répartir entre les copropriétaires</li>
 * </ul>
 *
 * <p>C'est à partir d'un {@code AppelDeFonds} que {@link LotChargeable#calculerQuotePart}
 * détermine la contribution individuelle de chaque lot.
 */
public record AppelDeFonds(CleDeRepartition cle, Montant montantTotal) {

    public AppelDeFonds {
        if (cle == null) throw new IllegalArgumentException("La clé de répartition est obligatoire");
        if (montantTotal == null) throw new IllegalArgumentException("Le montant total est obligatoire");
    }
}
