package com.kouasseu.copro.lots;

import com.kouasseu.copro.CoproApplication;
import com.kouasseu.copro.lots.domain.CoproprietaireId;
import com.kouasseu.copro.lots.domain.Lot;
import com.kouasseu.copro.lots.domain.LotRepository;
import com.kouasseu.copro.lots.domain.NatureLot;
import com.kouasseu.copro.lots.domain.NumeroLot;
import com.kouasseu.copro.lots.domain.Tantiemes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test d'intégration du module "Lots".
 *
 * <p><strong>{@code @RecordApplicationEvents} :</strong>
 * Capture tous les événements Spring publiés durant le test via
 * {@code ApplicationEventPublisher}. Permet de vérifier que le Domain Event
 * {@link LotMute} est bien émis sans dépendre d'un broker externe.
 *
 * <p><strong>Ce test est dans {@code com.kouasseu.copro.lots} :</strong>
 * Il fait partie du module "Lots" et peut donc accéder aux sous-packages internes
 * ({@code domain}, {@code application}) sans violer les règles de Spring Modulith.
 * Un test dans {@code com.kouasseu.copro} (racine) n'aurait pas cet accès.
 */
@SpringBootTest(classes = CoproApplication.class)
@RecordApplicationEvents
class MutationDeLotTest {

    @Autowired
    LotService lotService;   // ← Interface publique (com.kouasseu.copro.lots) — pas GestionDesLots

    @Autowired
    LotRepository lotRepository;

    @Autowired
    ApplicationEvents applicationEvents;

    @Test
    void uneLotMuteDevraitEtrePublieLorsduneNouvelleAcquisition() {
        // GIVEN — un lot pré-existant (en dehors des données de démo)
        var numero = new NumeroLot("T99");
        var proprietaireInitial = new CoproprietaireId("proprio-test-init");
        var lot = new Lot(numero, NatureLot.APPARTEMENT, new Tantiemes(100, 10000), proprietaireInitial);
        lotRepository.sauvegarder(lot);

        // WHEN — mutation vers un nouvel acquéreur via l'API publique du module
        var dateActe = LocalDate.of(2025, 3, 15);
        lotService.enregistrerMutation("T99", "proprio-test-acquereur", dateActe);

        // THEN — un événement LotMute a été publié (les autres contextes l'écouteront)
        var evenementsPublies = applicationEvents.stream(LotMute.class).toList();

        assertThat(evenementsPublies)
            .as("Un événement LotMute doit être publié après une mutation")
            .hasSize(1);

        var evenement = evenementsPublies.get(0);
        assertThat(evenement.numeroLot()).isEqualTo("T99");
        assertThat(evenement.ancienProprietaire()).isEqualTo("proprio-test-init");
        assertThat(evenement.nouveauProprietaire()).isEqualTo("proprio-test-acquereur");
        assertThat(evenement.dateMutation()).isEqualTo(dateActe);
    }

    @Test
    void uneDoubleMutationDevraitTracerLesDeuxProprietaires() {
        // GIVEN — lot A01 pre-existant avec Martin comme propriétaire
        var lot = lotRepository.parNumero(new NumeroLot("A01"));
        var proprietaireOriginel = lot.getProprietaire().value();

        // WHEN — Martin vend à Durand, qui revend ensuite à Leblanc
        lotService.enregistrerMutation("A01", "proprio-durand", LocalDate.of(2025, 1, 1));
        lotService.enregistrerMutation("A01", "proprio-leblanc", LocalDate.of(2025, 6, 1));

        // THEN — deux événements ont été publiés, dans l'ordre chronologique
        var evenements = applicationEvents.stream(LotMute.class).toList();
        assertThat(evenements).hasSize(2);

        // Première mutation : Martin → Durand
        assertThat(evenements.get(0).ancienProprietaire()).isEqualTo(proprietaireOriginel);
        assertThat(evenements.get(0).nouveauProprietaire()).isEqualTo("proprio-durand");

        // Deuxième mutation : Durand → Leblanc
        assertThat(evenements.get(1).ancienProprietaire()).isEqualTo("proprio-durand");
        assertThat(evenements.get(1).nouveauProprietaire()).isEqualTo("proprio-leblanc");
    }
}
