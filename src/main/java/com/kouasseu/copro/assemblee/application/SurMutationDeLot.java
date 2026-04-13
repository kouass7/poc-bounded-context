package com.kouasseu.copro.assemblee.application;

import com.kouasseu.copro.assemblee.domain.DroitDeVoteRepository;
import com.kouasseu.copro.assemblee.domain.Votant;
import com.kouasseu.copro.lots.LotMute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Listener du contexte "AG" : réagit aux mutations de lots.
 *
 * <p>Même événement {@link LotMute}, comportement différent :
 * <ul>
 *   <li>Contexte "Charges" → transfère le compte débiteur</li>
 *   <li>Contexte "AG" → change le titulaire du droit de vote  ← ici</li>
 * </ul>
 *
 * <p>Les deux contextes sont totalement indépendants l'un de l'autre.
 * Ils partagent l'événement {@link LotMute} (API publique de "Lots")
 * mais n'ont AUCUNE dépendance directe entre eux.
 *
 * <p><strong>Isolation des pannes :</strong> Si ce listener échoue (ex: lock en base),
 * grâce à {@code @ApplicationModuleListener}, l'événement est rejoué au redémarrage.
 * Le contexte "Charges" n'est PAS impacté par une panne dans le contexte "AG".
 */
// Nom explicite : voir com.kouasseu.copro.charges.application.SurMutationDeLot pour l'explication.
@Component("surMutationDeLotAssemblee")
public class SurMutationDeLot {

    private static final Logger log = LoggerFactory.getLogger(SurMutationDeLot.class);

    private final DroitDeVoteRepository repository;

    public SurMutationDeLot(DroitDeVoteRepository repository) {
        this.repository = repository;
    }

    /**
     * Réagit à une mutation de lot en changeant le titulaire du droit de vote.
     *
     * <p>Le nouvel acquéreur devient le titulaire du droit de vote pour ce lot.
     * La procuration éventuelle de l'ancien propriétaire est automatiquement annulée.
     */
    @ApplicationModuleListener
    // ↑ Exécution après commit + rejeu automatique en cas d'échec
    public void reagir(LotMute event) {
        log.debug("[AG] Réception de LotMute pour le lot {} → nouveau titulaire : {}",
            event.numeroLot(), event.nouveauProprietaire());

        var droitOpt = repository.parNumeroLot(event.numeroLot());

        if (droitOpt.isEmpty()) {
            log.warn("[AG] Droit de vote pour le lot {} non trouvé — événement ignoré",
                event.numeroLot());
            return;
        }

        var droit = droitOpt.get();

        // Langage ubiquitaire du contexte "AG" : "changerDeTitulaire"
        // Pas "muter" (Lots), pas "transfererCompte" (Charges)
        droit.changerDeTitulaire(Votant.de(event.nouveauProprietaire()));

        repository.sauvegarder(droit);

        log.info("[AG] Titulaire du droit de vote du lot {} changé vers {} (mutation du {})",
            event.numeroLot(), event.nouveauProprietaire(), event.dateMutation());
    }
}
