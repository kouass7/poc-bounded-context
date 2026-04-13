package com.kouasseu.copro.lots.application;

import com.kouasseu.copro.lots.domain.CoproprietaireId;
import com.kouasseu.copro.lots.domain.Lot;
import com.kouasseu.copro.lots.domain.LotRepository;
import com.kouasseu.copro.lots.domain.NatureLot;
import com.kouasseu.copro.lots.domain.NumeroLot;
import com.kouasseu.copro.lots.domain.Tantiemes;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Adaptateur infrastructure : implémentation en mémoire du repository de lots.
 *
 * <p><strong>POC uniquement</strong> — remplacer par une implémentation JPA/MongoDB
 * en production. Spring injectera automatiquement cette implémentation partout où
 * {@link LotRepository} est demandé (principe d'inversion de dépendance).
 *
 * <p>Visibilité {@code package-private} ({@code class} sans {@code public}) :
 * seule la couche {@code application} peut instancier cet adaptateur.
 * Les autres modules ne peuvent pas y accéder directement.
 */
@Repository
class LotRepositoryEnMemoire implements LotRepository {

    private final Map<String, Lot> store = new HashMap<>();

    /**
     * Données initiales pour la démo — simule un parc de copropriété existant.
     *
     * <p>En production, ces données viendraient d'une base de données.
     */
    @PostConstruct
    void initialiserJeuxDeDonnees() {
        var lots = new Lot[]{
            new Lot(
                new NumeroLot("A01"),
                NatureLot.APPARTEMENT,
                new Tantiemes(250, 10000),   // 2.5% des charges générales
                new CoproprietaireId("proprio-martin")
            ),
            new Lot(
                new NumeroLot("A02"),
                NatureLot.APPARTEMENT,
                new Tantiemes(300, 10000),
                new CoproprietaireId("proprio-dupont")
            ),
            new Lot(
                new NumeroLot("P01"),
                NatureLot.PARKING,
                new Tantiemes(50, 10000),
                new CoproprietaireId("proprio-martin")  // Martin possède aussi un parking
            ),
        };

        for (Lot lot : lots) {
            store.put(lot.getNumero().value(), lot);
        }
    }

    @Override
    public Lot parNumero(NumeroLot numero) {
        var lot = store.get(numero.value());
        if (lot == null) {
            throw new IllegalArgumentException("Lot introuvable dans le contexte 'Lots' : " + numero.value());
        }
        return lot;
    }

    @Override
    public void sauvegarder(Lot lot) {
        store.put(lot.getNumero().value(), lot);
    }
}
