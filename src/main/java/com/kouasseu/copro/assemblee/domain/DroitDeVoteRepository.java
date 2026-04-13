package com.kouasseu.copro.assemblee.domain;

import java.util.List;
import java.util.Optional;

/**
 * Port : contrat de persistance du contexte "Assemblée Générale".
 */
public interface DroitDeVoteRepository {

    /** Recherche le droit de vote associé à un lot. */
    Optional<DroitDeVote> parNumeroLot(String numeroLot);

    /** Retourne tous les droits de vote (pour construire la feuille de présence). */
    List<DroitDeVote> tous();

    /** Persiste un droit de vote. */
    void sauvegarder(DroitDeVote droitDeVote);
}
