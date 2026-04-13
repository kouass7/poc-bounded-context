package com.kouasseu.copro.charges.application;

import com.kouasseu.copro.charges.domain.CleDeRepartition;
import com.kouasseu.copro.charges.domain.CoproprietaireId;
import com.kouasseu.copro.charges.domain.LotChargeable;
import com.kouasseu.copro.charges.domain.LotChargeableRepository;
import com.kouasseu.copro.charges.domain.NumeroLot;
import com.kouasseu.copro.charges.domain.Tantiemes;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implémentation en mémoire du repository "Charges" — POC uniquement.
 *
 * <p>Pré-peuplé avec les mêmes lots que {@code LotRepositoryEnMemoire},
 * mais avec une représentation propre au contexte "Charges" :
 * pas de NatureLot, pas de tantièmes bruts, mais des quotes-parts par clé de répartition.
 *
 * <p>Illustre la duplication <em>volontaire et souhaitable</em> des données
 * entre contextes : chaque contexte maintient sa propre cohérence.
 */
@Repository
class LotChargeableRepositoryEnMemoire implements LotChargeableRepository {

    private final Map<String, LotChargeable> store = new HashMap<>();

    @PostConstruct
    void initialiserJeuxDeDonnees() {
        // Lot A01 — appartement, contribue aux charges générales ET à l'ascenseur
        var a01 = new LotChargeable(
            new NumeroLot("A01"),
            new CoproprietaireId("proprio-martin"),
            Map.of(
                CleDeRepartition.CHARGES_GENERALES, new Tantiemes(250, 10000),
                CleDeRepartition.ASCENSEUR, new Tantiemes(300, 10000)
            )
        );

        // Lot A02 — appartement, contribue aux charges générales ET à l'ascenseur
        var a02 = new LotChargeable(
            new NumeroLot("A02"),
            new CoproprietaireId("proprio-dupont"),
            Map.of(
                CleDeRepartition.CHARGES_GENERALES, new Tantiemes(300, 10000),
                CleDeRepartition.ASCENSEUR, new Tantiemes(350, 10000)
            )
        );

        // Lot P01 — parking, contribue aux charges générales UNIQUEMENT
        // (pas d'ascenseur pour un parking → clé ASCENSEUR absente)
        var p01 = new LotChargeable(
            new NumeroLot("P01"),
            new CoproprietaireId("proprio-martin"),
            Map.of(
                CleDeRepartition.CHARGES_GENERALES, new Tantiemes(50, 10000)
            )
        );

        store.put("A01", a01);
        store.put("A02", a02);
        store.put("P01", p01);
    }

    @Override
    public Optional<LotChargeable> parNumero(String numeroLot) {
        return Optional.ofNullable(store.get(numeroLot));
    }

    @Override
    public void sauvegarder(LotChargeable lot) {
        store.put(lot.getNumero().value(), lot);
    }
}
