package com.kouasseu.copro.charges.application;

import com.kouasseu.copro.charges.domain.CoproprietaireId;
import com.kouasseu.copro.charges.domain.LotChargeableRepository;
import com.kouasseu.copro.lots.LotMute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Listener du contexte "Charges" : réagit aux mutations de lots.
 *
 * <hr>
 *
 * <p><strong>{@code @ApplicationModuleListener} vs {@code @EventListener} :</strong>
 *
 * <table border="1" cellpadding="5">
 *   <tr><th>@EventListener</th><th>@ApplicationModuleListener</th></tr>
 *   <tr><td>Synchrone, dans la transaction courante</td>
 *       <td>Asynchrone, après commit de la transaction émettrice</td></tr>
 *   <tr><td>Si ce handler plante : la transaction émettrice est rollbackée</td>
 *       <td>Si ce handler plante : l'événement est <strong>rejoué</strong> au redémarrage</td></tr>
 *   <tr><td>Pas de persistance des événements</td>
 *       <td>L'événement est persisté dans EVENT_PUBLICATION_REGISTRY (H2 ici)</td></tr>
 *   <tr><td>Risque de perte d'événement si crash</td>
 *       <td>Garantie <em>at-least-once delivery</em></td></tr>
 * </table>
 *
 * <p><strong>Découplage inter-contextes :</strong>
 * Ce listener importe {@link LotMute} depuis {@code com.kouasseu.copro.lots} (API publique)
 * mais <strong>jamais</strong> depuis {@code com.kouasseu.copro.lots.domain} (interne).
 * {@code ApplicationModules.verify()} vérifie cette contrainte à chaque build.
 *
 * <p><strong>Langage ubiquitaire :</strong>
 * Ce contexte dit "transfererCompte" là où le contexte "Lots" dit "muter".
 * Chaque contexte réagit avec son propre vocabulaire, même si le phénomène
 * métier sous-jacent est identique.
 */
// Nom explicite : deux classes portent le nom SurMutationDeLot dans des modules différents.
// Sans ce qualificatif, Spring dérive "surMutationDeLot" pour les deux → ConflictingBeanDefinitionException.
// C'est un artefact Spring (nommage par classe simple) et non une violation du Bounded Context :
// chaque module a LÉGITIMEMENT sa propre classe SurMutationDeLot avec son propre comportement.
@Component("surMutationDeLotCharges")
public class SurMutationDeLot {

    private static final Logger log = LoggerFactory.getLogger(SurMutationDeLot.class);

    private final LotChargeableRepository repository;

    public SurMutationDeLot(LotChargeableRepository repository) {
        this.repository = repository;
    }

    /**
     * Réagit à une mutation de lot en transférant le compte débiteur.
     *
     * <p>Résilience : si le lot n'est pas (encore) enregistré dans ce contexte,
     * on logue et on passe — les contextes sont autonomes et peuvent avoir
     * des états d'avancement différents.
     *
     * @param event l'événement publié par le contexte "Lots" après commit
     */
    @ApplicationModuleListener
    // ↑ Spring Modulith : exécution après commit de la transaction émettrice
    //   + rejeu automatique en cas d'échec (Event Publication Registry)
    public void reagir(LotMute event) {
        log.debug("[Charges] Réception de LotMute pour le lot {} → nouveau débiteur : {}",
            event.numeroLot(), event.nouveauProprietaire());

        // ⚠ Seul LotMute (com.kouasseu.copro.lots) est accessible ici.
        // Toute tentative d'accéder à com.kouasseu.copro.lots.domain.* ferait échouer verify().
        var lotOpt = repository.parNumero(event.numeroLot());

        if (lotOpt.isEmpty()) {
            // Scénario normal : le lot existe dans "Lots" mais pas encore dans "Charges"
            // (migration progressive, lot non assujetti aux charges communes, etc.)
            log.warn("[Charges] Lot {} non trouvé dans ce contexte — événement ignoré",
                event.numeroLot());
            return;
        }

        // Chaque contexte réagit avec SON vocabulaire :
        //   - "muter"            → langage de "Lots"
        //   - "transfererCompte" → langage de "Charges"  ← ici
        //   - "changerDeTitulaire" → langage de "AG"
        var lot = lotOpt.get();
        lot.transfererCompte(new CoproprietaireId(event.nouveauProprietaire()));

        repository.sauvegarder(lot);

        log.info("[Charges] Compte du lot {} transféré vers {} (mutation du {})",
            event.numeroLot(), event.nouveauProprietaire(), event.dateMutation());
    }
}
