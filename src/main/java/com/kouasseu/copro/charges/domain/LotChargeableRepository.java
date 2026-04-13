package com.kouasseu.copro.charges.domain;

import java.util.Optional;

/**
 * Port : contrat de persistance du contexte "Charges".
 *
 * <p>Utilise {@link Optional} (et non une exception) pour le "non trouvé" :
 * un lot peut exister dans le contexte "Lots" mais pas encore être enregistré
 * dans le contexte "Charges" — les contextes évoluent de façon indépendante.
 */
public interface LotChargeableRepository {

    /** Recherche un lot chargeable par son numéro. Retourne {@link Optional#empty()} si absent. */
    Optional<LotChargeable> parNumero(String numeroLot);

    /** Persiste un lot chargeable. */
    void sauvegarder(LotChargeable lot);
}
