package com.kouasseu.copro.assemblee.application;

import com.kouasseu.copro.assemblee.domain.DroitDeVote;
import com.kouasseu.copro.assemblee.domain.DroitDeVoteRepository;
import com.kouasseu.copro.assemblee.domain.NumeroLot;
import com.kouasseu.copro.assemblee.domain.Voix;
import com.kouasseu.copro.assemblee.domain.Votant;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implémentation en mémoire du repository "AG" — POC uniquement.
 *
 * <p>Illustre que chaque contexte maintient ses propres données, avec
 * sa propre structure (ici : droits de vote avec voix et titulaires).
 */
@Repository
class DroitDeVoteRepositoryEnMemoire implements DroitDeVoteRepository {

    private final Map<String, DroitDeVote> store = new HashMap<>();

    @PostConstruct
    void initialiserJeuxDeDonnees() {
        // En loi 1965 : les voix sont proportionnelles aux tantièmes généraux
        // Lot A01 : 250 tantièmes → 250 voix
        var a01 = new DroitDeVote(
            new NumeroLot("A01"),
            new Voix(250),
            Votant.de("proprio-martin")
        );

        // Lot A02 : 300 tantièmes → 300 voix
        var a02 = new DroitDeVote(
            new NumeroLot("A02"),
            new Voix(300),
            Votant.de("proprio-dupont")
        );

        // Lot P01 : 50 tantièmes → 50 voix
        // (Martin possède ce lot ET le A01 → votes cumulés)
        var p01 = new DroitDeVote(
            new NumeroLot("P01"),
            new Voix(50),
            Votant.de("proprio-martin")
        );

        store.put("A01", a01);
        store.put("A02", a02);
        store.put("P01", p01);
    }

    @Override
    public Optional<DroitDeVote> parNumeroLot(String numeroLot) {
        return Optional.ofNullable(store.get(numeroLot));
    }

    @Override
    public List<DroitDeVote> tous() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void sauvegarder(DroitDeVote droitDeVote) {
        store.put(droitDeVote.getLot().value(), droitDeVote);
    }
}
