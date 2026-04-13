package com.kouasseu.copro.lots.application;

import com.kouasseu.copro.lots.LotMute;
import com.kouasseu.copro.lots.LotService;
import com.kouasseu.copro.lots.domain.CoproprietaireId;
import com.kouasseu.copro.lots.domain.LotRepository;
import com.kouasseu.copro.lots.domain.NumeroLot;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Application Service : orchestrateur du cas d'usage "Enregistrement d'une mutation".
 *
 * <p>Implémente l'interface publique {@link LotService} — c'est via cette interface
 * que les appelants <em>externes</em> au module interagissent avec ce contexte.
 * {@code GestionDesLots} elle-même reste <strong>interne</strong> au module
 * (package {@code com.kouasseu.copro.lots.application}).
 *
 * <p><strong>Responsabilités de l'Application Service :</strong>
 * <ol>
 *   <li>Charger l'agrégat via le repository (port)</li>
 *   <li>Déléguer la logique métier à l'agrégat {@link com.kouasseu.copro.lots.domain.Lot}</li>
 *   <li>Publier l'événement de domaine via {@link ApplicationEventPublisher}</li>
 * </ol>
 *
 * <p><strong>Rôle de {@code @Transactional} avec Spring Modulith :</strong>
 * La séquence "charger → muter → persister → publier événement" s'exécute dans
 * une transaction unique. Spring Modulith garantit que les listeners annotés
 * {@code @ApplicationModuleListener} (dans {@code charges} et {@code assemblee})
 * ne s'exécutent qu'<strong>après le commit réussi</strong> de cette transaction.
 * Si la transaction est rollbackée, les listeners ne reçoivent rien — cohérence garantie.
 */
@Service
@Transactional
class GestionDesLots implements LotService {

    private final LotRepository repository;
    private final ApplicationEventPublisher events;

    GestionDesLots(LotRepository repository, ApplicationEventPublisher events) {
        this.repository = repository;
        this.events = events;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Flux d'exécution :
     * <ol>
     *   <li>Charge le {@code Lot} (agrégat) depuis le repository</li>
     *   <li>Appelle {@code lot.muter()} — le domaine applique la règle métier
     *       et <strong>retourne</strong> l'événement (sans le publier lui-même)</li>
     *   <li>Publie l'événement {@link LotMute} via Spring's event bus</li>
     * </ol>
     *
     * <p>Grâce à {@code @Transactional} + Spring Modulith Event Publication Registry,
     * si ce service plante après la publication mais avant le commit,
     * l'événement ne sera PAS distribué aux listeners. Aucune incohérence possible.
     */
    @Override
    public void enregistrerMutation(String numeroLot, String acquereurId, LocalDate date) {
        var lot = repository.parNumero(new NumeroLot(numeroLot));

        // L'agrégat produit l'événement → respecte le principe de responsabilité unique
        // Le domaine reste indépendant des mécanismes de messagerie Spring
        var event = lot.muter(new CoproprietaireId(acquereurId), date);

        // Publication après-commit garantie par Spring Modulith
        // Les contextes "charges" et "assemblee" réagiront via @ApplicationModuleListener
        events.publishEvent(event);
    }
}
